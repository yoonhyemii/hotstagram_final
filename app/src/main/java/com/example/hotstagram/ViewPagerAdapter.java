package com.example.hotstagram;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;

import com.example.hotstagram.ui.home.CameraFragment;
import com.example.hotstagram.ui.home.HomeFragment;

public class ViewPagerAdapter extends FragmentPagerAdapter {

    private static int count = 2;

    public ViewPagerAdapter(@NonNull FragmentManager fm) {
        super(fm);

    }

    @NonNull
    @Override
    public Fragment getItem(int position) {
        switch (position){
            case 0:
                HomeFragment homeFragment = new HomeFragment();
                return homeFragment;
            case 1:
                CameraFragment cameraFragment = new CameraFragment();
                return cameraFragment;
            default:
                return null;
        }
    }

    @Override
    public int getCount() {
        return count;
    }
}
