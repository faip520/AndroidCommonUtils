package com.A1w0n.androidcommonutils.Activityutils;

import com.A1w0n.androidcommonutils.GlobalApplicationUtils.GlobalApplication;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Toast;

/**
 * 成员变量摆放应该分类摆放 顺序，如下：
 * 1 : public final static
 * 2 : public static 
 * 3 : private static final 
 * 4 : private static 
 * 5 : public primary 公有的基本类型
 * 6 : protected primary 继承的基本类型
 * 7 : private primary 私有的基本类型
 * 8 : private other object 
 * 9 : private views
 * 
 * @author A1w0n
 */
public class BaseActivity extends FragmentActivity {

	// 当前所用的主题
	protected int theme = 0;
	
	// 方便需要用到BaseActivity.this的情况，防止代码过长而折行，也方便输入
	// 不直接在声明的时候初始化为this，防止子类获取到的都是父类的实例
	protected Context mContext;
	
	@Override
	protected void onCreate(Bundle arg0) {
		super.onCreate(arg0);
		
		mContext = this;
	}

	@Override
	protected void onResume() {
		super.onResume();
		GlobalApplication.getInstance().setCurrentRunningActivity(this);
	}

	@Override
	protected void onPause() {
		super.onPause();
		// 只反注册自身
		if (GlobalApplication.getInstance().getCurrentRunningActivity() == this) {
			GlobalApplication.getInstance().setCurrentRunningActivity(null);
		}
	}

	/**
	 * 保存一些必要信息，比如：阅读到第几条新闻 这样Activity在重新OnCreate的时候接收到的参数里头就有你保存了的信息了
	 */
	@Override
	protected void onSaveInstanceState(Bundle outState) {
		super.onSaveInstanceState(outState);
		outState.putInt("theme", theme);
	}

	/**
	 * 重新加载本Activity，并且不显示任何切换动画 可以给人以一种，Activity并没有被换成另外一个 而是在更新自身的错觉
	 */
	public void reload() {
		Intent intent = getIntent();
		// 不显示任何切换动画
		overridePendingTransition(0, 0);
		// 不显示任何切换动画
		intent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
		finish();

		// 不显示任何切换动画
		overridePendingTransition(0, 0);
		startActivity(intent);
	}

	/**
	 * 让子类方便的设置NoTitle
	 */
	protected void requestNoTitle() {
		requestWindowFeature(Window.FEATURE_NO_TITLE);
	}

	/**
	 * 让子类方便的设置屏幕长亮
	 */
	protected void requestScreenOn() {
		getWindow().setFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON,
				WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
	}

	/**
	 * 让子类方便的请求全屏
	 * 
	 * @param activity
	 */
	protected void requestFullScreen() {
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
				WindowManager.LayoutParams.FLAG_FULLSCREEN);
	}
	
	protected void showToastShort(String message) {
		Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
	}
	
	protected void showToastLong(String message) {
		Toast.makeText(this, message, Toast.LENGTH_LONG).show();
	}
}
