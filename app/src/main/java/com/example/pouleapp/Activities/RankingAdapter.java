package com.example.pouleapp.Activities;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.example.pouleapp.Data.Team;
import com.example.pouleapp.R;

import java.util.ArrayList;

/**
 * Created by gezamenlijk on 4-3-2017.
 */

public class RankingAdapter extends BaseAdapter {

    private final ArrayList<Team> mTeamList;
    Activity activity;

    public RankingAdapter(Activity activity, ArrayList<Team> teamList) {
        super();
        this.activity = activity;
        this.mTeamList = teamList;
    }

    @Override
    public int getCount() {
        return mTeamList.size();
    }

    @Override
    public Object getItem(int position) {
        return mTeamList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    private class ViewHolder {
        TextView mRanking;
        TextView mTeam;
        TextView mMatchesWon;
        TextView mMatchesDraw;
        TextView mMatchesLost;
        TextView mMatches;
        TextView mPoints;
        TextView mGoalsFor;
        TextView mGoalsAgainst;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        ViewHolder holder;
        LayoutInflater inflater = activity.getLayoutInflater();

        if (convertView == null) {
            convertView = inflater.inflate(R.layout.ranking_row, null);
            holder = new ViewHolder();
            holder.mRanking = (TextView) convertView.findViewById(R.id.ranking);
            holder.mTeam = (TextView) convertView.findViewById(R.id.team);
            holder.mMatches = (TextView) convertView.findViewById(R.id.matches);
            holder.mPoints = (TextView) convertView.findViewById(R.id.points);
            holder.mMatchesWon = (TextView) convertView.findViewById(R.id.matches_won);
            holder.mMatchesDraw = (TextView) convertView.findViewById(R.id.matches_draw);
            holder.mMatchesLost = (TextView) convertView.findViewById(R.id.matches_lost);
            holder.mGoalsFor = (TextView) convertView.findViewById(R.id.goals_for);
            holder.mGoalsAgainst = (TextView) convertView.findViewById(R.id.goals_against);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        Team team = mTeamList.get(position);
        holder.mRanking.setText(Integer.toString(position+1));
        holder.mTeam.setText(team.getTeamName());
        holder.mMatches.setText(Integer.toString(team.getMatches()));
        holder.mPoints.setText(Integer.toString(team.getPoints()));
        holder.mMatchesWon.setText(Integer.toString(team.getWins()));
        holder.mMatchesDraw.setText(Integer.toString(team.getDraws()));
        holder.mMatchesLost.setText(Integer.toString(team.getLosses()));
        holder.mGoalsFor.setText(Integer.toString(team.getGoalsFor()));
        holder.mGoalsAgainst.setText(Integer.toString(team.getGoalsAgainst()));

        return convertView;
    }
}