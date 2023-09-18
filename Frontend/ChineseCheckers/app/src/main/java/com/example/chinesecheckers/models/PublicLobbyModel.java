package com.example.chinesecheckers.models;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * models a public lobby and the users inside.
 */
public class PublicLobbyModel extends LobbyModel implements Serializable {

    /**
     * constructs a public lobby object
     * @param users users in the lobby
     * @param lobbyId lobby id
     * @param playerCount max player count
     */
    public PublicLobbyModel(ArrayList<UserModel> users, long lobbyId, int playerCount){
        super(users, lobbyId, playerCount);
    }

    /**
     * default constructor for using parseJson.
     */
    public PublicLobbyModel(){
        super();
    }

    /**
     * parses the json and updates the public lobby model.
     *
     * @param response - json object
     * @return private lobby model
     */
    public PublicLobbyModel parseJson(JSONObject response) {
        try{
            setLobbyId(response.getLong("lobbyId"));
            setPlayerCount(response.getInt("playerCount"));

            for(int i = 1; i <= getPlayerCount(); i++){
                UserModel user = new UserModel();
                if(response.getJSONObject("player" + i) != null){
                    user = user.parseJson(response.getJSONObject("player" + i));
                    addPlayer(user);
                }
            }

        } catch (JSONException e) {
            e.printStackTrace();
        }

        return this;
    }
}
