package com.example.pouleapp.Activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ExpandableListView;
import android.widget.SimpleExpandableListAdapter;

import com.example.pouleapp.Data.GlobalData;
import com.example.pouleapp.Data.Match;
import com.example.pouleapp.Data.Poule;
import com.example.pouleapp.Data.PouleScheme;
import com.example.pouleapp.Data.Team;
import com.example.pouleapp.Data.Tournament;
import com.example.pouleapp.R;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.example.pouleapp.Data.GlobalData.POULE_INDEX;
import static com.example.pouleapp.Data.GlobalData.PREVIOUS_ACTIVITY;
import static com.example.pouleapp.Data.GlobalData.SCHEME_ACTIVITY;
import static com.example.pouleapp.Data.GlobalData.SCHEME_COLUMN;
import static com.example.pouleapp.Data.GlobalData.SCHEME_ROW;

/**
 * Created by gezamenlijk on 29-6-2017.
 */

public class SchemeActivity extends AppCompatActivity implements SimpleGestureFilter.SimpleGestureListener{
    private SimpleGestureFilter mDetector;
    private int mPoule_Index = 0;
    private Poule mPoule;

    private static final String NAME = "NAME";

    private SimpleExpandableListAdapter mAdapter;
    ExpandableListView simpleExpandableListView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_scheme);

        // Detect touched area
        mDetector = new SimpleGestureFilter(this,this);

        final GlobalData globalVariable = (GlobalData) getApplicationContext();
        Tournament tournament = globalVariable.getTournament();
        ArrayList<Poule> pouleList = tournament.getPouleList();

        Intent intent = getIntent();
        mPoule_Index = intent.getIntExtra(POULE_INDEX, 0);
        mPoule = pouleList.get(mPoule_Index);
        PouleScheme pouleScheme = mPoule.getPouleScheme();

        setTitle(getResources().getString(R.string.menu_poule_name_text) + mPoule.getPouleName());

        //  initiate the expandable list view
        simpleExpandableListView = (ExpandableListView) findViewById(R.id.simpleExpandableListView);
        // create lists for group and child items
        List<Map<String, String>> groupData = new ArrayList<Map<String, String>>();
        List<List<Map<String, String>>> childData = new ArrayList<List<Map<String, String>>>();
        // add data in group and child list
        for (int round = 1; round < pouleScheme.getNumberOfRounds(); round++) {
            Map<String, String> curGroupMap = new HashMap<String, String>();
            groupData.add(curGroupMap);
            curGroupMap.put(NAME, "Round " + round);

            List<Map<String, String>> children = new ArrayList<Map<String, String>>();
            Match[] mList = pouleScheme.getRoundMatchList(round);

            for (int j = 0; j < mList.length; j++) {
                Map<String, String> curChildMap = new HashMap<String, String>();
                children.add(curChildMap);
                curChildMap.put(NAME, mList[j].getMatchString());
            }
            childData.add(children);
        }
        // define arrays for displaying data in Expandable list view
        String groupFrom[] = {NAME};
        int groupTo[] = {R.id.heading};
        String childFrom[] = {NAME};
        int childTo[] = {R.id.childItem};


        // Set up the adapter
        mAdapter = new SimpleExpandableListAdapter(this, groupData,
                R.layout.group_items,
                groupFrom, groupTo,
                childData, R.layout.child_items,
                childFrom, childTo);
        simpleExpandableListView.setAdapter(mAdapter);

        for (int i=0; i<pouleScheme.getNumberOfRounds()-1; i++ ) {
            simpleExpandableListView.expandGroup(i);
        }

        // perform set on group click listener event
        simpleExpandableListView.setOnGroupClickListener(new ExpandableListView.OnGroupClickListener() {
            @Override
            public boolean onGroupClick(ExpandableListView parent, View v, int groupPosition, long id) {
                //Toast.makeText(getApplicationContext(), "Group Name Is: Round " + groupPosition, Toast.LENGTH_LONG).show();

                return false;
            }
        });
        // perform set on child click listener event
        simpleExpandableListView.setOnChildClickListener(new ExpandableListView.OnChildClickListener() {
            @Override
            public boolean onChildClick(ExpandableListView parent, View v, int groupPosition, int childPosition, long id) {

                // display a toast with child name whenever a user clicks on a child item
                int r = groupPosition;

                PouleScheme pouleScheme = mPoule.getPouleScheme();
                ArrayList<Team> teamList = mPoule.getTeamList();
                Match[] mList = pouleScheme.getRoundMatchList(r+1);

                Match match = mList[childPosition];
                String homeTeam = match.getHomeTeam();
                String opponent = match.getOpponent();

                int x = 0;
                int y = 0;

                for (int i = 0; i< teamList.size(); i++) {
                    if (homeTeam.equals(teamList.get(i).getTeamName())) { x = i; }
                    if (opponent.equals(teamList.get(i).getTeamName())) { y = i; }
                }

                Intent intent = new Intent(v.getContext(), EditMatchActivity.class);
                intent.putExtra(PREVIOUS_ACTIVITY,SCHEME_ACTIVITY);
                intent.putExtra(POULE_INDEX,mPoule_Index);
                intent.putExtra(SCHEME_ROW,x);
                intent.putExtra(SCHEME_COLUMN,y);
                startActivity(intent);

                //Toast.makeText(getApplicationContext(), "Match Index Is : [" + x + "][" + y + "]", Toast.LENGTH_LONG).show();
                return false;
            }
        });
    }

    @Override
    public boolean dispatchTouchEvent(MotionEvent me){
        // Call onTouchEvent of SimpleGestureFilter class
        this.mDetector.onTouchEvent(me);
        return super.dispatchTouchEvent(me);
    }
    @Override
    public void onSwipe(int direction) {
        String str = "";
        Intent intent;

        switch (direction) {

            case SimpleGestureFilter.SWIPE_RIGHT : str = "Swipe Right";
                // startTournamentActivity
                intent = new Intent(this, TournamentActivity.class);
                startActivity(intent);
                break;

            case SimpleGestureFilter.SWIPE_LEFT :  str = "Swipe Left";
                // startRankingActivity
                intent = new Intent(getApplicationContext(), RankingActivity.class);
                intent.putExtra(POULE_INDEX, mPoule_Index);
                startActivity(intent);
                break;

            case SimpleGestureFilter.SWIPE_DOWN :  str = "Swipe Down";
                break;
            case SimpleGestureFilter.SWIPE_UP :    str = "Swipe Up";
                break;

        }
        //Toast.makeText(this, str, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onDoubleTap() {
        //Toast.makeText(this, "Double Tap", Toast.LENGTH_SHORT).show();
    }

    public void startRankingActivity(View view) {
        Intent intent = new Intent(this, RankingActivity.class);
        intent.putExtra(POULE_INDEX, mPoule_Index);
        startActivity(intent);
    }

}


