package com.java2nb.novel.core.booksource;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.java2nb.novel.core.booksource.BookSourceConvertResult.Status;
import com.java2nb.novel.core.crawl.CrawlParseType;
import com.java2nb.novel.core.crawl.RuleBean;
import com.java2nb.novel.core.utils.UrlUtil;
import org.apache.commons.lang3.StringUtils;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.EnumMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.HashSet;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * 阅读3.0（LEGUMI）书源 → novel-plus采集规则转换器
 * <p>
 * 转换策略：
 * <ul>
 * <li>阅读DSL选择器（class./id./tag.）转换为jsoup CSS选择器，XPath/JSONPath规则直通</li>
 * <li>阅读##替换后处理由 {@code RuleExtractor} 原生支持，规则透传</li>
 * <li>混合语法书源通过规则级解析方式前缀（xpath:/css:/json:）保留</li>
 * <li>@js/&lt;js&gt;/@@ 等动态规则无法静态转换：出现在必填规则时整源标记不可导入</li>
 * <li>详情页/目录页/内容页URL采用 {bookId}/{indexId} 模板 + urlPrefix 相对地址补全方案</li>
 * </ul>
 *
 * @author xiongxiaoyang
 */
public class BookSourceRuleConverter {

    /**
     * 阅读规则的取值方式关键字（非属性名）
     */
    private static final Set<String> VALUE_GETTERS = new HashSet<>(Arrays.asList(
        "text", "textNodes", "html", "innerHtml", "outerHtml", "ownText", "all"));

    /**
     * 阅读规则中按属性取值的常见属性名
     */
    private static final Set<String> ATTR_NAMES = new HashSet<>(Arrays.asList(
        "href", "src", "alt", "content", "title", "value", "onclick", "class", "id", "style"));

    /**
     * 常见HTML标签名，裸词段出现在选择器位置时按标签处理而不是属性
     */
    private static final Set<String> TAG_WORDS = new HashSet<>(Arrays.asList(
        "a", "img", "div", "li", "ul", "ol", "p", "span", "h1", "h2", "h3", "h4", "h5", "h6",
        "dl", "dt", "dd", "td", "tr", "th", "table", "tbody", "thead", "font", "strong", "em",
        "br", "body", "head", "meta", "input", "button", "section", "header", "footer", "nav",
        "article", "cite", "small", "b", "i", "u", "h"));

    private static final Pattern BARE_WORD = Pattern.compile("^[a-zA-Z][a-zA-Z0-9_-]*$");

    private static final Pattern DSL_SEGMENT = Pattern.compile("^(class|id|tag)\\.(.+)$");

    private static final Pattern CHILDREN_SEGMENT = Pattern.compile("^children(?:\\[(\\d+)])?$");

    private static final Pattern SLICE_TAIL = Pattern.compile("\\[\\d*:?\\d*]$");

    private static final Pattern RAW_INDEX_TAIL = Pattern.compile("\\.(\\d+)$");

    private static final Pattern XPATH_ATTR_TAIL = Pattern.compile("/?@([a-zA-Z_][a-zA-Z0-9:._-]*)$");

    private static final Pattern CSS_ATTR_TAIL = Pattern.compile("@([a-zA-Z_][a-zA-Z0-9_-]*)$");

    /**
     * 阅读按文本定位的“下一页”规则，如 text.下页@href、text:下一页@href
     */
    private static final Pattern NEXT_TEXT_RULE = Pattern.compile("^text[:\uff1a.]?(.+?)@(href|src)$");

    /**
     * 阅读URL中的page分页占位：原文可能已是带花括号的{{page}}/{page}/${page}形式，
     * 也可能是独立page占位词（前后不能是字母），如 /fenlei/1/page/、-page.html、_page_0；
     * 整体匹配后统一归一为单个{page}，避免对花括号内的page再次套壳导致占位符嵌套
     */
    private static final Pattern PAGE_PLACEHOLDER =
        Pattern.compile("\\{+\\s*page\\s*\\}+|\\$\\{page\\}|(?<![A-Za-z])page(?![A-Za-z])");

