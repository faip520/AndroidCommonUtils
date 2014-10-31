package com.A1w0n.androidcommonutils.FragmentUtils;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.widget.Toast;

/**
 * Created by Aiwan on 2014/5/24 0024.
 * 所有Fragment的基类，configuration change的时候不重建。
 */
public class BaseFragment extends Fragment {
	// 方便需要用到Context的情况
	protected Context mContext;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setRetainInstance(true);
        // 显示系统的一切的Fragment相关的操作信息
        FragmentManager.enableDebugLogging(true);
    }

	@Override
	public void onAttach(Activity activity) {
		super.onAttach(activity);
		mContext = activity;
	}
    
	protected void showToastShort(String message) {
		Toast.makeText(mContext, message, Toast.LENGTH_SHORT).show();
	}
	
	protected void showToastLong(String message) {
		Toast.makeText(mContext, message, Toast.LENGTH_LONG).show();
	}
}
