package com.example.chinesecheckers.utils.FriendRequest;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.TextView;

import com.android.volley.Request;
import com.example.chinesecheckers.R;
import com.example.chinesecheckers.utils.Requests.FriendRequests.AcceptFriendRequest;
import com.example.chinesecheckers.utils.Requests.FriendRequests.DeleteFriendRequest;
import com.example.chinesecheckers.models.FriendRequestModel;
import com.example.chinesecheckers.models.UserModel;
import com.example.chinesecheckers.utils.Const;

import java.util.ArrayList;

/**
 * FriendRequestCustomAdapter displays friend requests as a list and determines the layout that
 * each row is implemented by based on the friend request type. The list allows users to accept or
 * delete incoming requests and to delete outgoing requests.
 */
public class FriendRequestCustomAdapter extends ArrayAdapter<FriendRequestModel> {
    private Activity activity;
    private ArrayList<FriendRequestModel> data;
    private FriendRequestEnum friendRequestType;
    private UserModel user;

    /**
     * The constructor takes an activity to know where the adapter is being displayed.
     * It requires the user of whom is loading their friend requests.
     * It has an array list of all the friend requests and what type of requests it is displaying.
     *
     * @param activity - current activity
     * @param user - logged in user
     * @param data - list of all friend requests of given type
     * @param requestType - friend requests type, INCOMING or OUTGOING
     */
    public FriendRequestCustomAdapter(Activity activity, UserModel user, ArrayList<FriendRequestModel> data, FriendRequestEnum requestType) {
        super(activity, 0, data);
        this.activity = activity;
        this.data = data;
        this.friendRequestType = requestType;
        this.user = user;
    }

    /**
     * getView generates the view for the activity by inflating either incoming requests layout or
     * outgoing requests layout. It then creates the buttons to accept or delete a friend request.
     *
     * @param position - position of the view
     * @param convertView - the view that we will change to display our data
     * @param parent - the parent of our adapter view
     * @return - our now custom convertView
     */
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        FriendRequestModel friendRequest = data.get(position);

        if (friendRequestType.equals(FriendRequestEnum.INCOMING) && convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.friend_request_incoming_row, parent, false);

            // Only include accept button if the friend request is incoming.
            Button acceptButton = (Button) convertView.findViewById(R.id.accept_friend_request_btn);

            acceptButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    // Accept the friend request.
                    AcceptFriendRequest acceptFQ = new AcceptFriendRequest(activity.getClass().getSimpleName(), activity, FriendRequestCustomAdapter.this, friendRequest);
                    acceptFQ.createRequestObject(friendRequest.getId(), true);
                    acceptFQ.sendRequest(Request.Method.PUT, Const.URL_ACCEPT_FRIEND_REQUEST);
                }
            });
        }
        else if(friendRequestType.equals(FriendRequestEnum.OUTGOING) && convertView == null)
        {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.friend_request_outgoing_row, parent, false);
        }

        TextView username = (TextView) convertView.findViewById(R.id.friend_request_string);
        Button deleteButton = (Button) convertView.findViewById(R.id.delete_friend_request_btn);

        // Set the username that is displayed in the list.
        if(friendRequestType.equals(FriendRequestEnum.INCOMING))
            username.setText(friendRequest.getRequester().getUsername());
        else
            username.setText(friendRequest.getAccepter().getUsername());

        deleteButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Deny the friend request.
                DeleteFriendRequest deleteFQ = new DeleteFriendRequest(activity.getClass().getSimpleName(), activity, FriendRequestCustomAdapter.this, friendRequest);
                deleteFQ.createRequestObject(user);
                String URL = Const.URL_DENY_FRIEND_REQUEST + "/" + friendRequest.getId();
                deleteFQ.sendRequest(Request.Method.PUT, URL);
            }
        });

        return convertView;
    }
}
