package com.A1w0n.androidcommonutils.Activityutils;

import android.os.SystemClock;
import android.support.v4.app.FragmentActivity;
import android.view.View;

import com.A1w0n.androidcommonutils.R;

/**
 * 继承自BaseFragmentActivity，可以防止用户狂点Activity上的按钮
 * 限制为：每300ms只能点击一次
 *
 * Created by A1w0n on 14/12/17.
 */
public abstract class ClickProtectedFragmentActivity extends FragmentActivity implements View.OnClickListener {

    /**
     * 每300毫秒才能点一次，单位：毫秒
     */
    private long mTimeProtected = 300;

    protected abstract void onClickProtected(View v);

    /**
     * 设置点击保护的间隔时间，最少是300毫秒，最长是5000毫秒
     * 单位：毫秒
     *
     * @param time
     */
    protected void setTimeProtected(long time) {
        if (time > 300 && time <= 5000) {
            mTimeProtected = time;
        }
    }

    @Override
    final public void onClick(View v) {
        long currentTime = SystemClock.elapsedRealtime();

        Object tag = v.getTag(R.id.prevent_click_too_frequently);

        long lastClickTime;
        if (tag instanceof Long) {
            lastClickTime = (Long) tag;
        } else {
            lastClickTime = 0;
        }

        if (currentTime - lastClickTime <= mTimeProtected) {
            return;
        }

        onClickProtected(v);
        v.setTag(R.id.prevent_click_too_frequently, currentTime);
    }
}
