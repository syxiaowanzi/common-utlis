package com.common.utils.test;

import java.util.Date;

import com.common.utils.encrypt.MD5Utils;
import com.common.utils.math.Arith;
import com.common.utils.register.FieldValidationUtil;
import com.common.utils.xml.ReadXmlFileToString;

public class Test {
	public static void main(String[] args) {
		//Arith test
//		System.out.println(Arith.add(12.67, 14.67));
//		System.out.println(Arith.div(10, 3, 3));
		
		//md5
		//System.out.println(MD5Utils.generateMD5("123"));
		
		//ReadXmlFileToString
		//System.out.println(ReadXmlFileToString.readXmlString("E:/test.xml"));
		
		//validation
//		System.out.println(FieldValidationUtil.vailStringByType(0, "123"));
//		System.out.println(FieldValidationUtil.vailStringByType(1, "15201042452"));
//		System.out.println(FieldValidationUtil.vailStringByType(2, "15201042"));
//		System.out.println(FieldValidationUtil.vailStringByType(4, "230622198908193552"));
//		System.out.println(FieldValidationUtil.vailStringByType(5, "wangming123"));
		String start_time = "12344444";
		long stime = Long.parseLong(start_time);
		System.out.println(stime);
		System.out.println(Long.valueOf(start_time));
		System.out.println(Long.getLong("java.version"));
		System.out.println(System.getProperty("java.version"));
		System.out.println(Long.decode("123") + Long.decode("123"));  // prints  246
		System.out.println(Long.parseLong("123") + Long.parseLong("123")); // same as above
		long time = new Date().getTime();
	}
}
