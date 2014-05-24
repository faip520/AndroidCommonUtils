package com.A1w0n.androidcommonutils;

import android.annotation.SuppressLint;
import android.content.Context;
import android.os.Vibrator;

/**
 * Vibrate permission is needed!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!
 */
public class VibrationManager {

	private Vibrator mVibrator;

	private static VibrationManager mInstance;

	private VibrationManager(Context context) {
		try {
			// Will return null if the device with a sdk level lower than 11 do not have a vibrator.
			mVibrator = (Vibrator) context.getSystemService(Context.VIBRATOR_SERVICE);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public static synchronized VibrationManager getInstance(Context context) {
		if (mInstance == null) {
			mInstance = new VibrationManager(context);
		}
		return mInstance;
	}

	public void vibrate(int milliseconds) {
		if (isAvailable()) {
			mVibrator.vibrate(milliseconds);
		}
	}

	/**
	 * Returns if the vibration is currently available on the current device
	 */
	@SuppressLint("NewApi")
	public boolean isAvailable() {
		if (null != mVibrator) {
			if (android.os.Build.VERSION.SDK_INT >= 11) {
				return mVibrator.hasVibrator();
			} else {
				// For sdk level lower than 11, vibrator's initilization succesfully means the device has a vibrator.
				return true;
			}
		}
		return false;
	}

}
