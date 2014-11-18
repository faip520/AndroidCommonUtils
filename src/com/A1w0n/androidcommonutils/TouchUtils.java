package com.A1w0n.androidcommonutils;

import android.content.Context;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;
import android.widget.GridView;

public class TouchUtils {

	private TouchUtils() {
	}
	
    /**
     * MotionEvent getX(int)方法 而没有 getRawX(int) 这样的方法的;
     * 现在只能用模拟的方法做到同样的功能
     */
	public static float getRawX(MotionEvent event, int pointerIndex) {
        if (pointerIndex < 0) return Float.MIN_VALUE;
        if (pointerIndex == 0) return event.getRawX();
        float offset = event.getRawX() - event.getX();
        return event.getX(pointerIndex) + offset;
    }
	
    /**
     * MotionEvent getY(int)方法 而没有 getRawY(int) 这样的方法的;
     * 现在只能用模拟的方法做到同样的功能
     */
	public static float getRawY(MotionEvent event, int pointerIndex) {
        if (pointerIndex < 0) return Float.MIN_VALUE;
        if (pointerIndex == 0) return event.getRawY();
        float offset = event.getRawY() - event.getY();
        return event.getY(pointerIndex) + offset;
    }

    /**
     * ScrollView中的ListView或者GridView或者ViewPager，如果子View和ScrollView
     * 滑动方向一致，则会滑动不了，如果是垂直，则能滑动，但是滑动体验非常差（例如：
     * ScrollView中得ViewPager），这个方法用于优化这种情况，使子View能有正常的滑动表现
     *
     * @param gridView
     */
    public static void handleScrollInScrollView(GridView gridView) {
        gridView.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View arg0, MotionEvent arg1) {
                // 子控件优先滑动
                arg0.getParent().requestDisallowInterceptTouchEvent(true);
                return false;
            }
        });
    }

    /**
     * 在View的onTouchListener中检测手指单击的事件
     *
     * @param view
     */
    public static void handlerOnClickInOnTouchListener(View view) {


        view.setOnTouchListener(new View.OnTouchListener() {

            private long mDeBounce = 0;

            @Override
            public boolean onTouch(View arg0, MotionEvent motionEvent) {
                if ( Math.abs(mDeBounce - motionEvent.getEventTime()) < 250) {
                    // Ignore if it's been less then 250ms since
                    // the item was last clicked
                    return true;
                }

                int intCurrentY = Math.round(motionEvent.getY());
                int intCurrentX = Math.round(motionEvent.getX());

                int intStartY = motionEvent.getHistorySize() > 0 ? Math.round(motionEvent.getHistoricalY(0)) : intCurrentY;
                int intStartX = motionEvent.getHistorySize() > 0 ? Math.round(motionEvent.getHistoricalX(0)) : intCurrentX;

                if ( (motionEvent.getAction() == MotionEvent.ACTION_UP) && (Math.abs(intCurrentX - intStartX) < 3) && (Math.abs(intCurrentY - intStartY) < 3) ) {
                    if ( mDeBounce > motionEvent.getDownTime() ) {
                        //Still got occasional duplicates without this
                        return true;
                    }

                    // Handle the click

                    mDeBounce = motionEvent.getEventTime();
                    return true;
                }
                return false;
            }
        });
    }

    /**
     * 让传进来的View优先处理touch事件，并且会处理单击事件的OnTouchListener
     */
    public static class InterceptHandleClickOnTouchListener implements View.OnTouchListener {

        private Context mContext;
        private View mView;

        public InterceptHandleClickOnTouchListener(Context mContext, View mView) {
            this.mContext = mContext;
            this.mView = mView;
        }

        GestureDetector mGestureDetector = new GestureDetector(mContext, new SingleTapConfirm());

        @Override
        public boolean onTouch(View view, MotionEvent motionEvent) {
            mView.getParent().requestDisallowInterceptTouchEvent(true);

            mGestureDetector.onTouchEvent(motionEvent);

            return false;
        }

        // =============================SingleTapConfirm========================================
        /**
         * 一个简单的手势检测器，检测到点击事件就返回True
         */
        private class SingleTapConfirm extends GestureDetector.SimpleOnGestureListener {

            @Override
            public boolean onSingleTapConfirmed(MotionEvent event) {
                // handle click
                return true;
            }
        }
        // =============================End of SingleTapConfirm========================================

    }


}
