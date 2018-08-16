package com.apple.conchstore.live.widgets.recyclerview;

import android.view.View;

import com.chad.library.adapter.base.BaseViewHolder;

/**
 * - @Author:  闫世豪
 * - @Time:  2018/5/14 下午12:28
 * - @Email whynightcode@gmail.com
 */
public class MultipleViewHolder extends BaseViewHolder {
    public MultipleViewHolder(View view) {
        super(view);
    }
    public static MultipleViewHolder create(View view) {
        return new MultipleViewHolder(view);
    }

}
