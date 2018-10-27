package com.example.pouleapp.data;

import android.app.Application;
import android.content.SharedPreferences;
import android.widget.Toast;

import com.example.pouleapp.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

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
    public final static String TAB_INDEX = "com.example.pouleapp.TABINDEX";
    public final static int SCHEME_TAB = 0;
    public final static int RANKING_TAB = 1;
    public final static int SCHEMETABLE_TAB = 2;

    public final static String NEXT_ACTIVITY = "Activity";
    public final static String ACTIVITY_SELECT_TOURNAMENT = "SelectTournament";
    public final static String ACTIVITY_FOLLOW_TOURNAMENT = "FollowTournament";

    public final static String SCHEME_ROW = "com.example.pouleapp.SCHEMEROW";
    public final static String SCHEME_COLUMN = "com.example.pouleapp.SCHEMECOLUMN";
    public final static String PREVIOUS_ACTIVITY = "com.example.pouleapp.PREVIOUSACTIVITY";
    public final static String PREVIOUS_TAB = "com.example.pouleapp.PREVIOUSTAB";

    public final static String SCHEME_ACTIVITY = "com.example.pouleapp.SCHEME";
    public final static String SCHEME_TABLE_ACTIVITY = "com.example.pouleapp.SCHEME_TABLE";

    public final static String ACTION_MESSAGE = "com.example.pouleapp.ACTIONMESSAGE";
    public final static String ACTION_ADD = "ADD";
    public final static String ACTION_EDIT = "EDIT";

    private Tournament mTournament;
    private PublishTournament mPublishTournament;
    private ArrayList<String> mTournamentIDList = new ArrayList<>();
    private ArrayList<String> mTournamentNameList = new ArrayList<>();
    private int mSelectedTournamentIndex;
    private int mNrofTournaments;

    private FirebaseAuth mFBAuth;
    private FirebaseUser mFBUser;

    private FirebaseDatabase mCupAppDB;

    public FirebaseAuth getFirebaseAuth() { return mFBAuth; }
    public void setFirebaseAuth(FirebaseAuth au) {
        mFBAuth = au;
    }

    public FirebaseUser getFirebaseUser() { return mFBUser; }
    public void setFirebaseUser(FirebaseUser user ) {
        mFBUser = user;
    }

    public FirebaseDatabase getFirebaseDatabase() { return mCupAppDB; }
    public void setFirebaseDatabase(FirebaseDatabase db) {
        mCupAppDB = db;
    }

    public Tournament getTournament() { return mTournament; }

    public PublishTournament getPublishTournament() { return mPublishTournament; }
    public void setPublishTournament(PublishTournament p) {
        mPublishTournament = p;
    }

    public ArrayList<String> getTournamentIDList() { return mTournamentIDList; }
    public ArrayList<String> getTournamentNameList() { return mTournamentNameList; }

    public static final String KEY_TOURNAMENT_NAME = "TournamentName";

    public static final String KEY_TOURNAMENT_INFO_LIST = "TournamentsInfo";
    public static final String KEY_TOURNAMENT_LIST = "Tournaments";
    private static final String KEY_TOURNAMENT_ID = "TournamentID";
    private static final String KEY_NROF_TOURNAMENTS = "nrofTournaments";
    private static final String KEY_SELECTED_TOURNAMENT_ID = "SelectedTournamentID";
    public static final String KEY_LOCATION = "Location";
    public static final String KEY_DATE = "Date";

    public static final String KEY_POULES = "Poules";
    public static final String KEY_POULE_NAME = "PouleName";
    public static final String KEY_NROF_POULES = "nrofPoules";
    public static final String KEY_POULE = "Poule";
    public static final String KEY_TEAMS = "Teams";
    public static final String KEY_NROF_TEAMS = "nrofTeams";
    public static final String KEY_TEAM = "Team";
    public static final String KEY_MATCHES = "Matches";
    public static final String KEY_MATCH = "Match";
    public static final String KEY_GOALS_FOR = "goalsFor";
    public static final String KEY_GOALS_AGAINST = "goalsAgainst";
    public static final String KEY_COMPETITION = "FullCompetition";
    public static final String KEY_COACH = "Coach";

    private static final String POULE_APP_PREFS = "PouleAppPrefs";
    private static final String DEFAULT_TOURNAMENT_NAME = "Tournament";
    private static final String DEFAULT_TOURNAMENT_LOCATION = "New York";
    private static final String DEFAULT_TOURNAMENT_DATE = "";
    private static final String DEFAULT_TOURNAMENT_ID = "0";

    public static final String DEFAULT_POULE_NAME = "PouleName";
    public static final String DEFAULT_TEAM_NAME = "Team";

    public void initApp() {
        SharedPreferences appPrefs = getSharedPreferences(POULE_APP_PREFS, MODE_PRIVATE);
        mNrofTournaments = appPrefs.getInt(KEY_NROF_TOURNAMENTS,0);

        mCupAppDB = FirebaseDatabase.getInstance();

        // In case home action, ID and Name list need to be emptied first and data should be retrieved from preference file
        mTournamentIDList.clear();
        mTournamentNameList.clear();

        String selectedTournamentID = appPrefs.getString(KEY_SELECTED_TOURNAMENT_ID, DEFAULT_TOURNAMENT_ID);
        mSelectedTournamentIndex = 0;

        for (int i=0; i < mNrofTournaments; i++) {
            String id = appPrefs.getString(KEY_TOURNAMENT_ID+i, DEFAULT_TOURNAMENT_ID);
            String name = appPrefs.getString(KEY_TOURNAMENT_NAME+i,DEFAULT_TOURNAMENT_NAME);

            mTournamentIDList.add(id);
            mTournamentNameList.add(name);

            if (selectedTournamentID.equals(id)) { mSelectedTournamentIndex = i; }
        }

    }

    public void initTournament(String id) {
        mSelectedTournamentIndex = mTournamentIDList.indexOf(id);

        //retrieve selected tournament id and name
        SharedPreferences tournamentPrefs = getSharedPreferences(id, MODE_PRIVATE);
        String tournamentName = tournamentPrefs.getString(KEY_TOURNAMENT_NAME, DEFAULT_TOURNAMENT_NAME);
        String location = tournamentPrefs.getString(KEY_LOCATION, DEFAULT_TOURNAMENT_LOCATION);
        String date = tournamentPrefs.getString(KEY_DATE, DEFAULT_TOURNAMENT_DATE);
        Boolean isFullComp = tournamentPrefs.getBoolean(KEY_COMPETITION, false);

        //retrieve pouleList information
        ArrayList<Poule> pouleList = initPouleList();

        mTournament = new Tournament(id, tournamentName, location, date, isFullComp, pouleList);

    }

    public ArrayList<Poule> initPouleList() {
        SharedPreferences prefs = getSharedPreferences(mTournamentIDList.get(mSelectedTournamentIndex), MODE_PRIVATE);
        int nrofPoules = prefs.getInt(KEY_NROF_POULES,0);
        ArrayList<Poule> pouleList = new ArrayList<>();

        if (nrofPoules == 0 ) {
            SharedPreferences.Editor editor = getSharedPreferences(mTournamentIDList.get(mSelectedTournamentIndex), MODE_PRIVATE).edit();
            //String defaultPouleName = DEFAULT_POULE_NAME;
            int defaultPouleIndex = 0;

            editor.putString(KEY_POULE+defaultPouleIndex,DEFAULT_POULE_NAME);
            nrofPoules = 1;
            editor.putInt(KEY_NROF_POULES,nrofPoules);

            pouleList.add(new Poule(0,DEFAULT_POULE_NAME,false));

            editor.apply();
            editor.commit();

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
        String pouleKey = KEY_POULE + pouleIndex;

        ArrayList<Team> teamList = new ArrayList<>();

        int nrofTeams = prefs.getInt(pouleKey+KEY_NROF_TEAMS, 0);

        if (nrofTeams == 0) {
            SharedPreferences.Editor editor = getSharedPreferences(mTournamentIDList.get(mSelectedTournamentIndex), MODE_PRIVATE).edit();
            editor.putString(pouleKey,DEFAULT_POULE_NAME);
            editor.putString(pouleKey+KEY_TEAM+"0", DEFAULT_TEAM_NAME+"0");
            editor.putString(pouleKey+KEY_TEAM+"1", DEFAULT_TEAM_NAME+"1");
            editor.putInt(pouleKey+KEY_NROF_TEAMS,2);
            nrofTeams = 2;

            editor.apply();
            editor.commit();
        }

        for (int i=0; i < nrofTeams; i++) {
            String teamKeystr= pouleKey + KEY_TEAM+i;

            String teamName = prefs.getString(teamKeystr, ""); //Empty string is the default value.
            String coachName = prefs.getString(teamKeystr + KEY_COACH, ""); //Empty string is the default value.
            teamList.add(new Team(teamName,coachName));
        }

        PouleScheme pouleScheme = new PouleScheme(teamList, prefs.getBoolean(KEY_COMPETITION,false));

        for (int i=0; i < nrofTeams; i++) {
            for (int j=0; j < nrofTeams; j++){
                String keystr1 = pouleKey + KEY_MATCH + i + "-" + j + KEY_GOALS_FOR;
                String keystr2 = pouleKey + KEY_MATCH + i + "-" + j + KEY_GOALS_AGAINST;

                int gf = prefs.getInt(keystr1, -1);
                int ga = prefs.getInt(keystr2, -1);

                if ((gf!=-1)&&(ga!=-1)) {
                    pouleScheme.updateMatch(teamList,i,j,gf,ga);
                }
            }
        }

        String pouleName = prefs.getString(pouleKey, DEFAULT_POULE_NAME);

        return new Poule(pouleIndex,pouleName,teamList,pouleScheme);
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
        editor.putString(KEY_TOURNAMENT_ID+mSelectedTournamentIndex,selectedTournamentID);
        editor.putString(KEY_TOURNAMENT_NAME+mSelectedTournamentIndex,DEFAULT_TOURNAMENT_NAME);
        editor.putString(KEY_SELECTED_TOURNAMENT_ID,selectedTournamentID);
        editor.putInt(KEY_NROF_TOURNAMENTS,mNrofTournaments);
        editor.apply();
        editor.commit();

        return selectedTournamentID;
    }

    public String addTournament(String tournamentName) {
        mTournament = new Tournament(tournamentName);

        String selectedTournamentID = mTournament.getTournamentID();

        //a new tournament is added at the beginning of the list at position 0
        mSelectedTournamentIndex = 0;
        mTournamentIDList.add(0,selectedTournamentID);
        mTournamentNameList.add(0,tournamentName);
        mNrofTournaments++;

        saveTournament();

        //Save App data
        SharedPreferences.Editor editor = getSharedPreferences(POULE_APP_PREFS, MODE_PRIVATE).edit();
        editor.putString(KEY_TOURNAMENT_ID+mSelectedTournamentIndex,selectedTournamentID);
        editor.putString(KEY_TOURNAMENT_NAME+mSelectedTournamentIndex,tournamentName);
        editor.putString(KEY_SELECTED_TOURNAMENT_ID,selectedTournamentID);
        editor.putInt(KEY_NROF_TOURNAMENTS,mNrofTournaments);
        editor.apply();
        editor.commit();

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
            editor.putString(KEY_TOURNAMENT_ID+i,mTournamentIDList.get(i));
            editor.putString(KEY_TOURNAMENT_NAME+i,mTournamentNameList.get(i));
        }

        if (mNrofTournaments > 0) {
            editor.putString(KEY_SELECTED_TOURNAMENT_ID,mTournamentIDList.get(mSelectedTournamentIndex));

            //mDBTournamentsRef.child(KEY_SELECTED_TOURNAMENT_ID).setValue(mTournamentIDList.get(mSelectedTournamentIndex));
        }
        
        editor.putInt(KEY_NROF_TOURNAMENTS,mNrofTournaments);
        editor.apply();
        editor.commit();

        //mDBTournamentsRef.child(KEY_NROF_TOURNAMENTS).setValue(mNrofTournaments);
    }

    public void saveTournament() {
        SharedPreferences.Editor editor = getSharedPreferences(mTournament.getTournamentID(), MODE_PRIVATE).edit();

        editor.clear(); // First clear shared preferences completely to avoid left overs

        editor.putString(KEY_TOURNAMENT_ID,mTournament.getTournamentID());
        editor.putString(KEY_TOURNAMENT_NAME,mTournament.getTournamentName());
        editor.putString(KEY_LOCATION,mTournament.getLocation());
        editor.putString(KEY_DATE,mTournament.getDate());
        editor.putBoolean(KEY_COMPETITION,mTournament.isFullCompetition());

        ArrayList<Poule> pouleList = mTournament.getPouleList();
        editor.putInt(KEY_NROF_POULES,pouleList.size());

        for (int n=0; n < pouleList.size(); n++) {
            Poule poule = pouleList.get(n);
            String pouleName = poule.getPouleName();
            ArrayList<Team> teamList = pouleList.get(n).getTeamList();
            PouleScheme pouleScheme = pouleList.get(n).getPouleScheme();
            String pouleKey = KEY_POULE+poule.getPouleNumber();

            //Store info of selected poule:
            //- poule name
            //- nrof teams in poule
            //- team list
            //- poule scheme

            editor.putString(pouleKey,pouleName);
            editor.putInt(pouleKey+KEY_NROF_TEAMS, teamList.size());

            for (int i = 0; i < teamList.size(); i++) {
                String teamKey = pouleKey + KEY_TEAM+i;

                editor.putString(teamKey, teamList.get(i).getTeamName());
                editor.putString(teamKey + KEY_COACH,teamList.get(i).getCoachName());

            }

            int r = poule.getPouleScheme().getNumberOfRounds();

            for (int i=1; i < r; i++) {
                Match[] matchList = poule.getPouleScheme().getRoundMatchList(i);

                for (int m = 0; m < matchList.length; m++) {
                    String hTeam = matchList[m].getHomeTeam();
                    String oTeam = matchList[m].getOpponent();

                    int x = 0;
                    int y = 0;

                    for(int p = 0; p < teamList.size(); p++){
                        Team t = teamList.get(p);

                        if (t.getTeamName().equals(hTeam)) { x = p;}
                        if (t.getTeamName().equals(oTeam)) { y = p;}
                    }

                    String keystr1 = pouleKey + KEY_MATCH + x + "-" + y + KEY_GOALS_FOR;
                    String keystr2 = pouleKey + KEY_MATCH + x + "-" + y + KEY_GOALS_AGAINST;

                    Integer gf = pouleScheme.getMatchGoalsFor(x, y);
                    Integer ga = pouleScheme.getMatchGoalsAgainst(x, y);

                    if ((gf != null) && (ga != null)) {
                        editor.putInt(keystr1, gf);
                        editor.putInt(keystr2, ga);

                    }


                }
            }
      }

        editor.apply();
        editor.commit();
    }

    public void saveTournament1() {
        SharedPreferences.Editor editor = getSharedPreferences(mTournament.getTournamentID(), MODE_PRIVATE).edit();

        editor.clear(); // First clear shared preferences completely to avoid left overs

        editor.putString(KEY_TOURNAMENT_ID,mTournament.getTournamentID());
        editor.putString(KEY_TOURNAMENT_NAME,mTournament.getTournamentName());
        editor.putString(KEY_LOCATION,mTournament.getLocation());
        editor.putString(KEY_DATE,mTournament.getDate());
        editor.putBoolean(KEY_COMPETITION,mTournament.isFullCompetition());

        ArrayList<Poule> pouleList = mTournament.getPouleList();
        editor.putInt(KEY_NROF_POULES,pouleList.size());

        for (int n=0; n < pouleList.size(); n++) {
            Poule poule = pouleList.get(n);
            String pouleName = poule.getPouleName();
            ArrayList<Team> teamList = pouleList.get(n).getTeamList();
            PouleScheme pouleScheme = pouleList.get(n).getPouleScheme();
            String pouleKey = KEY_POULE+poule.getPouleNumber();

            //Store info of selected poule:
            //- poule name
            //- nrof teams in poule
            //- team list
            //- poule scheme

            editor.putString(pouleKey,pouleName);
            editor.putInt(pouleKey+KEY_NROF_TEAMS, teamList.size());

            for (int i = 0; i < teamList.size(); i++) {
                String teamKey = pouleKey + KEY_TEAM+i;

                editor.putString(teamKey, teamList.get(i).getTeamName());
                editor.putString(teamKey + KEY_COACH,teamList.get(i).getCoachName());
            }

            for (int i = 0; i < teamList.size(); i++) {
                for (int j = 0; j < teamList.size(); j++) {
                    String keystr1 = pouleKey + KEY_MATCH + i + "-" + j + KEY_GOALS_FOR;
                    String keystr2 = pouleKey + KEY_MATCH + i + "-" + j + KEY_GOALS_AGAINST;

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
        editor.commit();
    }

     public void savePoule(Poule poule) {
         //Not needed anymore
         SharedPreferences.Editor editor = getSharedPreferences(mTournament.getTournamentID(), MODE_PRIVATE).edit();
         String pouleKey = KEY_POULE+poule.getPouleNumber();
         String pouleName = poule.getPouleName();
         ArrayList<Team> teamList = poule.getTeamList();
         PouleScheme pouleScheme = poule.getPouleScheme();

        //if new poule is stored added nrofPoules need to be updated
        //editor.putInt(KEY_NROF_POULES,mPouleList.size());

        //Store info of selected poule:
        //- poule name
        //- nrof teams in poule
        //- team list
        //- poule scheme

        editor.putString(pouleKey,pouleName);
        editor.putInt(pouleKey+KEY_NROF_TEAMS, teamList.size());


        for (int i = 0; i < teamList.size(); i++) {
            String teamstr = pouleKey + KEY_TEAM+i;

            editor.putString(teamstr, teamList.get(i).getTeamName());
            editor.putString(teamstr + KEY_COACH,teamList.get(i).getCoachName());
        }

        for (int i = 0; i < teamList.size(); i++) {
            for (int j = 0; j < teamList.size(); j++) {
                String keystr1 = pouleKey + KEY_MATCH + i + "-" + j + KEY_GOALS_FOR;
                String keystr2 = pouleKey + KEY_MATCH + i + "-" + j + KEY_GOALS_AGAINST;

                Integer gf = pouleScheme.getMatchGoalsFor(i, j);
                Integer ga = pouleScheme.getMatchGoalsAgainst(i, j);

                if ((gf != null) && (ga != null)) {
                    editor.putInt(keystr1, gf);
                    editor.putInt(keystr2, ga);
                }
            }
        }

        editor.apply();
        editor.commit();
    }

    public void publishTournament() {
        mTournament.setIsPublished(true);

        DatabaseReference dBTournamentsInfoRef = mCupAppDB.getReference(KEY_TOURNAMENT_INFO_LIST);
        DatabaseReference tournamentIDDBRef = mCupAppDB.getReference(mTournament.getTournamentID());

        dBTournamentsInfoRef.child(mTournament.getTournamentID()).setValue(mTournament.getTournamentInfo());

        //Create PublishTournament of tournament class
        mPublishTournament = new PublishTournament(mTournament);

        tournamentIDDBRef.setValue(mPublishTournament);

//        tournamentIDDBRef.child(KEY_TOURNAMENT_NAME).setValue(mTournament.getTournamentName());
//        tournamentIDDBRef.child(KEY_LOCATION).setValue(mTournament.getLocation());
//        tournamentIDDBRef.child(KEY_DATE).setValue(mTournament.getDate());
//        tournamentIDDBRef.child(KEY_COMPETITION).setValue(mTournament.isFullCompetition());
//
//        ArrayList<Poule> pouleList = mTournament.getPouleList();
//        tournamentIDDBRef.child(KEY_POULES).child(KEY_NROF_POULES).setValue(pouleList.size());
//
//        for (int n=0; n < pouleList.size(); n++) {
//            Poule poule = pouleList.get(n);
//            String pouleName = poule.getPouleName();
//            ArrayList<Team> teamList = pouleList.get(n).getTeamList();
//            PouleScheme pouleScheme = pouleList.get(n).getPouleScheme();
//            String pouleKey = KEY_POULE+poule.getPouleNumber();
//
//            //Store info of selected poule:
//            //- poule name
//            //- nrof teams in poule
//            //- team list
//            //- poule scheme
//
//            tournamentIDDBRef.child(KEY_POULES).child(pouleKey).child(KEY_POULE_NAME).setValue(pouleName);
//            tournamentIDDBRef.child(KEY_POULES).child(pouleKey).child(KEY_TEAMS).child(KEY_NROF_TEAMS).setValue(teamList.size());
//
//            for (int i = 0; i < teamList.size(); i++) {
//                String teamKey = KEY_TEAM+i;
//
//                tournamentIDDBRef.child(KEY_POULES).child(pouleKey).child(KEY_TEAMS).child(teamKey).setValue(teamList.get(i).getTeamName());
//                tournamentIDDBRef.child(KEY_POULES).child(pouleKey).child(KEY_TEAMS).child(teamKey + KEY_COACH).setValue(teamList.get(i).getCoachName());
//            }
//
//            int r = poule.getPouleScheme().getNumberOfRounds();
//
//            for (int i=1; i < r; i++) {
//                Match[] matchList = poule.getPouleScheme().getRoundMatchList(i);
//
//                for (int m = 0; m < matchList.length; m++) {
//                    String hTeam = matchList[m].getHomeTeam();
//                    String oTeam = matchList[m].getOpponent();
//
//                    int x = 0;
//                    int y = 0;
//
//                    for(int p = 0; p < teamList.size(); p++){
//                        Team t = teamList.get(p);
//
//                        if (t.getTeamName().equals(hTeam)) { x = p;}
//                        if (t.getTeamName().equals(oTeam)) { y = p;}
//                    }
//
//                    String keystr1 = KEY_MATCH + x + "-" + y + KEY_GOALS_FOR;
//                    String keystr2 = KEY_MATCH + x + "-" + y + KEY_GOALS_AGAINST;
//
//                    Integer gf = pouleScheme.getMatchGoalsFor(x, y);
//                    Integer ga = pouleScheme.getMatchGoalsAgainst(x, y);
//
//                    if ((gf != null) && (ga != null)) {
//                        tournamentIDDBRef.child(KEY_POULES).child(pouleKey).child(KEY_MATCHES).child(keystr1).setValue(gf);
//                        tournamentIDDBRef.child(KEY_POULES).child(pouleKey).child(KEY_MATCHES).child(keystr2).setValue(ga);
//                    } else {
//                        tournamentIDDBRef.child(KEY_POULES).child(pouleKey).child(KEY_MATCHES).child(keystr1).setValue("");
//                        tournamentIDDBRef.child(KEY_POULES).child(pouleKey).child(KEY_MATCHES).child(keystr2).setValue("");
//                    }
//
//
//                }
//            }
//        }


    }

    public void setTournamentSearchInfo(PublishTournamentSearchInfo info){
//        mTournament = new Tournament(sInfo.getTournamentName());
//        mTournament.setTournamentID(sInfo.getTournamentID());
//        mTournament.setLocation(sInfo.getLocation());
//        mTournament.setDate(sInfo.getDate());

        mPublishTournament = new PublishTournament(info);
    }
}



