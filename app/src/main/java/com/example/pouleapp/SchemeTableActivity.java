package com.example.pouleapp;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

import java.util.ArrayList;

import static android.R.color.darker_gray;

/**
 * Created by gezamenlijk on 26-2-2017.
 */

public class SchemeTableActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //setContentView(R.layout.activity_main_test);
        setContentView(R.layout.activity_scheme_table);
        ViewGroup radioGroup;
        ArrayList<Team> poule;
        PouleScheme pouleScheme;

        final GlobalData globalVariable = (GlobalData) getApplicationContext();

        poule = globalVariable.getPoule();
        pouleScheme = globalVariable.getPouleScheme();

        TableLayout tl = (TableLayout) findViewById(R.id.scheme_table);

        for (int i=0; i < poule.size()+1; i++) {
            TableRow tr = new TableRow(this);
            TextView tv = new TextView(this);
            tv.setMaxLines(1);
            setTextViewAttributes(tv);

            if (i==0) {
                tv.setText(""); //Top left remains empty: vertically home team, horizontally against team
                tv.setBackgroundColor(getResources().getColor(R.color.colorAccent));
            }
            else {
                tv.setText(poule.get(i-1).getTeamName()); // first column is home team
            }

            tr.addView(tv);

            for (int j=0; j < poule.size()+1; j++) {
                TextView tv1 = new TextView(this);
                tv1.setMaxLines(1);
                setTextViewAttributes(tv1);

                if (j==0) {
                    //skip first column
                } else {
                    if (i==0) {
                        // first row consists of team names
                        tv1.setText(poule.get(j-1).getTeamName());
                        tv1.setBackgroundColor(getResources().getColor(R.color.colorAccent));
                    } else if (i==j) {
                        // cells on the diagonal become gray
                        tv1.setText("");
                        tv1.setBackgroundColor(getResources().getColor(darker_gray));
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
                                intent.putExtra("SchemeRow",x);
                                intent.putExtra("SchemeColumn",y);
                                startActivity(intent);
                            }
                        });

                    }

                    tr.addView(tv1);
                }

            }

            tl.addView(tr);

        }

    }

    /** This method is called when ranking button is clicked */
    public void showRanking(View view) {
        Intent intent = new Intent(this, RankingActivity.class);
        startActivity(intent);
    }

    public void showTeamList(View view) {
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
    }

    private void setTextViewAttributes(TextView tv) {
        tv.setBackgroundResource(R.drawable.cell_shape);
        tv.setEllipsize(TextUtils.TruncateAt.END);
        tv.setPadding(5,5,5,5);

        tv.setLayoutParams(new TableRow.LayoutParams(0, TableRow.LayoutParams.WRAP_CONTENT, 1f));

    }

}
