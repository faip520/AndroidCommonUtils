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
import android.view.Window;
import android.view.WindowManager;

public class ActivityUtils {

	private ActivityUtils() {
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
}
