package com.example.pouleapp;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ExpandableListView;
import android.widget.SimpleExpandableListAdapter;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.example.pouleapp.GlobalData.POULE_INDEX;
import static com.example.pouleapp.GlobalData.TEAM_INDEX;

public class PouleActivity extends AppCompatActivity {

    private static final String NAME = "POULE";


    private SimpleExpandableListAdapter mAdapter;
    ExpandableListView ExpandablePouleListView;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_poule);
        ArrayList<Poule> pouleList;

        final GlobalData globalVariable = (GlobalData) getApplicationContext();
        Tournament tournament = globalVariable.getTournament();
        //globalVariable.initPouleList();
        pouleList = tournament.getPouleList();

        TextView tvTournamentName = (TextView) findViewById(R.id.editTournamentName);
        tvTournamentName.setText(tournament.getTournamentName());

        TextView tvTournamentLocation = (TextView) findViewById(R.id.editTournamentLocation);
        tvTournamentLocation.setText(tournament.getLocation());

        //  initiate the expandable list view
        ExpandablePouleListView = (ExpandableListView) findViewById(R.id.ExpandablePouleListView);
        // create lists for group and child items
        List<Map<String, String>> groupData = new ArrayList<Map<String, String>>();
        List<List<Map<String, String>>> childData = new ArrayList<List<Map<String, String>>>();
        // add data in group and child list
        for (int i = 0; i < pouleList.size(); i++) {
            Map<String, String> curGroupMap = new HashMap<String, String>();
            groupData.add(curGroupMap);
            curGroupMap.put(NAME, pouleList.get(i).getPouleName());

            ArrayList<Team> teamList = pouleList.get(i).getTeamList();

            List<Map<String, String>> children = new ArrayList<Map<String, String>>();
            for (int j = 0; j < teamList.size(); j++) {
                Map<String, String> curChildMap = new HashMap<String, String>();
                children.add(curChildMap);
                curChildMap.put(NAME, teamList.get(j).getTeamName());
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
        ExpandablePouleListView.setAdapter(mAdapter);

        // perform set on group click listener event
        ExpandablePouleListView.setOnGroupClickListener(new ExpandableListView.OnGroupClickListener() {
            @Override
            public boolean onGroupClick(ExpandableListView parent, View v, int groupPosition, long id) {

                // display a toast with group name whenever a user clicks on a group item
                // Toast.makeText(getApplicationContext(), "Group Name Is :" + groupItems[groupPosition], Toast.LENGTH_LONG).show();

                return false;
            }
        });
        // perform set on child click listener event
        ExpandablePouleListView.setOnChildClickListener(new ExpandableListView.OnChildClickListener() {
            @Override
            public boolean onChildClick(ExpandableListView parent, View v, int groupPosition, int childPosition, long id) {

                // display a toast with child name whenever a user clicks on a child item
                // groupPosition = pouleIndex, childPosition = team
                // when team is clicked open EditPouleActivity
                Intent intent = new Intent(getApplicationContext(), EditPouleActivity.class);
                intent.putExtra(POULE_INDEX, groupPosition);
                intent.putExtra(TEAM_INDEX,childPosition);

                startActivity(intent);

                //Toast.makeText(getApplicationContext(), "Child Name Is :" + childItems[groupPosition][childPosition], Toast.LENGTH_LONG).show();
                return false;
            }
        });
    }


    public void addPoule(View view) {
        final GlobalData globalVariable = (GlobalData) getApplicationContext();
        Tournament tournament = globalVariable.getTournament();
        ArrayList<Poule> pouleList = tournament.getPouleList();
        int pouleId = pouleList.size();

        Poule poule = new Poule("Poule"+pouleId);
        pouleList.add(poule);

        globalVariable.saveTournament();

        recreate();

    }

    public void saveTournament(View view) {
        final GlobalData globalVariable = (GlobalData) getApplicationContext();
        Tournament tournament = globalVariable.getTournament();

        TextView tvTournamentName = (TextView) findViewById(R.id.editTournamentName);
        String name = tvTournamentName.getText().toString();

        TextView tvTournamentLocation = (TextView) findViewById(R.id.editTournamentLocation);
        String location = tvTournamentLocation.getText().toString();

        tournament.setTournamentName(name);
        tournament.setLocation(location);

        globalVariable.saveTournament();
        globalVariable.updateTournamentName(name);
        globalVariable.saveAppData();

        recreate();

    }

    public void home(View view) {
        Intent intent = new Intent(getApplicationContext(), MainActivity.class);

        startActivity(intent);

    }
}