    /**
     * novel-plus单源固定支持的分类槽数（引擎按catId1..catId7并发采集）
     */
    private static final int MAX_CAT_COUNT = 7;

    private static final ObjectMapper MAPPER = new ObjectMapper();

    /**
     * 单条阅读书源的转换入口
     */
    public BookSourceConvertResult convert(BookSourceBean source) {
        BookSourceConvertResult result = new BookSourceConvertResult();
        result.setSourceName(source.getBookSourceName());
        result.setSourceUrl(source.getBookSourceUrl());
        Ctx ctx = new Ctx(result);

        if (StringUtils.isBlank(source.getBookSourceName())) {
            ctx.result.getErrors().add("缺少书源名称（bookSourceName）");
        }
        String origin = UrlUtil.origin(source.getBookSourceUrl());
        if (origin == null) {
            ctx.result.getErrors().add("书源地址（bookSourceUrl）缺失或不是http/https绝对地址");
        }
        if (!ctx.result.getErrors().isEmpty()) {
            result.setStatus(Status.FAILED);
            return result;
        }
        ctx.origin = origin;

        //发现分类不做默认顺序映射，条目暴露给导入用户在预览中手动映射分类槽
        List<String[]> exploreEntries = parseExploreEntries(source, ctx);
        for (String[] entry : exploreEntries) {
            ctx.result.getExploreOptions().add(new BookSourceConvertResult.ExploreOption(entry[0], entry[1]));
        }

        Map<String, String> bookInfo = nullSafe(source.getRuleBookInfo());
        Map<String, String> toc = nullSafe(source.getRuleToc());
        Map<String, String> content = nullSafe(source.getRuleContent());

        //详情页规则
        ConvertedRule nameRule = parseYdRule(bookInfo.get("name"), "详情页书名", ctx);
        ConvertedRule authorRule = parseYdRule(bookInfo.get("author"), "详情页作者", ctx);
        ConvertedRule coverRule = parseYdRule(bookInfo.get("coverUrl"), "详情页封面", ctx);
        ConvertedRule introRule = parseYdRule(bookInfo.get("intro"), "详情页简介", ctx);
        if (StringUtils.isNotBlank(bookInfo.get("tocUrl"))) {
            ctx.warn("详情页指定了自定义目录页（tocUrl），未自动导入，默认以详情页作为目录页，可在导入后手工调整");
        }

        //目录规则：chapterList与chapterName/chapterUrl组合
        ConvertedRule tocList = parseYdRule(toc.get("chapterList"), "目录列表", ctx);
        ConvertedRule indexNameRule = combine(tocList, toc.get("chapterName"), "text", "目录章节名", ctx);
        ConvertedRule indexUrlRule = combine(tocList, toc.get("chapterUrl"), "attr(href)", "目录章节链接", ctx);
        ConvertedRule nextTocRule = parseNextPageRule(toc.get("nextTocUrl"), "目录下一页", ctx);
        if (nextTocRule == null && StringUtils.isNotBlank(toc.get("nextTocUrl"))) {
            ctx.warn("目录下一页规则（nextTocUrl）无法静态转换，目录只采集第一页");
        }

        //正文规则
        ConvertedRule contentRule = parseYdRule(content.get("content"), "正文内容", ctx);
        mergeReplaceRegex(contentRule, content.get("replaceRegex"), ctx);
        ConvertedRule nextContentRule = parseNextPageRule(content.get("nextContentUrl"), "正文下一页", ctx);
        if (nextContentRule == null && StringUtils.isNotBlank(content.get("nextContentUrl"))) {
            ctx.warn("正文下一页规则（nextContentUrl）无法静态转换，章末分页内容可能缺失");
        }

        //分类列表取书链接：优先发现页规则，其次搜索页规则
        Map<String, String> listRule = chooseListRule(source, ctx);
        ConvertedRule bookListRule = parseYdRule(listRule.get("bookList"), "列表书目容器", ctx);
        ConvertedRule bookUrlRule = bookListRule == null ? null : combine(bookListRule, listRule.get("bookUrl"),
            "attr(href)", "列表书链接", ctx);
        if (bookUrlRule == null) {
            bookUrlRule = parseYdRule(listRule.get("bookUrl"), "列表书链接", ctx);
        }

        //解析方式按已转换规则中出现最多的语法族投票，平票取CSS
        CrawlParseType parseType = voteParseType(nameRule, authorRule, tocList, contentRule, bookUrlRule);

        //必填规则校验
        requireRule(nameRule, bookInfo, "name", "详情页书名", ctx);
        requireRule(authorRule, bookInfo, "author", "详情页作者", ctx);
        requireRule(indexNameRule, toc, "chapterList", "目录章节名", ctx);
        requireRule(indexUrlRule, toc, "chapterUrl", "目录章节链接", ctx);
        requireRule(contentRule, content, "content", "正文内容", ctx);

        RuleBean bean = null;
        if (ctx.result.getErrors().isEmpty()) {
            bean = new RuleBean();
            bean.setParseType(parseType.name());
            bean.setUrlPrefix(origin);
            //书id即详情页完整/相对地址，配合urlPrefix补全
            bean.setBookDetailUrl("{bookId}");
            bean.setBookIndexUrl("{bookId}");
            bean.setBookContentUrl("{indexId}");
            bean.setBookNamePatten(render(nameRule, parseType));
            bean.setAuthorNamePatten(render(authorRule, parseType));
            bean.setPicUrlPatten(render(coverRule, parseType));
            bean.setDescPatten(render(introRule, parseType));
            bean.setIndexNamePatten(render(indexNameRule, parseType));
            bean.setIndexIdPatten(render(indexUrlRule, parseType));
            bean.setNextIndexUrlPatten(render(nextTocRule, parseType));
            bean.setContentPatten(render(contentRule, parseType));
            bean.setNextContentUrlPatten(render(nextContentRule, parseType));
            bean.setBookIdPatten(render(bookUrlRule, parseType));
            applyExplore(exploreEntries, source, bookUrlRule != null, ctx);
            applyCharset(source, bean, ctx);
            applyIgnoredOptions(source, ctx);
        }

        if (!ctx.result.getErrors().isEmpty()) {
            result.setStatus(Status.FAILED);
        } else if (!ctx.result.getWarnings().isEmpty()) {
            result.setStatus(Status.PARTIAL);
        } else {
            result.setStatus(Status.OK);
        }
        result.setRuleBean(bean);
        return result;
    }

