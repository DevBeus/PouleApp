package com.example.pouleapp.Activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ExpandableListView;

import com.example.pouleapp.Data.GlobalData;
import com.example.pouleapp.Data.Match;
import com.example.pouleapp.Data.Poule;
import com.example.pouleapp.Data.PouleScheme;
import com.example.pouleapp.Data.Team;
import com.example.pouleapp.Data.Tournament;
import com.example.pouleapp.R;

import java.util.ArrayList;

import static com.example.pouleapp.Data.GlobalData.POULE_INDEX;
import static com.example.pouleapp.Data.GlobalData.PREVIOUS_TAB;
import static com.example.pouleapp.Data.GlobalData.SCHEME_COLUMN;
import static com.example.pouleapp.Data.GlobalData.SCHEME_ROW;
import static com.example.pouleapp.Data.GlobalData.SCHEME_TAB;

/**
 * Created by gezamenlijk on 16-8-2017.
 * This class is used for the tab named Scheme
 * Code concept copied from https://www.simplifiedcoding.net/android-tablayout-example-using-viewpager-fragments/
 */

//Our class extending fragment
public class TabScheme extends Fragment {
    private int mPoule_Index = 0;
    private Poule mPoule;


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
        ExpandableListView schemeExpandableListView = (ExpandableListView) v.findViewById(R.id.tab_scheme_expandable_list_view_scheme);

        RoundSchemeAdapter rsAdapter = new RoundSchemeAdapter(v.getContext(), pouleScheme);

        schemeExpandableListView.setAdapter(rsAdapter);

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

                PouleScheme pouleScheme = mPoule.getPouleScheme();
                ArrayList<Team> teamList = mPoule.getTeamList();
                Match[] mList = pouleScheme.getRoundMatchList(groupPosition+1);

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
                intent.putExtra(POULE_INDEX,mPoule_Index);
                intent.putExtra(SCHEME_ROW,x);
                intent.putExtra(SCHEME_COLUMN,y);
                startActivity(intent);

                return false;
            }
        });

        return v;
    }
}
