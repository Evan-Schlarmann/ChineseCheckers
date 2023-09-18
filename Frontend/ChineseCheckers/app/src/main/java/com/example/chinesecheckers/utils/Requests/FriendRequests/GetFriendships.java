package com.example.chinesecheckers.utils.Requests.FriendRequests;

import android.app.Activity;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import com.android.volley.Request;
import com.example.chinesecheckers.utils.Requests.VolleyArrayRequest;
import com.example.chinesecheckers.models.FriendRequestModel;
import com.example.chinesecheckers.models.UserModel;
import com.example.chinesecheckers.utils.Const;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

/**
 * Get friendships requires the chosen user id to be in the request URL. It retrieves all
 * friendships the user has and it then compares to see if there is a friendship that both
 * users are in and the status of that friendship to display the correct view.
 *
 * <br>
 * <br>Example:
 * <br>GetFriendships getFriendships = new GetFriendships(TAG, this, user, chosenUser, friendRequestButton);
 * <br>String URL = Const.URL_GET_FRIENDSHIPS + "/" + chosenUser.getUID();
 * <br>getFriendships.sendRequest(Request.Method.GET, URL);
 */
public class GetFriendships extends VolleyArrayRequest {

    private Button friendReqButton;
    private UserModel user;
    private UserModel chosenUser;
    private Activity activity;

    /**
     * Takes both users and the activity to correctly display the request button and its function.
     *
     * @param tag - activity identifier
     * @param activity - activity sending the request
     * @param user - logged in user
     * @param chosenUser - chosen user's profile
     * @param requestButton - the friend request button being displayed
     */
    public GetFriendships(String tag, Activity activity, UserModel user, UserModel chosenUser, Button requestButton) {
        super(tag, "json_friendships");
        this.friendReqButton = requestButton;
        this.user = user;
        this.chosenUser = chosenUser;
        this.activity = activity;
    }

    /**
     * Checks for a friendship between the two users in the request and updates the user profile given the response.
     * If there is a friendship that is accepted it displays the option to delete the friendship.
     * If there is a friendship that is incoming or outgoing it displays the friendship as pending.
     * If there is no friendship between them it displays send a friend request.
     *
     * @param response - json array response from the HTTP request
     */
    @Override
    protected void handleResponse(JSONArray response) {
        Log.d(TAG, response.toString());

        // If user is looking at their own profile in user list don't display a friend request button.
        if(user.getUID().equals(chosenUser.getUID())){
            friendReqButton.setVisibility(View.INVISIBLE);
            return;
        }

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

        // Test to see if there is a friendship between them.
        for(FriendRequestModel req : friendRequests){
            if(req.getAccepter().getUsername().equals(user.getUsername()) ||
                    req.getRequester().getUsername().equals(user.getUsername())){


                if(req.isAccepted()){
                    // Display option to remove friend.
                    friendReqButton.setText("Remove Friend");
                    friendReqButton.setBackgroundColor(0xFFFF0000);
                    friendReqButton.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {

                            // Handle the delete request.
                            DeleteFriendship deleteFriend = new DeleteFriendship(TAG, activity, true);
                            deleteFriend.createRequestObject(user);
                            String URL = Const.URL_DENY_FRIEND_REQUEST + "/" + req.getId();
                            deleteFriend.sendRequest(Request.Method.PUT, URL);
                        }
                    });

                    return;
                }
                else{
                    // Display that a friend request has already been made.
                    friendReqButton.setText("Pending Friend Request.");

                    return;
                }
            }
        }

        // Display option to send friend request.
        friendReqButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                // Make the friend request.
                CreateFriendRequest friendRequest = new CreateFriendRequest(TAG, activity, true);
                friendRequest.createRequestObject(user, chosenUser);

                friendRequest.sendRequest(Request.Method.POST, Const.URL_CREATE_FRIEND_REQUEST);
            }
        });
    }
}
