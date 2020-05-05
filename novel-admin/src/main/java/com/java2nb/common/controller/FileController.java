package com.java2nb.common.controller;

import com.java2nb.common.config.JnConfig;
import com.java2nb.common.domain.FileDO;
import com.java2nb.common.service.FileService;
import com.java2nb.common.utils.*;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.*;
import java.net.URLEncoder;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 文件上传
 * 
 * @author xiongxy
 * @email 1179705413@qq.com
 * @date 2019-09-19 16:02:20
 */
@Controller
@RequestMapping("/common/sysFile")
public class FileController extends BaseController {

	@Autowired
	private FileService sysFileService;

	@Autowired
	private JnConfig jnConfig;

	@GetMapping()
	@RequiresPermissions("common:sysFile:sysFile")
	String sysFile(Model model) {
		Map<String, Object> params = new HashMap<>(16);
		return "common/file/file";
	}

	@ResponseBody
	@GetMapping("/list")
	@RequiresPermissions("common:sysFile:sysFile")
	public PageBean list(@RequestParam Map<String, Object> params) {
		// 查询列表数据
		Query query = new Query(params);
		List<FileDO> sysFileList = sysFileService.list(query);
		int total = sysFileService.count(query);
		PageBean pageBean = new PageBean(sysFileList, total);
		return pageBean;
	}

	@GetMapping("/add")
	// @RequiresPermissions("common:bComments")
	String add() {
		return "common/sysFile/add";
	}

	@GetMapping("/edit")
	// @RequiresPermissions("common:bComments")
	String edit(Long id, Model model) {
		FileDO sysFile = sysFileService.get(id);
		model.addAttribute("sysFile", sysFile);
		return "common/sysFile/edit";
	}

	/**
	 * 信息
	 */
	@RequestMapping("/info/{id}")
	@RequiresPermissions("common:info")
	public R info(@PathVariable("id") Long id) {
		FileDO sysFile = sysFileService.get(id);
		return R.ok().put("sysFile", sysFile);
	}

	/**
	 * 保存
	 */
	@ResponseBody
	@PostMapping("/save")
	@RequiresPermissions("common:save")
	public R save(FileDO sysFile) {
		if (sysFileService.save(sysFile) > 0) {
			return R.ok();
		}
		return R.error();
	}

	/**
	 * 修改
	 */
	@RequestMapping("/update")
	@RequiresPermissions("common:update")
	public R update(@RequestBody FileDO sysFile) {
		sysFileService.update(sysFile);

		return R.ok();
	}

	/**
	 * 删除
	 */
	@PostMapping("/remove")
	@ResponseBody
	// @RequiresPermissions("common:remove")
	public R remove(Long id, HttpServletRequest request) {
		if ("test".equals(getUsername())) {
			return R.error(1, "演示系统不允许修改,完整体验请部署程序");
		}
		String fileName = jnConfig.getUploadPath() + sysFileService.get(id).getUrl().replace("/files/", "");
		if (sysFileService.remove(id) > 0) {
			boolean b = FileUtil.deleteFile(fileName);
			if (!b) {
				return R.error("数据库记录删除成功，文件删除失败");
			}
			return R.ok();
		} else {
			return R.error();
		}
	}

	/**
	 * 删除
	 */
	@PostMapping("/batchRemove")
	@ResponseBody
	@RequiresPermissions("common:remove")
	public R remove(@RequestParam("ids[]") Long[] ids) {
		if ("test".equals(getUsername())) {
			return R.error(1, "演示系统不允许修改,完整体验请部署程序");
		}
		sysFileService.batchRemove(ids);
		return R.ok();
	}

	@ResponseBody
	@PostMapping("/upload")
	R upload(@RequestParam("file") MultipartFile file, HttpServletRequest request) {
		if ("test".equals(getUsername())) {
			return R.error(1, "演示系统不允许修改,完整体验请部署程序");
		}
		Date date = new Date();
		String year = DateUtils.format(date,DateUtils.YEAR_PATTERN);
		String month = DateUtils.format(date,DateUtils.MONTH_PATTERN);
		String day = DateUtils.format(date,DateUtils.DAY_PATTERN);

		String fileName = file.getOriginalFilename();
		String fileDir = year+"/"+month+"/"+day + "/";
		fileName = FileUtil.renameToUUID(fileName);
		FileDO sysFile = new FileDO(FileType.fileType(fileName), "/files/" + fileDir + fileName, date);
		try {
			FileUtil.uploadFile(file.getBytes(), jnConfig.getUploadPath()+fileDir, fileName);
		} catch (Exception e) {
			return R.error();
		}

		if (sysFileService.save(sysFile) > 0) {
			return R.ok().put("fileName",sysFile.getUrl());
		}
		return R.error();
	}

	/**
	 * 文件下载
	 */
	@RequestMapping(value = "/download")
	public void fileDownload(String filePath,String fileName, HttpServletResponse resp) throws Exception {
			String realFilePath = jnConfig.getUploadPath() + filePath;
			InputStream in = new FileInputStream(realFilePath);
			//设置响应头，对文件进行url编码
			fileName = URLEncoder.encode(fileName, "UTF-8");
			resp.setHeader("Content-Disposition", "attachment;filename=" + fileName);

			resp.setContentLength(in.available());

			OutputStream out = resp.getOutputStream();
			byte[] b = new byte[1024];
			int len = 0;
			while ((len = in.read(b)) != -1) {
				out.write(b, 0, len);
			}
			out.flush();
			out.close();
			in.close();
	}


}
