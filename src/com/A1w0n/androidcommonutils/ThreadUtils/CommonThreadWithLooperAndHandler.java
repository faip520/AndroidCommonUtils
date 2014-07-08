package com.A1w0n.androidcommonutils.ThreadUtils;

import android.os.Handler;
import android.os.Looper;
import android.os.Message;

public class CommonThreadWithLooperAndHandler extends Thread {
	
	public static final int WHAT_FINISH = 0;
	public static final int WHAT_START = 0;
	
	private Handler mHandler;

	public CommonThreadWithLooperAndHandler() {
		setName("A1w0n common thread");
	}
	
	@Override
	public void run() {
		super.run();
		
		// 开始Looper循环
		Looper.prepare();
		
		mHandler = new CommonThreadHandler();
        
        Looper.loop();
	}
	
	public Handler getHandler() {
		return mHandler;
	}
	
	// ==============CommonThreadHandler========================
	public static class CommonThreadHandler extends Handler {
		@Override
		public void handleMessage(Message msg) {
			super.handleMessage(msg);
			
            if (msg.what == WHAT_START) {
            } else if (msg.what == WHAT_FINISH) {
                Looper.myLooper().quit();
            }
		}
	}
	
	// ======================================================
}
