package com.common.utils.register;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.Hashtable;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * 	判断 传入的字符串 是否符合特定的格式
 * 
 * @author 王明 
 * @date 2018-11-18
 *
 */
public class FieldValidationUtil {  
	
    /**
     *	 根据验证类型 判断传入的字符串是否符合规则
     * @author wangming
     * 
     * 0	非空 
     * 1	手机号
     * 2	验证固定电话
     * 3	日期(支持 yyyy-MM-dd和yyyyMMdd)
     * 4	邮箱
     * 4	身份证号
     * 6	姓名
     * 7	验证英文名
     * 8	邮编
     * 9	验证出生日期
     * 10	数字
     * 11	小数点后两位的数字  
     * 12	护照
     * 13	企业姓名
     * 14	企业证件号
     * 15	台湾居民来往大陆通行证,港澳同胞回乡证
     * 16	校验军官证
     * 17	校验出生证明
     * 18	校验港澳台居民居住证
     * @return  boolean 正确返回 true 否则 false
     */
    public static boolean vailStringByType(int authType,String value){
    	
    	switch(authType){
    	       // 验证非空
    	       case 0:{
    	    	 if(null==value){
    	    		 return false;
    	    	 } else{
    	    		 if("".equals(value.trim())){
    	    			 return false;
    	    		 }else{
    	    			 return true;
    	    		 }
    	    	 } 
    	       }
    	       // 验证手机号  
    	       case 1:{
    	    	   Pattern p = null;  
    	           Matcher m = null;  
    	           p = Pattern.compile(ValidationPropertiesUtil.getProperty("mobile")); // 验证手机号  
    	           m = p.matcher(value);  
    	           boolean b = m.matches();   
    	           return b; 
    	       }
    	       //验证固定电话
	     		case 2:{ 	
	 	    	     Pattern reg= Pattern.compile(ValidationPropertiesUtil.getProperty("tel"));
	 	   	    	 Matcher m = reg.matcher(value);
	 	   	    	 boolean b = m.matches();   
	 	 	    	 return b;
	     		}
    	       // 验证日期
    	       case 3:{
    	    	   value=value.replaceAll("/","").replaceAll("-","");
    	    	   if(value!=null&&!"".equals(value.trim()) && value.length()==8){
    					SimpleDateFormat sdf2=new SimpleDateFormat("yyyyMMdd");//小写的mm表示的是分钟  
    					try {
    						sdf2.setLenient(false);
    						sdf2.parse(value);
	    					return true;
    					} catch (ParseException e1) {
    						e1.printStackTrace();
    						return false;
    					}
    	    	    }else{
    	    	    	return false;
    	    	    }
    	       }
    	       // 验证 邮箱
    	       case 4:{
    	    	   Pattern reg= Pattern.compile(ValidationPropertiesUtil.getProperty("email"));
    	    	   Matcher m = reg.matcher(value);
    	    	   boolean b = m.matches();   
	    	       return b;
    	       }
    	       // 验证 身份证号
    	       case 5:{
    	    	   boolean b = vailIdCardNum(value);
    	    	   return b;
    	       }
    	       // 验证姓名类型
    	       case 6:{
    	    	   Pattern reg= Pattern.compile(ValidationPropertiesUtil.getProperty("name"));
    	    	   Matcher m = reg.matcher(value);
    	    	   boolean b = m.matches();   
	    	       return b;
    	       }
    	       //验证英文名
	     		case 7:{
	     			 Pattern reg= Pattern.compile(ValidationPropertiesUtil.getProperty("ename"));
	     	    	 Matcher m = reg.matcher(value);
	     	    	 boolean b = m.matches();   
	   	    	     return b;
	     		}
	     		//邮编
	     		case 8:{
	     			 Pattern reg= Pattern.compile(ValidationPropertiesUtil.getProperty("postCode"));
	      	    	 Matcher m = reg.matcher(value);
	      	    	 boolean b = m.matches();   
	      	    	 return b;
	     		}
	     		// 验证出生日期
    	       case 9:{
    	    	   value=value.replaceAll("/","").replaceAll("-","");
    	    	   if(value!=null&&!"".equals(value.trim()) && value.length()==8){
    					SimpleDateFormat sdf2=new SimpleDateFormat("yyyyMMdd");//小写的mm表示的是分钟  
    					SimpleDateFormat sdf=new SimpleDateFormat("yyyy-MM-dd");//小写的mm表示的是分钟  
    					try {
    						sdf2.setLenient(false);
    						sdf2.parse(value);
    						String strYear = value.substring(0, 4);// 年份     
	    				    String strMonth = value.substring(4, 6);// 月份     
	    				    String strDay = value.substring(6, 8);// 月份     
	    				    if (isDataFormat(strYear + "-" + strMonth + "-" + strDay) == false) {     
	    				         return false;     
	    				     }     
	    				     GregorianCalendar gc = new GregorianCalendar();     
	    				     
	    					 if ((gc.get(Calendar.YEAR) - Integer.parseInt(strYear)) > 200    
	    					         || (gc.getTime().getTime() - sdf.parse( strYear + "-" + strMonth + "-" + strDay).getTime()) < 0) {     
	    					     return false;     
	    					 }
	    					 
	    				     if (Integer.parseInt(strMonth) > 12 || Integer.parseInt(strMonth) == 0) {     
	    				         return false;     
	    				     }     
	    				     if (Integer.parseInt(strDay) > 31 || Integer.parseInt(strDay) == 0) {     
	    				         return false;     
	    				     }     
	    				     // =====================(end)=====================     
	    					return true;
    					} catch (ParseException e1) {
    						e1.printStackTrace();
    						return false;
    					}
    	    	    }else{
    	    	    	return false;
    	    	    }
	    	    }
    	       // 验证 数字
    	       case 10:{
    	    	   Pattern reg= Pattern.compile(ValidationPropertiesUtil.getProperty("num"));
    	    	   Matcher m = reg.matcher(value);
    	    	   boolean b = m.matches();   
	    	       return b;
    	       }
    	       //小数点后两位的数字
    	       case 11:{
    	    	   Pattern reg= Pattern.compile(ValidationPropertiesUtil.getProperty("floatCheck"));
    	    	   Matcher m = reg.matcher(value);
    	    	   boolean b = m.matches();   
    	    	   return b;
    	       }
    	       // 验证 护照
    	       case 12:{
    	    	   boolean b = checkPassportNumber(value);   
	    	       return b;
    	       }
	     		//企业姓名
	     		case 13:{
	     			 Pattern reg= Pattern.compile(ValidationPropertiesUtil.getProperty("firmName"));
	      	    	 Matcher m = reg.matcher(value);
	      	    	 boolean b = m.matches();   
	      	    	 return b;
	     		}
	     		//企业证件号
	     		case 14:{
	     			 Pattern reg= Pattern.compile(ValidationPropertiesUtil.getProperty("idCardNumber"));
	      	    	 Matcher m = reg.matcher(value);
	      	    	 boolean b = m.matches();   
	      	    	 return b;
	     		}
	     		//台湾居民来往大陆通行证,港澳同胞回乡证
	     		case 15:{
	     			 Pattern reg= Pattern.compile(ValidationPropertiesUtil.getProperty("chinaOtherCard"));
	      	    	 Matcher m = reg.matcher(value);
	      	    	 boolean b = m.matches();   
	      	    	 return b;
	     		}
	     		//校验军官证
	     		case 16:{
	     			 Pattern reg= Pattern.compile(ValidationPropertiesUtil.getProperty("officerNumber"));
	      	    	 Matcher m = reg.matcher(value);
	      	    	 boolean b = m.matches();   
	      	    	 return b;
	     		}
	     		//校验出生证明
	     		case 17:{
	     			 Pattern reg= Pattern.compile(ValidationPropertiesUtil.getProperty("birthCard"));
	      	    	 Matcher m = reg.matcher(value);
	      	    	 boolean b = m.matches();   
	      	    	 return b;
	     		}
	     		//校验港澳台居民居住证
	     		case 18:{
	     			 Pattern reg= Pattern.compile(ValidationPropertiesUtil.getProperty("gatNumber"));
	      	    	 Matcher m = reg.matcher(value);
	      	    	 boolean b = m.matches();   
	      	    	 return b;
	     		}
	     		//小数点后两位的数字
    	       case 19:{
    	    	   Pattern reg= Pattern.compile(ValidationPropertiesUtil.getProperty("salryCheck"));
    	    	   Matcher m = reg.matcher(value);
    	    	   boolean b = m.matches();   
	    	       return b;
    	       }
    	       default:{
                  return false;
               }
    	}
    }
    