    /**
     * 解析“下一页”类规则：阅读按文本定位的 text.xxx@href 近似转换为包含该文本的链接，
     * 其余走通用规则转换；无法转换返回null由调用方降级警告
     */
    private ConvertedRule parseNextPageRule(String raw, String field, Ctx ctx) {
        if (StringUtils.isBlank(raw)) {
            return null;
        }
        Matcher m = NEXT_TEXT_RULE.matcher(raw.trim());
        if (m.matches()) {
            ctx.warn(field + "：阅读按文本定位已近似转换为包含\"" + m.group(1) + "\"的链接，请核对采集效果");
            return new ConvertedRule(CrawlParseType.CSS, "a:containsOwn(" + m.group(1) + ")",
                "attr(" + m.group(2) + ")", null);
        }
        return parseYdRule(raw, field, ctx);
    }

    /**
     * 转换单条阅读规则，无法静态转换返回null并记录原因
     */
    private ConvertedRule parseYdRule(String raw, String field, Ctx ctx) {
        if (StringUtils.isBlank(raw)) {
            return null;
        }
        String yd = raw.trim();
        if (yd.startsWith("@js") || yd.contains("<js>") || yd.contains("@@")
            || yd.contains("@put") || yd.contains("@get")) {
            ctx.skip(field, "包含JS脚本或阅读高级语法（@js/<js>/@@），无法静态转换");
            return null;
        }
        if (yd.contains("@js")) {
            //形如"基础规则@js:后处理"，保留JS后处理之前的基础规则
            yd = StringUtils.substringBefore(yd, "@js").trim();
            ctx.warn(field + "：去掉了规则的JS后处理部分，提取结果可能需要人工清洗");
        }
        if (yd.contains("\n")) {
            ctx.skip(field, "包含多行组合规则，无法转换");
            return null;
        }
        String tail = null;
        int hashIndex = yd.indexOf("##");
        if (hashIndex >= 0) {
            tail = yd.substring(hashIndex);
            yd = yd.substring(0, hashIndex).trim();
        }
        if (yd.contains("&&")) {
            yd = StringUtils.substringBefore(yd, "&&").trim();
            ctx.warn(field + "：&&组合规则只导入了第一条");
        }
        if (yd.contains("||")) {
            yd = StringUtils.substringBefore(yd, "||").trim();
            ctx.warn(field + "：||兜底规则只导入了第一条");
        }
        if (yd.isEmpty()) {
            ctx.skip(field, "规则内容为空");
            return null;
        }
        //JSONPath规则直通
        if (yd.startsWith("$")) {
            return new ConvertedRule(CrawlParseType.JSONPATH, yd, null, tail);
        }
        //XPath规则：去掉结尾的@attr并转成::attr()取值
        if (yd.startsWith("//") || yd.startsWith("/*") || yd.startsWith("xpath:") || yd.startsWith("@xpath:")) {
            String x = StringUtils.removeStart(StringUtils.removeStart(yd, "@xpath:"), "xpath:");
            String hint = null;
            Matcher m = XPATH_ATTR_TAIL.matcher(x);
            if (m.find()) {
                x = x.substring(0, m.start());
                hint = "attr(" + m.group(1) + ")";
            }
            return new ConvertedRule(CrawlParseType.XPATH, x, hint, tail);
        }
        //显式@css:规则：去掉结尾@attr并转成::attr()取值
        if (yd.startsWith("@css:") || yd.startsWith("css:")) {
            String c = StringUtils.removeStart(StringUtils.removeStart(yd, "@css:"), "css:").trim();
            String hint = null;
            Matcher m = CSS_ATTR_TAIL.matcher(c);
            if (m.find()) {
                String getter = mapGetter(m.group(1), ctx, field);
                c = c.substring(0, m.start());
                hint = getter != null ? getter : "attr(" + m.group(1) + ")";
            }
            return new ConvertedRule(CrawlParseType.CSS, c, hint, tail);
        }
        //阅读DSL或裸CSS：按@拆段
        String[] segs = yd.split("@");
        List<String> parts = new ArrayList<>();
        String hint = null;
        for (int i = 0; i < segs.length; i++) {
            String seg = segs[i].trim();
            if (seg.isEmpty()) {
                continue;
            }
            boolean last = i == segs.length - 1;
            String getter = BARE_WORD.matcher(seg).matches() ? mapGetter(seg, ctx, field) : null;
            if (last && getter != null && !parts.isEmpty()) {
                hint = getter;
                continue;
            }
            String piece = mapSegment(seg, ctx, field);
            if (piece != null) {
                parts.add(piece);
                continue;
            }
            ctx.skip(field, "无法识别的规则片段：" + seg);
            return null;
        }
        if (parts.isEmpty()) {
            ctx.skip(field, "没有识别出有效的选择器");
            return null;
        }
        return new ConvertedRule(CrawlParseType.CSS, String.join(" ", parts), hint, tail);
    }

