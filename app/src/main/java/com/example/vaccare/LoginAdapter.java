package com.example.vaccare;

import android.content.Context;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;

public class LoginAdapter extends FragmentPagerAdapter {

    private Context context;
    int totalTabs;

    public LoginAdapter(FragmentManager fm/**, Context context*/, int totalTabs) {
        super(fm);
        //this.context=context;
        this.totalTabs=totalTabs;
    }

    @Override
    public int getCount() {
        return totalTabs;
    }

    public Fragment getItem(int position){
        switch (position){
            case 0:

                Main_Fragment main_fragment = new Main_Fragment();
                return main_fragment;

            case 1:
                LoginTabFragment loginTabFragment = new LoginTabFragment();
                return loginTabFragment;
            default:
                return null;
        }
    }
}

