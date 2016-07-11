package com.A1w0n.androidcommonutils.ViewUtils;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.FrameLayout;

/**
 * 可以监听键盘弹起或者收起的View, 设置给Activity的根view, 就可以了
 *
 * Created by A1w0n on 15/1/13.
 */
public class KeyboardFrameLayout extends FrameLayout {

    /**
     * 不包含键盘的高度, 默认值为0, 初次onLayout时会给它赋值
     */
    private int mHeightWithoutKeyboard = 0;

    public KeyboardFrameLayout(Context context) {
        super(context);
    }

    public KeyboardFrameLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public KeyboardFrameLayout(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }

    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {
        super.onLayout(changed, l, t, r, b);

        // 当前view的高度
        int height = b - t;

        if (mHeightWithoutKeyboard == 0) { // 第一次进来初始化完mHeightWithKeyboard就立马返回
            mHeightWithoutKeyboard = height;

            return;
        } else { // 非第一次进来
            if (mHeightWithoutKeyboard > height) { // 高度变小了, 说明键盘弹起了

            } else { // 高度变大或者变回原来的, 说明键盘收起了

            }
        }
    }
}
