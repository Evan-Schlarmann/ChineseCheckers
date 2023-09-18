package com.example.chinesecheckers.utils.Requests.FriendRequests;

import android.app.Activity;

import com.example.chinesecheckers.utils.Requests.StringVolleyRequest;
import com.example.chinesecheckers.models.FriendRequestModel;
import com.example.chinesecheckers.utils.FriendRequest.FriendRequestCustomAdapter;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Accepts a friend request and updates the list adapter to display the change.
 *
 * <br>
 * <br>Example:
 * <br>AcceptFriendRequest acceptFQ = new AcceptFriendRequest(activity.getClass().getSimpleName(), activity, FriendRequestCustomAdapter.this, friendRequest);
 * <br>acceptFQ.createRequestObject(friendRequest.getId(), true);
 * <br>acceptFQ.sendRequest(Request.Method.PUT, Const.URL_ACCEPT_FRIEND_REQUEST);
 */
public class AcceptFriendRequest extends StringVolleyRequest {
    private Activity activity;
    private FriendRequestCustomAdapter customAdapter;
    private FriendRequestModel friendRequest;

    /**
     * Creates a HTTP request object that accepts a friend request and updates the
     * list adapter to display the changes.
     *
     * @param tag - activity identifier
     * @param activity - activity sending the request
     * @param adapter - list adapter displaying the friend requests
     * @param friendRequest - the friend request being accepted
     */
    public AcceptFriendRequest(String tag, Activity activity, FriendRequestCustomAdapter adapter, FriendRequestModel friendRequest){
        super(tag, "json_accept_friend_request");
        this.activity = activity;
        this.customAdapter = adapter;
        this.friendRequest = friendRequest;
    }

    /**
     * Creates an object with friendshipId and accepted boolean to accept a friend request.
     *
     * @param friendshipId - friendship ID
     * @param isAccepted - whether or not the friendship is accepted
     */
    public void createRequestObject(long friendshipId, boolean isAccepted){
        //make JSON object packet
        requestObject = new JSONObject();

        try {

            requestObject.put("friendshipId", friendshipId);
            requestObject.put("accepted", isAccepted);

        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    /**
     * Refreshes the adapter after removing the accepted friend request.
     *
     * @param response - string response from the HTTP request
     */
    @Override
    protected void handleResponse(String response) {

            customAdapter.remove(friendRequest);
            customAdapter.notifyDataSetChanged();
    }
}
