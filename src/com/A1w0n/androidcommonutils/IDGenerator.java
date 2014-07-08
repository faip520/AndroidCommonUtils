package com.A1w0n.androidcommonutils;

import java.util.concurrent.atomic.AtomicInteger;

/**
 * 用来生成App级别唯一的ID
 */
public class IDGenerator {

	private IDGenerator() {
	}

	private static final AtomicInteger mNextId = new AtomicInteger(0);

	public static int generateID() {
		return mNextId.incrementAndGet();
	}

}
