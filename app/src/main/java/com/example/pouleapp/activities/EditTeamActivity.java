package com.example.pouleapp.activities;

import android.content.Intent;
import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.example.pouleapp.data.GlobalData;
import com.example.pouleapp.data.Match;
import com.example.pouleapp.data.Poule;
import com.example.pouleapp.data.Team;
import com.example.pouleapp.data.Tournament;
import com.example.pouleapp.data.PouleScheme;
import com.example.pouleapp.R;

import java.util.ArrayList;

import static com.example.pouleapp.data.GlobalData.ACTION_ADD;
import static com.example.pouleapp.data.GlobalData.ACTION_MESSAGE;
import static com.example.pouleapp.data.GlobalData.POULE_INDEX;
import static com.example.pouleapp.data.GlobalData.TEAM_INDEX;

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

        TextView textTeam = findViewById(R.id.edit_team_edit_text_team);

        ArrayList<Team> teamList = poule.getTeamList();
        Team team = teamList.get(mTeam_Index);

        textTeam.setText(team.getTeamName());

        TextView textCoach = findViewById(R.id.edit_team_edit_text_coach);
        textCoach.setText(team.getCoachName());

    }

    public void updateTeam(View view) {
        TextView textTeam = findViewById(R.id.edit_team_edit_text_team);
        TextView textCoach = findViewById(R.id.edit_team_edit_text_coach);

        String teamName = textTeam.getText().toString().trim();
        String teamCoach = textCoach.getText().toString().trim();

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

            globalVariable.saveTournament();

            Intent intent = new Intent(this, PouleActivity.class);
            intent.putExtra(POULE_INDEX, mPoule_Index);
            intent.putExtra(TEAM_INDEX, mTeam_Index);

            startActivity(intent);
        } else {
            String message = getResources().getString(R.string.toast_message_team_name_twice);
            Toast.makeText(getApplicationContext(), teamName + message, Toast.LENGTH_LONG).show();
        }

    }

    public void cancel(View view) {

        Intent intent = new Intent(this, PouleActivity.class);
        intent.putExtra(POULE_INDEX, mPoule_Index);
        intent.putExtra(TEAM_INDEX, mTeam_Index);

        startActivity(intent);

    }
}
