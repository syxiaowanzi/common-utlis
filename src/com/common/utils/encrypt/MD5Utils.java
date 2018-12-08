package com.common.utils.encrypt;

import java.nio.charset.Charset;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
/**
 * 根据字符串md5加密
 * 字符串长度小于1024*2
 * @author wangming
 *
 */
public class MD5Utils {
	private static final int MD5_LENGTH = 1024 * 2;//1024 * 2 - 512
	public static String generateMD5(String plainText) {
		try {
			if(null == plainText || plainText.length() == 0) {
				return null;
			}
			plainText = (plainText.length() > MD5_LENGTH)?  plainText.substring(0, MD5_LENGTH) : plainText;
			MessageDigest md = MessageDigest.getInstance("MD5");
			byte[] bytes = plainText.getBytes(Charset.forName("UTF-8"));
			md.update(bytes);
			byte b[] = md.digest();
			int i;
			StringBuffer buf = new StringBuffer("");
			for (int offset = 0, j = b.length; offset < j; offset++) {
				i = b[offset];
				if (i < 0) {
					i += 256;
				}
				if (i < 16) {
					buf.append("0");
				}
				buf.append(Integer.toHexString(i));
			}
			return buf.toString().toLowerCase();
		} catch (NoSuchAlgorithmException e) {
			e.printStackTrace();
			return null;
		}
	}	
}