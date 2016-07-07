package com.example.luhui1hao.newmp3player.activity;

import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.ServiceConnection;
import android.graphics.Color;
import android.media.AudioManager;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.v4.app.FragmentActivity;
import android.support.v7.widget.Toolbar;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.style.AbsoluteSizeSpan;
import android.text.style.ForegroundColorSpan;
import android.util.Log;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import com.example.luhui1hao.newmp3player.R;
import com.example.luhui1hao.newmp3player.model.Mp3Info;
import com.example.luhui1hao.newmp3player.service.PlayerService;
import com.example.luhui1hao.newmp3player.sqlite.MyDatabaseHelper;
import com.example.luhui1hao.newmp3player.utils.MediaUtil;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by luhui on 2016/6/12.
 */
public class PlayerActivity extends FragmentActivity {
    private static final int START = 1;
    private static final int PAUSE = 0;
    private int currentState = START;
    private ImageButton playBtn, preBtn, nextBtn;
    private PlayerService playerService;
    boolean mBound = false;
    private SeekBar seekBar;
    private int currentPosition;
    private int duration;
    private List<Mp3Info> list = new ArrayList<>();
    private int position;
    private CurrentPositionReceiver receiver;
    private PlayerService.MyBinder binder;
    private MyBtnListener listener;
    private MySeekBarListener seekBarListener;
    private TextView currentPositionTv, durationTv;
    private ImageButton playListBtn;
    private TextView titleTv,artistTv;
    private AudioManager audioManager;
    private int maxVolume;
    private int currentVolume;
    private SeekBar soundBar;
    private ImageButton listBtn;
    private MusicListAdapter adapter;

    private ServiceConnection conn = new ServiceConnection() {

        @Override
        public void onServiceConnected(ComponentName className,
                                       IBinder service) {
            binder = (PlayerService.MyBinder) service;
            playerService = binder.getService();
            mBound = true;
            Toast.makeText(PlayerActivity.this, "OKOKOKOK", Toast.LENGTH_SHORT).show();
            //！！！我也不知道为什么有关service的调用的东西都要在这里初始化
            initUIArgumentsSet();

        }

        @Override
        public void onServiceDisconnected(ComponentName arg0) {
            mBound = false;
        }
    };

