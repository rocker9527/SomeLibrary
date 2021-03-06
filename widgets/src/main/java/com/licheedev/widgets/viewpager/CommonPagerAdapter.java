package com.licheedev.widgets.viewpager;

/*
 *  viewpager普通公用adapter.Yz
 */

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;
import java.util.Arrays;
import java.util.List;

public class CommonPagerAdapter extends FragmentPagerAdapter {
    
    private List<Fragment> fragments;

    public CommonPagerAdapter(FragmentManager fm, List<Fragment> fragments) {
        super(fm);
        this.fragments = fragments;
    }

    public CommonPagerAdapter(FragmentManager fm, Fragment... fragments) {
        super(fm);
        this.fragments = Arrays.asList(fragments);
    }

    @Override
    public Fragment getItem(int position) {
        return fragments.get(position);
    }

    @Override
    public int getCount() {
        return fragments.size();
    }

    @Override
    public int getItemPosition(Object object) {
        return POSITION_NONE;
    }
}
