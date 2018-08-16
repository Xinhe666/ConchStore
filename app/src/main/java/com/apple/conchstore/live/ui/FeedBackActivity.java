package com.apple.conchstore.live.ui;

import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.apple.conchstore.R;
import com.apple.conchstore.live.AppManger;
import com.apple.conchstore.live.base.BaseActivity;
import com.apple.conchstore.live.ui.main.MainMvpView;
import com.apple.conchstore.live.ui.main.MainPersenter;
import com.apple.conchstore.live.utils.ToastUtils;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class FeedBackActivity extends BaseActivity<MainMvpView, MainPersenter>
        implements MainMvpView {

    @BindView(R.id.toolbar_back)
    ImageView mToolbarBack;
    @BindView(R.id.toolbar_title)
    TextView mToolbarTitle;
    @BindView(R.id.toolbar)
    Toolbar mToolbar;
    @BindView(R.id.feedback_text)
    EditText mFeedbackText;
    @BindView(R.id.feedback_sumit)
    Button mFeedbackSumit;

    @Override
    protected int setLayout() {
        return R.layout.activity_feed_back;
    }

    @Override
    protected void initView() {
        ButterKnife.bind(this);
        mToolbarTitle.setText("反馈");
    }

    @Override
    protected void initData() {

    }

    @Override
    protected MainPersenter createPresenter() {
        return new MainPersenter(this);
    }


    @OnClick({R.id.toolbar_back, R.id.feedback_sumit})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.toolbar_back:
                finish();
                break;
            case R.id.feedback_sumit:
                boolean net = AppManger.isNet();
                if (net){
                    String s = mFeedbackText.getText().toString();
                    if (TextUtils.isEmpty(s)){
                        ToastUtils.showToast("您没有输入任何内容");
                    }else {
                       mFeedbackSumit.postDelayed(new Runnable() {
                           @Override
                           public void run() {
                               ToastUtils.showToast("感谢您的宝贵意见");
                           }
                       },1000);
                    }
                }else {
                    ToastUtils.showToast("当前网络无连接");
                }
                break;
        }
    }
}
