package com.A1w0n.androidcommonutils;

/**
 *	这里所有命令里头的event0必须是鼠标设备才可以
 *	shell里运行getevent会返回所有可用的设备 
 *	需要Root权限才可以
 */
public class EmulateClickUtils {
	
	// 鼠标左键单击
	private static final String leftButtonClick = "sendevent /dev/input/event0 4 4 36865\n"
			+ "sendevent /dev/input/event0 1 272 1\n"
			+ "sendevent /dev/input/event0 0 0 0\n"
			+ "sendevent /dev/input/event0 4 4 36865\n"
			+ "sendevent /dev/input/event0 1 272 0\n"
			+ "sendevent /dev/input/event0 0 0 0\n";
	
	// 把鼠标光标移动到屏幕最左边
	private static final String moveToLeftEdge = "sendevent /dev/input/event0 2 0 4294963200\n"
			+ "sendevent /dev/input/event0 0 0 0\n";
	
	// 把鼠标光标移动到屏幕最上边
	private static final String moveToTopEdge = "sendevent /dev/input/event0 2 1 4294963200\n"
			+ "sendevent /dev/input/event0 0 0 0\n";
	
	private EmulateClickUtils() {
	}
	
	// 把鼠标光标移动到指定的屏幕坐标上
	private static String getMoveToTargetCommand(int x, int y) {
		return "sendevent /dev/input/event0 2 0 " + x + "\n"
				+ "sendevent /dev/input/event0 0 0 0\n"
				+ "sendevent /dev/input/event0 2 1 " + y + "\n"
				+ "sendevent /dev/input/event0 0 0 0\n";
	}
	
	/**
	 * 向鼠标设备写入事件序列，把鼠标光标移动到指定位置后，左键单击一下
	 * @param x
	 * @param y
	 * @return
	 */
	public static boolean emulateClick(int x, int y) {
		return CMDUtils.runWithRoot(moveToLeftEdge + moveToTopEdge + getMoveToTargetCommand(x, y)
				+ leftButtonClick);
	}
	
}
