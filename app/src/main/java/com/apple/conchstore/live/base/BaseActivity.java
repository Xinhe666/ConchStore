package com.apple.conchstore.live.base;

import android.annotation.SuppressLint;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;

import com.apple.conchstore.R;
import com.jaeger.library.StatusBarUtil;
import com.kaopiz.kprogresshud.KProgressHUD;
import com.umeng.analytics.MobclickAgent;


/**
 * - @Author:  闫世豪
 * - @Time:  2018/8/6 上午11:13
 * - @Email whynightcode@gmail.com
 */

public abstract class BaseActivity<V extends BaseMvpView, P extends BaseMvpPersenter<V>>
        extends AppCompatActivity implements BaseMvpView {

    protected KProgressHUD mKProgressHUD;

    private P presenter;
    protected String TAG;

    @SuppressLint("ResourceAsColor")
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (presenter == null) {
            presenter = createPresenter();
        }
        if (presenter == null) {
            throw new NullPointerException("presenter 不能为空!");
        }
        TAG = getComponentName().getShortClassName();
        //绑定view
        presenter.attachMvpView((V) this);
        setContentView(setLayout());
        StatusBarUtil.setColor(this, Color.parseColor("#51B1FA"), 40);

        mKProgressHUD = KProgressHUD.create(this)
                .setStyle(KProgressHUD.Style.SPIN_INDETERMINATE)
                .setWindowColor(getResources().getColor(R.color.gay))
                .setDimAmount(0.3f);
        initView();
        initData();
    }


    protected abstract int setLayout();

    protected abstract void initView();

    protected abstract void initData();

    @Override
    protected void onDestroy() {
        super.onDestroy();
        //解除绑定
        if (presenter != null) {
            presenter.detachMvpView();
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        MobclickAgent.onResume(this);
    }

    @Override
    public void onPause() {
        super.onPause();
        MobclickAgent.onPause(this);
    }

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
        return presenter;
    }
}
