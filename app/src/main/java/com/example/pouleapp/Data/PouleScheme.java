package com.example.pouleapp.Data;


import java.util.ArrayList;

/**
 * Created by gezamenlijk on 26-2-2017.
 * this class is intended to calculate and store the match scheme for a poule/group
 */

public class PouleScheme {
    private Match[][] mPouleMatches;
    private Round[] mRoundScheme;
    private int mPouleSize;
    private boolean mFullCompetition = false;

    public PouleScheme(ArrayList<Team> poule, Boolean b) {
        mPouleSize = poule.size();
        mPouleMatches = new Match[mPouleSize][mPouleSize];

        mFullCompetition = b;

        for (int i=0; i<poule.size(); i++) {
            for (int j=0; j<poule.size(); j++) {
                Team team1 = poule.get(i);
                Team team2 = poule.get(j);
                Match match = new Match(team1.getTeamName(), team2.getTeamName());
                mPouleMatches[i][j] = match;
            }

        }

        generateRoundScheme();
    }

    public void addTeam(String newTeam) {
        Match[][] pouleMatchesNew = new Match[mPouleSize+1][mPouleSize+1];

        // Copy all existing results
        for (int i = 0; i < mPouleSize; i++) {
            for (int j = 0; j < mPouleSize; j++) {
                pouleMatchesNew[i][j] = mPouleMatches[i][j];
            }
        }

        // Add new matches
        for (int i = 0; i < mPouleSize; i++) {
            pouleMatchesNew[mPouleSize][i] = new Match(newTeam, mPouleMatches[i][0].getHomeTeam());
            pouleMatchesNew[i][mPouleSize] = new Match(mPouleMatches[i][0].getHomeTeam(),newTeam);
        }

        pouleMatchesNew[mPouleSize][mPouleSize] = new Match(newTeam, newTeam);

        mPouleSize++;
        mPouleMatches = pouleMatchesNew;

        generateRoundScheme();

    }

    public void deleteTeam(ArrayList<Team> teamList, int sel_team) {

        Match[][] pouleMatchesNew = new Match[mPouleSize - 1][mPouleSize - 1];
        int a = 0;

        for (int i = 0; i < mPouleSize; i++) {
            int b = 0;

            if (i != sel_team) {

                for (int j = 0; j < mPouleSize; j++) {

                    if (j != sel_team) {
                        //copy match results
                        pouleMatchesNew[a][b] = mPouleMatches[i][j];
                        b++;
                    } else {
                        //don't copy Match and remove match results
                        removeMatch(teamList, i, j);
                    }
                }

                a++;
            }
            else {
                // remove complete row
                for (int j = 0; j < mPouleSize; j++) { removeMatch(teamList, i, j); }

            }

        }

        mPouleSize--;
        mPouleMatches = pouleMatchesNew;

        generateRoundScheme();
    }

    public void removeMatch(ArrayList<Team> poule, int i, int j){
        Match match = mPouleMatches[i][j];

        Team homeTeam = poule.get(i);
        Team opponent = poule.get(j);

        //First check whether Match was already filled, if yes remove match result first
        Integer act_gf = match.getGoalsFor();
        Integer act_ga = match.getGoalsAgainst();

        if ((act_gf!=null)&&(act_ga!=null)){
            homeTeam.removeMatchResult(act_gf,act_ga);
            opponent.removeMatchResult(act_ga,act_gf);
        }

    }

    public void updateMatch(ArrayList<Team> poule, int i, int j, Integer gf, Integer ga){
        Match match = mPouleMatches[i][j];

        Team homeTeam = poule.get(i);
        Team opponent = poule.get(j);

        //First check whether Match was already filled, if yes remove match result first
        Integer act_gf = match.getGoalsFor();
        Integer act_ga = match.getGoalsAgainst();

        if ((act_gf!=null)&&(act_ga!=null)){
            homeTeam.removeMatchResult(act_gf,act_ga);
            opponent.removeMatchResult(act_ga,act_gf);
        }

        match.setResult(gf,ga);

        if ((gf!=null) && (ga!=null)) {
            homeTeam.addMatchResult(gf, ga);
            opponent.addMatchResult(ga, gf);
        }
    }

    public String getMatchResult(int i, int j) {
        return mPouleMatches[i][j].getResultString();
    }

    public Integer getMatchGoalsFor(int i, int j) {
        Match match = mPouleMatches[i][j];
        Integer gf = match.getGoalsFor();
        return gf;
    }

    public Integer getMatchGoalsAgainst(int i, int j) {
        Match match = mPouleMatches[i][j];
        Integer ga = match.getGoalsAgainst();
        return ga;
    }

    public Match getMatch(int i, int j){ return mPouleMatches[i][j]; }

    public void setMatch(int i, int j, Match match) { mPouleMatches[i][j] = match; }

