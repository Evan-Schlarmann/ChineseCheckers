package com.example.chinesecheckers.utils.Requests.LobbyRequests;

import android.app.Activity;
import android.content.Intent;
import android.util.Log;

import com.example.chinesecheckers.GamePage;
import com.example.chinesecheckers.models.LobbyModel;
import com.example.chinesecheckers.models.PrivateLobbyModel;
import com.example.chinesecheckers.models.UserModel;
import com.example.chinesecheckers.utils.Requests.VolleyRequest;

import org.java_websocket.client.WebSocketClient;
import org.json.JSONException;
import org.json.JSONObject;

/**
 * Requests a game for the lobby and then sends that game to all users
 * in that lobbies web socket.
 */
public class LobbyGameReq extends VolleyRequest {
    private WebSocketClient websocket;
    private LobbyModel lobby;

    /**
     * Creates request for a game given the lobbyId.
     *
     * @param tag - activity name
     * @param websocket - websocket that the lobby is using
     */
    public LobbyGameReq(String tag, WebSocketClient websocket){
        super(tag, "json_create_private_lobby");
        this.websocket = websocket;
    }

    /**
     * Creates a string of the private lobbyId
     *
     * @param lobby - lobby the user is in
     */
    public void createRequestObject(LobbyModel lobby){
        //make JSON object packet
        requestObject = new JSONObject();

        try {
            this.lobby = lobby;
            requestObject.put("lobbyId", lobby.getLobbyId());

        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    /**
     * Join the private lobby activity if the join code was correct.
     * Start the lobby activity and pass the needed values.
     *
     * @param response - json object response from the HTTP request
     */
    @Override
    protected void handleResponse(JSONObject response) {
        Log.d(TAG, response.toString());

        try{
            long gameId = (Long) response.getLong("gameId");

            // Send that the game was created.

            websocket.send("lobbyId " + lobby.getLobbyId() + " gameId " + gameId);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }
}
