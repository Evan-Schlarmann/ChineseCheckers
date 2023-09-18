package com.example.chinesecheckers.utils.Requests.LobbyRequests;

import android.app.Activity;
import android.app.Dialog;
import android.content.Intent;
import android.util.Log;
import android.widget.TextView;

import com.example.chinesecheckers.PrivateLobbyPage;
import com.example.chinesecheckers.models.PrivateLobbyModel;
import com.example.chinesecheckers.models.UserModel;
import com.example.chinesecheckers.utils.Requests.VolleyRequest;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Join private lobby request asks for a private lobby object
 * and then puts the user into the lobby. It then starts the lobby activity.
 */
public class JoinPrivateLobbyReq extends VolleyRequest {
    private UserModel user;
    private Activity currentActivity;
    private Dialog joinDialog;
    private TextView joinCodeText;

    /**
     * Tries joining a private lobby with the given join code and user.
     *
     * @param tag - activity name
     * @param user - user making request
     * @param currActivity - current activity
     * @param joinDialog - the dialog used to send request
     * @param joinCodeText - the text view of the join code
     */
    public JoinPrivateLobbyReq(String tag, UserModel user, Activity currActivity, Dialog joinDialog, TextView joinCodeText){
        super(tag, "json_create_private_lobby");
        this.user = user;
        this.currentActivity = currActivity;
        this.joinDialog = joinDialog;
        this.joinCodeText = joinCodeText;
    }

    /**
     * Creates a user object whom is joining a private lobby.
     *
     * @param requestor - user sending request
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
     * Join the private lobby activity if the join code was correct.
     * Start the lobby activity and pass the needed values.
     *
     * @param response - json object response from the HTTP request
     */
    @Override
    protected void handleResponse(JSONObject response) {
        Log.d(TAG, response.toString());

        try{
            String joinStatus = response.getString("lobbyStatus");

            if(joinStatus.equals("failed")){
                joinCodeText.setError("Incorrect join code.");
                return;
            }

            PrivateLobbyModel privateLobby = new PrivateLobbyModel();
            privateLobby.parseJson(response);

            // Move into the private lobby activity.
            Intent i = new Intent(currentActivity, PrivateLobbyPage.class);
            i.putExtra("user", this.user);
            i.putExtra("lobby", privateLobby);

            joinDialog.dismiss();
            currentActivity.startActivity(i);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }
}
