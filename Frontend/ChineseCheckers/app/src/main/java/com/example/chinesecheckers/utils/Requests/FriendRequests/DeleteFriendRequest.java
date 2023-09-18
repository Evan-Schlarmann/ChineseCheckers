package com.example.chinesecheckers.utils.Requests.FriendRequests;

import android.app.Activity;

import com.example.chinesecheckers.utils.Requests.StringVolleyRequest;
import com.example.chinesecheckers.models.FriendRequestModel;
import com.example.chinesecheckers.models.UserModel;
import com.example.chinesecheckers.utils.FriendRequest.FriendRequestCustomAdapter;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Deletes a friend request and updates the adapter display the friend request list.
 *
 * <br>
 * <br>Example:
 * <br>DeleteFriendRequest deleteFQ = new DeleteFriendRequest(activity.getClass().getSimpleName(), activity, FriendRequestCustomAdapter.this, friendRequest);
 * <br>deleteFQ.createRequestObject(user);
 * <br>String URL = Const.URL_DENY_FRIEND_REQUEST + "/" + friendRequest.getId();
 * <br>deleteFQ.sendRequest(Request.Method.PUT, URL);
 */
public class DeleteFriendRequest extends StringVolleyRequest {
    private Activity activity;
    private FriendRequestCustomAdapter customAdapter;
    private FriendRequestModel friendRequest;

    /**
     * Creates HTTP request object to delete a friend request.
     *
     * @param tag - activity identifier
     * @param activity - activity sending the request
     * @param adapter - the adapter displaying list of friend requests
     * @param friendRequest - the friend request to delete
     */
    public DeleteFriendRequest(String tag, Activity activity, FriendRequestCustomAdapter adapter, FriendRequestModel friendRequest){
        super(tag, "json_delete_friend_request");
        this.activity = activity;
        this.customAdapter = adapter;
        this.friendRequest = friendRequest;
    }

    /**
     * Creates an object with userId of whom is deleting a friend request.
     *
     * @param user - user deleting a friend request
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
     * Refreshes the adapter after removing the deleted friend request.
     *
     * @param response - string response from the HTTP request
     */
    @Override
    protected void handleResponse(String response) {

        if(response.equals("Deleted Successfully")) {
            customAdapter.remove(friendRequest);
            customAdapter.notifyDataSetChanged();
        }
    }
}
