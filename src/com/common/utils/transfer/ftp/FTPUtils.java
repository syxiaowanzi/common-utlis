package com.common.utils.transfer.ftp;

import java.io.File;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Properties;

import com.jcraft.jsch.Channel;
import com.jcraft.jsch.ChannelSftp;
import com.jcraft.jsch.JSch;
import com.jcraft.jsch.JSchException;
import com.jcraft.jsch.Session;
import com.jcraft.jsch.SftpException;

public class FTPUtils {

	/**
	 * 日志类
	 */
	
	/**
	 * 获取ftp文件对象
	 * @param ftpserver
	 * @param ftpport
	 * @param ftpuser
	 * @param ftppassword
	 * @param filePath
	 * @return
	 */
	public static List<String> getFtpFilePath(String ftpserver,int ftpport,
			String ftpuser,String ftppassword,List<String> filePaths,String localPath){
		//主机地址
		String server = ftpserver;
		//端口号
		int port = ftpport;
		//用户名
		String user = ftpuser;
		//密码
		String password = ftppassword;
		List<String> paths = new ArrayList<String>();
		Session session = null;  
        Channel channel = null;
        ChannelSftp chSftp = null;
        try {
        	//连接sftp
        	JSch jsch = new JSch();  
        	session = jsch.getSession(user, server, port);  
        	session.setPassword(password);  
        	session.setTimeout(100000);  
        	Properties config = new Properties();  
        	config.put("StrictHostKeyChecking", "no");  
        	session.setConfig(config);  
        	session.connect();
        	channel = session.openChannel("sftp");  
            channel.connect();
            //下载sftp
            chSftp = (ChannelSftp) channel;
            //获取本地下载路径
            String localDir = localPath;
            localDir = makeDir(localDir);
            for (String path : filePaths) {
            	String fileName = path.substring(path.lastIndexOf("/") + 1);
    			String localFilePath = localDir + File.separatorChar + fileName;
    			chSftp.get(path, localFilePath);
    			paths.add(localFilePath);
			}
		} catch (SftpException e) {
			e.printStackTrace();
		} catch (JSchException e) {
			e.printStackTrace();
		} finally {  
            chSftp.quit();  
            channel.disconnect();  
            session.disconnect();  
        }  
		return paths;
	}
	/**
	 * 创建文件夹
	 * @param localDir
	 */
	public static String makeDir(String localDir){
		//获取当前时间;
		Date date = new Date();
		Calendar c = Calendar.getInstance();
		c.setTime(date);
		String year = c.get(Calendar.YEAR)+"";
		String month = (c.get(Calendar.MONTH)+1)+"";
		if(month.length()==1){
			month = "0"+month;
		}
		//在本地文件夹下创建文件存放目录
		File file = new File(localDir+File.separator+year+File.separator+month);
		if(file.exists()){
			return file.getAbsolutePath();
		}else{
			file.mkdirs();
			return file.getAbsolutePath();
		}
	}
	public static void main(String[] args) {
		makeDir("D:/download");
	}
}
