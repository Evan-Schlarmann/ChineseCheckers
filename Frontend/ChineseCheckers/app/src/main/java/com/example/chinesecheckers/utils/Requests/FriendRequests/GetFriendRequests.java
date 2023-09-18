package com.example.chinesecheckers.utils.Requests.FriendRequests;

import android.app.Activity;
import android.util.Log;
import android.widget.ListView;

import com.example.chinesecheckers.utils.Requests.VolleyArrayRequest;
import com.example.chinesecheckers.models.FriendRequestModel;
import com.example.chinesecheckers.models.UserModel;
import com.example.chinesecheckers.utils.FriendRequest.FriendRequestCustomAdapter;
import com.example.chinesecheckers.utils.FriendRequest.FriendRequestEnum;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

/**
 * Gets all friendships that are in the given request type of incoming or outgoing and then displays
 * them in a list using the custom friend request adapter.
 *
 * <br>
 * <br>Example:
 * <br>GetFriendRequests request =
 *                     new GetFriendRequests(TAG, this, user, friendRequestList, FriendRequestEnum.INCOMING);
 * <br>String URL = Const.URL_INCOMING_FRIEND_REQUESTS + "/" + user.getUID().toString();
 * <br>request.sendRequest(Request.Method.GET, URL);
 */
public class GetFriendRequests extends VolleyArrayRequest {

    private Activity activity;
    private ListView friendReqList;
    private FriendRequestEnum friendRequestType;
    private UserModel user;

    /**
     * Creates the HTTP request object to retrieve either incoming or outgoing friend requests
     * and display them in a list for the user.
     *
     * @param tag - activity identifier
     * @param activity - activity sending the request
     * @param user - logged in user
     * @param listView - the list that will display friend requests
     * @param requestType - the type of friend requests being retrieved either INCOMING or OUTGOING
     */
    public GetFriendRequests(String tag, Activity activity, UserModel user, ListView listView, FriendRequestEnum requestType) {
        super(tag, "json_friend_requests");
        this.activity = activity;
        this.friendReqList = listView;
        this.friendRequestType = requestType;
        this.user = user;
    }

    /**
     * Creates the list adapter for the friend request list page and populates it with all the
     * retrieved friend requests from the JSON array response.
     *
     * @param response - json array response from the HTTP request
     */
    @Override
    protected void handleResponse(JSONArray response) {
        Log.d(TAG, response.toString());

        ArrayList<FriendRequestModel> friendRequests = new ArrayList<>();
        FriendRequestModel friendRequest;

        try {
            for (int i = 0; i < response.length(); i++) {
                JSONObject tempReq = response.getJSONObject(i);

                // Parse into needed objects.
                JSONObject accepter = tempReq.getJSONObject("accepter");
                long userID = accepter.getLong("userId");
                String username = accepter.getString("username");
                String role = accepter.getString("role");
                UserModel accepterUser = new UserModel(username, role, null, userID, null);

                JSONObject requester = tempReq.getJSONObject("requester");
                userID = requester.getLong("userId");
                username = requester.getString("username");
                role = requester.getString("role");
                String secret = requester.getString("secret");
                UserModel requesterUser = new UserModel(username, role, secret, userID, null);

                Boolean accepted = tempReq.getBoolean("accepted");
                Long friendReqId = tempReq.getLong("friendshipId");

                friendRequest = new FriendRequestModel(friendReqId, requesterUser, accepterUser, accepted);

                friendRequests.add(friendRequest);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }

        // Need a new custom adapter for outgoing friend requests that don't have an accept button.
        FriendRequestCustomAdapter arrayAdapter = new FriendRequestCustomAdapter(activity, user, friendRequests, friendRequestType);

        friendReqList.setAdapter(arrayAdapter);
    }
}