    /**
     * 映射阅读DSL片段为CSS选择器片段，非class./id./tag.且含CSS选择器特征的按原样透传
     */
    private String mapSegment(String seg, Ctx ctx, String field) {
        //阅读按文本定位的text.xxx片段无法映射为CSS选择器（顶层形态由parseNextPageRule单独处理）
        if (seg.startsWith("text.") || seg.startsWith("text:") || seg.startsWith("text：")) {
            return null;
        }
        Matcher children = CHILDREN_SEGMENT.matcher(seg);
        if (children.matches()) {
            ctx.warn(field + "：@children子节点定位按首个子元素近似转换");
            String idx = children.group(1);
            return idx == null || "0".equals(idx) ? "> *:first-child" : "> *:nth-child(" + (Integer.parseInt(idx) + 1) + ")";
        }
        Matcher dsl = DSL_SEGMENT.matcher(seg);
        if (dsl.matches()) {
            String kind = dsl.group(1);
            String[] tokens = dsl.group(2).split("\\.");
            String name = tokens[0];
            if (SLICE_TAIL.matcher(name).find()) {
                name = SLICE_TAIL.matcher(name).replaceAll("");
                ctx.warn(field + "：[start:end]范围取值不支持，近似取全部匹配的第一条");
            }
            String base = "class".equals(kind) ? "." + name : "id".equals(kind) ? "#" + name : name;
            if (tokens.length > 1) {
                String idxToken = tokens[1];
                if (idxToken.matches("\\d+")) {
                    base += ":eq(" + idxToken + ")";
                } else if (idxToken.matches("-\\d+")) {
                    ctx.warn(field + "：负索引（取最后一条）不支持，近似取全部匹配的第一条");
                } else {
                    ctx.warn(field + "：索引写法 " + idxToken + " 不支持，已忽略");
                }
            }
            return StringUtils.isBlank(name) ? null : base;
        }
        //裸单词且不是取值关键字/属性名，按标签名处理（如 a@href 的 a）
        if (BARE_WORD.matcher(seg).matches() && mapGetter(seg, ctx, field) == null && !VALUE_GETTERS.contains(seg)) {
            return seg;
        }
        //裸CSS选择器透传（需含. # [ : > 等选择器特征），并处理 ".0" 尾缀索引
        if (seg.matches(".*[.#\\[:>].*")) {
            Matcher indexTail = RAW_INDEX_TAIL.matcher(seg);
            if (indexTail.find()) {
                String head = seg.substring(0, indexTail.start());
                return head + ":eq(" + indexTail.group(1) + ")";
            }
            return seg;
        }
        return null;
    }

