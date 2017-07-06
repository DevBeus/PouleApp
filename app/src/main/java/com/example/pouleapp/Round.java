package com.example.pouleapp;

/**
 * Created by gezamenlijk on 30-6-2017.
 */

public class Round {
    private int mRound;
    private int[][] mMatchList;

    // MatchList is defined per round: number of matches is pSize/2, array of size 2 to store home team nr and opponent team nr
    // Example: number of teams is 4
    // matchList[0] -> [0,3] describes team 0 playing team 3
    // matchList[1] -> [1,2] describes team 1 playing team 2

    public Round(int r, int[][] matchlist) {
        mRound  = r;
        mMatchList = matchlist;
    }

    public int getRound() {
        return mRound;
    }

    public int[][] getMatchList() {
        return mMatchList;
    }

}
