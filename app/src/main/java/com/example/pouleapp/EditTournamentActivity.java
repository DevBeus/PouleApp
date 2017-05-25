package com.example.pouleapp;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

/**
 * Created by gezamenlijk on 21-5-2017.
 */

public class EditTournamentActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_tournament);

        final GlobalData globalVariable = (GlobalData) getApplicationContext();
        globalVariable.initPouleList();

    }
}
