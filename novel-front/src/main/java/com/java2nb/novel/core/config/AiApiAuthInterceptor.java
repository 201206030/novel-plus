package com.java2nb.novel.core.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.java2nb.novel.core.ai.AiApiRateLimiter;
import com.java2nb.novel.core.ai.AiRateLimitProperties;
import com.java2nb.novel.core.bean.UserDetails;
import com.java2nb.novel.core.enums.ResponseStatus;
import com.java2nb.novel.core.utils.CookieUtil;
import com.java2nb.novel.core.utils.JwtTokenUtil;
import com.java2nb.novel.entity.Author;
import com.java2nb.novel.service.AuthorService;
import io.github.xxyopen.model.resp.RestResult;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

import java.io.IOException;
import java.nio.charset.StandardCharsets;

/**
 * 作者后台 AI 接口的统一防护：登录态 → 作家身份 → 参数上限 → 分级限流。
 *
 * <p>覆盖 {@code /author/ai/**}（写作/流式/看板解读）、{@code /author/aiAssistant/**}
 * （长篇创作助手）以及 {@code /author/queryAiGenPic}。放在拦截器里可以保证以后新增的
 * AI 接口默认受保护，也避免 SSE 接口在 Controller 抛异常时出现内容协商失败（406）。</p>
 *
 * <p>限流分三级：{@code startTask} / {@code saveChapter} 会触发模型任务，走共享额度窗口；
 * 其余 AI 助手接口（含 2.5 秒一次的任务轮询）走独立的轻量频率窗口，不会吃掉创作额度、
 * 也不会把轮询限死；{@code queryAiGenPic} 只读缓存且持续轮询，仅鉴权。</p>
 *
 * <p>错误响应与 {@code CommonExceptionHandler} 保持一致：HTTP 200 + JSON 业务码，
 * 前端 ajax / EventSource 都能按既有方式处理。</p>
 *
 * @author xiongxiaoyang
 */
@Slf4j
@Component
@RequiredArgsConstructor
public class AiApiAuthInterceptor implements HandlerInterceptor {

    /** 供 Controller 复用的当前用户 ID / 作者 ID */
    public static final String ATTR_USER_ID = AiApiAuthInterceptor.class.getName() + ".userId";
    public static final String ATTR_AUTHOR_ID = AiApiAuthInterceptor.class.getName() + ".authorId";

    private static final String PIC_QUERY_URI = "/author/queryAiGenPic";
    private static final String AI_ASSISTANT_PREFIX = "/author/aiAssistant/";
    private static final String START_TASK_URI = AI_ASSISTANT_PREFIX + "startTask";
    private static final String SAVE_CHAPTER_URI = AI_ASSISTANT_PREFIX + "saveChapter";
    private static final String TOKEN_COOKIE = "Authorization";

    /** AI 助手中可能携带大段正文的参数，先挡住超大请求体再进 Service */
    private static final String[] LONG_TEXT_PARAMS = {
        "content", "planContent", "creativeParams", "prefContent", "chapterOutline"
    };

