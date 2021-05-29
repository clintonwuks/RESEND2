package com.example.resend;

import android.content.Context;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentStatePagerAdapter;

class MyAdapter extends FragmentStatePagerAdapter {
    Context context;
    int totalTabs;
    public MyAdapter(Context c, FragmentManager fm, int totalTabs) {
        super(fm);
        context = c;
        this.totalTabs = totalTabs;
    }
    @NonNull
    @Override
    public Fragment getItem(int position) {
        switch (position) {
            case 0:
                return new Account();
            case 1:
                return new Transactions();
            case 2:
                return new Friends();
            default:
                return null;
        }
    }
    @Override
    public int getCount() {
        return totalTabs;
    }


//    @Override
//    public Fragment createFragment(int position) {
//        return null;
//    }
//
//    @Override
//    public int getItemCount() {
//        return 0;
//    }
}