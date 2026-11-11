package com.java2nb.novel.core.config;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.java2nb.novel.core.ai.AiApiRateLimiter;
import com.java2nb.novel.core.ai.AiRateLimitProperties;
import com.java2nb.novel.core.bean.UserDetails;
import com.java2nb.novel.core.enums.ResponseStatus;
import com.java2nb.novel.core.utils.JwtTokenUtil;
import com.java2nb.novel.entity.Author;
import com.java2nb.novel.service.AuthorService;
import jakarta.servlet.http.Cookie;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.mock.web.MockHttpServletResponse;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.eq;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoInteractions;
import static org.mockito.Mockito.when;

/**
 * AI 接口统一防护回归测试：匿名、非作家、封禁作家、非法参数、限流、轮询豁免。
 */
class AiApiAuthInterceptorTest {

    private static final long USER_ID = 1001L;
    private static final long AUTHOR_ID = 2002L;
    private static final String TOKEN = "valid-token";

    private JwtTokenUtil jwtTokenUtil;
    private AuthorService authorService;
    private AiApiRateLimiter rateLimiter;
    private AiApiAuthInterceptor interceptor;
    private final ObjectMapper objectMapper = new ObjectMapper();

    @BeforeEach
    void setUp() {
        jwtTokenUtil = mock(JwtTokenUtil.class);
        authorService = mock(AuthorService.class);
        rateLimiter = mock(AiApiRateLimiter.class);
        AiRateLimitProperties properties = new AiRateLimitProperties();
        interceptor = new AiApiAuthInterceptor(jwtTokenUtil, authorService, rateLimiter, properties,
            objectMapper);
        when(rateLimiter.tryAcquire(anyLong(), anyInt())).thenReturn(true);
        when(rateLimiter.tryAcquireLight(anyLong())).thenReturn(true);
    }

    @Test
    void anonymousRequestIsRejectedBeforeAnyQuotaCheck() throws Exception {
        MockHttpServletRequest request = post("/author/ai/expand", "text", "内容", "ratio", "150");
        MockHttpServletResponse response = new MockHttpServletResponse();

        assertFalse(interceptor.preHandle(request, response, new Object()));
        assertCode(response, ResponseStatus.NO_LOGIN);
        verifyNoInteractions(rateLimiter);
    }

    @Test
    void loggedInButNotAuthorIsRejected() throws Exception {
        MockHttpServletRequest request = loggedIn(post("/author/ai/polish", "text", "内容"), null);
        MockHttpServletResponse response = new MockHttpServletResponse();

        assertFalse(interceptor.preHandle(request, response, new Object()));
        assertCode(response, ResponseStatus.AI_AUTHOR_ONLY);
    }

    @Test
    void bannedAuthorIsRejected() throws Exception {
        Author banned = author();
        banned.setStatus((byte) 1);
        MockHttpServletRequest request = loggedIn(post("/author/ai/polish", "text", "内容"), banned);
        MockHttpServletResponse response = new MockHttpServletResponse();

        assertFalse(interceptor.preHandle(request, response, new Object()));
        assertCode(response, ResponseStatus.AUTHOR_STATUS_FORBIDDEN);
    }

    @Test
    void condenseRatioZeroIsRejectedInsteadOfDivisionByZero() throws Exception {
        MockHttpServletRequest request = loggedIn(
            post("/author/ai/condense", "text", "内容", "ratio", "0"), author());
        MockHttpServletResponse response = new MockHttpServletResponse();

        assertFalse(interceptor.preHandle(request, response, new Object()));
        assertCode(response, ResponseStatus.AI_PARAMS_INVALID);
        verifyNoInteractions(rateLimiter);
    }

    @Test
    void invalidExpandRatioAndLengthAreRejected() throws Exception {
        for (String ratio : new String[]{"NaN", "Infinity", "-1", "999999"}) {
            MockHttpServletRequest request = loggedIn(
                post("/author/ai/stream/expand", "text", "内容", "ratio", ratio), author());
            MockHttpServletResponse response = new MockHttpServletResponse();
            assertFalse(interceptor.preHandle(request, response, new Object()));
            assertCode(response, ResponseStatus.AI_PARAMS_INVALID);
        }

        MockHttpServletRequest request = loggedIn(
            post("/author/ai/stream/continue", "text", "内容", "length", "999999"), author());
        MockHttpServletResponse response = new MockHttpServletResponse();
        assertFalse(interceptor.preHandle(request, response, new Object()));
        assertCode(response, ResponseStatus.AI_PARAMS_INVALID);
    }

