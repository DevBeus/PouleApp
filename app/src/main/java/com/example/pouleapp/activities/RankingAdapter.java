package com.example.pouleapp.activities;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.example.pouleapp.data.Team;
import com.example.pouleapp.R;

import java.util.ArrayList;

/**
 * Created by gezamenlijk on 4-3-2017.
 * This adapter class is intended to map all ranking items to ranking tab row items
 */

class RankingAdapter extends BaseAdapter {

    private final ArrayList<Team> mTeamList;
    private Activity mActivity;

    RankingAdapter(Activity activity, ArrayList<Team> teamList) {
        super();
        this.mActivity = activity;
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
        LayoutInflater inflater = mActivity.getLayoutInflater();

        if (convertView == null) {
            final ViewGroup nullParent = null; // introduced to avoid warning

            convertView = inflater.inflate(R.layout.ranking_row, nullParent);
            holder = new ViewHolder();
            holder.mRanking = convertView.findViewById(R.id.ranking_row_text_view_ranking);
            holder.mTeam = convertView.findViewById(R.id.ranking_row_text_view_team);
            holder.mMatches = convertView.findViewById(R.id.ranking_row_text_view_matches);
            holder.mPoints = convertView.findViewById(R.id.ranking_row_text_view_points);
            holder.mMatchesWon = convertView.findViewById(R.id.ranking_row_text_view_matches_won);
            holder.mMatchesDraw = convertView.findViewById(R.id.ranking_row_text_view_matches_draw);
            holder.mMatchesLost = convertView.findViewById(R.id.ranking_row_text_view_matches_lost);
            holder.mGoalsFor = convertView.findViewById(R.id.ranking_row_text_view_goals_for);
            holder.mGoalsAgainst = convertView.findViewById(R.id.ranking_row_text_view_goals_against);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        Team team = mTeamList.get(position);
        String ranking = Integer.toString(position+1);
        String matches = Integer.toString(team.getMatches());
        String points = Integer.toString(team.getPoints());
        String wons = Integer.toString(team.getWins());
        String draws = Integer.toString(team.getDraws());
        String losses = Integer.toString(team.getLosses());
        String goalsFor = Integer.toString(team.getGoalsFor());
        String goalsAgainst = Integer.toString(team.getGoalsAgainst());

        holder.mRanking.setText(ranking);
        holder.mTeam.setText(team.getTeamName());
        holder.mMatches.setText(matches);
        holder.mPoints.setText(points);
        holder.mMatchesWon.setText(wons);
        holder.mMatchesDraw.setText(draws);
        holder.mMatchesLost.setText(losses);
        holder.mGoalsFor.setText(goalsFor);
        holder.mGoalsAgainst.setText(goalsAgainst);

        return convertView;
    }
}