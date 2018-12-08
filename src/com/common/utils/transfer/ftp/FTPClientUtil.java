package com.common.utils.transfer.ftp;

import it.sauronsoftware.ftp4j.FTPAbortedException;
import it.sauronsoftware.ftp4j.FTPClient;
import it.sauronsoftware.ftp4j.FTPDataTransferException;
import it.sauronsoftware.ftp4j.FTPException;
import it.sauronsoftware.ftp4j.FTPFile;
import it.sauronsoftware.ftp4j.FTPIllegalReplyException;
import it.sauronsoftware.ftp4j.FTPListParseException;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;


/**
 * ftp工具类
 * @author wangming
 * 时间:2016-6-12 15:05:34
 *
 */
public class FTPClientUtil {
	

	 private FTPClient ftpClient;  
	 
	 public FTPClientUtil(String server,  String user, String password, int port){
		 //连接服务器
		 connectServer(server, user, password,  port);
	 }
	 
	/**
	 * 使用详细信息进行服务器连接
     * @param server：服务器地址名称
     * @param port：端口号
     * @param user：用户名
     * @param password：用户密码
     */
	 public void connectServer(String server, String user, String password, int port){
		ftpClient = new FTPClient();
		try {
			 ftpClient.connect(server,port);
			 ftpClient.login(user,password);
		} catch (Exception e) {
			e.printStackTrace();
		}
	 }
	 
	 
	 
     /**
      * 切换服务器的文件目录
      * @param remotePath
      */
	 public void changeDirectory(String remotePath){
		 
		try {
			File fileDir = new File(remotePath);
			if(fileDir.isDirectory()){
				this.ftpClient.changeDirectory(remotePath);
			}
		} catch (IllegalStateException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} catch (FTPIllegalReplyException e) {
			e.printStackTrace();
		} catch (FTPException e) {
			e.printStackTrace();
		}
		 
	 }
	 
