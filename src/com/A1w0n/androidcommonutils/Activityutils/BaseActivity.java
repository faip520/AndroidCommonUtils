package com.A1w0n.androidcommonutils.Activityutils;

import com.A1w0n.androidcommonutils.GlobalApplicationUtils.GlobalApplication;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;

public class BaseActivity extends  FragmentActivity {
	
	// 当前所用的主题
	protected int theme = 0;

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
     * 保存一些必要信息，比如：阅读到第几条新闻
     * 这样Activity在重新OnCreate的时候接收到的参数里头就有你保存了的信息了
     */
    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putInt("theme", theme);
    }
    
    /**
     * 重新加载本Activity，并且不显示任何切换动画
     * 可以给人以一种，Activity并没有被换成另外一个
     * 而是在更新自身的错觉
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
    
    
}
