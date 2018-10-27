package com.example.pouleapp.activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import com.example.pouleapp.data.GlobalData;
import com.example.pouleapp.data.Poule;
import com.example.pouleapp.data.Tournament;
import com.example.pouleapp.R;

import java.util.ArrayList;

import static com.example.pouleapp.data.GlobalData.POULE_INDEX;

/**
 * Created by gezamenlijk on 11-8-2017.
 * This class takes care of the Poule Settings
 */

public class PouleSettingsActivity extends AppCompatActivity {
    private static int mPoule_Index = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_poule_settings);
        ArrayList<Poule> pouleList;

        Intent intent = getIntent();
        mPoule_Index = intent.getIntExtra(POULE_INDEX,0);

        // Show the Up button in the action bar.
        android.support.v7.app.ActionBar actionBar = getSupportActionBar();

        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
        }

        final GlobalData globalVariable = (GlobalData) getApplicationContext();
        Tournament tournament = globalVariable.getTournament();
        pouleList = tournament.getPouleList();

        setTitle(getResources().getString(R.string.menu_poule_settings_text));

        TextView tvPouleName = findViewById(R.id.poule_settings_edit_text_poule_name);
        tvPouleName.setText(pouleList.get(mPoule_Index).getPouleName());

    }

    public void savePouleSettings(View view) {
        final GlobalData globalVariable = (GlobalData) getApplicationContext();
        Tournament tournament = globalVariable.getTournament();

        TextView tvPouleName = findViewById(R.id.poule_settings_edit_text_poule_name);
        String name = tvPouleName.getText().toString().trim();

        ArrayList<Poule> pouleList = tournament.getPouleList();
        Poule poule = pouleList.get(mPoule_Index);

        poule.setPouleName(name);
        globalVariable.saveTournament();

        Intent intent = new Intent(this, PouleActivity.class);
        intent.putExtra(POULE_INDEX, mPoule_Index);


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
            navigateUpTo(new Intent(this, PouleActivity.class));
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

}
