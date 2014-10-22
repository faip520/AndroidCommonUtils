package com.A1w0n.androidcommonutils;

import java.util.Date;

import android.content.Context;
import android.text.format.DateFormat;

public class TimeUtils {

	private TimeUtils() {
	}
	
	/**
	 * 获取当然时间，输出例子 10:12，会自动根据系统当前设置，
	 * 决定用12小时制还是24小时制
	 * @return
	 */
	public static String getHourAndMinute(Context context) {
		Date d = new Date();
		
		if (is24HourFormat(context)) {
			return (String)DateFormat.format("kk:mm", d.getTime());
		} else {
			return (String)DateFormat.format("hh:mm", d.getTime());
		}
	}
	
	/**
	 * 输出样例：2012-06-06
	 */
	public static CharSequence getDateWithoutTime() {
		return android.text.format.DateFormat.format("yyyy-MM-dd", new java.util.Date());
	}
	
	/**
	 * 判断系统是12小时制还是24小时制
	 */
	public static boolean is24HourFormat(Context context) {
		return DateFormat.is24HourFormat(context);
	}
	

}
