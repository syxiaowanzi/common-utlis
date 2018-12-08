package com.common.utils.opslab.util;

/**
 * 获取double类型数据的小数长度
 */
public class ArrayUtil {

    /**
     * 获取一个double类型的数字的小数位有多长
     * @param dd
     * @return
     */
    public static int doueleBitCount(double dd){
        String temp = String.valueOf(dd);
        int i = temp.indexOf(".");
        if(i > -1){
            return temp.length()-i -1;
        }
        return 0;

    }
    
    /**
     * 获取一个double类型的数字的小数位有多长
     * @param arr double类型的数组
     * @return integer类型长度值
     */
    public static Integer[] doubleBitCount(double[] arr){
        Integer[] len = new Integer[arr.length];
        for (int i = 0; i < arr.length; i++) {
            len[i] = doueleBitCount(arr[i]);
        }
        return len;
    }
    
    public static void main(String[] args) {
		//double salary = 1343.3232;
		//System.out.println(doueleBitCount(salary));
		
		double[] salarys = {1223.233333333,3432.343243};
		Integer[] sal = doubleBitCount(salarys);
		for(int a : sal) {
			System.out.println(a);
		}
	}
}
