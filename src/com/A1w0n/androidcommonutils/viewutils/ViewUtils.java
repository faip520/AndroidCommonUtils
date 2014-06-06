package com.A1w0n.androidcommonutils.viewutils;

import com.A1w0n.androidcommonutils.APILevelUtils;
import com.crashlytics.android.internal.v;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Point;
import android.graphics.Rect;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.util.TypedValue;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;

public class ViewUtils {

	private ViewUtils() {
	}
	
	public static void setActivityFullScreen(Activity activity) {
		activity.requestWindowFeature(Window.FEATURE_NO_TITLE);
		activity.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
	}
	
	@SuppressLint("NewApi")
	public static void setBackground(View view, Drawable drawable) {
		int sdk = android.os.Build.VERSION.SDK_INT;
		if(sdk < android.os.Build.VERSION_CODES.JELLY_BEAN) {
		    view.setBackgroundDrawable(drawable);
		} else {
		    view.setBackground(drawable);
		}
	}
	
	public static void setBackground(View view, Bitmap bm, Context context) {
		if (view == null || bm == null) return;
		
		setBackground(view, new BitmapDrawable(context.getResources(),bm));
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
	
	/**
	 * 获取一个View的几何中心点在屏幕坐标系中的坐标
	 * 这个API在Level18之前返回的是左上角的坐标
	 * 在Level18及以后版本才正确的返回中点坐标
	 * @param view
	 * @param centerPoint
	 */
	public static void getCenterPoint(View view, Point centerPoint) {
		if (view == null || centerPoint == null) return;
		
		Rect rect = new Rect();
		view.getHitRect(rect);
		
		if (APILevelUtils.isLevel18AndAbove()) {
			centerPoint.x = rect.centerX();
			centerPoint.y = rect.centerY();
		}
	}

}
