package com.apple.conchstore.live.ui.main;


import com.apple.conchstore.live.base.BaseMvpPersenter;

/**
 * - @Author:  闫世豪
 * - @Time:  2018/8/8 下午2:12
 * - @Email whynightcode@gmail.com
 */
public class MainPersenter extends BaseMvpPersenter<MainMvpView> {

    private MainMvpView mMainMvpView;

    public MainPersenter(MainMvpView mainMvpView) {
        mMainMvpView = mainMvpView;
    }

}
