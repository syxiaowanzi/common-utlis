package com.common.utils.string;

import java.io.UnsupportedEncodingException;
/**
 * blob类型转为字符串
 * @author wangming
 */
public class CastUediterString {
	
	public static  String castByteToString(byte[] blobByte,String charSet) {
		String str = "";
		if(blobByte!=null){
			try {
				str = new String(blobByte,charSet);
			} catch (UnsupportedEncodingException e) {
				e.printStackTrace();
				throw new RuntimeException("BOLB转换字符集不支持");
			}
		}
		//str = str.replace("/RBAC/upload", "/eb/uplaod");
		//System.out.println(str);
		return str;
	}
	
	public static void main(String args[]){
		
		System.out.println("125413541<img src=\"/RBAC/upload/ueditor/jsp/upload/image/20160511/1462931347850096564.jpg\" title=\"1462931347850096564.jpg\"".replace("/RBAC/upload", "/eb/RBAC"));
		
	}
}
