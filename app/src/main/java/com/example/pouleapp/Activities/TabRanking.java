package com.example.pouleapp.Activities;

/**
 * Created by gezamenlijk on 16-8-2017.
 */

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import com.example.pouleapp.Data.GlobalData;
import com.example.pouleapp.Data.Poule;
import com.example.pouleapp.Data.Team;
import com.example.pouleapp.Data.Tournament;
import com.example.pouleapp.R;

import java.util.ArrayList;
import java.util.Collections;

import static com.example.pouleapp.Data.GlobalData.POULE_INDEX;

/**
 * Created by Belal on 2/3/2016.
 */

//Our class extending fragment
public class TabRanking extends Fragment {

    private int mPoule_Index = 0;

    //Overriden method onCreateView
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        //Returning the layout file after inflating
        //Change R.layout.tab_ranking in you classes
        View v = inflater.inflate(R.layout.tab_ranking, container, false);

        final GlobalData globalVariable = (GlobalData) getActivity().getApplicationContext();
        Tournament tournament = globalVariable.getTournament();
        ArrayList<Poule> pouleList = tournament.getPouleList();

        Intent intent = getActivity().getIntent();
        mPoule_Index = intent.getIntExtra(POULE_INDEX,0);

        Poule poule = pouleList.get(mPoule_Index);
        ArrayList<Team> teamList = poule.getTeamList();
        //setTitle(getResources().getString(R.string.menu_poule_name_text) + poule.getPouleName());

        ArrayList<Team> sortedPoule = new ArrayList<>(teamList);

        Collections.sort(sortedPoule, Team.RankingComparator);

        ListView lview = (ListView) v.findViewById(R.id.tab_ranking_list_view_ranking);
        RankingAdapter adapter = new RankingAdapter(getActivity(), sortedPoule);
        lview.setAdapter(adapter);

        adapter.notifyDataSetChanged();


        return v;
    }
}
