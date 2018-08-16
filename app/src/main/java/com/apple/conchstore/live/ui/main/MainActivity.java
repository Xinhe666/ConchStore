package com.apple.conchstore.live.ui.main;

import android.graphics.Color;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.MenuItem;
import android.widget.Toast;

import com.apple.conchstore.R;
import com.apple.conchstore.live.base.PermissionActivity;
import com.apple.conchstore.live.ui.home.HomeFragment;
import com.apple.conchstore.live.ui.loan.ProductFragment;
import com.apple.conchstore.live.ui.my.MyFragment;
import com.apple.conchstore.util.DoubleClickExit;
import com.ittianyu.bottomnavigationviewex.BottomNavigationViewEx;
import com.jaeger.library.StatusBarUtil;
import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * @author yanshihao
 */
public class MainActivity extends PermissionActivity<MainMvpView, MainPersenter> implements MainMvpView {

    private MainPersenter mMainPersenter;

    @BindView(R.id.bottom_navigation_bar)
    BottomNavigationViewEx mNavigationView;

    private FragmentManager mManager;


    private Fragment mFragments[] = new Fragment[3];
    private ProductFragment mProductFragment;
    private MyFragment mFragment;

    @Override
    protected int setLayout() {
        return R.layout.activity_main_store;
    }

    @Override
    protected void initView() {
        ButterKnife.bind(this);
        StatusBarUtil.setColor(this, Color.parseColor("#ffffff"), 10);
        mNavigationView.enableAnimation(false);
        mNavigationView.enableShiftingMode(false);
        mNavigationView.enableItemShiftingMode(false);
        mNavigationView.setIconSize(20, 20);
        mNavigationView.setTextSize(10);
        mNavigationView.setIconsMarginTop(BottomNavigationViewEx.dp2px(this, 8));
        mNavigationView.setItemHeight(BottomNavigationViewEx.dp2px(this, 48));
        mNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId()) {
                    case R.id.main:
                        selectFragment(0);
                        break;
                    case R.id.empty:
                        selectFragment(1);
                        break;
                    case R.id.my:
                        selectFragment(2);
                        break;
                    default:
                        break;
                }
                return true;
            }
        });
    }

    @Override
    protected void initData() {
        mManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = mManager.beginTransaction();
        HomeFragment homeFragment = new HomeFragment();
        fragmentTransaction.add(R.id.content_fl, homeFragment);
        fragmentTransaction.commit();
        mFragments[0] = homeFragment;
        checkPermission(this);
    }

    @Override
    protected MainPersenter createPresenter() {
        mMainPersenter = new MainPersenter(this);
        return mMainPersenter;
    }

    private int currPosition = 0;

    public void selectFragment(int position) {
        FragmentTransaction fragmentTransaction = mManager.beginTransaction();
        switch (position) {
            case 0:
                StatusBarUtil.setColor(this, Color.parseColor("#ffffff"), 10);
                fragmentTransaction.hide(mFragments[currPosition]).show(mFragments[0])
                        .commit();
                currPosition = 0;
                break;
            case 1:
                if (mProductFragment == null) {
                    mProductFragment = new ProductFragment();
                    mFragments[1] = mProductFragment;
                    fragmentTransaction.add(R.id.content_fl, mProductFragment);
                }
                fragmentTransaction.hide(mFragments[currPosition]).show(mFragments[1])
                        .commit();
                currPosition = 1;
                StatusBarUtil.setColor(this, Color.parseColor("#51B1FA"), 40);

                break;
            case 2:
                if (mFragment == null) {
                    mFragment = new MyFragment();
                    mFragments[2] = mFragment;
                    fragmentTransaction.add(R.id.content_fl, mFragment);
                }
                fragmentTransaction.hide(mFragments[currPosition]).show(mFragments[2])
                        .commit();
                currPosition = 2;
                StatusBarUtil.setColor(this, Color.parseColor("#51B1FA"), 40);

                break;

            default:
                break;
        }
    }

    @Override
    public void onBackPressed() {
        if (!DoubleClickExit.check()) {
            Toast.makeText(this, "再按一次退出", Toast.LENGTH_SHORT).show();
        } else {
            finish();
        }
    }
}