    /**
     * 验证身份证号
     * @param IDStr
     * @return
     * @throws NumberFormatException
     * @throws ParseException
     */
    public static boolean vailIdCardNum(String IDStr){
    	 try {
			 if(IDStr==null){
				 return false;
			 }else{
				 IDStr = IDStr.toLowerCase();
			 }
		     String[] ValCodeArr = { "1", "0", "x", "9", "8", "7", "6", "5", "4",     
		             "3", "2" };     
		     String[] Wi = { "7", "9", "10", "5", "8", "4", "2", "1", "6", "3", "7",     
		             "9", "10", "5", "8", "4", "2" };     
		     String Ai = "";
		     // ================ 号码的长度 15位或18位 ================     
		     if (IDStr.length() != 15 && IDStr.length() != 18) {     
		         return false;     
		     }     
		 
		     // ================ 数字 除最后以为都为数字 ================     
		     if (IDStr.length() == 18) {     
		         Ai = IDStr.substring(0, 17);     
		     } else if (IDStr.length() == 15) {     
		         Ai = IDStr.substring(0, 6) + "19" + IDStr.substring(6, 15);     
		     }     
		     if (vailStringByType(8,Ai) == false) { 
		    	 return false;    
		     }     
		     // =======================(end)========================     
		 
		     // ================ 出生年月是否有效 ================     
		     String strYear = Ai.substring(6, 10);// 年份     
		     String strMonth = Ai.substring(10, 12);// 月份     
		     String strDay = Ai.substring(12, 14);// 月份     
		     if (isDataFormat(strYear + "-" + strMonth + "-" + strDay) == false) {     
		         return false;     
		     }     
		     GregorianCalendar gc = new GregorianCalendar();     
		     SimpleDateFormat s = new SimpleDateFormat("yyyy-MM-dd");     
		     
			 if ((gc.get(Calendar.YEAR) - Integer.parseInt(strYear)) > 200    
			         || (gc.getTime().getTime() - s.parse( strYear + "-" + strMonth + "-" + strDay).getTime()) < 0) {     
			     return false;     
			 }
			 
		     if (Integer.parseInt(strMonth) > 12 || Integer.parseInt(strMonth) == 0) {     
		         return false;     
		     }     
		     if (Integer.parseInt(strDay) > 31 || Integer.parseInt(strDay) == 0) {     
		         return false;     
		     }     
		     // =====================(end)=====================     
		 
		     // ================ 地区码时候有效 ================     
		     Hashtable<String, String> h = GetAreaCode();     
		     if (h.get(Ai.substring(0, 2)) == null) {     
		         return false;     
		     }     
		     // ==============================================     
		 
		     // ================ 判断最后一位的值 ================     
		     int TotalmulAiWi = 0;     
		     for (int i = 0; i < 17; i++) {     
		         TotalmulAiWi = TotalmulAiWi     
		                 + Integer.parseInt(String.valueOf(Ai.charAt(i)))     
		                 * Integer.parseInt(Wi[i]);     
		     }     
		     int modValue = TotalmulAiWi % 11;     
		     String strVerifyCode = ValCodeArr[modValue];     
		     Ai = Ai + strVerifyCode;     
		 
		     if (IDStr.length() == 18) {   
		          if (!Ai.equals(IDStr)) {     
		              return false;     
		          }     
		      } else {     
		          return true;     
		      }     
		      // =====================(end)=====================     
		      return  true;   
		      
         } catch (NumberFormatException e) {
 			// TODO Auto-generated catch block
 			e.printStackTrace();
 		 } catch (ParseException e) {
 			// TODO Auto-generated catch block
 			e.printStackTrace();
 		 }
		return false;
    	
    }
    /**   
     * 	功能：设置地区编码   
     * @return Hashtable 对象   
     */    
    private static Hashtable<String, String> GetAreaCode() {     
        Hashtable<String, String> hashtable = new Hashtable<String, String>();     
        hashtable.put("11", "北京");     
        hashtable.put("12", "天津");     
        hashtable.put("13", "河北");     
        hashtable.put("14", "山西");     
        hashtable.put("15", "内蒙古");     
        hashtable.put("21", "辽宁");     
        hashtable.put("22", "吉林");     
        hashtable.put("23", "黑龙江");     
        hashtable.put("31", "上海");     
        hashtable.put("32", "江苏");     
        hashtable.put("33", "浙江");     
        hashtable.put("34", "安徽");     
        hashtable.put("35", "福建");     
        hashtable.put("36", "江西");     
        hashtable.put("37", "山东");     
        hashtable.put("41", "河南");     
        hashtable.put("42", "湖北");     
        hashtable.put("43", "湖南");     
        hashtable.put("44", "广东");     
        hashtable.put("45", "广西");     
        hashtable.put("46", "海南");     
        hashtable.put("50", "重庆");     
        hashtable.put("51", "四川");     
        hashtable.put("52", "贵州");     
        hashtable.put("53", "云南");     
        hashtable.put("54", "西藏");     
        hashtable.put("61", "陕西");     
        hashtable.put("62", "甘肃");     
        hashtable.put("63", "青海");     
        hashtable.put("64", "宁夏");     
        hashtable.put("65", "新疆");     
        hashtable.put("71", "台湾");     
        hashtable.put("81", "香港");     
        hashtable.put("82", "澳门");     
        hashtable.put("91", "国外");     
        return hashtable;     
    } 
    /**
	 * @desc 校验护照
	 * @param value
	 * @return boolean
	 */
	private static boolean checkPassportNumber(String value) {
		Pattern reg= Pattern.compile(ValidationPropertiesUtil.getProperty("passportNumber"));
		Matcher m = reg.matcher(value);
		boolean b = m.matches();
		return b;
	}
    
