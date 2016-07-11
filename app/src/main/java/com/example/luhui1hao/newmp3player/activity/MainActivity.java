package com.example.luhui1hao.newmp3player.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.FragmentActivity;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.ScrollView;
import android.widget.TextView;

import com.example.luhui1hao.newmp3player.R;
import com.example.luhui1hao.newmp3player.adapter.SimpleFragmentPagerAdapter;
import com.example.luhui1hao.newmp3player.model.Mp3Info;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends FragmentActivity {
    private SimpleFragmentPagerAdapter pagerAdapter;
    private ViewPager viewPager;
    private TabLayout tabLayout;
    private List<Mp3Info> list = new ArrayList<>();
    private int position;
    private ListView drawerLv;
    private DrawerLvAdapter drawerLvAdapter;
    private ScrollView scrollView;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        initToolBar();
        initTabLayout();
        initMp3ListInfo();
        initNavigationDrawer();
    }

    private void initNavigationDrawer() {
        drawerLv = (ListView) findViewById(R.id.drawer_lv);
        drawerLvAdapter = new DrawerLvAdapter();
        drawerLv.setAdapter(drawerLvAdapter);
        scrollView = (ScrollView)findViewById(R.id.drawer_scroll);
        scrollView.smoothScrollTo(0, 0);
    }

    private void initMp3ListInfo() {
        //TODO 根据SharePreference中的数据，加入list和position数据

    }

    public void startSwitch(View view) {
        Intent intent = new Intent(MainActivity.this, PlayerActivity.class);
        startActivity(intent);
        overridePendingTransition(R.anim.player_enter, R.anim.main_quit);
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
        tabLayout.setScrollPosition(1, 0, true);
    }

    private void initToolBar() {
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setNavigationIcon(R.mipmap.actionbar_menu);//设置导航栏图标
        toolbar.inflateMenu(R.menu.base_toolbar_menu);//设置右上角的填充菜单
    }

    class DrawerLvAdapter extends BaseAdapter {
        ImageView iv;
        TextView tv;
        private int[] imags = {R.mipmap.topmenu_icn_msg,
                R.mipmap.topmenu_icn_store,
                R.mipmap.topmenu_icn_member,
                R.mipmap.topmenu_icn_free,
                R.mipmap.topmenu_icn_identify,
                R.mipmap.topmenu_icn_skin,
                R.mipmap.topmenu_icn_night,
                R.mipmap.topmenu_icn_time,
                R.mipmap.topmenu_icn_clock,
                R.mipmap.topmenu_icn_vehicle,
                R.mipmap.topmenu_icn_cloud};
        private String[] texts = {"我的消息","积分商城","会员中心","在线听歌免流量",
                "听歌识曲","主题换肤","夜间模式","定时停止播放","音乐闹钟","驾驶模式","我的音乐云盘"};

        @Override
        public int getCount() {
            return imags.length;
        }

        @Override
        public Object getItem(int position) {
            return null;
        }

        @Override
        public long getItemId(int position) {
            return 0;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            View view = LayoutInflater.from(MainActivity.this).inflate(R.layout.drawer_lv_item, parent, false);
            iv = (ImageView)view.findViewById(R.id.drawer_lv_icn);
            tv = (TextView)view.findViewById(R.id.drawer_lv_tv);
            setSource(position);
            return view;
        }

        private void setSource(int position) {
            iv.setImageResource(imags[position]);
            tv.setText(texts[position]);
        }
    }

}
