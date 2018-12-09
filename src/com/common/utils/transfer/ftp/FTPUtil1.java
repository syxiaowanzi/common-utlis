package com.common.utils.transfer.ftp;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.SocketException;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.net.ftp.FTP;
import org.apache.commons.net.ftp.FTPClient;
import org.apache.commons.net.ftp.FTPFile;
import org.apache.commons.net.ftp.FTPReply;
  
public class FTPUtil1 {  
    private FTPClient ftpClient;  
    public static final int BINARY_FILE_TYPE = FTP.BINARY_FILE_TYPE;  
    public static final int ASCII_FILE_TYPE = FTP.ASCII_FILE_TYPE;  
      
    
    /**
     * 	利用FtpConfig进行服务器连接
     * @param ftpConfig 参数配置Bean类
     * @throws SocketException
     * @throws IOException
     */
    public FTPUtil1(String server,  String user,  
            String password, int port,String path) throws SocketException,  
            IOException {  
        connectServer(server, port, user, password, path);  
    }  
      
    /**
     * 	使用详细信息进行服务器连接
     * @param server：服务器地址名称
     * @param port：端口号
     * @param user：用户名
     * @param password：用户密码
     * @param path：转移到FTP服务器目录 
     * @throws SocketException
     * @throws IOException
     */
    public void connectServer(String server, int port, String user,  
            String password, String path) throws SocketException, IOException {  
        ftpClient = new FTPClient();  
        ftpClient.connect(server, port);  
        System.out.println("Connected to " + server + ".");  
        //连接成功后的回应码
        System.out.println(ftpClient.getReplyCode());  
        ftpClient.login(user, password);  
        
        if (!FTPReply.isPositiveCompletion(ftpClient.getReplyCode())) {
			ftpClient.disconnect();
			return;
		} 
        
        
        if (path!=null&&path.length() != 0) {  
            ftpClient.changeWorkingDirectory(path);  
        }  
    	ftpClient.setBufferSize(1024);//设置上传缓存大小
    	ftpClient.setControlEncoding("UTF-8");//设置编码
    	ftpClient.setFileType(BINARY_FILE_TYPE);//设置文件类型
    }  
    
    /**
     * 	设置传输文件类型:FTP.BINARY_FILE_TYPE | FTP.ASCII_FILE_TYPE  
     * 	二进制文件或文本文件
     * @param fileType
     * @throws IOException
     */
    public void setFileType(int fileType) throws IOException {  
        ftpClient.setFileType(fileType);  
    }  
  
    /**
     * 	关闭连接
     * @throws IOException
     */
    public void closeServer() throws IOException {  
        if (ftpClient!=null&&ftpClient.isConnected()) {  
        	ftpClient.logout();//退出FTP服务器 
            ftpClient.disconnect();//关闭FTP连接 
        }  
    }
    
    /**
     * 	转移到FTP服务器工作目录
     * @param path
     * @return
     * @throws IOException
     */
    public boolean changeDirectory(String path) throws IOException {  
        return ftpClient.changeWorkingDirectory(path);  
    }  
    
    /**
     * 	在服务器上创建目录
     * @param pathName
     * @return
     * @throws IOException
     */
    public boolean createDirectory(String pathName) throws IOException {  
        return ftpClient.makeDirectory(pathName);  
    }  
    
    /**
     * 	在服务器上删除目录
     * @param path
     * @return
     * @throws IOException
     */
    public boolean removeDirectory(String path) throws IOException {  
        return ftpClient.removeDirectory(path);  
    }  
      
    /**
     *	 删除所有文件和目录
     * @param path
     * @param isAll true:删除所有文件和目录
     * @return
     * @throws IOException
     */
    public boolean removeDirectory(String path, boolean isAll)  
            throws IOException {  
          
        if (!isAll) {  
            return removeDirectory(path);  
        }  
  
        FTPFile[] ftpFileArr = ftpClient.listFiles(path);  
        if (ftpFileArr == null || ftpFileArr.length == 0) {  
            return removeDirectory(path);  
        }  
        //   
        for (FTPFile ftpFile : ftpFileArr) {  
            String name = ftpFile.getName();  
            if (ftpFile.isDirectory()) {  
            	System.out.println("* [sD]Delete subPath ["+path + "/" + name+"]");               
                removeDirectory(path + "/" + name, true);  
            } else if (ftpFile.isFile()) {  
            	System.out.println("* [sF]Delete file ["+path + "/" + name+"]");                          
                deleteFile(path + "/" + name);  
            } else if (ftpFile.isSymbolicLink()) {  
  
            } else if (ftpFile.isUnknown()) {  
  
            }  
        }  
        return ftpClient.removeDirectory(path);  
    }  
    
