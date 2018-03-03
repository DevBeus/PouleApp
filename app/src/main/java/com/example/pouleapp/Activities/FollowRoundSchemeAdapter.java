package com.example.pouleapp.Activities;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.TextView;

import com.example.pouleapp.Data.PublishMatch;
import com.example.pouleapp.Data.PublishPoule;
import com.example.pouleapp.Data.PublishRound;
import com.example.pouleapp.R;

import java.util.ArrayList;

/**
 * Created by gezamenlijk on 21-8-2017.
 * This class is used to map the scheme rounds to a list view
 */

class FollowRoundSchemeAdapter extends BaseExpandableListAdapter {

    private Context mContext;
    private PublishPoule mPoule;

    FollowRoundSchemeAdapter(Context context, PublishPoule poule) {
        this.mContext = context;
        this.mPoule = poule;
    }

    @Override
    public Object getChild(int groupPosition, int childPosition) {
        // Numbering of rounds start at 1 iso 0
        // Match[] matchList = mPouleScheme.getRoundMatchList(groupPosition+1);
        ArrayList<PublishRound> roundList = mPoule.getRoundList();

        return roundList.get(groupPosition).getMatchList().get(childPosition);
    }

    @Override
    public long getChildId(int groupPosition, int childPosition) {
        return childPosition;
    }

    @Override
    public View getChildView(int groupPosition, int childPosition, boolean isLastChild, View view, ViewGroup parent) {

        PublishMatch match = (PublishMatch) getChild(groupPosition, childPosition);

        if (view == null) {
            final ViewGroup nullParent = null; // introduced to avoid warning

            LayoutInflater infalInflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            view = infalInflater.inflate(R.layout.child_items, nullParent);
        }

        TextView tvMatch = view.findViewById(R.id.child_items_match);
        String matchString = match.getHomeTeam()+" - " + match.getOpponent();
        tvMatch.setText(matchString);

        TextView tvResult = view.findViewById(R.id.child_items_match_result);
        String gf = ((match.getGoalsFor()!=null)? match.getGoalsFor().toString() : "");
        String ga = ((match.getGoalsAgainst()!=null)? match.getGoalsAgainst().toString() : "");
        String resultString = gf + " - " + ga;
        tvResult.setText(resultString);

        return view;
    }

    @Override
    public int getChildrenCount(int groupPosition) {

//        ArrayList<Match> childList = mRoundList.get(groupPosition).getChildList();
//        return childList.size();
//        return mPouleScheme.getRoundMatchList(groupPosition+1).length;
        PublishRound round = mPoule.getRoundList().get(groupPosition);

        return round.getMatchList().size();
    }

    @Override
    public Object getGroup(int groupPosition) {
        // Numbering of rounds start at 1 iso 0
        // return mPouleScheme.getRound(groupPosition+1);

        return mPoule.getRoundList().get(groupPosition);
    }

    @Override
    public int getGroupCount() {
        // Numbering of rounds start at 1 iso 0
        // return mPouleScheme.getNumberOfRounds()-1;
        return mPoule.getRoundList().size();
    }

    @Override
    public long getGroupId(int groupPosition) {
        return groupPosition;
    }

    @Override
    public View getGroupView(int groupPosition, boolean isLastChild, View view,
                             ViewGroup parent) {

        PublishRound round = (PublishRound) getGroup(groupPosition);
        if (view == null) {
            final ViewGroup nullParent = null; // introduced to avoid warning

            LayoutInflater inf = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            view = inf.inflate(R.layout.group_items, nullParent);
        }

        TextView heading = view.findViewById(R.id.group_items_text_view_heading);
        heading.setText(mContext.getString(R.string.round_scheme_adapter_text_round)+ round.getRound());

        return view;
    }

    @Override
    public boolean hasStableIds() {
        return true;
    }

    @Override
    public boolean isChildSelectable(int groupPosition, int childPosition) {
        return true;
    }

}