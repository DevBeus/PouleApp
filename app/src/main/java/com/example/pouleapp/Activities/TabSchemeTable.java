package com.example.pouleapp.Activities;

/**
 * Created by gezamenlijk on 16-8-2017.
 */

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.content.res.ResourcesCompat;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

import com.example.pouleapp.Data.GlobalData;
import com.example.pouleapp.Data.Poule;
import com.example.pouleapp.Data.PouleScheme;
import com.example.pouleapp.Data.Team;
import com.example.pouleapp.Data.Tournament;
import com.example.pouleapp.R;

import java.util.ArrayList;

import static com.example.pouleapp.Data.GlobalData.POULE_INDEX;
import static com.example.pouleapp.Data.GlobalData.PREVIOUS_ACTIVITY;
import static com.example.pouleapp.Data.GlobalData.PREVIOUS_TAB;
import static com.example.pouleapp.Data.GlobalData.SCHEMETABLE_TAB;
import static com.example.pouleapp.Data.GlobalData.SCHEME_COLUMN;
import static com.example.pouleapp.Data.GlobalData.SCHEME_ROW;
import static com.example.pouleapp.Data.GlobalData.SCHEME_TAB;
import static com.example.pouleapp.Data.GlobalData.SCHEME_TABLE_ACTIVITY;

/**
 * Created by Belal on 2/3/2016.
 */

//Our class extending fragment
public class TabSchemeTable extends Fragment {
    private int mPoule_Index = 0;

    //Overriden method onCreateView
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        //Returning the layout file after inflating
        //Change R.layout.tab_scheme_table in you classes

        View v = inflater.inflate(R.layout.tab_scheme_table, container, false);

        final GlobalData globalVariable = (GlobalData) getActivity().getApplicationContext();
        Tournament tournament = globalVariable.getTournament();
        ArrayList<Poule> pouleList = tournament.getPouleList();

        Intent intent = getActivity().getIntent();
        mPoule_Index = intent.getIntExtra(POULE_INDEX,0);

        Poule poule = pouleList.get(mPoule_Index);
        ArrayList<Team> teamList = poule.getTeamList();
        PouleScheme pouleScheme = poule.getPouleScheme();

        //setTitle(getResources().getString(R.string.menu_poule_name_text) + poule.getPouleName());

        TableLayout tl = (TableLayout) v.findViewById(R.id.tabSchemeTable_table);

        for (int i=0; i < teamList.size()+1; i++) {
            TableRow tr = new TableRow(v.getContext());
            TextView tv = new TextView(v.getContext());
            tv.setMaxLines(1);
            setTextViewAttributes(tv);

            if (i==0) {
                tv.setText(""); //Top left remains empty: vertically home team, horizontally against team
                tv.setBackgroundColor(ResourcesCompat.getColor(getResources(), R.color.colorYellow, null)); //null means no theme
            }
            else {
                tv.setText(teamList.get(i-1).getTeamName()); // first column is home team
            }

            tr.addView(tv);

            for (int j=0; j < teamList.size()+1; j++) {
                TextView tv1 = new TextView(v.getContext());
                tv1.setMaxLines(1);
                setTextViewAttributes(tv1);

                if (j==0) {
                    //skip first column
                } else {
                    if (i==0) {
                        // first row consists of team names
                        tv1.setText(teamList.get(j-1).getTeamName());
                        tv1.setBackgroundColor(ResourcesCompat.getColor(getResources(), R.color.colorYellow, null)); //null means no theme
                    } else if (i==j) {
                        // cells on the diagonal become gray
                        tv1.setText("");
                        tv1.setBackgroundColor(ResourcesCompat.getColor(getResources(), R.color.colorGray, null)); //null means no theme
                    }
                    else {
                        String t = pouleScheme.getMatchResult(i-1,j-1);
                        tv1.setText(t);
                        tv1.setClickable(true);

                        final int x = i-1;
                        final int y = j-1;

                        tv1.setOnClickListener(new View.OnClickListener(){
                            public void onClick(View v) {
                                Intent intent = new Intent(v.getContext(), EditMatchActivity.class);
                                intent.putExtra(PREVIOUS_ACTIVITY,SCHEME_TABLE_ACTIVITY);
                                intent.putExtra(PREVIOUS_TAB,SCHEMETABLE_TAB);
                                intent.putExtra(POULE_INDEX,mPoule_Index);
                                intent.putExtra(SCHEME_ROW,x);
                                intent.putExtra(SCHEME_COLUMN,y);
                                startActivity(intent);
                            }
                        });

                    }

                    tr.addView(tv1);
                }

            }

            tl.addView(tr);

        }

        return v;
    }

    private void setTextViewAttributes(TextView tv) {
        tv.setBackgroundResource(R.drawable.cell_shape);
        tv.setEllipsize(TextUtils.TruncateAt.END);
        tv.setPadding(5,5,5,5);

        tv.setLayoutParams(new TableRow.LayoutParams(0, TableRow.LayoutParams.WRAP_CONTENT, 1f));

    }

}
