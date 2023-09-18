package com.chinesecheckers.serverside;

import com.chinesecheckers.serverside.entity.User;
import io.swagger.annotations.ApiModelProperty;

/**
 * Pair of users for verification purposes where the target User is experiencing some change
 * and the sender User is the one responsible for it. This allows for role permissions.
 */
public class Stats {
    // Basic
    @ApiModelProperty(notes = "Matches played", name = "matches", required = true, value = "Matches played in by a user")
    private int matches;
    @ApiModelProperty(notes = "Opponents faced", name = "opponents", required = true, value = "Opponents faced by a user")
    private int opponents;
    @ApiModelProperty(notes = "Games won", name = "wins", required = true, value = "Games won by a user")
    private int wins;

    public Stats() {}

    // User
    public void setMatches(int matches) { this.matches = matches; }
    public void setOpponents(int opponents) { this.opponents = opponents; }
    public void setWins(int wins) { this.wins = wins; }

    public int getMatches() { return matches; }
    public int getOpponents() { return opponents; }
    public int getWins() { return wins; }

}
