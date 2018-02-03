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
import android.widget.Toast;

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
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Iterator;

import static com.example.pouleapp.Data.GlobalData.KEY_TOURNAMENT_INFO_LIST;
import static com.example.pouleapp.Data.GlobalData.KEY_TOURNAMENT_NAME;

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
    private ArrayList<String> mTournamentList = new ArrayList<String>();
    private ArrayList<String> mTournamentListKeys = new ArrayList<String>();
    private ArrayAdapter<String> mAdapter;
    private ValueEventListener mQueryValueListener;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_follow_tournament);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        //setSupportActionBar(toolbar);
        toolbar.setTitle(getResources().getString(R.string.app_name));

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        View header = navigationView.getHeaderView(0);
        TextView tvUser = (TextView) header.findViewById(R.id.nav_header_select_tournament_text_view_user);
        TextView tvEmail = (TextView) header.findViewById(R.id.nav_header_select_tournament_text_view_email);

        mFBAuth = FirebaseAuth.getInstance();
        mFBUser = mFBAuth.getCurrentUser();

        //Now check if this user is null
        if (mFBUser == null){
            //send user to the login page
            startActivity(new Intent(this, AuthUIActivity.class));
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

        tournamentListView = (ListView) findViewById(R.id.follow_tournament_content_list_view_tournaments);
        searchText = (EditText) findViewById(R.id.follow_tournament_content_edit_text_search_text);
        searchButton = (Button) findViewById(R.id.follow_tournament_content_button_find);


        mAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, mTournamentList);
        tournamentListView.setAdapter(mAdapter);

        addChildEventListener();

        tournamentListView.setOnItemClickListener(
                new AdapterView.OnItemClickListener() {
                    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                        mSelectedPosition = position;
                        // Toast.makeText(getApplicationContext(), mTournamentListKeys.get(position) + ": " + mTournamentList.get(position), Toast.LENGTH_LONG).show();

                        final GlobalData globalVariable = (GlobalData) getApplicationContext();
                        globalVariable.setTournamentSearchInfo(mPublishTournamentSearchInfoList.get(position));

                        Intent intent = new Intent(getApplicationContext(), FollowTournamentInfoActivity.class);

                        startActivity(intent);

                    }
                });


        mQueryValueListener = new ValueEventListener() {

            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                Iterable<DataSnapshot> snapshotIterator = dataSnapshot.getChildren();
                Iterator<DataSnapshot> iterator = snapshotIterator.iterator();

                mTournamentList.clear();
                mTournamentListKeys.clear();

                while (iterator.hasNext()) {
                    DataSnapshot next = (DataSnapshot) iterator.next();

                    PublishTournamentSearchInfo sInfo = (PublishTournamentSearchInfo) next.getValue(PublishTournamentSearchInfo.class);
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
    }

    private void addChildEventListener() {
//        ChildEventListener childListener = new ChildEventListener() {
//
//            @Override
//            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
//                //String tournamentName = (String) dataSnapshot.child(KEY_TOURNAMENT_NAME).getValue();
//                PublishTournamentSearchInfo sInfo = (PublishTournamentSearchInfo) dataSnapshot.getValue(PublishTournamentSearchInfo.class);
//                String tournamentName = sInfo.getTournamentName();
//                mTournamentList.add(tournamentName);
//                mTournamentListKeys.add(dataSnapshot.getKey());
//
//                mAdapter.notifyDataSetChanged();
//            }
//
//            @Override
//            public void onChildChanged(DataSnapshot dataSnapshot, String s) {
//
//            }
//
//            @Override
//            public void onChildMoved(DataSnapshot dataSnapshot, String s) {
//            }
//
//            @Override
//            public void onChildRemoved(DataSnapshot dataSnapshot) {
//                String key = dataSnapshot.getKey();
//                int index = mTournamentListKeys.indexOf(key);
//
//                if (index != -1) {
//                    mTournamentList.remove(index);
//                    mTournamentListKeys.remove(index);
//                    mAdapter.notifyDataSetChanged();
//                }
//            }
//
//            @Override
//            public void onCancelled(DatabaseError databaseError) {
//            }
//        };

        ValueEventListener valueEventListener = new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                Iterable<DataSnapshot> snapshotIterator = dataSnapshot.getChildren();
                Iterator<DataSnapshot> iterator = snapshotIterator.iterator();

                mPublishTournamentSearchInfoList.clear();
                mTournamentList.clear();
                mTournamentListKeys.clear();

                while (iterator.hasNext()) {
                    DataSnapshot next = (DataSnapshot) iterator.next();

                    //String tournamentName = (String) next.child(KEY_TOURNAMENT_NAME).getValue();
                    PublishTournamentSearchInfo sInfo = (PublishTournamentSearchInfo) next.getValue(PublishTournamentSearchInfo.class);
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

        //mFBRef.addChildEventListener(childListener);
        mFBRef.addListenerForSingleValueEvent(valueEventListener);
    }

    public void searchItems(View view) {

        Query query;

        if (!mSearchMode) {
            searchButton.setText("Clear");
            query = mFBRef.orderByChild(KEY_TOURNAMENT_NAME).equalTo(searchText.getText().toString());
            mSearchMode = true;
        } else {
            mSearchMode = false;
            searchButton.setText("Find");
            searchText.setText("");
            query = mFBRef.orderByKey();
        }

        if (mItemSelected) {
            //dataListView.setItemChecked(selectedPosition, false);
            mItemSelected = false;
        }

        query.addListenerForSingleValueEvent(mQueryValueListener);
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
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
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_log_out) {
            // Handle log out
            mFBAuth.signOut();

            Intent intent = new Intent(this, AuthUIActivity.class);
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

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
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
