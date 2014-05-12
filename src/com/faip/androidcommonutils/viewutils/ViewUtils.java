package com.faip.androidcommonutils.viewutils;

import android.annotation.SuppressLint;
import android.graphics.drawable.Drawable;
import android.view.View;

public class ViewUtils {

	private ViewUtils() {
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

}
