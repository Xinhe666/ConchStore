package com.apple.conchstore.live.ui.loan;


import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.apple.conchstore.R;
import com.apple.conchstore.live.base.BaseFragment;
import com.apple.conchstore.live.base.BaseMvpPersenter;
import com.apple.conchstore.live.bean.Product;
import com.apple.conchstore.live.net.OkgoUtils;
import com.apple.conchstore.live.ui.html.HtmlActivity;
import com.apple.conchstore.live.ui.login.LoginActivity;
import com.apple.conchstore.live.utils.Contents;
import com.apple.conchstore.live.utils.SPUtil;
import com.apple.conchstore.live.utils.ToastUtils;
import com.apple.conchstore.live.widgets.recyclerview.MultipleItem;
import com.apple.conchstore.live.widgets.recyclerview.RecycleViewDivider;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.scwang.smartrefresh.layout.SmartRefreshLayout;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.listener.OnLoadMoreListener;
import com.scwang.smartrefresh.layout.listener.OnRefreshListener;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;

/**
 * A simple {@link Fragment} subclass.
 * @author yanshihao
 */
public class ProductFragment extends BaseFragment
        implements ProductMvpView, OnRefreshListener, OnLoadMoreListener {

    private ProductPersenter mPersenter;
    @BindView(R.id.recylerview)
    RecyclerView mRecyclerView;
    @BindView(R.id.loan_refresh)
    SmartRefreshLayout mRefreshLayout;
    @BindView(R.id.toolbar_title)
    TextView mTilte;

    @BindView(R.id.toolbar_back)
    ImageView mImageView;

    private ProductAdapter mAdapter;

    private Unbinder mUnbinder;

    @Override
    protected void onBindView(@NonNull Bundle savedInstanceState, @NonNull View rootView) {

        mUnbinder = ButterKnife.bind(this, rootView);
        mTilte.setText("产品列表");
        mImageView.setVisibility(View.GONE);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        mRefreshLayout.setOnRefreshListener(this);
        mRefreshLayout.setOnLoadMoreListener(this);
        mAdapter = new ProductAdapter(null, mRecyclerView);
        mRecyclerView.addItemDecoration(new RecycleViewDivider(getContext(),
                LinearLayoutManager.HORIZONTAL, 20, getResources().getColor(R.color.divider_background)));

        mAdapter.setOnItemChildClickListener(new BaseQuickAdapter.OnItemChildClickListener() {
            @Override
            public void onItemChildClick(BaseQuickAdapter adapter, View view, int position) {
                if (view.getId() == R.id.go) {
                    start(mAdapter.getData().get(position).getProduct());
                }
            }
        });
        mAdapter.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
                start(mAdapter.getData().get(position).getProduct());
            }
        });
    }

    @Override
    protected void initData() {
        mPersenter.initData(mKProgressHUD);
    }

    @Override
    protected Object getLayoutId() {
        return R.layout.fragment_product;
    }

    @Override
    protected BaseMvpPersenter createPresenter() {
        mPersenter = new ProductPersenter(this);
        return mPersenter;
    }

    @Override
    public void resultSuccess(List<MultipleItem> list) {

        mAdapter.addData(list);
    }

    @Override
    public void resultFailure(String error) {
        ToastUtils.showToast(error);
        mRefreshLayout.finishRefresh();
        mRefreshLayout.finishLoadMore();
        mAdapter.setEmptyView(R.layout.item_multiple_empty);

    }

    @Override
    public void onLoadMore(List<MultipleItem> list) {
        mAdapter.addData(list);
        mRefreshLayout.finishLoadMore();
    }

    @Override
    public void onRefresh(List<MultipleItem> list) {
        mAdapter.replaceData(list);
        mRefreshLayout.finishRefresh();
    }

    @Override
    public void onRefresh(@NonNull RefreshLayout refreshLayout) {
        mPersenter.refresh();

    }

    @Override
    public void onLoadMore(@NonNull RefreshLayout refreshLayout) {
        mPersenter.loadMore(mAdapter.getData().size());

    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        mUnbinder.unbind();
    }

    private void start(Product product) {
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
}
