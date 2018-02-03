package com.example.pouleapp.Data;

/**
 * Created by gezamenlijk on 27-1-2018.
 */

public class PublishTeam {
    private String mTeamName = "";
    private String mCoachName = "";

    public PublishTeam() {
        //empty constructor for firebase
    }

    public PublishTeam(Team team) {
        mTeamName = team.getTeamName();
        mCoachName = team.getCoachName();
    }

    public String getTeamName() {
        return mTeamName;
    }

    public void setTeamName(String teamName) {
        this.mTeamName = teamName;
    }

    public String getCoachName() {
        return mCoachName;
    }

    public void setCoachName(String coachName) {
        this.mCoachName = coachName;
    }
}
