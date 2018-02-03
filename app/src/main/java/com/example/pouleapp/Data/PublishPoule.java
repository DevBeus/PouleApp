package com.example.pouleapp.Data;

import java.util.ArrayList;

/**
 * Created by gezamenlijk on 27-1-2018.
 */

public class PublishPoule {
    private String mPublishPouleName;
    private ArrayList<PublishTeam> mPublishTeamList = new ArrayList<>();
    private ArrayList<PublishRound> mPublishRoundList = new ArrayList<>();

    public PublishPoule() {
        //empty constructor for firebase
    }

    public PublishPoule(Poule poule) {
        mPublishPouleName = poule.getPouleName();

        for (Team team: poule.getTeamList()) {
            mPublishTeamList.add(new PublishTeam(team));
        }

        int r = poule.getPouleScheme().getNumberOfRounds();

        for(int i=1; i<r; i++) {
            mPublishRoundList.add(new PublishRound(poule.getPouleScheme(),i));
        }

    }

    public String getPouleName() {
        return mPublishPouleName;
    }

    public void setPouleName(String pouleName) {
        this.mPublishPouleName = pouleName;
    }

    public ArrayList<PublishTeam> getTeamList() {
        return mPublishTeamList;
    }

    public void setTeamList(ArrayList<PublishTeam> teamList) {
        this.mPublishTeamList = teamList;
    }

    public ArrayList<PublishRound> getRoundList() {
        return mPublishRoundList;
    }

    public void setRoundList(ArrayList<PublishRound> roundList) {
        this.mPublishRoundList = roundList;
    }
}
