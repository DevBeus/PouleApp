package com.example.pouleapp.Data;

/**
 * Created by gezamenlijk on 11-2-2017.
 */

public class Match {
    private String mHomeTeam = "";
    private String mOpponent = "";
    private Integer mGoalsFor;
    private Integer mGoalsAgainst;

    public Integer getGoalsFor() {
        return mGoalsFor;
    }

    public void setGoalsFor(Integer goalsFor) {
        this.mGoalsFor = goalsFor;
    }

    public Integer getGoalsAgainst() {
        return mGoalsAgainst;
    }

    public void setGoalsAgainst(Integer goalsAgainst) {
        this.mGoalsAgainst = goalsAgainst;
    }


    public Match(String homeTeam, String opponent) {
        this.mHomeTeam = homeTeam;
        this.mOpponent = opponent;
    }

    public Match(String homeTeam, String opponent, int goalsFor, int goalsAgainst) {
        this.mHomeTeam = homeTeam;
        this.mOpponent = opponent;
        this.mGoalsFor = goalsFor;
        this.mGoalsAgainst = goalsAgainst;
    }

    public String getHomeTeam() { return mHomeTeam; }

    public void setHomeTeam(String team) {
        this.mHomeTeam = team;
    }
    public String getOpponent() { return mOpponent; }

    public void setOpponent(String op) {
        this.mOpponent = op;
    }
    public void setResult(Integer gf, Integer ga) {
        this.mGoalsFor = gf;
        this.mGoalsAgainst = ga;
    }

    public String getMatchString() {
        String resultString;

        if ((mGoalsFor == null) || (mGoalsAgainst == null)) {
            //resultString = mHomeTeam + " - " + mOpponent + ": - ";
            resultString = mHomeTeam + " - " + mOpponent;
        } else {
            //resultString = mHomeTeam + " - " + mOpponent + ": " + mGoalsFor + " - " + mGoalsAgainst;
            resultString = mHomeTeam + " - " + mOpponent;
        }

        return resultString;
    }

    public String getResultString() {
        String resultString;

        if ((mGoalsFor == null) || (mGoalsAgainst == null)) {
            resultString = " - ";
        } else {
            resultString = "" + mGoalsFor + " - " + mGoalsAgainst;
        }

        return resultString;
    }

}