    /**
     * 	检查目录在服务器上是否存在 true：存在  false：不存在
     * @param path
     * @return
     * @throws IOException
     */
    public boolean existDirectory(String path,String dirName) throws IOException {  
        boolean flag = false;  
        FTPFile[] ftpFileArr = ftpClient.listFiles(path);  
        for (FTPFile ftpFile : ftpFileArr) {  
            if (ftpFile.isDirectory()  
                    && ftpFile.getName().equalsIgnoreCase(dirName)) {  
                flag = true;  
                break;  
            }  
        }  
        return flag;  
    }  
  
    /**
     * 	得到文件列表,listFiles返回包含目录和文件，它返回的是一个FTPFile数组
     * listNames()：只包含目录的字符串数组
     * String[] fileNameArr = ftpClient.listNames(path); 
     * @param path:服务器上的文件目录:/DF4
     */
    public List<String> getFileList(String path) throws IOException {  
        FTPFile[] ftpFiles= ftpClient.listFiles(path);  
        //通过FTPFileFilter遍历只获得文件
/*      FTPFile[] ftpFiles2= ftpClient.listFiles(path,new FTPFileFilter() {
      @Override
      public boolean accept(FTPFile ftpFile) {
        return ftpFile.isFile();
      }
    });  */
        List<String> retList = new ArrayList<String>();  
        if (ftpFiles == null || ftpFiles.length == 0) {  
            return retList;  
        }  
        for (FTPFile ftpFile : ftpFiles) {  
            if (ftpFile.isFile()) {  
                retList.add(ftpFile.getName());  
            }  
        }  
        return retList;  
    }  
  
    /**
     * 	删除服务器上的文件
     * @param pathName
     * @return
     * @throws IOException
     */
    public boolean deleteFile(String pathName) throws IOException {  
        return ftpClient.deleteFile(pathName);  
    }  
  
    /**
     * 	上传文件到ftp服务器
     * 	在进行上传和下载文件的时候，设置文件的类型最好是：
     * ftpUtil.setFileType(FtpUtil.BINARY_FILE_TYPE)
     * localFilePath:本地文件路径和名称
     * remoteFileName:服务器文件名称
     */
    public boolean uploadFile(String localFilePath, String remoteFileName)  
            throws IOException {  
        boolean flag = false;  
        InputStream iStream = null;  
        try {  
            iStream = new FileInputStream(localFilePath);  
            //我们可以使用BufferedInputStream进行封装
            //BufferedInputStream bis=new BufferedInputStream(iStream);
            //flag = ftpClient.storeFile(remoteFileName, bis); 
            flag = ftpClient.storeFile(remoteFileName, iStream);  
        } catch (IOException e) {  
            flag = false;  
            return flag;  
        } finally {  
            if (iStream != null) {  
                iStream.close();  
            }  
        }  
        return flag;  
    }  
  
    /**
     *	 上传文件到ftp服务器，上传新的文件名称和原名称一样
     * @param fileName：文件名称
     * @return
     * @throws IOException
     */
    public boolean uploadFile(String fileName) throws IOException {  
        return uploadFile(fileName, fileName);  
    }  
  
    /**
     * 	上传文件到ftp服务器
     * @param iStream 输入流
     * @param newName 新文件名称
     * @return
     * @throws IOException
     */
    public boolean uploadFile(InputStream iStream, String newName)  
            throws IOException {  
        boolean flag = false;  
        try {  
            flag = ftpClient.storeFile(newName, iStream);  
        } catch (IOException e) {  
            flag = false;  
            return flag;  
        } finally {  
            if (iStream != null) {  
                iStream.close();  
            }  
        }  
        return flag;  
    }  
  
