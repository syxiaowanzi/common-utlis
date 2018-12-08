package com.common.utils.string;

/**
 * 	字符串工具类
 * @author wangming
 *	1	isEmpty()		为空或者null
 *	2	isNotEmpty()	不为空
 *	3	parseStringInt()	String转换成int
 *	4	parseStringLong()	String转换成long
 *	5	parseStringArray()	切分string成数组
 */
public class StringUtil {
	
    /**
     * 	判断是否为空字符串最优代码
     * @param str
     * @return 如果为空，则返回true
     */
    public static boolean isEmpty(String str){
        return str == null || str.trim().length() == 0;
    }

    /**
     *	 判断字符串是否非空
     * @param str 如果不为空，则返回true
     * @return
     */
    public static boolean isNotEmpty(String str){
        return !isEmpty(str);
    }
    /**
     * 	将String类型转换成int类型
     * @param str
     * @return
     */
    public static int parseStringInt(String str) {
    	return Integer.parseInt(str);
    }
    /**
     * 	将String类型数据转换成long类型
     * @param str
     * @return
     */
    public static long parseStringLong(String str) {
    	return Long.parseLong(str);
    }
    
    /**
     * 	根据规则将string转换成数组
     * @param str 字符串
     * @param regex 规则
     * @return 数组
     */
    public static String[] parseStringArray(String str,String regex) {
    	String [] arr = str.split(regex);
    	return arr;
    }
}
