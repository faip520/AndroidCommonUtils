package com.A1w0n.androidcommonutils.Activityutils;

import java.io.File;
import java.util.List;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.net.Uri;
import android.provider.MediaStore;
import android.view.Display;
import android.view.Window;
import android.view.WindowManager;

import com.A1w0n.androidcommonutils.GlobalApplicationUtils.GlobalApplication;

public class ActivityUtils {

	private ActivityUtils() {
	}
	
	/**
	 * 让目标activity的保持屏幕常亮
	 * @param activity
	 */
	public static void keepScreenOn(Activity activity) {
		activity.getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
	}

	/**
	 * Start the system gallery activity to pick an image
	 */
	public static void pickFromGallery(Activity activity) {
		Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
		intent.setType("image/*");

		Intent chooser = Intent.createChooser(intent, "Choose a Picture");
		int requestCode = 10086;
		activity.startActivityForResult(chooser, requestCode);
	}
	
	/**
	 * 获取屏幕当前的方向
	 * @param context
	 * @return
	 */
	public static int getRotation(Context context) {
		WindowManager wm = (WindowManager)context.getSystemService(Context.WINDOW_SERVICE);
		Display display = wm.getDefaultDisplay();
		
		return  display.getRotation();
	}

	/**
	 * Open the device's camera, take a photo and storage it into the targetFile
	 * 
	 * @param activity
	 * @param targetFile
	 */
	public static void pickFromCamera(Activity activity, File targetFile) {
		Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE, null);
		intent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(targetFile));
		int requestCode = 10086;
		activity.startActivityForResult(intent, requestCode);
	}

	/**
	 * Determine if the function or component required by the intent ia available.
	 * @param ctx
	 * @param intent
	 * @return
	 */
	public static boolean isIntentAvailable(Context ctx, Intent intent) {
		final PackageManager mgr = ctx.getPackageManager();
		List<ResolveInfo> list = mgr.queryIntentActivities(intent, PackageManager.MATCH_DEFAULT_ONLY);
		return list.size() > 0;
	}
	
	public static void requestFullScreen(Activity activity) {
		activity.requestWindowFeature(Window.FEATURE_NO_TITLE);
		activity.getWindow().setFlags(
				WindowManager.LayoutParams.FLAG_FULLSCREEN, 
				WindowManager.LayoutParams.FLAG_FULLSCREEN);
	}
	
	/**
	 * 双击 back 键返回，单击执行其他动作
	 */
	private long mLastBackPress = 0;
	private boolean mSecondPress = false;
	public void onBackPressed() {
		long current = System.currentTimeMillis();
		
		if (current - mLastBackPress < 1000) {
			mSecondPress = true;
			// handle double click
		} else {
			GlobalApplication.mUiHandler.postDelayed(new Runnable() {
				@Override
				public void run() {
					if (!mSecondPress) {
						// handle single click
					}
				}
			}, 1000);
		}
		mLastBackPress = current;
	}
	
	/**
	 * 关闭系统通知界面
	 */
	public static void closeSystemNotificationDrawer(Context context) {
		if (context != null) {
			context.sendBroadcast(new Intent(Intent.ACTION_CLOSE_SYSTEM_DIALOGS));
		}
	}
}
