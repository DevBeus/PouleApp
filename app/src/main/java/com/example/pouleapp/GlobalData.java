package com.example.pouleapp;

import android.app.Application;
import android.content.SharedPreferences;

import java.util.ArrayList;


/**
 * Created by gezamenlijk on 21-1-2017.
 *
 * Viewing MyPrefsFile can be done with Android Device Manager (ADM)
 * - Phone must be rooted, can be done by executing adb root in folder \Android\sdk\platform-tools after emulator has started
 */

public class GlobalData extends Application {

    private ArrayList<Poule> mPouleList = new ArrayList<Poule>();

    private int selectedTeam = 0;

    public static final String MY_PREFS_NAME = "MyPrefsFile";
    public int getSelectedTeam() {
        return selectedTeam;
    }
    public void setSelectedTeam(int selectedTeam) {
        this.selectedTeam = selectedTeam;
    }
    // public ArrayList<Team> getTeamList() { return teamList; }
    public ArrayList<Poule> getPouleList() { return mPouleList; }

    public void initPouleList() {
        SharedPreferences prefs = getSharedPreferences(MY_PREFS_NAME, MODE_PRIVATE);
        int nrofPoules = prefs.getInt("nrofPoules",0);
        mPouleList.clear();

        if (nrofPoules == 0 ) {
            SharedPreferences.Editor editor = getSharedPreferences(MY_PREFS_NAME, MODE_PRIVATE).edit();
            String defaultPouleName = "Poule0";
            int defaultPouleIndex = 0;

            editor.putString("Poule0",defaultPouleName);
            nrofPoules = 1;
            editor.putInt("nrofPoules",nrofPoules);

            mPouleList.add(new Poule(defaultPouleName));

            editor.apply();

            savePoule(defaultPouleIndex);
        }
        else {

            for (int i=0; i < nrofPoules; i++) {
                initPoule(i);
            }
        }

        //Debug statement
        int x = prefs.getInt("nrofPoules",0);
        x = x + x;
    }

    public void initPoule(int pouleIndex) {
        SharedPreferences prefs = getSharedPreferences(MY_PREFS_NAME, MODE_PRIVATE);
        String pouleName = "Poule" + pouleIndex;

        ArrayList<Team> teamList = new ArrayList<Team>();

        int nrofTeams = prefs.getInt(pouleName+"nrofTeams", 0);
        //teamList.clear();

        if (nrofTeams == 0) {
            SharedPreferences.Editor editor = getSharedPreferences(MY_PREFS_NAME, MODE_PRIVATE).edit();
            editor.putString(pouleName+"Team0", "Team0");
            editor.putString(pouleName+"Team1", "Team1");
            editor.putInt(pouleName+"nrofTeams",2);
            nrofTeams = 2;

            editor.apply();
        }

        for (int i=0; i < nrofTeams; i++) {
            String teamstr= pouleName + "Team"+i;

            String teamName = prefs.getString(teamstr, ""); //Empty string is the default value.
            teamList.add(new Team(teamName));
        }

        PouleScheme pouleScheme = new PouleScheme(teamList);

        for (int i=0; i < nrofTeams; i++) {
            for (int j=0; j < nrofTeams; j++){
                String keystr1 = pouleName + "Match" + i + "-" + j + "goalsFor";
                String keystr2 = pouleName + "Match" + i + "-" + j + "goalsAgainst";

                int gf = prefs.getInt(keystr1, -1);
                int ga = prefs.getInt(keystr2, -1);

                if ((gf!=-1)&&(ga!=-1)) {
                    pouleScheme.updateMatch(teamList,i,j,gf,ga);
                }
            }
        }

        mPouleList.add(new Poule(pouleName,teamList,pouleScheme));
    }

    public void savePoule(int pouleIndex) {
        SharedPreferences.Editor editor = getSharedPreferences(MY_PREFS_NAME, MODE_PRIVATE).edit();
        String pouleName = mPouleList.get(pouleIndex).getPouleName();
        ArrayList<Team> teamList = mPouleList.get(pouleIndex).getTeamList();
        PouleScheme pouleScheme = mPouleList.get(pouleIndex).getPouleScheme();

        //if new poule is stored added nrofPoules need to be updated
        editor.putInt("nrofPoules",mPouleList.size());

        //Store info of selected poule:
        //- nrof teams in poule
        //- team list
        //- poule scheme

        editor.putInt(pouleName+"nrofTeams", teamList.size());

        for (int i = 0; i < teamList.size(); i++) {
            String teamstr = pouleName + "Team"+i;

            editor.putString(teamstr, teamList.get(i).getTeamName());
        }

        for (int i = 0; i < teamList.size(); i++) {
            for (int j = 0; j < teamList.size(); j++) {
                String keystr1 = pouleName + "Match" + i + "-" + j + "goalsFor";
                String keystr2 = pouleName + "Match" + i + "-" + j + "goalsAgainst";

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
