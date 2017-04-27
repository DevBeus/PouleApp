package com.example.pouleapp;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

import static com.example.pouleapp.MainActivity.POULE_INDEX;


/**
 * Created by gezamenlijk on 26-2-2017.
 */

public class EditMatchActivity extends AppCompatActivity {
    private int x = 0;
    private int y = 1;
    private int mPoule_Index = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_match);

        Intent intent = getIntent();
        x = intent.getIntExtra("SchemeRow", 0);
        y = intent.getIntExtra("SchemeColumn", 0);
        mPoule_Index = intent.getIntExtra(EditPouleActivity.POULE_INDEX,0);

        final GlobalData globalVariable = (GlobalData) getApplicationContext();
        ArrayList<Poule> pouleList = globalVariable.getPouleList();
        Poule poule = pouleList.get(mPoule_Index);
        ArrayList<Team> teamList = poule.getTeamList();
        PouleScheme pouleScheme = poule.getPouleScheme();

        String homeTeam = teamList.get(x).getTeamName();
        String opponent = teamList.get(y).getTeamName();

        TextView textHomeTeam = (TextView) findViewById(R.id.textHomeTeam);
        textHomeTeam.setText(homeTeam);
        TextView textOpponent = (TextView) findViewById(R.id.textOpponentTeam);
        textOpponent.setText(opponent);

        Integer gf = pouleScheme.getMatchGoalsFor(x, y);
        Integer ga = pouleScheme.getMatchGoalsAgainst(x, y);

        TextView textGF = (TextView) findViewById(R.id.editHomeScore);
        TextView textGA = (TextView) findViewById(R.id.editOpponentScore);

        if ((gf == null) || (ga == null)) {
            textGF.setText("");
            textGA.setText("");
        } else {
            textGF.setText(Integer.toString(gf));
            textGA.setText(Integer.toString(ga));
        }
    }

    public void updateMatch(View view) {
        TextView textHS = (TextView) findViewById(R.id.editHomeScore);
        TextView textOS = (TextView) findViewById(R.id.editOpponentScore);

        String strHS = textHS.getText().toString();
        String strOS = textOS.getText().toString();
        Integer hs = null;
        Integer os = null;

        if ((TextUtils.isEmpty(strHS)) && (TextUtils.isEmpty(strOS))) {
            hs = null;
            os = null;
        } else if ((TextUtils.isEmpty(strHS)) || (TextUtils.isEmpty(strOS))) {
            Toast.makeText(getApplicationContext(),"Both scores should be filled or left empty !", Toast.LENGTH_LONG).show();
            return;
        } else {
            hs = Integer.parseInt(strHS);
            os = Integer.parseInt(strOS);
        }

        final GlobalData globalVariable = (GlobalData) getApplicationContext();
        ArrayList<Poule> pouleList = globalVariable.getPouleList();
        Poule poule = pouleList.get(mPoule_Index);
        ArrayList<Team> teamList = poule.getTeamList();
        PouleScheme pouleScheme = poule.getPouleScheme();

        pouleScheme.updateMatch(teamList,x,y,hs,os);

        globalVariable.savePoule(mPoule_Index);

        Intent intent = new Intent(this, SchemeTableActivity.class);
        intent.putExtra(POULE_INDEX, mPoule_Index);
        startActivity(intent);

    }

    public void cancel(View view) {
        Intent intent = new Intent(this, SchemeTableActivity.class);
        intent.putExtra(POULE_INDEX, mPoule_Index);
        startActivity(intent);

    }

}
