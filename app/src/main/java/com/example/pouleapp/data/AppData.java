package com.example.pouleapp.data;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

public class AppData {
    @SerializedName("selected_tournament_index")
    private int mSelectedTournamentIndex = 0;

    @SerializedName("tournament_info_list")
    private ArrayList<TournamentInfo> mTournamentInfoList;

    public AppData(int n, ArrayList<TournamentInfo> list ) {
        mSelectedTournamentIndex = n;
        mTournamentInfoList = list;
    }

    public int getSelectedTournamentIndex() {
        return mSelectedTournamentIndex;
    }
    public void setSelectedTournamentIndex(int tournamentIndex) { mSelectedTournamentIndex = tournamentIndex; }

    public ArrayList<TournamentInfo> getTournamentInfoList() {
        return mTournamentInfoList;
    }
    public void setTournamentInfoList(ArrayList<TournamentInfo> tournamentInfoList) { mTournamentInfoList = tournamentInfoList; }
    public int getNrofTournaments() { return mTournamentInfoList.size();  }

    public void setTournamentName(String id, String name) {

        for (int i=0; i < mTournamentInfoList.size(); i++) {
            if (mTournamentInfoList.get(i).getTournamentID().equals(id)) {
                mTournamentInfoList.get(i).setTournamentName(name);
                i = mTournamentInfoList.size();
            }
        }
    }

    public void addTournament(String tournamentID, String tournamentName) {
        //a new tournament is added at the beginning of the list at position 0
        mTournamentInfoList.add(0, new TournamentInfo(tournamentID,tournamentName));
    }

    public void deleteTournament(String tournamentID) {

        for(int i=0; i<mTournamentInfoList.size(); i++) {
            if (mTournamentInfoList.get(i).getTournamentID().equals(tournamentID)) { mTournamentInfoList.remove(i); }
        }

    }

}
