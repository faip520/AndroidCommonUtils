package com.A1w0n.androidcommonutils.ViewUtils;

import android.view.View;
import android.view.View.OnClickListener;


/**
 * @author Aiwan
 * Forbidden the user clicking view too fast.
 */
public abstract class CommonOnlickListener implements OnClickListener {
	
	private long mLastClickTime = 0;

	@Override
	public void onClick(View v) {
		long currentTime = System.currentTimeMillis();
		
		if (mLastClickTime == 0 || currentTime - mLastClickTime <= 1000) {
			mLastClickTime = currentTime;
			doClick(v);
		}
	}
	
	public abstract void doClick(View v);

}
