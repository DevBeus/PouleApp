package com.example.pouleapp;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

/**
 * Created by gezamenlijk on 26-4-2017.
 */

public class EditPouleActivity extends AppCompatActivity {
    public final static String EXTRA_MESSAGE = "com.example.pouleapp.MESSAGE";
    public final static String POULE_INDEX = "com.example.pouleapp.POULEINDEX";
    public final static String TEAM_INDEX = "com.example.pouleapp.TEAMINDEX";
    private static int mPoule_Index = 0;
    private static int mTeam_Index = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_poule);
        ViewGroup radioGroup;
        ArrayList<Team> teamList;
        ArrayList<Poule> pouleList;
        Poule poule;

        final GlobalData globalVariable = (GlobalData) getApplicationContext();
        Intent intent = getIntent();
        mPoule_Index = intent.getIntExtra(EditPouleActivity.POULE_INDEX,0);
        //mTeam_Index = intent.getIntExtra(EditPouleActivity.TEAM_INDEX,0);

        pouleList = globalVariable.getPouleList();
        poule = pouleList.get(mPoule_Index);

        teamList = poule.getTeamList();
        // When teams are removed, activity is re-entrant with same TEAM_INDEX value in intent
        mTeam_Index = Math.min(intent.getIntExtra(EditPouleActivity.TEAM_INDEX,0),teamList.size()-1);

        TextView textView = (TextView) findViewById(R.id.textViewPoule);
        textView.setText(poule.getPouleName());

        radioGroup = (ViewGroup) findViewById(R.id.radio_group);

        for (int i = 0; i < teamList.size(); i++) {
            RadioButton team = new RadioButton(this);
            team.setId(i);
            team.setText(teamList.get(i).getTeamName());

            //Default first team is selected
            if ( i==mTeam_Index ) {
                team.setChecked(true);
            }

            radioGroup.addView(team);
        }
    }

    /** This method is called when Edit button is clicked */
    public void editTeam(View view) {
        String message = "edit";
        RadioGroup radioGroup;

        final GlobalData globalVariable = (GlobalData) getApplicationContext();

        radioGroup = (RadioGroup) findViewById(R.id.radio_group);
        mTeam_Index = radioGroup.getCheckedRadioButtonId();
        //globalVariable.setSelectedTeam(selectedId);

        Intent intent = new Intent(this, EditTeamActivity.class);
        intent.putExtra(EXTRA_MESSAGE, message);
        intent.putExtra(POULE_INDEX, mPoule_Index);
        intent.putExtra(TEAM_INDEX, mTeam_Index);

        startActivity(intent);
    }

    public void addTeam(View view) {
        String message = "add";
        Intent intent = new Intent(this, EditTeamActivity.class);
        intent.putExtra(EXTRA_MESSAGE,message);
        intent.putExtra(POULE_INDEX, mPoule_Index);
        intent.putExtra(TEAM_INDEX, 0); //When new team is added, team index is not relevant

        startActivity(intent);
    }

    public void deleteTeam(View view) {
        ArrayList<Poule> pouleList;
        Poule poule;
        RadioGroup radioGroup;

        final GlobalData globalVariable = (GlobalData) getApplicationContext();
        pouleList = globalVariable.getPouleList();
        poule = pouleList.get(mPoule_Index);

        radioGroup = (RadioGroup) findViewById(R.id.radio_group);

        if (poule.getTeamList().size()>2) {

            int sel_team = radioGroup.getCheckedRadioButtonId();
            poule.deleteTeam(sel_team);

            // After delete, checked team will be the first team
            mTeam_Index = 0;

            globalVariable.savePoule(mPoule_Index);

            recreate();

            Toast.makeText(getApplicationContext(), "Team removed", Toast.LENGTH_LONG).show();
        } else {
            Toast.makeText(getApplicationContext(), "Poule should contain at least 2 teams", Toast.LENGTH_LONG).show();
        }
    }

    /** This method is called when scheme button is clicked */
    public void showScheme(View view) {
        String message = "Scheme";

        Intent intent = new Intent(this, SchemeTableActivity.class);
        intent.putExtra(EXTRA_MESSAGE,message);
        intent.putExtra(POULE_INDEX, mPoule_Index);

        startActivity(intent);
    }

    /** This method is called when ranking button is clicked */
    public void showRanking(View view) {
        String message = "Ranking";

        Intent intent = new Intent(this, RankingActivity.class);
        intent.putExtra(EXTRA_MESSAGE,message);
        intent.putExtra(POULE_INDEX, mPoule_Index);

        startActivity(intent);
    }

    /** This method is called when scheme button is clicked */
    public void showPoules(View view) {
        String message = "Poules";

        Intent intent = new Intent(this, MainActivity.class);
        intent.putExtra(EXTRA_MESSAGE,message);

        startActivity(intent);
    }
}

