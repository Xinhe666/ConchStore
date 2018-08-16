package com.apple.conchstore.live.ui.loan;

import com.apple.conchstore.live.base.BaseMvpView;
import com.apple.conchstore.live.widgets.recyclerview.MultipleItem;

import java.util.List;

/**
 * - @Author:  闫世豪
 * - @Time:  2018/8/13 上午10:39
 * - @Email whynightcode@gmail.com
 */
public interface ProductMvpView extends BaseMvpView {
    /**
     * 请求成功
     */
    void resultSuccess(List<MultipleItem> list);

    /**
     * 请求失败
     */
    void resultFailure(String error);

    /**
     * 加载更多
     *
     * @param list
     */
    void onLoadMore(List<MultipleItem> list);

    /**
     * 刷新
     *
     * @param list
     */
    void onRefresh(List<MultipleItem> list);

}
