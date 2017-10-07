package com.example.pouleapp.Activities;

import android.app.DatePickerDialog;
import android.content.Intent;
import java.util.Calendar;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.Switch;
import android.widget.TextView;

import com.example.pouleapp.Data.GlobalData;
import com.example.pouleapp.Data.Tournament;
import com.example.pouleapp.R;

/**
 * Created by gezamenlijk on 10-8-2017.
 * this class is used to define all tournament settings
 */

public class TournamentSettingsActivity extends AppCompatActivity {
    Calendar calendar = Calendar.getInstance();
    Button selectDateButton;

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

        selectDateButton = (Button) findViewById(R.id.tournament_settings_button_select_date);

        if (!tournament.getDate().equals("")) { selectDateButton.setText(tournament.getDate());}

        Switch switchCompetition = (Switch) findViewById(R.id.tournament_settings_switch_competition);
        switchCompetition.setChecked(tournament.isFullCompetition());

        //hide soft keyboard
        //getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
    }

    DatePickerDialog.OnDateSetListener listener = new DatePickerDialog.OnDateSetListener() {
        @Override
        public void onDateSet(DatePicker view, int year, int month, int day) {
            final GlobalData globalVariable = (GlobalData) getApplicationContext();
            Tournament tournament = globalVariable.getTournament();

            tournament.setDate(day+"/"+month+"/"+year);
            selectDateButton.setText(day+"/"+month+"/"+year);
        }
    };

    public void startSelectDateDialog(View v){
        new DatePickerDialog(TournamentSettingsActivity.this, listener, calendar.get(Calendar.YEAR),calendar.get(Calendar.MONTH),calendar.get(Calendar.DAY_OF_MONTH)).show();
    }

    public void saveTournamentSettings(View view) {
        final GlobalData globalVariable = (GlobalData) getApplicationContext();
        Tournament tournament = globalVariable.getTournament();

        TextView tvTournamentName = (TextView) findViewById(R.id.tournament_settings_edit_text_tournament_name);
        String name = tvTournamentName.getText().toString().trim();

        TextView tvTournamentLocation = (TextView) findViewById(R.id.tournament_settings_edit_text_location);
        String location = tvTournamentLocation.getText().toString().trim();

        Button btnTournamentSelectDate = (Button) findViewById(R.id.tournament_settings_button_select_date);
        String date = btnTournamentSelectDate.getText().toString();

        Switch switchCompetition = (Switch) findViewById(R.id.tournament_settings_switch_competition);

        tournament.setTournamentName(name);
        tournament.setLocation(location);
        tournament.setDate(date);

        boolean b = switchCompetition.isChecked();

        tournament.setFullCompetition(b);

        globalVariable.saveTournament();
        globalVariable.updateTournamentName(name);
        globalVariable.saveAppData();

        // when competition is switched from full to half, tournament needs to be reloaded to clear old data
        globalVariable.initTournament(tournament.getTournamentID());

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
