package com.example.pouleapp.Activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

import com.example.pouleapp.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

/**
 * Created by gezamenlijk on 21-5-2017.
 * this class is used for the welcome screen
 */

public class WelcomeActivity extends AppCompatActivity {

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_welcome);

    }

    public void startSelectTournamentActivity(View view) {

        Intent intent = new Intent(this, AuthUIActivity.class);

        startActivity(intent);
    }

}
