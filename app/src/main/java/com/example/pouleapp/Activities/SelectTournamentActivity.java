package com.example.pouleapp.Activities;

import android.app.AlertDialog;
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
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.EditText;
import android.widget.TextView;

import com.baoyz.swipemenulistview.SwipeMenu;
import com.baoyz.swipemenulistview.SwipeMenuCreator;
import com.baoyz.swipemenulistview.SwipeMenuItem;
import com.baoyz.swipemenulistview.SwipeMenuListView;
import com.example.pouleapp.Data.GlobalData;
import com.example.pouleapp.R;
import com.firebase.ui.auth.AuthUI;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by gezamenlijk on 8-8-2017.
 * This class is used for the selection of a tournament or creation of a new one
 */

public class SelectTournamentActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {
    final Context mContext = this;

    private FirebaseAuth mFBAuth;
    private FirebaseUser mFBUser;
    private String mUserName, mUserEmail;

    //    private SwipeMenuListView mListView;
    private ArrayList<String> mArrayList=new ArrayList<>();
    private ListDataAdapter mListDataAdapter;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_select_tournament);

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

        final GlobalData globalVariable = (GlobalData) getApplicationContext();
        globalVariable.initApp();

        List<String> tournamentlist = globalVariable.getTournamentNameList();

        initListView(tournamentlist);

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


    private void initListView(List<String> list) {
        SwipeMenuListView listView;

        listView= findViewById(R.id.select_tournament_list_view_tournaments);
        listView.setSwipeDirection(SwipeMenuListView.DIRECTION_LEFT);

        for (int i=0;i<list.size();i++){
            mArrayList.add(list.get(i));
        }

        mListDataAdapter=new ListDataAdapter();
        listView.setAdapter(mListDataAdapter);

        SwipeMenuCreator creator = new SwipeMenuCreator() {

            @Override
            public void create(SwipeMenu menu) {
                // Create different menus depending on the view type
                SwipeMenuItem goodItem = new SwipeMenuItem(getApplicationContext());
                //goodItem.setBackground(new ColorDrawable(Color.rgb(0x30, 0xB1, 0xF5)));
                goodItem.setWidth(dp2px(48));
                goodItem.setIcon(R.mipmap.ic_edit_sq_48);
                goodItem.setIcon(R.drawable.ic_edit_48dp);
                goodItem.setBackground(R.color.colorPrimary);
                menu.addMenuItem(goodItem);

                // create "delete" item
                SwipeMenuItem deleteItem = new SwipeMenuItem(getApplicationContext());
                //deleteItem.setBackground(new ColorDrawable(Color.rgb(0xF9, 0x3F, 0x25)));
                deleteItem.setWidth(dp2px(48));
                //deleteItem.setIcon(R.mipmap.ic_recycle_bin_sq_48);
                deleteItem.setIcon(R.drawable.ic_delete_48dp);
                deleteItem.setBackground(R.color.colorRed);
                menu.addMenuItem(deleteItem);
            }

        };

        // set creator
        listView.setMenuCreator(creator);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> a, View v, int position, long id) {
                // startTournamentActivity
                final GlobalData globalVariable = (GlobalData) getApplicationContext();
                String tournamentID = globalVariable.getTournamentIDList().get(position);
                globalVariable.initTournament(tournamentID);

                Intent intent = new Intent(getApplicationContext(), TournamentActivity.class);

                startActivity(intent);
            }
        });

        listView.setOnMenuItemClickListener(new SwipeMenuListView.OnMenuItemClickListener() {

            @Override
            public boolean onMenuItemClick(int position, SwipeMenu menu, int index) {
                switch (index) {
                    case 0:
                        //Toast.makeText(SelectTournamentActivity.this,"Position: "+position+ " selected",Toast.LENGTH_SHORT).show();
                        // startTournamentActivity
                        final GlobalData globalVariable = (GlobalData) getApplicationContext();
                        String tournamentID = globalVariable.getTournamentIDList().get(position);
                        globalVariable.initTournament(tournamentID);

                        Intent intent = new Intent(getApplicationContext(), TournamentActivity.class);

                        startActivity(intent);

                        break;
                    case 1:
                        deleteTournament(position);
                        mListDataAdapter.notifyDataSetChanged();
                        //Toast.makeText(TournamentActivity.this,"Item deleted",Toast.LENGTH_SHORT).show();
                        break;
                }

                return true;
            }

        });

        //listView
        listView.setOnMenuStateChangeListener(new SwipeMenuListView.OnMenuStateChangeListener() {
            @Override
            public void onMenuOpen(int position) {
            }

            @Override
            public void onMenuClose(int position) {
            }

        });

        listView.setOnSwipeListener(new SwipeMenuListView.OnSwipeListener() {
            @Override
            public void onSwipeStart(int position) {
            }

            @Override
            public void onSwipeEnd(int position) {
            }

        });

    }

    private class ListDataAdapter extends BaseAdapter {
        ViewHolder holder;

        @Override
        public int getCount() { return mArrayList.size(); }

        @Override
        public Object getItem(int i) { return null; }

        @Override
        public long getItemId(int i) { return 0; }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            if(convertView==null){
                holder=new ViewHolder();
                final ViewGroup nullParent = null; // introduced to avoid warning

                convertView=getLayoutInflater().inflate(R.layout.list_item,nullParent);
                holder.mTextview= convertView.findViewById(R.id.list_item_text_view_1);

                convertView.setTag(holder);

            } else {
                holder= (ViewHolder) convertView.getTag();
            }

            holder.mTextview.setText(mArrayList.get(position));

            return convertView;
        }

        class ViewHolder { TextView mTextview; }

    }

    private int dp2px(int dp) {
        return (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dp,getResources().getDisplayMetrics());
    }

    public void addTournament(View view) {
         // get enter_tournament_dialog.xml view
        LayoutInflater li = LayoutInflater.from(mContext);
        final ViewGroup nullParent = null; // introduced to avoid warning

        View DialogView = li.inflate(R.layout.dialog_enter_tournament_name, nullParent);

        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(mContext);

        // set enter_tournament_dialog.xml to alertdialog builder
        alertDialogBuilder.setView(DialogView);

        final EditText etTournamentName = DialogView.findViewById(R.id.dialog_enter_tournament_name_edit_text_tournament_name);

        alertDialogBuilder
                .setCancelable(false)
                .setPositiveButton("OK",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog,int id) {
                                // get user input and create new tournament

                                final GlobalData globalVariable = (GlobalData) getApplicationContext();
                                globalVariable.addTournament(etTournamentName.getText().toString().trim());
                                globalVariable.saveAppData();
                             }
                        })
                .setNegativeButton("Cancel",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog,int id) {
                                dialog.cancel();
                            }
                        })
                .setOnDismissListener(new DialogInterface.OnDismissListener() {
                        // Needed to refresh listView after update
                        @Override
                        public void onDismiss(DialogInterface dialog) {
                             recreate();
                        }
                                      }
                    );

        // create alert enter_tournament_dialog.xml
        AlertDialog EnterTournamentNameDialog = alertDialogBuilder.create();

        // show it
        EnterTournamentNameDialog.show();
    }

    public void deleteTournament(int pos) {
        final GlobalData globalVariable = (GlobalData) getApplicationContext();
//        ArrayList<String> tournamentIDList = globalVariable.getTournamentIDList();

        globalVariable.deleteTournament(pos);
        globalVariable.saveAppData();

        recreate();
    }



}
