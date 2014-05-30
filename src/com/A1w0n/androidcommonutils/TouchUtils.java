package com.A1w0n.androidcommonutils;

import android.view.MotionEvent;

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

}
