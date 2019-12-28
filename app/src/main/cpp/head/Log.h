//
// Created by Administrator on 2017/5/24.
//

#ifndef NDKBASE_LOG_H
#define NDKBASE_LOG_H

#include <android/log.h>

#define   LOG_TAG    "JNI_LOG"
#define   LOGV(...)  __android_log_print(ANDROID_LOG_VERBOSE,LOG_TAG,__VA_ARGS__)
#define   LOGD(...)  __android_log_print(ANDROID_LOG_DEBUG,LOG_TAG,__VA_ARGS__)
#define   LOGI(...)  __android_log_print(ANDROID_LOG_INFO,LOG_TAG,__VA_ARGS__)
#define   LOGW(...)  __android_log_print(ANDROID_LOG_WARN,LOG_TAG,__VA_ARGS__)
#define   LOGE(...)  __android_log_print(ANDROID_LOG_ERROR,LOG_TAG,__VA_ARGS__)

#endif //NDKBASE_LOG_H
