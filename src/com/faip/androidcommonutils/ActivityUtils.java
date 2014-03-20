package com.faip.androidcommonutils;

import android.app.Activity;
import android.view.inputmethod.InputMethodManager;

/**
 * Common utils for activity stuff, much of this utils must pass in a 
 * activity instance as a parameter.
 * @author Faip
 */
public class ActivityUtils {
	
	private ActivityUtils() {}
	
	/**
	 * Close soft keyboard for a activity.
	 */
	public static void hideSoftKeyboard(Activity activity) {
		InputMethodManager inputMethodManager = (InputMethodManager) activity
				.getSystemService(Activity.INPUT_METHOD_SERVICE);
		inputMethodManager.hideSoftInputFromWindow(activity.getCurrentFocus()
				.getWindowToken(), 0);
		activity = null;
	}
}
