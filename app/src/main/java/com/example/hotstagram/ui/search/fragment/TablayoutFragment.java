package com.example.hotstagram.ui.search.fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.viewpager.widget.ViewPager;

import com.example.hotstagram.R;
import com.example.hotstagram.ui.search.adapter.TabPagerAdapter;
import com.google.android.material.tabs.TabLayout;

public class TablayoutFragment extends Fragment {

    TabLayout tabLayout;
    ViewPager viewPager;
    FragmentManager fragmentManager;

    public View onCreateView(@NonNull final LayoutInflater inflater,
                             final ViewGroup container, Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_search_tablayout, container, false);

        tabLayout = root.findViewById(R.id.tabs);
        fragmentManager = getChildFragmentManager();
        viewPager = root.findViewById(R.id.search_view_pager);
        TabPagerAdapter tabPagerAdapter = new TabPagerAdapter(fragmentManager,tabLayout.getTabCount());

        viewPager.setAdapter(tabPagerAdapter);
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