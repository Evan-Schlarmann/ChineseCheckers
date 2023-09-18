package com.example.chinesecheckers.utils.Requests;

import android.annotation.SuppressLint;
import android.util.Log;

import com.android.volley.Request;
import com.example.chinesecheckers.models.StatsModel;
import com.example.chinesecheckers.utils.Const;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Gets all stats for the user with the given user id in the URL when sending a get request.
 * Also sends an update to the stats if a stat type and change amount is given.
 *
 * <br>
 * <br>Example:
 * <br>UserStatsRequest statsUpdate = new UserStatsRequest(TAG, "matches", 1);
 * <br>statsUpdate.sendRequest(Request.Method.GET, Const.URL_GET_USER_BYID + user.getUID());
 */
public class UserStatsRequest extends VolleyRequest {

    /**
     * Creates a stat request to update a single stat type.
     *
     * @param tag - activity identifier
     */
    public UserStatsRequest(String tag) {
        super(tag, "json_get_stats");
    }

    /**
     * Loads the stats of the user. Sends an update request on the retrieved data if
     * an the stat type and amount have been specified.
     *
     * @param response - json object response from the HTTP request
     */
    @SuppressLint("SetTextI18n")
    @Override
    protected void handleResponse(JSONObject response) {
        Log.d(TAG, response.toString());

        try {
            long userId = response.getLong("userId");
            int matchCount = response.getInt("matches");
            int opponentCount = response.getInt("opponents");
            int winCount = response.getInt("wins");
            int elo = response.getInt("elo");

            StatsModel stats = new StatsModel(matchCount, opponentCount, winCount, elo);

        } catch (JSONException e) {
            e.printStackTrace();
        }
    }
}
