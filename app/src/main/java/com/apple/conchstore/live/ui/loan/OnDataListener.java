package com.apple.conchstore.live.ui.loan;


import com.apple.conchstore.live.widgets.recyclerview.MultipleItem;

import java.util.List;

/**
 * - @Author:  闫世豪
 * - @Time:  2018/8/13 下午12:43
 * - @Email whynightcode@gmail.com
 */
public interface OnDataListener {
    void onSuccess(List<MultipleItem> list);

    void onFailure(String error);
}
