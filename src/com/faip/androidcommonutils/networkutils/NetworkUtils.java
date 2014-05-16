package com.faip.androidcommonutils.networkutils;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.IntentFilter;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.telephony.TelephonyManager;
import android.text.TextUtils;

public class NetworkUtils {

	private NetworkUtils() {

	}

	public static boolean isNetworkConnected(Context context) {
		ConnectivityManager connMgr = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
		NetworkInfo networkInfo = connMgr.getActiveNetworkInfo();
		return (networkInfo != null && networkInfo.isConnected());
	}

	public static boolean isWifiConnected(Context context) {
		ConnectivityManager connMgr = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
		NetworkInfo networkInfo = connMgr.getNetworkInfo(ConnectivityManager.TYPE_WIFI);
		return networkInfo.isConnected();
	}

	public static boolean isMobileNetworkConnected(Context context) {
		ConnectivityManager connMgr = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
		NetworkInfo networkInfo = connMgr.getNetworkInfo(ConnectivityManager.TYPE_MOBILE);
		return networkInfo.isConnected();
	}

	/**
	 * Register a broadcast receiver for listening network state change.
	 * When calling this method, remember to unregister in your onDestroy callback of your activity.
	 */
	public static void registerNetworkStateChangeReceiver(Context context, BroadcastReceiver receiver) {
		IntentFilter filter = new IntentFilter(ConnectivityManager.CONNECTIVITY_ACTION);
		context.registerReceiver(receiver, filter);
	}
	
	/**
	 * May return null for some carriers.
	 * Note: <uses-permission android:name="android.permission.CHANGE_NETWORK_STATE" /> required！
	 */
	public static String getPhoneNumber(Context context) {
		TelephonyManager tMgr = (TelephonyManager)context.getSystemService(Context.TELEPHONY_SERVICE);
		String mPhoneNumber = tMgr.getLine1Number();
		if (!TextUtils.isEmpty(mPhoneNumber)) {
			return mPhoneNumber;
		}
		return null;
	}

}
