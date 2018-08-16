package com.apple.conchstore.fragment;


import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.SwitchCompat;
import android.support.v7.widget.Toolbar;
import android.text.InputType;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.apple.conchstore.LoginActivity;
import com.apple.conchstore.R;
import com.apple.conchstore.model.DBManager;
import com.apple.conchstore.util.SPUtils;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * A simple {@link Fragment} subclass.
 */
public class MineFragment extends Fragment implements View.OnClickListener {
    public static String NAME = "NAME";
    private static final String TAG = MineFragment.class.getSimpleName();
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    @BindView(R.id.limit)
    TextView limit;

    private String mParam1;
    private String mParam2;
    private boolean isLogin = false;

    private View parent;
    private Toolbar toolbar;
    private CollapsingToolbarLayout collapsingToolbarLayout;
    private TextView tvLogin;
    private TextView tvClearData;
    private TextView tvSpendLimit;
    private RelativeLayout rlSpendLimit;
    private TextView tvFeedback;
    private TextView tvCheckUpdate;
    private SwitchCompat switchCompatNeedPw;

    private DBManager mDBManager;

    public static MineFragment newInstance(String text) {
        Bundle args = new Bundle();
        args.putString(NAME, text);
        MineFragment fragment = new MineFragment();
        fragment.setArguments(args);
        return fragment;
    }

    public MineFragment() {
    }

    public static MineFragment newInstance(String param1, String param2) {
        MineFragment fragment = new MineFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        parent = inflater.inflate(R.layout.fragment_mine, container, false);
       /* if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            LinearLayout barLl = (LinearLayout) parent.findViewById(R.id.me_fragment_top);
            barLl.setVisibility(View.VISIBLE);
            LinearLayout.LayoutParams ll = (LinearLayout.LayoutParams) barLl.getLayoutParams();
            ll.height = Utill.getStatusBarHeight(getContext());
            ll.width = RelativeLayout.LayoutParams.MATCH_PARENT;
            barLl.setLayoutParams(ll);
            barLl.setBackgroundColor(ContextCompat.getColor(getContext(), R.color.window_background));
        }*/
        initView();
        ButterKnife.bind(this, parent);
        return parent;
    }

    private void initView() {
        mDBManager=new DBManager(getContext());
        tvLogin = (TextView) parent.findViewById(R.id.tv_login);
        collapsingToolbarLayout = (CollapsingToolbarLayout) parent.findViewById(R.id.collapse_toolbar);
        toolbar = (Toolbar) parent.findViewById(R.id.toolbar);
        tvClearData = (TextView) parent.findViewById(R.id.tv_clear_data);
        tvSpendLimit = (TextView) parent.findViewById(R.id.tv_spend_limit);
        tvFeedback = (TextView) parent.findViewById(R.id.tv_feedback);
        tvCheckUpdate = (TextView) parent.findViewById(R.id.tv_check_update);
        rlSpendLimit = (RelativeLayout) parent.findViewById(R.id.rl_spend_limit);
        switchCompatNeedPw = (SwitchCompat) parent.findViewById(R.id.switch_need_pw);

        String userName = (String) SPUtils.get(getActivity(), "username", "");
        if (TextUtils.isEmpty(userName)) {
            isLogin = false;
            tvLogin.setOnClickListener(this);
        } else {
            tvLogin.setText(userName);
            isLogin = true;
            long spendLimit = (long) SPUtils.get(getActivity(), "spendlimit", 3000L);
            tvSpendLimit.setText("￥" + spendLimit);
        }
        Long s= 0L;
        long spendLimit = (long) SPUtils.get(getActivity(), "spendlimit", s);
        tvSpendLimit.setText("￥" + spendLimit);
        rlSpendLimit.setOnClickListener(this);
        tvFeedback.setOnClickListener(this);
        tvCheckUpdate.setOnClickListener(this);
        tvClearData.setOnClickListener(this);
        switchCompatNeedPw.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean checked) {
                if (checked) {
                    SPUtils.put(MineFragment.this.getActivity(), "haspassword", true);
                } else {
                    SPUtils.put(MineFragment.this.getActivity(), "haspassword", false);
                }
            }
        });

        collapsingToolbarLayout.setTitleEnabled(false);
    }

    public void onButtonPressed(Uri uri) {
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
    }

    @Override
    public void onDetach() {
        super.onDetach();
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.tv_login:
                Intent intent = new Intent(getActivity(), LoginActivity.class);
                startActivityForResult(intent, 201);
                break;
            case R.id.rl_spend_limit:
                showLimitDialog();
                break;
            case R.id.tv_feedback:
                showDialog("维护中哦！");
                break;
            case R.id.tv_check_update:
                showDialog("已经是最新版本了哦！");
                break;
            case R.id.tv_clear_data:
                showClearDataDialog();
                break;

        }
    }

    private void showLimitDialog() {
        final EditText etLimit = new EditText(this.getActivity());
        etLimit.setInputType(InputType.TYPE_CLASS_NUMBER);
        new AlertDialog.Builder(this.getActivity())
                .setTitle("请输入")
                .setView(etLimit)
                .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        if (etLimit.getText() == null || "".equals(etLimit.getText().toString())) {
                            tvSpendLimit.setText("￥0");
                        } else {
                            Long spendLimit = Long.valueOf(etLimit.getText().toString());
                            tvSpendLimit.setText("￥" + spendLimit);
                            SPUtils.put(MineFragment.this.getActivity(), "spendlimit", spendLimit);
                        }
                        dialogInterface.dismiss();
                    }
                })
                .setNegativeButton("取消", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        dialogInterface.dismiss();
                    }
                })
                .show();
    }

    private void showDialog(String message) {
        new AlertDialog.Builder(this.getActivity())
                .setTitle("提示")
                .setMessage(message)
                .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        dialogInterface.dismiss();
                    }
                })
                .show();
    }

    //后期需要对是否加密做判断
    private void showClearDataDialog() {
        new AlertDialog.Builder(this.getActivity())
                .setTitle("提示")
                .setMessage("您确定要删除所有数据吗?")
                .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        mDBManager.deleteAllBill();
                        Toast.makeText(MineFragment.this.getActivity(), "删除完成", Toast.LENGTH_SHORT).show();
                    }
                })
                .setNegativeButton("取消", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        Toast.makeText(MineFragment.this.getActivity(), "取消", Toast.LENGTH_SHORT).show();
                    }
                })
                .show();
    }

    private void showPasswordDialog() {
        final EditText etPassword = new EditText(this.getActivity());
        etPassword.setInputType(InputType.TYPE_CLASS_NUMBER);
        new AlertDialog.Builder(this.getActivity())
                .setTitle("请输入密码")
                .setView(etPassword)
                .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        SPUtils.put(MineFragment.this.getActivity(), etPassword.getText().toString(), "");
                        dialogInterface.dismiss();
                    }
                })
                .setNegativeButton("取消", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        dialogInterface.dismiss();
                    }
                })
                .show();
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {

            if (resultCode == -1) {
                tvLogin.setText((String) SPUtils.get(getActivity(), "username", ""));
                isLogin = true;
            }
        super.onActivityResult(requestCode, resultCode, data);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();

    }
}
