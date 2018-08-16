package com.apple.conchstore.live.ui;

import com.apple.conchstore.R;
import com.apple.conchstore.live.base.BaseActivity;
import com.apple.conchstore.live.ui.main.MainMvpView;
import com.apple.conchstore.live.ui.main.MainPersenter;

import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * @author yanshihao
 */
public class AboutActivity extends BaseActivity<MainMvpView, MainPersenter> implements MainMvpView {


    @Override
    protected int setLayout() {
        return R.layout.activity_about;
    }

    @Override
    protected void initView() {
        ButterKnife.bind(this);

    }

    @Override
    protected void initData() {

    }

    @Override
    protected MainPersenter createPresenter() {
        return new MainPersenter(this);
    }

    @OnClick(R.id.back)
    public void onViewClicked() {
        finish();
    }
}
