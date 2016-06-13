package com.example.luhui1hao.newmp3player.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.example.luhui1hao.newmp3player.R;
import com.example.luhui1hao.newmp3player.activity.PlayerActivity;

/**
 * Created by luhui on 2016/6/12.
 */
public class MusicFragment extends Fragment {
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.music_layout,container,false);
        view.findViewById(R.id.btn1).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(MusicFragment.this.getActivity(),"进入播放界面",Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(MusicFragment.this.getActivity(), PlayerActivity.class);
                startActivity(intent);
            }
        });
        return view;
    }
}
