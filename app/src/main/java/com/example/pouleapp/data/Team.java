package com.example.pouleapp.data;

import java.util.Comparator;

/**
 * Created by gezamenlijk on 7-1-2017.
 */

public class Team {
    private String mTeamName = "";
    private String mCoachName = "";

    private int mMatches = 0;
    private int mPoints = 0;
    private int mGoalsFor = 0;
    private int mGoalsAgainst = 0;
    private int mWins = 0;
    private int mDraws = 0;
    private int mLosses = 0;

    public Team(String name) {
        mTeamName = name;
    }

    public Team(String tName, String cName) {
        mTeamName = tName;
        mCoachName = cName;
    }

    public String getTeamName() {
        return mTeamName;
    }

    public void setTeamName(String teamName) {
        this.mTeamName = teamName;
    }

    public String getCoachName() {
        return mCoachName;
    }

    public void setCoachName(String coachName) {
        this.mCoachName = coachName;
    }

    public void addMatchResult(int goalsFor, int goalsAgainst) {

        if (goalsFor > goalsAgainst) {
            this.mPoints = this.mPoints + 3;
            this.mWins++;
        }
        else if (goalsFor == goalsAgainst) {
            this.mPoints = this.mPoints + 1;
            this.mDraws++;
        }
        else {
            this.mLosses++;
        }

        this.mMatches++;
        this.mGoalsFor = this.mGoalsFor + goalsFor;
        this.mGoalsAgainst = this.mGoalsAgainst + goalsAgainst;
    }

    public void removeMatchResult(int goalsFor, int goalsAgainst) {

        if (goalsFor > goalsAgainst) {
            this.mPoints = this.mPoints - 3;
            this.mWins--;
        }
        else if (goalsFor == goalsAgainst) {
            this.mPoints = this.mPoints - 1;
            this.mDraws--;
        }
        else {
            this.mLosses--;
        }

        this.mMatches--;
        this.mGoalsFor = this.mGoalsFor - goalsFor;
        this.mGoalsAgainst = this.mGoalsAgainst - goalsAgainst;

    }

    public int getMatches() {
        return mMatches;
    }
    public int getPoints() {
        return mPoints;
    }

    public int getGoalsFor() {
        return mGoalsFor;
    }

    public int getGoalsAgainst() {
        return mGoalsAgainst;
    }

    public int getWins() {
        return mWins;
    }

    public int getDraws() {
        return mDraws;
    }

    public int getLosses() {
        return mLosses;
    }

    public static Comparator<Team> RankingComparator = new Comparator<Team>() {

        @Override
        public int compare(Team t1, Team t2) {
            //This ranking is done in the following order:
            //- px: mPoints
            //- grx: goals result: mGoalsFor - mGoalsAgainst
            //- gfx: mGoalsFor

            int p1 = t1.getPoints();
            int p2 = t2.getPoints();
            int gr1 = t1.getGoalsFor()-t1.getGoalsAgainst();
            int gr2 = t2.getGoalsFor()-t2.getGoalsAgainst();
            int gf1 = t1.getGoalsFor();
            int gf2 = t2.getGoalsFor();
            int result;

            result = p2-p1;

            if (result == 0) { result = gr2-gr1; }

            if (result == 0) { result = gf2-gf1; }

            return result;
        }
    };

}
