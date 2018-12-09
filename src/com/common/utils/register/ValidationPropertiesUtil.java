package com.common.utils.register;

import java.io.IOException;
import java.util.Properties;

/**
 * @author wangming
 * @version
 * 	读取获取配置文件相关信息
 * */
public class ValidationPropertiesUtil {
	private static Properties  p = System.getProperties();
	private static ValidationPropertiesUtil propertiesUtil = null;
	private ValidationPropertiesUtil(){
		try {
			p.load(ValidationPropertiesUtil.class.getResourceAsStream("validation.properties"));
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public static String getProperty(String key){
		if(propertiesUtil == null){
			propertiesUtil = new ValidationPropertiesUtil();
		}
		return p.getProperty(key);
	}
	
	public static void clear(){
		p.clear();
	}
}
