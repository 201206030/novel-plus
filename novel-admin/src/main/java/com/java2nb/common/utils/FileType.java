package com.java2nb.common.utils;

/* author：zss
 * 日期：2017年3月31日
 * 功能：根据文件名称判断类型
 * 接受参数类型：String 
 * 返回参数类型：String
 * 备注：文件类型不完善，有需要的自行添加
 */
public class FileType {
	public static int fileType(String fileName) {
		if (fileName == null) {
			fileName = "文件名为空！";
			return 500;

		} else {
			// 获取文件后缀名并转化为写，用于后续比较
			String fileType = fileName.substring(fileName.lastIndexOf(".") + 1, fileName.length()).toLowerCase();
			// 创建图片类型数组0
			String[] img = { "bmp", "jpg", "jpeg", "png", "tiff", "gif", "pcx", "tga", "exif", "fpx", "svg", "psd",
					"cdr", "pcd", "dxf", "ufo", "eps", "ai", "raw", "wmf" };
			for (int i = 0; i < img.length; i++) {
				if (img[i].equals(fileType)) {
					return 0;
				}
			}

			// 创建文档类型数组1
			String[] document = { "txt", "doc", "docx", "xls", "htm", "html", "jsp", "rtf", "wpd", "pdf", "ppt" };
			for (int i = 0; i < document.length; i++) {
				if (document[i].equals(fileType)) {
					return 1;
				}
			}
			// 创建视频类型数组2
			String[] video = { "mp4", "avi", "mov", "wmv", "asf", "navi", "3gp", "mkv", "f4v", "rmvb", "webm" };
			for (int i = 0; i < video.length; i++) {
				if (video[i].equals(fileType)) {
					return 2;
				}
			}
			// 创建音乐类型数组3
			String[] music = { "mp3", "wma", "wav", "mod", "ra", "cd", "md", "asf", "aac", "vqf", "ape", "mid", "ogg",
					"m4a", "vqf" };
			for (int i = 0; i < music.length; i++) {
				if (music[i].equals(fileType)) {
					return 3;
				}
			}

		}
		//4
		return 99;
	}
}