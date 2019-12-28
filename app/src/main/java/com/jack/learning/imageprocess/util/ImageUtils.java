package com.jack.learning.imageprocess.util;

import android.graphics.Bitmap;

/*
 * author: Jack
 * created time:2019/12/28 19:03
 * description: 图像处理工具
 */
public class ImageUtils {

    static {
        //生成的so库名字为libimage-process.so
        System.loadLibrary("image-process");
    }

    /**
     * 测试so库是否调用成功
     *
     * @return 测试字符串
     */
    public native String test();

    /**
     * 图片灰度化
     * @param in 输入图像
     * @return
     */
    public native int gray(Bitmap in);

    /**
     * 处理图片
     * @param in 输入图片
     * @return
     */
    public native int handleImage(Bitmap in);

}
