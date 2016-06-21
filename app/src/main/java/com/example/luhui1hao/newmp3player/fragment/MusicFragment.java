package com.example.luhui1hao.newmp3player.fragment;

import android.content.Context;
import android.content.Intent;
import android.content.res.TypedArray;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.widget.ListViewAutoScrollHelper;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.example.luhui1hao.newmp3player.R;
import com.example.luhui1hao.newmp3player.activity.LocalMusicActivity;

import java.util.List;

/**
 * Created by luhui on 2016/6/12.
 */
public class MusicFragment extends Fragment {
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.music_layout,container,false);
        ListView listView = (ListView)view.findViewById(R.id.music_listview);
        MusicIcnListAdapter musicIcnListAdapter = new MusicIcnListAdapter(getActivity());
        listView.setAdapter(musicIcnListAdapter);
        listView.setOnItemClickListener(new MusicIcnListItemListener());
        return view;
    }

    class MusicIcnListItemListener implements AdapterView.OnItemClickListener{

        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
            if(position == 0){
                Intent intent = new Intent(MusicFragment.this.getActivity(), LocalMusicActivity.class);
                MusicFragment.this.getActivity().startActivity(intent);
            }
        }
    }

    class MusicIcnListAdapter extends BaseAdapter {
        LayoutInflater inflater;
        ViewHolder holder;
        int[] icns;
        String[] texts;

        MusicIcnListAdapter(Context context) {
            inflater = LayoutInflater.from(context);
        }

        @Override
        public int getCount() {
            return 5;
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
            if (convertView == null) {
                holder = new ViewHolder();
                convertView = inflater.inflate(R.layout.music_icn_list_item_layout, null);
                holder.icn = (ImageView)convertView.findViewById(R.id.music_list_icn);
                holder.tv1 = (TextView)convertView.findViewById(R.id.music_list_text);
                convertView.setTag(holder);
            } else {
                holder = (ViewHolder) convertView.getTag();
            }

            setSource(position);
            return convertView;
        }

        private void setSource(int position) {
            texts = getResources().getStringArray(R.array.music_list_text_arr);
            //获取图片数组资源
            TypedArray typedArray = getResources().obtainTypedArray(R.array.music_list_icn_arr);
            int len = typedArray.length();
            icns = new int[len];
            for(int i = 0; i < len; i++){
                icns[i] = typedArray.getResourceId(i, 0);
            }
            typedArray.recycle();

            holder.icn.setImageResource(icns[position]);
            holder.tv1.setText(texts[position]);
        }
    }

    static class ViewHolder {
        public ImageView icn;
        public TextView tv1;
    }

}
