package com.example.pouleapp;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ListView;

import java.util.ArrayList;
import java.util.Collections;

import static com.example.pouleapp.GlobalData.POULE_INDEX;

public class RankingActivity extends AppCompatActivity {
    private int mPoule_Index = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ranking);

        final GlobalData globalVariable = (GlobalData) getApplicationContext();
        ArrayList<Poule> pouleList = globalVariable.getPouleList();

        Intent intent = getIntent();
        mPoule_Index = intent.getIntExtra(POULE_INDEX,0);

        Poule poule = pouleList.get(mPoule_Index);
        ArrayList<Team> teamList = poule.getTeamList();

        ArrayList<Team> sortedPoule = new ArrayList<>(teamList);

        Collections.sort(sortedPoule, Team.RankingComparator);

        ListView lview = (ListView) findViewById(R.id.listview);
        RankingAdapter adapter = new RankingAdapter(this, sortedPoule);
        lview.setAdapter(adapter);

        adapter.notifyDataSetChanged();

    }

    /** This method is called when Scheme button is clicked */
    public void showScheme(View view) {
        Intent intent = new Intent(this, SchemeTableActivity.class);
        intent.putExtra(POULE_INDEX, mPoule_Index);
        startActivity(intent);
    }

    /** This method is called when Team List button is clicked */
    public void showTeamList(View view) {
        Intent intent = new Intent(this, EditPouleActivity.class);
        intent.putExtra(POULE_INDEX, mPoule_Index);
        startActivity(intent);
    }

}
