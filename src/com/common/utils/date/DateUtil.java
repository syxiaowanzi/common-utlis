package com.common.utils.date;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

/**
 * 
 * 	用于处理时间的工具类
 * @author wangming
 *
 */
public class DateUtil {
	/**
	 *	获取当前时间
	 *@param format 要格式化的类型，如yyyy-MM-dd HH:mm:ss---yyyy-MM-dd
	 *@return string类型时间
	 */
	public static String getCurrentDateString(String format){
//		Long time = System.currentTimeMillis();
		Calendar cl = Calendar.getInstance();
		Long time = cl.getTimeInMillis();
		//int times = time.intValue();//long转换成int
		SimpleDateFormat sdf=new SimpleDateFormat(format);
		String dataString = sdf.format(time);
		return dataString;
	}
	/**
	 * 	获取当前时间
	 * @return date类型时间
	 */
	public static Date getCurrentData() {
		Calendar c = Calendar.getInstance();
		Date date = c.getTime();
		return date;
	}
	
	/**
	 * 	获取昨天的日期
	 */
	public static String getAccountDate(){
		Date date = new Date();
		Calendar c=Calendar.getInstance();
		c.setTime(date);
		c.add(Calendar.DATE,-1);
		date = c.getTime();
		SimpleDateFormat sdf=new SimpleDateFormat("yyyy-MM-dd");
		String datestr = sdf.format(date);
		return datestr;
	}
	/**
	 * 	获取给定日期前一天的日期
	 */
	public static String getAccountDate(Date date){
		Calendar c=Calendar.getInstance();
		c.setTime(date);
		c.add(Calendar.DATE,-1);
		date = c.getTime();
		SimpleDateFormat sdf=new SimpleDateFormat("yyyy-MM-dd");
		String datestr = sdf.format(date);
		return datestr;
	}
	/**
	 * 	获得当天某个时间的毫秒数
	 * @param hour 小时
	 * @param second 分钟
	 * @param minute 秒数
	 * @param millisecode 毫秒
	 */
	public static long getTimesmorning(int hour,int second,int minute,int millisecode){
		Calendar cal = Calendar.getInstance();
		cal.set(Calendar.HOUR_OF_DAY, hour);//获取小时
		cal.set(Calendar.SECOND, second);//获取分钟
		cal.set(Calendar.MINUTE, minute);//获取秒
		cal.set(Calendar.MILLISECOND, millisecode);//获取毫秒
		return	cal.getTimeInMillis();
	}
	/**
	 * 	获得本周一0点时间
	 */
	public static long getTimesWeekmorning(){
		Calendar cal = Calendar.getInstance();
		cal.set(cal.get(Calendar.YEAR),cal.get(Calendar.MONDAY), cal.get(Calendar.DAY_OF_MONTH), 0, 0,0);
		cal.set(Calendar.DAY_OF_WEEK, Calendar.MONDAY);
		return	cal.getTimeInMillis();
	}
	/**
	 * 	获得本周日24点时间
	 */
	public static long getTimesWeeknight(){
		Calendar cal = Calendar.getInstance();
		cal.set(cal.get(Calendar.YEAR),cal.get(Calendar.MONDAY), cal.get(Calendar.DAY_OF_MONTH), 0, 0,0);
		cal.set(Calendar.DAY_OF_WEEK, Calendar.MONDAY);
		return	cal.getTime().getTime()+ (7 * 24 * 60 * 60 * 1000);
	}
	/**
	 * 	获得本月第一天0点时间
	 */
	public static long getTimesMonthmorning(){
		Calendar cal = Calendar.getInstance();
		cal.set(cal.get(Calendar.YEAR),cal.get(Calendar.MONDAY), cal.get(Calendar.DAY_OF_MONTH), 0, 0,0);
		cal.set(Calendar.DAY_OF_MONTH,cal.getActualMinimum(Calendar.DAY_OF_MONTH));
		return	cal.getTimeInMillis();
	}
	/**
	 * 	获得本月最后一天24点时间
	 */
	public static long getTimesMonthnight(){
		Calendar cal = Calendar.getInstance();
		cal.set(cal.get(Calendar.YEAR),cal.get(Calendar.MONDAY), cal.get(Calendar.DAY_OF_MONTH), 0, 0,0);
		cal.set(Calendar.DAY_OF_MONTH,cal.getActualMaximum(Calendar.DAY_OF_MONTH));
		cal.set(Calendar.HOUR_OF_DAY, 24);
		return	cal.getTimeInMillis();
	}
	
}