    /**
     * 阅读规则的取值段映射为取值方式，非取值关键字返回null
     */
    private String mapGetter(String seg, Ctx ctx, String field) {
        if (TAG_WORDS.contains(seg.toLowerCase())) {
            return null;
        }
        switch (seg) {
            case "text":
                return "text";
            case "textNodes":
                ctx.warn(field + "：textNodes按文本近似取值");
                return "text";
            case "html":
            case "innerHtml":
                return "html";
            case "outerHtml":
            case "all":
                ctx.warn(field + "：" + seg + "按内部HTML近似取值");
                return "html";
            case "ownText":
                return "ownText";
            default:
                if (ATTR_NAMES.contains(seg) || seg.startsWith("data-")) {
                    return "attr(" + seg + ")";
                }
                return null;
        }
    }

    /**
     * 组合列表选择器与子规则，如 chapterList+chapterName/chapterUrl、bookList+bookUrl
     */
    private ConvertedRule combine(ConvertedRule parent, String subRaw, String defaultHint, String field, Ctx ctx) {
        if (parent == null) {
            if (StringUtils.isNotBlank(subRaw)) {
                ctx.skip(field, "缺少列表容器规则，无法定位子规则");
            }
            return null;
        }
        ConvertedRule out = new ConvertedRule(parent.family, parent.selector, defaultHint, parent.tail);
        if (StringUtils.isBlank(subRaw)) {
            return out;
        }
        if (parent.family == CrawlParseType.JSONPATH) {
            ctx.skip(field, "JSONPath列表规则的子规则无法组合转换");
            return null;
        }
        String sub = subRaw.trim();
        String subTail = null;
        int hashIndex = sub.indexOf("##");
        if (hashIndex >= 0) {
            subTail = sub.substring(hashIndex);
            sub = sub.substring(0, hashIndex).trim();
        }
        if (subTail != null && subTail.contains("{")) {
            //形如 href##$##,{'webView':true} 的请求选项附加，无法映射
            ctx.warn(field + "：链接附加的请求选项（webView等）未导入");
            subTail = null;
        }
        String getter = BARE_WORD.matcher(sub).matches() ? mapGetter(sub, ctx, field) : null;
        if (getter != null) {
            out.hint = getter;
            out.tail = appendTail(out.tail, subTail, field, ctx);
            return out;
        }
        ConvertedRule child = parseYdRule(sub, field + "子规则", ctx);
        if (child == null) {
            return null;
        }
        if (child.family != parent.family) {
            ctx.skip(field, "子规则语法与列表选择器不一致，无法组合");
            return null;
        }
        if (parent.family == CrawlParseType.CSS) {
            out.selector = parent.selector + " " + child.selector;
        } else {
            out.selector = parent.selector + "/" + StringUtils.prependIfMissing(child.selector, ".");
        }
        out.hint = child.hint != null ? child.hint : defaultHint;
        out.tail = appendTail(out.tail, child.tail, field, ctx);
        return out;
    }

