package com.jaesun.mortgagecalculator.activities;

import android.os.Bundle;
import android.util.SparseArray;
import android.widget.RadioButton;
import android.widget.RadioGroup;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;
import androidx.viewpager.widget.ViewPager;

import com.jaesun.mortgagecalculator.R;
import com.jaesun.mortgagecalculator.fragments.CommercialLoanFragment;
import com.jaesun.mortgagecalculator.fragments.FundLoanFragment;
import com.jaesun.mortgagecalculator.fragments.GroupLoanFragment;



public class MainActivity extends AppCompatActivity {

    private SparseArray<Fragment> mainFragments;
    private RadioGroup mainRadioGroup;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //1. 初始化 添加所有的主页Fragment 的数组
        mainFragments = new SparseArray<>();
        CommercialLoanFragment commercialLoanFragment =  new CommercialLoanFragment();
        mainFragments.append(R.id.today_tab,commercialLoanFragment);

        FundLoanFragment fundLoanFragment = new FundLoanFragment();
        mainFragments.append(R.id.record_tab,fundLoanFragment);

        GroupLoanFragment groupLoanFragment = new GroupLoanFragment();
        mainFragments.append(R.id.contact_tab,groupLoanFragment);

        // 底部导航栏
        mainRadioGroup = findViewById(R.id.bottom_nav_bar);

        mainRadioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup radioGroup, int i) {
                getSupportFragmentManager().beginTransaction().replace(R.id.view_container,
                        mainFragments.get(i)).commit();
            }
        });

        getSupportFragmentManager().beginTransaction().add(R.id.view_container,
                mainFragments.get(R.id.today_tab)).commit();

    }
}
