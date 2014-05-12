package com.faip.androidcommonutils.activityutils;

import com.faip.androidcommonutils.globalapplication.GlobalApplication;

import android.support.v4.app.FragmentActivity;

public class BaseActivity extends  FragmentActivity {

    @Override
    protected void onResume() {
        super.onResume();
        GlobalApplication.getInstance().setCurrentRunningActivity(this);
    }

    @Override
    protected void onPause() {
        super.onPause();
        // Set crrentRunningActivity to null when leaving.
        if (GlobalApplication.getInstance().getCurrentRunningActivity() == this) {
        	GlobalApplication.getInstance().setCurrentRunningActivity(null);
        }
    }
}
