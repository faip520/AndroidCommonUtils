package com.A1w0n.androidcommonutils;

import android.app.ActivityManager;
import android.content.Context;

/**
 *
 *
 * Created by A1w0n on 15/3/12.
 */
public class ProcessUtils {

    /**
     * 获取当前线程的名字
     *
     * @param context
     * @return
     */
    public static String getCurProcessName(Context context) {
        int pid = android.os.Process.myPid();

        ActivityManager mActivityManager = (ActivityManager) context.
                getSystemService(Context.ACTIVITY_SERVICE);

        for (ActivityManager.RunningAppProcessInfo appProcess :
                mActivityManager.getRunningAppProcesses()) {
            if (appProcess.pid == pid) {
                return appProcess.processName;
            }
        }

        return "";
    }
}
