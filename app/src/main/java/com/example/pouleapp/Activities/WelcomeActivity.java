package com.example.pouleapp.Activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

import com.example.pouleapp.Data.GlobalData;
import com.example.pouleapp.R;

/**
 * Created by gezamenlijk on 21-5-2017.
 */

public class WelcomeActivity extends AppCompatActivity {

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_welcome);

    }

    public void startTournamentOverviewActivity(View view) {

        Intent intent = new Intent(this, SelectTournamentActivity.class);

        startActivity(intent);
    }

}
