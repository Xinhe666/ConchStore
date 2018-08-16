package com.apple.conchstore.live.ui.login;

import android.content.Intent;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RelativeLayout;

import com.apple.conchstore.R;
import com.apple.conchstore.live.base.BaseActivity;
import com.apple.conchstore.live.bean.MessageEvent;
import com.apple.conchstore.live.ui.html.HtmlActivity;
import com.apple.conchstore.live.utils.CaptchaTimeCount;
import com.apple.conchstore.live.utils.CommonUtil;
import com.apple.conchstore.live.utils.Contents;
import com.apple.conchstore.live.utils.SPUtil;
import com.apple.conchstore.live.utils.ToastUtils;
import com.apple.conchstore.live.widgets.SlideView;
import com.kaopiz.kprogresshud.KProgressHUD;

import org.greenrobot.eventbus.EventBus;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * @author yanshihao
 */
public class LoginActivity extends BaseActivity<LoginMvpView, LoginPersenter>
        implements LoginMvpView, VerListener, SlideView.OnSlideListener {

    private LoginPersenter mLoginPersenter;

    @BindView(R.id.et_phone)
    EditText mPhone;
    @BindView(R.id.et_code)
    EditText mCode;
    @BindView(R.id.slideview)
    SlideView mSlideView;
    @BindView(R.id.bt_code)
    Button mBtnCode;
    @BindView(R.id.layout_code)
    RelativeLayout mLayoutCode;
    private KProgressHUD mKProgressHUD;

    private CaptchaTimeCount captchaTimeCount;

    private String mHtml;

    private String mTitel;

    private int type;

    @Override
    protected int setLayout() {
        return R.layout.activity_login_store;
    }

    @Override
    protected void initView() {
        ButterKnife.bind(this);
        mHtml = getIntent().getStringExtra(Contents.HTML);
        mTitel = getIntent().getStringExtra(Contents.TITLE);
        type = getIntent().getIntExtra(Contents.TYPE, 0);
        mSlideView.addSlideListener(this);
        mSlideView.setVisibility(View.GONE);
        mLayoutCode.setVisibility(View.GONE);
        captchaTimeCount = new CaptchaTimeCount(60000, 1000, mBtnCode, this);
        mBtnCode.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // TODO: 2018/8/8 弹出对话框 防刷
                boolean b = CommonUtil.checkPhone(getAccount(), true);
                if (b) {
                    showDialog();
                }
            }
        });
        mKProgressHUD = KProgressHUD.create(this)
                .setStyle(KProgressHUD.Style.SPIN_INDETERMINATE)
                .setDimAmount(0.5f);
    }

    @Override
    protected void initData() {

    }

    @Override
    protected LoginPersenter createPresenter() {
        mLoginPersenter = new LoginPersenter(this);
        return mLoginPersenter;
    }

    @Override
    public void loginSuccess(String token) {
        // TODO: 2018/8/8 保存token 跳转
        saveToken(token);
        launch();
    }

    @Override
    public void loginFaild(String error) {
        ToastUtils.showToast(error);
    }

    @Override
    public String getAccount() {
        return mPhone.getText().toString().trim();
    }

    @Override
    public String getPassward() {
        return mCode.getText().toString().trim();
    }

    @Override
    public void showDialog() {
        VerificationFragment verificationFragment = new VerificationFragment();
        verificationFragment.show(getSupportFragmentManager(), "code");
    }


    @Override
    public void isNewUser(int isolduser, String token) {
        //判断新老用户（1是（老用户） 0否（新用户））
        if (1 == isolduser) {
            //TODO 保存token
            saveToken(token);
            mLayoutCode.setVisibility(View.GONE);
            mSlideView.setVisibility(View.VISIBLE);
        } else {
            mLayoutCode.setVisibility(View.VISIBLE);
            mSlideView.setVisibility(View.VISIBLE);
            //TODO 获取验证码
            captchaTimeCount.start();
            mLoginPersenter.getCode();
        }
    }

    private void saveToken(String token) {
        SPUtil.putString(getApplicationContext(), Contents.TOKEN, token);
        SPUtil.putString(getApplicationContext(), Contents.PHONE, getAccount());
    }

    @Override
    public void getCodeSuccess() {
        //获取验证吗成功
        ToastUtils.showToast("验证码已发送");
    }

    @Override
    public void getCodeFaild(String error) {
        ToastUtils.showToast(error);
    }

    @Override
    public void success() {
        // 图片验证码输入正确 判断新老用户
        mLoginPersenter.judgment();
    }

    @Override
    public void onSlideSuccess() {
        //TODO 滑动图片是否正确
        if (mLayoutCode.getVisibility() == View.VISIBLE) {
            //新用户 验证 code 是否填入正确
            if (!TextUtils.isEmpty(getPassward()) && getPassward().length() == 4) {
                mLoginPersenter.verifyCode(mKProgressHUD);
            } else {
                ToastUtils.showToast("验证码错误");
            }
        } else {
            //老用户 跳转
            launch();
        }
        mSlideView.reset();

    }

    private void launch() {
        if (type == 1) {

        } else {
            //跳转到h5
            Intent intent = new Intent(this, HtmlActivity.class);
            intent.putExtra(Contents.HTML, mHtml);
            intent.putExtra(Contents.TITLE, mTitel);
            startActivity(intent);
        }
        EventBus.getDefault().post(new MessageEvent());
        finish();
    }

    @OnClick(R.id.toolbar_back)
    public void onViewClicked() {
        finish();
    }
}
