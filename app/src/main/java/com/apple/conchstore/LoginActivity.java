package com.apple.conchstore;

import android.content.Intent;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.apple.conchstore.util.DoubleClickExit;
import com.apple.conchstore.util.SPUtils;
import com.umeng.analytics.MobclickAgent;

public class LoginActivity extends AppCompatActivity implements View.OnClickListener{

    private static final String USER_NAME = "test";
    private static final String PASSWORD = "123456";
    private EditText etUserName;
    private EditText etPassword;
    private Button btLogin;
    private TextView tvRegister;
   // private TextView tvForgetPw;
    private ImageView ivClose;
    private Handler handler = new Handler();
    private int requestCode;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        initView();
    }

    private void initView(){
        etUserName = (EditText) findViewById(R.id.et_user_name);
        etPassword = (EditText) findViewById(R.id.et_password);
        tvRegister = (TextView) findViewById(R.id.tv_register);
       // tvForgetPw = (TextView) findViewById(R.id.tv_forget_pw);
        btLogin = (Button) findViewById(R.id.bt_login);
        ivClose = (ImageView) findViewById(R.id.iv_close);

        btLogin.setOnClickListener(this);
        ivClose.setOnClickListener(this);
      //  tvForgetPw.setOnClickListener(this);
        tvRegister.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.bt_login:
                final String strUserName = etUserName.getText().toString();
                String strPassWord = etPassword.getText().toString();
                if(!TextUtils.isEmpty(strUserName) && !TextUtils.isEmpty(strPassWord) ){
                    String o = (String) SPUtils.get(this, strUserName, strPassWord);
                    if(o.length()>0){
                        //模拟登录延迟
                        handler.postDelayed(new Runnable() {
                            @Override
                            public void run() {
                                setResult(-1);
                                SPUtils.put(LoginActivity.this,"username",strUserName);
                                Toast.makeText(LoginActivity.this, "登陆成功", Toast.LENGTH_SHORT).show();
                                finish();
                            }
                        }, 1000);

                    }else{
                        Toast.makeText(this, "用户名或密码不正确", Toast.LENGTH_SHORT).show();
                    }
                }else{
                    Toast.makeText(this, "用户名或密码不能为空", Toast.LENGTH_SHORT).show();
                }
                break;
            case R.id.iv_close:
                this.finish();
                break;
           /* case R.id.tv_forget_pw:
                Toast.makeText(this, "忘记密码？密码是"+ PASSWORD, Toast.LENGTH_SHORT).show();
                break;*/
            case R.id.tv_register:
                    startActivity(new Intent(this,RegisterActivity.class));
                    finish();
                break;
        }
    }
    public void onResume() {
        super.onResume();
        MobclickAgent.onResume(this);
    }
    public void onPause() {
        super.onPause();
        MobclickAgent.onPause(this);
    }
    @Override
    public void onBackPressed() {
        if (!DoubleClickExit.check()) {
            Toast.makeText(this,"再按一次退出",Toast.LENGTH_SHORT).show();
        } else {
            finish();
        }
    }
}
