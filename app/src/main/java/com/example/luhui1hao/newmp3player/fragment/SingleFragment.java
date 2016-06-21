package com.example.luhui1hao.newmp3player.fragment;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.example.luhui1hao.newmp3player.R;
import com.example.luhui1hao.newmp3player.model.Mp3Info;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by luhui on 2016/6/21.
 */
public class SingleFragment extends Fragment {
    List<Mp3Info> list = new ArrayList<>();

    public SingleFragment() {
        Bundle data = getArguments();
        list = (List<Mp3Info>) data.getSerializable("list");
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.single_layout, container, false);
        ListView listView = (ListView) view.findViewById(R.id.local_music_more_menu_listview);
        SingleListViewAdapter adapter = new SingleListViewAdapter(getActivity());
        listView.setAdapter(adapter);
        return view;
    }

    class SingleListViewAdapter extends BaseAdapter {
        Context context;
        LayoutInflater inflater;
        ViewHolder holder;
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
            if(convertView == null){
                holder = new ViewHolder();
                convertView = inflater.inflate(R.layout.single_item_layout, null);
                holder.musicNameTv = (TextView)convertView.findViewById(R.id.single_item_music_name_tv);
                holder.artistTv = (TextView)convertView.findViewById(R.id.single_item_artist_tv);
                convertView.setTag(holder);
            }else {
                holder = (ViewHolder)convertView.getTag();
            }
            inflateContent();
            return convertView;
        }

        private void inflateContent() {
            //TODO 在这里修改内容
        }

        class ViewHolder{
            public TextView musicNameTv;
            public TextView artistTv;
        }
    }
}
