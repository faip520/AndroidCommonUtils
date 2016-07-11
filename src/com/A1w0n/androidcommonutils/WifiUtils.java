package com.A1w0n.androidcommonutils;

import android.content.Context;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;

/**
 * Wifi相关工具类
 *
 * Created by A1w0n on 15/3/19.
 */
public class WifiUtils {

    /**
     * 获取当前wifi连接的SSID
     *
     * @return 有可能返回空的字符串，意为获取失败
     */
    public static String getWifiSSID(Context context) {
        try {
            WifiManager wifiManager = (WifiManager) context.
                    getSystemService(Context.WIFI_SERVICE);
            WifiInfo wifiInfo = wifiManager.getConnectionInfo();
            return wifiInfo.getSSID();
        } catch (Exception e) { // 捕获一切异常
            e.printStackTrace();
            return "";
        }
    }

}
