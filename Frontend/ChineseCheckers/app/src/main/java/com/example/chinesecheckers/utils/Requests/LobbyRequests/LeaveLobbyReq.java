package com.example.chinesecheckers.utils.Requests.LobbyRequests;

import android.app.Activity;
import android.util.Log;

import com.example.chinesecheckers.models.UserModel;
import com.example.chinesecheckers.utils.Requests.VolleyRequest;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Leave lobby request send a post request to the server to remove the
 * parameter user from the lobby object. It then finishes the lobby
 * activity the user was currently in.
 */
public class LeaveLobbyReq extends VolleyRequest {
    private Activity currentActivity;

    /**
     * leaves a lobby and then finishes the current activity.
     *
     * @param tag - activity name
     * @param currActivity - current activity
     */
    public LeaveLobbyReq(String tag, Activity currActivity){
        super(tag, "json_create_private_lobby");
        this.currentActivity = currActivity;
    }

    /**
     * Creates a user object whom is leaving a lobby.
     *
     * @param requestor - user making the request
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
     * Finish the activity after leaving the lobby.
     *
     * @param response - json object response from the HTTP request
     */
    @Override
    protected void handleResponse(JSONObject response) {
        Log.d(TAG, response.toString());
        currentActivity.finish();
    }
}
