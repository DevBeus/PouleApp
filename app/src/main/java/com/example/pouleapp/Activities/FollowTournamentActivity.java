package com.example.pouleapp.Activities;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;

import com.example.pouleapp.Data.GlobalData;
import com.example.pouleapp.Data.PublishTournamentSearchInfo;
import com.example.pouleapp.R;
import com.firebase.ui.auth.AuthUI;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Iterator;

import static com.example.pouleapp.Data.GlobalData.KEY_TOURNAMENT_INFO_LIST;

/**
 * Created by gezamenlijk on 6-1-2018.
 */

public class FollowTournamentActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {
    final Context mContext = this;

    private ListView tournamentListView;
    private EditText searchText;
    private Button searchButton;
    private int selectedPosition = 0;

    private FirebaseAuth mFBAuth;
    private FirebaseUser mFBUser;
    private FirebaseDatabase mFBDB;
    private DatabaseReference mFBRef;
    private String mUserName, mUserEmail;

    private Boolean mSearchMode = false;
    private Boolean mItemSelected = false;
    private int mSelectedPosition = 0;

    private ArrayList<PublishTournamentSearchInfo> mPublishTournamentSearchInfoList = new ArrayList<>();
    private ArrayList<String> mTournamentList = new ArrayList<>();
    private ArrayList<String> mTournamentListKeys = new ArrayList<>();
    private ArrayAdapter<String> mAdapter;
    private ValueEventListener mQueryValueListener;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_follow_tournament);

        Toolbar toolbar = findViewById(R.id.toolbar);
        //setSupportActionBar(toolbar);
        toolbar.setTitle(getResources().getString(R.string.app_name));

        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        View header = navigationView.getHeaderView(0);
        TextView tvUser = header.findViewById(R.id.nav_header_select_tournament_text_view_user);
        TextView tvEmail = header.findViewById(R.id.nav_header_select_tournament_text_view_email);

        mFBAuth = FirebaseAuth.getInstance();
        mFBUser = mFBAuth.getCurrentUser();

        //Now check if this user is null
        if (mFBUser == null){
            //send user to the login page
            startActivity(new Intent(this, WelcomeActivity.class));
            return;
        } else {
            mUserName = mFBUser.getDisplayName();
            mUserEmail = mFBUser.getEmail();
        }

        tvUser.setText(mUserName);
        tvEmail.setText(mUserEmail);

        mFBDB = FirebaseDatabase.getInstance();

        final GlobalData globalVariable = (GlobalData) getApplicationContext();
        globalVariable.setFirebaseAuth(mFBAuth);
        globalVariable.setFirebaseUser(mFBUser);
        globalVariable.setFirebaseDatabase(mFBDB);

        mFBRef = mFBDB.getReference(KEY_TOURNAMENT_INFO_LIST);

        tournamentListView = findViewById(R.id.follow_tournament_content_list_view_tournaments);
        searchText = findViewById(R.id.follow_tournament_content_edit_text_search_text);
        searchButton = findViewById(R.id.follow_tournament_content_button_find);


        mAdapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, mTournamentList);
        tournamentListView.setAdapter(mAdapter);

        addChildEventListener();

        tournamentListView.setOnItemClickListener(
                new AdapterView.OnItemClickListener() {
                    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                        mSelectedPosition = position;
                        // Toast.makeText(getApplicationContext(), mTournamentListKeys.get(position) + ": " + mTournamentList.get(position), Toast.LENGTH_LONG).show();

                        String tournamentID = mTournamentListKeys.get(position);
                        PublishTournamentSearchInfo matchInfo = new PublishTournamentSearchInfo("","","","");

                        for (PublishTournamentSearchInfo info: mPublishTournamentSearchInfoList) {
                            if (info.getTournamentID().equals(tournamentID)) {
                                matchInfo = info;
                                break;
                            }
                        }

                        final GlobalData globalVariable = (GlobalData) getApplicationContext();
                        // globalVariable.setTournamentSearchInfo(mPublishTournamentSearchInfoList.get(position));
                        globalVariable.setTournamentSearchInfo(matchInfo);

                        Intent intent = new Intent(getApplicationContext(), FollowTournamentInfoActivity.class);

                        startActivity(intent);

                    }
                });

    }

    private void addChildEventListener() {

        ValueEventListener valueEventListener = new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                Iterable<DataSnapshot> snapshotIterator = dataSnapshot.getChildren();
                Iterator<DataSnapshot> iterator = snapshotIterator.iterator();

                mPublishTournamentSearchInfoList.clear();
                mTournamentList.clear();
                mTournamentListKeys.clear();

                while (iterator.hasNext()) {
                    DataSnapshot next = iterator.next();

                    //String tournamentName = (String) next.child(KEY_TOURNAMENT_NAME).getValue();
                    PublishTournamentSearchInfo sInfo = next.getValue(PublishTournamentSearchInfo.class);
                    mPublishTournamentSearchInfoList.add(sInfo);

                    String tournamentName = sInfo.getTournamentName();
                    String key = next.getKey();

                    mTournamentListKeys.add(key);
                    mTournamentList.add(tournamentName);

                }

                mAdapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        };

        mFBRef.addListenerForSingleValueEvent(valueEventListener);
    }

    public void searchItems(View view) {

        if (!mSearchMode) {
            searchButton.setText("Clear");

            mTournamentList.clear();
            mTournamentListKeys.clear();

            for (PublishTournamentSearchInfo info: mPublishTournamentSearchInfoList) {
                String matchStr = searchText.getText().toString().toLowerCase().trim();

                // Alternative could be org.apache.commons.lang3.StringUtils.containsIgnoreCase("AbBaCca", "bac");
                if (info.getTournamentName().toLowerCase().contains(matchStr)) {
                    mTournamentList.add(info.getTournamentName());
                    mTournamentListKeys.add(info.getTournamentID());
                }
            }

//            query = mFBRef.orderByChild(KEY_TOURNAMENT_NAME).equalTo(searchText.getText().toString());
            mSearchMode = true;
        } else {
            mSearchMode = false;
            searchButton.setText("Find");
            searchText.setText("");

            mTournamentList.clear();
            mTournamentListKeys.clear();

            for (PublishTournamentSearchInfo info: mPublishTournamentSearchInfoList) {
                mTournamentList.add(info.getTournamentName());
                mTournamentListKeys.add(info.getTournamentID());
            }
        }

        mAdapter.notifyDataSetChanged();
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        //getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();


        return super.onOptionsItemSelected(item);
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_log_out) {
            // Handle log out
            mFBAuth.signOut();

            Intent intent = new Intent(this, WelcomeActivity.class);
            startActivity(intent);

        } else if (id == R.id.nav_delete_account) {
            // handle delete account

            new android.support.v7.app.AlertDialog.Builder(this)
                    .setMessage("Are you sure you want to delete this account?")
                    .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            deleteAccount();
                        }
                    })
                    .setNegativeButton("Cancel", null)
                    .show();

        }

        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    private void deleteAccount() {
        AuthUI.getInstance()
                .delete(this)
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if (task.isSuccessful()) {
                            recreate();
                        }
                    }
                });
    }


}
