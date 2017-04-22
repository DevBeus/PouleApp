package com.example.pouleapp;

import java.util.Comparator;

/**
 * Created by gezamenlijk on 7-1-2017.
 */

public class Team {
    private String teamName = "";
    private String coachName = "";

    private int matches = 0;
    private int points = 0;
    private int goalsFor = 0;
    private int goalsAgainst = 0;
    private int wins  = 0;
    private int draws = 0;
    private int losses = 0;

    public Team(String name) {
        teamName = name;
    }

    public Team(String tName, String cName) {
        teamName = tName;
        coachName = cName;
    }

    public String getTeamName() {
        return teamName;
    }

    public void setTeamName(String teamName) {
        this.teamName = teamName;
    }

    public String getCoachName() {
        return coachName;
    }

    public void setCoachName(String coachName) {
        this.coachName = coachName;
    }

    public void addMatchResult(int goalsFor, int goalsAgainst) {

        if (goalsFor > goalsAgainst) {
            this.points = this.points + 3;
            this.wins++;
        }
        else if (goalsFor == goalsAgainst) {
            this.points = this.points + 1;
            this.draws++;
        }
        else {
            this.losses++;
        }

        this.matches++;
        this.goalsFor = this.goalsFor + goalsFor;
        this.goalsAgainst = this.goalsAgainst + goalsAgainst;
    }

    public void removeMatchResult(int goalsFor, int goalsAgainst) {

        if (goalsFor > goalsAgainst) {
            this.points = this.points - 3;
            this.wins--;
        }
        else if (goalsFor == goalsAgainst) {
            this.points = this.points - 1;
            this.draws--;
        }
        else {
            this.losses--;
        }

        this.matches--;
        this.goalsFor = this.goalsFor - goalsFor;
        this.goalsAgainst = this.goalsAgainst - goalsAgainst;

    }

    public int getMatches() {
        return matches;
    }
    public int getPoints() {
        return points;
    }

    public int getGoalsFor() {
        return goalsFor;
    }

    public int getGoalsAgainst() {
        return goalsAgainst;
    }

    public int getWins() {
        return wins;
    }

    public int getDraws() {
        return draws;
    }

    public int getLosses() {
        return losses;
    }

    public static Comparator<Team> RankingComparator = new Comparator<Team>() {

        @Override
        public int compare(Team t1, Team t2) {
            //This ranking is done in the following order:
            //- px: points
            //- grx: goals result: goalsFor - goalsAgainst
            //- gfx: goalsFor

            int p1 = t1.getPoints();
            int p2 = t2.getPoints();
            int gr1 = t1.getGoalsFor()-t1.getGoalsAgainst();
            int gr2 = t2.getGoalsFor()-t2.getGoalsAgainst();
            int gf1 = t1.getGoalsFor();
            int gf2 = t2.getGoalsFor();
            int result = 0;

            result = p2-p1;

            if (result == 0) { result = gr2-gr1; }

            if (result == 0) { result = gf2-gf1; }

            return result;
        }
    };

}
