package com.apple.conchstore.live.net;

import com.alibaba.fastjson.JSONObject;

/**
 * - @Author:  闫世豪
 * - @Time:  2018/8/8 下午2:43
 * - @Email whynightcode@gmail.com
 */
public interface OnRequestListener {


    void requestSuccess(JSONObject jsonObject);

    void requestFailure(String error);

}
