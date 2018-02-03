package com.example.pouleapp.Activities;


import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;

import com.example.pouleapp.Data.GlobalData;
import com.example.pouleapp.Data.PublishTournament;
import com.example.pouleapp.Data.Tournament;
import com.example.pouleapp.R;

import static com.example.pouleapp.Data.GlobalData.SCHEME_TAB;
import static com.example.pouleapp.Data.GlobalData.TAB_INDEX;

//Implementing the interface OnTabSelectedListener to our SchemeRankingActivity
//This interface would help in swiping views
public class FollowSchemeRankingActivity extends AppCompatActivity implements TabLayout.OnTabSelectedListener{

    //This is our tablayout
    private TabLayout tabLayout;

    //This is our viewPager
    private ViewPager viewPager;
    private int mSelected_Tab;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_follow_scheme_ranking);

        Intent intent = getIntent();
        mSelected_Tab = intent.getIntExtra(TAB_INDEX,SCHEME_TAB); // Default tab is Scheme Tab

        //Adding toolbar to the activity
        Toolbar toolbar = (Toolbar) findViewById(R.id.follow_scheme_ranking_toolbar);
        setSupportActionBar(toolbar);
        // Show the Up button in the action bar.

        android.support.v7.app.ActionBar actionBar = getSupportActionBar();

        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
        }


        //Initializing the tablayout
        tabLayout = (TabLayout) findViewById(R.id.follow_scheme_ranking_tab_layout);

        //Adding the tabs using addTab() method
        tabLayout.addTab(tabLayout.newTab().setText(R.string.scheme_ranking_activity_text_view_scheme));
        tabLayout.addTab(tabLayout.newTab().setText(R.string.scheme_ranking_activity_text_view_ranking));

//        final GlobalData globalVariable = (GlobalData) getApplicationContext();
//        PublishTournament tournament = globalVariable.getPublishTournament();

        tabLayout.setTabGravity(TabLayout.GRAVITY_FILL);

        //Initializing viewPager
        viewPager = (ViewPager) findViewById(R.id.follow_scheme_ranking_view_pager);

        //Creating our pager adapter
        FollowSchemeRankingPager adapter = new FollowSchemeRankingPager(getSupportFragmentManager(), tabLayout.getTabCount());

        //Adding adapter to pager
        viewPager.setAdapter(adapter);
        viewPager.addOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(tabLayout));
        viewPager.setCurrentItem(mSelected_Tab);



        //Adding onTabSelectedListener to swipe views
        //tabLayout.setOnTabSelectedListener(this);
        tabLayout.addOnTabSelectedListener(this);

    }

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

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == android.R.id.home) {
            // This ID represents the Home or Up button. In the case of this
            // activity, the Up button is shown. For
            // more details, see the Navigation pattern on Android Design:
            //
            // http://developer.android.com/design/patterns/navigation.html#up-vs-back
            //
            //navigateUpTo(new Intent(this, PouleActivity.class));

            Intent intent = new Intent(this,FollowTournamentInfoActivity.class);

            startActivity(intent);

            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
