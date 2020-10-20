package com.example.pouleapp.activities;


import android.content.Intent;
import android.os.Bundle;
import androidx.fragment.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import com.example.pouleapp.data.GlobalData;
import com.example.pouleapp.data.PublishMatch;
import com.example.pouleapp.data.PublishPoule;
import com.example.pouleapp.data.PublishRound;
import com.example.pouleapp.data.PublishTeam;
import com.example.pouleapp.data.PublishTournament;
import com.example.pouleapp.data.Team;
import com.example.pouleapp.R;

import java.util.ArrayList;
import java.util.Collections;

import static com.example.pouleapp.data.GlobalData.POULE_INDEX;

/**
 * Created by gezamenlijk on 16-8-2017.
 * This class is used for the tab named Ranking
 * Code concept copied from https://www.simplifiedcoding.net/android-tablayout-example-using-viewpager-fragments/
 */

//Our class extending fragment
public class FollowTabRanking extends Fragment {


    //Overriden method onCreateView
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        //Returning the layout file after inflating
        //Change R.layout.tab_ranking in you classes
        View v = inflater.inflate(R.layout.follow_tab_ranking, container, false);

        final GlobalData globalVariable = (GlobalData) getActivity().getApplicationContext();
        PublishTournament tournament = globalVariable.getPublishTournament();
        ArrayList<PublishPoule> pouleList = tournament.getPouleList();

        Intent intent = getActivity().getIntent();
        int pouleIndex = intent.getIntExtra(POULE_INDEX,0);

        PublishPoule poule = pouleList.get(pouleIndex);
        ArrayList<PublishTeam> publishTeamList = poule.getTeamList();
        //setTitle(getResources().getString(R.string.menu_poule_name_text) + poule.getPouleName());

        ArrayList<Team> teamList =  new ArrayList<>();

        for (PublishTeam t: publishTeamList ) {
            teamList.add(new Team(t.getTeamName(),t.getCoachName()));
        }

        // Calculate ranking for all teams
        // publishTeamList only contains team name and coach
        // teamList contains team name, coach, and all results

        for(PublishRound r: poule.getRoundList()) {
            for (PublishMatch m: r.getMatchList()) {
                for (Team t: teamList){
                    if (t.getTeamName().equals(m.getHomeTeam())){

                        if ((m.getGoalsFor()!=null) && (m.getGoalsAgainst()!=null)) {
                            t.addMatchResult(m.getGoalsFor(), m.getGoalsAgainst());
                         }
                     }

                    if (t.getTeamName().equals(m.getOpponent())){

                        if ((m.getGoalsFor()!=null) && (m.getGoalsAgainst()!=null)) {
                            t.addMatchResult(m.getGoalsAgainst(), m.getGoalsFor());
                        }
                    }

                }

            }
        }

        ArrayList<Team> sortedPoule = new ArrayList<>(teamList);

        Collections.sort(sortedPoule, Team.RankingComparator);

        ListView lview = v.findViewById(R.id.follow_tab_ranking_list_view_ranking);
        RankingAdapter adapter = new RankingAdapter(getActivity(), sortedPoule);
        lview.setAdapter(adapter);

        adapter.notifyDataSetChanged();


        return v;
    }
}
