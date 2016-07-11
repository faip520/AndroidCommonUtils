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

    /**
     * Fragment的构造函数必须是没参数的，如果发生屏幕旋转，系统会重新构建fragment，
     * 而此时调用的是Fragment的默认构造函数，所以任何你在带参数的fragment里头做的
     * 操作都不会被调用
     */
    public BaseFragment() {
    }

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

    /**
     * 安全的结束Activity
     */
    protected void finishActivity() {
        Activity activity = getActivity();

        if (activity != null && !activity.isFinishing()) {
            activity.finish();
        }
    }

    /**
     * 怎么样new 本类的实例，必须封装在内部
     * 可以减轻外部对本类的依赖
     */
    public static void newInstance() {

    }
}
