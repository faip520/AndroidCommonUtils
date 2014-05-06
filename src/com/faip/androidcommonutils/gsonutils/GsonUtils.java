package com.faip.androidcommonutils.gsonutils;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;

public class GsonUtils {

	private GsonUtils() {
	}
	
	/**
	 * Give a path and this will return a InputStreamReader with which you can call gson.fromJson(inputStreamReader, class)
	 * This function will handle all unicode character(including chinese).
	 */
	public static InputStreamReader getFileReaderForGsonHandlingUnicode(String path) throws FileNotFoundException {
		InputStreamReader isr = null;
		try {
			isr = new InputStreamReader(new FileInputStream(path), "GB2312");
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
		return isr;
	}

}
