package com.example.pouleapp.Data;

import java.util.ArrayList;

/**
 * Created by gezamenlijk on 23-4-2017.
 */

public class Poule {

    // A poule is a combination of:
    // - poule name
    // - list of teams
    // - Poule Scheme (containing all matches and scores)
    private ArrayList<Team> mTeamList = new ArrayList<>();
    private PouleScheme mPouleScheme;
    private String mPouleName;
    private int mPouleNumber;

    public Poule(int number, String name) {
        this.mPouleNumber = number;
        this.mPouleName = name;

        //initialize poule with 2 dummy teams
        mTeamList.add(new Team("Team0"));
        mTeamList.add(new Team("Team1"));

        //initialize mPouleScheme
        mPouleScheme = new PouleScheme(mTeamList);
    }

    public Poule(int number, String name, ArrayList<Team> teams, PouleScheme ps ) {
        this.mPouleNumber = number;
        this.mPouleName = name;
        this.mTeamList = teams;
        this.mPouleScheme = ps;
    }

    public int getPouleNumber() { return mPouleNumber; }
    public String getPouleName() { return mPouleName; }
    public void setPouleName(String name) { this.mPouleName = name; }

    public PouleScheme getPouleScheme() { return mPouleScheme; }
    public void setPouleScheme(PouleScheme ps) { this.mPouleScheme = ps;}

    public ArrayList<Team> getTeamList() { return mTeamList; }
    public void setTeamList(ArrayList<Team> teams ) { this.mTeamList = teams; }

    public void addTeam( String teamName ) {
        mTeamList.add(new Team(teamName));
        mPouleScheme.addTeam(teamName);

    }

    public void deleteTeam( int deleteTeam ) {

        // All results of selected team need to be removed first and team needs to be removed afterwards
        mPouleScheme.deleteTeam(mTeamList, deleteTeam);
        mTeamList.remove(deleteTeam);
    }
}
