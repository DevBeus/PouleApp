package com.example.pouleapp.Data;

/**
 * Created by gezamenlijk on 13-1-2018.
 * This class stores all info that is used for external searching
 */

public class PublishTournamentSearchInfo {

    private String mTournamentName;
    private String mLocation = "";
    private String mDate = "";
    private String mTournamentID;

    // empty constructor for firebase storage required
    public PublishTournamentSearchInfo() {
    }

    public PublishTournamentSearchInfo(String id, String name, String date, String location) {
        mTournamentID = id;
        mTournamentName = name;
        mDate = date;
        mLocation = location;
    }

    public String getLocation() {
        return mLocation;
    }

    public void setLocation(String location) {
        mLocation = location;
    }

    public String getDate() {
        return mDate;
    }

    public void setDate(String date) {
        mDate = date;
    }

    public String getTournamentID() {
        return mTournamentID;
    }

    public void setTournamentID(String tournamentID) {
        mTournamentID = tournamentID;
    }


    public String getTournamentName() {
        return mTournamentName;
    }

    public void setTournamentName(String tournamentName) {
        mTournamentName = tournamentName;
    }
}
