package com.java2nb.novel.service.impl;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.pagehelper.PageHelper;
import com.java2nb.novel.core.cache.CacheService;
import com.java2nb.novel.core.crawl.CrawlParser;
import com.java2nb.novel.core.crawl.RuleBean;
import com.java2nb.novel.core.enums.ResponseStatus;
import com.java2nb.novel.entity.Book;
import com.java2nb.novel.entity.CrawlSingleTask;
import com.java2nb.novel.entity.CrawlSource;
import com.java2nb.novel.mapper.CrawlSingleTaskDynamicSqlSupport;
import com.java2nb.novel.mapper.CrawlSingleTaskMapper;
import com.java2nb.novel.mapper.CrawlSourceDynamicSqlSupport;
import com.java2nb.novel.mapper.CrawlSourceMapper;
import com.java2nb.novel.service.BookService;
import com.java2nb.novel.service.CrawlService;
import com.java2nb.novel.utils.CrawlHttpClient;
import com.java2nb.novel.vo.CrawlSingleTaskVO;
import com.java2nb.novel.vo.CrawlSourceVO;
import io.github.xxyopen.model.page.PageBean;
import io.github.xxyopen.model.page.builder.pagehelper.PageBuilder;
import io.github.xxyopen.util.IdWorker;
import io.github.xxyopen.util.ThreadUtil;
import io.github.xxyopen.web.exception.BusinessException;
import io.github.xxyopen.web.util.BeanUtil;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.mybatis.dynamic.sql.render.RenderingStrategies;
import org.mybatis.dynamic.sql.select.render.SelectStatementProvider;
import org.springframework.stereotype.Service;

import java.time.Duration;
import java.util.*;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static com.java2nb.novel.mapper.CrawlSourceDynamicSqlSupport.*;
import static org.mybatis.dynamic.sql.SqlBuilder.isEqualTo;
import static org.mybatis.dynamic.sql.select.SelectDSL.select;

/**
 * @author Administrator
 */
@Service
@RequiredArgsConstructor
@Slf4j
public class CrawlServiceImpl implements CrawlService {

    private final CrawlParser crawlParser;

    private final CrawlSourceMapper crawlSourceMapper;

    private final CrawlSingleTaskMapper crawlSingleTaskMapper;

    private final BookService bookService;

    private final IdWorker idWorker = IdWorker.INSTANCE;

    private final CrawlHttpClient crawlHttpClient;

    private final Map<Integer, Byte> crawlSourceStatusMap = new HashMap<>();

    private final Map<Integer, Set<Long>> runningCrawlThread = new HashMap<>();


    @Override
    public void addCrawlSource(CrawlSource source) {
        Date currentDate = new Date();
        source.setCreateTime(currentDate);
        source.setUpdateTime(currentDate);
        crawlSourceMapper.insertSelective(source);

    }

    @Override
    public void updateCrawlSource(CrawlSource source) {
        if (source.getId() != null) {
            Optional<CrawlSource> opt = crawlSourceMapper.selectByPrimaryKey(source.getId());
            if (opt.isPresent()) {
                CrawlSource crawlSource = opt.get();
                if (crawlSource.getSourceStatus() == (byte) 1) {
                    //关闭
                    openOrCloseCrawl(crawlSource.getId(), (byte) 0);
                }
                Date currentDate = new Date();
                crawlSource.setUpdateTime(currentDate);
                crawlSource.setCrawlRule(source.getCrawlRule());
                crawlSource.setSourceName(source.getSourceName());
                crawlSourceMapper.updateByPrimaryKey(crawlSource);
            }
        }
    }

    @Override
    public PageBean<CrawlSource> listCrawlByPage(int page, int pageSize) {
        PageHelper.startPage(page, pageSize);
        SelectStatementProvider render = select(id, sourceName, sourceStatus, createTime, updateTime)
            .from(crawlSource)
            .orderBy(updateTime.descending())
            .build()
            .render(RenderingStrategies.MYBATIS3);
        List<CrawlSource> crawlSources = crawlSourceMapper.selectMany(render);
        crawlSources.forEach(crawlSource -> crawlSource.setSourceStatus(
            Optional.ofNullable(crawlSourceStatusMap.get(crawlSource.getId())).orElse((byte) 0)));
        PageBean<CrawlSource> pageBean = PageBuilder.build(crawlSources);
        pageBean.setList(BeanUtil.copyList(crawlSources, CrawlSourceVO.class));
        return pageBean;
    }

