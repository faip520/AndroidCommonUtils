package com.A1w0n.androidcommonutils.NetworkUtils;

import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.SocketException;
import java.util.Enumeration;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.IntentFilter;
import android.net.ConnectivityManager;
import android.net.DhcpInfo;
import android.net.NetworkInfo;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.telephony.TelephonyManager;
import android.text.TextUtils;
import android.text.format.Formatter;

public class NetworkUtils {
	
	private static final String PATTERN_IPV4_ADDRESS = 
	        "^([01]?\\d\\d?|2[0-4]\\d|25[0-5])\\." +
	        "([01]?\\d\\d?|2[0-4]\\d|25[0-5])\\." +
	        "([01]?\\d\\d?|2[0-4]\\d|25[0-5])\\." +
	        "([01]?\\d\\d?|2[0-4]\\d|25[0-5])$";

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
	 * Register a broadcast receiver for listening network state change. When calling this method,
	 * remember to unregister in your onDestroy callback of your activity.
	 */
	public static void registerNetworkStateChangeReceiver(Context context, BroadcastReceiver receiver) {
		IntentFilter filter = new IntentFilter(ConnectivityManager.CONNECTIVITY_ACTION);
		context.registerReceiver(receiver, filter);
	}

	/**
	 * May return null for some carriers. Note: <uses-permission
	 * android:name="android.permission.CHANGE_NETWORK_STATE" /> required！
	 */
	public static String getPhoneNumber(Context context) {
		TelephonyManager tMgr = (TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE);
		String mPhoneNumber = tMgr.getLine1Number();
		if (!TextUtils.isEmpty(mPhoneNumber)) {
			return mPhoneNumber;
		}
		return null;
	}

	/**
	 * 如果同时有内外网IP，用这个来获取内网IP
	 * @return
	 */
	public static String getLocalIpAddress() {
		try {
			for (Enumeration<NetworkInterface> en = NetworkInterface.getNetworkInterfaces(); en.hasMoreElements();) {
				NetworkInterface intf = en.nextElement();
				for (Enumeration<InetAddress> enumIpAddr = intf.getInetAddresses(); enumIpAddr.hasMoreElements();) {
					InetAddress inetAddress = enumIpAddr.nextElement();
					if (!inetAddress.isLoopbackAddress() && !inetAddress.isLinkLocalAddress()) {
						return inetAddress.getHostAddress().toString();
					}
				}
			}
		} catch (SocketException ex) {

		}
		return null;
	}

	/**
	 * 获取WIFI局域网中的内网IP
	 * @param context
	 * @return
	 */
	public static String getInternalIP(Context context) {
		WifiManager wifiManager = (WifiManager) context.getSystemService(Context.WIFI_SERVICE);
		WifiInfo wifiInfo = wifiManager.getConnectionInfo();
		int ipAddress = wifiInfo.getIpAddress();
		if (ipAddress == 0)
			return null;
		return ((ipAddress & 0xff) + "." + (ipAddress >> 8 & 0xff) + "." + (ipAddress >> 16 & 0xff) + "." + (ipAddress >> 24 & 0xff));
	}
	
	/**
	 * 获取已连上的 Wifi 路由器 的 ip
	 * @param context
	 * @return
	 */
	@SuppressWarnings("deprecation")
	public static String getWifiRouterIp(Context context) {
		final WifiManager manager = (WifiManager) context.getSystemService(context.WIFI_SERVICE);
		final DhcpInfo dhcp = manager.getDhcpInfo();
		return Formatter.formatIpAddress(dhcp.gateway);
	}
	
	/**
	 * 通过正则表达式判断 ip 是不是合法的 IPv4 地址
	 * @param ip
	 * @return
	 */
	public static boolean isValidIPv4Address(String ip) {
		if (!TextUtils.isEmpty(ip) && ip.length() < 16) {
			Pattern pattern = Pattern.compile(PATTERN_IPV4_ADDRESS);
			Matcher matcher = pattern.matcher(ip);
			return matcher.matches();
		}
		
		return false;
	}
}
