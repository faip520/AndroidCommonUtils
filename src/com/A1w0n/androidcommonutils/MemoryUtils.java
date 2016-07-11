package com.A1w0n.androidcommonutils;

import android.app.ActivityManager;
import android.content.Context;

import com.A1w0n.androidcommonutils.GlobalApplicationUtils.GlobalApplication;

/**
 * Utils about memory info.
 *
 * Created by A1w0n on 15/4/24.
 */
public class MemoryUtils {

    /**
     * Get system memory info: free memory and total memory size.
     */
    @SuppressWarnings("All")
    public static String getFreeMemory() {
        String result;

        ActivityManager.MemoryInfo mi = new ActivityManager.MemoryInfo();

        Context context = GlobalApplication.getInstance();

        ActivityManager activityManager = (ActivityManager)context.getSystemService(
                Context.ACTIVITY_SERVICE);
        activityManager.getMemoryInfo(mi);

        // Convert the unit to Mb 1024 * 1024 = 1048576
        long availableMegs = mi.availMem / 1048576L;

        result = "System available memory size = " + availableMegs + "Mb";

        if (APILevelUtils.isLevel16AndAbove()) {
            result = result + " System total memory size = " + mi.totalMem / 1048576L + "Mb";
        }

        return result;
    }
}