    /**验证日期字符串是否是YYYY-MM-DD格式 
     * @param str 
     * @return 
     */  
	private static boolean isDataFormat(String str){  
	   boolean flag=false;
	   String regxStr=ValidationPropertiesUtil.getProperty("dateFormat");  
	   Pattern pattern1=Pattern.compile(regxStr);  
	   Matcher isNo=pattern1.matcher(str);  
	   if(isNo.matches()){  
		   flag=true;  
	   }  
	   return flag;  
	}  
	
	//验证出生日期是否在当前日期之前
	public static boolean validationBirthDate(Date birthDate){
		Date date =new Date();
		int number=date.compareTo(birthDate);
		if(number>0){
			return true;
		}else{
			return false;
		}
	}
	
	/**
	 * 
	 * @param str 字符串
	 * @param flag 1 过滤全部字符串  、 2 过滤收尾字符串 
	 * @return
	 */
	public static String trim(String str,Integer flag){
		if(flag == 1){
			return str.replaceAll(" ", "");
		}else if(flag == 2){
			return str.trim();
		}
		return str;
	}
	
	/**
     * 	判断字符串中是否包含中文
     * @param str
     * 	待校验字符串
     * @return 是否为中文
     * @warn 不能校验是否为中文标点符号
     */
    public static boolean isContainChinese(String str) {
        Pattern p = Pattern.compile(ValidationPropertiesUtil.getProperty("is_chinese"));
        Matcher m = p.matcher(str);
        if (m.find()) {
            return true;
        }
        return false;
    }
  
    /**
     * 	过滤后空格的字符串
     * @param str
     * @return
     */
    public static String getAfterTrimStr(String str){
    	if(str!=null&&!"".equals(str)){
        	if(isContainChinese(str)){
        		return trim(str,1);
        	}else{
        		return trim(str,2);
        	}
    	}
		return "";
    }
	
    
    /**
     *	 身份证号的特殊字符转化
     * @param str
     * @return
     */
    public static String getIdCardNumberStr(String str){
    	if(str!=null&&!"".equals(str)){
    		return str.toUpperCase().replace("×", "X").replace("x", "X");
    		
    	}
		return "";
    }
    
    /**
     * 	证件有效期判断转换
     */
    public static String cardDate(String idStartDate , String idEndDate,String isLongterm){
    	String cardDateString="";
    	if(idStartDate=="" || idStartDate==null){
    		return cardDateString;
    	}
    	if("1".equals(isLongterm) || "".equals(idEndDate) || idEndDate == null) {
    		cardDateString = idStartDate + "-" +"长期"; 
    	}else {
    		cardDateString = idStartDate + "-" + idEndDate;
    	}
    	return cardDateString;
    }
    
}
