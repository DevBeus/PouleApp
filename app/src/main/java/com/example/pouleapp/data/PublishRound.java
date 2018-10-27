package com.example.pouleapp.data;

import java.util.ArrayList;

/**
 * Created by gezamenlijk on 27-1-2018.
 */

public class PublishRound {
    private int mRound;
    private ArrayList<PublishMatch> mMatchList = new ArrayList<>();

    public PublishRound() {
        //empty constructor for firebase
    }

    public PublishRound(PouleScheme pouleScheme, int r) {
        mRound = r;
        Match[] matchList = pouleScheme.getRoundMatchList(mRound);

        for(int i=0; i<matchList.length; i++) {
            mMatchList.add(new PublishMatch(matchList[i]));
        }
    }

    public int getRound() {
        return mRound;
    }

    public void setRound(int mRound) {
        this.mRound = mRound;
    }

    public ArrayList<PublishMatch> getMatchList() {
        return mMatchList;
    }

    public void setMatchList(ArrayList<PublishMatch> matchList) {
        this.mMatchList = matchList;
    }
}
