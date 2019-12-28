#include $(test-root-dir)mk/Android.mk

LOCAL_PATH:= $(call my-dir)

include $(CLEAR_VARS)

LOCAL_MODULE    := libimage-process
LOCAL_SRC_FILES := util/ImageUtils.cpp
LOCAL_LDLIBS += -llog -ljnigraphics

include $(BUILD_SHARED_LIBRARY)

#####################lib说明###################
#log 是Android输出日志的库
#jnigraphics 是一个很小的库，展示一个稳定的，基于C语言的接口，使本机代码安全地访问Java对象的像素缓冲区的位图.
