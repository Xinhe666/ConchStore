package com.apple.conchstore.live.ui.loan;

import com.apple.conchstore.live.base.BaseMvpPersenter;
import com.apple.conchstore.live.widgets.recyclerview.MultipleItem;
import com.kaopiz.kprogresshud.KProgressHUD;

import java.util.List;

/**
 * - @Author:  闫世豪
 * - @Time:  2018/8/13 上午10:40
 * - @Email whynightcode@gmail.com
 */
public class ProductPersenter extends BaseMvpPersenter<ProductMvpView> implements OnDataListener {

    private ProductMvpView mProductMvpView;
    private HomeModel mHomeModel;
    private int type = 0;

    public ProductPersenter(ProductMvpView productMvpView) {
        mProductMvpView = productMvpView;
        mHomeModel = new HomeModel();
        mHomeModel.setOnDataListener(this);
    }

    public void initData(KProgressHUD kProgressHUD) {
        type = 0;
        mHomeModel.cover(kProgressHUD, 0, 10);
    }

    public void refresh() {
        type = 1;
        mHomeModel.cover(null, 0, 10);
    }

    public void loadMore(int size) {
        type = 2;
        mHomeModel.cover(null, size, 5);
    }

    @Override
    public void onSuccess(List<MultipleItem> list) {
        switch (type) {
            case 0:
                mProductMvpView.resultSuccess(list);
                break;
            case 1:
                mProductMvpView.onRefresh(list);
                break;
            case 2:
                mProductMvpView.onLoadMore(list);
                break;
            default:
                break;
        }

    }

    @Override
    public void onFailure(String error) {
        mProductMvpView.resultFailure(error);
    }
}