    @Test
    void oversizeTextIsRejected() throws Exception {
        String text = "x".repeat(new AiRateLimitProperties().getMaxTextLength() + 1);
        MockHttpServletRequest request = loggedIn(post("/author/ai/polish", "text", text), author());
        MockHttpServletResponse response = new MockHttpServletResponse();

        assertFalse(interceptor.preHandle(request, response, new Object()));
        assertCode(response, ResponseStatus.AI_PARAMS_INVALID);
        verifyNoInteractions(rateLimiter);
    }

    @Test
    void rateLimitedAuthorIsRejected() throws Exception {
        when(rateLimiter.tryAcquire(anyLong(), anyInt())).thenReturn(false);
        MockHttpServletRequest request = loggedIn(post("/author/ai/polish", "text", "内容"), author());
        MockHttpServletResponse response = new MockHttpServletResponse();

        assertFalse(interceptor.preHandle(request, response, new Object()));
        assertCode(response, ResponseStatus.AI_RATE_LIMIT);
    }

    @Test
    void validRequestPassesAndExposesAuthorId() throws Exception {
        MockHttpServletRequest request = loggedIn(
            post("/author/ai/expand", "text", "内容", "ratio", "150"), author());
        MockHttpServletResponse response = new MockHttpServletResponse();

        assertTrue(interceptor.preHandle(request, response, new Object()));
        assertEquals(USER_ID, request.getAttribute(AiApiAuthInterceptor.ATTR_USER_ID));
        assertEquals(AUTHOR_ID, request.getAttribute(AiApiAuthInterceptor.ATTR_AUTHOR_ID));
        verify(rateLimiter).tryAcquire(eq(USER_ID), anyInt());
    }

    @Test
    void coverPollingIsAuthenticatedButDoesNotConsumeAiQuota() throws Exception {
        MockHttpServletRequest request = loggedIn(
            get("/author/queryAiGenPic", "bookId", "1"), author());
        MockHttpServletResponse response = new MockHttpServletResponse();

        assertTrue(interceptor.preHandle(request, response, new Object()));
        assertEquals(AUTHOR_ID, request.getAttribute(AiApiAuthInterceptor.ATTR_AUTHOR_ID));
        verifyNoInteractions(rateLimiter);
    }

    @Test
    void anonymousCoverPollingIsRejected() throws Exception {
        MockHttpServletRequest request = get("/author/queryAiGenPic", "bookId", "1");
        MockHttpServletResponse response = new MockHttpServletResponse();

        assertFalse(interceptor.preHandle(request, response, new Object()));
        assertCode(response, ResponseStatus.NO_LOGIN);
    }

    @Test
    void corsPreflightIsAllowed() throws Exception {
        MockHttpServletRequest request = new MockHttpServletRequest("OPTIONS", "/author/ai/expand");
        request.setRequestURI("/author/ai/expand");
        MockHttpServletResponse response = new MockHttpServletResponse();

        assertTrue(interceptor.preHandle(request, response, new Object()));
        verifyNoInteractions(jwtTokenUtil, authorService);
        verify(rateLimiter, never()).tryAcquire(anyLong(), anyInt());
        verify(rateLimiter, never()).tryAcquireLight(anyLong());
    }

    @Test
    void anonymousAssistantRequestIsRejected() throws Exception {
        MockHttpServletRequest request = post("/author/aiAssistant/startTask",
            "planId", "1", "taskType", "1");
        MockHttpServletResponse response = new MockHttpServletResponse();

        assertFalse(interceptor.preHandle(request, response, new Object()));
        assertCode(response, ResponseStatus.NO_LOGIN);
        verifyNoInteractions(rateLimiter);
    }

    @Test
    void nonAuthorAssistantRequestIsRejectedInsteadOfServerError() throws Exception {
        MockHttpServletRequest request = loggedIn(
            get("/author/aiAssistant/progress", "planId", "1"), null);
        MockHttpServletResponse response = new MockHttpServletResponse();

        assertFalse(interceptor.preHandle(request, response, new Object()));
        assertCode(response, ResponseStatus.AI_AUTHOR_ONLY);
    }

    @Test
    void startTaskConsumesTaskQuota() throws Exception {
        int taskCost = new AiRateLimitProperties().getTaskCost();
        MockHttpServletRequest request = loggedIn(post("/author/aiAssistant/startTask",
            "planId", "1", "taskType", "1"), author());
        MockHttpServletResponse response = new MockHttpServletResponse();

        assertTrue(interceptor.preHandle(request, response, new Object()));
        verify(rateLimiter).tryAcquire(eq(USER_ID), eq(taskCost));
        verify(rateLimiter, never()).tryAcquireLight(anyLong());
    }