	/**
     * 检查目录在服务器上是否存在 true：存在  false：不存在
     * @param path
     * @return
     * @throws IOException
     */
    public boolean existDirectory(String path,String dirName) throws IOException {  
        boolean flag = false;  
        FTPFile[] ftpFileArr = null;
		try {
			ftpFileArr = this.ftpClient.list(path);
			for (FTPFile ftpFile : ftpFileArr) {  
	            if (ftpFile.getName().equalsIgnoreCase(dirName)) {  
	                flag = true;  
	                break;  
	            }  
	        }  
		} catch (IllegalStateException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (FTPIllegalReplyException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (FTPException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (FTPDataTransferException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (FTPAbortedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (FTPListParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} 
        
        return flag;  
    }  
	 
	 /**
	  * 从ftp服务器上下载文件到本地
	  * @param remotePath 服务器文件的路径(包含文件名)
	  * @param localpath 下载到本地的路径目录
	  * @return  boolean
	  */
	 public boolean download(String remotePath,String localpath){
		 
		 boolean boo = false;
		 
		 if(remotePath.lastIndexOf("/")==-1){
			 return boo;
		 }
		 //文件名
		 String fileName = remotePath.substring(remotePath.lastIndexOf("/") + 1);  
		 //服务器文件的目录
         String remoteFileName = remotePath.substring(0, remotePath.lastIndexOf("/")); 
		 
         try {
        	 //切换到该下载的目录
			this.ftpClient.changeDirectory(remoteFileName);
			File localFile = new File(localpath + "/" +fileName);
			if(!localFile.exists()){
				localFile.getParentFile().mkdirs();
			}
			this.ftpClient.download(remotePath, localFile);
			boo = true;
		} catch (Exception  e) {
			boo = false;
			e.printStackTrace();
		}
		
		return boo;
	 }
	 
	 /**
	  * 上传本地文件到服务器
	  * @param ftpPath 服务器的路径
	  * @param sendTime 发送时间
	  * @param localFilePath 本地上传的文件路径(包含文件)
	  * @param fileName 文件名
	  * @return boolean
	  */
	 public String uploadFile(String ftpPath,String sendTime,String localFilePath,String fileName){
		 
		 String dateStr = sendTime.split(" ")[0];
    	 String[] dates = dateStr.split("-");
    	 String YM_dirName = dates[0] + dates[1];
    	 String YM_ftpPath = ftpPath + "/" + YM_dirName;
    	 String D_dirName = dates[2];
    	 String D_ftpPath = YM_ftpPath + "/" + D_dirName;
		 
		 try {
			 if(this.existDirectory(ftpPath, YM_dirName) == false){//年月目录不存在
			    this.ftpClient.createDirectory(YM_ftpPath);
	    		this.ftpClient.createDirectory(D_ftpPath);
	    	}else{
	    		if(this.existDirectory(YM_ftpPath, D_dirName) == false){
	    			this.ftpClient.createDirectory(D_ftpPath);
	    		}
	    	}
			 
		   File localFile = new File(localFilePath);
		   if(localFile.isFile()){
			   this.ftpClient.changeDirectory(D_ftpPath);
			   this.ftpClient.upload(localFile);
			   return D_ftpPath+"/"+fileName;
		   }
		 } catch (IllegalStateException e) {
			e.printStackTrace();
		 } catch (FileNotFoundException e) {
			e.printStackTrace();
		 } catch (IOException e) {
			e.printStackTrace();
		 } catch (FTPIllegalReplyException e) {
			e.printStackTrace();
		 } catch (FTPException e) {
			e.printStackTrace();
		 } catch (FTPDataTransferException e) {
			e.printStackTrace();
		 } catch (FTPAbortedException e) {
			e.printStackTrace();
		 }
		 
		return null;
		 
	 }
	 
	 /**
	  * 上传本地文件到服务器（目录有变化，上传文件前一级加一层目录） 税延产品
	  * @param ftpPath 服务器的路径
	  * @param sendTime 发送时间
	  * @param localFilePath 本地上传的文件路径(包含文件)
	  * @param fileName 文件名
	  * @param orderCode 汇交申请单号(在文件前多加一级文件夹 名称)
	  * @return boolean
	  */
	 public String uploadSYFilepath(String ftpPath,String sendTime,String localFilePath,String fileName,String orderCode){
		 
		 String dateStr = sendTime.split(" ")[0];
    	 String[] dates = dateStr.split("-");
    	 String YM_dirName = dates[0] + dates[1];
    	 String YM_ftpPath = ftpPath + "/" + YM_dirName;
    	 String D_dirName = dates[2];
    	 String D_ftpPath = YM_ftpPath + "/" + D_dirName;
    	 String O_dirName = orderCode;
    	 String O_ftpPath = D_ftpPath + "/" + orderCode;
		
    	 try {
			 if(this.existDirectory(ftpPath, YM_dirName) == false){//年月目录不存在
				 this.ftpClient.createDirectory(YM_ftpPath);
		    		this.ftpClient.createDirectory(D_ftpPath);
		    		this.ftpClient.createDirectory(O_ftpPath);
	    	}else{
	    		if(this.existDirectory(YM_ftpPath, D_dirName) == false){  //日目录不存在
	    			this.ftpClient.createDirectory(D_ftpPath);
	    			this.ftpClient.createDirectory(O_ftpPath);
	    		}else{
	    			if(this.existDirectory(D_ftpPath, O_dirName) ==false){  //汇交单申请单号目录不存在
	    				this.ftpClient.createDirectory(O_ftpPath);
	    			}
	    		}
	    	}
			 
		   File localFile = new File(localFilePath);
		   if(localFile.isFile()){
			   this.ftpClient.changeDirectory(O_ftpPath);
			   this.ftpClient.upload(localFile);
			   return O_ftpPath+"/"+fileName;
		   }
		 } catch (IllegalStateException e) {
			e.printStackTrace();
		 } catch (FileNotFoundException e) {
			e.printStackTrace();
		 } catch (IOException e) {
			e.printStackTrace();
		 } catch (FTPIllegalReplyException e) {
			e.printStackTrace();
		 } catch (FTPException e) {
			e.printStackTrace();
		 } catch (FTPDataTransferException e) {
			e.printStackTrace();
		 } catch (FTPAbortedException e) {
			e.printStackTrace();
		 }
		 
		return null;
		 
	 }
	 /**
	  * 上传 发票资料，其他资料 
	  * 
	  * /app/xml/MidPlat/EBWebSite/insjpg/201608/29/受理号_rpt  发票资料
	  * /app/xml/MidPlat/EBWebSite/insjpg/201608/29/受理号_oth  其他资料   
	  * 
	  * @param ftpPath   /app/xml/MidPlat/EBWebSite/insjpg/
	  * @param localpath 本地上传的文件路径(包含文件)
	  * @param fileName  文件名称（服务器重命名）
	  * @param acceptNo  受理号
	  * @param type 	  其他  oth 发票  rpt
	  * @return String   上传地址
	  *  ztr
	  */
	 public String uploadImg(String ftpPath,String sendTime,String localFilePath,String fileName,String acceptNo,String type){
		 
		 String dateStr = sendTime.split(" ")[0];
		 String[] dates = dateStr.split("-");
		 String YM_dirName = dates[0] + dates[1];
		 String YM_ftpPath = ftpPath + "/" + YM_dirName;
		 String D_dirName = dates[2];
		 String D_ftpPath = YM_ftpPath + "/" + D_dirName; // /app/xml/MidPlat/EBWebSite/insjpg/201608/29/
		 
		 String acceptDir = acceptNo + "_"+type;
		 String Q_ftpPath = "";
		 if("rpt".equals(type)){
			 Q_ftpPath = D_ftpPath + "/" + acceptNo + "_"+type;
		 }else if("oth".equals(type)){
			 Q_ftpPath = D_ftpPath + "/" + acceptNo + "_"+type;
		 }else if("pdf".equals(type)){
			 Q_ftpPath = D_ftpPath + "/" + acceptNo + "_"+type;
		 }
		 
		 try {
			 if(this.existDirectory(ftpPath, YM_dirName) == false){//年月目录不存在
				 this.ftpClient.createDirectory(YM_ftpPath);
				 this.ftpClient.createDirectory(D_ftpPath);
				 this.ftpClient.createDirectory(Q_ftpPath);
			 }else{
				 if(this.existDirectory(YM_ftpPath, D_dirName) == false){
					 this.ftpClient.createDirectory(D_ftpPath);
					 this.ftpClient.createDirectory(Q_ftpPath);
				 }else{
					 if(this.existDirectory(D_ftpPath,acceptDir) == false){
						 this.ftpClient.createDirectory(Q_ftpPath);
					 }
				 }
			 }
			 
			 File localFile = new File(localFilePath);
			 if(localFile.isFile()){
				 this.ftpClient.changeDirectory(Q_ftpPath);
				 this.ftpClient.upload(localFile);
//				 try {
//					String[] d = ftpClient.listNames();
////					for(String a : d){
////						System.out.println("listNames-->"+a);
////						
////					}
//				} catch (FTPListParseException e) {
//					e.printStackTrace();
//				}
				 return Q_ftpPath+"/"+fileName;
			 }
		 } catch (IllegalStateException e) {
			 e.printStackTrace();
		 } catch (FileNotFoundException e) {
			 e.printStackTrace();
		 } catch (IOException e) {
			 e.printStackTrace();
		 } catch (FTPIllegalReplyException e) {
			 e.printStackTrace();
		 } catch (FTPException e) {
			 e.printStackTrace();
		 } catch (FTPDataTransferException e) {
			 e.printStackTrace();
		 } catch (FTPAbortedException e) {
			 e.printStackTrace();
		 }
		 
		 return null;
		 
	 }
    /**
     * 关闭连接
     * @throws IOException
     */
    public void closeServer(){  
        if (ftpClient!=null&&ftpClient.isConnected()) {  
            try {
				ftpClient.disconnect(true);
			} catch (Exception e) {
				e.printStackTrace();
			} 
        }  
    }
	 
    /**
     * 设置编码格式
     * @param encoding
     */
    public void setCharset(String encoding){
    	ftpClient.setCharset(encoding);
    }
    
    
	 public static void main(String[] args) {
		    FTPClientUtil fTPUtil = new FTPClientUtil("192.168.180.87","ftpuser","ftp", 21);
			boolean boo = fTPUtil.download("/app/xml/MidPlat/EBWebSite/instxt/201605/10/EB0001_00000000000000001187.ins.txt", "D:/ftp");
			System.out.println(boo);

	}
}
