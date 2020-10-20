package com.example.pouleapp.data;

import com.google.gson.annotations.SerializedName;

/**
 * Created by gezamenlijk on 13-1-2018.
 * This class stores all info that is used for external searching
 */

public class TournamentInfo {
    @SerializedName("tournament_name")
    private String mTournamentName;

    @SerializedName("tournament_ID")
    private String mTournamentID;

    public TournamentInfo(String id, String name) {
        mTournamentID = id;
        mTournamentName = name;
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
