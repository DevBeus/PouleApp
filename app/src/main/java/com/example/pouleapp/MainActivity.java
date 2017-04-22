package com.example.pouleapp;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.ViewGroup;
import android.widget.RadioButton;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

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
}
