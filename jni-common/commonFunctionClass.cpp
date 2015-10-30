/*
 * Copyright (C) 2008 The Android Open Source Project
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

/*
 * Copyright (C) 2007 The Android Open Source Project
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

#include "common.h"

#define LOG_TAG "termExec"

#include <sys/types.h>
#include <sys/ioctl.h>
#include <sys/wait.h>
#include <errno.h>
#include <fcntl.h>
#include <stdlib.h>
#include <stdio.h>
#include <unistd.h>
#include <termios.h>
#include <signal.h>

#include "termExec.h"

static jclass class_fileDescriptor;
static jfieldID field_fileDescriptor_descriptor;
static jmethodID method_fileDescriptor_init;

typedef unsigned short char16_t;

static int throwOutOfMemoryError(JNIEnv *env, const char *message)
{
}

static int create_subprocess(const char *cmd,
    char *const argv[], char *const envp[], int* pProcessId)
{
}


static jobject android_os_Exec_createSubProcess(JNIEnv *env, jobject clazz,
    jstring cmd, jobjectArray args, jobjectArray envVars,
    jintArray processIdArray)
{
}


static void android_os_Exec_setPtyWindowSize(JNIEnv *env, jobject clazz,
    jobject fileDescriptor, jint row, jint col, jint xpixel, jint ypixel)
{
}

static void android_os_Exec_setPtyUTF8Mode(JNIEnv *env, jobject clazz,
    jobject fileDescriptor, jboolean utf8Mode)
{
}

static int android_os_Exec_waitFor(JNIEnv *env, jobject clazz,
    jint procId) {
}

static int android_os_Exec_test(JNIEnv *env, jobject clazz,
    jint procId) {
}

static void android_os_Exec_close(JNIEnv *env, jobject clazz, jobject fileDescriptor)
{
    int fd;

    fd = env->GetIntField(fileDescriptor, field_fileDescriptor_descriptor);

    if (env->ExceptionOccurred() != NULL) {
        return;
    }

    close(fd);
}

static void android_os_Exec_hangupProcessGroup(JNIEnv *env, jobject clazz,
    jint procId) {
    kill(-procId, SIGHUP);
}

/**
 * 测试java.io.FileDescriptor类是否存在，能否new，有没有descritor成员，有没有构造函数
 * 并且把对象，成员，和构造函数存起来
 */
static int register_FileDescriptor(JNIEnv *env)
{
}

// 这里填写Jni native 函数的封装类
static const char *classPathName = "com/A1w0n/androidcommonutils/JniUtils/Exec";

/**
 * 写法：
 *     括号 () 里头的是参数 紧跟着的是返回值 比如 ： (I)I 就是参数是一个int 返回值也是一个int
 *	   返回值void 用 V 表示
 *	   多个参数用分号分隔，非Java基本类型要用大写L开头加包名和类型
 *	   左大括号表示该类型的数组
 *
 *	被注册的函数，前面都有两个固定的参数：JNIEnv *env, jobject clazz
 */
static JNINativeMethod method_table[] = {
    { "createSubprocess", "(Ljava/lang/String;[Ljava/lang/String;[Ljava/lang/String;[I)Ljava/io/FileDescriptor;",
        (void*) android_os_Exec_createSubProcess },
    { "setPtyWindowSize", "(Ljava/io/FileDescriptor;IIII)V",
        (void*) android_os_Exec_setPtyWindowSize},
    { "setPtyUTF8Mode", "(Ljava/io/FileDescriptor;Z)V",
        (void*) android_os_Exec_setPtyUTF8Mode},
    { "waitFor", "(I)I",
        (void*) android_os_Exec_waitFor},
    { "test", "(I)I",
        (void*) android_os_Exec_test},
    { "close", "(Ljava/io/FileDescriptor;)V",
        (void*) android_os_Exec_close},
    { "hangupProcessGroup", "(I)V",
        (void*) android_os_Exec_hangupProcessGroup}
};

int init_Exec(JNIEnv *env) {
    if (register_FileDescriptor(env) < 0) {
        LOGE("Failed to register class java/io/FileDescriptor");
        return JNI_FALSE;
    }

    if (!registerNativeMethods(env, classPathName, method_table,
                 sizeof(method_table) / sizeof(method_table[0]))) {
        return JNI_FALSE;
    }

    return JNI_TRUE;
}
