package com.example.pouleapp.Activities;

/**
 * Created by gezamenlijk on 16-8-2017.
 */

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.widget.ListViewAutoScrollHelper;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ExpandableListView;
import android.widget.SimpleExpandableListAdapter;
import android.widget.TextView;

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
import static com.example.pouleapp.Data.GlobalData.PREVIOUS_TAB;
import static com.example.pouleapp.Data.GlobalData.SCHEME_ACTIVITY;
import static com.example.pouleapp.Data.GlobalData.SCHEME_COLUMN;
import static com.example.pouleapp.Data.GlobalData.SCHEME_ROW;
import static com.example.pouleapp.Data.GlobalData.SCHEME_TAB;

/**
 * Created by Belal on 2/3/2016.
 */

//Our class extending fragment
public class TabScheme extends Fragment {
    private int mPoule_Index = 0;
    private Poule mPoule;

    private static final String NAME = "NAME";

//    private SimpleExpandableListAdapter mAdapter;

    private RoundSchemeAdapter mAdapter;
    ExpandableListView schemeExpandableListView;


    //Overriden method onCreateView
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        //Returning the layout file after inflating
        //Change R.layout.tab_scheme in you classes

        final GlobalData globalVariable = (GlobalData) getActivity().getApplicationContext();
        Tournament tournament = globalVariable.getTournament();
        ArrayList<Poule> pouleList = tournament.getPouleList();

        Intent intent = getActivity().getIntent();
        mPoule_Index = intent.getIntExtra(POULE_INDEX, 0);
        mPoule = pouleList.get(mPoule_Index);
        PouleScheme pouleScheme = mPoule.getPouleScheme();

        getActivity().setTitle(getResources().getString(R.string.menu_poule_name_text) + mPoule.getPouleName());

        View v = inflater.inflate(R.layout.tab_scheme, container, false);

        //  initiate the expandable list view
        schemeExpandableListView = (ExpandableListView) v.findViewById(R.id.schemeExpandableListView);

//        // create lists for group and child items
//        List<Map<String, String>> groupData = new ArrayList<Map<String, String>>();
//        List<List<Map<String, String>>> childData = new ArrayList<List<Map<String, String>>>();
//        // add data in group and child list
//        for (int round = 1; round < pouleScheme.getNumberOfRounds(); round++) {
//            Map<String, String> curGroupMap = new HashMap<String, String>();
//            groupData.add(curGroupMap);
//            curGroupMap.put(NAME, "Round " + round);
//
//            List<Map<String, String>> children = new ArrayList<Map<String, String>>();
//            Match[] mList = pouleScheme.getRoundMatchList(round);
//
//            for (int j = 0; j < mList.length; j++) {
//                Map<String, String> curChildMap = new HashMap<String, String>();
//                children.add(curChildMap);
//                curChildMap.put(NAME, mList[j].getMatchString());
//            }
//            childData.add(children);
//        }
//        // define arrays for displaying data in Expandable list view
//        String groupFrom[] = {NAME};
//        int groupTo[] = {R.id.heading};
//        String childFrom[] = {NAME};
//        int childTo[] = {R.id.childItem};
//
//
//        // Set up the adapter
////        mAdapter = new SimpleExpandableListAdapter(this, groupData,
////                R.layout.group_items,
////                groupFrom, groupTo,
////                childData, R.layout.child_items,
////                childFrom, childTo);
//
//        mAdapter = new SimpleExpandableListAdapter(v.getContext(), groupData,
//                R.layout.group_items,
//                groupFrom, groupTo,
//                childData, R.layout.child_items,
//                childFrom, childTo);

        mAdapter = new RoundSchemeAdapter(v.getContext(), pouleScheme);

        schemeExpandableListView.setAdapter(mAdapter);

        for (int i=0; i<pouleScheme.getNumberOfRounds()-1; i++ ) {
            schemeExpandableListView.expandGroup(i);
        }

        // perform set on group click listener event
        schemeExpandableListView.setOnGroupClickListener(new ExpandableListView.OnGroupClickListener() {
            @Override
            public boolean onGroupClick(ExpandableListView parent, View v, int groupPosition, long id) {
                //Toast.makeText(getApplicationContext(), "Group Name Is: Round " + groupPosition, Toast.LENGTH_LONG).show();

                return false;
            }
        });
        // perform set on child click listener event
        schemeExpandableListView.setOnChildClickListener(new ExpandableListView.OnChildClickListener() {
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
                intent.putExtra(PREVIOUS_TAB,SCHEME_TAB);
                //intent.putExtra(PREVIOUS_ACTIVITY,SCHEME_ACTIVITY);
                intent.putExtra(POULE_INDEX,mPoule_Index);
                intent.putExtra(SCHEME_ROW,x);
                intent.putExtra(SCHEME_COLUMN,y);
                startActivity(intent);

                //Toast.makeText(getApplicationContext(), "Match Index Is : [" + x + "][" + y + "]", Toast.LENGTH_LONG).show();
                return false;
            }
        });

        return v;
    }
}
