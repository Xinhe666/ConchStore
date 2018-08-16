package com.apple.conchstore.live.utils;

import android.util.Log;

/**
 * - @Author:  闫世豪
 * - @Time:  2018/8/8 下午3:05
 * - @Email whynightcode@gmail.com
 */
public class LogUtils {

    private static boolean isLog = true;


    public static void e(String tag, String context) {
        if (isLog) {
            Log.e(tag, context+"");
        }
    }
}
