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

import java.util.ArrayList;
import java.util.List;

/**
 * EventBus的事件队列中的某个事件封装，事件队列并没有用任何用来装载事件的容器，
 * 而是靠事件队列中的每一个事件的next指针(指向队列中的下一个事件)来运作队列的
 *
 * 这个事件封装里头包含一个事件的对象池，用于复用本类的对象
 */
final class PendingPost {

    // 本类对象的对象池，用来复用本类的对象
    private final static List<PendingPost> pendingPostPool = new ArrayList<PendingPost>();

    // 事件发布者发布出来的事件
    Object event;
    Subscription subscription;
    PendingPost next;

    /**
     * 外部没有办法调用这个构造函数
     *
     * @param event
     * @param subscription
     */
    private PendingPost(Object event, Subscription subscription) {
        this.event = event;
        this.subscription = subscription;
    }

    /**
     * 从对象池里头获取一个本类的对象，外部类是没办法new本类的对象的，只能通过这个方法来获取
     * 本类的对象
     *
     * @param subscription
     * @param event
     * @return
     */
    static PendingPost obtainPendingPost(Subscription subscription, Object event) {
        synchronized (pendingPostPool) {
            int size = pendingPostPool.size();
            if (size > 0) {
                // 返回对象池最后一个给调用者
                PendingPost pendingPost = pendingPostPool.remove(size - 1);
                pendingPost.event = event;
                pendingPost.subscription = subscription;
                pendingPost.next = null;
                return pendingPost;
            }
        }

        return new PendingPost(event, subscription);
    }

    /**
     * 调用者用完了本类的对象，调用这个接口来把本类的对象缓存进对象池里头
     *
     * @param pendingPost
     */
    static void releasePendingPost(PendingPost pendingPost) {
        pendingPost.event = null;
        pendingPost.subscription = null;
        pendingPost.next = null;

        synchronized (pendingPostPool) {
            // Don't let the pool grow indefinitely
            // 防止对象池无限增长
            if (pendingPostPool.size() < 10000) {
                pendingPostPool.add(pendingPost);
            }
        }
    }

}