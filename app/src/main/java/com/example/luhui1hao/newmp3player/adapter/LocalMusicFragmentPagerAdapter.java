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
    Fragment fragment = null;

    public LocalMusicFragmentPagerAdapter(FragmentManager fm, Context context) {
        super(fm);
        this.context = context;
    }

    @Override
    public Fragment getItem(int position) {
        switch (position){
            case 0:
                startSingleFragment();
                break;
            case 1:
                startSingleFragment();
                break;
            case 2:
                startSingleFragment();
                break;
            case 3:
                startSingleFragment();
                break;
        }
        return fragment;
    }

    private void startSingleFragment() {
        fragment = new SingleFragment();
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
