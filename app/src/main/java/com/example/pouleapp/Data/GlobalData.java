package com.example.pouleapp.Data;

import android.app.Application;
import android.content.SharedPreferences;
import android.widget.Toast;

import com.example.pouleapp.R;

import java.io.File;
import java.util.ArrayList;


/**
 * Created by gezamenlijk on 21-1-2017.
 *
 * Viewing MyPrefsFile can be done with Android Device Manager (ADM)
 * - Phone must be rooted, can be done by executing adb root in folder \Android\sdk\platform-tools after emulator has started
 */

public class GlobalData extends Application {
    public final static String POULE_INDEX = "com.example.pouleapp.POULEINDEX";
    public final static String TEAM_INDEX = "com.example.pouleapp.TEAMINDEX";
    public final static String SCHEME_ROW = "com.example.pouleapp.SCHEMEROW";
    public final static String SCHEME_COLUMN = "com.example.pouleapp.SCHEMECOLUMN";
    public final static String PREVIOUS_ACTIVITY = "com.example.pouleapp.PREVIOUSACTIVITY";

    public final static String SCHEME_ACTIVITY = "com.example.pouleapp.SCHEME";
    public final static String SCHEME_TABLE_ACTIVITY = "com.example.pouleapp.SCHEME_TABLE";

    public final static String ACTION_MESSAGE = "com.example.pouleapp.ACTIONMESSAGE";
    public final static String ACTION_ADD = "ADD";
    public final static String ACTION_EDIT = "EDIT";

    private Tournament mTournament;
    private ArrayList<String> mTournamentIDList = new ArrayList<>();
    private ArrayList<String> mTournamentNameList = new ArrayList<>();
    private int mSelectedTournamentIndex;
    private int mNrofTournaments;

    public Tournament getTournament() { return mTournament; }
    public ArrayList<String> getTournamentIDList() { return mTournamentIDList; }
    public ArrayList<String> getTournamentNameList() { return mTournamentNameList; }

    public static final String POULE_APP_PREFS = "PouleAppPrefs";
    public static final String DEFAULT_TOURNAMENT_NAME = "Tournament";
    public static final String DEFAULT_TOURNAMENT_ID = "0";
    public static final String DEFAULT_POULE_NAME = "PouleName";
    public static final String DEFAULT_TEAM_NAME = "Team";


    public void initApp() {
        SharedPreferences appPrefs = getSharedPreferences(POULE_APP_PREFS, MODE_PRIVATE);
        mNrofTournaments = appPrefs.getInt("nrofTournaments",0);

        // In case home action, ID and Name list need to be emptied first and data should be retrieved from preference file
        mTournamentIDList.clear();
        mTournamentNameList.clear();

        String selectedTournamentID = appPrefs.getString("SelectedTournamentID", DEFAULT_TOURNAMENT_ID);
        mSelectedTournamentIndex = 0;

        for (int i=0; i < mNrofTournaments; i++) {
            String id = appPrefs.getString("TournamentID"+i, DEFAULT_TOURNAMENT_ID);
            String name = appPrefs.getString("TournamentName"+i,DEFAULT_TOURNAMENT_NAME);

            mTournamentIDList.add(id);
            mTournamentNameList.add(name);

            if (selectedTournamentID.equals(id)) { mSelectedTournamentIndex = i; }
        }

        //Element added to create new tournament
        mTournamentIDList.add(DEFAULT_TOURNAMENT_ID);
        mTournamentNameList.add("Create new tournament..");

    }

    public void initTournament(String id) {
        mSelectedTournamentIndex = mTournamentIDList.indexOf(id);

        //retrieve selected tournament id and name
        SharedPreferences tournamentPrefs = getSharedPreferences(id, MODE_PRIVATE);
        String tournamentName = tournamentPrefs.getString("TournamentName", "Tournament");
        String location = tournamentPrefs.getString("Location", "New York");

        //retrieve pouleList information
        ArrayList<Poule> pouleList = initPouleList();

        mTournament = new Tournament(id, tournamentName, location, pouleList);

    }