    /**
     * 合并##后处理段，已有尾缀时只保留先到的那段
     */
    private String appendTail(String current, String extra, String field, Ctx ctx) {
        if (StringUtils.isEmpty(extra)) {
            return current;
        }
        if (current == null) {
            return extra;
        }
        ctx.warn(field + "：多条##过滤规则只保留了第一条：" + current);
        return current;
    }

    /**
     * 合并正文过滤规则replaceRegex到正文规则的##后处理
     */
    private void mergeReplaceRegex(ConvertedRule contentRule, String replaceRegex, Ctx ctx) {
        if (contentRule == null || StringUtils.isBlank(replaceRegex)) {
            return;
        }
        String extra = replaceRegex.trim();
        if (!extra.startsWith("##")) {
            extra = "##" + extra;
        }
        if (contentRule.tail == null) {
            contentRule.tail = extra;
            return;
        }
        //两者都有##段：仅当都是纯删除语义时用选择字符合并
        String[] originParts = splitTail(contentRule.tail);
        String[] extraParts = splitTail(extra);
        if (originParts != null && extraParts != null && StringUtils.isEmpty(extraParts[1]) && StringUtils.isEmpty(originParts[1])) {
            contentRule.tail = "##(?:" + originParts[0] + ")|(?:" + extraParts[0] + ")";
        } else {
            ctx.warn("正文规则自带##过滤与replaceRegex同时存在，只保留了正文规则的过滤");
        }
    }

    /**
     * 拆分"##正则##替换"为[正则,替换]，替换缺省空串；无##返回null
     */
    private String[] splitTail(String tail) {
        if (tail == null || !tail.startsWith("##")) {
            return null;
        }
        String body = tail.substring(2);
        int index = body.indexOf("##");
        return index < 0 ? new String[]{body, ""} : new String[]{body.substring(0, index), body.substring(index + 2)};
    }

    /**
     * 列表书链接规则来源：优先发现页（enabledExplore且有ruleExplore），否则搜索页
     */
    private Map<String, String> chooseListRule(BookSourceBean source, Ctx ctx) {
        Map<String, String> explore = nullSafe(source.getRuleExplore());
        boolean exploreUsable = Boolean.FALSE != source.getEnabled()
            && StringUtils.isNotBlank(source.getExploreUrl())
            && StringUtils.isNotBlank(explore.get("bookUrl"));
        if (exploreUsable) {
            return explore;
        }
        Map<String, String> search = nullSafe(source.getRuleSearch());
        if (StringUtils.isNotBlank(search.get("bookUrl"))) {
            if (StringUtils.isNotBlank(source.getExploreUrl())) {
                ctx.warn("发现页规则（ruleExplore）缺失或不生效，列表书链接改用搜索页规则近似，分类采集可能不匹配");
            }
            return search;
        }
        return Collections.emptyMap();
    }

