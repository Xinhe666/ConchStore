package com.apple.conchstore.live.ui.my;


import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.annotation.RequiresApi;
import android.support.v4.app.Fragment;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.apple.conchstore.R;
import com.apple.conchstore.live.bean.MessageEvent;
import com.apple.conchstore.live.ui.AboutActivity;
import com.apple.conchstore.live.ui.FeedBackActivity;
import com.apple.conchstore.live.ui.login.LoginActivity;
import com.apple.conchstore.live.utils.Contents;
import com.apple.conchstore.live.utils.SPUtil;
import com.apple.conchstore.live.utils.ToastUtils;
import com.apple.conchstore.live.widgets.ProfileEdit;
import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;

/**
 * A simple {@link Fragment} subclass.
 *
 * @author yanshihao
 */
public class MyFragment extends Fragment {


    @BindView(R.id.phone)
    TextView mTextView;
    @BindView(R.id.info_updata)
    ProfileEdit mInfoUpdata;
    @BindView(R.id.info_about)
    ProfileEdit mInfoAbout;
    @BindView(R.id.btn)
    Button mBtn;
    Unbinder unbinder;

    private boolean isLogin;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_my, container, false);
        unbinder = ButterKnife.bind(this, view);
        EventBus.getDefault().register(this);
        return view;
    }

    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        initView(null);
    }

    @RequiresApi(api = Build.VERSION_CODES.M)
    @Subscribe(threadMode = ThreadMode.MAIN)
    public void initView(MessageEvent event) {
        String token = SPUtil.getString(getActivity(), Contents.TOKEN);
        if (TextUtils.isEmpty(token)) { //没有登陆
            mBtn.setVisibility(View.GONE);
            isLogin = false;
            mTextView.setText("登录");
        } else {//登陆了
            String phone = SPUtil.getString(getContext(), Contents.PHONE);
            mTextView.setText(phone);
            isLogin = true;
            mBtn.setVisibility(View.VISIBLE);
            mBtn.setBackgroundResource(R.drawable.button_sure);
            mBtn.setTextColor(getResources().getColor(R.color.white, null));
        }

    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);

    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();

    }


    @SuppressLint("ResourceAsColor")
    @OnClick({R.id.phone, R.id.info_updata, R.id.info_about, R.id.btn, R.id.info_feedback})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.phone:
                if (!isLogin) {
                    Intent intent = new Intent(getContext(), LoginActivity.class);
                    intent.putExtra(Contents.TYPE, 1); //单纯的登录
                    startActivity(intent);
                }
                break;
            case R.id.info_updata:
                ToastUtils.showToast("已是最新版本");
                break;
            case R.id.info_about:
                startActivity(new Intent(getContext(), AboutActivity.class));
                break;
            case R.id.btn:
                if (isLogin) {
                    SPUtil.remove(getContext(),Contents.TOKEN);
                    SPUtil.remove(getContext(),Contents.PHONE);
                    ToastUtils.showToast("用户已退出");
                    mBtn.setVisibility(View.GONE);
                    mTextView.setText("登录");
                    isLogin = false;
                } else {
                    ToastUtils.showToast("您还没有登录，请先登录");
                }
                break;
            case R.id.info_feedback:
                // TODO: 2018/8/14  一件反馈
                startActivity(new Intent(getContext(), FeedBackActivity.class));

                break;
            default:
                break;
        }
    }

}
