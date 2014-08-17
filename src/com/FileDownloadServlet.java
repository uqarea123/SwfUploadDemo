package com;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class FileDownloadServlet extends HttpServlet {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public FileDownloadServlet() {
		super();
	}

	public void destroy() {
		super.destroy(); // Just puts "destroy" string in log
		// Put your code here
	}

	public void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		doPost(request,response);
	}

	public void doPost(HttpServletRequest req, HttpServletResponse res)
			throws ServletException, IOException {

		// 获取下载路径
		String path = req.getParameter("filePath");
		//path = java.net.URLEncoder.encode(path, "UTF-8");
		//System.out.println("path:" + path);
		//String prjPath = req.getSession().getServletContext().getRealPath("/");
		//String prjPath = req.getServerName();
		//path = prjPath +"\\"+ path;
		res.getWriter().print(path);
		
		// 服务器相对路�?
		// String path = req.getParameter("path");
		// 服务器绝对路�?
		// path = getServletContext().getRealPath("/") + path;
		 //path = "C:\\Users\\lenovo\\Desktop\\新建文本文档.txt";
		 //path = "E:\\kankan\\窃听风云2__3_160974_263013.xv";
		// �?��文件是否存在
		
		/* File obj = new File(path);
		 if (!obj.exists()) {
			res.setContentType("text/html;charset=utf-8");
			res.getWriter().print("指定文件不存在！");
			return;
		}
		//res.setContentType("application/octet-stream");
		// 读取文件名：用于设置客户端保存时指定默认文件�?
		int index = path.lastIndexOf("\\"); // 前提：传入的path字符串以“\”表示目录分隔符
		String fileName = path.substring(index + 1);
		// 写流文件到前端浏览器
		ServletOutputStream out = res.getOutputStream();
		// 中文文件名转码
		res.setHeader("Content-disposition", "attachment;filename="
				+ java.net.URLEncoder.encode(fileName, "UTF-8"));
		System.out.println("fuck2");
		BufferedInputStream bis = null;
		BufferedOutputStream bos = null;
		try {
			bis = new BufferedInputStream(new FileInputStream(path));
			bos = new BufferedOutputStream(out);
			byte[] buff = new byte[2048];
			int bytesRead;
			System.out.println("fuck3");
			while (-1 != (bytesRead = bis.read(buff, 0, buff.length))) {
				bos.write(buff, 0, bytesRead);
			}
		} catch (IOException e) {
			throw e;
		} finally {
			if (bis != null)
				bis.close();
			if (bos != null)
				bos.close();
		}*/
	}

	

	public void init() throws ServletException {
		// Put your code here
	}
}