    /**
     * 解析阅读exploreUrl为[标题, 绝对URL({page}占位)]条目，兼容JSON数组与“分类名::路径”按行两种格式；
     * 未配置或JS动态生成/解析失败时返回空列表
     */
    private List<String[]> parseExploreEntries(BookSourceBean source, Ctx ctx) {
        String exploreUrl = source.getExploreUrl();
        if (StringUtils.isBlank(exploreUrl)) {
            return Collections.emptyList();
        }
        List<String[]> entries = new ArrayList<>();
        try {
            String trimmed = exploreUrl.trim();
            if (trimmed.startsWith("[") || trimmed.startsWith("{")) {
                //JSON数组格式 [{"title":"xx","url":"/yy"},...]
                JsonNode array = MAPPER.readTree(trimmed);
                for (JsonNode node : array) {
                    String title = node.path("title").asText("");
                    String url = node.path("url").asText("");
                    if (StringUtils.isNotBlank(url)) {
                        entries.add(new String[]{title, url});
                    }
                }
            } else if (trimmed.startsWith("@js") || trimmed.contains("java.")) {
                return Collections.emptyList();
            } else {
                //普通"分类名::路径"按行格式
                for (String line : trimmed.split("\n")) {
                    if (!line.contains("::")) {
                        continue;
                    }
                    String title = StringUtils.substringBefore(line, "::").trim();
                    String url = StringUtils.substringAfter(line, "::").trim();
                    if (StringUtils.isNotBlank(url)) {
                        entries.add(new String[]{title, url});
                    }
                }
            }
        } catch (Exception e) {
            ctx.warn("发现分类（exploreUrl）解析失败：" + e.getMessage() + "，未导入分类采集");
            return Collections.emptyList();
        }
        //统一补全为绝对地址并把page分页占位（含{{page}}/${page}等变体）归一为{page}
        List<String[]> out = new ArrayList<>(entries.size());
        int noPageFlag = 0;
        for (String[] entry : entries) {
            String url = UrlUtil.absolutize(ctx.origin, entry[1]);
            Matcher matcher = PAGE_PLACEHOLDER.matcher(url);
            if (matcher.find()) {
                url = matcher.replaceAll("{page}");
            } else {
                noPageFlag++;
            }
            out.add(new String[]{entry[0], url});
        }
        if (noPageFlag > 0) {
            ctx.warn("有" + noPageFlag + "个分类URL没有page分页占位，分类采集只采第一页");
        }
        return out;
    }

    /**
     * 发现分类只做可用性提示，不生成默认catIdRule：分类映射由导入用户在预览中逐个选择，
     * 未映射的分类槽不采集（规则里catIdRule保持空Map，确认导入时由前端按用户选择回写）
     */
    private void applyExplore(List<String[]> entries, BookSourceBean source, boolean hasBookIdRule, Ctx ctx) {
        if (entries.isEmpty()) {
            ctx.warn(StringUtils.isBlank(source.getExploreUrl())
                ? "书源没有配置发现分类（exploreUrl），不支持分类批量采集，仅支持按详情页地址单本采集"
                : "发现分类（exploreUrl）由JS动态生成或无可用条目，未能导入，仅支持按详情页地址单本采集");
            return;
        }
        if (!hasBookIdRule) {
            //列表规则不可转换时映射无意义，不暴露映射条目
            ctx.result.getExploreOptions().clear();
            ctx.warn("发现分类不可用：列表书链接规则无法转换，仅支持按详情页地址单本采集");
            return;
        }
        ctx.warn("发现分类共" + entries.size() + "个，请在预览中逐个选择对应的本系统分类（玄幻奇幻/武侠仙侠/都市言情/历史军事/科幻灵异/网游竞技/女生频道），未映射的分类不会批量采集");
        if (entries.size() > MAX_CAT_COUNT) {
            ctx.warn("发现分类超过" + MAX_CAT_COUNT + "个，本系统只有" + MAX_CAT_COUNT + "个分类槽，至多映射" + MAX_CAT_COUNT + "个分类");
        }
    }

    /**
     * 从searchUrl中探测站点编码（如charset:gbk）
     */
    private void applyCharset(BookSourceBean source, RuleBean bean, Ctx ctx) {
        String searchUrl = StringUtils.lowerCase(StringUtils.defaultString(source.getSearchUrl()));
        if (searchUrl.contains("gbk") || searchUrl.contains("gb2312")) {
            bean.setCharset(searchUrl.contains("gb2312") ? "gb2312" : "gbk");
            ctx.warn("从搜索规则探测到站点编码为" + bean.getCharset() + "，已应用到全部页面，如有偏差请导入后手工改回utf-8");
        }
    }