    private final JwtTokenUtil jwtTokenUtil;
    private final AuthorService authorService;
    private final AiApiRateLimiter rateLimiter;
    private final AiRateLimitProperties properties;
    private final ObjectMapper objectMapper;

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler)
        throws IOException {
        // CORS 预检不携带凭据，直接放行，避免破坏跨域调用
        if ("OPTIONS".equalsIgnoreCase(request.getMethod())) {
            return true;
        }

        UserDetails userDetails = resolveUser(request);
        if (userDetails == null || userDetails.getId() == null) {
            writeError(response, ResponseStatus.NO_LOGIN);
            return false;
        }

        // queryAuthorAll 在用户不是作家时返回 null，不会像 queryAuthor 那样抛越界异常
        Author author = authorService.queryAuthorAll(userDetails.getId());
        if (author == null || author.getId() == null) {
            writeError(response, ResponseStatus.AI_AUTHOR_ONLY);
            return false;
        }
        if (author.getStatus() != null && author.getStatus() == 1) {
            writeError(response, ResponseStatus.AUTHOR_STATUS_FORBIDDEN);
            return false;
        }

        request.setAttribute(ATTR_USER_ID, userDetails.getId());
        request.setAttribute(ATTR_AUTHOR_ID, author.getId());

        String uri = requestPath(request);
        ResponseStatus invalid = validateParams(request, uri);
        if (invalid != null) {
            writeError(response, invalid);
            return false;
        }

        // 只读封面生成结果，前端每 3 秒轮询一次，不占用任何限流配额
        if (PIC_QUERY_URI.equals(uri)) {
            return true;
        }

        // AI 助手中只有提交任务、保存章节会触发模型调用；其余 CRUD 与任务轮询走轻量窗口
        if (uri.startsWith(AI_ASSISTANT_PREFIX)
            && !START_TASK_URI.equals(uri) && !SAVE_CHAPTER_URI.equals(uri)) {
            if (!rateLimiter.tryAcquireLight(userDetails.getId())) {
                writeError(response, ResponseStatus.AI_RATE_LIMIT);
                return false;
            }
            return true;
        }

        if (!rateLimiter.tryAcquire(userDetails.getId(), calcCost(request, uri))) {
            writeError(response, ResponseStatus.AI_RATE_LIMIT);
            return false;
        }
        return true;
    }

    /**
     * 规范化请求路径：去掉矩阵参数（如 {@code ;jsessionid=xxx}）和末尾斜杠，
     * 防止攻击者用路径变体绕过 URI 精确匹配（例如把 {@code /startTask/} 伪装成轻量接口）。
     */
    private String requestPath(HttpServletRequest request) {
        String uri = request.getRequestURI();
        if (uri == null) {
            return "";
        }
        int semicolon = uri.indexOf(';');
        if (semicolon >= 0) {
            uri = uri.substring(0, semicolon);
        }
        if (uri.endsWith("/") && uri.length() > 1) {
            uri = uri.substring(0, uri.length() - 1);
        }
        return uri;
    }

    private UserDetails resolveUser(HttpServletRequest request) {
        String token = CookieUtil.getCookie(request, TOKEN_COOKIE);
        if (StringUtils.isBlank(token)) {
            token = request.getHeader(TOKEN_COOKIE);
        }
        if (StringUtils.isBlank(token)) {
            return null;
        }
        if (token.startsWith("Bearer ")) {
            token = token.substring(7);
        }
        try {
            return jwtTokenUtil.getUserDetailsFromToken(token);
        } catch (Exception e) {
            log.debug("AI 接口 token 解析失败：{}", e.getMessage());
            return null;
        }
    }

    /**
     * 参数硬校验。在拦截器里做一次，SSE 接口也能拿到明确错误而不是 406；
     * Controller 里仍保留同样的业务校验，防止拦截器漏配时出问题。
     */
    private ResponseStatus validateParams(HttpServletRequest request, String uri) {
        if (uri.startsWith(AI_ASSISTANT_PREFIX)) {
            return validateAssistantParams(request);
        }
        boolean textApi = uri.contains("expand") || uri.contains("condense")
            || uri.contains("continue") || uri.contains("polish");
        if (!textApi) {
            return null;
        }

        String text = request.getParameter("text");
        if (StringUtils.isBlank(text) || text.length() > properties.getMaxTextLength()) {
            return ResponseStatus.AI_PARAMS_INVALID;
        }

        try {
            if (uri.contains("expand")) {
                double ratio = Double.parseDouble(request.getParameter("ratio"));
                // NaN、Infinity、0、负数、超大倍数一律拒绝
                if (!(ratio > 0) || ratio > properties.getMaxExpandRatio()) {
                    return ResponseStatus.AI_PARAMS_INVALID;
                }
            } else if (uri.contains("condense")) {
                // 修复 100 / ratio 的除零：ratio 必须落在 [1, maxCondenseRatio]
                int ratio = Integer.parseInt(request.getParameter("ratio"));
                if (ratio <= 0 || ratio > properties.getMaxCondenseRatio()) {
                    return ResponseStatus.AI_PARAMS_INVALID;
                }
            } else if (uri.contains("continue")) {
                int length = Integer.parseInt(request.getParameter("length"));
                if (length <= 0 || length > properties.getMaxContinueLength()) {
                    return ResponseStatus.AI_PARAMS_INVALID;
                }
            }
        } catch (NumberFormatException | NullPointerException e) {
            return ResponseStatus.AI_PARAMS_INVALID;
        }
        return null;
    }

    /** AI 助手的长文本参数上限，避免超大正文直接打到 Service 与数据库 */
    private ResponseStatus validateAssistantParams(HttpServletRequest request) {
        for (String param : LONG_TEXT_PARAMS) {
            String value = request.getParameter(param);
            if (value != null && value.length() > properties.getMaxContentLength()) {
                return ResponseStatus.AI_PARAMS_INVALID;
            }
        }
        return null;
    }

    /**
     * 提交生成任务与保存章节（会触发 AI 摘要）按固定点数计费；
     * 其余写作接口的长文本按字符数折算更多点数，短文本至少 1 点。
     */
    private int calcCost(HttpServletRequest request, String uri) {
        if (START_TASK_URI.equals(uri)) {
            return properties.getTaskCost();
        }
        if (SAVE_CHAPTER_URI.equals(uri)) {
            return properties.getSummaryCost();
        }
        String text = request.getParameter("text");
        int chars = text == null ? 0 : text.length();
        int per = Math.max(1, properties.getCostPerChars());
        return 1 + (chars + per - 1) / per;
    }

    private void writeError(HttpServletResponse response, ResponseStatus status) throws IOException {
        response.setStatus(200);
        response.setContentType(MediaType.APPLICATION_JSON_VALUE);
        response.setCharacterEncoding(StandardCharsets.UTF_8.name());
        response.setHeader("Cache-Control", "no-store");
        response.getWriter().write(objectMapper.writeValueAsString(RestResult.fail(status)));
        response.getWriter().flush();
    }
}
