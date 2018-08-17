package com.apple.conchstore.live.ui.home;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.View;

import com.apple.conchstore.R;
import com.apple.conchstore.live.base.BaseFragment;
import com.apple.conchstore.live.base.BaseMvpPersenter;
import com.apple.conchstore.live.bean.Product;
import com.apple.conchstore.live.net.OkgoUtils;
import com.apple.conchstore.live.ui.html.HtmlActivity;
import com.apple.conchstore.live.ui.login.LoginActivity;
import com.apple.conchstore.live.ui.product.ProductActivity;
import com.apple.conchstore.live.utils.Contents;
import com.apple.conchstore.live.utils.SPUtil;
import com.apple.conchstore.live.utils.ToastUtils;
import com.apple.conchstore.live.widgets.recyclerview.HorizontalDividerItemDecoration;
import com.apple.conchstore.live.widgets.recyclerview.ItemType;
import com.apple.conchstore.live.widgets.recyclerview.MultipleItem;
import com.apple.conchstore.live.widgets.recyclerview.VerticalDividerItemDecoration;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.scwang.smartrefresh.layout.SmartRefreshLayout;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.listener.OnRefreshListener;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;

/**
 * - @Author:  闫世豪
 * - @Time:  2018/8/8 下午12:26
 * - @Email whynightcode@gmail.com
 */
public class HomeFragment extends BaseFragment implements HomeMvpView {

    @BindView(R.id.home_rv)
    RecyclerView mRecyclerView;
    @BindView(R.id.loan_refresh)
    SmartRefreshLayout mRefreshLayout;
    private HomePersenter mHomePersenter;

    private Unbinder mUnbinder;

    private HomeProductAdapter mAdapter;

    @Override
    protected void onBindView(@NonNull Bundle savedInstanceState, @NonNull View rootView) {
        mUnbinder = ButterKnife.bind(this, rootView);

        mRecyclerView.setLayoutManager(new GridLayoutManager(getContext(), 3));

        mAdapter = new HomeProductAdapter(null, mRecyclerView);
        mRefreshLayout.setEnableLoadMore(false);
        mRefreshLayout.setOnRefreshListener(new OnRefreshListener() {
            @Override
            public void onRefresh(@NonNull RefreshLayout refreshLayout) {
                mHomePersenter.refresh();
            }
        });
        mAdapter.setOnItemChildClickListener(new BaseQuickAdapter.OnItemChildClickListener() {
            @Override
            public void onItemChildClick(BaseQuickAdapter adapter, View view, int position) {
                switch (view.getId()) {
                    case R.id.home_iamge_above:
                        ProductActivity.launcher(getContext(), 1);
                        break;
                    case R.id.home_iamge_bangnijie:
                        mHomePersenter.launcher();
                        break;
                    case R.id.home_iamge_big:
                        ProductActivity.launcher(getContext(), 3);
                        break;
                    case R.id.home_iamge_below:
                        ProductActivity.launcher(getContext(), 4);
                        break;
                    default:
                        break;
                }
            }
        });
        mAdapter.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
                int itemType = mAdapter.getData().get(position).getItemType();
                if (itemType == ItemType.HOME_PRODUCT) {
                    skipActivity(mAdapter.getData().get(position).getProduct());
                }
            }
        });
        mRecyclerView.addItemDecoration(new HorizontalDividerItemDecoration.Builder(getContext()).margin(0).colorResId(R.color.diver).size(4).build());
        mRecyclerView.addItemDecoration(new VerticalDividerItemDecoration.Builder(getContext()).margin(0, 0).colorResId(R.color.diver).size(4).build());
    }

    private void skipActivity(Product product) {
        String token = SPUtil.getString(getActivity(), Contents.TOKEN);
        OkgoUtils.apply(product.getId(), token);
        Intent intent;
        if (TextUtils.isEmpty(token)) {
            intent = new Intent(getContext(), LoginActivity.class);
        } else {
            intent = new Intent(getContext(), HtmlActivity.class);
        }
        intent.putExtra(Contents.HTML, product.getUrl());
        intent.putExtra(Contents.TITLE, product.getP_name());
        startActivity(intent);
    }

    @Override
    protected void initData() {
        mHomePersenter.start(mKProgressHUD);
    }

    @Override
    protected Object getLayoutId() {
        return R.layout.fargment_home;
    }

    @Override
    protected BaseMvpPersenter createPresenter() {
        mHomePersenter = new HomePersenter(this);
        return mHomePersenter;
    }


    @Override
    public void resultSuccess(List<MultipleItem> list) {
        MultipleItem multipleItem = list.get(0);
        int itemType = multipleItem.getItemType();
        if (itemType == ItemType.BANNER) {
            mAdapter.replaceData(list);
        }
    }

    @Override
    public void resultFailure(String error) {
        ToastUtils.showToast(error);
        mRefreshLayout.finishRefresh();
        mAdapter.setEmptyView(R.layout.item_multiple_empty);

    }

    @Override
    public void onLoadMore(List<MultipleItem> list) {

    }

    @Override
    public void onRefresh(List<MultipleItem> list) {
        MultipleItem multipleItem = list.get(0);
        int itemType = multipleItem.getItemType();
        if (itemType == ItemType.BANNER) {
            mAdapter.replaceData(list);
        }
        mRefreshLayout.finishRefresh();
    }

    @Override
    public void launcher(String title, String url) {
        Intent intent = new Intent(getContext(), HtmlActivity.class);
        intent.putExtra(Contents.HTML, url);
        intent.putExtra(Contents.TITLE, title);
        startActivity(intent);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        mUnbinder.unbind();
    }
}
