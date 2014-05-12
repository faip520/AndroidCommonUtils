package com.faip.androidcommonutils.activityutils;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.graphics.Rect;
import android.util.TypedValue;
import android.view.inputmethod.InputMethodManager;

/**
 * Common utils for activity stuff, much of this utils must pass in a activity instance(or context
 * instance) as a parameter.
 * 
 * @author Faip
 */
public class ActivityUtils {

	private ActivityUtils() {
	}

	/**
	 * Close soft keyboard for a activity.
	 */
	public static void hideSoftKeyboard(Activity activity) {
		InputMethodManager inputMethodManager = (InputMethodManager) activity.getSystemService(Activity.INPUT_METHOD_SERVICE);
		inputMethodManager.hideSoftInputFromWindow(activity.getCurrentFocus().getWindowToken(), 0);
	}

	public static int dip2px(Context context, int dipValue) {
		float reSize = context.getResources().getDisplayMetrics().density;
		return (int) ((dipValue * reSize) + 0.5);
	}

	public static int px2dip(Context context, int pxValue) {
		float reSize = context.getResources().getDisplayMetrics().density;
		return (int) ((pxValue / reSize) + 0.5);
	}

	public static float sp2px(Context context, int spValue) {
		return TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_SP, spValue, context.getResources().getDisplayMetrics());
	}
	
	/**
	 * Get screen size in pixel
	 */
	@SuppressLint("NewApi")
	public static int[] getScreenSize(Activity context) {
		Rect rect = new Rect();
		context.getWindow().getDecorView().getWindowVisibleDisplayFrame(rect);
		return new int[]{rect.width(), rect.height()};
	}
}
