package com.example.pouleapp.Data;

import java.util.ArrayList;

/**
 * Created by gezamenlijk on 27-1-2018.
 */

public class PublishTournament {
    private PublishTournamentSearchInfo mTournamentInfo;
    private ArrayList<PublishPoule> mPublishPouleList = new ArrayList<>();

    public PublishTournament() {
        //empty constructor for firebase
    }

    public PublishTournament(PublishTournamentSearchInfo info) {
        mTournamentInfo = info;
    }


    public PublishTournament(Tournament tournament) {
        mTournamentInfo = tournament.getTournamentInfo();

        for (Poule poule: tournament.getPouleList()) {
            mPublishPouleList.add(new PublishPoule(poule));
        }
    }

    public PublishTournamentSearchInfo getTournamentInfo() {
        return mTournamentInfo;
    }

    public void setTournamentInfo(PublishTournamentSearchInfo tournamentInfo) {
        this.mTournamentInfo = tournamentInfo;
    }

    public ArrayList<PublishPoule> getPouleList() {
        return mPublishPouleList;
    }

    public void setPouleList(ArrayList<PublishPoule> pouleList) {
        this.mPublishPouleList = pouleList;
    }
}
