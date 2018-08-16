package com.apple.conchstore.live.ui.home;

import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.widget.ImageView;

import com.apple.conchstore.R;
import com.apple.conchstore.live.widgets.recyclerview.ItemType;
import com.apple.conchstore.live.widgets.recyclerview.MultipleItem;
import com.apple.conchstore.live.widgets.recyclerview.MultipleItemQuickAdapter;
import com.apple.conchstore.live.widgets.recyclerview.MultipleViewHolder;
import com.bumptech.glide.Glide;
import com.chad.library.adapter.base.BaseQuickAdapter;

import java.util.List;

/**
 * - @Author:  闫世豪
 * - @Time:  2018/8/8 下午4:05
 * - @Email whynightcode@gmail.com
 */
public class HomeProductAdapter extends MultipleItemQuickAdapter implements BaseQuickAdapter.SpanSizeLookup {

    public HomeProductAdapter(List<MultipleItem> data, RecyclerView recyclerView) {
        super(data, recyclerView);
        addItemType(ItemType.HOME_PRODUCT, R.layout.item_multiple_home_product);
        setSpanSizeLookup(this);
    }

    @Override
    protected void convert(MultipleViewHolder helper, MultipleItem item) {
        super.convert(helper, item);
        int itemType = item.getItemType();

        switch (itemType) {
            case ItemType.HOME_PRODUCT:
                helper.setText(R.id.home_product_title, item.getProduct().getP_name())
                        .setText(R.id.home_product_apply, item.getProduct().getApply() + "人申请");

                Glide.with(mContext)
                        .load(item.getProduct().getP_logo())
                        .apply(mRequestOptions)
                        .into((ImageView) helper.getView(R.id.home_product_iamge));
                break;
            default:
                break;
        }
    }

    @Override
    public int getSpanSize(GridLayoutManager gridLayoutManager, int position) {
        int itemType = getData().get(position).getItemType();
        int spanSize = 0;
        switch (itemType) {
            case ItemType.BANNER:
                spanSize = 3;
                break;
            case ItemType.IMAGE:
                spanSize = 3;
                break;
            case ItemType.TEXT:
                spanSize = 3;
                break;
            case ItemType.TEXT_IMAGE:
                spanSize = 3;
                break;
            case ItemType.HOME_PRODUCT:
                spanSize = 1;
                break;
            default:
                break;
        }
        return spanSize;
    }
}
