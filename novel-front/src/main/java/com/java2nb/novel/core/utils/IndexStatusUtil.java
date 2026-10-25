package com.java2nb.novel.core.utils;

import com.java2nb.novel.entity.BookIndex;

import java.util.Date;

/**
 * 章节发布状态工具：草稿箱/定时发布功能的状态定义与读者可见性判定
 *
 * @author 11797
 */
public final class IndexStatusUtil {

    /**
     * 章节发布状态：草稿（仅作者可见）
     */
    public static final byte STATUS_DRAFT = 0;

    /**
     * 章节发布状态：已发布（读者可见），历史数据与爬虫章节默认此状态
     */
    public static final byte STATUS_PUBLISHED = 1;

    /**
     * 章节发布状态：定时发布（到达 publish_time 后由定时任务置为已发布）
     */
    public static final byte STATUS_SCHEDULED = 2;

    private IndexStatusUtil() {
    }

    /**
     * 章节是否对读者可见：已发布，或定时发布且已到时（兜底防定时任务延迟）
     */
    public static boolean isReaderVisible(BookIndex bookIndex) {
        if (bookIndex == null) {
            return false;
        }
        byte status = bookIndex.getStatus() == null ? STATUS_PUBLISHED : bookIndex.getStatus();
        if (status == STATUS_PUBLISHED) {
            return true;
        }
        return status == STATUS_SCHEDULED
            && bookIndex.getPublishTime() != null
            && !bookIndex.getPublishTime().after(new Date());
    }

}
