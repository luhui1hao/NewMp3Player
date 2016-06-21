package com.example.luhui1hao.newmp3player.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;

import com.example.luhui1hao.newmp3player.R;

/**
 * Created by luhui on 2016/6/16.
 */
public class ScanMusicActivity extends Activity {
    Button overallScanBtn;
    BtnListener btnListener;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.scan_music_layout);

        initToolbar();
        overallScanBtn = (Button)findViewById(R.id.local_music_scan_btn_red);
        btnListener = new BtnListener();
        overallScanBtn.setOnClickListener(btnListener);
    }

    private void initToolbar() {
        Toolbar toolbar = (Toolbar)findViewById(R.id.scan_music_toolbar);
        toolbar.setNavigationIcon(R.mipmap.actionbar_back);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

    class BtnListener implements View.OnClickListener{
        @Override
        public void onClick(View v) {
            int id = v.getId();
            if(id == R.id.local_music_scan_btn_red){
                Intent intent = new Intent(ScanMusicActivity.this, RunningScanMusicActivity.class);
                startActivity(intent);
                overridePendingTransition(R.anim.push_up, R.anim.fixed_anim);
            }
        }
    }
}
