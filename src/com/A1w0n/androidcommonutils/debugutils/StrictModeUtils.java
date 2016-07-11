package com.A1w0n.androidcommonutils.DebugUtils;

import android.annotation.TargetApi;
import android.os.Build;
import android.os.Build.VERSION_CODES;
import android.os.StrictMode;

/**
 * Level 11 之后才引入的StrictMode
 * @author A1w0n
 */
public class StrictModeUtils {

	private StrictModeUtils() {
	}

	/**
	 * Remember to setClassInstanceLimit！！！！！！！！！！！！！！！！！！
	 * Note : class instance limit counting is just for information. Don't care too much about this.
	 */
	@TargetApi(Build.VERSION_CODES.HONEYCOMB)
	public static void enableStrictMode() {
		if (StrictModeUtils.hasGingerbread()) {
			StrictMode.ThreadPolicy.Builder threadPolicyBuilder = new StrictMode.ThreadPolicy.Builder().detectAll().penaltyLog();
			StrictMode.VmPolicy.Builder vmPolicyBuilder = new StrictMode.VmPolicy.Builder().detectAll().penaltyLog();

			if (StrictModeUtils.hasHoneycomb()) {
				threadPolicyBuilder.penaltyFlashScreen();
				// Will give a log when instance count bigger than given number.
				// vmPolicyBuilder.setClassInstanceLimit(ImageGridActivity.class, 1)
				// .setClassInstanceLimit(ImageDetailActivity.class, 1);
			}
			StrictMode.setThreadPolicy(threadPolicyBuilder.build());
			StrictMode.setVmPolicy(vmPolicyBuilder.build());
		}
	}

	public static boolean hasFroyo() {
		// Can use static final constants like FROYO, declared in later versions
		// of the OS since they are inlined at compile time. This is guaranteed behavior.
		return Build.VERSION.SDK_INT >= VERSION_CODES.FROYO;
	}

	public static boolean hasGingerbread() {
		return Build.VERSION.SDK_INT >= VERSION_CODES.GINGERBREAD;
	}

	public static boolean hasHoneycomb() {
		return Build.VERSION.SDK_INT >= VERSION_CODES.HONEYCOMB;
	}

	public static boolean hasHoneycombMR1() {
		return Build.VERSION.SDK_INT >= VERSION_CODES.HONEYCOMB_MR1;
	}

	public static boolean hasJellyBean() {
		return Build.VERSION.SDK_INT >= VERSION_CODES.JELLY_BEAN;
	}

	public static boolean hasKitKat() {
		return Build.VERSION.SDK_INT >= VERSION_CODES.KITKAT;
	}

}
