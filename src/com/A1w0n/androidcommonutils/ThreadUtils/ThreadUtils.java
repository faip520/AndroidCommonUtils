package com.A1w0n.androidcommonutils.ThreadUtils;

import android.app.ActivityManager;
import android.content.Context;
import android.os.Looper;
import android.os.SystemClock;

import java.util.Map;
import java.util.Set;

public class ThreadUtils {
	
	private boolean mTest;

	private ThreadUtils() {
	}
	
	/**
	 * 获取apk所在进程的名字
	 * @param context
	 * @return
	 */
	public static  String getCurProcessName(Context context) {
		int pid = android.os.Process.myPid();
		ActivityManager mActivityManager = (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
		for (ActivityManager.RunningAppProcessInfo appProcess : mActivityManager.getRunningAppProcesses()) {
			if (appProcess.pid == pid) {
				return appProcess.processName;
			}
		}
		return "";
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

    /**
     * 获取当前apk程序，所在的进程的所有线程的信息
     */
    public static void getAllThread() {
        Map<Thread, StackTraceElement[]> map = Thread.getAllStackTraces();
        Set<Thread> threadSet = map.keySet();
        Thread[] threadArray = threadSet.toArray(new Thread[threadSet.size()]);
    }
	
}