    @Test
    void saveChapterConsumesSummaryQuota() throws Exception {
        int summaryCost = new AiRateLimitProperties().getSummaryCost();
        MockHttpServletRequest request = loggedIn(post("/author/aiAssistant/saveChapter",
            "planId", "1", "chapterNo", "1", "indexName", "第一章", "content", "正文"), author());
        MockHttpServletResponse response = new MockHttpServletResponse();

        assertTrue(interceptor.preHandle(request, response, new Object()));
        verify(rateLimiter).tryAcquire(eq(USER_ID), eq(summaryCost));
        verify(rateLimiter, never()).tryAcquireLight(anyLong());
    }

    @Test
    void taskPollingUsesOnlyLightQuota() throws Exception {
        MockHttpServletRequest request = loggedIn(
            get("/author/aiAssistant/task/123"), author());
        MockHttpServletResponse response = new MockHttpServletResponse();

        assertTrue(interceptor.preHandle(request, response, new Object()));
        verify(rateLimiter).tryAcquireLight(eq(USER_ID));
        verify(rateLimiter, never()).tryAcquire(anyLong(), anyInt());
    }

    @Test
    void lightRateLimitedAssistantRequestIsRejected() throws Exception {
        when(rateLimiter.tryAcquireLight(anyLong())).thenReturn(false);
        MockHttpServletRequest request = loggedIn(
            get("/author/aiAssistant/progress", "planId", "1"), author());
        MockHttpServletResponse response = new MockHttpServletResponse();

        assertFalse(interceptor.preHandle(request, response, new Object()));
        assertCode(response, ResponseStatus.AI_RATE_LIMIT);
    }

    @Test
    void quotaLimitedStartTaskIsRejected() throws Exception {
        when(rateLimiter.tryAcquire(anyLong(), anyInt())).thenReturn(false);
        MockHttpServletRequest request = loggedIn(post("/author/aiAssistant/startTask",
            "planId", "1", "taskType", "1"), author());
        MockHttpServletResponse response = new MockHttpServletResponse();

        assertFalse(interceptor.preHandle(request, response, new Object()));
        assertCode(response, ResponseStatus.AI_RATE_LIMIT);
    }

    @Test
    void oversizeAssistantContentIsRejected() throws Exception {
        String content = "x".repeat(new AiRateLimitProperties().getMaxContentLength() + 1);
        MockHttpServletRequest request = loggedIn(post("/author/aiAssistant/saveDraft",
            "planId", "1", "chapterNo", "1", "content", content), author());
        MockHttpServletResponse response = new MockHttpServletResponse();

        assertFalse(interceptor.preHandle(request, response, new Object()));
        assertCode(response, ResponseStatus.AI_PARAMS_INVALID);
        verifyNoInteractions(rateLimiter);
    }

    private MockHttpServletRequest loggedIn(MockHttpServletRequest request, Author author) {
        request.setCookies(new Cookie("Authorization", TOKEN));
        UserDetails userDetails = new UserDetails();
        userDetails.setId(USER_ID);
        when(jwtTokenUtil.getUserDetailsFromToken(TOKEN)).thenReturn(userDetails);
        when(authorService.queryAuthorAll(USER_ID)).thenReturn(author);
        return request;
    }

    private Author author() {
        Author author = new Author();
        author.setId(AUTHOR_ID);
        author.setStatus((byte) 0);
        return author;
    }

    private MockHttpServletRequest post(String uri, String... params) {
        return request("POST", uri, params);
    }

    private MockHttpServletRequest get(String uri, String... params) {
        return request("GET", uri, params);
    }

    private MockHttpServletRequest request(String method, String uri, String... params) {
        MockHttpServletRequest request = new MockHttpServletRequest(method, uri);
        request.setRequestURI(uri);
        for (int i = 0; i < params.length; i += 2) {
            request.setParameter(params[i], params[i + 1]);
        }
        return request;
    }

    private void assertCode(MockHttpServletResponse response, ResponseStatus status) throws Exception {
        JsonNode body = objectMapper.readTree(response.getContentAsString());
        JsonNode code = body.get("code");
        assertNotNull(code, "响应缺少业务码字段：" + body);
        assertEquals(status.getCode(), code.asInt());
        assertTrue(response.getContentType().startsWith("application/json"));
    }
}
