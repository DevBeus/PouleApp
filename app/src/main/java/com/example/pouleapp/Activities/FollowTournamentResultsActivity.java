package com.example.pouleapp.Activities;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.ExpandableListView;

import com.example.pouleapp.Data.GlobalData;
import com.example.pouleapp.Data.Poule;
import com.example.pouleapp.Data.PublishPoule;
import com.example.pouleapp.Data.PublishTournament;
import com.example.pouleapp.Data.PublishTournamentSearchInfo;
import com.example.pouleapp.Data.Tournament;
import com.example.pouleapp.R;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.Iterator;

import static com.example.pouleapp.Data.GlobalData.POULE_INDEX;

/**
 * Created by gezamenlijk on 28-1-2018.
 */

public class FollowTournamentResultsActivity extends AppCompatActivity {
    private PublishTournament mPublishTournament;
    private FollowRoundSchemeAdapter mAdapter;
    private FirebaseDatabase mFBDB;
    private DatabaseReference mFBRef;
    private PublishPoule mPoule;
    private int mPouleIndex;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_follow_tournament_results);

        ExpandableListView schemeExpandableListView = (ExpandableListView) findViewById(R.id.follow_tournament_results_expandable_list_view_scheme);

        final GlobalData globalVariable = (GlobalData) getApplicationContext();
        mPublishTournament = globalVariable.getPublishTournament();
        mFBDB = globalVariable.getFirebaseDatabase();

        Intent intent = getIntent();
        mPouleIndex = intent.getIntExtra(POULE_INDEX, 0);
        mPoule = mPublishTournament.getPouleList().get(mPouleIndex);

        mAdapter = new FollowRoundSchemeAdapter(this, mPoule);

        schemeExpandableListView.setAdapter(mAdapter);

        mFBRef = mFBDB.getReference(mPublishTournament.getTournamentInfo().getTournamentID());

        mFBRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                PublishTournament pTournament = dataSnapshot.getValue(PublishTournament.class);
                globalVariable.setPublishTournament(pTournament);
                mPublishTournament = globalVariable.getPublishTournament();
                mPoule.setRoundList(mPublishTournament.getPouleList().get(mPouleIndex).getRoundList());

                mAdapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(DatabaseError error) {
                // Failed to read value
                //Log.e(TAG, "Failed to read app title value.", error.toException());
            }
        });

    }


}
