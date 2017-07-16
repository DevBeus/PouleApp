package com.example.pouleapp;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ListView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Collections;

import static com.example.pouleapp.GlobalData.POULE_INDEX;

public class RankingActivity extends AppCompatActivity implements SimpleGestureFilter.SimpleGestureListener {
    private SimpleGestureFilter detector;
    private int mPoule_Index = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ranking);
        // Detect touched area
        detector = new SimpleGestureFilter(this,this);

        final GlobalData globalVariable = (GlobalData) getApplicationContext();
        Tournament tournament = globalVariable.getTournament();
        ArrayList<Poule> pouleList = tournament.getPouleList();

        Intent intent = getIntent();
        mPoule_Index = intent.getIntExtra(POULE_INDEX,0);

        Poule poule = pouleList.get(mPoule_Index);
        ArrayList<Team> teamList = poule.getTeamList();
        setTitle(getResources().getString(R.string.menu_poule_name_text) + poule.getPouleName());

        ArrayList<Team> sortedPoule = new ArrayList<>(teamList);

        Collections.sort(sortedPoule, Team.RankingComparator);

        ListView lview = (ListView) findViewById(R.id.listview);
        RankingAdapter adapter = new RankingAdapter(this, sortedPoule);
        lview.setAdapter(adapter);

        adapter.notifyDataSetChanged();

    }

    @Override
    public boolean dispatchTouchEvent(MotionEvent me){
        // Call onTouchEvent of SimpleGestureFilter class
        this.detector.onTouchEvent(me);
        return super.dispatchTouchEvent(me);
    }
    @Override
    public void onSwipe(int direction) {
        String str = "";
        Intent intent;

        switch (direction) {

            case SimpleGestureFilter.SWIPE_RIGHT : str = "Swipe Right";
                intent = new Intent(this, SchemeActivity.class);
                intent.putExtra(POULE_INDEX, mPoule_Index);
                startActivity(intent);
                break;

            case SimpleGestureFilter.SWIPE_LEFT :  str = "Swipe Left";
                intent = new Intent(getApplicationContext(), SchemeTableActivity.class);
                intent.putExtra(POULE_INDEX, mPoule_Index);
                startActivity(intent);
                break;

            case SimpleGestureFilter.SWIPE_DOWN :  str = "Swipe Down";
                break;
            case SimpleGestureFilter.SWIPE_UP :    str = "Swipe Up";
                break;

        }
        //Toast.makeText(this, str, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onDoubleTap() {
        //Toast.makeText(this, "Double Tap", Toast.LENGTH_SHORT).show();
    }
    /** This method is called when Tournament button is clicked */
    public void showTournament(View view) {
        Intent intent = new Intent(this, TournamentActivity.class);

        startActivity(intent);
    }

    /** This method is called when Scheme button is clicked */
    public void showScheme(View view) {
        Intent intent = new Intent(this, SchemeActivity.class);
        intent.putExtra(POULE_INDEX, mPoule_Index);
        startActivity(intent);
    }

    /** This method is called when Scheme button is clicked */
    public void showSchemeTable(View view) {
        Intent intent = new Intent(this, SchemeTableActivity.class);
        intent.putExtra(POULE_INDEX, mPoule_Index);
        startActivity(intent);
    }

    /** This method is called when Team List button is clicked */
    public void showPoule(View view) {
        Intent intent = new Intent(this, PouleActivity.class);
        intent.putExtra(POULE_INDEX, mPoule_Index);
        startActivity(intent);
    }

}
