package com.example.luhui1hao.newmp3player.adapter;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import com.example.luhui1hao.newmp3player.fragment.SingleFragment;
import com.example.luhui1hao.newmp3player.model.Mp3Info;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by luhui on 2016/6/21.
 */
public class LocalMusicFragmentPagerAdapter extends FragmentPagerAdapter {
    final int PAGE_COUNT = 4;
    private String tabTitles[] = new String[]{"单曲","歌手","专辑","文件夹"};
    private Context context;
    private List<Mp3Info> list = new ArrayList<>();

    public LocalMusicFragmentPagerAdapter(FragmentManager fm, Context context, List<Mp3Info> list) {
        super(fm);
        this.context = context;
        this.list = list;
    }

    @Override
    public Fragment getItem(int position) {
        Fragment fragment = null;
        switch (position){
            case 0:
                fragment = new SingleFragment();
                Bundle data = new Bundle();
                data.putSerializable("list", (Serializable) list);
                fragment.setArguments(data);
                break;
            case 1:
                break;
            case 2:
                break;
            case 3:
                break;
        }
        return fragment;
    }

    @Override
    public int getCount() {
        return PAGE_COUNT;
    }

    @Override
    public CharSequence getPageTitle(int position) {
        return tabTitles[position];
    }
}
