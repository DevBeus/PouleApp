package com.example.pouleapp.activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.example.pouleapp.data.GlobalData;
import com.example.pouleapp.data.PublishPoule;
import com.example.pouleapp.data.PublishTournament;
import com.example.pouleapp.data.PublishTournamentSearchInfo;
import com.example.pouleapp.R;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

import static com.example.pouleapp.data.GlobalData.POULE_INDEX;

/**
 * Created by gezamenlijk on 14-1-2018.
 */

public class FollowTournamentInfoActivity extends AppCompatActivity{
    private FirebaseDatabase mFBDB;
    private DatabaseReference mFBRef;
    private PublishTournament mTournament;
    private ArrayList<PublishPoule> mPouleList = new ArrayList<>();
    private ArrayList<String> mPouleListNames = new ArrayList<>();
    private ArrayAdapter<String> mAdapter;
    private ValueEventListener mValueEventListener;

    private static final String TAG = "FollowTournamentInfo: ";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_follow_tournament_info);

        // Show the Up button in the action bar.
        android.support.v7.app.ActionBar actionBar = getSupportActionBar();

        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
        }

        final GlobalData globalVariable = (GlobalData) getApplicationContext();
        mTournament = globalVariable.getPublishTournament();
        PublishTournamentSearchInfo info = mTournament.getTournamentInfo();

        //setTitle(getResources().getString(R.string.menu_tournament_settings_text));

        TextView tvTournamentName = findViewById(R.id.follow_tournament_info_text_view_tournament_name_show);
        tvTournamentName.setText(info.getTournamentName());

        TextView tvTournamentLocation = findViewById(R.id.follow_tournament_info_text_view_location_show);
        tvTournamentLocation.setText(info.getLocation());

        TextView tvDate = findViewById(R.id.follow_tournament_info_text_view_date_show);
        tvDate.setText(info.getDate());

        ListView listView = findViewById(R.id.follow_tournament_info_list_view_poules);
        mAdapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, mPouleListNames);
        listView.setAdapter(mAdapter);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> a, View v, int position, long id) {
                Intent intent = new Intent(getApplicationContext(), FollowSchemeRankingActivity.class);
                intent.putExtra(POULE_INDEX, position);

                mFBRef.removeEventListener(mValueEventListener);

                startActivity(intent);
            }
        });



        mFBDB = FirebaseDatabase.getInstance();
        mFBRef = mFBDB.getReference(mTournament.getTournamentInfo().getTournamentID());

        mValueEventListener = new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                mPouleListNames.clear();

                PublishTournament pTournament = dataSnapshot.getValue(PublishTournament.class);
                globalVariable.setPublishTournament(pTournament);
                mPouleList = pTournament != null ? pTournament.getPouleList() : null; // pTournament could be null

                for(PublishPoule p: mPouleList) { mPouleListNames.add(p.getPouleName()); }

                mAdapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(DatabaseError error) {
                // Failed to read value
                Log.e(TAG, "Failed to read app title value.", error.toException());
            }
        };

        mFBRef.addValueEventListener(mValueEventListener);

        //hide soft keyboard
        //getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
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
            navigateUpTo(new Intent(this, FollowTournamentActivity.class));
            return true;
        }
        return super.onOptionsItemSelected(item);
    }



}
