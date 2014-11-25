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

import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

/**
 * 一个查找类，用来查找某个类的事件接受函数，比如查找某个Activity的onEventMainThread函数
 */
class SubscriberMethodFinder {

    private static final int MODIFIERS_IGNORE = Modifier.ABSTRACT | Modifier.STATIC;
    // 一个静态的缓存，用来缓存某个订阅者(用类名来区分)的所有事件接收函数
    private static final Map<String, List<SubscriberMethod>> methodCache = new HashMap<String, List<SubscriberMethod>>();
    private static final Map<Class<?>, Class<?>> skipMethodVerificationForClasses = new ConcurrentHashMap<Class<?>, Class<?>>();

    /**
     *
     *
     * @param subscriberClass
     * @param eventMethodName
     * @return
     */
    List<SubscriberMethod> findSubscriberMethods(Class<?> subscriberClass, String eventMethodName) {
        String key = subscriberClass.getName() + '.' + eventMethodName;
        List<SubscriberMethod> subscriberMethods;

        synchronized (methodCache) {
            subscriberMethods = methodCache.get(key);
        }

        // 命中缓存，则直接返回
        if (subscriberMethods != null) {
            return subscriberMethods;
        }

        subscriberMethods = new ArrayList<SubscriberMethod>();
        Class<?> clazz = subscriberClass;
        // 已经找到的事件的类型(这个类型用特定格式的String来标识)
        HashSet<String> eventTypesFound = new HashSet<String>();
        StringBuilder methodKeyBuilder = new StringBuilder();

        while (clazz != null) {
            String name = clazz.getName();

            // 跳过系统类
            if (name.startsWith("java.") || name.startsWith("javax.") || name.startsWith("android.")) {
                // Skip system classes, this just degrades performance
                break;
            }

            // Starting with EventBus 2.2 we enforced methods to be public (might change with annotations again)
            // 返回clazz的所有方法
            Method[] methods = clazz.getMethods();
            // 遍历所有的方法
            for (Method method : methods) {
                // 获取方法的名字
                String methodName = method.getName();
                // 是否是约定的方法名前缀，一般是onEvent
                if (methodName.startsWith(eventMethodName)) {
                    int modifiers = method.getModifiers();
                    if ((modifiers & Modifier.PUBLIC) != 0 && (modifiers & MODIFIERS_IGNORE) == 0) {
                        // 所有参数的类型
                        Class<?>[] parameterTypes = method.getParameterTypes();
                        // 参数的个数只能是一个
                        if (parameterTypes.length == 1) {
                            // 获取方法名的后缀
                            String modifierString = methodName.substring(eventMethodName.length());
                            ThreadMode threadMode;

                            if (modifierString.length() == 0) { // 默认在事件发布者的线程接收和处理事件
                                threadMode = ThreadMode.PostThread;
                            } else if (modifierString.equals("MainThread")) { // 在主线程接收和处理事件
                                threadMode = ThreadMode.MainThread;
                            } else if (modifierString.equals("BackgroundThread")) {
                                threadMode = ThreadMode.BackgroundThread;
                            } else if (modifierString.equals("Async")) {
                                threadMode = ThreadMode.Async;
                            } else {
                                if (skipMethodVerificationForClasses.containsKey(clazz)) {
                                    continue;
                                } else {
                                    throw new EventBusException("Illegal onEvent method, check for typos: " + method);
                                }
                            }

                            // 获取一个参数的类型
                            Class<?> eventType = parameterTypes[0];
                            methodKeyBuilder.setLength(0);
                            methodKeyBuilder.append(methodName);
                            methodKeyBuilder.append('>').append(eventType.getName());
                            String methodKey = methodKeyBuilder.toString();
                            if (eventTypesFound.add(methodKey)) {
                                // Only add if not already found in a sub class
                                subscriberMethods.add(new SubscriberMethod(method, threadMode, eventType));
                            }
                        }
                    } else if (!skipMethodVerificationForClasses.containsKey(clazz)) {
                        // 如果这个列表里头没有对应的类

                        Log.d(EventBus.TAG, "Skipping method (not public, static or abstract): " + clazz + "."
                                + methodName);
                    }
                }
            }

            clazz = clazz.getSuperclass();
        }

        if (subscriberMethods.isEmpty()) {
            throw new EventBusException("Subscriber " + subscriberClass + " has no public methods called "
                    + eventMethodName);
        } else {
            synchronized (methodCache) {
                methodCache.put(key, subscriberMethods);
            }
            return subscriberMethods;
        }
    }

    static void clearCaches() {
        synchronized (methodCache) {
            methodCache.clear();
        }
    }

    static void skipMethodVerificationFor(Class<?> clazz) {
        if (!methodCache.isEmpty()) {
            throw new IllegalStateException("This method must be called before registering anything");
        }
        skipMethodVerificationForClasses.put(clazz, clazz);
    }

    public static void clearSkipMethodVerifications() {
        skipMethodVerificationForClasses.clear();
    }
}
