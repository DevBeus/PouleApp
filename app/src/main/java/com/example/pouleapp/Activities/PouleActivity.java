package com.example.pouleapp.Activities;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.baoyz.swipemenulistview.SwipeMenu;
import com.baoyz.swipemenulistview.SwipeMenuCreator;
import com.baoyz.swipemenulistview.SwipeMenuItem;
import com.baoyz.swipemenulistview.SwipeMenuListView;
import com.example.pouleapp.Data.GlobalData;
import com.example.pouleapp.Data.Poule;
import com.example.pouleapp.Data.Team;
import com.example.pouleapp.Data.Tournament;
import com.example.pouleapp.R;

import java.util.ArrayList;

import static com.example.pouleapp.Data.GlobalData.ACTION_EDIT;
import static com.example.pouleapp.Data.GlobalData.ACTION_MESSAGE;
import static com.example.pouleapp.Data.GlobalData.POULE_INDEX;
import static com.example.pouleapp.Data.GlobalData.TEAM_INDEX;

public class PouleActivity extends AppCompatActivity {
    final Context mContext = this;

    private static int mPoule_Index = 0;
    private ArrayList<String> mArrayList=new ArrayList<>();
    private ListDataAdapter mListDataAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_poule);
        ArrayList<Team> teamList;
        ArrayList<Poule> pouleList;
        Poule poule;

        // Show the Up button in the action bar.
        android.support.v7.app.ActionBar actionBar = getSupportActionBar();

        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
        }

        final GlobalData globalVariable = (GlobalData) getApplicationContext();
        Tournament tournament = globalVariable.getTournament();
        Intent intent = getIntent();
        mPoule_Index = intent.getIntExtra(POULE_INDEX,0);

        pouleList = tournament.getPouleList();
        poule = pouleList.get(mPoule_Index);

        setTitle(getResources().getString(R.string.menu_poule_name_text) + poule.getPouleName());

        TextView tvPouleName = findViewById(R.id.poule_text_view_edit_poule_name);
        tvPouleName.setText(poule.getPouleName());

        teamList = poule.getTeamList();

        initListView(teamList);
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()) {
            // Respond to the action bar's Up/Home button
            case android.R.id.home:
                navigateUpTo(new Intent(this, TournamentActivity.class));
                return true;
            case R.id.action_help:
                Toast.makeText(getApplicationContext(), "Start Help Activity", Toast.LENGTH_LONG).show();

        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_help, menu);

        return true;
    }


    private void initListView(ArrayList<Team> list) {
        SwipeMenuListView listView= findViewById(R.id.poule_list_view_teams);
        listView.setSwipeDirection(SwipeMenuListView.DIRECTION_LEFT);

        for (int i=0;i<list.size();i++){
            mArrayList.add(list.get(i).getTeamName());
        }

        mListDataAdapter=new ListDataAdapter();
        listView.setAdapter(mListDataAdapter);
        SwipeMenuCreator creator = new SwipeMenuCreator() {

            @Override
            public void create(SwipeMenu menu) {
                // Create different menus depending on the view type
                SwipeMenuItem editItem = new SwipeMenuItem(getApplicationContext());
                editItem.setWidth(dp2px(48));
                editItem.setIcon(R.drawable.ic_edit_48dp);
                editItem.setBackground(R.color.colorPrimary);
                menu.addMenuItem(editItem);

                // create "delete" item
                SwipeMenuItem deleteItem = new SwipeMenuItem(getApplicationContext());
                deleteItem.setWidth(dp2px(48));
                deleteItem.setIcon(R.drawable.ic_delete_48dp);
                deleteItem.setBackground(R.color.colorRed);
                menu.addMenuItem(deleteItem);
            }

        };

        // set creator
        listView.setMenuCreator(creator);

        listView.setOnMenuItemClickListener(new SwipeMenuListView.OnMenuItemClickListener() {

            @Override

            public boolean onMenuItemClick(int position, SwipeMenu menu, int index) {
                switch (index) {
                    case 0:
                        Intent intent = new Intent(getApplicationContext(), EditTeamActivity.class);
                        intent.putExtra(ACTION_MESSAGE, ACTION_EDIT);
                        intent.putExtra(POULE_INDEX, mPoule_Index);
                        intent.putExtra(TEAM_INDEX, position);

                        startActivity(intent);

                        break;
                    case 1:
                        mArrayList.remove(position);
                        mListDataAdapter.notifyDataSetChanged();

                        ArrayList<Poule> pouleList;
                        Poule poule;

                        final GlobalData globalVariable = (GlobalData) getApplicationContext();
                        Tournament tournament = globalVariable.getTournament();
                        pouleList = tournament.getPouleList();
                        poule = pouleList.get(mPoule_Index);

                        if (poule.getTeamList().size()>2) {

                            poule.deleteTeam(position);
                            globalVariable.savePoule(poule);
                        } else {
                            String message = getResources().getString(R.string.team_removal_warning);
                            Toast.makeText(getApplicationContext(), message, Toast.LENGTH_LONG).show();
                        }

                        break;
                }

                return true;

            }

        });

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

                convertView=getLayoutInflater().inflate(R.layout.list_item, nullParent);
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

    public void editPouleName(View view) {
        // get dialog_enter_poule_name.xml view
        LayoutInflater li = LayoutInflater.from(mContext);
        final ViewGroup nullParent = null; // introduced to avoid warning

        View DialogView = li.inflate(R.layout.dialog_enter_poule_name, nullParent);

        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(mContext);

        // set dialog_enter_poule_name.xml to alertdialog builder
        alertDialogBuilder.setView(DialogView);

        final EditText etPouleName = DialogView.findViewById(R.id.dialog_enter_poule_name_edit_text_poule_name);

        alertDialogBuilder
                .setCancelable(false)
                .setPositiveButton("OK",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog,int id) {
                                // get user input and create new poule
                                final GlobalData globalVariable = (GlobalData) getApplicationContext();
                                Tournament tournament = globalVariable.getTournament();
                                ArrayList<Poule> pouleList = tournament.getPouleList();

                                //Check whether new poule name already exists
                                boolean found = false;
                                String pouleName = etPouleName.getText().toString().trim();

                                for (int i=0; i < pouleList.size(); i++) {
                                    if ((pouleList.get(i).getPouleName().equals(pouleName))) { found = true; }
                                }

                                if ( !found ) {
                                    pouleList.get(mPoule_Index).setPouleName(pouleName);

                                    globalVariable.saveTournament();
                                }
                                else {
                                    String message = getResources().getString(R.string.toast_message_poule_name_twice);
                                    Toast.makeText(getApplicationContext(), pouleName + message, Toast.LENGTH_LONG).show();
                                }


                                globalVariable.saveTournament();
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


    public void addTeam(View view) {
        // get dialog_enter_team_name.xml view
        LayoutInflater li = LayoutInflater.from(mContext);
        final ViewGroup nullParent = null; // introduced to avoid warning

        View DialogView = li.inflate(R.layout.dialog_enter_team_name, nullParent);

        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(mContext);

        // set dialog_enter_team_name.xml to alertdialog builder
        alertDialogBuilder.setView(DialogView);

        final EditText inputTeamName = DialogView.findViewById(R.id.dialog_enter_team_name_edit_text_team_name);

        alertDialogBuilder
                .setCancelable(false)
                .setPositiveButton("OK",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog,int id) {
                                // get user input and create new team
                                final GlobalData globalVariable = (GlobalData) getApplicationContext();

                                Tournament tournament = globalVariable.getTournament();
                                ArrayList<Poule> pouleList = tournament.getPouleList();
                                Poule poule = pouleList.get(mPoule_Index);

                                //Check whether new team name already exists
                                boolean found = false;
                                String teamName = inputTeamName.getText().toString().trim();
                                ArrayList<Team> teamList = poule.getTeamList();

                                for (int i=0; i < teamList.size(); i++) {
                                    if ((teamList.get(i).getTeamName().equals(teamName))) { found = true; }
                                }

                                if ( !found ) {
                                    poule.addTeam(teamName);

                                    globalVariable.saveTournament();
                                }
                                else {
                                    String message = getResources().getString(R.string.toast_message_team_name_twice);
                                    Toast.makeText(getApplicationContext(), teamName + message, Toast.LENGTH_LONG).show();
                                }

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

}

