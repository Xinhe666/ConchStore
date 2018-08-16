package com.apple.conchstore.live.ui.home;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.apple.conchstore.live.base.BaseMvpPersenter;
import com.apple.conchstore.live.bean.BannerEntity;
import com.apple.conchstore.live.bean.Product;
import com.apple.conchstore.live.net.Api;
import com.apple.conchstore.live.net.OkgoUtils;
import com.apple.conchstore.live.net.OnRequestListener;
import com.apple.conchstore.live.widgets.recyclerview.ItemType;
import com.apple.conchstore.live.widgets.recyclerview.MultipleItem;
import com.kaopiz.kprogresshud.KProgressHUD;
import java.util.ArrayList;
import java.util.List;

/**
 * - @Author:  闫世豪
 * - @Time:  2018/8/8 下午2:27
 * - @Email whynightcode@gmail.com
 */
public class HomePersenter extends BaseMvpPersenter<HomeMvpView> {

    private HomeMvpView mHomeMvpView;

    private List<MultipleItem> mItems;

    public HomePersenter(HomeMvpView homeMvpView) {
        mHomeMvpView = homeMvpView;
        mItems = new ArrayList();
    }


    public void start(KProgressHUD kProgressHUD) {
        mItems.clear();
        //初始化数据
        OkgoUtils.execute(Api.BANNER, null, null, new OnRequestListener() {
            @Override
            public void requestSuccess(JSONObject jsonObject) {
                JSONArray jsonArray = jsonObject.getJSONArray("data");
                List<BannerEntity> entities = JSON.parseArray(jsonArray.toJSONString(), BannerEntity.class);
                MultipleItem multipleItem = new MultipleItem();
                multipleItem.setItemType(ItemType.BANNER).setImageUrl(entities);
                mItems.add(0, multipleItem);
                MultipleItem iamgeAndText = new MultipleItem();
                iamgeAndText.setItemType(ItemType.TEXT_IMAGE);
                MultipleItem iamge = new MultipleItem();
                iamge.setItemType(ItemType.IMAGE);
                MultipleItem text = new MultipleItem();
                text.setItemType(ItemType.TEXT);
                mItems.add(1, iamgeAndText);
                mItems.add(2, iamge);
                mItems.add(3, text);
                mHomeMvpView.resultSuccess(mItems);
            }

            @Override
            public void requestFailure(String error) {
                mHomeMvpView.resultFailure(error);
            }

        });
        OkgoUtils.execute(Api.HOT, null, kProgressHUD, new OnRequestListener() {
            @Override
            public void requestSuccess(JSONObject jsonObject) {
                JSONArray jsonArray = jsonObject.getJSONArray("data");
                List<Product> entities = JSON.parseArray(JSON.toJSONString(jsonArray), Product.class);
                for (Product p : entities) {
                    MultipleItem multipleItem = new MultipleItem();
                    multipleItem.setItemType(ItemType.HOME_PRODUCT)
                            .setProduct(p);
                    mItems.add(multipleItem);
                }
                mHomeMvpView.resultSuccess(mItems);
            }

            @Override
            public void requestFailure(String error) {
                mHomeMvpView.resultFailure(error);
            }

        });
    }

    public void refresh() {
        mItems.clear();
        //初始化数据
        OkgoUtils.execute(Api.BANNER, null, null, new OnRequestListener() {
            @Override
            public void requestSuccess(JSONObject jsonObject) {
                JSONArray jsonArray = jsonObject.getJSONArray("data");
                List<BannerEntity> entities = JSON.parseArray(jsonArray.toJSONString(), BannerEntity.class);
                MultipleItem multipleItem = new MultipleItem();
                multipleItem.setItemType(ItemType.BANNER).setImageUrl(entities);
                mItems.add(0, multipleItem);
                MultipleItem iamgeAndText = new MultipleItem();
                iamgeAndText.setItemType(ItemType.TEXT_IMAGE);
                MultipleItem iamge = new MultipleItem();
                iamge.setItemType(ItemType.IMAGE);
                MultipleItem text = new MultipleItem();
                text.setItemType(ItemType.TEXT);
                mItems.add(1, iamgeAndText);
                mItems.add(2, iamge);
                mItems.add(3, text);
                mHomeMvpView.onRefresh(mItems);
            }

            @Override
            public void requestFailure(String error) {
                mHomeMvpView.resultFailure(error);
            }

        });
        OkgoUtils.execute(Api.HOT, null, null, new OnRequestListener() {
            @Override
            public void requestSuccess(JSONObject jsonObject) {
                JSONArray jsonArray = jsonObject.getJSONArray("data");
                List<Product> entities = JSON.parseArray(JSON.toJSONString(jsonArray), Product.class);
                for (Product p : entities) {
                    MultipleItem multipleItem = new MultipleItem();
                    multipleItem.setItemType(ItemType.HOME_PRODUCT)
                            .setProduct(p);
                    mItems.add(multipleItem);
                }
                mHomeMvpView.onRefresh(mItems);
            }

            @Override
            public void requestFailure(String error) {
                mHomeMvpView.resultFailure(error);
            }

        });
    }

    public void launcher() {
        OkgoUtils.execute(Api.ANWEI, null, null, new OnRequestListener() {

            @Override
            public void requestSuccess(JSONObject jsonObject) {
                JSONObject object = jsonObject.getJSONObject("data");
                String url = object.getString("url");
                String name = object.getString("name");
                mHomeMvpView.launcher("帮你借", url);
            }

            @Override
            public void requestFailure(String error) {
                mHomeMvpView.resultFailure(error);
            }

        });
    }
}
