package com.apple.conchstore.live.base;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.apple.conchstore.R;
import com.kaopiz.kprogresshud.KProgressHUD;


/**
 * - @Author:  闫世豪
 * - @Time:  2018/7/17 下午5:22
 * - @Email whynightcode@gmail.com
 */
public abstract class BaseFragment<V extends BaseMvpView, P extends BaseMvpPersenter<V>> extends Fragment implements BaseMvpView {
    protected KProgressHUD mKProgressHUD;
    private View mContentView;

    private P persenter;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        if (getLayoutId() instanceof Integer) {
            mContentView = inflater.inflate((int) getLayoutId(), container, false);
        } else if (getLayoutId() instanceof View) {
            mContentView = (View) getLayoutId();
        } else {
            throw new ClassCastException("type of setLayout() must be int or View!");
        }
        if (persenter == null) {
            persenter = createPresenter();
        }
        if (persenter == null) {
            throw new NullPointerException("presenter 不能为空!");
        }
        //绑定view
        persenter.attachMvpView((V) this);
        mKProgressHUD = KProgressHUD.create(getActivity())
                .setStyle(KProgressHUD.Style.SPIN_INDETERMINATE)
                .setWindowColor(getResources().getColor(R.color.gay))
                .setDimAmount(0.3f);
        onBindView(savedInstanceState, mContentView);
        return mContentView;
    }

    protected abstract void onBindView(@NonNull Bundle savedInstanceState, @NonNull View rootView);


    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        initData();
    }


    protected abstract void initData();

    protected abstract Object getLayoutId();

    /**
     * 创建Presenter
     *
     * @return 子类自己需要的Presenter
     */
    protected abstract P createPresenter();

    /**
     * 获取Presenter
     *
     * @return 返回子类创建的Presenter
     */
    public P getPresenter() {
        return persenter;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        //解除绑定
        if (persenter != null) {
            persenter.detachMvpView();
        }
    }
}
