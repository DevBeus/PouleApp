package com.example.pouleapp.Data;

import java.util.ArrayList;
import java.util.UUID;

import static com.example.pouleapp.Data.GlobalData.DEFAULT_POULE_NAME;

/**
 * Created by gezamenlijk on 21-5-2017.
 * This class holds the complete Tournament
 */

public class Tournament {
//    private String mTournamentName;
//    private String mLocation = "";
//    private String mDate = "";
//    private String mTournamentID;


    private PublishTournamentSearchInfo mTournamentInfo;


    private Boolean mIsPublished = false;
    private Boolean mIsFullCompetition = false;
    private ArrayList<Poule> mPouleList;

    public Tournament() {
        // empty constructor needed for firebase storage
    }

    public Tournament(String name) {
        mTournamentInfo = new PublishTournamentSearchInfo(UUID.randomUUID().toString(), name, "", "");

        mPouleList = new ArrayList<>();
        mPouleList.add(new Poule(0,DEFAULT_POULE_NAME,mIsFullCompetition));
    }

    public Tournament(String name, ArrayList<Poule> pouleList) {
        mTournamentInfo = new PublishTournamentSearchInfo(UUID.randomUUID().toString(), name, "", "");

        mPouleList = pouleList;
    }

    public Tournament(String id, String name, String location, String date, Boolean isFullComp, ArrayList<Poule> pouleList) {
        mTournamentInfo = new PublishTournamentSearchInfo(id, name, date, location);

        mIsFullCompetition = isFullComp;
        mPouleList = pouleList;

        for (int i=0; i < mPouleList.size(); i++) { mPouleList.get(i).getPouleScheme().setFullCompetition(isFullComp); }
    }

    public PublishTournamentSearchInfo getTournamentInfo() {
        return mTournamentInfo;
    }

    public String getTournamentName() {
        return mTournamentInfo.getTournamentName();
    }

    public String getTournamentID() {

        return mTournamentInfo.getTournamentID();
    }

    public void setTournamentID(String x) {

        mTournamentInfo.setTournamentID(x);
    }

    public void setTournamentName(String TournamentName) {
        this.mTournamentInfo.setTournamentName(TournamentName);
    }

    public String getLocation() {
        return mTournamentInfo.getLocation();
    }

    public void setLocation(String Location) {
        this.mTournamentInfo.setLocation(Location);
    }

    public String getDate() {

        return mTournamentInfo.getDate();
    }

    public void setDate(String Date) {
        this.mTournamentInfo.setDate(Date);
    }

    public Boolean isPublished() {
        return mIsPublished;
    }

    public void setIsPublished(Boolean mIsPublished) {
        this.mIsPublished = mIsPublished;
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
