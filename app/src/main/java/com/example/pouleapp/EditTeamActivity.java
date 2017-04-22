package com.example.pouleapp;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.TextView;

import java.util.ArrayList;

public class EditTeamActivity extends AppCompatActivity {
    private int mPoule_Index = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_team);

        Intent intent = getIntent();
        String message = intent.getStringExtra(MainActivity.EXTRA_MESSAGE);
        mPoule_Index = intent.getIntExtra(MainActivity.TEAM_INDEX,0);

        TextView textTeam = (TextView) findViewById(R.id.editTextTeam);

        // Calling Application class (see application tag in AndroidManifest.xml)
        final GlobalData globalVariable = (GlobalData) getApplicationContext();
        ArrayList<Team> poule = globalVariable.getPoule();
        Team team = poule.get(mPoule_Index);

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
        ArrayList<Team> poule = globalVariable.getPoule();
        Team team = poule.get(mPoule_Index);

        team.setTeamName(teamName);
        team.setCoachName(teamCoach);

        globalVariable.savePoule();

        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);

    }

    public void cancel(View view) {

        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);

    }
}
