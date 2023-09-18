package com.example.chinesecheckers.models;

import org.json.JSONObject;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * this is the base class for private and public lobbies.
 */
public abstract class LobbyModel implements Serializable {

    protected ArrayList<UserModel> users;
    private long lobbyId;
    private int playerCount;

    /**
     * constructs a lobby object
     * @param users users in the lobby
     * @param lobbyId lobby id
     * @param playerCount max player count
     */
    public LobbyModel(ArrayList<UserModel> users, long lobbyId, int playerCount){
        this.users = users;
        this.lobbyId = lobbyId;
        this.playerCount = playerCount;
    }

    /**
     * default constructor when using parseJson later.
     */
    public LobbyModel(){
        this.users = new ArrayList<>();
    }

    public ArrayList<UserModel> getUsers(){
        return users;
    }

    public long getLobbyId(){
        return lobbyId;
    }

    public void setLobbyId(Long lobbyId){
        this.lobbyId = lobbyId;
    }

    public int getPlayerCount(){ return playerCount; }

    public void setPlayerCount(int maxPlayerCount){ this.playerCount = maxPlayerCount; }

    public int getCurrentPlayerCount() { return users.size(); }

    // Get players by their index, 1, 2, 3, 4, 5, or 6
    public UserModel getPlayer(int index){
        index--;
        return users.get(index);
    }

    public void addPlayer(UserModel user){
        users.add(user);
    }

    public void removePlayer(UserModel user) {
        for(int i = 0; i < users.size(); i++){
            if(users.get(i).getUsername().equals(user.getUsername())) {
                users.remove(i);
            }
        }
    }

    public abstract LobbyModel parseJson(JSONObject response);
}
