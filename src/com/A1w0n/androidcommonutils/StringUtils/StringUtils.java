package com.A1w0n.androidcommonutils.StringUtils;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import android.text.TextUtils;

public class StringUtils {
	
	/**
	 * 返回String字符串中，第一个完整的数字
	 * 
	 * 比如：Str98 就返回 98 比如：Str87uyuy232 就返回 87
	 * 
	 * @param target
	 * @return 出错的话返回 -1
	 * @author A1w0n
	 */
	public static int getFirstIntFromString(String target) {
		int result = -1;

		if (!TextUtils.isEmpty(target)) {
			Matcher matcher = Pattern.compile("\\d+").matcher(target);
			matcher.find();
			result = Integer.valueOf(matcher.group());
		}

		return result;
	}

}
