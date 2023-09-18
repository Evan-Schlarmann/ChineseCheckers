package com.example.chinesecheckers.utils.Requests.FriendRequests;

import android.app.Activity;

import com.example.chinesecheckers.utils.Requests.VolleyRequest;
import com.example.chinesecheckers.models.FriendRequestModel;
import com.example.chinesecheckers.models.UserModel;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * creates a friend request and refreshes the current activity if the activity passed
 * is not null and refreshActivity is true.
 *
 * <br>
 * <br>Example:
 * <br>CreateFriendRequest friendRequest = new CreateFriendRequest(TAG, activity, true);
 * <br>friendRequest.createRequestObject(user, chosenUser);
 * <br>friendRequest.sendRequest(Request.Method.POST, Const.URL_CREATE_FRIEND_REQUEST);
 */
public class CreateFriendRequest extends VolleyRequest {

    private boolean refreshActivity;
    private Activity activity;

    /**
     * Creates a HTTP request object to create a friend list and refresh the activity.
     *
     * @param tag - activity identifier
     * @param activity - activity sending the request
     * @param refreshActivity - true to refresh the activity, false to not refresh it
     */
    public CreateFriendRequest(String tag, Activity activity, boolean refreshActivity){
        super(tag, "json_create_friend_request");
        this.refreshActivity = refreshActivity;
        this.activity = activity;
    }

    /**
     * Creates the request object of who created it (requester) and the target
     * of the friend request (accepter).
     *
     * @param requestor - user creating the friend request
     * @param accepter - target of the friend request
     */
    public void createRequestObject(UserModel requestor, UserModel accepter){
        //make JSON object packet
        requestObject = new JSONObject();

        try {
            FriendRequestModel friendRequest;

            JSONObject requesterObject = new JSONObject();
            requesterObject.put("userId", requestor.getUID());

            JSONObject accepterObject = new JSONObject();
            accepterObject.put("userId", accepter.getUID());

            requestObject.put("requester", requesterObject);
            requestObject.put("accepter", accepterObject);
            requestObject.put("accepted", "false");

        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    /**
     * Refreshes the activity without a transition screen making it seamless.
     *
     * @param response - json object response from the HTTP request
     */
    @Override
    protected void handleResponse(JSONObject response) {
        if(activity != null && refreshActivity){
            activity.finish();
            activity.overridePendingTransition(0, 0);
            activity.startActivity(activity.getIntent());
            activity.overridePendingTransition(0, 0);
        }
    }
}