    @SneakyThrows
    @Override
    public void openOrCloseCrawl(Integer sourceId, Byte sourceStatus) {

        // 判断是开启还是关闭，如果是关闭，则获取该爬虫源正在运行的线程集合并全部中断
        // 如果是开启，先判断该爬虫源是否还在运行，如果在运行，则忽略，如果没有运行则启动线程爬取小说数据并加入到runningCrawlThread中
        // 最后，保存爬虫源状态
        if (sourceStatus == (byte) 0) {
            // 关闭
            // 将该爬虫源正在运行的线程集合全部停止
            Set<Long> runningCrawlThreadId = runningCrawlThread.get(sourceId);
            if (runningCrawlThreadId != null) {
                for (Long ThreadId : runningCrawlThreadId) {
                    Thread thread = ThreadUtil.findThread(ThreadId);
                    if (thread != null && thread.isAlive()) {
                        thread.interrupt();
                    }
                }
            }


        } else {
            // 开启
            Byte realSourceStatus = Optional.ofNullable(crawlSourceStatusMap.get(sourceId)).orElse((byte) 0);
            if (realSourceStatus == (byte) 0) {
                // 查询爬虫源规则
                CrawlSource source = queryCrawlSource(sourceId);
                //该爬虫源已经停止运行了,启动线程爬取小说数据并将线程加入到runningCrawlThread中
                RuleBean ruleBean = new ObjectMapper().readValue(source.getCrawlRule(), RuleBean.class);
                Set<Long> threadIds = new HashSet<>();
                //按分类开始爬虫解析任务
                for (int i = 1; i < 8; i++) {
                    final int catId = i;
                    Thread thread = new Thread(() -> CrawlServiceImpl.this.parseBookList(catId, ruleBean, sourceId));
                    thread.start();
                    //thread加入到监控缓存中
                    threadIds.add(thread.getId());
                }
                runningCrawlThread.put(sourceId, threadIds);
            }

        }

        // 保存爬虫源状态
        crawlSourceStatusMap.put(sourceId, sourceStatus);

    }

    @Override
    public CrawlSource queryCrawlSource(Integer sourceId) {
        SelectStatementProvider render = select(CrawlSourceDynamicSqlSupport.sourceStatus,
            CrawlSourceDynamicSqlSupport.crawlRule)
            .from(crawlSource)
            .where(id, isEqualTo(sourceId))
            .build()
            .render(RenderingStrategies.MYBATIS3);
        return crawlSourceMapper.selectMany(render).get(0);
    }

    @Override
    public void addCrawlSingleTask(CrawlSingleTask singleTask) {

        if (bookService.queryIsExistByBookNameAndAuthorName(singleTask.getBookName(), singleTask.getAuthorName())) {
            throw new BusinessException(ResponseStatus.BOOK_EXISTS);

        }
        singleTask.setCreateTime(new Date());
        crawlSingleTaskMapper.insertSelective(singleTask);


    }

    @Override
    public PageBean<CrawlSingleTask> listCrawlSingleTaskByPage(int page, int pageSize) {
        PageHelper.startPage(page, pageSize);
        SelectStatementProvider render = select(CrawlSingleTaskDynamicSqlSupport.crawlSingleTask.allColumns())
            .from(CrawlSingleTaskDynamicSqlSupport.crawlSingleTask)
            .orderBy(CrawlSingleTaskDynamicSqlSupport.createTime.descending())
            .build()
            .render(RenderingStrategies.MYBATIS3);
        List<CrawlSingleTask> crawlSingleTasks = crawlSingleTaskMapper.selectMany(render);
        PageBean<CrawlSingleTask> pageBean = PageBuilder.build(crawlSingleTasks);
        pageBean.setList(BeanUtil.copyList(crawlSingleTasks, CrawlSingleTaskVO.class));
        for (CrawlSingleTask crawlSingleTask : pageBean.getList()) {
            if (crawlSingleTask.getTaskStatus() == 2
                && crawlParser.getCrawlTaskProgress(crawlSingleTask.getId()) != null) {
                // 如果排队中的任务有任务进度，将排队中的任务状态修改成采集中并设置任务进度
                crawlSingleTask.setTaskStatus((byte) 3);
                crawlSingleTask.setCrawlChapters(crawlParser.getCrawlTaskProgress(crawlSingleTask.getId()));
                // 只会有一个任务在采集中
                break;
            }
        }
        return pageBean;
    }

