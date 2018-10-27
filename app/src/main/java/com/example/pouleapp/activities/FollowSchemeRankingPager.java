package com.example.pouleapp.activities;


import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

/**
 * Created by gezamenlijk on 16-8-2017.
 * this class is used to support the 2 tabs in FollowSchemeRankingActivity
 */

//Extending FragmentStatePagerAdapter
class FollowSchemeRankingPager extends FragmentStatePagerAdapter {

    //integer to count number of tabs
    private int mTabCount;

    //Constructor to the class
    FollowSchemeRankingPager(FragmentManager fm, int tabCount) {
        super(fm);
        //Initializing tab count
        this.mTabCount = tabCount;
    }

    //Overriding method getItem
    @Override
    public Fragment getItem(int position) {
        //Returning the current tabs
        switch (position) {
            case 0:
                return new FollowTabScheme();
            case 1:
                return new FollowTabRanking();
            default:
                return null;
        }
    }

    //Overriden method getCount to get the number of tabs
    @Override
    public int getCount() {
        return mTabCount;
    }

    @Override
    public int getItemPosition(Object object) {
        return POSITION_NONE;
    }
}
