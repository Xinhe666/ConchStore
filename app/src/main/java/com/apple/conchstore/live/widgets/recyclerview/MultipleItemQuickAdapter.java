package com.apple.conchstore.live.widgets.recyclerview;

import android.content.Intent;
import android.support.annotation.Nullable;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;

import com.apple.conchstore.R;
import com.apple.conchstore.live.bean.BannerEntity;
import com.apple.conchstore.live.ui.html.HtmlActivity;
import com.apple.conchstore.live.utils.Contents;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.RequestOptions;
import com.chad.library.adapter.base.BaseMultiItemQuickAdapter;

import java.util.List;

import cn.bingoogolapple.bgabanner.BGABanner;

/**
 * - @Author:  闫世豪
 * - @Time:  2018/8/9 上午10:47
 * - @Email whynightcode@gmail.com
 */
public class MultipleItemQuickAdapter extends BaseMultiItemQuickAdapter<MultipleItem, MultipleViewHolder>
        implements BGABanner.Delegate<ImageView, BannerEntity> {
    protected final RequestOptions mRequestOptions =
            new RequestOptions()
                    .centerCrop()
                    .transform(new GlideCircleTransform())
                    .diskCacheStrategy(DiskCacheStrategy.ALL)
                    .dontAnimate();
    //确保初始化一次Banner，防止重复Item加载
    private boolean mIsInitBanner = false;

    public MultipleItemQuickAdapter(List<MultipleItem> data, RecyclerView recyclerView) {
        super(data);
        bindToRecyclerView(recyclerView);
        init();
    }

    private void init() {
        addItemType(ItemType.TEXT, R.layout.item_multiple_text);
        addItemType(ItemType.IMAGE, R.layout.item_multiple_image);
        addItemType(ItemType.TEXT_IMAGE, R.layout.item_multiple_image_text);
        addItemType(ItemType.BANNER, R.layout.item_multiple_banner);
        //多次执行动画
        isFirstOnly(false);

    }

    @Override
    protected MultipleViewHolder createBaseViewHolder(View view) {
        return MultipleViewHolder.create(view);
    }

    @Override
    protected void convert(MultipleViewHolder helper, MultipleItem item) {
        switch (item.getItemType()) {
            case ItemType.BANNER:
                if (!mIsInitBanner) {
                    List<BannerEntity> imageUrl = item.getImageUrl();
                    BGABanner banner = helper.getView(R.id.item_multiple_banner);
                    banner.setAdapter(new BGABanner.Adapter<ImageView, BannerEntity>() {
                        @Override
                        public void fillBannerItem(BGABanner banner, ImageView itemView, @Nullable BannerEntity model, int position) {
                            Glide.with(mContext)
                                    .load(model.getPictrue())
                                    .into(itemView);
                        }
                    });
                    banner.setDelegate(this);
                    banner.setData(imageUrl, null);
                    mIsInitBanner = true;
                }
                break;
            case ItemType.IMAGE:
                helper.addOnClickListener(R.id.home_iamge_big)
                        .addOnClickListener(R.id.home_iamge_above)
                        .addOnClickListener(R.id.home_iamge_below);
                break;
            case ItemType.TEXT:
                helper.addOnClickListener(R.id.home_iamge_bangnijie);
                break;
            case ItemType.TEXT_IMAGE:
                // TODO: 2018/8/13  没有任何操作，不能点击
                break;
            default:
                break;
        }
    }

    @Override
    public void onBannerItemClick(BGABanner banner, ImageView itemView, @Nullable BannerEntity model, int position) {
        //TODO 跳转到html 界面
        Intent intent = new Intent(mContext, HtmlActivity.class);
        intent.putExtra(Contents.HTML, model.getApp());
        intent.putExtra(Contents.TITLE, model.getAdvername());
        mContext.startActivity(intent);
    }

}
