package com.example.pouleapp.Data;

import com.example.pouleapp.Data.Poule;

import java.util.ArrayList;
import java.util.UUID;

import static com.example.pouleapp.Data.GlobalData.DEFAULT_POULE_NAME;

/**
 * Created by gezamenlijk on 21-5-2017.
 * This class holds the complete Tournament
 */

public class Tournament {
    private String mTournamentName;
    private String mLocation = "";
    private String mDate = "";
    private Boolean mIsFullCompetition = false;
    private String mTournamentID;
    private ArrayList<Poule> mPouleList;

    public Tournament(String name) {
        mTournamentID = UUID.randomUUID().toString();
        mTournamentName = name;
        mPouleList = new ArrayList<>();
        mPouleList.add(new Poule(0,DEFAULT_POULE_NAME,mIsFullCompetition));
    }

    public Tournament(String name, ArrayList<Poule> pouleList) {
        mTournamentID = UUID.randomUUID().toString();
        mTournamentName = name;
        mPouleList = pouleList;
    }

    public Tournament(String id, String name, String location, String date, Boolean isFullComp, ArrayList<Poule> pouleList) {
        mTournamentID = id;
        mTournamentName = name;
        mLocation = location;
        mDate = date;
        mIsFullCompetition = isFullComp;
        mPouleList = pouleList;

        for (int i=0; i < mPouleList.size(); i++) { mPouleList.get(i).getPouleScheme().setFullCompetition(isFullComp); }
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

    public String getDate() {
        return mDate;
    }

    public void setDate(String Date) {
        this.mDate = Date;
    }

    public Boolean isFullCompetition() {
        return mIsFullCompetition;
    }

    public void setFullCompetition(Boolean b) {
        this.mIsFullCompetition = b;

        for (int i=0; i < mPouleList.size(); i++) { mPouleList.get(i).getPouleScheme().setFullCompetition(b); }
    }

    public ArrayList<Poule> getPouleList() {
        return mPouleList;
    }

    public void setPouleList(ArrayList<Poule> PouleList) {
        this.mPouleList = PouleList;
    }

}
