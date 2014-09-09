#include "common.h"

#define LOG_TAG "common"

/**
 * 注册java的native函数
 */
int registerNativeMethods(JNIEnv* env, const char* className,
    JNINativeMethod* gMethods, int numMethods)
{
    jclass clazz;

    clazz = env->FindClass(className);
    if (clazz == NULL) {
        LOGE("Native registration unable to find class '%s'", className);
        return JNI_FALSE;
    }

    if (env->RegisterNatives(clazz, gMethods, numMethods) < 0) {
        LOGE("RegisterNatives failed for '%s'", className);
        return JNI_FALSE;
    }

    return JNI_TRUE;
}

typedef union {
    JNIEnv* env;
    void* venv;
} UnionJNIEnvToVoid;

/**
 * loadlibrary的时候，会触发这个函数
 */
jint JNI_OnLoad(JavaVM* vm, void* reserved) {
    UnionJNIEnvToVoid uenv;
    uenv.venv = NULL;
    jint result = -1;
    JNIEnv* env = NULL;

    LOGD("JNI_OnLoad_A1w0n_ScreenVideoCapture");

    if (vm->GetEnv(&uenv.venv, JNI_VERSION_1_4) != JNI_OK) {
        LOGE("ERROR: GetEnv failed");
        goto bail;
    }

    // 从VM获取到了JNIEnv对象
    env = uenv.env;

    // 每个类都需要分别在这里注册一下
    // init_Exec是Exec.cpp注册具体native的地方
//    if (init_Exec(env) != JNI_TRUE) {
//        LOGE("ERROR: init of Exec failed");
//        goto bail;
//    }

//    if (init2_Exec(env) != JNI_TRUE) {
//        LOGE("ERROR: init of Exec failed");
//        goto bail;
//    }

    result = JNI_VERSION_1_4;

bail:
    return result;
}

// =========================================================
/**
 * jstring 转换成 char* 的示例，用完记得释放
 */
static void jstring2CharPointer(JNIEnv *env, jobject obj, jstring javaString)
{
   const char *nativeString = (*env)->GetStringUTFChars(env, javaString, 0);

   // use your string

   (*env)->ReleaseStringUTFChars(env, javaString, nativeString);
}
