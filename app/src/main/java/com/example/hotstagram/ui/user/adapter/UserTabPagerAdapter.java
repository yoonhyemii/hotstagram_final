package com.example.hotstagram.ui.user.adapter;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;

import com.example.hotstagram.ui.user.fragment.PhotoFragment;
import com.example.hotstagram.ui.user.fragment.ProfileFragment;


public class UserTabPagerAdapter extends FragmentPagerAdapter {

    private int tabCount;

    public UserTabPagerAdapter(@NonNull FragmentManager fm, int tabCount) {
        super(fm);
        this.tabCount = tabCount;

    }

    // 프래그먼트 교체를 보여주는 처리 구현
    @NonNull
    @Override
    public Fragment getItem(int position) {
        switch (position){
            case 0:
                ProfileFragment profileFragment = new ProfileFragment();
                return profileFragment;
            case 1:
                PhotoFragment photoFragment = new PhotoFragment();
                return  photoFragment;
            default:
                return null;
        }
    }

    @Override
    public int getCount () { return tabCount; }
}

