package com.example.luhui1hao.newmp3player.adapter;

import android.content.Context;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;

import com.example.luhui1hao.newmp3player.R;
import com.example.luhui1hao.newmp3player.fragment.MusicFragment;
import com.example.luhui1hao.newmp3player.fragment.TestFragment;

/**
 * Created by luhui on 2016/6/11.
 */
public class SimpleFragmentPagerAdapter extends FragmentPagerAdapter {
    final int PAGE_COUNT = 3;
    private Context context;
    private int[] imageResId = {
            R.drawable.actionbar_discover,
            R.drawable.actionbar_music,
            R.drawable.actionbar_friends
    };

    public SimpleFragmentPagerAdapter(FragmentManager fm, Context context) {
        super(fm);
        this.context = context;
    }

    @Override
    public Fragment getItem(int position) {
        if(position == 1){
            return new MusicFragment();
        }
        return TestFragment.newInstance(position + 1);
    }

    @Override
    public int getCount() {
        return PAGE_COUNT;
    }

    @Override
    public CharSequence getPageTitle(int position) {
        return null;
    }

    public View getTabView(int position){
        View view = LayoutInflater.from(context).inflate(R.layout.custom_tablayout_item, null);
        ImageView img = (ImageView) view.findViewById(R.id.iv);
        img.setImageResource(imageResId[position]);
        return view;
    }
}
