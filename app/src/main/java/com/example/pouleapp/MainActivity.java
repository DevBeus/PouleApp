package com.example.pouleapp;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Spinner;

import java.util.ArrayList;
import java.util.List;

import static com.example.pouleapp.GlobalData.DEFAULT_TOURNAMENT_ID;

/**
 * Created by gezamenlijk on 21-5-2017.
 */

public class MainActivity extends AppCompatActivity {
    private Spinner mSpinner;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        final GlobalData globalVariable = (GlobalData) getApplicationContext();
        globalVariable.initApp();

        mSpinner = (Spinner) findViewById(R.id.spinner);
        List<String> list = globalVariable.getTournamentNameList();

        ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(this,
                android.R.layout.simple_spinner_item, list);
        dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        mSpinner.setAdapter(dataAdapter);
    }

    public void showTournament(View view) {
        final GlobalData globalVariable = (GlobalData) getApplicationContext();
        ArrayList<String> tournamentIDList = globalVariable.getTournamentIDList();
        int selectedIndex = mSpinner.getSelectedItemPosition();
        String id = tournamentIDList.get(selectedIndex);

        if (id == DEFAULT_TOURNAMENT_ID) { id = globalVariable.addTournament(); }

        globalVariable.initTournament(id);

        Intent intent = new Intent(this, TournamentActivity.class);

        startActivity(intent);
    }

    public void deleteTournament(View view) {
        final GlobalData globalVariable = (GlobalData) getApplicationContext();
        ArrayList<String> tournamentIDList = globalVariable.getTournamentIDList();
        int selectedIndex = mSpinner.getSelectedItemPosition();

        globalVariable.deleteTournament(selectedIndex);
        globalVariable.saveAppData();

        recreate();
    }

}
