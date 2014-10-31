package com.A1w0n.androidcommonutils;

import java.util.Calendar;
import java.util.Date;

import android.content.Context;
import android.text.format.DateFormat;

public class TimeUtils {

	private TimeUtils() {
	}

	/**
	 * 获取当然时间，输出例子 10:12，会自动根据系统当前设置， 决定用12小时制还是24小时制
	 * 
	 * @return
	 */
	public static String getHourAndMinute(Context context) {
		Date d = new Date();

		if (is24HourFormat(context)) {
			return (String) DateFormat.format("kk:mm", d.getTime());
		} else {
			return (String) DateFormat.format("hh:mm", d.getTime());
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

	/**
	 * 获取当前年份
	 */
	public static int getCurrentYear(Context context) {
		return Calendar.getInstance().get(Calendar.YEAR);
	}

	/**
	 * 根据日期，返回星座
	 * 
	 * @param month
	 * @param day
	 * @return
	 */
	private String getStar(int month, int day) {
		String[] starArr = { "魔羯座", "水瓶座", "双鱼座", "牡羊座", "金牛座", "双子座", "巨蟹座", "狮子座", "处女座", "天秤座", "天蝎座", "射手座" };
		int[] DayArr = { 22, 20, 19, 21, 21, 21, 22, 23, 23, 23, 23, 22 }; // 两个星座分割日
		int index = month;
		// 所查询日期在分割日之前，索引-1，否则不变
		if (day < DayArr[month - 1]) {
			index = index - 1;
		}
		// 返回索引指向的星座string
		return starArr[index];
	}
}
