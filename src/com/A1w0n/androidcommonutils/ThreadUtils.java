package com.A1w0n.androidcommonutils;

import android.os.Looper;

public class ThreadUtils {

	private ThreadUtils() {
	}

	public static boolean isUiThread() {
		return Thread.currentThread().getId() == Looper.getMainLooper().getThread().getId();
	}
	
}
