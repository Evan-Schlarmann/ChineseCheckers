package com.example.chinesecheckers;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;

import com.android.volley.Request;
import com.example.chinesecheckers.utils.Requests.FriendRequests.GetFriends;
import com.example.chinesecheckers.models.UserModel;
import com.example.chinesecheckers.utils.Const;
import com.example.chinesecheckers.utils.FriendRequest.FriendRequestEnum;

import java.util.Objects;

/**
 * FriendListPage displays all friends a user has in a list. The user can redirect to their
 * friend's profile page or the user can redirect to their friend request list.
 */
public class FriendListPage extends AppCompatActivity {

    private String TAG = FriendListPage.class.getSimpleName();
    private Button friendRequestList;
    private ListView friendListView;
    private UserModel user;

    /**
     * onCreate loads the XML layout for this activity and saves its instance.
     *
     * @param savedInstanceState - contains all saved instances and information.
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_friend_list);

        // The activity is created when it calls on resume.
        // It also calls on resume the first time it is created.
    }

    /**
     * onOptionsItemSelected finishes the activity when the support action bar back button is pressed.
     *
     * @param item - item that is selected.
     * @return boolean
     */
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            finish();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    /**
     * onResume is called when the activity is created and resumed.
     * It calls setup activity which creates the view for the activity.
     */
    @Override
    public void onResume(){
        super.onResume();

        // This reloads the activity information in-case of updates.
        // On Resume is automatically called when the activity is created.
        setupActivity();
    }

    /**
     * setupActivity links the friend list and friend request button. It requires the user
     * from the previous intent in order to have the user's information. It loads the user's
     * friends and displays them in a list. With a button that redirect to friend requests.
     */
    private void setupActivity(){

        friendRequestList = findViewById(R.id.friendRequestListButton);
        friendListView = findViewById(R.id.friendList);
        user = (UserModel) getIntent().getSerializableExtra("user");

        Objects.requireNonNull(getSupportActionBar()).setDisplayHomeAsUpEnabled(true);

        // Get all friend and makes a custom list view.
        GetFriends getFriends = new GetFriends(TAG, this, user, friendListView);
        String URL = Const.URL_GET_FRIENDS + "/" + user.getUID() + "/users";
        getFriends.sendRequest(Request.Method.GET, URL);

        friendRequestList.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(FriendListPage.this, FriendRequestListPage.class);
                i.putExtra("friendRequestType", FriendRequestEnum.INCOMING);
                i.putExtra("user", user);
                startActivity(i);
            }
        });
    }
}