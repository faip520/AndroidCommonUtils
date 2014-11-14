package com.A1w0n.androidcommonutils;

import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnTouchListener;
import android.widget.GridView;

public class ScrollViewUtil {
	
	/**
	 * ScrollView中的ListView或者GridView或者ViewPager，如果子View和ScrollView
	 * 滑动方向一致，则会滑动不了，如果是垂直，则能滑动，但是滑动体验非常差（例如：
	 * ScrollView中得ViewPager），这个方法用于优化这种情况，使子View能有正常的滑动表现
	 * 
	 * @param gridView
	 */
	public static void handleScrollInScrollView(GridView gridView) {
		gridView.setOnTouchListener(new OnTouchListener() {
			@Override
			public boolean onTouch(View arg0, MotionEvent arg1) {
				// 子控件优先滑动
				arg0.getParent().requestDisallowInterceptTouchEvent(true);
			    return false;
			}
		});
	}
	
}
