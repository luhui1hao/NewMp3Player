package com.example.luhui1hao.newmp3player.fragment;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.luhui1hao.newmp3player.R;
import com.example.luhui1hao.newmp3player.activity.PlayerActivity;
import com.example.luhui1hao.newmp3player.model.Mp3Info;
import com.example.luhui1hao.newmp3player.service.PlayerService;
import com.example.luhui1hao.newmp3player.sqlite.MyDatabaseHelper;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by luhui on 2016/6/21.
 */
public class SingleFragment extends Fragment {
    List<Mp3Info> list = new ArrayList<>();
    SingleListViewAdapter adapter;
    Context mContext;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mContext = getActivity();
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view;
        //更新list
        list.clear();
        list = obtainList();
        //根据是否有歌曲判断加载哪种布局
        if (list.size() == 0) {
            view = inflater.inflate(R.layout.single_none_layout, container, false);
        } else {
            view = inflater.inflate(R.layout.single_layout, container, false);
            ListView listView = (ListView) view.findViewById(R.id.single_listview);
            adapter = new SingleListViewAdapter(getActivity());
            listView.setAdapter(adapter);
            adapter.notifyDataSetChanged();
            listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                    //启动PlayerService
                    Intent psIntent = new Intent(mContext, PlayerService.class);
                    psIntent.putExtra("mp3Infos", (Serializable)list);
                    psIntent.putExtra("position", position);
                    mContext.startService(psIntent);
                    //启动PlayerActivity
                    Intent intent = new Intent(mContext, PlayerActivity.class);
                    intent.putExtra("list", (Serializable)list);
                    intent.putExtra("position", position);
                    startActivity(intent);
                    ((Activity)mContext).overridePendingTransition(R.anim.player_enter,R.anim.main_quit);
                }
            });
        }
        return view;
    }

    private List<Mp3Info> obtainList() {
        //从数据库中取出list
        return MyDatabaseHelper.getInstance(getActivity()).getMp3Infos();
    }

    class SingleListViewAdapter extends BaseAdapter {
        Context context;
        LayoutInflater inflater;
        Mp3Info mp3Info;
        ViewHolder holder;
        ImageBtnListener listener;

        SingleListViewAdapter(Context context) {
            this.context = context;
            inflater = LayoutInflater.from(context);
        }

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
            mp3Info = list.get(position);
            if(convertView == null){
                holder = new ViewHolder();
                convertView = inflater.inflate(R.layout.single_item_layout, null);
                holder.musicNameTv = (TextView)convertView.findViewById(R.id.single_item_music_name_tv);
                holder.artistTv = (TextView)convertView.findViewById(R.id.single_item_artist_tv);
                holder.imgBtn = (ImageButton)convertView.findViewById(R.id.single_item_more_btn);
                convertView.setTag(holder);
            }else {
                holder = (ViewHolder)convertView.getTag();
            }
            inflateContent();
            return convertView;
        }

        private void inflateContent() {
            //在这里修改内容
            holder.musicNameTv.setText(mp3Info.getDisplayName());
            holder.artistTv.setText(mp3Info.getArtist());
            listener = new ImageBtnListener();
            holder.imgBtn.setOnClickListener(listener);
        }

        class ViewHolder{
            public TextView musicNameTv;
            public TextView artistTv;
            public ImageButton imgBtn;
        }

        class ImageBtnListener implements View.OnClickListener{
            @Override
            public void onClick(View v) {
                Toast.makeText(mContext, "点击了", Toast.LENGTH_SHORT).show();
            }
        }
    }
}
