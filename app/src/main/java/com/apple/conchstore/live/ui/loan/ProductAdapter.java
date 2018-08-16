package com.apple.conchstore.live.ui.loan;

import android.support.v7.widget.RecyclerView;
import android.widget.ImageView;

import com.apple.conchstore.R;
import com.apple.conchstore.live.bean.Product;
import com.apple.conchstore.live.widgets.recyclerview.ItemType;
import com.apple.conchstore.live.widgets.recyclerview.MultipleItem;
import com.apple.conchstore.live.widgets.recyclerview.MultipleItemQuickAdapter;
import com.apple.conchstore.live.widgets.recyclerview.MultipleViewHolder;
import com.bumptech.glide.Glide;

import java.util.List;

/**
 * - @Author:  闫世豪
 * - @Time:  2018/8/13 上午10:44
 * - @Email whynightcode@gmail.com
 */
public class ProductAdapter extends MultipleItemQuickAdapter {

    public ProductAdapter(List<MultipleItem> data, RecyclerView recyclerView) {
        super(data, recyclerView);
        addItemType(ItemType.PRODUCT, R.layout.item_multiple_product);
    }

    @Override
    protected void convert(MultipleViewHolder helper, MultipleItem item) {
        super.convert(helper, item);
        int itemType = item.getItemType();
        switch (itemType) {
            case ItemType.PRODUCT:
                Product product = item.getProduct();
                helper.setText(R.id.tv_name, product.getP_name())
                        .setText(R.id.tv_desc, product.getP_desc())
                        .setText(R.id.average_time_Special, "放款速度: " + product.getFastest_time())
                        .addOnClickListener(R.id.go);

                int interestAlgorithm = product.getInterest_algorithm();
                if (interestAlgorithm == 0) {
                    helper.setText(R.id.special_rate, "参考日利率: " + product.getMin_algorithm() + "%");
                } else {
                    helper.setText(R.id.special_rate, "参考月利率: " + product.getMin_algorithm() + "%");
                }
                helper.setText(R.id.tv_people, product.getApply() + "人申请");
                String maximumAmount = product.getMaximum_amount();
                if (maximumAmount.length() > 4) {
                    String substring = maximumAmount.substring(0, maximumAmount.length() - 4);
                    helper.setText(R.id.min_max_Special, product.getMinimum_amount() + "-" + substring + "万");
                } else {
                    helper.setText(R.id.min_max_Special, product.getMinimum_amount() + "-" + maximumAmount);
                }

                Glide.with(mContext)
                        .load(product.getP_logo())
                        .apply(mRequestOptions)
                        .into((ImageView) helper.getView(R.id.iv_logo));

                helper.addOnClickListener(R.id.go);
                break;
            default:
                break;
        }

    }
}
