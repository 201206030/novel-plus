package com.java2nb.novel.service;


import io.github.xxyopen.model.page.PageBean;
import com.java2nb.novel.entity.*;
import com.java2nb.novel.vo.BookCommentVO;
import com.java2nb.novel.vo.BookSettingVO;
import com.java2nb.novel.vo.BookSpVO;
import com.java2nb.novel.vo.BookVO;

import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * @author 11797
 */
public interface BookService {

    /**
     * 查询首页小说设置列表数据
     * @return
     * */
    Map<Byte, List<BookSettingVO>> listBookSettingVO();

    /**
     * 查询首页点击榜单数据
     * @return
     * */
    List<Book> listClickRank();

    /**
     * 查询首页新书榜单数据
     * @return 小说列表
     * */
    List<Book> listNewRank();

    /**
     * 查询首页更新榜单数据
     * @return
     * */
    List<BookVO> listUpdateRank();

    /**
     * 分页搜索
     * @param params 搜索参数
     * @param page 页码
     * @param pageSize 分页大小
     * @return 小说集合分页信息
     * */
    PageBean<?> searchByPage(BookSpVO params, int page, int pageSize);

    /**
     * 查询小说分类列表
     * @return 分类集合
     * */
    List<BookCategory> listBookCategory();

    /**
     * 查询小说详情信息
     * @return 书籍信息
     * @param id 书籍ID*/
    Book queryBookDetail(Long id);

    /**
     * 查询目录列表
     * @param bookId 书籍ID
     * @param orderBy 排序
     * @param page 查询页码
     *@param pageSize 分页大小
     *@return 目录集合
     * */
    List<BookIndex> queryIndexList(Long bookId, String orderBy, Integer page, Integer pageSize);


    /**
     * 查询目录
     * @param bookIndexId 目录ID
     * @return 目录信息
     * */
    BookIndex queryBookIndex(Long bookIndexId);

    /**
     * 查询上一章节目录ID
     * @param bookId 书籍ID
     * @param indexNum 目录号
     * @return 上一章节目录ID，没有则返回0
     * */
    Long queryPreBookIndexId(Long bookId, Integer indexNum);

    /**
     * 查询下一章目录ID
     * @param bookId 书籍ID
     * @param indexNum 目录号
     * @return 下一章节目录ID，没有则返回0
     * */
    Long queryNextBookIndexId(Long bookId, Integer indexNum);

    /**
     * 查询章节内容
     * @param bookIndexId 目录ID
     * @return 书籍内容
     * */
    @Deprecated
    BookContent queryBookContent(Long bookIndexId);

    /**
     * 查询小说排行信息
     * @param type 排行类型，0点击排行，1新书排行，2更新排行
     * @param limit 查询条数
     * @return 小说集合
     * */
    List<Book> listRank(Byte type, Integer limit);

    /**
     * 增加点击次数
     * @param bookId 书籍ID
     * @param visitCount 点击量
     * */
    void addVisitCount(Long bookId, Integer visitCount);

    /**
     * 查询章节数
     * @param bookId 书籍ID
     * @return 章节数量
     * */
    long queryIndexCount(Long bookId);

    /**
     * 根据分类id查询同类推荐书籍
     * @param catId 分类id
     * @return 书籍集合
     * */
    List<Book> listRecBookByCatId(Integer catId);

    /**
     * 查询首章目录ID
     * @param bookId 书籍ID
     * @return 首章目录ID
     * */
    Long queryFirstBookIndexId(Long bookId);

    /**
     *分页查询书籍评论列表
     * @param userId 用户ID
     * @param bookId 书籍ID
     * @param page 页码
     * @param pageSize 分页大小
     * @return 评论分页数据
     * */
    PageBean<BookCommentVO> listCommentByPage(Long userId, Long bookId, int page, int pageSize);

    /**
     * 新增评价
     * @param userId 用户ID
     * @param comment 评论内容
     * */
    void addBookComment(Long userId, BookComment comment);

    /**
     * 通过作者名获取或创建作者Id
     * @param authorName 作者名
     * @param workDirection 作品方向
     * @return 作者ID
     * */
    @Deprecated
    Long getOrCreateAuthorIdByName(String authorName, Byte workDirection);



    /**
     * 查询小说ID
     * @param bookName 书名
     * @param author 作者名
     * @return 小说ID
     * */
    Long queryIdByNameAndAuthor(String bookName, String author);

    /**
     * 根据小说ID查询目录号集合
     * @param bookId 小说ID
     * @return 目录号集合
     * */
    @Deprecated
    List<Integer> queryIndexNumByBookId(Long bookId);

    /**
     * 查询网络图片的小说
     *
     * @param localPicPrefix
     * @param limit 查询条数
     * @return 返回小说集合
     * */
    List<Book> queryNetworkPicBooks(String localPicPrefix, Integer limit);


    /**
     * 更新爬取的小说网络图片到自己的存储介质（本地、OSS、fastDfs）
     * @param picUrl 爬取的网络图片路径
     * @param bookId 小说ID
     */
    void updateBookPicToLocal(String picUrl, Long bookId);

    /**
     * 通过作者ID查询小说分页列表
     * @param userId 用户ID
     * @param page 页码
     * @param pageSize 分页大小
     * @return 小说分页数据
     * */
    PageBean<Book> listBookPageByUserId(Long userId, int page, int pageSize);

    /**
     * 发布小说
     * @param book 小说信息
     * @param authorId 作家ID
     * @param penName 作家笔名
     * */
    void addBook(Book book, Long authorId, String penName);

    /**
     * 更新小说状态,上架或下架
     * @param bookId 小说ID
     * @param status 更新的状态
     * @param authorId 作者ID
     * */
    void updateBookStatus(Long bookId, Byte status, Long authorId);

    /**
     * 发布章节内容
     * @param bookId 小说ID
     * @param indexName 章节名
     * @param content 章节内容
     * @param isVip 是否收费
     * @param authorId 作者ID   */
    void addBookContent(Long bookId, String indexName, String content, Byte isVip, Long authorId);


    /**
     * 根据更新时间分页查询书籍列表
     * @param startDate 开始时间，包括该时间
     * @param limit 查询数量
     * @return 书籍列表
     * */
    List<Book> queryBookByUpdateTimeByPage(Date startDate, int limit);

    /**
     * 查询作品列表
     * @param authorId 作家ID
     * @return 作品列表
     */
    List<Book> queryBookList(Long authorId);

    /**
     * 删除章节
     * @param indexId
     * @param authorId 作家ID
     */
    void deleteIndex(Long indexId, Long authorId);

    /**
     * 更新章节名
     * @param indexId
     * @param indexName
     * @param authorId
     */
    void updateIndexName(Long indexId, String indexName, Long authorId);

    /**
     * 查询章节内容
     * @param indexId
     * @param authorId
     * @return
     */
    String queryIndexContent(Long indexId, Long authorId);

    /**
     *  更新章节内容
     * @param indexId
     * @param indexName
     * @param content
     * @param authorId
     */
    void updateBookContent( Long indexId, String indexName, String content, Long authorId);

    /**
     * 修改小说封面
     * @param bookId
     * @param bookPic
     * @param authorId
     */
    void updateBookPic(Long bookId, String bookPic, Long authorId);
}
