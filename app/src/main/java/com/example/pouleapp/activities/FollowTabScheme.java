package com.example.pouleapp.activities;

import android.content.Intent;
import android.os.Bundle;
import androidx.fragment.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ExpandableListView;

import com.example.pouleapp.data.GlobalData;
import com.example.pouleapp.data.PublishPoule;
import com.example.pouleapp.data.PublishTournament;
import com.example.pouleapp.R;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import static com.example.pouleapp.data.GlobalData.POULE_INDEX;

/**
 * Created by gezamenlijk on 16-8-2017.
 * This class is used for the tab named Scheme
 * Code concept copied from https://www.simplifiedcoding.net/android-tablayout-example-using-viewpager-fragments/
 */

//Our class extending fragment
public class FollowTabScheme extends Fragment {
    private PublishTournament mPublishTournament;
    private FollowRoundSchemeAdapter mAdapter;
    private FirebaseDatabase mFBDB;
    private DatabaseReference mFBRef;
    private PublishPoule mPoule;
    private int mPouleIndex;



    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        //Returning the layout file after inflating
        //Change R.layout.tab_scheme in you classes

        final GlobalData globalVariable = (GlobalData) getActivity().getApplicationContext();
        mPublishTournament = globalVariable.getPublishTournament();
        mFBDB = globalVariable.getFirebaseDatabase();

        Intent intent = getActivity().getIntent();
        mPouleIndex = intent.getIntExtra(POULE_INDEX, 0);
        mPoule = mPublishTournament.getPouleList().get(mPouleIndex);

        getActivity().setTitle(getResources().getString(R.string.menu_poule_name_text) + mPoule.getPouleName());

        View v = inflater.inflate(R.layout.follow_tab_scheme, container, false);

        //  initiate the expandable list view
        ExpandableListView schemeExpandableListView = v.findViewById(R.id.follow_tab_scheme_expandable_list_view_scheme);

//        RoundSchemeAdapter rsAdapter = new RoundSchemeAdapter(v.getContext(), pouleScheme);

//        schemeExpandableListView.setAdapter(rsAdapter);

        mAdapter = new FollowRoundSchemeAdapter(v.getContext(), mPoule);

        schemeExpandableListView.setAdapter(mAdapter);

        for (int i=0; i<mPoule.getRoundList().size(); i++ ) {
            schemeExpandableListView.expandGroup(i);
        }

//        mFBRef = mFBDB.getReference(mPublishTournament.getTournamentInfo().getTournamentID());
//
//        mFBRef.addValueEventListener(new ValueEventListener() {
//            @Override
//            public void onDataChange(DataSnapshot dataSnapshot) {
//                PublishTournament pTournament = dataSnapshot.getValue(PublishTournament.class);
//                globalVariable.setPublishTournament(pTournament);
//                mPublishTournament = globalVariable.getPublishTournament();
//                mPoule.setRoundList(mPublishTournament.getPouleList().get(mPouleIndex).getRoundList());
//
//                mAdapter.notifyDataSetChanged();
//            }
//
//            @Override
//            public void onCancelled(DatabaseError error) {
//                // Failed to read value
//                //Log.e(TAG, "Failed to read app title value.", error.toException());
//            }
//        });

        return v;
    }


}
