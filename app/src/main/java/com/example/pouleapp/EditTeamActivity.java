package com.example.pouleapp;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

import static com.example.pouleapp.GlobalData.ACTION_ADD;
import static com.example.pouleapp.GlobalData.ACTION_MESSAGE;
import static com.example.pouleapp.GlobalData.POULE_INDEX;
import static com.example.pouleapp.GlobalData.TEAM_INDEX;

public class EditTeamActivity extends AppCompatActivity {
    private int mPoule_Index = 0;
    private int mTeam_Index = 0;
    private String mMessage;
    private String mDefaultTeamName = "team";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_team);

        Intent intent = getIntent();
        mMessage = intent.getStringExtra(ACTION_MESSAGE);
        mPoule_Index = intent.getIntExtra(POULE_INDEX,0);
        mTeam_Index = intent.getIntExtra(TEAM_INDEX,0);

        final GlobalData globalVariable = (GlobalData) getApplicationContext();
        Tournament tournament = globalVariable.getTournament();
        ArrayList<Poule> pouleList = tournament.getPouleList();
        Poule poule = pouleList.get(mPoule_Index);

        setTitle(getResources().getString(R.string.menu_poule_name_text) + poule.getPouleName());

        if (mMessage.equals(ACTION_ADD)) {
            poule.addTeam(mDefaultTeamName);
            mTeam_Index =  poule.getTeamList().size()-1;
        }

        TextView textTeam = (TextView) findViewById(R.id.editTextTeam);

        ArrayList<Team> teamList = poule.getTeamList();
        Team team = teamList.get(mTeam_Index);

        textTeam.setText(team.getTeamName());

        TextView textCoach = (TextView) findViewById(R.id.editTextCoach);
        textCoach.setText(team.getCoachName());

    }

    public void updateTeam(View view) {
        TextView textTeam = (TextView) findViewById(R.id.editTextTeam);
        TextView textCoach = (TextView) findViewById(R.id.editTextCoach);

        String teamName = textTeam.getText().toString();
        String teamCoach = textCoach.getText().toString();

        final GlobalData globalVariable = (GlobalData) getApplicationContext();
        Tournament tournament = globalVariable.getTournament();
        ArrayList<Poule> pouleList = tournament.getPouleList();
        Poule poule = pouleList.get(mPoule_Index);

        ArrayList<Team> teamList = poule.getTeamList();

        //Check whether new team name already exists
        boolean found = false;

        for (int i=0; i < teamList.size(); i++) {
            if ((teamList.get(i).getTeamName().equals(teamName)) && (mTeam_Index !=i)) { found = true; }
        }

        if ( !found ) {
            Team team = teamList.get(mTeam_Index);

            team.setTeamName(teamName);
            team.setCoachName(teamCoach);

            // all matches of teamName need to be updated with new team name
            PouleScheme pouleScheme = poule.getPouleScheme();
            Match m;

            for (int j=0; j < teamList.size(); j++) {
                m = pouleScheme.getMatch(mTeam_Index,j);
                m.setHomeTeam(teamName);

                m = pouleScheme.getMatch(j,mTeam_Index);
                m.setOpponent(teamName);
            }

            globalVariable.savePoule(poule);

            Intent intent = new Intent(this, PouleActivity.class);
            intent.putExtra(POULE_INDEX, mPoule_Index);
            intent.putExtra(TEAM_INDEX, mTeam_Index);

            startActivity(intent);
        } else {
            Toast.makeText(getApplicationContext(), "Team name " + teamName + " already part of poule", Toast.LENGTH_LONG).show();
        }

    }

    public void cancel(View view) {
//        //if cancel is done for addition of team, new team needs to be removed
//        if (mMessage.equals(ACTION_ADD)) {
//            final GlobalData globalVariable = (GlobalData) getApplicationContext();
//            Tournament tournament = globalVariable.getTournament();
//            ArrayList<Poule> pouleList = tournament.getPouleList();
//            Poule poule = pouleList.get(mPoule_Index);
//            poule.deleteTeam(mTeam_Index);
//        }

        Intent intent = new Intent(this, PouleActivity.class);
        intent.putExtra(POULE_INDEX, mPoule_Index);
        intent.putExtra(TEAM_INDEX, mTeam_Index);

        startActivity(intent);

    }
}
