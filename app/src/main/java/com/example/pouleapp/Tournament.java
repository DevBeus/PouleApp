package com.example.pouleapp;

import java.util.ArrayList;
import java.util.UUID;

import static com.example.pouleapp.GlobalData.DEFAULT_POULE_NAME;

/**
 * Created by gezamenlijk on 21-5-2017.
 */

public class Tournament {
    private String mTournamentName;
    private String mLocation = "";
    private String mTournamentID;
    private ArrayList<Poule> mPouleList;

    public Tournament(String name) {
        mTournamentID = UUID.randomUUID().toString();
        mTournamentName = name;
        mPouleList = new ArrayList<>();
        mPouleList.add(new Poule(DEFAULT_POULE_NAME));
    }

    public Tournament(String name, ArrayList<Poule> pouleList) {
        mTournamentID = UUID.randomUUID().toString();
        mTournamentName = name;
        mPouleList = pouleList;
    }

    public Tournament(String id, String name, String location, ArrayList<Poule> pouleList) {
        mTournamentID = id;
        mTournamentName = name;
        mLocation = location;
        mPouleList = pouleList;
    }

    public String getTournamentName() {
        return mTournamentName;
    }

    public String getTournamentID() {
        return mTournamentID;
    }

    public void setTournamentName(String TournamentName) {
        this.mTournamentName = TournamentName;
    }

    public String getLocation() {
        return mLocation;
    }

    public void setLocation(String Location) {
        this.mLocation = Location;
    }

    public ArrayList<Poule> getPouleList() {
        return mPouleList;
    }

    public void setPouleList(ArrayList<Poule> PouleList) {
        this.mPouleList = PouleList;
    }

}
