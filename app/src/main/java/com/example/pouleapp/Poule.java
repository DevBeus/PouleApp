package com.example.pouleapp;

import java.util.ArrayList;

/**
 * Created by gezamenlijk on 23-4-2017.
 */

public class Poule {

    // A poule is a combination of:
    // - poule name
    // - list of teams
    // - Poule Scheme (containing all matches and scores)
    private ArrayList<Team> teamList = new ArrayList<Team>();
    private PouleScheme pouleScheme;
    private String pouleName;

    public Poule(String name) {
        this.pouleName = name;

        //initialize poule with 2 dummy teams
        teamList.add(new Team("Team0"));
        teamList.add(new Team("Team1"));

        //initialize pouleScheme
        pouleScheme = new PouleScheme(teamList);
    }

    public Poule(String name, ArrayList<Team> teams, PouleScheme ps ) {
        this.pouleName = name;
        this.teamList = teams;
        this.pouleScheme = ps;
    }

    public String getPouleName() { return pouleName; }
    public void setPouleName(String name) { this.pouleName = name; }

    public PouleScheme getPouleScheme() { return pouleScheme; }
    public void setPouleScheme(PouleScheme ps) { this.pouleScheme = ps;}

    public ArrayList<Team> getTeamList() { return teamList; }
    public void setTeamList(ArrayList<Team> teams ) { this.teamList = teams; }
}
