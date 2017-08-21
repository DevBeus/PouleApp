package com.example.pouleapp.Activities;

import android.app.ActionBar;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.NavUtils;
import android.support.v7.app.AppCompatActivity;
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
import com.example.pouleapp.Data.Poule;
import com.example.pouleapp.Data.Tournament;
import com.example.pouleapp.R;

import java.util.ArrayList;

import static com.example.pouleapp.Data.GlobalData.POULE_INDEX;
import static com.example.pouleapp.Data.GlobalData.TEAM_INDEX;

public class TournamentActivity extends AppCompatActivity {
    final Context mContext = this;

    private SwipeMenuListView mListView;
    private ArrayList<String> mArrayList=new ArrayList<>();
    private ListDataAdapter mListDataAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tournament);
        ArrayList<Poule> pouleList;

        // Show the Up button in the action bar.
        android.support.v7.app.ActionBar actionBar = getSupportActionBar();

        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
        }

        final GlobalData globalVariable = (GlobalData) getApplicationContext();
        Tournament tournament = globalVariable.getTournament();

        setTitle(getResources().getString(R.string.menu_tournament_name_text) + tournament.getTournamentName());
        //globalVariable.initPouleList();
        pouleList = tournament.getPouleList();

//        TextView tvTournamentName = (TextView) findViewById(R.id.editTournamentName);
//        tvTournamentName.setText(tournament.getTournamentName());
//
//        TextView tvTournamentLocation = (TextView) findViewById(R.id.editTournamentLocation);
//        tvTournamentLocation.setText(tournament.getLocation());

        initListView(pouleList);

        //hide soft keyboard
        //getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
    }

    private void initListView(ArrayList<Poule> list) {
        mListView=(SwipeMenuListView)findViewById(R.id.listViewPoules);
        mListView.setSwipeDirection(SwipeMenuListView.DIRECTION_LEFT);

        for (int i=0;i<list.size();i++){
            mArrayList.add(list.get(i).getPouleName());
        }

        // mListView.setCloseInterpolator(new BounceInterpolator());

        mListDataAdapter=new ListDataAdapter();
        mListView.setAdapter(mListDataAdapter);
        SwipeMenuCreator creator = new SwipeMenuCreator() {

            @Override
            public void create(SwipeMenu menu) {
                // Create different menus depending on the view type
                SwipeMenuItem goodItem = new SwipeMenuItem(getApplicationContext());
                //goodItem.setBackground(new ColorDrawable(Color.rgb(0x30, 0xB1, 0xF5)));
                goodItem.setWidth(dp2px(48));
                goodItem.setIcon(R.drawable.ic_edit_48dp);
                goodItem.setBackground(R.color.colorPrimary);
                menu.addMenuItem(goodItem);

                // create "delete" item
                SwipeMenuItem deleteItem = new SwipeMenuItem(getApplicationContext());
                //deleteItem.setBackground(new ColorDrawable(Color.rgb(0xF9, 0x3F, 0x25)));
                deleteItem.setWidth(dp2px(48));
                deleteItem.setIcon(R.drawable.ic_delete_48dp);
                deleteItem.setBackground(R.color.colorRed);
                menu.addMenuItem(deleteItem);
            }

        };

        // set creator
        mListView.setMenuCreator(creator);
        mListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> a, View v, int position, long id) {
                // startSchemeActivity
                Intent intent = new Intent(getApplicationContext(), SchemeRankingActivity.class);
                intent.putExtra(POULE_INDEX, position);

                startActivity(intent);
            }
        });

        mListView.setOnMenuItemClickListener(new SwipeMenuListView.OnMenuItemClickListener() {

            @Override
            public boolean onMenuItemClick(int position, SwipeMenu menu, int index) {
                switch (index) {
                    case 0:
                        //Toast.makeText(TournamentActivity.this,"Like button press",Toast.LENGTH_SHORT).show();
                        // startPouleActivity
                        Intent intent = new Intent(getApplicationContext(), PouleActivity.class);
                        intent.putExtra(POULE_INDEX, position);
                        intent.putExtra(TEAM_INDEX,0);

                        startActivity(intent);

                        break;
                    case 1:
                        deletePoule(position);
                        mListDataAdapter.notifyDataSetChanged();
                        //Toast.makeText(TournamentActivity.this,"Item deleted",Toast.LENGTH_SHORT).show();
                        break;
                }

                return true;
            }

        });

        //mListView
        mListView.setOnMenuStateChangeListener(new SwipeMenuListView.OnMenuStateChangeListener() {
            @Override
            public void onMenuOpen(int position) {
            }

            @Override
            public void onMenuClose(int position) {
            }

        });

        mListView.setOnSwipeListener(new SwipeMenuListView.OnSwipeListener() {
            @Override
            public void onSwipeStart(int position) {
            }

            @Override
            public void onSwipeEnd(int position) {
            }

        });

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);

        return true;
    }

