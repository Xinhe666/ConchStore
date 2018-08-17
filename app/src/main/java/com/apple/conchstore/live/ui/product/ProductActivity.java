package com.apple.conchstore.live.ui.product;


import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.View;
import android.widget.TextView;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.apple.conchstore.R;
import com.apple.conchstore.live.base.BaseActivity;
import com.apple.conchstore.live.bean.Product;
import com.apple.conchstore.live.net.Api;
import com.apple.conchstore.live.net.OkgoUtils;
import com.apple.conchstore.live.net.OnRequestListener;
import com.apple.conchstore.live.ui.html.HtmlActivity;
import com.apple.conchstore.live.ui.loan.ProductAdapter;
import com.apple.conchstore.live.ui.login.LoginActivity;
import com.apple.conchstore.live.ui.main.MainMvpView;
import com.apple.conchstore.live.ui.main.MainPersenter;
import com.apple.conchstore.live.utils.Contents;
import com.apple.conchstore.live.utils.SPUtil;
import com.apple.conchstore.live.utils.ToastUtils;
import com.apple.conchstore.live.widgets.recyclerview.ItemType;
import com.apple.conchstore.live.widgets.recyclerview.MultipleItem;
import com.apple.conchstore.live.widgets.recyclerview.RecycleViewDivider;
import com.apple.conchstore.util.Constant;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.kaopiz.kprogresshud.KProgressHUD;
import com.scwang.smartrefresh.layout.SmartRefreshLayout;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.listener.OnRefreshListener;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * @author yanshihao
 */
public class ProductActivity extends BaseActivity<MainMvpView, MainPersenter>
        implements MainMvpView, OnRefreshListener {

    @BindView(R.id.recylerview)
    RecyclerView mRecyclerView;
    @BindView(R.id.loan_refresh)
    SmartRefreshLayout mRefreshLayout;
    @BindView(R.id.toolbar_title)
    TextView mTilte;

    private List<MultipleItem> mList;

    private ProductAdapter mAdapter;

    private int mInt;

    @Override
    protected int setLayout() {
        return R.layout.fragment_product;
    }

    @Override
    protected void initView() {
        ButterKnife.bind(this);
        mTilte.setText("贷款");
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        mRefreshLayout.setOnRefreshListener(this);
        mRefreshLayout.setEnableLoadMore(false);
        mAdapter = new ProductAdapter(null, mRecyclerView);
        mRecyclerView.addItemDecoration(new RecycleViewDivider(this,
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
        mList = new ArrayList<>();

        mInt = getIntent().getIntExtra(Contents.TYPE, 0);
    }

    @Override
    protected void initData() {
        getData(mKProgressHUD);
    }

    private void getData(KProgressHUD kProgressHUD) {
        HashMap<String, String> params = new HashMap<>();
        params.put("identity", mInt + "");
        mList.clear();
        OkgoUtils.execute(Api.PRODUCT_SCREEN, params, kProgressHUD, new OnRequestListener() {
            @Override
            public void requestSuccess(JSONObject jsonObject) {
                JSONArray jsonArray = jsonObject.getJSONArray("data");
                List<Product> entities = JSON.parseArray(JSON.toJSONString(jsonArray), Product.class);
                for (Product p : entities) {
                    MultipleItem multipleItem = new MultipleItem();
                    multipleItem.setItemType(ItemType.PRODUCT)
                            .setProduct(p);
                    mList.add(multipleItem);
                }
                mAdapter.setNewData(mList);
            }

            @Override
            public void requestFailure(String error) {
                ToastUtils.showToast(error);
                mAdapter.setEmptyView(R.layout.item_multiple_empty);
            }
        });
    }

    @Override
    protected MainPersenter createPresenter() {
        return new MainPersenter(this);
    }

    @Override
    public void onRefresh(@NonNull RefreshLayout refreshLayout) {
        getData(null);
        mRecyclerView.postDelayed(new Runnable() {
            @Override
            public void run() {
                mRefreshLayout.finishRefresh();
            }
        }, 1500);
    }

    private void start(Product product) {
        String token = SPUtil.getString(this, Contents.TOKEN);
        OkgoUtils.apply(product.getId(), token);
        Intent intent;
        if (TextUtils.isEmpty(token)) {
            intent = new Intent(this, LoginActivity.class);
        } else {
            intent = new Intent(this, HtmlActivity.class);
        }
        intent.putExtra(Contents.HTML, product.getUrl());
        intent.putExtra(Contents.TITLE, product.getP_name());
        startActivity(intent);
    }


    @OnClick(R.id.toolbar_back)
    public void onViewClicked() {
        finish();
    }

    public static void launcher(Context context, int type) {
        Intent intent = new Intent(context, ProductActivity.class);
        intent.putExtra(Contents.TYPE, type);
        context.startActivity(intent);
    }
}
