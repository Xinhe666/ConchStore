package com.apple.conchstore.live.widgets.recyclerview;

import com.apple.conchstore.live.bean.BannerEntity;
import com.apple.conchstore.live.bean.Product;
import com.chad.library.adapter.base.entity.MultiItemEntity;

import java.util.List;

/**
 * - @Author:  闫世豪
 * - @Time:  2018/8/9 上午10:35
 * - @Email whynightcode@gmail.com
 */
public class MultipleItem implements MultiItemEntity {
    private int itemType;

    private Product mProduct;

    private List<BannerEntity> imageUrl;

    public Product getProduct() {
        return mProduct;
    }

    public List<BannerEntity> getImageUrl() {
        return imageUrl;
    }

    public MultipleItem setImageUrl(List<BannerEntity> imageUrl) {
        this.imageUrl = imageUrl;
        return this;
    }

    public MultipleItem setProduct(Product product) {
        mProduct = product;
        return this;
    }

    @Override
    public int getItemType() {
        return itemType;
    }

    public MultipleItem setItemType(int type) {
        itemType = type;
        return this;
    }
}
