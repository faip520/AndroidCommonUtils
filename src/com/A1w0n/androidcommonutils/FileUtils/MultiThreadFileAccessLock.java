package com.A1w0n.androidcommonutils.FileUtils;

import java.io.File;
import java.util.HashMap;

/**
 * @author Aiwan
 * 如果你有一个文件 125.txt 这个文件可以new 出来很多个File对象
 * 然后有多线程在通过这些File对象来读写这个文件，你如何做到限制
 * 同一时间内，最多只有一个线程能读写这个文件？
 * 
 * 这个类就是解决这个问题的
 * 
 * 你可以通过File对象来获取一个Object，然后用这个Object做锁，就能到达目的了
 * 
 * 因为这个类内部会用File对象的绝对路径做key 来映射Object对象，指向同一个文件的
 * File对象获取到的Object对象肯定是同一个
 */
public class MultiThreadFileAccessLock {
	
	private static MultiThreadFileAccessLock mInstance;
	
	private HashMap<String, Object> mHashMap;

	private MultiThreadFileAccessLock() {
		mHashMap = new HashMap<String, Object>();
	}
	
	public static MultiThreadFileAccessLock getInstance() {
		if (mInstance == null) {
			mInstance = new MultiThreadFileAccessLock();
		}
		
		return mInstance;
	}
	
	public Object get(File key) {
		if (key == null) {
			throw new NullPointerException("Cannot generate key for null file!");
		}
		
		String absolutePath = key.getAbsolutePath();
		Object result = mHashMap.get(absolutePath);
		if (result == null) {
			result = new Object();
			mHashMap.put(absolutePath, result);
		}
		
		return result;
	}

}
