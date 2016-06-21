package com.example.luhui1hao.newmp3player.activity;

import android.annotation.TargetApi;
import android.app.Activity;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.FragmentActivity;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.Toolbar;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.TextView;

import com.example.luhui1hao.newmp3player.R;
import com.example.luhui1hao.newmp3player.adapter.LocalMusicFragmentPagerAdapter;
import com.example.luhui1hao.newmp3player.adapter.SimpleFragmentPagerAdapter;
import com.example.luhui1hao.newmp3player.model.Mp3Info;
import com.example.luhui1hao.newmp3player.sqlite.MyDatabaseHelper;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by luhui on 2016/6/16.
 */
public class LocalMusicActivity extends FragmentActivity {
    private LocalMusicFragmentPagerAdapter pagerAdapter;
    private ViewPager viewPager;
    private TabLayout tabLayout;
    private List<Mp3Info> list =  new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.local_activity_layout);
        initToolbar();

        //从数据库中取出本地音乐list
        list = obtainList();

        initTabLayout();
    }

    private List<Mp3Info> obtainList() {
        //从数据库中取出list
        return MyDatabaseHelper.getInstance(this).getMp3Infos();
    }

    private void initTabLayout() {
        //在Fragment里面判断加载哪种布局
        pagerAdapter = new LocalMusicFragmentPagerAdapter(getSupportFragmentManager(), this, list);
        viewPager = (ViewPager)findViewById(R.id.local_music_viewpager);
        viewPager.setAdapter(pagerAdapter);
        tabLayout = (TabLayout)findViewById(R.id.local_music_sliding_tabs);
        tabLayout.setupWithViewPager(viewPager);
        tabLayout.setTabMode(TabLayout.MODE_FIXED);
    }

    private void initToolbar() {
        Toolbar toolbar = (Toolbar) findViewById(R.id.local_music_toolbar);
        toolbar.setNavigationIcon(R.mipmap.actionbar_back);//设置导航栏图标
        toolbar.inflateMenu(R.menu.local_music_menu);//设置右上角的填充菜单

        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        toolbar.setOnMenuItemClickListener(new Toolbar.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                int menuItemId = item.getItemId();
                if (menuItemId == R.id.local_music_toolbar_search) {
                    //TODO 执行搜索逻辑
                } else if (menuItemId == R.id.local_music_toolbar_more) {
                    showMoreMenu();
                }
                return true;
            }
        });
    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    private void showMoreMenu() {
        View contentView = LayoutInflater.from(LocalMusicActivity.this).inflate(R.layout.local_music_more_menu_layout, null);
        final PopupWindow mPopWindow = new PopupWindow(contentView,
                700, Toolbar.LayoutParams.WRAP_CONTENT, true);
        mPopWindow.setAnimationStyle(R.style.popupWindowAnim);
        if (Build.VERSION.SDK_INT >= 21) {
            mPopWindow.setElevation(50.0f);
        }
        ListView listView = (ListView) contentView.findViewById(R.id.local_music_more_menu_listview);
        listView.setAdapter(new MoreMenuAdapter());
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                if (position == 0) {
                    Intent intent = new Intent(LocalMusicActivity.this, ScanMusicActivity.class);
                    startActivity(intent);
                }
                //解除弹窗
                mPopWindow.dismiss();
            }
        });
        //显示PopupWindow
        View rootview = LayoutInflater.from(LocalMusicActivity.this).inflate(R.layout.local_activity_layout, null);
        mPopWindow.showAtLocation(rootview, Gravity.RIGHT | Gravity.TOP, 0, 0);
    }

    class MoreMenuAdapter extends BaseAdapter {
        View view;
        ImageView img;
        TextView tv;
        int[] imgs = new int[]{R.mipmap.actionbar_menu_icn_scan
                , R.mipmap.actionbar_menu_icn_order_default
                , R.mipmap.actionbar_menu_icn_lrc
                , R.mipmap.actionbar_menu_icn_upquality};
        String[] texts = new String[]{"扫描歌曲", "选择排序方式", "一键获取封面歌词", "升级音质"};

        @Override
        public int getCount() {
            return 4;
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
            view = LayoutInflater.from(LocalMusicActivity.this).inflate(R.layout.local_music_more_menu_item_layout, null);
            img = (ImageView) view.findViewById(R.id.local_music_more_menu_item_icn);
            tv = (TextView) view.findViewById(R.id.local_music_more_menu_item_tv);
            setSource(position);
            return view;
        }

        public void setSource(int position) {
            img.setImageResource(imgs[position]);
            tv.setText(texts[position]);
        }
    }
}

