package com.example.pouleapp.Data;

/**
 * Created by gezamenlijk on 27-1-2018.
 */

public class PublishMatch {
    private String mHomeTeam = "";
    private String mOpponent = "";
    private Integer mGoalsFor;
    private Integer mGoalsAgainst;

    public PublishMatch() {
        //empty constructor for firebase
    }

    public PublishMatch(Match match) {
        mHomeTeam = match.getHomeTeam();
        mOpponent = match.getOpponent();
        mGoalsFor = match.getGoalsFor();
        mGoalsAgainst = match.getGoalsAgainst();
    }

    public String getHomeTeam() {
        return mHomeTeam;
    }

    public void setHomeTeam(String mHomeTeam) {
        this.mHomeTeam = mHomeTeam;
    }

    public String getOpponent() {
        return mOpponent;
    }

    public void setOpponent(String mOpponent) {
        this.mOpponent = mOpponent;
    }

    public Integer getGoalsFor() {
        return mGoalsFor;
    }

    public void setGoalsFor(Integer mGoalsFor) {
        this.mGoalsFor = mGoalsFor;
    }

    public Integer getGoalsAgainst() {
        return mGoalsAgainst;
    }

    public void setGoalsAgainst(Integer mGoalsAgainst) {
        this.mGoalsAgainst = mGoalsAgainst;
    }
}
