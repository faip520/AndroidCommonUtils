package com.A1w0n.androidcommonutils.Otto;

/**
 * Maintains a singleton instance for obtaining the bus. Ideally this would be replaced with a more
 * efficient means such as through injection directly into interested classes.
 * 
 * 如果不反注册，可能会因为长期持有对象引用而引起内存泄漏！
 */
public final class BusProvider {
	
	/**
	 * 如果设置了 ThreadEnforcer.ANY, 那在
	 * 任何线程都可以 Bus.post 消息的，但是如果你的消息的订阅者要操作 view 对象，那你在别的线程 post 是不可以的
	 * 因为在哪个线程 post 订阅者的方法就会在哪个线程运行
	 */
	private static final Bus mBusInstance = new Bus(ThreadEnforcer.ANY);

	public static Bus getInstance() {
		return mBusInstance;
	}

	private BusProvider() {
	}
}
