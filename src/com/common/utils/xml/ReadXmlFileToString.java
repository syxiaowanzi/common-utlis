package com.common.utils.xml;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;

/**
 * 	读取xml数据
 * @author wangming
 * return String 类型数据
 */
public class ReadXmlFileToString {
	
	public static String readXmlString(String filePath)
	{
	    String str="";
	    File file = new File(filePath);
	    try {
	        FileInputStream in = new FileInputStream(file);
	        // size  为字串的长度 ，这里一次性读完
	        int size=in.available();
	        byte[] buffer=new byte[size];
	        in.read(buffer);
	        in.close();
	        str=new String(buffer,"UTF-8");
	    } catch (IOException e) {
	        // TODO Auto-generated catch block
	        e.printStackTrace();
	        return null;
	    }
	    return str;
	}
}
