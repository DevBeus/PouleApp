package com.example.pouleapp.activities;


import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import com.example.pouleapp.data.GlobalData;
import com.example.pouleapp.data.Tournament;
import com.example.pouleapp.R;

import static com.example.pouleapp.data.GlobalData.SCHEME_TAB;
import static com.example.pouleapp.data.GlobalData.TAB_INDEX;

//Implementing the interface OnTabSelectedListener to our SchemeRankingActivity
//This interface would help in swiping views
public class SchemeRankingActivity extends AppCompatActivity implements TabLayout.OnTabSelectedListener{
    final Context mContext = this;

    //This is our viewPager
    private ViewPager viewPager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_scheme_ranking);

        Intent intent = getIntent();
        int selectedTab = intent.getIntExtra(TAB_INDEX, SCHEME_TAB);

        //Adding toolbar to the activity
        Toolbar toolbar = findViewById(R.id.scheme_ranking_toolbar);
        setSupportActionBar(toolbar);
        // Show the Up button in the action bar.

        android.support.v7.app.ActionBar actionBar = getSupportActionBar();

        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
        }


        //Initializing the tablayout
        TabLayout tabLayout = findViewById(R.id.scheme_ranking_tab_layout);

        //Adding the tabs using addTab() method
        tabLayout.addTab(tabLayout.newTab().setText(R.string.scheme_ranking_activity_text_view_scheme));
        tabLayout.addTab(tabLayout.newTab().setText(R.string.scheme_ranking_activity_text_view_ranking));

        final GlobalData globalVariable = (GlobalData) getApplicationContext();
        Tournament tournament = globalVariable.getTournament();

        if (tournament.isFullCompetition()) { tabLayout.addTab(tabLayout.newTab().setText(R.string.scheme_ranking_activity_text_view_tab_scheme)); }

        tabLayout.setTabGravity(TabLayout.GRAVITY_FILL);

        //Initializing viewPager
        viewPager = findViewById(R.id.scheme_ranking_view_pager);

        //Creating our pager adapter
        SchemeRankingPager adapter = new SchemeRankingPager(getSupportFragmentManager(), tabLayout.getTabCount());

        //Adding adapter to pager
        viewPager.setAdapter(adapter);
        viewPager.addOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(tabLayout));
        viewPager.setCurrentItem(selectedTab);

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

            Intent intent = new Intent(this,TournamentActivity.class);

            startActivity(intent);

            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    public void publishTournamentDialog(View view) {
        // get dialog_enter_poule_name.xml view
        LayoutInflater li = LayoutInflater.from(mContext);
        final ViewGroup nullParent = null; // introduced to avoid warning

        View DialogView = li.inflate(R.layout.dialog_publish_tournament, nullParent);

        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(mContext);

        // set dialog_publish_tournament.xml to alertdialog builder
        alertDialogBuilder.setView(DialogView);

        alertDialogBuilder
                .setCancelable(false)
                .setPositiveButton("OK",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog,int id) {
                                // publish tournament into firbase database
                                final GlobalData globalVariable = (GlobalData) getApplicationContext();

                                globalVariable.saveTournament();
                                globalVariable.publishTournament();
                            }
                        })
                .setNegativeButton("Cancel",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog,int id) {
                                dialog.cancel();
                            }
                        })
                .setOnDismissListener(new DialogInterface.OnDismissListener() {
                                          // Needed to refresh listView after update
                                          @Override
                                          public void onDismiss(DialogInterface dialog) {
                                              recreate();
                                          }
                                      }
                );

        // create alert enter_tournament_dialog.xml
        AlertDialog publishTournamentDialog = alertDialogBuilder.create();

        // show it
        publishTournamentDialog.show();

    }

}
