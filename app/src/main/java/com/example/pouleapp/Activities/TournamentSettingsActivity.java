package com.example.pouleapp.Activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import com.example.pouleapp.Data.GlobalData;
import com.example.pouleapp.Data.Tournament;
import com.example.pouleapp.R;

/**
 * Created by gezamenlijk on 10-8-2017.
 * this class is used to define all tournament settings
 */

public class TournamentSettingsActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tournament_settings);

        // Show the Up button in the action bar.
        android.support.v7.app.ActionBar actionBar = getSupportActionBar();

        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
        }

        final GlobalData globalVariable = (GlobalData) getApplicationContext();
        Tournament tournament = globalVariable.getTournament();

        setTitle(getResources().getString(R.string.menu_tournament_settings_text));

        TextView tvTournamentName = (TextView) findViewById(R.id.tournament_settings_edit_text_tournament_name);
        tvTournamentName.setText(tournament.getTournamentName());

        TextView tvTournamentLocation = (TextView) findViewById(R.id.tournament_settings_edit_text_location);
        tvTournamentLocation.setText(tournament.getLocation());

        //hide soft keyboard
        //getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
    }

    public void saveTournamentSettings(View view) {
        final GlobalData globalVariable = (GlobalData) getApplicationContext();
        Tournament tournament = globalVariable.getTournament();

        TextView tvTournamentName = (TextView) findViewById(R.id.tournament_settings_edit_text_tournament_name);
        String name = tvTournamentName.getText().toString();

        TextView tvTournamentLocation = (TextView) findViewById(R.id.tournament_settings_edit_text_location);
        String location = tvTournamentLocation.getText().toString();

        tournament.setTournamentName(name);
        tournament.setLocation(location);

        globalVariable.saveTournament();
        globalVariable.updateTournamentName(name);
        globalVariable.saveAppData();

        Intent intent = new Intent(this, TournamentActivity.class);

        startActivity(intent);

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == android.R.id.home) {
            // This ID represents the Home or Up button. In the case of this
            // activity, the Up button is shown. For
            // more details, see the Navigation pattern on Android Design:
            //
            // http://developer.android.com/design/patterns/navigation.html#up-vs-back
            //
            navigateUpTo(new Intent(this, TournamentActivity.class));
            return true;
        }
        return super.onOptionsItemSelected(item);
    }



}
