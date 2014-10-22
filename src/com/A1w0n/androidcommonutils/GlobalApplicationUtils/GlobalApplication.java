package com.A1w0n.androidcommonutils.GlobalApplicationUtils;

import java.lang.Thread.UncaughtExceptionHandler;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import android.app.Activity;
import android.app.ActivityManager;
import android.app.Application;
import android.content.Context;
import android.content.Intent;
import android.content.Intent.ShortcutIconResource;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager.NameNotFoundException;
import android.os.Handler;
import android.support.v4.app.FragmentManager;
import android.util.DisplayMetrics;
import android.view.WindowManager;

import com.A1w0n.androidcommonutils.debugutils.Logger;
import com.A1w0n.androidcommonutils.debugutils.StrictModeUtils;
import com.crashlytics.android.Crashlytics;
import com.faip.androidcommonutils.BuildConfig;
import com.faip.androidcommonutils.R;

/**
 * Global singleton applicatioin subclass.
 */
public final class GlobalApplication extends Application {
	
	public static ExecutorService mGloabExecutorService = Executors.newFixedThreadPool(3);
	// 方便获取UI线程的Handler
	public static Handler mUiHandler = new Handler();

	// 发生异常时，用来做默认处理
	private static final UncaughtExceptionHandler mUEHandler = Thread.getDefaultUncaughtExceptionHandler();
	// Singleton.
	private static GlobalApplication mGlobalApplication = null;

	// Record the current running activity.
	private Activity activity = null;

	private Activity currentRunningActivity = null;

	private DisplayMetrics displayMetrics = null;

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
		
		reportMemoryClass();
		initExecptionHandler();
	}
	
	/**
	 * 对于未被捕捉到的异常，在这里可以做最后的
	 */
	private void initExecptionHandler() {
		Thread.setDefaultUncaughtExceptionHandler(new UncaughtExceptionHandler() {
			@Override
			public void uncaughtException(Thread thread, Throwable ex) {
				// 发生未捕捉异常的时候，做什么
				// ......
				// .......
				mUEHandler.uncaughtException(thread, ex);
			}
		});
	}
	
	/**
	 * App 最多可以申请的内存大小，和正常工作的话，最好不要大于多少
	 */
	private void reportMemoryClass() {
		long max = Runtime.getRuntime().maxMemory();
		Logger.d("App can comsume memory maxiumly up to : " + max + " bytes!");
		
		ActivityManager am = (ActivityManager) getSystemService(ACTIVITY_SERVICE);
		int proper = am.getMemoryClass();
		Logger.d("App should comsume memory lower to : " + proper + "mb to work properly in Android OS!");
	}

	public static GlobalApplication getInstance() {
		return mGlobalApplication;
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
	
	/**
	 * 添加快捷方式到launcher
	 * @param mContext
	 */
	public static void addShortcut(Context mContext) {
		Intent shortcut = new Intent("com.android.launcher.action.INSTALL_SHORTCUT");

		shortcut.putExtra(Intent.EXTRA_SHORTCUT_NAME,mContext.getString(R.string.app_name));
		shortcut.putExtra("duplicate", false);

		Intent intent = new Intent(Intent.ACTION_MAIN);
		intent.setClassName(mContext, mContext.getPackageName() + ".ui.other.Splash");
		intent.addCategory("android.intent.category.LAUNCHER");
		intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
		intent.addFlags(Intent.FLAG_ACTIVITY_RESET_TASK_IF_NEEDED);
		shortcut.putExtra(Intent.EXTRA_SHORTCUT_INTENT, intent);

		ShortcutIconResource iconRes = Intent.ShortcutIconResource.fromContext(mContext, R.drawable.ic_launcher);
		shortcut.putExtra(Intent.EXTRA_SHORTCUT_ICON_RESOURCE, iconRes);

		mContext.sendBroadcast(shortcut);
	}
}
