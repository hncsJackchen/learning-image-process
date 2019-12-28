#include <jni.h>
#include "../head/Log.h"
#include <cstring>
#include <android/bitmap.h>
#include "../head/ImageConstant.h"

extern "C"
JNIEXPORT jstring JNICALL
Java_com_jack_learning_imageprocess_util_ImageUtils_test
        (JNIEnv *env, jobject /* this */) {
    LOGI("Java_com_jack_learning_imageprocess_util_ImageUtils_test() 被调用");
    return env->NewStringUTF("call libimage-process.so success.");
}

extern "C"
JNIEXPORT jint JNICALL
Java_com_jack_learning_imageprocess_util_ImageUtils_gray
        (JNIEnv *env, jobject object, jobject bmpObj) {
    LOGI("Java_com_jack_learning_imageprocess_util_ImageUtils_gray() 被调用");
    if (bmpObj == NULL) {
        LOGD("传入的图片参数不能为 null");
        return -1;//传入参数为空
    }

    //Get bitmap info
    AndroidBitmapInfo bitmapInfo;
    memset(&bitmapInfo, 0, sizeof(bitmapInfo));
    AndroidBitmap_getInfo(env, bmpObj, &bitmapInfo);

    //Check format,only RGB565 and RGBA are supported
    if (bitmapInfo.width <= 0 || bitmapInfo.height <= 0 ||
        (bitmapInfo.format != ANDROID_BITMAP_FORMAT_RGB_565 &&
         bitmapInfo.format != ANDROID_BITMAP_FORMAT_RGBA_8888)) {
        LOGE("invalid bitmap");
        return -1;
    }

    //lock the bitmap to get the buffer
    void * pixels = NULL;
    int res = AndroidBitmap_lockPixels(env, bmpObj, &pixels);
    if (pixels == NULL) {
        LOGE("fail to lock bitmap");
        return -1;
    }

    //handle pixels
    LOGD("Effect: %dx%d, %d\n", bitmapInfo.width, bitmapInfo.height, bitmapInfo.format);
    for (int h = 0; h < bitmapInfo.height; ++h) {
        for (int w = 0; w < bitmapInfo.width; ++w) {
            int a = 0, r = 0, g = 0, b = 0;
            void *pixel = NULL;
            // Get each pixel by format
            if (bitmapInfo.format == ANDROID_BITMAP_FORMAT_RGB_565) {
                pixel = ((uint16_t *) pixels) + h * bitmapInfo.width + w;
                uint16_t v = *(uint16_t *)pixel;
                r = RGB565_R(v);
                g = RGB565_G(v);
                b = RGB565_B(v);

            } else{
                pixel = ((uint32_t *) pixels) + h * bitmapInfo.width + w;
                uint32_t v = *(uint32_t *)pixel;
                a = RGBA_A(v);
                r = RGBA_R(v);
                g = RGBA_G(v);
                b = RGBA_B(v);
            }

            // Grayscale
            int gray = (r * 38 + g * 75 + b * 15) >> 7;

            // Write the pixel back
            if (bitmapInfo.format == ANDROID_BITMAP_FORMAT_RGB_565) {
                *((uint16_t *)pixel) = MAKE_RGB565(gray, gray, gray);
            } else {// RGBA
                *((uint32_t *)pixel) = MAKE_RGBA(gray, gray, gray, a);
            }
        }
    }

    //unlock the bitmap
    AndroidBitmap_unlockPixels(env, bmpObj);
    return 1;
}

extern "C"
JNIEXPORT jint JNICALL
Java_com_jack_learning_imageprocess_util_ImageUtils_handleImage
        (JNIEnv *env, jobject oj, jobject bmpObj) {
    LOGI("Java_com_jack_learning_imageprocess_util_ImageUtils_handleImage() 被调用");
    return 1;
}