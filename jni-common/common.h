#ifndef LOGHELPER_H_
#define LOGHELPER_H_

#include <android/log.h>

// include了这个头文件后，很多jni相关的变量 JniEnv jint等，才有定义
// 其他文件include了本文就后，就不用显示include <jni.h>了
#include <jni.h>

// 包含NULL定义
#include <stddef.h>

// 这里不定义LOG_TAG了，其他类想用LOG?方法，而又没定义LOG_TAG的话，会编译错误
//#ifndef LOG_TAG
//#define LOG_TAG "A1w0n_Log_Tag_Jni"
//#endif

#define LOGI(...) do { __android_log_print(ANDROID_LOG_INFO, LOG_TAG, __VA_ARGS__); } while(0)
#define LOGW(...) do { __android_log_print(ANDROID_LOG_WARN, LOG_TAG, __VA_ARGS__); } while(0)
#define LOGE(...) do { __android_log_print(ANDROID_LOG_ERROR, LOG_TAG, __VA_ARGS__); } while(0)
#define LOGD(...) do { __android_log_print(ANDROID_LOG_DEBUG, LOG_TAG, __VA_ARGS__); } while(0)

// 用来注册Jni函数的，注册过后的函数才能被java层的native方法调用
int registerNativeMethods(JNIEnv* env, const char* className, JNINativeMethod* gMethods, int numMethods);

#endif
