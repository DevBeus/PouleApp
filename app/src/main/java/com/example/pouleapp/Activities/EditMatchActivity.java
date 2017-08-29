package com.example.pouleapp.Activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.example.pouleapp.Data.GlobalData;
import com.example.pouleapp.Data.Poule;
import com.example.pouleapp.Data.Team;
import com.example.pouleapp.Data.Tournament;
import com.example.pouleapp.Data.PouleScheme;
import com.example.pouleapp.R;

import java.util.ArrayList;

import static com.example.pouleapp.Data.GlobalData.POULE_INDEX;
import static com.example.pouleapp.Data.GlobalData.PREVIOUS_ACTIVITY;
import static com.example.pouleapp.Data.GlobalData.PREVIOUS_TAB;
import static com.example.pouleapp.Data.GlobalData.SCHEME_ROW;
import static com.example.pouleapp.Data.GlobalData.SCHEME_COLUMN;
import static com.example.pouleapp.Data.GlobalData.SCHEME_TAB;
import static com.example.pouleapp.Data.GlobalData.TAB_INDEX;


/**
 * Created by gezamenlijk on 26-2-2017.
 */

public class EditMatchActivity extends AppCompatActivity {
    private int x = 0;
    private int y = 1;
    private int mPoule_Index = 0;
    private int mPrevious_Tab;
    private String mPrevious_Activity;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_match);

        Intent intent = getIntent();
        mPrevious_Activity = intent.getStringExtra(PREVIOUS_ACTIVITY);
        mPrevious_Tab = intent.getIntExtra(PREVIOUS_TAB,SCHEME_TAB);
        x = intent.getIntExtra(SCHEME_ROW, 0);
        y = intent.getIntExtra(SCHEME_COLUMN, 0);
        mPoule_Index = intent.getIntExtra(POULE_INDEX,0);

        final GlobalData globalVariable = (GlobalData) getApplicationContext();
        Tournament tournament = globalVariable.getTournament();
        ArrayList<Poule> pouleList = tournament.getPouleList();
        Poule poule = pouleList.get(mPoule_Index);
        ArrayList<Team> teamList = poule.getTeamList();
        PouleScheme pouleScheme = poule.getPouleScheme();

        setTitle(getResources().getString(R.string.menu_poule_name_text) + poule.getPouleName());

        String homeTeam = teamList.get(x).getTeamName();
        String opponent = teamList.get(y).getTeamName();

        TextView textHomeTeam = (TextView) findViewById(R.id.edit_match_text_view_home_team);
        textHomeTeam.setText(homeTeam);
        TextView textOpponent = (TextView) findViewById(R.id.edit_match_text_view_opponent_team);
        textOpponent.setText(opponent);

        Integer gf = pouleScheme.getMatchGoalsFor(x, y);
        Integer ga = pouleScheme.getMatchGoalsAgainst(x, y);

        TextView textGF = (TextView) findViewById(R.id.edit_match_edit_text_home_score);
        TextView textGA = (TextView) findViewById(R.id.edit_match_edit_text_opponent_score);

        if ((gf == null) || (ga == null)) {
            textGF.setText("");
            textGA.setText("");
        } else {
            textGF.setText(Integer.toString(gf));
            textGA.setText(Integer.toString(ga));
        }
    }

    public void updateMatch(View view) {
        TextView textHS = (TextView) findViewById(R.id.edit_match_edit_text_home_score);
        TextView textOS = (TextView) findViewById(R.id.edit_match_edit_text_opponent_score);

        String strHS = textHS.getText().toString();
        String strOS = textOS.getText().toString();
        Integer hs = null;
        Integer os = null;

        if ((TextUtils.isEmpty(strHS)) && (TextUtils.isEmpty(strOS))) {
            hs = null;
            os = null;
        } else if ((TextUtils.isEmpty(strHS)) || (TextUtils.isEmpty(strOS))) {
            String message = getResources().getString(R.string.no_empty_scores_message);
            Toast.makeText(getApplicationContext(),message, Toast.LENGTH_LONG).show();
            return;
        } else {
            try {
                hs = Integer.parseInt(strHS);
                os = Integer.parseInt(strOS);
            } catch (NumberFormatException e) {
                hs = null;
                os = null;

                String message = getResources().getString(R.string.scores_should_be_numbers_message);
                Toast.makeText(getApplicationContext(), message, Toast.LENGTH_LONG).show();

                return;
            }

        }

        final GlobalData globalVariable = (GlobalData) getApplicationContext();
        Tournament tournament = globalVariable.getTournament();
        ArrayList<Poule> pouleList = tournament.getPouleList();
        Poule poule = pouleList.get(mPoule_Index);
        ArrayList<Team> teamList = poule.getTeamList();
        PouleScheme pouleScheme = poule.getPouleScheme();

        pouleScheme.updateMatch(teamList,x,y,hs,os);

        globalVariable.savePoule(poule);

//        Intent intent;
//
//        if (mPrevious_Activity.equals(SCHEME_TABLE_ACTIVITY)){
//            intent = new Intent(this, SchemeTableActivity.class);
//        } else {
//            intent = new Intent(this, SchemeActivity.class);
//        }

        Intent intent = new Intent(this, SchemeRankingActivity.class);
        intent.putExtra(POULE_INDEX, mPoule_Index);
        intent.putExtra(TAB_INDEX,mPrevious_Tab);
        startActivity(intent);

    }

    public void cancel(View view) {
//        Intent intent;
//
//        if (mPrevious_Activity.equals(SCHEME_TABLE_ACTIVITY)){
//            intent = new Intent(this, SchemeTableActivity.class);
//        } else {
//            intent = new Intent(this, SchemeActivity.class);
//        }

        Intent intent = new Intent(this, SchemeRankingActivity.class);
        intent.putExtra(POULE_INDEX, mPoule_Index);
        intent.putExtra(TAB_INDEX,mPrevious_Tab);
        startActivity(intent);

    }

}
