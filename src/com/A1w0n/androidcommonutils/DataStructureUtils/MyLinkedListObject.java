package com.A1w0n.androidcommonutils.DataStructureUtils;

import android.view.View;

/**
 * 自定义链表,代码取自ViewGroup源码
 *
 * Created by A1w0n on 15/1/23.
 */
public class MyLinkedListObject {

    private static final int MAX_RECYCLED = 32;
    private static final Object sRecycleLock = new Object();
    /**
     * 链表的表头,指向链表的第一个对象
     */
    private static MyLinkedListObject sRecycleBin;
    private static int sRecycledCount;

    public static final int ALL_POINTER_IDS = -1; // all ones

    // The touched child view.
    public View child;

    // The combined bit mask of pointer ids for all pointers captured by the target.
    public int pointerIdBits;

    // The next target in the target list.
    public MyLinkedListObject next;

    private MyLinkedListObject() {
    }

    /**
     * 如果缓存列表不为空,则从缓存里头取,如果缓存链表为空,则新建对象
     *
     * @param child
     * @param pointerIdBits
     * @return
     */
    public static MyLinkedListObject obtain(View child, int pointerIdBits) {
        final MyLinkedListObject target;
        synchronized (sRecycleLock) {
            if (sRecycleBin == null) {
                target = new MyLinkedListObject();
            } else {
                target = sRecycleBin;
                sRecycleBin = target.next;
                sRecycledCount--;
                target.next = null;
            }
        }
        target.child = child;
        target.pointerIdBits = pointerIdBits;
        return target;
    }

    /**
     * 如果缓存大小没超过上限则插入到表头
     */
    public void recycle() {
        synchronized (sRecycleLock) {
            if (sRecycledCount < MAX_RECYCLED) {
                next = sRecycleBin;
                sRecycleBin = this;
                sRecycledCount += 1;
            } else {
                next = null;
            }
            child = null;
        }
    }

    /**
     * 链表的遍历
     *
     * @param mFirst 链表表头
     */
    public static void travelsal(MyLinkedListObject mFirst) {
        MyLinkedListObject target = mFirst;

        while (target != null) {
            target = target.next;
        }
    }
}
