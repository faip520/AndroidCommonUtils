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

import android.os.Looper;
import android.util.Log;

import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * EventBus is a central publish/subscribe event system for Android. Events are posted ({@link #post(Object)} to the
 * bus, which delivers it to subscribers that have matching handler methods for the event type. To receive events,
 * subscribers must register themselves to the bus using the {@link #register(Object)} method. Once registered,
 * subscribers receive events until the call of {@link #unregister(Object)}. By convention, event handling methods must
 * be named "onEvent", be public, return nothing (void), and have exactly one parameter (the event).
 * 
 * @author Markus Junginger, greenrobot
 */
public class EventBus {

    // 线程池
    static ExecutorService executorService = Executors.newCachedThreadPool();

    /** Log tag, apps may override it. */
    public static String TAG = "UxEventBus";

    private static volatile EventBus defaultInstance;

    private static final String DEFAULT_METHOD_NAME = "onEvent";
    private static final Map<Class<?>, List<Class<?>>> eventTypesCache = new HashMap<Class<?>, List<Class<?>>>();

    private final Map<Class<?>, CopyOnWriteArrayList<Subscription>> subscriptionsByEventType;
    private final Map<Object, List<Class<?>>> typesBySubscriber;
    private final Map<Class<?>, Object> stickyEvents;

    // 新建一个线程本地变量
    private final ThreadLocal<PostingThreadState> currentPostingThreadState = new ThreadLocal<PostingThreadState>() {
        @Override
        protected PostingThreadState initialValue() {
            return new PostingThreadState();
        }
    };


    private final HandlerPoster mainThreadPoster;
    // 实际上就是一个Runnable
    private final BackgroundPoster backgroundPoster;
    private final AsyncPoster asyncPoster;
    private final SubscriberMethodFinder subscriberMethodFinder;

    private boolean subscribed;
    private boolean logSubscriberExceptions;

    /** Convenience singleton for apps using a process-wide EventBus instance. */
    public static EventBus getDefault() {
        if (defaultInstance == null) {
            synchronized (EventBus.class) {
                if (defaultInstance == null) {
                    defaultInstance = new EventBus();
                }
            }
        }
        return defaultInstance;
    }

    /** For unit test primarily. */
    public static void clearCaches() {
        SubscriberMethodFinder.clearCaches();
        eventTypesCache.clear();
    }

    /**
     * Method name verification is done for methods starting with onEvent to avoid typos; using this method you can
     * exclude subscriber classes from this check. Also disables checks for method modifiers (public, not static nor
     * abstract).
     */
    public static void skipMethodVerificationFor(Class<?> clazz) {
        SubscriberMethodFinder.skipMethodVerificationFor(clazz);
    }

    /** For unit test primarily. */
    public static void clearSkipMethodNameVerifications() {
        SubscriberMethodFinder.clearSkipMethodVerifications();
    }

    /**
     * Creates a new EventBus instance; each instance is a separate scope in which events are delivered. To use a
     * central bus, consider {@link #getDefault()}.
     */
    public EventBus() {
        subscriptionsByEventType = new HashMap<Class<?>, CopyOnWriteArrayList<Subscription>>();
        typesBySubscriber = new HashMap<Object, List<Class<?>>>();
        stickyEvents = new ConcurrentHashMap<Class<?>, Object>();
        mainThreadPoster = new HandlerPoster(this, Looper.getMainLooper(), 10);
        backgroundPoster = new BackgroundPoster(this);
        asyncPoster = new AsyncPoster(this);
        subscriberMethodFinder = new SubscriberMethodFinder();
        logSubscriberExceptions = true;
    }

    /**
     * Before registering any subscribers, use this method to configure if EventBus should log exceptions thrown by
     * subscribers (default: true).
     */
    public void configureLogSubscriberExceptions(boolean logSubscriberExceptions) {
        if (subscribed) {
            throw new EventBusException("This method must be called before any registration");
        }
        this.logSubscriberExceptions = logSubscriberExceptions;
    }

    /**
     * Registers the given subscriber to receive events. Subscribers must call {@link #unregister(Object)} once they are
     * no longer interested in receiving events.
     * 
     * Subscribers have event handling methods that are identified by their name, typically called "onEvent". Event
     * handling methods must have exactly one parameter, the event. If the event handling method is to be called in a
     * specific thread, a modifier is appended to the method name. Valid modifiers match one of the {@link ThreadMode}
     * enums. For example, if a method is to be called in the UI/main thread by EventBus, it would be called
     * "onEventMainThread".
     */
    public void register(Object subscriber) {
        register(subscriber, DEFAULT_METHOD_NAME, false, 0);
    }

    /**
     * Like {@link #register(Object)} with an additional subscriber priority to influence the order of event delivery.
     * Within the same delivery thread ({@link ThreadMode}), higher priority subscribers will receive events before
     * others with a lower priority. The default priority is 0. Note: the priority does *NOT* affect the order of
     * delivery among subscribers with different {@link ThreadMode}s!
     */
    public void register(Object subscriber, int priority) {
        register(subscriber, DEFAULT_METHOD_NAME, false, priority);
    }

    /**
     * @deprecated For simplification of the API, this method will be removed in the future.
     */
    @Deprecated
    public void register(Object subscriber, String methodName) {
        register(subscriber, methodName, false, 0);
    }

    /**
     * Like {@link #register(Object)}, but also triggers delivery of the most recent sticky event (posted with
     * {@link #postSticky(Object)}) to the given subscriber.
     */
    public void registerSticky(Object subscriber) {
        register(subscriber, DEFAULT_METHOD_NAME, true, 0);
    }

    /**
     * Like {@link #register(Object,int)}, but also triggers delivery of the most recent sticky event (posted with
     * {@link #postSticky(Object)}) to the given subscriber.
     */
    public void registerSticky(Object subscriber, int priority) {
        register(subscriber, DEFAULT_METHOD_NAME, true, priority);
    }

    /**
     * @deprecated For simplification of the API, this method will be removed in the future.
     */
    @Deprecated
    public void registerSticky(Object subscriber, String methodName) {
        register(subscriber, methodName, true, 0);
    }

    /**
     *
     *
     * @param subscriber 事件订阅者的对象
     * @param methodName 事件订阅者们约定的事件接受和处理函数的前缀，默认是onEvent，也可以是其他的
     * @param sticky 是否是粘滞性事件
     * @param priority 优先级，默认是0
     */
    private synchronized void register(Object subscriber, String methodName, boolean sticky, int priority) {
        // 查找某个类(事件订阅者)的所有事件接收函数
        List<SubscriberMethod> subscriberMethods = subscriberMethodFinder.findSubscriberMethods(subscriber.getClass(),
                methodName);
        for (SubscriberMethod subscriberMethod : subscriberMethods) {
            subscribe(subscriber, subscriberMethod, sticky, priority);
        }
    }

    /**
     * @deprecated For simplification of the API, this method will be removed in the future.
     */
    @Deprecated
    public void register(Object subscriber, Class<?> eventType, Class<?>... moreEventTypes) {
        register(subscriber, DEFAULT_METHOD_NAME, false, eventType, moreEventTypes);
    }

    /**
     * @deprecated For simplification of the API, this method will be removed in the future.
     */
    @Deprecated
    public void register(Object subscriber, String methodName, Class<?> eventType, Class<?>... moreEventTypes) {
        register(subscriber, methodName, false, eventType, moreEventTypes);
    }

    /**
     * @deprecated For simplification of the API, this method will be removed in the future.
     */
    @Deprecated
    public void registerSticky(Object subscriber, Class<?> eventType, Class<?>... moreEventTypes) {
        register(subscriber, DEFAULT_METHOD_NAME, true, eventType, moreEventTypes);
    }

    /**
     * @deprecated For simplification of the API, this method will be removed in the future.
     */
    @Deprecated
    public void registerSticky(Object subscriber, String methodName, Class<?> eventType, Class<?>... moreEventTypes) {
        register(subscriber, methodName, true, eventType, moreEventTypes);
    }

    private synchronized void register(Object subscriber, String methodName, boolean sticky, Class<?> eventType,
            Class<?>... moreEventTypes) {
        Class<?> subscriberClass = subscriber.getClass();
        List<SubscriberMethod> subscriberMethods = subscriberMethodFinder.findSubscriberMethods(subscriberClass,
                methodName);
        for (SubscriberMethod subscriberMethod : subscriberMethods) {
            if (eventType == subscriberMethod.eventType) {
                subscribe(subscriber, subscriberMethod, sticky, 0);
            } else if (moreEventTypes != null) {
                for (Class<?> eventType2 : moreEventTypes) {
                    if (eventType2 == subscriberMethod.eventType) {
                        subscribe(subscriber, subscriberMethod, sticky, 0);
                        break;
                    }
                }
            }
        }
    }

    // Must be called in synchronized block
    private void subscribe(Object subscriber, SubscriberMethod subscriberMethod, boolean sticky, int priority) {
        subscribed = true;
        Class<?> eventType = subscriberMethod.eventType;
        CopyOnWriteArrayList<Subscription> subscriptions = subscriptionsByEventType.get(eventType);
        Subscription newSubscription = new Subscription(subscriber, subscriberMethod, priority);
        if (subscriptions == null) {
            subscriptions = new CopyOnWriteArrayList<Subscription>();
            subscriptionsByEventType.put(eventType, subscriptions);
        } else {
            for (Subscription subscription : subscriptions) {
                if (subscription.equals(newSubscription)) {
                    throw new EventBusException("Subscriber " + subscriber.getClass() + " already registered to event "
                            + eventType);
                }
            }
        }

        // Starting with EventBus 2.2 we enforced methods to be public (might change with annotations again)
        // subscriberMethod.method.setAccessible(true);

        int size = subscriptions.size();
        for (int i = 0; i <= size; i++) {
            if (i == size || newSubscription.priority > subscriptions.get(i).priority) {
                subscriptions.add(i, newSubscription);
                break;
            }
        }

        List<Class<?>> subscribedEvents = typesBySubscriber.get(subscriber);
        if (subscribedEvents == null) {
            subscribedEvents = new ArrayList<Class<?>>();
            typesBySubscriber.put(subscriber, subscribedEvents);
        }
        subscribedEvents.add(eventType);

        if (sticky) {
            Object stickyEvent;
            synchronized (stickyEvents) {
                stickyEvent = stickyEvents.get(eventType);
            }
            if (stickyEvent != null) {
                // If the subscriber is trying to abort the event, it will fail (event is not tracked in posting state)
                // --> Strange corner case, which we don't take care of here.
                postToSubscription(newSubscription, stickyEvent, Looper.getMainLooper() == Looper.myLooper());
            }
        }
    }

    public synchronized boolean isRegistered(Object subscriber) {
        return typesBySubscriber.containsKey(subscriber);
    }

    /**
     * @deprecated For simplification of the API, this method will be removed in the future.
     */
    @Deprecated
    public synchronized void unregister(Object subscriber, Class<?>... eventTypes) {
        if (eventTypes.length == 0) {
            throw new IllegalArgumentException("Provide at least one event class");
        }
        List<Class<?>> subscribedClasses = typesBySubscriber.get(subscriber);
        if (subscribedClasses != null) {
            for (Class<?> eventType : eventTypes) {
                unubscribeByEventType(subscriber, eventType);
                subscribedClasses.remove(eventType);
            }
            if (subscribedClasses.isEmpty()) {
                typesBySubscriber.remove(subscriber);
            }
        } else {
            Log.w(TAG, "Subscriber to unregister was not registered before: " + subscriber.getClass());
        }
    }

    /** Only updates subscriptionsByEventType, not typesBySubscriber! Caller must update typesBySubscriber. */
    private void unubscribeByEventType(Object subscriber, Class<?> eventType) {
        List<Subscription> subscriptions = subscriptionsByEventType.get(eventType);
        if (subscriptions != null) {
            int size = subscriptions.size();
            for (int i = 0; i < size; i++) {
                Subscription subscription = subscriptions.get(i);
                if (subscription.subscriber == subscriber) {
                    subscription.active = false;
                    subscriptions.remove(i);
                    i--;
                    size--;
                }
            }
        }
    }

    /** Unregisters the given subscriber from all event classes. */
    public synchronized void unregister(Object subscriber) {
        List<Class<?>> subscribedTypes = typesBySubscriber.get(subscriber);

        if (subscribedTypes != null) {
            for (Class<?> eventType : subscribedTypes) {
                unubscribeByEventType(subscriber, eventType);
            }
            typesBySubscriber.remove(subscriber);
        } else {
            Log.w(TAG, "Subscriber to unregister was not registered before: " + subscriber.getClass());
        }
    }

    /** Posts the given event to the event bus. */
    public void post(Object event) {
        // 获取当前线程的ThreadLocal本地变量中存储的PostingThreadState对象
        PostingThreadState postingState = currentPostingThreadState.get();
        // 获取当前线程的事件队列
        List<Object> eventQueue = postingState.eventQueue;
        // 在事件队列Array里头添加本次的事件
        eventQueue.add(event);

        if (postingState.isPosting) {
            return;
        } else {
            // 判断当前线程是否是UI线程，并且保存该值到ThreadLocal中的PostingThreadState对象里头
            postingState.isMainThread = Looper.getMainLooper() == Looper.myLooper();
            // 设置标志位，表明该线程正在发送事件
            postingState.isPosting = true;
            if (postingState.canceled) {
                throw new EventBusException("Internal error. Abort state was not reset");
            }
            try {
                // 分发事件队列里头的所有事件
                while (!eventQueue.isEmpty()) {
                    postSingleEvent(eventQueue.remove(0), postingState);
                }
            } finally {
                postingState.isPosting = false;
                postingState.isMainThread = false;
            }
        }
    }

    /**
     * Called from a subscriber's event handling method, further event delivery will be canceled. Subsequent subscribers
     * won't receive the event. Events are usually canceled by higher priority subscribers (see
     * {@link #register(Object, int)}). Canceling is restricted to event handling methods running in posting thread
     * {@link ThreadMode#PostThread}.
     */
    public void cancelEventDelivery(Object event) {
        PostingThreadState postingState = currentPostingThreadState.get();
        if (!postingState.isPosting) {
            throw new EventBusException(
                    "This method may only be called from inside event handling methods on the posting thread");
        } else if (event == null) {
            throw new EventBusException("Event may not be null");
        } else if (postingState.event != event) {
            throw new EventBusException("Only the currently handled event may be aborted");
        } else if (postingState.subscription.subscriberMethod.threadMode != ThreadMode.PostThread) {
            throw new EventBusException(" event handlers may only abort the incoming event");
        }

        postingState.canceled = true;
    }

    /**
     * Posts the given event to the event bus and holds on to the event (because it is sticky). The most recent sticky
     * event of an event's type is kept in memory for future access. This can be {@link #registerSticky(Object)} or
     * {@link #getStickyEvent(Class)}.
     */
    public void postSticky(Object event) {
        synchronized (stickyEvents) {
            stickyEvents.put(event.getClass(), event);
        }
        // Should be posted after it is putted, in case the subscriber wants to remove immediately
        post(event);
    }

    /**
     * Gets the most recent sticky event for the given type.
     * 
     * @see #postSticky(Object)
     */
    public <T> T getStickyEvent(Class<T> eventType) {
        synchronized (stickyEvents) {
            return eventType.cast(stickyEvents.get(eventType));
        }
    }

    /**
     * Remove and gets the recent sticky event for the given event type.
     * 
     * @see #postSticky(Object)
     */
    public <T> T removeStickyEvent(Class<T> eventType) {
        synchronized (stickyEvents) {
            return eventType.cast(stickyEvents.remove(eventType));
        }
    }

    /**
     * Removes the sticky event if it equals to the given event.
     * 
     * @return true if the events matched and the sticky event was removed.
     */
    public boolean removeStickyEvent(Object event) {
        synchronized (stickyEvents) {
            Class<? extends Object> eventType = event.getClass();
            Object existingEvent = stickyEvents.get(eventType);
            if (event.equals(existingEvent)) {
                stickyEvents.remove(eventType);
                return true;
            } else {
                return false;
            }
        }
    }

    /**
     * Removes all sticky events.
     */
    public void removeAllStickyEvents() {
        synchronized (stickyEvents) {
            stickyEvents.clear();
        }
    }

    /**
     * 分发事件队列中的单个事件
     *
     * @param event        需要发送出去的事件
     * @param postingState
     * @throws Error
     */
    private void postSingleEvent(Object event, PostingThreadState postingState) throws Error {
        // 获取事件的Class类对象
        Class<? extends Object> eventClass = event.getClass();
        // 调用完这一行，eventTypes里头就包含了，event本身的class(含这些class的所有父类)，和event所implements的所有类(和这些类的所有
        // 父类，以及这些类的所有父类所implements的类)
        List<Class<?>> eventTypes = findEventTypes(eventClass);

        boolean subscriptionFound = false;

        // 遍历eventTypes里头所有的类
        int countTypes = eventTypes.size();
        for (int h = 0; h < countTypes; h++) {
            Class<?> clazz = eventTypes.get(h);
            CopyOnWriteArrayList<Subscription> subscriptions;

            // 尝试从缓存里头获取Event类对应的所有订阅者
            synchronized (this) {
                subscriptions = subscriptionsByEventType.get(clazz);
            }

            // 如果订阅者列表不为空
            if (subscriptions != null && !subscriptions.isEmpty()) {
                for (Subscription subscription : subscriptions) {
                    postingState.event = event;
                    postingState.subscription = subscription;
                    boolean aborted = false;

                    try {
                        postToSubscription(subscription, event, postingState.isMainThread);
                        aborted = postingState.canceled;
                    } finally {
                        postingState.event = null;
                        postingState.subscription = null;
                        postingState.canceled = false;
                    }
                    if (aborted) {
                        break;
                    }
                }
                subscriptionFound = true;
            }
        }

        if (!subscriptionFound) {
            Log.d(TAG, "No subscribers registered for event " + eventClass);
            if (eventClass != NoSubscriberEvent.class && eventClass != SubscriberExceptionEvent.class) {
                post(new NoSubscriberEvent(this, event));
            }
        }
    }

    /**
     * 把消息发布到某一个消息订阅者
     *
     * @param subscription 某一个消息订阅者
     * @param event 消息发布者所发布的消息
     * @param isMainThread 消息发布者，是不是在主线程里头发布的
     */
    private void postToSubscription(Subscription subscription, Object event, boolean isMainThread) {
        switch (subscription.subscriberMethod.threadMode) {
            case PostThread:
                invokeSubscriber(subscription, event);
                break;
            case MainThread:
                if (isMainThread) {
                    invokeSubscriber(subscription, event);
                } else {
                    mainThreadPoster.enqueue(subscription, event);
                }
                break;
            case BackgroundThread:
                if (isMainThread) {
                    backgroundPoster.enqueue(subscription, event);
                } else {
                    invokeSubscriber(subscription, event);
                }
                break;
            case Async:
                asyncPoster.enqueue(subscription, event);
                break;
            default:
                throw new IllegalStateException("Unknown thread mode: " + subscription.subscriberMethod.threadMode);
        }
    }

    /**
     * Finds all Class objects including super classes and interfaces.
     *
     * @param eventClass EventBus事件对应的类的class对象
     * @return
     */
    private List<Class<?>> findEventTypes(Class<?> eventClass) {
        synchronized (eventTypesCache) {
            // 尝试从cache中获取
            List<Class<?>> eventTypes = eventTypesCache.get(eventClass);
            // 如果cache中获取不到
            if (eventTypes == null) {
                eventTypes = new ArrayList<Class<?>>();
                Class<?> clazz = eventClass;
                while (clazz != null) {
                    eventTypes.add(clazz);
                    // getInterfaces会返回clazz对象源码里头声明的时候，implements了哪些接口
                    addInterfaces(eventTypes, clazz.getInterfaces());
                    clazz = clazz.getSuperclass();
                }
                eventTypesCache.put(eventClass, eventTypes);
            }

            return eventTypes;
        }
    }

    /**
     * Recurses through super interfaces.
     *
     * @param eventTypes 包含Event对象所对应的类的class对象，同时也包含，Event对象的所有父类
     *                   的class对象
     * @param interfaces event对象对应的类的class所现实的所有接口，或者是，event对象对应的类
     *                   的父类所现实的所有接口
     */
    static void addInterfaces(List<Class<?>> eventTypes, Class<?>[] interfaces) {
        // 遍历实现的所有接口(也就是声明class对象的时候，implements了哪些接口)
        for (Class<?> interfaceClass : interfaces) {
            // 如果eventTypes里头还没有包含这个类
            if (!eventTypes.contains(interfaceClass)) {
                eventTypes.add(interfaceClass);
                // getInterfaces会返回clazz对象源码里头声明的时候，implements了哪些接口
                // 这里是递归调用
                addInterfaces(eventTypes, interfaceClass.getInterfaces());
            }
        }
    }

    /**
     * Invokes the subscriber if the subscriptions is still active. Skipping subscriptions prevents race conditions
     * between {@link #unregister(Object)} and event delivery. Otherwise the event might be delivered after the
     * subscriber unregistered. This is particularly important for main thread delivery and registrations bound to the
     * live cycle of an Activity or Fragment.
     *
     * 考虑这样一种情形：两个线程，一个是主线程A，一个是后台线程B，主线程想反注册某一个Activity C，线程B已经准备调用Activity C的
     * 事件接受函数，这样最后是什么情况，取决于两个线程的调度顺序
     */
    void invokeSubscriber(PendingPost pendingPost) {
        Object event = pendingPost.event;
        Subscription subscription = pendingPost.subscription;
        PendingPost.releasePendingPost(pendingPost);

        if (subscription.active) {
            invokeSubscriber(subscription, event);
        }
    }

    /**
     * 调用某一个订阅者的事件接收函数，让其接收并处理该事件
     *
     * @param subscription 某一个订阅者
     * @param event 消息发布者发出来的消息
     * @throws Error
     */
    void invokeSubscriber(Subscription subscription, Object event) throws Error {
        try {
            // 调用消息订阅者的消息接收函数
            subscription.subscriberMethod.method.invoke(subscription.subscriber, event);
        } catch (InvocationTargetException e) {
            Throwable cause = e.getCause();
            if (event instanceof SubscriberExceptionEvent) {
                // Don't send another SubscriberExceptionEvent to avoid infinite event recursion, just log
                Log.e(TAG, "SubscriberExceptionEvent subscriber " + subscription.subscriber.getClass()
                        + " threw an exception", cause);
                SubscriberExceptionEvent exEvent = (SubscriberExceptionEvent) event;
                Log.e(TAG, "Initial event " + exEvent.causingEvent + " caused exception in "
                        + exEvent.causingSubscriber, exEvent.throwable);
            } else {
                if (logSubscriberExceptions) {
                    Log.e(TAG, "Could not dispatch event: " + event.getClass() + " to subscribing class "
                            + subscription.subscriber.getClass(), cause);
                }
                SubscriberExceptionEvent exEvent = new SubscriberExceptionEvent(this, cause, event,
                        subscription.subscriber);
                post(exEvent);
            }
        } catch (IllegalAccessException e) {
            throw new IllegalStateException("Unexpected exception", e);
        }
    }

    /** For ThreadLocal, much faster to set (and get multiple values). */
    final static class PostingThreadState {
        List<Object> eventQueue = new ArrayList<Object>();
        // 是否正在发送事件
        boolean isPosting;
        boolean isMainThread;
        // 订阅者？
        Subscription subscription;
        Object event;
        boolean canceled;
    }

    // Just an idea: we could provide a callback to post() to be notified, an alternative would be events, of course...
    /* public */interface PostCallback {
        void onPostCompleted(List<SubscriberExceptionEvent> exceptionEvents);
    }

}
