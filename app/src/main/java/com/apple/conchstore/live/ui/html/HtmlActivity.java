package com.apple.conchstore.live.ui.html;

import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.View;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.apple.conchstore.R;
import com.apple.conchstore.live.AppManger;
import com.apple.conchstore.live.base.BaseActivity;
import com.apple.conchstore.live.service.DownAPKService;
import com.apple.conchstore.live.ui.main.MainMvpView;
import com.apple.conchstore.live.ui.main.MainPersenter;
import com.apple.conchstore.live.utils.Contents;
import com.apple.conchstore.live.utils.ToastUtils;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * @author yanshihao
 */
public class HtmlActivity extends BaseActivity<MainMvpView, MainPersenter>
        implements MainMvpView {


    @BindView(R.id.bar)
    ProgressBar bar;
    @BindView(R.id.webView)
    WebView webView;
    @BindView(R.id.toolbar_title)
    TextView mTitle;

    @Override
    protected int setLayout() {
        return R.layout.activity_html;
    }

    @Override
    protected void initView() {
        ButterKnife.bind(this);
        String title = getIntent().getStringExtra(Contents.TITLE);
        mTitle.setText(title);
    }

    @Override
    protected void initData() {
        CheckInternet();
    }

    private void CheckInternet() {
        boolean net = AppManger.isNet();
        if (net){
            getDate();
        } else {
            ToastUtils.showToast("当前无网络连接");
        }
    }

    private void getDate() {
        String html = getIntent().getStringExtra(Contents.HTML);
        if (html != null) {
            WebSettings webSettings = webView.getSettings();
            webSettings.setJavaScriptEnabled(true);
            webSettings.setJavaScriptCanOpenWindowsAutomatically(true);
            webSettings.setUseWideViewPort(true);
            webSettings.setLoadWithOverviewMode(true);
            webSettings.setBlockNetworkImage(false);
            webSettings.setRenderPriority(WebSettings.RenderPriority.HIGH);
            webSettings.setGeolocationEnabled(true);
            webSettings.setDomStorageEnabled(true);
            webSettings.setDatabaseEnabled(true);
            webSettings.setAppCacheEnabled(true);
            webSettings.setSupportZoom(false);
            webSettings.setNeedInitialFocus(false);
            webSettings.setLoadsImagesAutomatically(true);
            webSettings.setBuiltInZoomControls(false);
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                webView.getSettings().setMixedContentMode(
                        WebSettings.MIXED_CONTENT_COMPATIBILITY_MODE);
            }
            if (Build.VERSION.SDK_INT >= 21) {
                webSettings.setMixedContentMode(WebSettings.MIXED_CONTENT_ALWAYS_ALLOW);
            }
            webView.loadUrl(html);

            webView.setWebViewClient(new WebViewClient() {
                @Override
                public boolean shouldOverrideUrlLoading(WebView view, String url) {
                    if (parseScheme(url)) {

                    } else {

                        WebView.HitTestResult hitTestResult = view.getHitTestResult();
                        if (!TextUtils.isEmpty(url) && hitTestResult == null) {
                            view.loadUrl(url);
                            return true;
                        }
                    }
                    return false;
                }
            });
            webView.setOnKeyListener(new View.OnKeyListener() {
                @Override
                public boolean onKey(View view, int i, KeyEvent keyEvent) {
                    if (keyEvent.getAction() == KeyEvent.ACTION_DOWN) {

                        if (i == KeyEvent.KEYCODE_BACK && webView.canGoBack()) {
                            webView.goBack();
                            return true;
                        }
                    }
                    return false;
                }
            });
            webView.setWebChromeClient(new WebChromeClient() {
                @Override
                public void onProgressChanged(WebView view, int newProgress) {
                    // TODO 自动生成的方法存根

                    if (newProgress == 100) {
                        bar.setVisibility(View.GONE);
                        //加载完网页进度条消失
                    } else {
                        bar.setVisibility(View.VISIBLE);
                        //开始加载网页时显示进度条
                        bar.setProgress(newProgress);
                        //设置进度值
                    }
                }
            });
        }
    }

    public boolean parseScheme(String url) {
        if (url.contains("platformapi/startapp")) {
            try {
                Uri uri = Uri.parse(url);
                Intent intent;
                intent = Intent.parseUri(url,
                        Intent.URI_INTENT_SCHEME);
                intent.addCategory("android.intent.category.BROWSABLE");
                intent.setComponent(null);
                startActivity(intent);
            } catch (Exception e) {
                ToastUtils.showToast("请安装最新版支付宝");
            }
            return true;
        } else if (url.contains("qqapi")) {
            try {
                Uri uri = Uri.parse(url);
                Intent intent;
                intent = Intent.parseUri(url,
                        Intent.URI_INTENT_SCHEME);
                intent.addCategory("android.intent.category.BROWSABLE");
                intent.setComponent(null);
                startActivity(intent);
            } catch (Exception e) {
                ToastUtils.showToast("请安装最新版腾讯QQ");
            }
            return true;
        } else if (url.contains("tmast://appdetails?")) {
            try {
                Uri uri = Uri.parse(url);
                Intent intent = new Intent();
                intent.setAction(Intent.ACTION_VIEW);
                intent.setData(Uri.parse(url));
                startActivity(intent);
            } catch (Exception e) {
                Toast.makeText(HtmlActivity.this, "请安装最新版应用宝", Toast.LENGTH_SHORT).show();
            }
            return true;
        } else if (url.endsWith(".apk")) {
            downloadApk(url);
            return true;

        } else {
            return false;
        }

    }

    /**
     * 应用内拦截下载
     */
    private void downloadApk(String url) {

        Intent intent = new Intent(this, DownAPKService.class);
        intent.putExtra("apk_url", url);
        startService(intent);

    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        webView.destroy();
    }

    @Override
    protected MainPersenter createPresenter() {
        return new MainPersenter(this);
    }

    @OnClick(R.id.toolbar_back)
    public void onViewClicked() {
        finish();
    }
}
