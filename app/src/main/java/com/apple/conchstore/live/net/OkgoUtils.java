package com.apple.conchstore.live.net;

import android.text.TextUtils;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONException;
import com.alibaba.fastjson.JSONObject;
import com.apple.conchstore.live.AppManger;
import com.apple.conchstore.live.utils.Contents;
import com.apple.conchstore.live.utils.SPUtil;
import com.kaopiz.kprogresshud.KProgressHUD;
import com.lzy.okgo.OkGo;
import com.lzy.okgo.callback.StringCallback;
import com.lzy.okgo.model.Response;
import com.lzy.okgo.request.base.Request;

import java.util.Map;

/**
 * - @Author:  闫世豪
 * - @Time:  2018/8/8 下午2:42
 * - @Email whynightcode@gmail.com
 */

public class OkgoUtils {

    public static void execute(String url, Map<String, String> params, final KProgressHUD kProgressHUD, final OnRequestListener listener) {
        OkGo.<String>post(url)
                .params(params, false)
                .execute(new StringCallback() {
                    @Override
                    public void onSuccess(Response<String> response) {
                        if (response.body() != null && !TextUtils.isEmpty(response.body())) {
                            try {
                                JSONObject jsonObject = JSON.parseObject(response.body());
                                int code = jsonObject.getIntValue("error_code");
                                if (code == 0) {
                                    listener.requestSuccess(jsonObject);
                                } else if (code == 2) {
                                    SPUtil.remove(AppManger.getApplication(), Contents.TOKEN);
                                } else {
                                    listener.requestFailure(jsonObject.getString("error_message"));

                                }
                            } catch (JSONException e) {
                                e.printStackTrace();
                                listener.requestFailure("网络异常");
                            }

                        } else {
                            listener.requestFailure("获取数据为空，请稍后重试");
                        }
                    }

                    @Override
                    public void onError(Response<String> response) {
                        super.onError(response);
                        listener.requestFailure("网络错误");
                    }

                    @Override
                    public void onFinish() {
                        super.onFinish();
                        if (kProgressHUD != null) {
                            if (kProgressHUD.isShowing()) {
                                kProgressHUD.dismiss();
                            }
                        }
                    }

                    @Override
                    public void onStart(Request<String, ? extends Request> request) {
                        super.onStart(request);
                        if (kProgressHUD != null) {
                            kProgressHUD.show();
                        }

                    }
                });
    }

    public static void apply(String productId, String token) {
        OkGo.<String>post(Api.APPLY)
                .params("id", productId)
                .params("token", token)
                .execute(new StringCallback() {
                    @Override
                    public void onSuccess(Response<String> response) {
                    }
                });
    }
}
