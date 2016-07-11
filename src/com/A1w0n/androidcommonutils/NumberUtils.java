package com.A1w0n.androidcommonutils;

import java.util.Random;

public class NumberUtils {

	private NumberUtils() {
	}

	public static int byteArrayToInt(byte[] b) {
		return b[3] & 0xFF | (b[2] & 0xFF) << 8 | (b[1] & 0xFF) << 16 | (b[0] & 0xFF) << 24;
	}

	public static byte[] intToByteArray(int a) {
		return new byte[] { (byte) ((a >> 24) & 0xFF), (byte) ((a >> 16) & 0xFF), (byte) ((a >> 8) & 0xFF), (byte) (a & 0xFF) };
	}

    /**
     * 获取随机数
     */
    public static int getRandom() {
        Random random = new Random();

        // 返回小于3的大于等于0的随机数
        return random.nextInt(3);
    }
}
