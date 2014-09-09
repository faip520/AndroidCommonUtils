package com.A1w0n.androidcommonutils.ThreadUtils;

import android.os.Looper;
import android.os.SystemClock;

public class ThreadUtils {
	
	private boolean mTest;

	private ThreadUtils() {
	}

	public static boolean isUiThread() {
		return Thread.currentThread().getId() == Looper.getMainLooper().getThread().getId();
	}
	
	/**
	 * 如果等到了期待的结果就返回True，如果超时了就返回false
	 * @return
	 */
	private boolean waitUntilOKOrTimeout() {
		int count = 0;
		while (mTest) {
			SystemClock.sleep(1000);
			count++;
			if (count == 10) {
				return false;
			}
		}
		return true;
	}
	
}