//    @Override
//    public boolean onOptionsItemSelected(MenuItem item) {
//        int id = item.getItemId();
//
//        if (id == R.id.action_settings) {
//            Intent intent = new Intent(this, TournamentSettingsActivity.class);
//
//            startActivity(intent);
//        }
//
//        return super.onOptionsItemSelected(item);
//    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        Intent intent;

        switch (item.getItemId()) {
            // Respond to the action bar's Up/Home button
            case android.R.id.home:
                navigateUpTo(new Intent(this, SelectTournamentActivity.class));
                return true;
//                intent = new Intent(this, SelectTournamentActivity.class);
//
//                startActivity(intent);

            case R.id.action_settings:
                intent = new Intent(this, TournamentSettingsActivity.class);

                startActivity(intent);

        }
        return super.onOptionsItemSelected(item);
    }

    class ListDataAdapter extends BaseAdapter {
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

                convertView=getLayoutInflater().inflate(R.layout.list_item,null);
                holder.mTextview=(TextView)convertView.findViewById(R.id.textView);

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

    public void saveTournament(View view) {
        final GlobalData globalVariable = (GlobalData) getApplicationContext();
//        Tournament tournament = globalVariable.getTournament();

//        TextView tvTournamentName = (TextView) findViewById(R.id.editTournamentName);
//        String name = tvTournamentName.getText().toString();
//
//        TextView tvTournamentLocation = (TextView) findViewById(R.id.editTournamentLocation);
//        String location = tvTournamentLocation.getText().toString();

//        tournament.setTournamentName(name);
//        tournament.setLocation(location);
//
        globalVariable.saveTournament();
//        globalVariable.updateTournamentName(name);
//        globalVariable.saveAppData();

        recreate();

    }

    public void addPoule(View view) {
        // get enter_poule_name_dialog.xml view
        LayoutInflater li = LayoutInflater.from(mContext);
        View DialogView = li.inflate(R.layout.enter_poule_name_dialog, null);

        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(mContext);

        // set enter_poule_name_dialog.xml to alertdialog builder
        alertDialogBuilder.setView(DialogView);

        final EditText etPouleName = (EditText) DialogView.findViewById(R.id.editTextPouleName);

        alertDialogBuilder
                .setCancelable(false)
                .setPositiveButton("OK",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog,int id) {
                                // get user input and create new poule

                                final GlobalData globalVariable = (GlobalData) getApplicationContext();
                                Tournament tournament = globalVariable.getTournament();
                                ArrayList<Poule> pouleList = tournament.getPouleList();
                                int pouleId = pouleList.size();
                                //Poule poule = new Poule(pouleId,"Poule"+pouleId);
                                Poule poule = new Poule(pouleId,etPouleName.getText().toString());
                                pouleList.add(poule);

                                globalVariable.saveTournament();

                                //globalVariable.addTournament(etTournamentName.getText().toString());
                                //globalVariable.saveAppData();
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

    public void deletePoule(int index) {
        final GlobalData globalVariable = (GlobalData) getApplicationContext();
        Tournament tournament = globalVariable.getTournament();
        ArrayList<Poule> pouleList = tournament.getPouleList();
        pouleList.remove(index);

        globalVariable.saveTournament();

        recreate();

    }


}