    @Override
    public void delCrawlSingleTask(Long id) {
        crawlSingleTaskMapper.deleteByPrimaryKey(id);
    }

    @Override
    public CrawlSingleTask getCrawlSingleTask() {

        List<CrawlSingleTask> list = crawlSingleTaskMapper.selectMany(
            select(CrawlSingleTaskDynamicSqlSupport.crawlSingleTask.allColumns())
                .from(CrawlSingleTaskDynamicSqlSupport.crawlSingleTask)
                .where(CrawlSingleTaskDynamicSqlSupport.taskStatus, isEqualTo((byte) 2))
                .orderBy(CrawlSingleTaskDynamicSqlSupport.createTime)
                .limit(1)
                .build()
                .render(RenderingStrategies.MYBATIS3));

        return list.size() > 0 ? list.get(0) : null;
    }

    @Override
    public void updateCrawlSingleTask(CrawlSingleTask task, Byte status) {
        byte excCount = task.getExcCount();
        excCount += 1;
        task.setExcCount(excCount);
        if (status == 1 || excCount == 5) {
            // 当采集成功或者采集次数等于5，则更新采集最终状态，并停止采集
            task.setTaskStatus(status);
        }
        if (status == 1) {
            // 当采集成功，保存采集的章节数量
            task.setCrawlChapters(crawlParser.getCrawlTaskProgress(task.getId()));
        }
        crawlSingleTaskMapper.updateByPrimaryKeySelective(task);
        // 删除任务进度
        crawlParser.removeCrawlTaskProgress(task.getId());

    }

    @Override
    public CrawlSource getCrawlSource(Integer id) {
        return crawlSourceMapper.selectByPrimaryKey(id).orElse(null);
    }

    @Override
    public Integer getTaskProgress(Long taskId) {
        return Optional.ofNullable(crawlParser.getCrawlTaskProgress(taskId)).orElse(0);
    }

    /**
     * 解析分类列表
     */
    @Override
    public void parseBookList(int catId, RuleBean ruleBean, Integer sourceId) {

        String catIdRule = ruleBean.getCatIdRule().get("catId" + catId);
        if (StringUtils.isBlank(catIdRule)) {
            return;
        }

        //当前页码1
        int page = 1;
        int totalPage = page;

        while (page <= totalPage) {

            try {
                String catBookListUrl;
                if (StringUtils.isNotBlank(ruleBean.getBookListUrl())) {
                    // 兼容老规则
                    // 拼接分类URL
                    catBookListUrl = ruleBean.getBookListUrl()
                        .replace("{catId}", catIdRule)
                        .replace("{page}", page + "");
                } else {
                    // 新规则
                    // 拼接分类URL
                    catBookListUrl = catIdRule.replace("{page}", page + "");
                }
                log.info("catBookListUrl：{}", catBookListUrl);

                String bookListHtml = crawlHttpClient.get(catBookListUrl, ruleBean.getCharset());
                if (bookListHtml != null) {
                    Pattern bookIdPatten = Pattern.compile(ruleBean.getBookIdPatten());
                    Matcher bookIdMatcher = bookIdPatten.matcher(bookListHtml);
                    boolean isFindBookId = bookIdMatcher.find();
                    while (isFindBookId) {
                        try {
                            //1.阻塞过程（使用了 sleep,同步锁的 wait,socket 中的 receiver,accept 等方法时）
                            //捕获中断异常InterruptedException来退出线程。
                            //2.非阻塞过程中通过判断中断标志来退出线程。
                            if (Thread.currentThread().isInterrupted()) {
                                return;
                            }

                            String bookId = bookIdMatcher.group(1);
                            parseBookAndSave(catId, ruleBean, sourceId, bookId, null);
                        } catch (InterruptedException e) {
                            log.error(e.getMessage(), e);
                            //1.阻塞过程（使用了 sleep,同步锁的 wait,socket 中的 receiver,accept 等方法时）
                            //捕获中断异常InterruptedException来退出线程。
                            //2.非阻塞过程中通过判断中断标志来退出线程。
                            return;
                        } catch (Exception e) {
                            log.error(e.getMessage(), e);
                        }

                        isFindBookId = bookIdMatcher.find();
                    }

                    Pattern totalPagePatten = Pattern.compile(ruleBean.getTotalPagePatten());
                    Matcher totalPageMatcher = totalPagePatten.matcher(bookListHtml);
                    boolean isFindTotalPage = totalPageMatcher.find();
                    if (isFindTotalPage) {

                        totalPage = Integer.parseInt(totalPageMatcher.group(1));

                    }
                }
            } catch (InterruptedException e) {
                log.error(e.getMessage(), e);
                //1.阻塞过程（使用了 sleep,同步锁的 wait,socket 中的 receiver,accept 等方法时）
                //捕获中断异常InterruptedException来退出线程。
                //2.非阻塞过程中通过判断中断标志来退出线程。
                return;
            } catch (Exception e) {
                log.error(e.getMessage(), e);
            }
            if (page >= totalPage) {
                // 第一遍采集完成，翻到第一页，继续第二次采集，适用于分页数比较少的最近更新列表
                page = 1;
                try {
                    // 第一遍采集完成，休眠1分钟
                    Thread.sleep(Duration.ofMinutes(1));
                } catch (InterruptedException e) {
                    log.error(e.getMessage(), e);
                    //1.阻塞过程（使用了 sleep,同步锁的 wait,socket 中的 receiver,accept 等方法时）
                    //捕获中断异常InterruptedException来退出线程。
                    //2.非阻塞过程中通过判断中断标志来退出线程。
                    return;
                }
            } else {
                page += 1;
            }
        }


    }

