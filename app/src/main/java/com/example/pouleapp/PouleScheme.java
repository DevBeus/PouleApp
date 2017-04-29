package com.example.pouleapp;


import java.util.ArrayList;

/**
 * Created by gezamenlijk on 26-2-2017.
 */

public class PouleScheme {
    private Match[][] mPouleScheme;
    private int mPouleSize;

    public PouleScheme(ArrayList<Team> poule) {
        mPouleSize = poule.size();
        mPouleScheme = new Match[mPouleSize][mPouleSize];

        for (int i=0; i<poule.size(); i++) {
            for (int j=0; j<poule.size(); j++) {
                Team team1 = poule.get(i);
                Team team2 = poule.get(j);
                Match match = new Match(team1.getTeamName(), team2.getTeamName());
                mPouleScheme[i][j] = match;
            }

        }
    }

    public void addTeam(String newTeam) {
        Match[][] pouleSchemeNew = new Match[mPouleSize+1][mPouleSize+1];

        // Copy all existing results
        for (int i = 0; i < mPouleSize; i++) {
            for (int j = 0; j < mPouleSize; j++) {
                pouleSchemeNew[i][j] = mPouleScheme[i][j];
            }
        }

        // Add new matches
        for (int i = 0; i < mPouleSize; i++) {
            pouleSchemeNew[mPouleSize][i] = new Match(newTeam,mPouleScheme[0][i].getHomeTeam());
            pouleSchemeNew[i][mPouleSize] = new Match(mPouleScheme[i][0].getOpponent(),newTeam);
        }

        pouleSchemeNew[mPouleSize][mPouleSize] = new Match(newTeam, newTeam);

        mPouleSize++;
        mPouleScheme = pouleSchemeNew;

    }

    public void deleteTeam(ArrayList<Team> teamList, int sel_team) {

        Match[][] pouleSchemeNew = new Match[mPouleSize - 1][mPouleSize - 1];
        int a = 0;

        for (int i = 0; i < mPouleSize; i++) {
            int b = 0;

            if (i != sel_team) {

                for (int j = 0; j < mPouleSize; j++) {

                    if (j != sel_team) {
                        //copy match results
                        pouleSchemeNew[a][b] = mPouleScheme[i][j];
                        b++;
                    } else {
                        //don't copy Match and remove match results
                        removeMatch(teamList, i, j);
                    }
                }

                a++;
            }
            else {
                // remove complete row
                for (int j = 0; j < mPouleSize; j++) { removeMatch(teamList, i, j); }

            }

        }

        mPouleSize--;
        mPouleScheme = pouleSchemeNew;
    }

    public void removeMatch(ArrayList<Team> poule, int i, int j){
        Match match = mPouleScheme[i][j];

        Team homeTeam = poule.get(i);
        Team opponent = poule.get(j);

        //First check whether Match was already filled, if yes remove match result first
        Integer act_gf = match.getGoalsFor();
        Integer act_ga = match.getGoalsAgainst();

        if ((act_gf!=null)&&(act_ga!=null)){
            homeTeam.removeMatchResult(act_gf,act_ga);
            opponent.removeMatchResult(act_ga,act_gf);
        }

    }

    public void updateMatch(ArrayList<Team> poule, int i, int j, Integer gf, Integer ga){
        Match match = mPouleScheme[i][j];

        Team homeTeam = poule.get(i);
        Team opponent = poule.get(j);

        //First check whether Match was already filled, if yes remove match result first
        Integer act_gf = match.getGoalsFor();
        Integer act_ga = match.getGoalsAgainst();

        if ((act_gf!=null)&&(act_ga!=null)){
            homeTeam.removeMatchResult(act_gf,act_ga);
            opponent.removeMatchResult(act_ga,act_gf);
        }

        match.setResult(gf,ga);

        if ((gf!=null) && (ga!=null)) {
            homeTeam.addMatchResult(gf, ga);
            opponent.addMatchResult(ga, gf);
        }
    }

    public String getMatchResult(int i, int j) {
        return mPouleScheme[i][j].getResultString();
    }

    public Integer getMatchGoalsFor(int i, int j) {
        Match match = mPouleScheme[i][j];
        Integer gf = match.getGoalsFor();
        return gf;
    }

    public Integer getMatchGoalsAgainst(int i, int j) {
        Match match = mPouleScheme[i][j];
        Integer ga = match.getGoalsAgainst();
        return ga;
    }

    public Match getMatch(int i, int j){ return mPouleScheme[i][j]; }

    public void setMatch(int i, int j, Match match) { mPouleScheme[i][j] = match; }

    public int getPouleSize() { return mPouleSize; }



    public void fillTestData(ArrayList<Team> poule) {
        Match match = mPouleScheme[0][1];

        Team homeTeam = poule.get(0);
        Team opponent = poule.get(1);

        match.setResult(3,1);

        homeTeam.addMatchResult(3,1);
        opponent.addMatchResult(1,3);

    }

}
