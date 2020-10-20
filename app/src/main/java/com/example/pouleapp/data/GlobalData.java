package com.example.pouleapp.data;

import android.app.Application;
import android.content.SharedPreferences;
import android.widget.Toast;

import com.example.pouleapp.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.gson.Gson;

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
    private AppData mCupAppData;

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

    public ArrayList<String> getTournamentIDList() {
        ArrayList<String> tournamentIDList = new ArrayList<>();

        for (int i=0; i< mCupAppData.getTournamentInfoList().size(); i++) {
            tournamentIDList.add(mCupAppData.getTournamentInfoList().get(i).getTournamentID());
        }

        return tournamentIDList;
    }

    public ArrayList<String> getTournamentNameList() {
        ArrayList<String> tournamentNameList = new ArrayList<>();

        for (int i=0; i< mCupAppData.getTournamentInfoList().size(); i++) {
            tournamentNameList.add(mCupAppData.getTournamentInfoList().get(i).getTournamentName());
        }
        return tournamentNameList;
    }

    private static final String CUP_APP_DATA = "CupAppData";
    private static final String CUP_APP_DATA_GSON = "CupAppDataGson";
    private static final String DEFAULT_CUP_APP_DATA_JSON = "{\"selected_tournament_index\":0,\"tournament_info_list\":[]}";
    private static final String TOURNAMENT_GSON = "TournamentGson";
    private static final String DEFAULT_TOURNAMENT_JSON = "";

    //===================================================================
    // constants to be moved to other classes
    public static final String KEY_TOURNAMENT_INFO_LIST = "TournamentsInfo";
    public static final String DEFAULT_POULE_NAME = "PouleName";

    private static final String DEFAULT_TOURNAMENT_ID = "0";
//===================================================================

    public void initApp() {
        SharedPreferences cupAppPrefs = getSharedPreferences(CUP_APP_DATA,MODE_PRIVATE);
        String cupAppDataGson_string = cupAppPrefs.getString(CUP_APP_DATA_GSON, DEFAULT_CUP_APP_DATA_JSON);

        Gson gson = new Gson();

        mCupAppData = gson.fromJson(cupAppDataGson_string, AppData.class);
        mCupAppDB = FirebaseDatabase.getInstance();

    }

    public void initTournament(String id) {
        SharedPreferences tournamentPrefs = getSharedPreferences(id,MODE_PRIVATE);
        String tournamentGson_string = tournamentPrefs.getString(TOURNAMENT_GSON, DEFAULT_TOURNAMENT_JSON);

        Gson gson = new Gson();

        mTournament = gson.fromJson(tournamentGson_string, Tournament.class);
    }


    public void addTournament(String tournamentName) {
        mTournament = new Tournament(tournamentName);
        saveTournament();

        String selectedTournamentID = mTournament.getTournamentID();

        mCupAppData.addTournament(selectedTournamentID,tournamentName);
        saveAppData();
    }

    public void deleteTournament(int index) {
        String id = mCupAppData.getTournamentInfoList().get(index).getTournamentID();

        if (id.equals(DEFAULT_TOURNAMENT_ID)) {
            String message = getResources().getString(R.string.tournament_creation_cannot_be_removed_message);
            Toast.makeText(getApplicationContext(), message, Toast.LENGTH_LONG).show();
        } else {
            //First SharedPrefsFile needs to be cleared (i.e. remove data from memory), and the prefs file needs to deleted
            SharedPreferences.Editor editor = getSharedPreferences(id, MODE_PRIVATE).edit();
            editor.clear();

            String filePath = getApplicationContext().getFilesDir().getParent() + "/shared_prefs/" + id + ".xml";
            File deletePrefFile = new File(filePath);
            deletePrefFile.delete();

            mCupAppData.deleteTournament(id);
            saveAppData();
        }
    }

    public void updateTournamentName(String id, String name) {
        saveTournament();
        mCupAppData.setTournamentName(id,name);
        saveAppData();
    }

    private void saveAppData() {
        SharedPreferences.Editor edit = getSharedPreferences(CUP_APP_DATA, MODE_PRIVATE).edit();

        Gson gson = new Gson();

        String jsonString = gson.toJson(mCupAppData);

        edit.putString(CUP_APP_DATA_GSON, jsonString);
        edit.apply();
        edit.commit();

    }

    public void saveTournament() {
        SharedPreferences.Editor edit = getSharedPreferences(mTournament.getTournamentID(), MODE_PRIVATE).edit();

        Gson gson = new Gson();

        String jsonString = gson.toJson(mTournament);

        edit.putString(TOURNAMENT_GSON, jsonString);
        edit.apply();
        edit.commit();

    }


    public void publishTournament() {
        mTournament.setIsPublished(true);

        DatabaseReference dBTournamentsInfoRef = mCupAppDB.getReference(KEY_TOURNAMENT_INFO_LIST);
        DatabaseReference tournamentIDDBRef = mCupAppDB.getReference(mTournament.getTournamentID());

        dBTournamentsInfoRef.child(mTournament.getTournamentID()).setValue(mTournament.getTournamentInfo());

        //Create PublishTournament of tournament class
        mPublishTournament = new PublishTournament(mTournament);

        tournamentIDDBRef.setValue(mPublishTournament);

    }

    public void setTournamentSearchInfo(PublishTournamentSearchInfo info){
        mPublishTournament = new PublishTournament(info);
    }
}



