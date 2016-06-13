package com.example.luhui1hao.newmp3player.activity;

import android.os.Bundle;
import android.os.PersistableBundle;
import android.support.annotation.Nullable;
import android.support.v4.app.FragmentActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;

import com.example.luhui1hao.newmp3player.R;

/**
 * Created by luhui on 2016/6/12.
 */
public class PlayerActivity extends FragmentActivity {
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.player_layout);
        Log.e("123","woshi=--------playeractivity");
        initToolBar();
    }

    private void initToolBar() {
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar_player);
        toolbar.setNavigationIcon(R.mipmap.login_ban_back);//设置导航栏图标
        toolbar.setTitle("遇见");//设置主标题
        toolbar.setSubtitle("孙燕姿");//设置子标题
        toolbar.inflateMenu(R.menu.player_toolbar_menu);//设置右上角的填充菜单
    }
}
