package com.A1w0n.androidcommonutils.ViewUtils;

import android.os.SystemClock;
import android.view.View;
import com.A1w0n.androidcommonutils.R;


/**
 * @author Aiwan
 * Forbidden the user clicking view too fast.
 */
public abstract class CommonOnlickListener implements View.OnClickListener {
	
	@Override
	public void onClick(View v) {
		long currentTime = SystemClock.elapsedRealtime();

		Object tag = v.getTag(R.id.example);

		long lastClickTime;
		if (tag instanceof Long) {
			lastClickTime = (Long) tag;
		} else {
			lastClickTime = 0;
		}

		if (currentTime - lastClickTime <= 1000) {
			return;
		}

		v.setTag(R.id.example, currentTime);
		doClick(v);
	}
	
	public abstract void doClick(View v);

}
