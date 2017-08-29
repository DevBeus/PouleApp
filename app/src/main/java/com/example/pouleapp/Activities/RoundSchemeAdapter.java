package com.example.pouleapp.Activities;

/**
 * Created by gezamenlijk on 21-8-2017.
 */

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.TextView;

import com.example.pouleapp.Data.Match;
import com.example.pouleapp.Data.PouleScheme;
import com.example.pouleapp.Data.Round;
import com.example.pouleapp.R;


public class RoundSchemeAdapter extends BaseExpandableListAdapter {

    private Context mContext;
    private PouleScheme mPouleScheme;

    public RoundSchemeAdapter(Context context, PouleScheme pouleScheme) {
        this.mContext = context;
        this.mPouleScheme = pouleScheme;
    }

    @Override
    public Object getChild(int groupPosition, int childPosition) {
        // Numbering of rounds start at 1 iso 0
        Match[] matchList = mPouleScheme.getRoundMatchList(groupPosition+1);

        return matchList[childPosition];
    }

    @Override
    public long getChildId(int groupPosition, int childPosition) {
        return childPosition;
    }

    @Override
    public View getChildView(int groupPosition, int childPosition, boolean isLastChild, View view, ViewGroup parent) {

        Match match = (Match) getChild(groupPosition, childPosition);
        if (view == null) {
            LayoutInflater infalInflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            view = infalInflater.inflate(R.layout.child_items, null);
        }

        TextView tvMatch = (TextView) view.findViewById(R.id.child_items_match);
        tvMatch.setText(match.getMatchString());

        TextView tvResult = (TextView) view.findViewById(R.id.child_items_match_result);
        tvResult.setText((match.getResultString()));

        return view;
    }

    @Override
    public int getChildrenCount(int groupPosition) {

//        ArrayList<Match> childList = mRoundList.get(groupPosition).getChildList();
//        return childList.size();

        return mPouleScheme.getRoundMatchList(groupPosition+1).length;
    }

    @Override
    public Object getGroup(int groupPosition) {
        // Numbering of rounds start at 1 iso 0
        return mPouleScheme.getRound(groupPosition+1);
    }

    @Override
    public int getGroupCount() {
        // Numbering of rounds start at 1 iso 0
        return mPouleScheme.getNumberOfRounds()-1;
    }

    @Override
    public long getGroupId(int groupPosition) {
        return groupPosition;
    }

    @Override
    public View getGroupView(int groupPosition, boolean isLastChild, View view,
                             ViewGroup parent) {

        Round round = (Round) getGroup(groupPosition);
        if (view == null) {
            LayoutInflater inf = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            view = inf.inflate(R.layout.group_items, null);
        }

        TextView heading = (TextView) view.findViewById(R.id.group_items_text_view_heading);
        heading.setText(round.getRoundName());

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