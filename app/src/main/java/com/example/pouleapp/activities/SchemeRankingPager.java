package com.example.pouleapp.activities;


import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentStatePagerAdapter;

/**
 * Created by gezamenlijk on 16-8-2017.
 * this class is used to support the 3 tabs in SchemeRankingActivity
 */

//Extending FragmentStatePagerAdapter
class SchemeRankingPager extends FragmentStatePagerAdapter {

    //integer to count number of tabs
    private int mTabCount;

    //Constructor to the class
    SchemeRankingPager(FragmentManager fm, int tabCount) {
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
                return new TabScheme();
            case 1:
                return new TabRanking();
            case 2:
                return new TabSchemeTable();
            default:
                return null;
        }
    }

    //Overriden method getCount to get the number of tabs
    @Override
    public int getCount() {
        return mTabCount;
    }
}
