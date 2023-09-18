package com.example.chinesecheckers.models;

import java.io.Serializable;

/**
 * Models the user's stats including matches played, amount of opponents met, and win count.
 */
public class StatsModel implements Serializable {
    private int matchCount;
    private int opponentCount;
    private int winCount;
    private int elo;

    /**
     * Creates a stats model of the user's stats.
     *
     * @param matchCount - total matches played
     * @param oppnentCount - amount of opponents encountered
     * @param winCount - total amount of wins
     */
    public StatsModel(int matchCount, int oppnentCount, int winCount, int elo){
        this.matchCount = matchCount;
        this.opponentCount = oppnentCount;
        this.winCount = winCount;
        this.elo = elo;
    }

    /**
     * @return - total matches played
     */
    public int getMatchCount(){
        return this.matchCount;
    }

    /**
     * Adds the amount of matches played to the total.
     *
     * @param amount - amount of matches played
     */
    public void increaseMatchCount( int amount ) { this.matchCount += amount; }

    /**
     * @return - amount of opponents encountered
     */
    public int getOpponentCount(){
        return this.opponentCount;
    }

    /**
     * @return - total amount of wins
     */
    public int getWinCount(){
        return this.winCount;
    }

    /**
     * @return - ELO score
     */
    public int getELO(){
        return this.elo;
    }
}
