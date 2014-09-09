package com.A1w0n.androidcommonutils.ObjectPool;

public class FramebufferObjectPool extends CommonObjectPool<Integer> {
	
	public static FramebufferObjectPool Instance = new FramebufferObjectPool();
	
	private static byte[] buffer_one;
	private static byte[] buffer_two;
	private static byte[] buffer_three;
	
	private static final int KEY_ONE = 10086;
	private static final int KEY_TWO = 20086;
	private static final int KEY_THREE = 30086;
	
	private FramebufferObjectPool() {
		buffer_one = new byte[101024];
		buffer_two = new byte[101024];
		buffer_three = new byte[101024];
		
		checkIn(KEY_ONE);
		checkIn(KEY_TWO);
		checkIn(KEY_THREE);
	}
	
	public FBContainer checkOutFB() {
		int result = checkOut();
		
		switch (result) {
		case KEY_ONE:
			return new FBContainer(KEY_ONE, buffer_one);
		case KEY_TWO:
			return new FBContainer(KEY_TWO, buffer_two);
		case KEY_THREE:
			return new FBContainer(KEY_THREE, buffer_three);
		default:
			return null;
		}
	}
}
