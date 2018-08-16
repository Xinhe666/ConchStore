package com.apple.conchstore.live.utils;

import android.content.res.Resources;
import android.util.DisplayMetrics;

import com.apple.conchstore.live.AppManger;


public final class DimenUtil {

    public static int getScreenWidth() {
        final Resources resources = AppManger.getApplication().getResources();
        final DisplayMetrics dm = resources.getDisplayMetrics();
        return dm.widthPixels;
    }

    public static int getScreenHeight() {
        final Resources resources = AppManger.getApplication().getResources();
        final DisplayMetrics dm = resources.getDisplayMetrics();
        return dm.heightPixels;
    }
}
