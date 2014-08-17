package com;

import java.io.File;
import java.io.IOException;
import java.util.Enumeration;

import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import net.sf.json.JSONObject;

import org.apache.commons.io.FilenameUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.time.DateFormatUtils;


import com.oreilly.servlet.MultipartRequest;
import com.oreilly.servlet.multipart.FileRenamePolicy;

/**
 * @author 周勇
 * @version 2011-12-4
 */
public class FileUploadServlet extends HttpServlet {

	private static final long serialVersionUID = -3096800116651263134L;
	//private AttachmentManager am = new AttachmentManager();
	private String fileSizeLimit;
	private File oldFile;
	private Attachment attachment;//附件

	public void init(ServletConfig config) throws ServletException {
		this.fileSizeLimit = config.getInitParameter("fileSizeLimit");
	}

	public void destroy() {
		this.fileSizeLimit = null;
		super.destroy();
	}

	class MyFileRenamePolicy implements FileRenamePolicy {
		public File rename(File file) {
			oldFile = file;
			String fileSaveName = StringUtils.join(new String[] { java.util.UUID.randomUUID().toString(), ".",
					FilenameUtils.getExtension(file.getName()) });
			File result = new File(file.getParentFile(), fileSaveName);
			return result;
		}
	}

	@SuppressWarnings("unchecked")
	public void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		LogPrinter.info("--- BEGIN DOPOST ---");
		HttpSession session = request.getSession();
		//文件保存位置，当前项目下的upload/attachment
		String uploadDir = "upload" + File.separatorChar + "attachment" + File.separatorChar;
		//每天上传的文件根据日期存放在不同的文件夹
		String autoCreatedDateDirByParttern = "yyyy" + File.separatorChar + "MM" + File.separatorChar + "dd"
				+ File.separatorChar;
		String autoCreatedDateDir = DateFormatUtils.format(new java.util.Date(), autoCreatedDateDirByParttern);
		String ctxDir = session.getServletContext().getRealPath(String.valueOf(File.separatorChar));
		if (!ctxDir.endsWith(String.valueOf(File.separatorChar))) {
			ctxDir = ctxDir + File.separatorChar;
		}
		File savePath = new File(ctxDir + uploadDir + autoCreatedDateDir);
		if (!savePath.exists()) {
			savePath.mkdirs();
		}
		LogPrinter.info(savePath.getName()+"======");
		String saveDirectory = ctxDir + uploadDir + autoCreatedDateDir;
		
		if (StringUtils.isBlank(this.fileSizeLimit.toString())) {
			this.fileSizeLimit = "80";// 默认100M
		}
		int maxPostSize = Integer.parseInt(this.fileSizeLimit) * 1024 * 1024;
		String encoding = "UTF-8";
		FileRenamePolicy rename = new MyFileRenamePolicy();
		MultipartRequest multi = null;
		try {
			multi = new MultipartRequest(request, saveDirectory, maxPostSize, encoding, rename);
			
			LogPrinter.info(oldFile.getName()+"==================");
		} catch (IOException e) {
			e.printStackTrace();
			return;
		}

		// 输出
		Enumeration files = multi.getFileNames();
		String fileSavePath = "";
		File newFile = null;
		String contentType = "";
		while (files.hasMoreElements()) {
			String name = (String) files.nextElement();
			contentType =  multi.getContentType(name);
			//System.out.println("ContentType:"+contentType);
			File f = multi.getFile(name);
			LogPrinter.info(f.getName());
			if (f != null) {
				newFile = f;
				String fileName = multi.getFilesystemName(name);
				String lastFileName = saveDirectory + "\\" + fileName;
				fileSavePath = uploadDir + autoCreatedDateDir + fileName;
				LogPrinter.info("SimpleUploaderServlet");
				LogPrinter.info("文件地址:" + lastFileName);
				LogPrinter.info("保存路径:" + fileSavePath);
				
			}
		}
		String oldFileName = oldFile.getName();
		//保存数据库
		LogPrinter.info("开始保存数据库");
		attachment = new Attachment();
		attachment.setFilename(oldFileName);
		attachment.setMemberID("111");
		attachment.setContenttype(contentType);
		attachment.setDescription("这是描述");
		if(newFile!=null){
			attachment.setFilelength(newFile.length());
		}else{
			attachment.setFilelength(0);
		}
		attachment.setFilenameindisk(fileSavePath);
		attachment.setNeedwatermark(1);
		JSONObject jsonObj = JSONObject.fromObject(attachment);
		
		//jsonObj = null;
		String returnData = "";
		//传回前台数据
		if(jsonObj!=null){
			//取出前台传来的数据
			String saveDataId = request.getParameter("saveDataId");
			jsonObj.accumulate("saveDataId", saveDataId);
			returnData = jsonObj.toString();
			if(returnData.isEmpty()){
				returnData = "{\"error\":\"system error\"}";
				LogPrinter.error("json为空："+FileUploadServlet.class);
				//删除上传的文件
				newFile.delete();
			}
		}else{
			returnData = "{\"error\":\"system error\"}";
			LogPrinter.error("object转json出错了:"+FileUploadServlet.class);
			//删除上传的文件
			newFile.delete();
		}
		LogPrinter.debug("jsonObj:"+returnData+"--"+FileUploadServlet.class);
		//am.saveAttachment(attachment);
		//String attachmentID = attachment.getID();
		//System.out.println("附件id:"+attachmentID);
		
		
		//返回前台的数据
		response.setCharacterEncoding("utf-8");
		response.getWriter().print(returnData);
		LogPrinter.info("--- END DOPOST ---");
	}

	public void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		this.doPost(request, response);
	}

	public String getFileSizeLimit() {
		return fileSizeLimit;
	}

	public void setFileSizeLimit(String fileSizeLimit) {
		this.fileSizeLimit = fileSizeLimit;
	}

}
