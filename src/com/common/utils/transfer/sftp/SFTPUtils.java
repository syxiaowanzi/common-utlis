package com.common.utils.transfer.sftp;

import java.util.Properties;

import com.jcraft.jsch.Channel;
import com.jcraft.jsch.ChannelSftp;
import com.jcraft.jsch.JSch;
import com.jcraft.jsch.JSchException;
import com.jcraft.jsch.Session;
import com.jcraft.jsch.SftpException;

public class SFTPUtils {
	
	private static Session session;
	private static Channel channel;
	private static ChannelSftp chSftp;

	public SFTPUtils() {
		super();
	}
	/**
	 * 	连接sftp
	 */
	public static void connection(){
        try {
        	//连接sftp
        	JSch jsch = new JSch();  
        	session = jsch.getSession(SFTPParamter.SFTPUSERNAME, SFTPParamter.SFTPSERVER, SFTPParamter.SFTPPORT);  
        	session.setPassword(SFTPParamter.SFTPPASSWORD);  
        	session.setTimeout(100000);  
        	Properties config = new Properties();  
        	config.put("StrictHostKeyChecking", "no");  
        	session.setConfig(config);  
        	session.connect();
        	channel = session.openChannel("sftp");  
        	channel.connect();
        	//下载sftp
            chSftp = (ChannelSftp) channel;
		} catch (JSchException e) {
			e.printStackTrace();
		}
	}
	/**
	 * 	从sftp下载文件
	 */
	public static void downFile(String sftpPath,String localPath){
		SFTPUtils.connection();
		try {
			chSftp.get(sftpPath, localPath);
		} catch (SftpException e) {
			e.printStackTrace();
		} finally {  
            chSftp.quit();  
            channel.disconnect();  
            session.disconnect();  
        }
	}
}