    /**
     *	 从ftp服务器上下载文件到本地
     * @param remoteFileName：ftp服务器上文件名称
     * @param localFileName：本地文件名称
     * @return
     * @throws IOException
     */
    public boolean download(String remoteFileName, String localFileName)  
            throws IOException {  
        boolean flag = false;  
        File outfile = new File(localFileName);  
        OutputStream oStream = null;  
        try {  
            oStream = new FileOutputStream(outfile);  
            //我们可以使用BufferedOutputStream进行封装
         	//BufferedOutputStream bos=new BufferedOutputStream(oStream);
         	//flag = ftpClient.retrieveFile(remoteFileName, bos); 
            flag = ftpClient.retrieveFile(remoteFileName, oStream);  
        } catch (IOException e) {  
            flag = false;  
            return flag;  
        } finally {  
            oStream.close();  
        }  
        return flag;  
    }  
      
    /**
     * 	从ftp服务器上下载文件到本地
     * @param sourceFileName：服务器资源文件名称
     * @return InputStream 输入流
     * @throws IOException
     */
    public InputStream downFile(String sourceFileName) throws IOException {  
        return ftpClient.retrieveFileStream(sourceFileName);  
    }  
    
    /**
     * 
     * @param sendTime YYYY-MM-DD hh:mm:ss
     * @param localFilePath 本地地址(文件名NBU002_9ebc1589-2e7b-4340-a3f7-a6109862a6e3.xls) 
     * @param ftpPath ftp约定上传地址 保存地址为ftpPath/YYYYMM/DD/文件名
     * @return ftp保存文件的完整地址
     * @throws IOException 
     */
    public String uploadEBFile(String ftpPath, String sendTime, String localFilePath,String fileName) throws IOException{
    	
    	
    	/**
		 * 1.创建ftp
		 * 2.给出上传目录
		 * 3.需要检测是否需要创建的目录如：201604/06
		 * 4.需要则创建
		 * 5.创建文件
		 * 6.NBU002_9ebc1589-2e7b-4340-a3f7-a6109862a6e3.xls   交易类型+前台交易UUID+.xls
		 */
    	
    	String dateStr = sendTime.split(" ")[0];
    	String[] dates = dateStr.split("-");
    	String YM_dirName = dates[0] + dates[1];
    	String YM_ftpPath = ftpPath + "/" + YM_dirName;
    	String D_dirName = dates[2];
    	String D_ftpPath = YM_ftpPath + "/" + D_dirName;
    	
    	if(this.existDirectory(ftpPath, YM_dirName) == false)
    	{//年月目录不存在
    		this.createDirectory(YM_ftpPath);
    		this.createDirectory(D_ftpPath);
    	}else{
    		if(this.existDirectory(YM_ftpPath, D_dirName) == false){
    			this.createDirectory(D_ftpPath);
    		}
    	}
    	ftpClient.changeWorkingDirectory(D_ftpPath);//切换到对应的ftp目录中
    	this.uploadFile(localFilePath,fileName);//文件上传
    	return D_ftpPath + "/" + fileName;
    }
    
    
    /**
     * 	下载文件
     * @param directoryString
     * @return
     */
    public  boolean  downLoadFile(String directoryString,String targetPath){
	
        boolean success = false;    
        String fileName = directoryString.substring(directoryString  
                .lastIndexOf("/") + 1);  
        String remotePath = directoryString.substring(1, directoryString  
                .lastIndexOf("/"));  
            try {    
                this.ftpClient.changeWorkingDirectory(remotePath);//转移到FTP服务器目录    
                FTPFile[] fs = ftpClient.listFiles(remotePath);    
                for(FTPFile ff:fs){    
                    if(ff.getName().equals(fileName)){    
                            
                        File localFile = new File(targetPath+"/");  
                        if (!localFile.exists()) {  
                            localFile.mkdirs();  
                        }  
                        OutputStream is = new FileOutputStream(localFile+"/"+ff.getName());    
                        success =  ftpClient.retrieveFile(ff.getName(), is);    
                        is.close();    
                    }    
                }    
                ftpClient.logout();    
            } catch (IOException e) {    
                e.printStackTrace();    
            } finally {    
                if (ftpClient.isConnected()) {    
                    try {    
                    	ftpClient.disconnect();    
                    } catch (IOException ioe) {    
                    }    
                }    
            }    
        return success;    
    }
}