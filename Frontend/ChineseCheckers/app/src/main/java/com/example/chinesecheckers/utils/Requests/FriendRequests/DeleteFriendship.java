package com.example.chinesecheckers.utils.Requests.FriendRequests;

import android.app.Activity;

import com.example.chinesecheckers.utils.Requests.StringVolleyRequest;
import com.example.chinesecheckers.models.UserModel;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Deletes an accepted friendship and refreshes the current activity if the
 * activity passed is not null and refreshActivity is true.
 *
 * <br>
 * <br>Example:
 * <br>DeleteFriendship deleteFriend = new DeleteFriendship(TAG, activity, true);
 * <br>deleteFriend.createRequestObject(user);
 * <br>String URL = Const.URL_DENY_FRIEND_REQUEST + "/" + req.getId();
 * <br>deleteFriend.sendRequest(Request.Method.PUT, URL);
 */
public class DeleteFriendship extends StringVolleyRequest {
    private boolean refreshActivity;
    private Activity activity;

    /**
     * Creates the HTTP request object that deletes a friendship and can refresh the activity.
     *
     * @param tag - activity identifier
     * @param activity - activity sending the request
     * @param refreshActivity - true to refresh the activity, false to not refresh it
     */
    public DeleteFriendship(String tag, Activity activity, boolean refreshActivity){
        super(tag, "json_delete_friendship");
        this.refreshActivity = refreshActivity;
        this.activity = activity;
    }

    /**
     * Creates an object with userId of who is deleting a friend request.
     *
     * @param user - user who is deleting a friend request
     */
    public void createRequestObject(UserModel user){
        //make JSON object packet
        requestObject = new JSONObject();

        try {
            requestObject.put("userId", user.getUID());
            requestObject.put("username", user.getUsername());
            requestObject.put("role", user.getRole());
            requestObject.put("secret", user.getSecret());

        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    /**
     * Refreshes the activity without a transition screen making it seamless.
     *
     * @param response - string response from the HTTP request
     */
    @Override
    protected void handleResponse(String response) {
        if(activity != null && refreshActivity){
            activity.finish();
            activity.overridePendingTransition(0, 0);
            activity.startActivity(activity.getIntent());
            activity.overridePendingTransition(0, 0);
        }
    }
}