    /**
     * 更新界面用
     */
    private Handler handler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            if(msg.what == 0x01){
                //更新SeekBar的进度
                Bundle bundle = msg.getData();
                int currentPosition = bundle.getInt("currentPosition", 0);
                seekBar.setProgress(currentPosition);
                //每隔一秒更新CurrentPositionTv
                currentPositionTv.setText(MediaUtil.formatTime((long)currentPosition));
            }
        }
    };


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.player_layout);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN);
        }
        Log.e("123", "woshi=--------playeractivity");

        //这个是为了从数据库中获取到Mp3Info对象
        list = (List<Mp3Info>) getIntent().getSerializableExtra("list");
        position = getIntent().getIntExtra("position", 0);

        initToolBar();
        initFindViewById();
        initEventListener();
        initServiceBinding();
        initBroadCastReceiver();
        initSoundBar();
    }

    /**
     * 初始化音量条
     */
    private void initSoundBar() {
        audioManager = (AudioManager) getSystemService(Context.AUDIO_SERVICE);
        maxVolume = audioManager.getStreamMaxVolume(AudioManager.STREAM_MUSIC);
        //指定音频流的类型
        setVolumeControlStream(AudioManager.STREAM_MUSIC);

        soundBar = (SeekBar)findViewById(R.id.progress_volume);
        //绑定监听器
        MySoundBarListener mySoundBarListener = new MySoundBarListener();
        soundBar.setOnSeekBarChangeListener(mySoundBarListener);
    }

    private void initFindViewById() {
        titleTv = (TextView)findViewById(R.id.titleTv);
        artistTv = (TextView)findViewById(R.id.artistTv);
    }

    private void initUIArgumentsSet() {
        //获取各种界面信息

        seekBar = (SeekBar)findViewById(R.id.seekbar_progress_player);
        seekBarListener = new MySeekBarListener();
        seekBar.setOnSeekBarChangeListener(seekBarListener);
        currentPositionTv = (TextView)findViewById(R.id.currentPositionTv);
        durationTv = (TextView)findViewById(R.id.durationTv);

        duration = playerService.getDuration();
        //设置SeekBar的最大值
        seekBar.setMax(duration);
        //设置TV歌曲的长度
        durationTv.setText(MediaUtil.formatTime((long)duration));

        updateTitleAndArtist();
//        MyDatabaseHelper helper = MyDatabaseHelper.getInstance(this);
//        Mp3Info mp3Info = list.get(position);
//        duration = (int)mp3Info.getDuration();
//        Log.e("DURATION", duration+"");
//        seekBar.setMax(duration);
    }

    private void updateTitleAndArtist() {
        Mp3Info mp3Info = list.get(position);
        //显示歌曲名
        titleTv.setText(mp3Info.getTitle());
        //显示作者
        artistTv.setText(mp3Info.getArtist());
    }

    private void initBroadCastReceiver() {
        //注册广播接收器，接收SeekBar进度改变信息
        IntentFilter filter = new IntentFilter("com.example.luhui1hao.CURRENT_POSITION");
        receiver = new CurrentPositionReceiver();
        registerReceiver(receiver,filter);
    }

    class CurrentPositionReceiver extends BroadcastReceiver{
        @Override
        public void onReceive(Context context, Intent intent) {
            // 接收SeekBar进度广播
            Message msg = handler.obtainMessage();
            msg.what = 0x01;
            Bundle data = new Bundle();
            data.putInt("currentPosition", intent.getIntExtra("currentPosition", 0));
            msg.setData(data);
            handler.sendMessage(msg);
        }
    }

   /* private void startSeekBarUpdateThread() {
        while(currentState == START) {
            handler.postAtTime(new Runnable() {
                @Override
                public void run() {
                    int currentPosition = playerService.getCurrentPosition();
                    int duration = playerService.getDuration();
                    int currentPercentage = currentPosition / duration;
                    Message msg = handler.obtainMessage();
                    msg.what = 0x01;
                    Bundle data = new Bundle();
                    data.putInt("currentPecentage", currentPercentage);
                    msg.setData(data);
                    handler.sendMessage(msg);
                }
            }, 1000);
        }
    }*/

    private void initServiceBinding() {
        Intent intent = new Intent(this, PlayerService.class);
        bindService(intent, conn, Context.BIND_AUTO_CREATE);
    }

    private void initEventListener() {
        playBtn = (ImageButton)findViewById(R.id.play_play_btn);
        preBtn = (ImageButton)findViewById(R.id.play_prev_btn);
        nextBtn = (ImageButton)findViewById(R.id.play_next_btn);
        listBtn = (ImageButton)findViewById(R.id.play_list_btn);

        listener = new MyBtnListener();
        playBtn.setOnClickListener(listener);
        preBtn.setOnClickListener(listener);
        nextBtn.setOnClickListener(listener);
        listBtn.setOnClickListener(listener);
    }

    class MySeekBarListener implements SeekBar.OnSeekBarChangeListener{
        int progress;
        @Override
        public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
            this.progress = progress;
        }

        @Override
        public void onStartTrackingTouch(SeekBar seekBar) {

        }

        @Override
        public void onStopTrackingTouch(SeekBar seekBar) {
            // 改变歌曲的进度
            playerService.seekTo(progress);
            //更新CurrentPositionTv
            currentPositionTv.setText(MediaUtil.formatTime((long)progress));
        }
    }

    class MySoundBarListener implements SeekBar.OnSeekBarChangeListener{

        @Override
        public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
            audioManager.setStreamVolume(AudioManager.STREAM_MUSIC, progress, 0);
        }

        @Override
        public void onStartTrackingTouch(SeekBar seekBar) {

        }

        @Override
        public void onStopTrackingTouch(SeekBar seekBar) {

        }
    }

    class MyBtnListener implements View.OnClickListener
    {
        @Override
        public void onClick(View v) {
            int id = v.getId();
            switch(id){
                case R.id.play_play_btn:
                    sendPlayBtnBroadcast();
                    break;
                case R.id.play_prev_btn:
                    sendPreBtnBroadcast();
                    //TODO 有点矬
                    if (position == 0) {
                        position = list.size() - 1;
                    } else {
                        position--;
                    }
                    updateCurrentUIInfo();
                    updateTitleAndArtist();
                    break;
                case R.id.play_next_btn:
                    sendNextBtnBroadcast();
                    //TODO 更新position，最好想个好一点的方法，感觉既绑定Service又调用数据库矬死了
                    if (position == list.size() - 1) {
                        position = 0;
                    } else {
                        position++;
                    }
                    updateCurrentUIInfo();
                    updateTitleAndArtist();
                    break;
                case R.id.play_list_btn:
                    //TODO 弹出播放列表弹窗
                    showList();
                    break;
            }
        }

        private void updateCurrentUIInfo() {
            Mp3Info mp3Info = list.get(position);
            //更新歌曲长度信息
            durationTv.setText(MediaUtil.formatTime(mp3Info.getDuration()));
            Toast.makeText(PlayerActivity.this, mp3Info.getDuration()+"", Toast.LENGTH_SHORT).show();
            //更新SeekBar最大值
            seekBar.setMax((int)mp3Info.getDuration());
        }

        private void sendNextBtnBroadcast() {
            //发送“下一首”广播
            Intent xiayishouBroadcastIntent = new Intent("com.example.luhui1hao.NEXT");
            sendBroadcast(xiayishouBroadcastIntent);
            //将图标替换成暂停
            playBtn.setImageResource(R.drawable.dra_play_btn_pause);
            currentState = START;
        }

        private void sendPreBtnBroadcast() {
            //发送“上一首”广播
            Intent shangyishouBroadcastIntent = new Intent("com.example.luhui1hao.PREVIOUS");
            sendBroadcast(shangyishouBroadcastIntent);
            //将图标替换成暂停
            playBtn.setImageResource(R.drawable.dra_play_btn_pause);
            currentState = START;
        }

        private void sendPlayBtnBroadcast() {
            //发送播放或暂停广播
            if (currentState == START) {
                Intent startBroadcastIntent = new Intent("com.example.luhui1hao.PAUSE");
                sendBroadcast(startBroadcastIntent);
                //将图标替换成播放
                playBtn.setImageResource(R.drawable.dra_play_btn_play);
                currentState = PAUSE;
            } else if (currentState == PAUSE) {
                Intent pauseBroadcastIntent = new Intent("com.example.luhui1hao.START");
                sendBroadcast(pauseBroadcastIntent);
                //将图标替换成暂停
                playBtn.setImageResource(R.drawable.dra_play_btn_pause);
                currentState = START;
            }
        }
    }

    private void showList() {
        View contentView = LayoutInflater.from(this).inflate(R.layout.player_list_layout, null);
        final PopupWindow mPopWindow = new PopupWindow(contentView,
                Toolbar.LayoutParams.MATCH_PARENT, 1400, true);
        mPopWindow.setAnimationStyle(R.style.listPopupWindowAnim);
        ListView listView = (ListView) contentView.findViewById(R.id.player_list_lv);
        adapter = new MusicListAdapter();
        listView.setAdapter(adapter);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                PlayerActivity.this.position = position;
                //TODO 播放相应音乐
                Intent intent = new Intent("com.example.luhui1hao.RANDOM");
                intent.putExtra("listPosition", position);
                sendBroadcast(intent);
                updateUI();
                //解除弹窗
//                mPopWindow.dismiss();
            }
        });
        //显示PopupWindow
        View rootview = LayoutInflater.from(PlayerActivity.this).inflate(R.layout.player_layout, null);
        mPopWindow.showAtLocation(rootview, Gravity.BOTTOM, 0, 0);
    }

    private void updateUI() {
        adapter.notifyDataSetChanged();
        updateTitleAndArtist();
        //将图标替换成暂停
        playBtn.setImageResource(R.drawable.dra_play_btn_pause);

        //TODO 这里要改改
        duration = (int)MyDatabaseHelper.getInstance(PlayerActivity.this).getMp3Infos().get(position).getDuration();
        //设置SeekBar的最大值
        seekBar.setMax(duration);
        //设置TV歌曲的长度
        durationTv.setText(MediaUtil.formatTime((long)duration));
    }

    class MusicListAdapter extends BaseAdapter{
        private ImageView icnPlaying;
        private TextView playlistTv;
        private ImageButton linkBtn,deleteBtn;
        private ListBtnListener listener;
        private int listPosition;
        @Override
        public int getCount() {
            return list.size();
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
            listPosition = position;
            View view = LayoutInflater.from(PlayerActivity.this).inflate(R.layout.player_list_item_layout,parent,false);
            icnPlaying = (ImageView)view.findViewById(R.id.player_list_icn_playing);
            playlistTv = (TextView)view.findViewById(R.id.play_playlist_tv);
            linkBtn = (ImageButton)view.findViewById(R.id.play_playlist_link_btn);
            deleteBtn = (ImageButton)view.findViewById(R.id.play_playlist_delete_btn);
            listener = new ListBtnListener();
            linkBtn.setOnClickListener(listener);
            deleteBtn.setOnClickListener(listener);

            //填充内容
            setContent();
            return view;
        }

        private void setContent() {
            Mp3Info mp3Info = list.get(listPosition);
            String title = mp3Info.getTitle();
            String author = mp3Info.getArtist();
            // 如果正在播放，就将喇叭显示出来，并且把字体变红
            if(listPosition != position){
                SpannableString spanString = new SpannableString(title + " - " + author);
                AbsoluteSizeSpan span = new AbsoluteSizeSpan(15, true);
                spanString.setSpan(span, 0, title.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
                AbsoluteSizeSpan spanAuthor = new AbsoluteSizeSpan(11,true);
                spanString.setSpan(spanAuthor, title.length() + 1, spanString.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
                ForegroundColorSpan spanAuthorColor = new ForegroundColorSpan(getResources().getColor(R.color.author));
                spanString.setSpan(spanAuthorColor, title.length() + 1, spanString.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
                playlistTv.setText(spanString);
            }else if(listPosition == position){
                icnPlaying.setVisibility(View.VISIBLE);
                linkBtn.setVisibility(View.VISIBLE);
                SpannableString spanString = new SpannableString(title + " - " + author);
                AbsoluteSizeSpan span = new AbsoluteSizeSpan(15, true);
                spanString.setSpan(span, 0, title.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
                AbsoluteSizeSpan spanAuthor = new AbsoluteSizeSpan(11,true);
                spanString.setSpan(spanAuthor, title.length() + 1, spanString.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
                ForegroundColorSpan spanAuthorColor = new ForegroundColorSpan(getResources().getColor(R.color.wangyiyun_red));
                spanString.setSpan(spanAuthorColor, 0, spanString.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
                playlistTv.setText(spanString);
            }
        }

        class ListBtnListener implements View.OnClickListener{

            @Override
            public void onClick(View v) {
                Toast.makeText(PlayerActivity.this, "点击了", Toast.LENGTH_SHORT).show();
            }
        }
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

    @Override
    protected void onStart() {
        super.onStart();
        currentVolume = audioManager.getStreamVolume(AudioManager.STREAM_MUSIC);
        soundBar.setProgress(currentVolume);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        unbindService(conn);
        unregisterReceiver(receiver);
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event){
        if(keyCode == KeyEvent.KEYCODE_VOLUME_DOWN){
            currentVolume = audioManager.getStreamVolume(AudioManager.STREAM_MUSIC);
            if(currentVolume > 0){
                currentVolume--;
            }
            soundBar.setProgress(currentVolume);
            //设置音量
            audioManager.setStreamVolume(AudioManager.STREAM_MUSIC, currentVolume, 0);
//            Toast.makeText(this, "Down", Toast.LENGTH_SHORT).show();
            return true;
        }else if(keyCode == KeyEvent.KEYCODE_VOLUME_UP){
            currentVolume = audioManager.getStreamVolume(AudioManager.STREAM_MUSIC);
            if(currentVolume < maxVolume){
                currentVolume++;
            }
            soundBar.setProgress(currentVolume);
            //设置音量
            audioManager.setStreamVolume(AudioManager.STREAM_MUSIC, currentVolume, 0);
//            Toast.makeText(this, "Up", Toast.LENGTH_SHORT).show();
            return true;
        }else return super.onKeyDown(keyCode, event);
    }
}

