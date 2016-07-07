package com.example.luhui1hao.newmp3player.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.FragmentActivity;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.Toolbar;
import android.view.View;

import com.example.luhui1hao.newmp3player.R;
import com.example.luhui1hao.newmp3player.adapter.SimpleFragmentPagerAdapter;
import com.example.luhui1hao.newmp3player.model.Mp3Info;
import com.example.luhui1hao.newmp3player.service.PlayerService;
import com.example.luhui1hao.newmp3player.sqlite.MyDatabaseHelper;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends FragmentActivity{
    private SimpleFragmentPagerAdapter pagerAdapter;
    private ViewPager viewPager;
    private TabLayout tabLayout;
    private List<Mp3Info> list = new ArrayList<>();
    private int position;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        initToolBar();
        initTabLayout();
        initMp3ListInfo();
    }

    private void initMp3ListInfo() {
        //TODO 根据SharePreference中的数据，加入list和position数据

    }

    public void startSwitch(View view){
        Intent intent = new Intent(MainActivity.this, PlayerActivity.class);
        startActivity(intent);
        overridePendingTransition(R.anim.player_enter,R.anim.main_quit);
    }

    private void initTabLayout() {
        pagerAdapter = new SimpleFragmentPagerAdapter(getSupportFragmentManager(), this);
        viewPager = (ViewPager) findViewById(R.id.viewpager);
        viewPager.setAdapter(pagerAdapter);
        tabLayout = (TabLayout) findViewById(R.id.sliding_tabs_toolbar);
        tabLayout.setupWithViewPager(viewPager);
        tabLayout.setTabMode(TabLayout.MODE_FIXED);
        for (int i = 0; i < tabLayout.getTabCount(); i++) {
            TabLayout.Tab tab = tabLayout.getTabAt(i);
            tab.setCustomView(pagerAdapter.getTabView(i));
        }
        //临时凑的数，就是想解决默认选中的问题
        tabLayout.setScrollPosition(1,0,true);
    }

    private void initToolBar(){
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setNavigationIcon(R.mipmap.actionbar_menu);//设置导航栏图标
        toolbar.inflateMenu(R.menu.base_toolbar_menu);//设置右上角的填充菜单
    }

}
