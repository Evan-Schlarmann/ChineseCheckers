package com.example.chinesecheckers.models;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.Serializable;
import java.util.ArrayList;

public class PrivateLobbyModel extends LobbyModel implements Serializable {

    private ArrayList<Boolean> userStatus;
    private long joinCode;

    public PrivateLobbyModel(ArrayList<UserModel> users, long lobbyId, long joinCode, int playerCount){
        super(users, lobbyId, playerCount);
        this.joinCode = joinCode;
    }

    public PrivateLobbyModel(){
        super();
        this.userStatus = new ArrayList<>();
    }

    public void addPlayer(UserModel user){

        users.add(user);
        userStatus.add(false);
    }

    public void removePlayer(UserModel user) {
        for (int i = 0; i < users.size(); i++) {
            if (users.get(i).getUsername().equals(user.getUsername())) {

                users.remove(i);
                userStatus.remove(i);
            }
        }
    }

    public Boolean getPlayerStatus(int index){
        index--;
        return userStatus.get(index);
    }

    public void changePlayerStatus(int index, Boolean status){
        index--;
        userStatus.set(index, status);
    }

    public void changePlayerStatus(UserModel user, Boolean status) {
        for (int i = 0; i < users.size(); i++) {
            if (users.get(i).getUsername().equals(user.getUsername())) {

                userStatus.set(i, status);
            }
        }
    }
    public long getJoinCode(){
        return this.joinCode;
    }
    /**
     * parses the json and updates the private lobby model.
     *
     * @param response - json object
     * @return private lobby model
     */
    public PrivateLobbyModel parseJson(JSONObject response) {
        try{
            setLobbyId(response.getLong("lobbyId"));
            this.joinCode = (Long) response.getLong("joinCode");
            setPlayerCount(response.getInt("playerCount"));

            for(int i = 1; i <= getPlayerCount(); i++){
                UserModel user = new UserModel();
                addPlayer(user.parseJson(response.getJSONObject("player" + i)));
                userStatus.add(false);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return this;
    }
}
