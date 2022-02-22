package com.example.hotstagram.ui.search.adapter;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;

import com.example.hotstagram.ui.search.fragment.AccountFragment;
import com.example.hotstagram.ui.search.fragment.HotFragment;
import com.example.hotstagram.ui.search.fragment.PlaceFragment;
import com.example.hotstagram.ui.search.fragment.TagFragment;

import java.util.ArrayList;

public class TabPagerAdapter extends FragmentPagerAdapter {

    int tabCount;

    public TabPagerAdapter(@NonNull FragmentManager fm, int tabCount) {
        super(fm);
        this.tabCount = tabCount;

    }

    @NonNull
    @Override
    public Fragment getItem(int position) {
        switch (position){
            case 0:
                HotFragment hotFragment = new HotFragment();
                return hotFragment;
            case 1:
                AccountFragment accountFragment = new AccountFragment();
                return accountFragment;
            case 2:
                TagFragment tagFragment = new TagFragment();
                return tagFragment;
            case 3:
                PlaceFragment placeFragment = new PlaceFragment();
                return placeFragment;
            default:
                return null;
        }
    }

    @Override
    public int getCount() {
        return tabCount;
    }
}
