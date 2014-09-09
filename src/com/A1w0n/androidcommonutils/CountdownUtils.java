package com.A1w0n.androidcommonutils;

import android.os.SystemClock;

public class CountdownUtils {

	private CountdownUtils() {
	}
	
	private boolean waitUntilOK(boolean waitToChange) {
		int count = 0;
		
		while (!waitToChange) {
			SystemClock.sleep(1000);
			count++;
			if (count == 20) {
				return false;
			}
		}
		
		return true;
	}

}
