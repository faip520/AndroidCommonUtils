package com.A1w0n.androidcommonutils.BinaryUtils;

/**
 * 二进制相关工具类
 *
 * Created by A1w0n on 15/1/23.
 */
public class BinaryUtils {

    /**
     * int型是32位,这个就是32位都是1的二进制数
     * java中数字也是用反码表示的,-1的反码就是全都是1
     */
    private static final int ALL = -1;

    /**
     * 一个mask位,用来获取,设置或者比较的,这里设置的是第8个二进制位
     */
    private static final int MASK = 0x40;

    /**
     * 设置标志位
     *
     * @param target
     * @param mask
     */
    public static void set(int target, int mask) {
        target |= mask;
    }

    /**
     * 清空标志位
     *
     * @param target
     * @param mask
     */
    public static void reset(int target, int mask) {
        target &= ~mask;
    }

    /**
     * 判断标志位是否都设置了
     *
     * @param target
     * @param mask
     * @return
     */
    public static boolean isSet(int target, int mask) {
        return (target & mask) == mask;
    }

    /**
     * 判断标志位是否都设置了
     *
     * @param target
     * @param mask
     * @return
     */
    public static boolean isSet2(int target, int mask) {
        return (target & mask) != 0;
    }
}
