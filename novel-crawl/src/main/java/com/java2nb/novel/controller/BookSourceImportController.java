package com.java2nb.novel.controller;

import com.java2nb.novel.core.enums.ResponseStatus;
import com.java2nb.novel.service.BookSourceImportService;
import com.java2nb.novel.vo.BookSourceImportConfirmVO;
import io.github.xxyopen.model.resp.RestResult;
import io.github.xxyopen.web.exception.BusinessException;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.nio.charset.StandardCharsets;
import java.util.List;

/**
 * 第三方规则导入（目前支持阅读3.0书源类型，后续扩展其他类型）
 *
 * @author xiongxiaoyang
 */
@RestController
@RequestMapping("crawl/bookSourceImport")
@RequiredArgsConstructor
public class BookSourceImportController {

    private final BookSourceImportService bookSourceImportService;

    /**
     * 按规则类型解析并预览转换结果，支持JSON文本或上传文件
     */
    @PostMapping("preview")
    public RestResult<?> preview(
        @RequestParam(value = "bookSourceJson", required = false) String bookSourceJson,
        @RequestParam(value = "sourceType", required = false) String sourceType,
        @RequestParam(value = "file", required = false) MultipartFile file) throws Exception {
        String json = bookSourceJson;
        if (file != null && !file.isEmpty()) {
            json = new String(file.getBytes(), StandardCharsets.UTF_8);
        }
        try {
            return RestResult.ok(bookSourceImportService.preview(json, sourceType));
        } catch (BusinessException e) {
            return RestResult.fail(e.getResultCode());
        } catch (Exception e) {
            return RestResult.fail(ResponseStatus.BOOK_SOURCE_PARSE_FAIL);
        }
    }

    /**
     * 确认导入勾选的规则条目，同名或非法规则会被跳过
     */
    @PostMapping("confirm")
    public RestResult<BookSourceImportService.ImportSummary> confirm(@RequestBody List<BookSourceImportConfirmVO> items) {
        return RestResult.ok(bookSourceImportService.confirm(items));
    }

}
