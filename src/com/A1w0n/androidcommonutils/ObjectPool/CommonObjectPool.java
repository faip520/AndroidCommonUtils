package com.A1w0n.androidcommonutils.ObjectPool;

import java.util.ArrayList;

public class CommonObjectPool<T> {
	
	private ArrayList<T> unlocked;

	public CommonObjectPool() {
		unlocked = new ArrayList<T>();
	}
	
	public synchronized T checkOut() {
		T t = null;
		
		if (unlocked.size() > 0) {
			t = unlocked.get(0);
			unlocked.remove(t);
			return t;
		}
		
		return t;
	}
	
	public synchronized void checkIn(T t) {
		unlocked.add(t);
	}

}
