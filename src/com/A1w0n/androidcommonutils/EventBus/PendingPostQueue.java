package com.A1w0n.androidcommonutils.EventBus;

/**
 * EventBus的事件队列，要注意的是，每个EventBus对象并不只有一个事件队列
 */
final class PendingPostQueue {

    private PendingPost head;
    private PendingPost tail;

    synchronized void enqueue(PendingPost pendingPost) {
        if (pendingPost == null) {
            throw new NullPointerException("null cannot be enqueued");
        }

        if (tail != null) {
            tail.next = pendingPost;
            tail = pendingPost;
        } else if (head == null) {
            head = tail = pendingPost;
        } else {
            throw new IllegalStateException("Head present, but no tail");
        }

        notifyAll();
    }

    synchronized PendingPost poll() {
        PendingPost pendingPost = head;

        if (head != null) {
            head = head.next;
            if (head == null) {
                tail = null;
            }
        }
        
        return pendingPost;
    }

    synchronized PendingPost poll(int maxMillisToWait) throws InterruptedException {
        if (head == null) {
            wait(maxMillisToWait);
        }

        return poll();
    }

}
