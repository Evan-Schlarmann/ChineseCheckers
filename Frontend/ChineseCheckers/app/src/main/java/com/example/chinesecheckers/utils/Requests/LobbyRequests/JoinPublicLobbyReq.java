package com.example.chinesecheckers.utils.Requests.LobbyRequests;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Paint;
import android.util.Log;

import com.example.chinesecheckers.LobbyPage;
import com.example.chinesecheckers.PrivateLobbyPage;
import com.example.chinesecheckers.models.PublicLobbyModel;
import com.example.chinesecheckers.utils.Requests.VolleyRequest;
import com.example.chinesecheckers.models.UserModel;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Fetches a lobby for the requesting user to join.
 * Can fetch a casual or competitive lobby.
 */
public class JoinPublicLobbyReq extends VolleyRequest {
    private UserModel user;
    private Activity currentActivity;

    /**
     * Creates a request object to fetch a lobby.
     *
     * @param tag          - activity making the request
     * @param user         - user making request
     * @param currActivity - current activity
     */
    public JoinPublicLobbyReq(String tag, UserModel user, Activity currActivity) {
        super(tag, "json_create_private_lobby");
        this.user = user;
        this.currentActivity = currActivity;
    }

    /**
     * Creates a user object of who is requesting a lobby.
     *
     * @param requestor - user making request
     */
    public void createRequestObject(UserModel requestor) {
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
     * Join the fetched lobby and load the new lobby activity.
     *
     * @param response - JSON object response
     */
    @Override
    protected void handleResponse(JSONObject response) {
        Log.d(TAG, response.toString());

        PublicLobbyModel publicLobby = new PublicLobbyModel();
        publicLobby.parseJson(response);

        // Move into the lobby activity.
        Intent i = new Intent(currentActivity, LobbyPage.class);
        i.putExtra("user", this.user);
        i.putExtra("lobby", publicLobby);

        currentActivity.startActivity(i);
    }
}
