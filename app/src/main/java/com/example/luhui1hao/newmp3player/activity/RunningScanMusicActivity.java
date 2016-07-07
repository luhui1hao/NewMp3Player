package com.example.luhui1hao.newmp3player.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.transition.Scene;
import android.transition.Transition;
import android.transition.TransitionManager;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.luhui1hao.newmp3player.R;
import com.example.luhui1hao.newmp3player.model.Mp3Info;
import com.example.luhui1hao.newmp3player.sqlite.MyDatabaseHelper;
import com.example.luhui1hao.newmp3player.utils.MediaUtil;

import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

/**
 * Created by luhui on 2016/6/18.
 */
public class RunningScanMusicActivity extends Activity {
    ImageView img, scanDownImg;
    TextView pathTv;
    List<Mp3Info> mp3List = new ArrayList<>();
    private Scene current;
    private Scene another;
    private Transition mytransition;
    private ViewGroup container;
    ButtonListener buttonListener;
    TextView countTv;
    public Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            //处理tv的更新
            if (msg.what == 0x01) {
                //处理tv更新
                String path = msg.getData().getString("path");
                pathTv.setText(path);
            }
            //切换布局
            else if(msg.what == 0x02){
                Scene tem = current;
                current = another;
                another = tem;
                TransitionManager.go(current);
                //为新布局绑定监听器
                initializeAnotherSceneListener();
                countTv = (TextView)findViewById(R.id.running_scan_tv);
                countTv.setText("共扫描到"+ mp3List.size() +"首歌曲");
            }
        }
    };


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.running_scan_music_layout);

        enterScene();
        //从MediaProvider获取歌曲列表
        mp3List = getMusicFromContentProvider();
        //将获取到的歌曲信息插入到数据库
        clearAndUpdateDataBase();

        initializeAnim();
        initializeCurrentSceneListener();

        pathTv = (TextView) findViewById(R.id.running_scan_pathing_tv);

        //等待3秒然后更改布局
        new Timer().schedule(new TimerTask() {
            @Override
            public void run() {
                //发送Message切换布局
                Message msg = handler.obtainMessage();
                msg.what = 0x02;
                handler.sendMessage(msg);
            }
        }, 3000);
    }

    private void clearAndUpdateDataBase() {
        MyDatabaseHelper dbHelper = MyDatabaseHelper.getInstance(this);
        //清空数据库
        dbHelper.clear();
        //插入一组数据
        dbHelper.insertMp3Infos(mp3List);
    }

    private void enterScene() {
        container = (ViewGroup)findViewById(R.id.running_scan_scene_base);
        current = Scene.getSceneForLayout(container, R.layout.running_scan_music_layout, this);
        another = Scene.getSceneForLayout(container, R.layout.running_scan_music_ok_layout, this);

        current.enter();
    }

    private void initializeCurrentSceneListener() {
        buttonListener = new ButtonListener();

        ImageButton quitBtn = (ImageButton) findViewById(R.id.running_scan_back_btn);
        quitBtn.setOnClickListener(buttonListener);
    }

    private void initializeAnotherSceneListener(){
        ImageButton quitBtn = (ImageButton) findViewById(R.id.running_scan_back_btn);
        Button backLocalMusicBtn = (Button)findViewById(R.id.running_scan_back_music_btn);
        quitBtn.setOnClickListener(buttonListener);
        backLocalMusicBtn.setOnClickListener(buttonListener);
    }

    /**
     * 用于初始化一开始的动画
     */
    private void initializeAnim() {
        img = (ImageView) findViewById(R.id.running_scan_icn);
        scanDownImg = (ImageView) findViewById(R.id.scan_down_img);

        Animation anim = AnimationUtils.loadAnimation(this, R.anim.scan_icn_anim);
        img.startAnimation(anim);
        Animation scanDownAnim = AnimationUtils.loadAnimation(this, R.anim.scan_down_anim);
        scanDownImg.startAnimation(scanDownAnim);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
        overridePendingTransition(R.anim.fixed_anim, R.anim.push_down);
    }

    public List<Mp3Info> getMusicFromContentProvider() {
        return MediaUtil.getMp3Infos(this);
    }

    class ButtonListener implements View.OnClickListener {
        @Override
        public void onClick(View v) {
            int id = v.getId();
            if (id == R.id.running_scan_back_btn) {
                onBackPressed();
            }else if(id == R.id.running_scan_back_music_btn){
                Intent intent = new Intent(RunningScanMusicActivity.this, LocalMusicActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent);
            }
        }
    }
}
