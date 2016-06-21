package com.example.luhui1hao.newmp3player.activity;

import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.FragmentActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;

import com.example.luhui1hao.newmp3player.R;

/**
 * Created by luhui on 2016/6/12.
 */
public class PlayerActivity extends FragmentActivity {
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.player_layout);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN);
        }
        Log.e("123", "woshi=--------playeractivity");
        initToolBar();
    }

    private void initToolBar() {
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar_player);
        toolbar.setNavigationIcon(R.mipmap.actionbar_back);//设置导航栏图标
        toolbar.inflateMenu(R.menu.player_toolbar_menu);//设置右上角的填充菜单

        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        overridePendingTransition(R.anim.main_enter,R.anim.player_quit);
    }
}
