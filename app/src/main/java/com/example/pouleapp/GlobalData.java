package com.example.pouleapp;

import android.app.Application;
import android.content.SharedPreferences;

import java.util.ArrayList;


/**
 * Created by gezamenlijk on 21-1-2017.
 */

public class GlobalData extends Application {

    private ArrayList<Team> poule = new ArrayList<Team>();
    private PouleScheme pouleScheme;

    private int selectedTeam = 0;

    public static final String MY_PREFS_NAME = "MyPrefsFile";
    public int getSelectedTeam() {
        return selectedTeam;
    }
    public void setSelectedTeam(int selectedTeam) {
        this.selectedTeam = selectedTeam;
    }
    public ArrayList<Team> getPoule() {
        return poule;
    }
    public PouleScheme getPouleScheme() {
        return pouleScheme;
    }

    public void initPoule() {
        SharedPreferences prefs = getSharedPreferences(MY_PREFS_NAME, MODE_PRIVATE);
        int nrofTeams = prefs.getInt("nrofTeams", 0);
        poule.clear();

        if (nrofTeams == 0) {
            SharedPreferences.Editor editor = getSharedPreferences(MY_PREFS_NAME, MODE_PRIVATE).edit();
            editor.putString("Team0", "Team0");
            editor.putString("Team1", "Team1");
            editor.putInt("nrofTeams",2);

            editor.apply();
        }

        for (int i=0; i < nrofTeams; i++) {
            String teamstr= "Team"+i;

            String teamName = prefs.getString(teamstr, ""); //Empty string is the default value.
            poule.add(new Team(teamName));
        }

        pouleScheme = new PouleScheme(poule);

        for (int i=0; i < nrofTeams; i++) {
            for (int j=0; j < nrofTeams; j++){
                String keystr1 = "Match" + i + "-" + j + "goalsFor";
                String keystr2 = "Match" + i + "-" + j + "goalsAgainst";

                int gf = prefs.getInt(keystr1, -1);
                int ga = prefs.getInt(keystr2, -1);

                if ((gf!=-1)&&(ga!=-1)) {
                    pouleScheme.updateMatch(poule,i,j,gf,ga);
                }
            }
        }
    }

    public void savePoule() {
        SharedPreferences prefs = getSharedPreferences(MY_PREFS_NAME, MODE_PRIVATE);
        SharedPreferences.Editor editor = getSharedPreferences(MY_PREFS_NAME, MODE_PRIVATE).edit();

        //First clear current Shared Preferences
        editor.clear();
        editor.commit();

        editor.putInt("nrofTeams", poule.size());

        for (int i = 0; i < poule.size(); i++) {
            String teamstr = "Team"+i;

            editor.putString(teamstr, poule.get(i).getTeamName());
        }

        if (pouleScheme.getPouleSize() < poule.size()) {
            //If new team was added, new pouleScheme should be created and data needs to be copied
            PouleScheme pouleSchemeNew = new PouleScheme(poule);
            for (int i = 0; i < pouleScheme.getPouleSize(); i++) {
                for (int j = 0; j < pouleScheme.getPouleSize(); j++) {
                    pouleSchemeNew.setMatch(i,j,pouleScheme.getMatch(i,j));
                }
            }

            pouleScheme = pouleSchemeNew;

        }

        for (int i = 0; i < poule.size(); i++) {
            for (int j = 0; j < poule.size(); j++) {
                String keystr1 = "Match" + i + "-" + j + "goalsFor";
                String keystr2 = "Match" + i + "-" + j + "goalsAgainst";

                Integer gf = pouleScheme.getMatchGoalsFor(i, j);
                Integer ga = pouleScheme.getMatchGoalsAgainst(i, j);

                if ((gf != null) && (ga != null)) {
                    editor.putInt(keystr1, gf);
                    editor.putInt(keystr2, ga);
                }
            }
        }

        editor.apply();
    }
}
