package com.apple.conchstore.live;

import android.app.Application;
import android.content.Context;
import android.net.ConnectivityManager;

import com.apple.conchstore.R;
import com.apple.conchstore.live.utils.Contents;
import com.apple.conchstore.live.utils.LogUtils;
import com.lzy.okgo.OkGo;
import com.lzy.okgo.cache.CacheEntity;
import com.lzy.okgo.cache.CacheMode;
import com.lzy.okgo.model.HttpParams;
import com.meituan.android.walle.WalleChannelReader;
import com.umeng.commonsdk.UMConfigure;

/**
 * - @Author:  闫世豪
 * - @Time:  2018/8/6 下午12:54
 * - @Email whynightcode@gmail.com
 */
public class AppManger extends Application {

    private static Context mContext;


    @Override
    public void onCreate() {
        super.onCreate();
        mContext = this;
        initOKGO();
        initUMeng();
    }

    public static boolean isNet() {
        ConnectivityManager con = (ConnectivityManager) mContext.getSystemService(CONNECTIVITY_SERVICE);
        boolean wifi = con.getNetworkInfo(ConnectivityManager.TYPE_WIFI).isConnectedOrConnecting();
        boolean internet = con.getNetworkInfo(ConnectivityManager.TYPE_MOBILE).isConnectedOrConnecting();

        return wifi | internet;
    }

    private void initUMeng() {
        String channel = WalleChannelReader.getChannel(this.getApplicationContext());
        UMConfigure.init(this, Contents.UMENG, channel, UMConfigure.DEVICE_TYPE_PHONE, null);
        LogUtils.e("app",channel);
    }

    private void initOKGO() {
        HttpParams params = new HttpParams();
        String name = getResources().getString(R.string.appName);
        params.put("market", "baidu");
        params.put("name", name);
        OkGo.getInstance().init(this)
                .setCacheMode(CacheMode.NO_CACHE)
                .setCacheTime(CacheEntity.CACHE_NEVER_EXPIRE)
                .setRetryCount(3)
                .addCommonParams(params);
    }

    public static Context getApplication() {
        return mContext;
    }
}
