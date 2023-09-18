package com.example.chinesecheckers.models;

import androidx.annotation.NonNull;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.Serializable;

/**
 * Models a user with their general data and their secret.
 */
public class UserModel implements Serializable {

    private String username;
    private String role;
    private String secret;
    private Long userID;
    private String password;
    private StatsModel stats;

    // 1 if muted otherwise 0
    private int isMuted;

    /**
     *
     * @param username - username of the user
     * @param role - role of the user
     * @param secret - secret of the suer
     * @param userID - users unique identification number
     * @param password - password of the users account
     */
    public UserModel(String username, String role, String secret, Long userID, String password) {
        this.username = username;
        this.role = role;
        this.secret = secret;
        this.userID = userID;
        this.password = password;

        // created in parseJson
        this.stats = null;
        this.isMuted = -1;
    }

    /**
     * Default constructor used when parsing from json.
     */
    public UserModel(){
        this.username = "";
    }

    /**
     * @return - username of the user
     */
    public String getUsername() {
        return username;
    }

    /**
     * @return - role of the user
     */
    public String getRole() {
        return role;
    }

    /**
     * @return secret of the user
     */
    public String getSecret() {
        return secret;
    }

    /**
     * @return - user's unique user identification number
     */
    public Long getUID() {
        return userID;
    }

    /**
     * @return - password of the user
     */
    public String getPassword() { return password; }

    /**
     * @param username - new username of the user
     */
    public void setUsername(String username){
        this.username = username;
    }

    /**
     * @param password - new password of the user
     */
    public void setPassword(String password){
        this.password = password;
    }

    /**
     * @param role - new role of the user
     */
    public void setRole(String role){
        this.role = role;
    }

    /**
     * @return - user stats
     */
    public StatsModel getStats(){
        return this.stats;
    }

    /**
     * @return 1 if muted 0 otherwise
     */
    public int getIsMuted(){
        return this.isMuted;
    }

    /**
     * @return - username of the user
     */
    @NonNull
    @Override
    public String toString() {
        return username;
    }

    /**
     * parses the json and updates the user model.
     *
     * @param response - json object
     * @return - user model
     */
    public UserModel parseJson(JSONObject response){

        try {

            this.username = response.getString("username");
            this.role = response.getString("role");
            this.secret = response.getString("secret");
            this.password = response.getString("password");
            this.userID = response.getLong("userId");
            this.isMuted = response.getInt("muted");

            // stats
            int matches = response.getInt("matches");
            int wins = response.getInt("wins");
            int opponents = response.getInt("opponents");
            int elo = response.getInt("elo");
            this.stats = new StatsModel(matches, opponents, wins,  elo);
            this.isMuted = response.getInt("muted");


        } catch (JSONException e) {
            e.printStackTrace();
        }

        return this;
    }
}
