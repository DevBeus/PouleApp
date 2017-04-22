package com.example.pouleapp;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;


/**
 * Created by gezamenlijk on 26-2-2017.
 */

public class EditMatchActivity extends AppCompatActivity {
    private int x = 0;
    private int y = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_match);

        Intent intent = getIntent();
        x = intent.getIntExtra("SchemeRow", 0);
        y = intent.getIntExtra("SchemeColumn", 0);

        ArrayList<Team> poule;
        PouleScheme pouleScheme;
        final GlobalData globalVariable = (GlobalData) getApplicationContext();

        poule = globalVariable.getPoule();
        pouleScheme = globalVariable.getPouleScheme();

        String homeTeam = poule.get(x).getTeamName();
        String opponent = poule.get(y).getTeamName();

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

        ArrayList<Team> poule;
        PouleScheme pouleScheme;
        final GlobalData globalVariable = (GlobalData) getApplicationContext();

        poule = globalVariable.getPoule();
        pouleScheme = globalVariable.getPouleScheme();

        pouleScheme.updateMatch(poule,x,y,hs,os);

        globalVariable.savePoule();

        Intent intent = new Intent(this, SchemeTableActivity.class);
        startActivity(intent);

    }

    public void cancel(View view) {
        Intent intent = new Intent(this, SchemeTableActivity.class);
        startActivity(intent);

    }

}
