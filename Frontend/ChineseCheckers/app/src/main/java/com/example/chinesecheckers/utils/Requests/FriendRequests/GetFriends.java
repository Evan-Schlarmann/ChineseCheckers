package com.example.chinesecheckers.utils.Requests.FriendRequests;

import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.example.chinesecheckers.utils.Requests.VolleyArrayRequest;
import com.example.chinesecheckers.UserProfile;
import com.example.chinesecheckers.models.UserModel;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

/**
 * Gets all friendships that have been accepted for the sent user and displays them in a list.
 *
 * <br>
 * <br>Example:
 * <br>GetFriends getFriends = new GetFriends(TAG, this, user, friendListView);
 * <br>String URL = Const.URL_GET_FRIENDS + "/" + user.getUID() + "/users";
 * <br>getFriends.sendRequest(Request.Method.GET, URL);
 */
public class GetFriends extends VolleyArrayRequest {

    private Context context;
    private ListView friendListView;
    private UserModel currentUser;
    private ArrayList<UserModel> friendList;

    /**
     * Creates the HTTP object to get all accepted friendships for the current user.
     *
     * @param tag - activity identifier
     * @param context - current context sending the request
     * @param currentUser - logged in user
     * @param friendListView - the list to display friendships
     */
    public GetFriends(String tag, Context context, UserModel currentUser, ListView friendListView) {
        super(tag, "json_friends");
        this.context = context;
        this.currentUser = currentUser;
        this.friendListView = friendListView;
        this.friendList = new ArrayList<>();
    }

    /**
     * Creates the list adapter for the friends list page and fills it with accepted friendships.
     * Each friendship can be clicked to redirect to the user's profile page.
     *
     * @param response - json array response from the HTTP request
     */
    @Override
    protected void handleResponse(JSONArray response) {
        Log.d(TAG, response.toString());

        try {
            for(int i = 0; i < response.length(); i++){
                JSONObject tempUser = response.getJSONObject(i);

                UserModel friend = new UserModel();
                friend.parseJson(tempUser);
                friendList.add(friend);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }

        ArrayAdapter<UserModel> arrayAdapter = new ArrayAdapter<>(context.getApplicationContext(),
                androidx.appcompat.R.layout.support_simple_spinner_dropdown_item, friendList);

        friendListView.setAdapter(arrayAdapter);

        // Allows user to click and go to the friends profile page.
        friendListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int pos, long id) {
                UserModel chosenUser = (UserModel) friendListView.getItemAtPosition(pos);

                Intent i = new Intent(context.getApplicationContext(), UserProfile.class);
                i.putExtra("user", currentUser);
                i.putExtra("chosenUser", chosenUser);
                context.startActivity(i);
            }
        });
    }
}
