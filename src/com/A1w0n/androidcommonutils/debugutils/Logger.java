package com.A1w0n.androidcommonutils.DebugUtils;

import com.A1w0n.androidcommonutils.FileUtils.ProjectFileManager;
import com.crashlytics.android.Crashlytics;

import java.io.File;

/**
 * Wrapper API for sending log output.
 */
public class Logger {

	protected static final String TAG = "===A1w0n===";
	
	public static final boolean DEBUG = true;

	private Logger() {
	}

	/**
	 * Send a VERBOSE log message.
	 * 
	 * @param msg
	 *            The message you would like logged.
	 */
	public static void v(String msg) {
		if (DEBUG)
			android.util.Log.v(TAG, buildMessage(msg));
	}

	/**
	 * Send a VERBOSE log message and log the exception.
	 * 
	 * @param msg
	 *            The message you would like logged.
	 * @param thr
	 *            An exception to log
	 */
	public static void v(String msg, Throwable thr) {
		if (DEBUG)
			android.util.Log.v(TAG, buildMessage(msg), thr);
	}

	/**
	 * Send a DEBUG log message.
	 * 
	 * @param msg
	 */
	public static void d(String msg) {
		if (DEBUG)
			android.util.Log.d(TAG, buildMessage(msg));
	}

	/**
	 * Send a DEBUG log message and log the exception.
	 * 
	 * @param msg
	 *            The message you would like logged.
	 * @param thr
	 *            An exception to log
	 */
	public static void d(String msg, Throwable thr) {
		if (DEBUG)
			android.util.Log.d(TAG, buildMessage(msg), thr);
	}

	/**
	 * Send an INFO log message.
	 * 
	 * @param msg
	 *            The message you would like logged.
	 */
	public static void i(String msg) {
		if (DEBUG)
			android.util.Log.i(TAG, buildMessage(msg));
	}

    /**
     * 可以用来做性能分析的
     *
     * 用法实例：
     *      williamPerformance(“Activity onCreate start”)
     *      williamPerformance(“Activity onCreate end”)
     *      williamPerformance(“Download file thread start”)
     *      williamPerformance(“Download file thread end”)
     *
     * 后面可以把机器的logcat日志内容dump出来(带上时间戳), 然后根据软件的流程日志，分析问题
     *
     * 也可以用来做日志系统，在软件的关键部位打印log，然后测试部门的同事提出问题的时候，
     * 把他们机器上的logcat内容dump出来，然后分析问题
     *
     * 经测试：不少机器的logcat保存了超过12个小时的logcat日志，这个用来解决测试部同事提出的问题
     * 已经足够
     *
     * @param msg
     */
	public static void e(String msg) {
		if (DEBUG)
			android.util.Log.e(TAG, buildMessage(msg));
	}

	/**
	 * Send a WARN log message
	 * 
	 * @param msg
	 *            The message you would like logged.
	 */
	public static void w(String msg) {
		if (DEBUG)
			android.util.Log.w(TAG, buildMessage(msg));
	}

    public static void logToFile(String msg) {
        String result = buildMessage(msg);


    }

	/**
	 * Building Message
	 * 
	 * @param msg
	 *            The message you would like logged.
	 * @return Message String
	 */
	protected static String buildMessage(String msg) {
		StackTraceElement caller = new Throwable().fillInStackTrace().getStackTrace()[2];

		return new StringBuilder().append("===A1W0n===").append(caller.getClassName()).append(".").append(caller.getMethodName()).append("(): ").append(msg)
				.toString();
	}

    private void addMessageToLogFile() {
        File log = ProjectFileManager.getLoggerFile();

    }
}