    public ArrayList<Poule> initPouleList() {
        SharedPreferences prefs = getSharedPreferences(mTournamentIDList.get(mSelectedTournamentIndex), MODE_PRIVATE);
        int nrofPoules = prefs.getInt("nrofPoules",0);
        ArrayList<Poule> pouleList = new ArrayList<>();

        if (nrofPoules == 0 ) {
            SharedPreferences.Editor editor = getSharedPreferences(mTournamentIDList.get(mSelectedTournamentIndex), MODE_PRIVATE).edit();
            //String defaultPouleName = DEFAULT_POULE_NAME;
            int defaultPouleIndex = 0;

            editor.putString("Poule0",DEFAULT_POULE_NAME);
            nrofPoules = 1;
            editor.putInt("nrofPoules",nrofPoules);

            pouleList.add(new Poule(0,DEFAULT_POULE_NAME));

            editor.apply();

            savePoule(pouleList.get(defaultPouleIndex));
        }
        else {

            for (int i=0; i < nrofPoules; i++) {
                pouleList.add(initPoule(i));
            }
        }

        return pouleList;
    }



    public Poule initPoule(int pouleIndex) {
        SharedPreferences prefs = getSharedPreferences(mTournamentIDList.get(mSelectedTournamentIndex), MODE_PRIVATE);
        String pouleKey = "Poule" + pouleIndex;

        ArrayList<Team> teamList = new ArrayList<>();

        int nrofTeams = prefs.getInt(pouleKey+"nrofTeams", 0);

        if (nrofTeams == 0) {
            SharedPreferences.Editor editor = getSharedPreferences(mTournamentIDList.get(mSelectedTournamentIndex), MODE_PRIVATE).edit();
            editor.putString(pouleKey,DEFAULT_POULE_NAME);
            editor.putString(pouleKey+"Team0", "Team0");
            editor.putString(pouleKey+"Team1", "Team1");
            editor.putInt(pouleKey+"nrofTeams",2);
            nrofTeams = 2;

            editor.apply();
        }

        for (int i=0; i < nrofTeams; i++) {
            String teamstr= pouleKey + "Team"+i;

            String teamName = prefs.getString(teamstr, ""); //Empty string is the default value.
            teamList.add(new Team(teamName));
        }

        PouleScheme pouleScheme = new PouleScheme(teamList);

        for (int i=0; i < nrofTeams; i++) {
            for (int j=0; j < nrofTeams; j++){
                String keystr1 = pouleKey + "Match" + i + "-" + j + "goalsFor";
                String keystr2 = pouleKey + "Match" + i + "-" + j + "goalsAgainst";

                int gf = prefs.getInt(keystr1, -1);
                int ga = prefs.getInt(keystr2, -1);

                if ((gf!=-1)&&(ga!=-1)) {
                    pouleScheme.updateMatch(teamList,i,j,gf,ga);
                }
            }
        }

        String pouleName = prefs.getString(pouleKey, DEFAULT_POULE_NAME);

        Poule poule = new Poule(pouleIndex,pouleName,teamList,pouleScheme);

        return poule;
    }

    public String addTournament() {
        mTournament = new Tournament(DEFAULT_TOURNAMENT_NAME);

        String selectedTournamentID = mTournament.getTournamentID();

        //a new tournament is added at the beginning of the list at position 0
        mSelectedTournamentIndex = 0;
        mTournamentIDList.add(0,selectedTournamentID);
        mTournamentNameList.add(0,DEFAULT_TOURNAMENT_NAME);
        mNrofTournaments++;

        saveTournament();

        //Save App data
        SharedPreferences.Editor editor = getSharedPreferences(POULE_APP_PREFS, MODE_PRIVATE).edit();
        editor.putString("TournamentID"+mSelectedTournamentIndex,selectedTournamentID);
        editor.putString("TournamentName"+mSelectedTournamentIndex,DEFAULT_TOURNAMENT_NAME);
        editor.putString("SelectedTournamentID",selectedTournamentID);
        editor.putInt("nrofTournaments",mNrofTournaments);
        editor.apply();

        return selectedTournamentID;
    }

