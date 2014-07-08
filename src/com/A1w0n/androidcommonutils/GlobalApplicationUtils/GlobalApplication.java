package com.A1w0n.androidcommonutils.GlobalApplicationUtils;

import android.app.Activity;
import android.app.Application;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager.NameNotFoundException;
import android.os.Handler;
import android.support.v4.app.FragmentManager;
import android.util.DisplayMetrics;
import android.view.Display;
import android.view.WindowManager;

import com.A1w0n.androidcommonutils.debugutils.StrictModeUtils;
import com.crashlytics.android.Crashlytics;
import com.faip.androidcommonutils.BuildConfig;

/**
 * Global singleton applicatioin subclass.
 */
public final class GlobalApplication extends Application {

	// Singleton.
	private static GlobalApplication mGlobalApplication = null;

	// Record the current running activity.
	private Activity activity = null;

	private Activity currentRunningActivity = null;

	private DisplayMetrics displayMetrics = null;

	private Handler handler = new Handler();

	@Override
	public void onCreate() {
		super.onCreate();
		mGlobalApplication = this;
		Crashlytics.start(this);

		if (BuildConfig.DEBUG) {
			// Remeber to set class instance limit inside this api.
			StrictModeUtils.enableStrictMode();
			// 显示系统的一切的Fragment相关的操作信息
			FragmentManager.enableDebugLogging(true);
		}
	}

	public static GlobalApplication getInstance() {
		return mGlobalApplication;
	}

	public Handler getUIHandler() {
		return handler;
	}

	public DisplayMetrics getDisplayMetrics() {
		if (displayMetrics != null) {
			return displayMetrics;
		} else {
			DisplayMetrics dm = new DisplayMetrics();
			WindowManager windowManager = (WindowManager) getSystemService(WINDOW_SERVICE);
			windowManager.getDefaultDisplay().getMetrics(dm);
			this.displayMetrics = dm;
			return dm;
		}
	}
	
	public String getApplicationVersionName() {
		PackageInfo pInfo = null;
		try {
			pInfo = getPackageManager().getPackageInfo(getPackageName(), 0);
		} catch (NameNotFoundException e) {
			e.printStackTrace();
			return null;
		}
		
		return pInfo.versionName;
	}
	
	/**
	 * @return
	 * Return -1 if failed.
	 */
	public int getApplicationVersionCode() {
		PackageInfo pInfo = null;
		try {
			pInfo = getPackageManager().getPackageInfo(getPackageName(), 0);
		} catch (NameNotFoundException e) {
			e.printStackTrace();
			return -1;
		}
		
		return pInfo.versionCode;
	}

	public Activity getActivity() {
		return activity;
	}

	public void setActivity(Activity activity) {
		this.activity = activity;
	}

	public Activity getCurrentRunningActivity() {
		return currentRunningActivity;
	}

	public void setCurrentRunningActivity(Activity currentRunningActivity) {
		this.currentRunningActivity = currentRunningActivity;
	}
}
