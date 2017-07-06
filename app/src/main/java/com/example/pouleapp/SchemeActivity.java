package com.example.pouleapp;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ExpandableListView;
import android.widget.SimpleExpandableListAdapter;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.example.pouleapp.GlobalData.POULE_INDEX;

/**
 * Created by gezamenlijk on 29-6-2017.
 */

public class SchemeActivity extends AppCompatActivity {
    private int mPoule_Index = 0;

    private static final String NAME = "NAME";

    private SimpleExpandableListAdapter mAdapter;
    ExpandableListView simpleExpandableListView;
    // string arrays for group and child items
    //private String groupItems[] = {"Poule A", "Poule B", "Poule C"};
    private String[][] childItems = {{"Ajax", "Feyenoord", "PSV"}, {"Heerenveen", "Twente", "Utrecht"}, {"NEC", "Heracles", "GA Eagles"}};


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //setContentView(R.layout.activity_main_test);
        setContentView(R.layout.activity_scheme);
        ViewGroup radioGroup;

        final GlobalData globalVariable = (GlobalData) getApplicationContext();
        Tournament tournament = globalVariable.getTournament();
        ArrayList<Poule> pouleList = tournament.getPouleList();

        Intent intent = getIntent();
        mPoule_Index = intent.getIntExtra(POULE_INDEX, 0);

        Poule poule = pouleList.get(mPoule_Index);
        ArrayList<Team> teamList = poule.getTeamList();
        PouleScheme pouleScheme = poule.getPouleScheme();

        setTitle(getResources().getString(R.string.menu_poule_name_text) + poule.getPouleName());

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

//                Match m = pouleScheme.getMatchFromScheme(round,team_index);

//                String matchString = m.getMatchString();
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

        // perform set on group click listener event
        simpleExpandableListView.setOnGroupClickListener(new ExpandableListView.OnGroupClickListener() {
            @Override
            public boolean onGroupClick(ExpandableListView parent, View v, int groupPosition, long id) {

                // display a toast with group name whenever a user clicks on a group item
                Toast.makeText(getApplicationContext(), "Group Name Is: Round " + groupPosition, Toast.LENGTH_LONG).show();

                return false;
            }
        });
        // perform set on child click listener event
        simpleExpandableListView.setOnChildClickListener(new ExpandableListView.OnChildClickListener() {
            @Override
            public boolean onChildClick(ExpandableListView parent, View v, int groupPosition, int childPosition, long id) {

                // display a toast with child name whenever a user clicks on a child item
                Toast.makeText(getApplicationContext(), "Child Name Is :" + childItems[groupPosition][childPosition], Toast.LENGTH_LONG).show();
                return false;
            }
        });
    }



}


