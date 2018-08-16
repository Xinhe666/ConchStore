package com.apple.conchstore.live.base;

/**
 * - @Author:  闫世豪
 * - @Time:  2018/8/8 下午12:38
 * - @Email whynightcode@gmail.com
 */
public abstract class BaseMvpPersenter <V extends BaseMvpView> {

    private V mMvpView;

    /**
     * 绑定V层
     *
     * @param view
     */
    public void attachMvpView(V view) {
        this.mMvpView = view;
    }

    /**
     * 解除绑定V层
     */
    public void detachMvpView() {
        mMvpView = null;
    }

    /**
     * 获取V层
     *
     * @return
     */
    public V getmMvpView() {
        return mMvpView;
    }

}
