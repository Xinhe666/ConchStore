package com.apple.conchstore.live.ui.loan;


import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.apple.conchstore.live.bean.Product;
import com.apple.conchstore.live.net.Api;
import com.apple.conchstore.live.net.OkgoUtils;
import com.apple.conchstore.live.net.OnRequestListener;
import com.apple.conchstore.live.widgets.recyclerview.ItemType;
import com.apple.conchstore.live.widgets.recyclerview.MultipleItem;
import com.kaopiz.kprogresshud.KProgressHUD;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * - @Author:  闫世豪
 * - @Time:  2018/8/8 下午3:08
 * - @Email whynightcode@gmail.com
 */
public class HomeModel {

    private OnDataListener mOnDataListener;

    public void setOnDataListener(OnDataListener onDataListener) {
        mOnDataListener = onDataListener;
    }

    public void cover(KProgressHUD kProgressHUD, int offset, int limit) {

        HashMap<String, String> params = new HashMap<>();
        params.put("offset", offset + "");
        params.put("number", limit + "");
        final List<MultipleItem> mItems = new ArrayList<>();
        //初始化数据
        OkgoUtils.execute(Api.PRODUCT_LSIT, params, kProgressHUD, new OnRequestListener() {
            @Override
            public void requestSuccess(JSONObject jsonObject) {
                JSONArray jsonArray = jsonObject.getJSONArray("data");
                List<Product> entities = JSON.parseArray(JSON.toJSONString(jsonArray), Product.class);
                for (Product p : entities) {
                    MultipleItem multipleItem = new MultipleItem();
                    multipleItem.setItemType(ItemType.PRODUCT)
                            .setProduct(p);
                    mItems.add(multipleItem);
                }
                if (mOnDataListener != null) {
                    mOnDataListener.onSuccess(mItems);
                }
            }

            @Override
            public void requestFailure(String error) {
                if (mOnDataListener != null) {
                    mOnDataListener.onFailure(error);
                }
            }
        });
    }

}