    public int getPouleSize() { return mPouleSize; }

    public Match getMatchFromScheme(int r, int t) {
        // this function returns the match team t plays in round r
        // Explanation on this function can be found in SpeelSchema.xlsx
        // r in [1..n)
        // t in [0..n)

        int n = mPouleSize;
        int f = mod(n-t-r,n);

        if (f==t) {
            if (f!=(n-1)) {
                f = n-1;
            } else {
                f = n/2 - 1;
            }
        }

        Match m = mPouleMatches[t][f];

        return m;
    }

    private int calculateOpponent(int n, int r, int t) {
        // this function returns the match team t plays in round r with poule size n
        // Explanation on this function can be found in SpeelSchema.xlsx
        // r in [1..n)
        // t in [0..n)

        int f = mod(n-t-r,n);

        if (f==t) {
            f = mod((t+(n/2)),n);
        }

        return f;
    }

    private int mod(int x, int y)
    {
        int result = x % y;
        if (result < 0)
        {
            result += y;
        }
        return result;
    }

    private void generateRoundScheme() {
        int pSize = mPouleSize;
        boolean teamAdded = false;

        //even number of teams required to calculate scheme, additional team is used as free match
        if (mPouleSize%2==1) { pSize = mPouleSize+1; teamAdded = true; }

        if (mFullCompetition) {
            // number of playing rounds is pSize-1 where 0 is not used as numbering starts at 1
            // So full competition is (pSize-1)+ (pSize-1) + 1 for position 0
            mRoundScheme = new Round[2*pSize-1];
        } else {
            //number of playing rounds is pSize-1
            mRoundScheme = new Round[pSize]; // element 0 will not be used, round numbering starts at 1
        }

        for (int r = 1; r<pSize; r++) {
            // Number of matches per round is mPouleSize/2, Remark: not pSize/2, example when poule consists of 3 teams, only 1 match can be played per round
            // Array of size 2 to store home team nr and opponent team nr
            // Example: matchList[0] -> [1,3] describes team 1 playing team 3
            int[][] matchList = new int[mPouleSize/2][2];
            boolean[] teamScheduled = new boolean[pSize]; //implicitly whole array is set to false
            int m = 0;

            for (int t = 0; t <pSize; t++){
                int op = calculateOpponent(pSize,r,t);

                //Only plan teams when not yet scheduled
                if ( !teamScheduled[t] && !teamScheduled[op] ) {
                    teamScheduled[t] = true;
                    teamScheduled[op] = true;

                    if ( teamAdded && (op==(pSize-1)) ) {
                        // When team is added, pSize-1 as opponent means team is free
                        // Skip
                    } else {
                        matchList[m][0] = t;
                        matchList[m][1] = op;
                        m++;
                    }
                }
            }

            mRoundScheme[r] = new Round(r,matchList);
        }

        if (mFullCompetition) {
            // Copy first half of competition with swapping home and opponent team
            for (int r = pSize; r < 2*pSize-1; r++) {
                int[][] matchList = new int[mPouleSize/2][2];

                Round orgRound = mRoundScheme[r-pSize+1];
                int[][] orgMatchList = orgRound.getMatchList();

                for (int i = 0; i < orgMatchList.length; i++) {
                    matchList[i][0] = orgMatchList[i][1];
                    matchList[i][1] = orgMatchList[i][0];
                }

                mRoundScheme[r] = new Round(r,matchList);
            }
        }

    }

    public Round getRound(int r) {
        // r in [1..n] with n = getNumberOfRounds()

        return mRoundScheme[r];
    }
    public int getNumberOfRounds() {
        //int rounds = mPouleSize;

        //When number of teams is odd, additional round is required
        //if (mPouleSize%2==1) { rounds++; }

        //return rounds;

        return mRoundScheme.length;
    }

    public Match[] getRoundMatchList(int r) {
        // Example: mList[0] -> [1,3] describes team 1 playing team 3
        int[][] mList = mRoundScheme[r].getMatchList();
        Match[] matchList = new Match[mList.length];

        for (int i=0; i < mList.length; i++) {
            int x = mList[i][0];
            int y = mList[i][1];

            matchList[i] = mPouleMatches[x][y];
        }

        return matchList;
    }

    public String[] getRoundMatchListStrings(int r) {
        String[] matchList = new String[mPouleSize];

        int i = 0;

        for (int t=0; t<mPouleSize; t++) {
            Match match;
            match = getMatchFromScheme(r,t);

            matchList[t] = match.getMatchString();
        }

        return matchList;
    }

    public boolean IsFullCompetition() { return mFullCompetition; }

    public void setFullCompetition(boolean b) {

        if ( mFullCompetition != b) {
            mFullCompetition = b;
            generateRoundScheme(); // when competition is changed from full to half or vice versa round scheme needs to be recalculated
        }
    }
}
