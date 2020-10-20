package com.example.pouleapp.activities;

import android.content.Intent;
import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import android.view.View;

import com.example.pouleapp.R;
import com.google.firebase.FirebaseApp;

import static com.example.pouleapp.data.GlobalData.ACTIVITY_FOLLOW_TOURNAMENT;
import static com.example.pouleapp.data.GlobalData.ACTIVITY_SELECT_TOURNAMENT;
import static com.example.pouleapp.data.GlobalData.NEXT_ACTIVITY;

/**
 * Created by gezamenlijk on 21-5-2017.
 * this class is used for the welcome screen
 */

public class WelcomeActivity extends AppCompatActivity {

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_welcome);

        FirebaseApp.initializeApp(this);

    }

    public void startSelectTournamentActivity(View view) {

        Intent intent = new Intent(this, AuthUIActivity.class);
        intent.putExtra(NEXT_ACTIVITY, ACTIVITY_SELECT_TOURNAMENT);

        startActivity(intent);
    }

    public void startFollowTournamentActivity(View view) {

        Intent intent = new Intent(this, AuthUIActivity.class);
        intent.putExtra(NEXT_ACTIVITY, ACTIVITY_FOLLOW_TOURNAMENT);

        startActivity(intent);
    }

}
