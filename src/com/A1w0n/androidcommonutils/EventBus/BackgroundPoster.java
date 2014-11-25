/*
 * Copyright (C) 2012 Markus Junginger, greenrobot (http://greenrobot.de)
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.A1w0n.androidcommonutils.EventBus;

import android.util.Log;

/**
 * Posts events in background.
 *
 * 如果事件发布者是非UI线程，则会直接在发布者的线程调用订阅者的事件接收函数
 * 如果事件发布者是UI线程，则会通过后台线程来调用事件订阅者的事件接收函数
 *
 * 每个EventBus都会有一个BackgroundPoster对象
 * 
 * @author Markus
 */
final class BackgroundPoster implements Runnable {

    private final PendingPostQueue queue;
    private volatile boolean executorRunning;

    private final EventBus eventBus;

    BackgroundPoster(EventBus eventBus) {
        this.eventBus = eventBus;
        queue = new PendingPostQueue();
    }

    /**
     * 考虑这样一种情形，两个线程A、B，他们都调用EventBus.getDefault().post()，而刚好他们两的事件都分别有一个
     * onEventBackGround订阅者，则两者都会进来尝试调用这个方法，假设A先进来这个方法的synchronize函数块，
     * 出发EventBus.executorService.execute(this)，并且设置executorRunning = true
     * 则线程B就会因为executorRunning == true，而无法调用EventBus.executorService.execute(this)
     *
     * 这种情况肿么办？
     *
     * 其实这种情况没关系，因为run方法体里头会从事件队列里头取出所有的事件并且分发出去，反倒是如果两个同时运行，
     * 则有点多余了
     *
     * @param subscription
     * @param event
     */
    public void enqueue(Subscription subscription, Object event) {
        PendingPost pendingPost = PendingPost.obtainPendingPost(subscription, event);

        synchronized (this) {
            queue.enqueue(pendingPost);
            if (!executorRunning) {
                executorRunning = true;
                EventBus.executorService.execute(this);
            }
        }
    }

    @Override
    public void run() {
        try {
            try {
                while (true) {
                    PendingPost pendingPost = queue.poll(1000);

                    if (pendingPost == null) {
                        synchronized (this) {
                            // Check again, this time in synchronized
                            pendingPost = queue.poll();
                            if (pendingPost == null) {
                                executorRunning = false;
                                // 如果没有事件了，则退出while循环，并且结束运行
                                return;
                            }
                        }
                    }

                    eventBus.invokeSubscriber(pendingPost);
                }
            } catch (InterruptedException e) {
                Log.w("Event", Thread.currentThread().getName() + " was interruppted", e);
            }
        } finally {
            executorRunning = false;
        }
    }

}
