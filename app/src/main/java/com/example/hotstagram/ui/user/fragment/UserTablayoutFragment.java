package com.example.hotstagram.ui.user.fragment;

import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.viewpager.widget.ViewPager;

import com.example.hotstagram.R;
import com.example.hotstagram.ui.user.adapter.UserTabPagerAdapter;
import com.google.android.material.tabs.TabItem;
import com.google.android.material.tabs.TabLayout;

public class UserTablayoutFragment extends Fragment {

    TabLayout tabLayout;
    ViewPager viewPager;
    FragmentManager fragmentManager;

    public View onCreateView(@NonNull final LayoutInflater inflater,
                             final ViewGroup container, Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_user_tablayout, container, false);

        tabLayout = root.findViewById(R.id.tabs);

        tabLayout.setSelectedTabIndicatorColor(Color.parseColor("#B66FDE"));
        fragmentManager = getChildFragmentManager();

        viewPager = root.findViewById(R.id.view_pager);
        UserTabPagerAdapter userTabPagerAdapter = new UserTabPagerAdapter(fragmentManager,tabLayout.getTabCount());

        viewPager.setAdapter(userTabPagerAdapter);
        viewPager.addOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(tabLayout));

        tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                viewPager.setCurrentItem(tab.getPosition());
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });

        return root;
    }
}