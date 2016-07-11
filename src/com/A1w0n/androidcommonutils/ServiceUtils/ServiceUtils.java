package com.A1w0n.androidcommonutils.ServiceUtils;

import android.app.ActivityManager;
import android.content.Context;

import java.util.ArrayList;

/**
 *
 *
 * Created by A1w0n on 14/12/19.
 */
public class ServiceUtils {

    /**
     * 判断给定的名字的Service是否还在运行
     *
     * @param mContext
     * @param serviceName
     * @return true 则仍在运行
     * 		   false 则没在运行了
     */
    public static boolean isConnectionServiceWorked(Context mContext, String serviceName) {
        ActivityManager myManager = (ActivityManager) mContext.getSystemService(Context.ACTIVITY_SERVICE);
        ArrayList<ActivityManager.RunningServiceInfo> runningService = (ArrayList<ActivityManager.RunningServiceInfo>) myManager.getRunningServices(50);
        if(runningService != null) {
            for (int i = 0; i < runningService.size(); i++) {
                String servicesName = runningService.get(i).service.getClassName().toString();
                if (servicesName.equals(serviceName)) {
                    return true;
                }
            }
        }

        return false;
    }
}
