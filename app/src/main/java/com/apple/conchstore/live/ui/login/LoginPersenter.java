package com.apple.conchstore.live.ui.login;

import com.alibaba.fastjson.JSONObject;
import com.apple.conchstore.live.base.BaseMvpPersenter;
import com.apple.conchstore.live.net.Api;
import com.apple.conchstore.live.net.OkgoUtils;
import com.apple.conchstore.live.net.OnRequestListener;
import com.apple.conchstore.live.utils.ToastUtils;
import com.kaopiz.kprogresshud.KProgressHUD;

import java.util.HashMap;
import java.util.Map;

/**
 * - @Author:  闫世豪
 * - @Time:  2018/8/8 下午4:22
 * - @Email whynightcode@gmail.com
 */

public class LoginPersenter extends BaseMvpPersenter<LoginMvpView> {
    private LoginMvpView mLoginMvpView;

    public LoginPersenter(LoginMvpView loginMvpView) {
        mLoginMvpView = loginMvpView;
    }

    /**
     * 判断新老用户
     */
    public void judgment() {
        String account = mLoginMvpView.getAccount();
        Map<String, String> map = new HashMap<>();
        map.put("userphone", account);
        OkgoUtils.execute(Api.isOldUser, map, null, new OnRequestListener() {
            @Override
            public void requestSuccess(JSONObject jsonObject) {
                JSONObject date = jsonObject.getJSONObject("data");
                int i = date.getIntValue("isolduser");
                String token = date.getString("token");
                mLoginMvpView.isNewUser(i, token);
            }

            @Override
            public void requestFailure(String error) {
                ToastUtils.showToast(error);
            }

        });
    }

    //获取验证吗
    public void getCode() {
        String account = mLoginMvpView.getAccount();
        Map<String, String> map = new HashMap<>();
        map.put("userphone", account);
        OkgoUtils.execute(Api.CODE, map, null, new OnRequestListener() {

            @Override
            public void requestSuccess(JSONObject jsonObject) {
                JSONObject date = jsonObject.getJSONObject("data");
                String string = date.getString("isSucess");
                //验证码获取是否成功（1成功 0失败）
                if ("1".equals(string)) {
                    mLoginMvpView.getCodeSuccess();
                } else {
                    mLoginMvpView.getCodeFaild(date.getString("msg"));
                }
            }

            @Override
            public void requestFailure(String error) {
                ToastUtils.showToast(error);
            }
        });
    }

    public void verifyCode(KProgressHUD kProgressHUD) {

        String account = mLoginMvpView.getAccount();
        String passward = mLoginMvpView.getPassward();

        Map<String, String> map = new HashMap<>();
        map.put("userphone", account);
        map.put("code", passward);
        OkgoUtils.execute(Api.CHECKCODE, map, kProgressHUD, new OnRequestListener() {
            @Override
            public void requestSuccess(JSONObject jsonObject) {
                JSONObject date = jsonObject.getJSONObject("data");
                String msg = date.getString("msg");
                String isSucess = date.getString("isSuccess");
                String token = date.getString("token");
                if ("1".equals(isSucess)) {
                    mLoginMvpView.loginSuccess(token);
                } else {
                    mLoginMvpView.loginFaild(msg);
                }
            }

            @Override
            public void requestFailure(String error) {
                mLoginMvpView.loginFaild(error);

            }
        });
    }
}
