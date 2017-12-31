package com.example.gypc.petsday;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.MenuItem;
import android.view.WindowManager;
import android.widget.Button;

import com.example.gypc.petsday.model.Pet;
import com.example.gypc.petsday.utils.AppContext;
import com.youth.banner.Banner;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    public static final int
            LOGIN_REQ_CODE = 1,
            ADD_PET_REQ_CODE = 2,
            EDIT_PET_CODE = 3,
            NEW_HOTSPOT_REQ_CODE = 4;

    private ViewPager viewPager;
    private MenuItem menuItem;
    private BottomNavigationView navigation;
    private Banner banner;

    private boolean hasInit = false;

    //为底部导航栏添加监听，当导航栏有所改动时，设置相应的Fragment
    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener
            = new BottomNavigationView.OnNavigationItemSelectedListener() {
        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            switch (item.getItemId()) {
                case R.id.navigation_hotspot:
                    viewPager.setCurrentItem(0);
                    return true;
                case R.id.navigation_market:
                    viewPager.setCurrentItem(1);
                    return true;
                case R.id.navigation_mine:
                    viewPager.setCurrentItem(2);
                    return true;
            }
            return false;
        }

    };

    //为viewPager添加监听，设置当viewpager有所变动时，更改导航栏的选项
    private ViewPager.OnPageChangeListener mOnPageChangeListener
            = new ViewPager.OnPageChangeListener() {
        @Override
        public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
        }
        @Override
        public void onPageSelected(int position) {
            if (menuItem != null) {
                menuItem.setChecked(false);
            } else {
                navigation.getMenu().getItem(0).setChecked(false);
            }
            menuItem = navigation.getMenu().getItem(position);
            menuItem.setChecked(true);
        }
        @Override
        public void onPageScrollStateChanged(int state) {
        }
    };



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        try {
            if (!isLogin()) {
                navigateToLoginRegisterPage();
            } else {
                init();
            }
        } catch (Exception e) {
            Log.e("MainActivity", "onCreate", e);
        }
        getWindow().setSoftInputMode(
                WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN
                        | WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);
    }

    private boolean isLogin() {
        AppContext app = AppContext.getInstance();
        String name = app.getLoginUsername();
        return name != null;
    }

    // 控件初始化
    private void init() {
        if (hasInit)
            return;
        viewPager = (ViewPager) findViewById(R.id.vPager);
        navigation = (BottomNavigationView) findViewById(R.id.navigation);
        navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);
        viewPager.addOnPageChangeListener(mOnPageChangeListener);
        setupViewPager(viewPager);
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN | WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);
        hasInit = true;
    }

    //添加fragment，进行页面切换
    private void setupViewPager(ViewPager viewPager) {
        ViewPagerAdapter adapter = new ViewPagerAdapter(getSupportFragmentManager());
        adapter.addFragment(new HotSpotFragment());
        adapter.addFragment(new MarketFragment());
        adapter.addFragment(new MineFragment());
        viewPager.setAdapter(adapter);
    }

    private void navigateToLoginRegisterPage() {
        startActivityForResult(new Intent(this, LoginRegisterNavigateActivity.class), LOGIN_REQ_CODE);
    }

    @Override
    protected void onActivityResult(int reqCode, int resCode, Intent dataIntent) {
        if (reqCode == LOGIN_REQ_CODE) {
            if (resCode == LoginRegisterNavigateActivity.LOGIN_OK) {
                init();
            } else if (resCode == LoginRegisterNavigateActivity.QUIT_RES_CODE) {
                finish();
            }
        } else if (reqCode == ADD_PET_REQ_CODE) {
            if (resCode == NewpetActivity.SUCCESS_RES_CODE) {
                MineFragment.getInstance().addPet(dataIntent.getExtras());
            }
        } else if (reqCode == EDIT_PET_CODE) {
            if (resCode == NewpetActivity.SUCCESS_RES_CODE) {
                MineFragment.getInstance().updatePet(dataIntent.getExtras());
            }
        } else if (reqCode == NEW_HOTSPOT_REQ_CODE) {
            if (resCode == PublishActivity.PUBLISH_SUCCESS) {
                HotSpotFragment.getInstance().addHotspot(dataIntent.getExtras());
            }
        }
    }
}
