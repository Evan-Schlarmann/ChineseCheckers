package com.example.chinesecheckers.utils.Requests.LobbyRequests;


import android.app.Activity;
import android.content.Intent;
import android.util.Log;

import com.example.chinesecheckers.PrivateLobbyPage;
import com.example.chinesecheckers.models.PrivateLobbyModel;
import com.example.chinesecheckers.models.UserModel;
import com.example.chinesecheckers.utils.Requests.VolleyRequest;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * create private lobby requests asks the server to make a private lobby
 * and then puts the user into a lobby activity.
 */
public class CreatePrivateLobbyReq extends VolleyRequest {
    private UserModel user;
    private Activity currentActivity;

    /**
     * Creates a private lobby and then joins that lobby with the given user.
     *
     * @param tag - activity name
     * @param user - user sending request
     * @param currActivity - current activity
     */
    public CreatePrivateLobbyReq(String tag, UserModel user, Activity currActivity){
        super(tag, "json_create_private_lobby");
        this.user = user;
        this.currentActivity = currActivity;
    }

    /**
     * Creates a user object whom is creating a private lobby.
     *
     * @param requestor
     */
    public void createRequestObject(UserModel requestor){
        //make JSON object packet
        requestObject = new JSONObject();

        try {

            requestObject.put("userId", requestor.getUID());
            requestObject.put("username", requestor.getUsername());
            requestObject.put("role", requestor.getRole());
            requestObject.put("secret", requestor.getSecret());

        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    /**
     * Join the private lobby activity after it was created.
     *
     * @param response - json object response from the HTTP request
     */
    @Override
    protected void handleResponse(JSONObject response) {
        Log.d(TAG, response.toString());

        PrivateLobbyModel privateLobby = new PrivateLobbyModel();
        privateLobby.parseJson(response);

        // Move into the private lobby activity.
        Intent i = new Intent(currentActivity, PrivateLobbyPage.class);
        i.putExtra("user", this.user);
        i.putExtra("lobby", privateLobby);

        currentActivity.startActivity(i);
    }
}
