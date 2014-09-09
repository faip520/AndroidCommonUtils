#
# 用来描述mk文件的配置项目的含义，以及提供几种常情形的mk文件例子
#

# 路径表示的时候，可以用..表示上级目录 例如：$(LOCAL_PATH)/../../include

# 定义变量 adb = dddf 就可以了 例如：
# libjpeg_SOURCES_DIST = a1w0n/common.c
# LOCAL_SRC_FILES:= $(libjpeg_SOURCES_DIST)

# 如果非源码环境下的jni编译，则这里的所有路径都是相对jni目录而言的

LOCAL_PATH:= $(call my-dir)
include $(CLEAR_VARS)

# 包含的源码文件的名字和相对路径
# 如果是jni路径下的/a1w0n/common.c 就要包含相对路径，示例如下
# 注意一行写不完时的写法
LOCAL_SRC_FILES:= a1w0n/common.c a1w0n/termExec.c  jcapimin.c jcapistd.c jccoefct.c jccolor.c \
        jcdctmgr.c jchuff.c jcinit.c jcmainct.c jcmarker.c jcmaster.c

# 需要包含的头文件
# 如果是在源码环境下通过jni编译so库，就需要包含$(JNI_H_INCLUDE)
# 在这里添加路径后，对应路径下的.h文件就可以在代码里头通过 #include "abc.h"的方式包含进来了
LOCAL_C_INCLUDES :=  \
	$(CEDARX_TOP)/include/include_camera \
	$(TARGET_HARDWARE_INCLUDE) \
	hardware/aw/CameraSource \
	frameworks\native\libs\utils \
	$(LOCAL_PATH)/../../include \
	$(JNI_H_INCLUDE)

LOCAL_FORCE_STATIC_EXECUTABLE := true

# 模块被编译出来后的名字，可以是可执行文件的名字，也可以是so库的文件
LOCAL_MODULE:= a1w0n

# 这里是想编译成什么东西，有好几种选择
include $(BUILD_EXECUTABLE)
#include $(BUILD_SHARED_LIBRARY)