package com.apple.conchstore.live.ui;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;

import com.alibaba.fastjson.JSONObject;
import com.apple.conchstore.MaJiaActivity;
import com.apple.conchstore.R;
import com.apple.conchstore.live.net.Api;
import com.apple.conchstore.live.net.OkgoUtils;
import com.apple.conchstore.live.net.OnRequestListener;
import com.apple.conchstore.live.ui.main.MainActivity;
import com.apple.conchstore.live.utils.Contents;
import com.apple.conchstore.live.utils.SPUtil;
import com.jaeger.library.StatusBarUtil;
import java.lang.ref.WeakReference;

/**
 * @author yanshihao
 */
public class SplashActivity extends AppCompatActivity {

    private SwitchHandler mHandler = new SwitchHandler(this);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        StatusBarUtil.setColor(this, getResources().getColor(R.color.theme_color), 40);

        boolean open = SPUtil.getBoolean(SplashActivity.this, "open", false);
        if (!open) {
            setUrl();
        } else {
            mHandler.sendEmptyMessageDelayed(3, 1000);
        }
    }

    private void setUrl() {
        OkgoUtils.execute(Api.getStatus, null, null, new OnRequestListener() {
            @Override
            public void requestSuccess(JSONObject jsonObject) {
                JSONObject data = jsonObject.getJSONObject("data");
                int status = data.getIntValue("status");
                if (status == 0) {
                    mHandler.sendEmptyMessageDelayed(2
                            , 1000);
                } else {
                    SPUtil.putBoolean(SplashActivity.this, "open", true);
                    setWelcome();
                }
            }

            @Override
            public void requestFailure(String error) {

            }
        });
    }


    private static class SwitchHandler extends Handler {
        private WeakReference<SplashActivity> mWeakReference;

        SwitchHandler(SplashActivity activity) {
            mWeakReference = new WeakReference<>(activity);

        }

        @Override
        public void handleMessage(Message msg) {
            SplashActivity activity = mWeakReference.get();
            if (activity != null) {
                switch (msg.what) {
                    case 2:
                        activity.startActivity(new Intent(activity, MaJiaActivity.class));
                        activity.finish();
                        break;
                    case 3:
                        activity.startActivity(new Intent(activity, MainActivity.class));
                        activity.finish();
                        break;
                    default:
                        break;
                }
            }
        }
    }

    private void setWelcome() {

        boolean isFirstOpen = SPUtil.getBoolean(SplashActivity.this, Contents.FRIST, true);
        if (isFirstOpen) {
            Intent intent = new Intent(SplashActivity.this, GuideActivity.class);
            startActivity(intent);
            finish();
            return;
        } else {
            mHandler.sendEmptyMessageDelayed(3, 1000);
        }
    }


}


