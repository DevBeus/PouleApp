package com.example.pouleapp;

/**
 * Created by gezamenlijk on 11-2-2017.
 */

public class Match {
    private String homeTeam = "";
    private String opponent = "";

    public Integer getGoalsFor() {
        return goalsFor;
    }

    public void setGoalsFor(Integer goalsFor) {
        this.goalsFor = goalsFor;
    }

    public Integer getGoalsAgainst() {
        return goalsAgainst;
    }

    public void setGoalsAgainst(Integer goalsAgainst) {
        this.goalsAgainst = goalsAgainst;
    }

    private Integer goalsFor;
    private Integer goalsAgainst;

    public Match(String homeTeam, String opponent) {
        this.homeTeam = homeTeam;
        this.opponent = opponent;
    }

    public Match(String homeTeam, String opponent, int goalsFor, int goalsAgainst) {
        this.homeTeam = homeTeam;
        this.opponent = opponent;
        this.goalsFor = goalsFor;
        this.goalsAgainst = goalsAgainst;
    }

    public String getHomeTeam() { return homeTeam; }
    public String getOpponent() { return opponent; }

    public void setResult(Integer gf, Integer ga) {
        this.goalsFor = gf;
        this.goalsAgainst = ga;
    }

    public String getMatchString() {
        String resultString;

        if ((goalsFor == null) || (goalsAgainst == null)) {
            resultString = homeTeam + " - " + opponent + ": - ";
        } else {
            resultString = homeTeam + " - " + opponent + ": " + goalsFor + " - " + goalsAgainst;
        }


        return resultString;
    }

    public String getResultString() {
        String resultString;

        if ((goalsFor == null) || (goalsAgainst == null)) {
            resultString = " - ";
        } else {
            resultString = "" + goalsFor + " - " + goalsAgainst;
        }

        return resultString;
    }

}
