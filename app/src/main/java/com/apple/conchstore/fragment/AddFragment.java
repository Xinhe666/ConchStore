package com.apple.conchstore.fragment;


import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;


import com.apple.conchstore.R;

import org.greenrobot.eventbus.EventBus;

import java.util.Calendar;

/**
 * A simple {@link Fragment} subclass.
 */
public class AddFragment extends Fragment implements View.OnClickListener{
    public static String NAME = "NAME";


    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    private static final int PAGE_NUM = 2;
    private String mParam1;
    private String mParam2;
    private int year = 0;
    private int month = 0;
    private int day = 0;

    private View parent;
    private ViewPager vpAdd;
    private TabLayout tabLayout;
    private Toolbar toolbar;
    private FloatingActionButton fabtSubmit;
    private TextView tvCalendar;

    private OnIncomeFabtClickListener onIncomeFabtClickListener;
    private OnSpendFabtClickListener onSpendFabtClickListener;

    public static AddFragment newInstance(String text) {
        Bundle args = new Bundle();
        args.putString(NAME, text);
        AddFragment fragment = new AddFragment();
        fragment.setArguments(args);
        return fragment;
    }
    public AddFragment() {
        // Required empty public constructor
    }

   /* public static AddFragment newInstance() {
        AddFragment fragment = new AddFragment();
    *//*    Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);*//*
        return fragment;
    }*/

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
        parent = inflater.inflate(R.layout.fragment_add, container, false);
       /* if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            LinearLayout barLl = (LinearLayout) parent.findViewById(R.id.me_fragment_top);
            barLl.setVisibility(View.VISIBLE);
            CoordinatorLayout.LayoutParams ll = (CoordinatorLayout.LayoutParams) barLl.getLayoutParams();
            ll.height = Utill.getStatusBarHeight(getContext());
            ll.width = RelativeLayout.LayoutParams.MATCH_PARENT;
            barLl.setLayoutParams(ll);
            barLl.setBackgroundColor(ContextCompat.getColor(getContext(), R.color.window_background));
        }*/
        vpAdd = (ViewPager) parent.findViewById(R.id.vp_add);
        toolbar = (Toolbar) parent.findViewById(R.id.toolbar);
        tabLayout = (TabLayout) parent.findViewById(R.id.tl_add);
        fabtSubmit = (FloatingActionButton) parent.findViewById(R.id.fabt_submit);
        tvCalendar = (TextView) parent.findViewById(R.id.tv_calendar);

        Calendar calendar = Calendar.getInstance();
        year = calendar.get(Calendar.YEAR);
        month = calendar.get(Calendar.MONTH) + 1;
        day = calendar.get(Calendar.DAY_OF_MONTH);
        Log.e("----OUT", "year:" + year + "month:" + month + "day:" + day);
        tvCalendar.setText(year % 100 + "-" + (month > 9 ? month : "0" + month) +"-"+ (day > 9 ? day : "0" + day));
        fabtSubmit.setOnClickListener(this);
        tabLayout.setTabMode(TabLayout.MODE_FIXED);
        vpAdd.setAdapter(new AddPagerAdapter(getChildFragmentManager()));
        tabLayout.setupWithViewPager(vpAdd);
        toolbar = (Toolbar) parent.findViewById(R.id.toolbar);
        return parent;
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.fabt_submit:
                if(vpAdd.getCurrentItem() == 0){
                    if(onSpendFabtClickListener != null){
                        onSpendFabtClickListener.onFabtClick(year, month, day);
                    }
                }else{
                    if(onIncomeFabtClickListener != null){
                        onIncomeFabtClickListener.onFabtClick(year, month, day);
                    }
                }
                Toast.makeText(getContext(), "添加成功", Toast.LENGTH_SHORT).show();
                EventBus.getDefault().post("updata");
                break;
        }
    }

    public void setOnIncomeFabtClickListener(OnIncomeFabtClickListener onIncomeFabtClickListener) {
        this.onIncomeFabtClickListener = onIncomeFabtClickListener;
    }

    public void setOnSpendFabtClickListener(OnSpendFabtClickListener onSpendFabtClickListener) {
        this.onSpendFabtClickListener = onSpendFabtClickListener;
    }

    public interface OnIncomeFabtClickListener{
        void onFabtClick(int year, int month, int day);//留出选择时间的接口
    }
    public interface OnSpendFabtClickListener{
        void onFabtClick(int year, int month, int day);//留出选择时间的接口
    }
    class AddPagerAdapter extends FragmentPagerAdapter {

        public AddPagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {
            switch (position){
                case 0:
                    return AddSpendFragment.newInstance("", "");
                case 1:
                    return AddIncomeFragment.newInstance("","");

            }
            return null;
        }

        @Override
        public int getCount() {
            return PAGE_NUM;
        }

        @Override
        public CharSequence getPageTitle(int position) {
            switch (position){
                case 0:
                    return "添加支出";
                case 1:
                    return "添加收入";
            }
            return null;
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }
}
