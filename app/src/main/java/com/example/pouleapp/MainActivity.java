package com.example.pouleapp;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    public final static String EXTRA_MESSAGE = "com.example.pouleapp.MESSAGE";
    public final static String TEAM_INDEX = "com.example.pouleapp.TEAMINDEX";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ViewGroup radioGroup;
        ArrayList<Team> poule;

        final GlobalData globalVariable = (GlobalData) getApplicationContext();
        globalVariable.initPoule();

        poule = globalVariable.getPoule();
        radioGroup = (ViewGroup) findViewById(R.id.radio_group);

        for (int i = 0; i < poule.size(); i++) {
            RadioButton team = new RadioButton(this);
            team.setId(i);
            team.setText(poule.get(i).getTeamName());

            if (globalVariable.getSelectedTeam() == i) {
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
        int selectedId = radioGroup.getCheckedRadioButtonId();
        globalVariable.setSelectedTeam(selectedId);

        Intent intent = new Intent(this, EditTeamActivity.class);
        intent.putExtra(EXTRA_MESSAGE, message);
        intent.putExtra(TEAM_INDEX, selectedId);

        startActivity(intent);
    }

    public void addTeam(View view) {
        String message = "add";
        RadioGroup radioGroup;
        ArrayList<Team> poule;

        final GlobalData globalVariable = (GlobalData) getApplicationContext();
        poule = globalVariable.getPoule();
        poule.add(new Team(""));

        radioGroup = (RadioGroup) findViewById(R.id.radio_group);
        int selectedId = radioGroup.getCheckedRadioButtonId();
        globalVariable.setSelectedTeam(selectedId);

        Intent intent = new Intent(this, EditTeamActivity.class);
        intent.putExtra(EXTRA_MESSAGE,message);
        intent.putExtra(TEAM_INDEX, poule.size()-1);

        startActivity(intent);
    }

    public void deleteTeam(View view) {
        ArrayList<Team> poule;
        RadioGroup radioGroup;

        final GlobalData globalVariable = (GlobalData) getApplicationContext();
        poule = globalVariable.getPoule();
        PouleScheme pouleScheme = globalVariable.getPouleScheme();
        radioGroup = (RadioGroup) findViewById(R.id.radio_group);

        int sel_team = radioGroup.getCheckedRadioButtonId();
        globalVariable.setSelectedTeam(sel_team);

        // All results of selected team need to be removed and team needs to be removed
        pouleScheme.removeTeamResults(poule, sel_team);
        poule.remove(sel_team);

        globalVariable.savePoule();

        recreate();

        Toast.makeText(getApplicationContext(),"Team removed", Toast.LENGTH_LONG).show();
    }

    /** This method is called when scheme button is clicked */
    public void showScheme(View view) {
        String message = "Scheme";

        Intent intent = new Intent(this, SchemeTableActivity.class);
        intent.putExtra(EXTRA_MESSAGE,message);

        startActivity(intent);
    }

    /** This method is called when ranking button is clicked */
    public void showRanking(View view) {
        String message = "Ranking";

        Intent intent = new Intent(this, RankingActivity.class);
        intent.putExtra(EXTRA_MESSAGE,message);

        startActivity(intent);
    }
}
