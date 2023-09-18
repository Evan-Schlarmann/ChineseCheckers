package com.example.chinesecheckers.utils.Requests;

import android.app.Activity;
import android.content.Intent;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.example.chinesecheckers.FriendListPage;
import com.example.chinesecheckers.models.UserModel;

import org.json.JSONException;
import org.json.JSONObject;
import org.w3c.dom.Text;

/**
 * Gets a user by the user ID using an HTTP request and then
 * updates the necessary views in the current activity.
 *
 * <br>
 * <br>Example:
 * <br>GetUserProfileRequest getUserById =
 *                 new GetUserProfileRequest(TAG, ActiveUserProfile.this, user, profileUsername, profileRole, getFriendList);
 * <br>getUserById.setStatsViews(matchCount, winCount);
 * <br>getUserById.sendRequest(Request.Method.GET, Const.URL_GET_USER_BYID + user.getUID());
 */
public class GetUserProfileRequest extends VolleyRequest{

    private Activity activity;
    private UserModel user;
    private TextView usernameView;
    private TextView roleView;
    private Button friendListButton;
    private TextView matchCountView;
    private TextView winCountView;
    private TextView ELOView;

    /**
     * Constructs the HTTP request and necessary parts of the view that contain user information.
     *
     * @param tag - activity identifier
     * @param activity - current activity
     * @param user - logged in user
     * @param username - TextView that contains the user's username
     * @param role - TextView that contains the user's role
     * @param friendListButton - button that redirects to the user's friend list
     */
    public GetUserProfileRequest(String tag, Activity activity, UserModel user, TextView username, TextView role, Button friendListButton) {
        super(tag, "json_get_user");
        this.activity = activity;
        this.user = user;
        this.usernameView = username;
        this.roleView = role;
        this.friendListButton = friendListButton;
    }

    /**
     * Set the text views to display the user's stats.
     *
     * @param matchCount - text view for total match count
     * @param winCount - text view for total win count
     */
    public void setStatsViews(TextView matchCount, TextView winCount, TextView ELO){
        this.matchCountView = matchCount;
        this.winCountView = winCount;
        this.ELOView = ELO;
    }

    /**
     * retrieves the user from the JSON object response and populates all the text views
     * that need the updated data from the server.
     *
     * @param response - json object response from the HTTP request
     */
    @Override
    protected void handleResponse(JSONObject response) {
        Log.d(TAG, response.toString());

        try {

            String name = response.getString("username");
            String role = response.getString("role");
            String secret = response.getString("secret");
            String password = response.getString("password");
            long id = response.getLong("userId");
            int matches = response.getInt("matches");
            int wins = response.getInt("wins");
            int ELO = response.getInt("elo");

            user = new UserModel(name, role, secret, id, password);

            usernameView.setText(user.getUsername());
            roleView.setText(user.getRole());
            matchCountView.setText(Integer.toString(matches));
            winCountView.setText(Integer.toString(wins));
            ELOView.setText(Integer.toString(ELO));

            friendListButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent i = new Intent(activity, FriendListPage.class);
                    i.putExtra("user", user);
                    activity.startActivity(i);
                }
            });
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }
}
