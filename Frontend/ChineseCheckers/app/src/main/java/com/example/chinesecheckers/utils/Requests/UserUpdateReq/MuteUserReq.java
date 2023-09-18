package com.example.chinesecheckers.utils.Requests.UserUpdateReq;

import android.app.Activity;
import android.content.Intent;
import android.util.Log;

import com.example.chinesecheckers.models.UserModel;
import com.example.chinesecheckers.utils.Requests.VolleyRequest;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * makes a request that either mutes or unmutes a user from chat.
 */
public class MuteUserReq extends VolleyRequest {
    private Activity activity;

    /**
     * Creates the HTTP request object and the views that need to be updated.
     *
     * @param tag      - activity identifier
     * @param activity - activity sending request
     */
    public MuteUserReq(String tag, Activity activity) {
        super(tag, "mute_user");

        this.activity = activity;
    }

    /**
     * Parses the JSON object response and then refreshes the activity to show changes.
     *
     * @param response - json object response from the HTTP request
     */
    @Override
    protected void handleResponse(JSONObject response) {
        Log.d(TAG, response.toString());

        UserModel updatedUser = new UserModel();
        updatedUser.parseJson(response);

        activity.finish();

        activity.overridePendingTransition(0, 0);
        Intent i = activity.getIntent();
        i.putExtra("updatedUser", updatedUser);
        activity.startActivity(i);
        activity.overridePendingTransition(0, 0);
    }
}
