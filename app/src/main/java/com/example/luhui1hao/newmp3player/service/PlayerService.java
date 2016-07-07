package com.example.luhui1hao.newmp3player.service;

import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.media.MediaPlayer;
import android.os.Binder;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.util.Log;
import android.widget.Toast;


import com.example.luhui1hao.newmp3player.model.Mp3Info;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

/**
 * Created by luhui1hao on 2015/12/16.
 */
public class PlayerService extends Service {
    public static final String TAG = "PlayerService";
    private List<Mp3Info> mp3Infos = new ArrayList<>();
    private int position;
    private Mp3Info mp3Info;
    private Mp3Info currentMp3Info = new Mp3Info();
    private MediaPlayer mediaPlayer;
    private StartReceiver startReceiver;
    private PauseReceiver pauseReceiver;
    private PreviousReceiver previousReceiver;
    private NextReceiver nextReceiver;
    private RandomReceiver randomReceiver;
    private final IBinder mBinder = new MyBinder();
    private Timer timer;
    private TimerTask task;

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return mBinder;
    }

    public class MyBinder extends Binder {
        //我也不知道为什么会出问题
        public PlayerService getService() {
            return PlayerService.this;
        }
    }

    /**
     * 改变歌曲播放进度，由PlayerActivty调用
     *
     * @param msec
     */
    public void seekTo(int msec) {
        mediaPlayer.seekTo(msec);
    }

    /**
     * 获取当前当前歌曲的时间进度，由PlayerActivity调用
     *
     * @return
     */
    public int getCurrentPosition() {
        return mediaPlayer.getCurrentPosition();
    }

    /**
     * 获取当前歌曲的总时长
     */
    public int getDuration() {
        return mediaPlayer.getDuration();
    }

    @Override
    public void onCreate() {
        super.onCreate();
        initMediaPlayer();
        //绑定播放广播接收器
        IntentFilter startFilter = new IntentFilter();
        startFilter.addAction("com.example.luhui1hao.START");
        startReceiver = new StartReceiver();
        registerReceiver(startReceiver, startFilter);
        //绑定暂停广播接收器
        IntentFilter pauseFilter = new IntentFilter();
        pauseFilter.addAction("com.example.luhui1hao.PAUSE");
        pauseReceiver = new PauseReceiver();
        registerReceiver(pauseReceiver, pauseFilter);
        //绑定上一首广播接收器
        IntentFilter previousFilter = new IntentFilter();
        previousFilter.addAction("com.example.luhui1hao.PREVIOUS");
        previousReceiver = new PreviousReceiver();
        registerReceiver(previousReceiver, previousFilter);
        //绑定下一首广播接收器
        IntentFilter nextFilter = new IntentFilter();
        nextFilter.addAction("com.example.luhui1hao.NEXT");
        nextReceiver = new NextReceiver();
        registerReceiver(nextReceiver, nextFilter);
        //绑定任意一首广播接收器
        IntentFilter randomFilter = new IntentFilter();
        randomFilter.addAction("com.example.luhui1hao.RANDOM");
        randomReceiver = new RandomReceiver();
        registerReceiver(randomReceiver, randomFilter);
        Log.e(TAG, "onCreate has run");

    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        //获取Mp3Info对象
        mp3Infos = (List<Mp3Info>) intent.getSerializableExtra("mp3Infos");
        //获取position对象
        position = intent.getIntExtra("position", 0);
        //得到传入的的mp3Info对象
        mp3Info = mp3Infos.get(position);
        //设置MediaPlayer
        setMediaPlayer(mp3Infos, position);
        return super.onStartCommand(intent, flags, startId);
    }

    private void startTimer() {
        timer = new Timer();
        task = new TimerTask() {
            @Override
            public void run() {
                Intent intent = new Intent("com.example.luhui1hao.CURRENT_POSITION");
                intent.putExtra("currentPosition", getCurrentPosition());
                sendBroadcast(intent);
                Log.e("TimerTask", getCurrentPosition() + "");
            }
        };
        timer.schedule(task, 0, 1000);
    }

    private void initMediaPlayer() {
        // 创建MediaPlayer
        mediaPlayer = new MediaPlayer();
        mediaPlayer.reset();
    }

    private void setMediaPlayer(List<Mp3Info> mp3Infos, int position) {
        //如果mp3的名字一致，则维持原状
        //否则，停止原来的歌，播放现在的歌
        Log.e(TAG, mp3Info + "\n" + currentMp3Info);
        if (mp3Info.getDisplayName().equals(currentMp3Info.getDisplayName())) {

        } else {
            mediaPlayer.stop();
            mediaPlayer.reset();
            try {
                File file = new File(mp3Info.getUrl());
                if (file.exists()) {
                    mediaPlayer.setDataSource(file.getPath());
                    mediaPlayer.prepare();
                    mediaPlayer.start();
                    //开启定时器任务
                    startTimer();
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
            //获得当前的Mp3Info对象
            currentMp3Info = mp3Infos.get(position);
        }
    }

    class StartReceiver extends BroadcastReceiver {

        @Override
        public void onReceive(Context context, Intent intent) {
            if (!mediaPlayer.isPlaying()) {
                mediaPlayer.start();
                //开启定时器任务
                timer.cancel();
                startTimer();
            }
        }
    }

    class PauseReceiver extends BroadcastReceiver {

        @Override
        public void onReceive(Context context, Intent intent) {
            if (mediaPlayer.isPlaying()) {
                mediaPlayer.pause();
                //取消定时器任务
                timer.cancel();
            }
        }
    }

    class PreviousReceiver extends BroadcastReceiver {

        @Override
        public void onReceive(Context context, Intent intent) {
            //先释放掉当前的歌曲
            mediaPlayer.stop();
            mediaPlayer.reset();
            if (position == 0) {
                position = mp3Infos.size() - 1;
            } else {
                position--;
            }
            //获得当前的Mp3Info对象
            currentMp3Info = mp3Infos.get(position);
            try {
                File file = new File(mp3Infos.get(position).getUrl());
                if (file.exists()) {
                    mediaPlayer.setDataSource(file.getPath());
                    mediaPlayer.prepare();
                    mediaPlayer.start();
                    //开启定时器任务
                    timer.cancel();
                    startTimer();
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    class NextReceiver extends BroadcastReceiver {

        @Override
        public void onReceive(Context context, Intent intent) {
            //先释放掉当前的歌曲
            mediaPlayer.stop();
            mediaPlayer.reset();
            if (position == mp3Infos.size() - 1) {
                position = 0;
            } else {
                position++;
            }
            //获得当前的Mp3Info对象
            currentMp3Info = mp3Infos.get(position);
            try {
                File file = new File(mp3Infos.get(position).getUrl());
                if (file.exists()) {
                    mediaPlayer.setDataSource(file.getPath());
                    mediaPlayer.prepare();
                    mediaPlayer.start();
                    //开启定时器任务
                    timer.cancel();
                    startTimer();
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    class RandomReceiver extends BroadcastReceiver{

        @Override
        public void onReceive(Context context, Intent intent) {
            int listPosition = intent.getIntExtra("listPosition", 0);
            //如果是当前歌曲，则不进行任何操作
            if(listPosition == position){

            }else if(listPosition != position){
                position = listPosition;
                //先释放掉当前的歌曲
                mediaPlayer.stop();
                mediaPlayer.reset();
                //获得当前的Mp3Info对象
                currentMp3Info = mp3Infos.get(position);
                try {
                    File file = new File(mp3Infos.get(position).getUrl());
                    if (file.exists()) {
                        mediaPlayer.setDataSource(file.getPath());
                        mediaPlayer.prepare();
                        mediaPlayer.start();
                        //开启定时器任务
                        timer.cancel();
                        startTimer();
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        unregisterReceiver(startReceiver);
        unregisterReceiver(pauseReceiver);
        unregisterReceiver(previousReceiver);
        unregisterReceiver(nextReceiver);
        mediaPlayer.stop();
        mediaPlayer.release();
    }
}
