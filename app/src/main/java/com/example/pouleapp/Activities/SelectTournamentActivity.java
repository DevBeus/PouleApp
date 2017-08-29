package com.example.pouleapp.Activities;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.TypedValue;
import android.view.LayoutInflater;
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

import java.util.ArrayList;
import java.util.List;

/**
 * Created by gezamenlijk on 8-8-2017.
 */

public class SelectTournamentActivity extends AppCompatActivity {
    final Context mContext = this;

    private SwipeMenuListView mListView;
    private ArrayList<String> mArrayList=new ArrayList<>();
    private ListDataAdapter mListDataAdapter;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_select_tournament);

        final GlobalData globalVariable = (GlobalData) getApplicationContext();
        globalVariable.initApp();

        List<String> tournamentlist = globalVariable.getTournamentNameList();

        initListView(tournamentlist);

    }

    private void initListView(List<String> list) {
        mListView=(SwipeMenuListView)findViewById(R.id.select_tournament_list_view_tournaments);
        mListView.setSwipeDirection(SwipeMenuListView.DIRECTION_LEFT);

        for (int i=0;i<list.size();i++){
            mArrayList.add(list.get(i));
        }

        mListDataAdapter=new ListDataAdapter();
        mListView.setAdapter(mListDataAdapter);

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
        mListView.setMenuCreator(creator);
        mListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {

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

        mListView.setOnMenuItemClickListener(new SwipeMenuListView.OnMenuItemClickListener() {

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
                holder.mTextview=(TextView)convertView.findViewById(R.id.list_item_text_view_1);

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
        //final GlobalData globalVariable = (GlobalData) getApplicationContext();

        // get enter_tournament_dialog.xml view
        LayoutInflater li = LayoutInflater.from(mContext);
        View DialogView = li.inflate(R.layout.enter_tournament_name_dialog, null);

        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(mContext);

        // set enter_tournament_dialog.xml to alertdialog builder
        alertDialogBuilder.setView(DialogView);

        final EditText etTournamentName = (EditText) DialogView.findViewById(R.id.enter_tournament_name_dialog_edit_text_tournament_name);

        final String tournamentName;

        alertDialogBuilder
                .setCancelable(false)
                .setPositiveButton("OK",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog,int id) {
                                // get user input and create new tournament

                                final GlobalData globalVariable = (GlobalData) getApplicationContext();
                                globalVariable.addTournament(etTournamentName.getText().toString());
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