    /**
     * 记录未导入的登录/请求头等配置
     */
    private void applyIgnoredOptions(BookSourceBean source, Ctx ctx) {
        List<String> ignored = new ArrayList<>();
        if (StringUtils.isNotBlank(source.getHeader())) {
            ignored.add("自定义请求头header");
        }
        if (StringUtils.isNotBlank(source.getLoginUrl())) {
            ignored.add("登录配置loginUrl");
        }
        if (Boolean.TRUE.equals(source.getEnabledCookieJar())) {
            ignored.add("Cookie启用enabledCookieJar");
        }
        if (!ignored.isEmpty()) {
            ctx.warn("未导入的配置：" + String.join("、", ignored) + "，站点若有反爬可能导致采集失败");
        }
        if (Boolean.FALSE.equals(source.getEnabled())) {
            ctx.warn("该书源在阅读App中已被标记停用，可能存在可用性问题");
        }
    }

    /**
     * 必填规则缺失时记录不可导入原因
     */
    private void requireRule(ConvertedRule rule, Map<String, String> sourceMap, String key, String name, Ctx ctx) {
        if (rule != null) {
            return;
        }
        String reason = ctx.skippedReasons.get(name);
        if (reason != null) {
            ctx.result.getErrors().add(name + "无法转换：" + reason);
        } else if (StringUtils.isBlank(sourceMap.get(key))) {
            ctx.result.getErrors().add(name + "规则（ruleBookInfo/ruleToc/ruleContent." + key + "）缺失");
        } else {
            ctx.result.getErrors().add(name + "规则无法转换");
        }
    }

    /**
     * 按规则语法族投票整源parseType
     */
    private CrawlParseType voteParseType(ConvertedRule... rules) {
        Map<CrawlParseType, Integer> counter = new EnumMap<>(CrawlParseType.class);
        for (ConvertedRule rule : rules) {
            if (rule != null) {
                counter.merge(rule.family, 1, Integer::sum);
            }
        }
        CrawlParseType best = CrawlParseType.CSS;
        int max = 0;
        for (Map.Entry<CrawlParseType, Integer> entry : counter.entrySet()) {
            if (entry.getValue() > max) {
                max = entry.getValue();
                best = entry.getKey();
            }
        }
        return best;
    }

    /**
     * 渲染为novel-plus规则串：取值方式后缀 + ##后处理透传 + 与整源parseType不一致时的语法前缀
     */
    private String render(ConvertedRule rule, CrawlParseType parseType) {
        if (rule == null) {
            return null;
        }
        StringBuilder sb = new StringBuilder();
        if (rule.family != parseType) {
            //引擎识别的语法前缀：json而非jsonpath
            String prefix = rule.family == CrawlParseType.JSONPATH ? "json" : StringUtils.lowerCase(rule.family.name());
            sb.append(prefix).append(':');
        }
        sb.append(rule.selector);
        if (rule.hint != null && rule.family != CrawlParseType.JSONPATH) {
            sb.append("::").append(rule.hint);
        }
        if (rule.tail != null) {
            sb.append(rule.tail);
        }
        return sb.toString();
    }

    private Map<String, String> nullSafe(Map<String, String> map) {
        return map == null ? Collections.emptyMap() : map;
    }

    /**
     * 转换中的单条规则中间结果
     */
    private static class ConvertedRule {
        /**
         * 规则语法族
         */
        private final CrawlParseType family;
        /**
         * 选择器主体
         */
        private String selector;
        /**
         * 取值方式（text/html/ownText/attr(x)），可为null
         */
        private String hint;
        /**
         * 阅读##后处理段原样保留
         */
        private String tail;

        private ConvertedRule(CrawlParseType family, String selector, String hint, String tail) {
            this.family = family;
            this.selector = selector;
            this.hint = hint;
            this.tail = tail;
        }
    }

    /**
     * 转换上下文
     */
    private static class Ctx {
        private final BookSourceConvertResult result;
        /**
         * 字段名 -> 无法转换原因
         */
        private final Map<String, String> skippedReasons = new LinkedHashMap<>();
        private String origin;

        private Ctx(BookSourceConvertResult result) {
            this.result = result;
        }

        private void warn(String message) {
            if (!result.getWarnings().contains(message)) {
                result.getWarnings().add(message);
            }
        }

        private void skip(String field, String reason) {
            skippedReasons.putIfAbsent(field, reason);
        }
    }

}