    public void deleteTournament(int index) {
        if (mTournamentIDList.get(index).equals(DEFAULT_TOURNAMENT_ID)) {
            String message = getResources().getString(R.string.tournament_creation_cannot_be_removed_message);
            Toast.makeText(getApplicationContext(), message, Toast.LENGTH_LONG).show();
        } else {
            //First SharedPrefsFile needs to be cleared (i.e. remove data from memory), and the prefs file needs to deleted
            SharedPreferences.Editor editor = getSharedPreferences(mTournamentIDList.get(index), MODE_PRIVATE).edit();
            editor.clear();

            String filePath = getApplicationContext().getFilesDir().getParent() + "/shared_prefs/" + mTournamentIDList.get(index) + ".xml";
            File deletePrefFile = new File(filePath);
            deletePrefFile.delete();

            mTournamentIDList.remove(index);
            mTournamentNameList.remove(index);
            mSelectedTournamentIndex = 0;
            mNrofTournaments--;
        }
    }

    public void updateTournamentName(String name) {
        mTournamentNameList.set(mSelectedTournamentIndex,name);
    }

    public void saveAppData() {
        SharedPreferences.Editor editor = getSharedPreferences(POULE_APP_PREFS, MODE_PRIVATE).edit();
        for (int i=0; i< mNrofTournaments; i++) {
            editor.putString("TournamentID"+i,mTournamentIDList.get(i));
            editor.putString("TournamentName"+i,mTournamentNameList.get(i));
        }

        editor.putString("SelectedTournamentID",mTournamentIDList.get(mSelectedTournamentIndex));
        editor.putInt("nrofTournaments",mNrofTournaments);
        editor.apply();
    }


    public void saveTournament() {
        SharedPreferences.Editor editor = getSharedPreferences(mTournament.getTournamentID(), MODE_PRIVATE).edit();

        editor.putString("TournamentID",mTournament.getTournamentID());
        editor.putString("TournamentName",mTournament.getTournamentName());
        editor.putString("Location",mTournament.getLocation());

        ArrayList<Poule> pouleList = mTournament.getPouleList();
        editor.putInt("nrofPoules",pouleList.size());

        for (int n=0; n < pouleList.size(); n++) {
            String pouleName = pouleList.get(n).getPouleName();
            ArrayList<Team> teamList = pouleList.get(n).getTeamList();
            PouleScheme pouleScheme = pouleList.get(n).getPouleScheme();

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
        }

        editor.apply();
    }

     public void savePoule(Poule poule) {
         //Not needed anymore
         SharedPreferences.Editor editor = getSharedPreferences(mTournament.getTournamentID(), MODE_PRIVATE).edit();
         String pouleKey = "Poule"+poule.getPouleNumber();
         String pouleName = poule.getPouleName();
         ArrayList<Team> teamList = poule.getTeamList();
         PouleScheme pouleScheme = poule.getPouleScheme();

        //if new poule is stored added nrofPoules need to be updated
        //editor.putInt("nrofPoules",mPouleList.size());

        //Store info of selected poule:
        //- poule name
        //- nrof teams in poule
        //- team list
        //- poule scheme

        editor.putString(pouleKey,pouleName);
        editor.putInt(pouleKey+"nrofTeams", teamList.size());


        for (int i = 0; i < teamList.size(); i++) {
            String teamstr = pouleKey + "Team"+i;

            editor.putString(teamstr, teamList.get(i).getTeamName());
        }

        for (int i = 0; i < teamList.size(); i++) {
            for (int j = 0; j < teamList.size(); j++) {
                String keystr1 = pouleKey + "Match" + i + "-" + j + "goalsFor";
                String keystr2 = pouleKey + "Match" + i + "-" + j + "goalsAgainst";

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



