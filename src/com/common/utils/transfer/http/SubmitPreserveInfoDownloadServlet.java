package com.common.utils.transfer.http;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * 	提交保全信息下载servlet
 * @author wangming
 *
 */
public class SubmitPreserveInfoDownloadServlet extends HttpServlet {
	private static final long serialVersionUID = -2037904400107860069L;
	@Override
	protected void doPost(HttpServletRequest req, HttpServletResponse resp)throws ServletException, IOException {
		//获取数据库中存放地址
		String assureAddress = req.getParameter("assureAddress");
		//获取文件存放根目录
		//String storePath=XConfig.getValue("xConfig.uploadExcel")+"/";
		String storePath = "";
		//获取文件绝对路径
		String fullPath = storePath+assureAddress;
		//创建file对象
		File file = new File(fullPath);
		//获取文件名
		String fileName = fullPath.substring(fullPath.lastIndexOf("/")+1);
		//设置文件ContentType类型，这样设置，会自动判断下载文件类型
		resp.setContentType("multipart/form-data");
		//设置文件头：最后一个参数是设置下载文件名  
        resp.setHeader("Content-Disposition", "attachment;fileName="+fileName);
		ServletOutputStream out = null;
		FileInputStream fis = null;
		try {
			//创建输入流，用来读取
			fis = new FileInputStream(file);
			//获取输出流，用来写出
			out = resp.getOutputStream();
			int b=0;
			byte[] buf = new byte[1024];
			while((b=fis.read(buf)) != -1){
				out.write(buf,0,b);
			}
			out.flush();
		} catch (IOException e) {
			e.printStackTrace();
		} finally{
			if(fis!=null){
				try {
					fis.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
			if(out != null){
				try {
					out.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
		
		
	}
}
