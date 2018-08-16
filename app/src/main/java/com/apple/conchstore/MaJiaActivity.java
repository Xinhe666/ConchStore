package com.apple.conchstore;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import com.apple.conchstore.fragment.AddFragment;
import com.apple.conchstore.fragment.DetailFragment;
import com.apple.conchstore.fragment.MineFragment;
import com.apple.conchstore.util.DoubleClickExit;
import com.umeng.analytics.MobclickAgent;
import com.wingsofts.byeburgernavigationview.ByeBurgerBehavior;

import java.util.ArrayList;
import java.util.List;

public class MaJiaActivity extends AppCompatActivity {
    private RadioGroup rgMain;
    private ViewPager vpMain;
    private MineFragment mineFragment;
    private RadioButton detail, my, add;
    private ByeBurgerBehavior mBehavior;
    private DetailFragment detailFragment;
    private ViewPager mViewPager;
    private List<Fragment> fragmentList;
    private BottomNavigationView mNavigationView;

    public static void launch(Context context) {
        context.startActivity(new Intent(context, MaJiaActivity.class));
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);

        initDate();
        initView();
    }

    private void initDate() {
        fragmentList = new ArrayList<>();

        fragmentList.add(DetailFragment.newInstance());

        fragmentList.add(AddFragment.newInstance("发现"));

        fragmentList.add(MineFragment.newInstance("关于"));
    }

    private void initView() {
        mViewPager = (ViewPager) findViewById(R.id.viewpager);
        mNavigationView = (BottomNavigationView) findViewById(R.id.bye_burger);
        mViewPager.setAdapter(new FragmentPagerAdapter(getSupportFragmentManager()) {
            @Override
            public Fragment getItem(int position) {
                return fragmentList.get(position);
            }

            @Override
            public int getCount() {
                return fragmentList.size();
            }
        });
        mViewPager.setCurrentItem(1);
        mNavigationView.setOnNavigationItemSelectedListener(
                new BottomNavigationView.OnNavigationItemSelectedListener() {
                    @Override
                    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                        if (item.getTitle().equals("首页")) {
                            mViewPager.setCurrentItem(0);

                        } else if (item.getTitle().equals("发现")) {
                            mViewPager.setCurrentItem(1);


                        } else if (item.getTitle().equals("关于")) {
                            mViewPager.setCurrentItem(2);
                        }
                        return false;
                    }
                });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == 202 && mineFragment != null) {
            mineFragment.onActivityResult(requestCode, resultCode, data);
        }
        super.onActivityResult(requestCode, resultCode, data);
    }

    @Override
    public void onResume() {
        super.onResume();
        MobclickAgent.onResume(this);
    }

    @Override
    public void onPause() {
        super.onPause();
        MobclickAgent.onPause(this);
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
