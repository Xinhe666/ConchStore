package com.apple.conchstore.live.utils;

import android.widget.Toast;

import com.apple.conchstore.live.AppManger;


/**
 *
 * @author apple
 * @date 2017/4/6
 */

public class ToastUtils {
    private static Toast toast;

    public static void showToast( String message) {
        if (toast == null) {
            toast = Toast.makeText(AppManger.getApplication(), message, Toast.LENGTH_SHORT);
        } else {
            toast.setText(message);
        }
        toast.show();
    }
}
