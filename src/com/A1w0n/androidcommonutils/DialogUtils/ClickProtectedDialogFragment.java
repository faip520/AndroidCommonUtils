package com.A1w0n.androidcommonutils.DialogUtils;

import android.os.SystemClock;
import android.support.v4.app.DialogFragment;
import android.view.View;

import com.A1w0n.androidcommonutils.R;

/**
 * 继承自DialogFragment，可以防止用户狂点Dialog上的按钮
 *
 * Created by A1w0n on 15/1/7.
 */
public abstract class ClickProtectedDialogFragment extends DialogFragment implements View.OnClickListener {

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
    public final void onClick(View v) {
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