    @Override
    public boolean parseBookAndSave(int catId, RuleBean ruleBean, Integer sourceId, String bookId, CrawlSingleTask task)
        throws InterruptedException {

        final AtomicBoolean parseResult = new AtomicBoolean(false);

        crawlParser.parseBook(ruleBean, bookId, book -> {
            if (book.getBookName() == null || book.getAuthorName() == null) {
                return;
            }
            //这里只做新书入库，查询是否存在这本书
            Book existBook = bookService.queryBookByBookNameAndAuthorName(book.getBookName(), book.getAuthorName());
            //如果该小说不存在，则可以解析入库，但是标记该小说正在入库，30分钟之后才允许再次入库
            if (existBook == null) {
                //没有该书，可以入库
                book.setCatId(catId);
                //根据分类ID查询分类
                book.setCatName(bookService.queryCatNameByCatId(catId));
                if (catId == 7) {
                    //女频
                    book.setWorkDirection((byte) 1);
                } else {
                    //男频
                    book.setWorkDirection((byte) 0);
                }
                book.setCrawlBookId(bookId);
                book.setCrawlSourceId(sourceId);
                book.setCrawlLastTime(new Date());
                book.setId(idWorker.nextId());
                //解析章节目录
                boolean parseIndexContentResult = crawlParser.parseBookIndexAndContent(bookId, book, ruleBean,
                    new HashMap<>(0), chapter -> {
                        bookService.saveBookAndIndexAndContent(book, chapter.getBookIndexList(),
                            chapter.getBookContentList());
                    }, task);
                parseResult.set(parseIndexContentResult);

            } else {
                //只更新书籍的爬虫相关字段
                bookService.updateCrawlProperties(existBook.getId(), sourceId, bookId);
                parseResult.set(true);
            }
        });

        return parseResult.get();

    }

    @Override
    public void updateCrawlSourceStatus(Integer sourceId, Byte sourceStatus) {
        CrawlSource source = new CrawlSource();
        source.setId(sourceId);
        source.setSourceStatus(sourceStatus);
        crawlSourceMapper.updateByPrimaryKeySelective(source);
    }

    @Override
    public List<CrawlSource> queryCrawlSourceByStatus(Byte sourceStatus) {
        SelectStatementProvider render = select(CrawlSourceDynamicSqlSupport.id,
            CrawlSourceDynamicSqlSupport.sourceStatus, CrawlSourceDynamicSqlSupport.crawlRule)
            .from(crawlSource)
            .where(CrawlSourceDynamicSqlSupport.sourceStatus, isEqualTo(sourceStatus))
            .build()
            .render(RenderingStrategies.MYBATIS3);
        return crawlSourceMapper.selectMany(render);
    }

}
