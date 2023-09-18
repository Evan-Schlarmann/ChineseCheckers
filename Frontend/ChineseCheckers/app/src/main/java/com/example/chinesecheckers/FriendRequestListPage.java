package com.example.chinesecheckers;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;

import com.android.volley.Request;
import com.example.chinesecheckers.utils.Requests.FriendRequests.GetFriendRequests;
import com.example.chinesecheckers.models.UserModel;
import com.example.chinesecheckers.utils.Const;
import com.example.chinesecheckers.utils.FriendRequest.FriendRequestEnum;

import java.util.Objects;

/**
 * FriendRequestListPage displays either incoming or outgoing friend requests depending
 * on which button the user has clicked. The user can then accept or delete incoming requests and
 * they can delete outgoing requests.
 */
public class FriendRequestListPage extends AppCompatActivity {

    private final String TAG = FriendRequestListPage.class.getSimpleName();
    private ListView friendRequestList;
    private Button incomingFriendRequests;
    private Button outgoingFriendRequests;
    private FriendRequestEnum selectedMenu = FriendRequestEnum.DEFAULT;
    private UserModel user;

    /**
     * onCreate loads the XML layout for the activity and saves its instance. It requires the
     * user from the previous intent in order to have the user's information. It then links the layout
     * to buttons allowing the user to toggle between incoming and outgoing friend requests. The
     * requests are displayed in a list that gives them the options to accept or delete them.
     *
     * @param savedInstanceState - contains all saved instances and information.
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_friend_request_list);

        friendRequestList = findViewById(R.id.friendRequestList);
        incomingFriendRequests = findViewById(R.id.incomingFriendRequests);
        outgoingFriendRequests = findViewById(R.id.outgoingFriendRequests);
        user = (UserModel) getIntent().getSerializableExtra("user");
        FriendRequestEnum requestType = (FriendRequestEnum) getIntent().getSerializableExtra("friendRequestType");

        // This creates the back button.
        Objects.requireNonNull(getSupportActionBar()).setDisplayHomeAsUpEnabled(true);

        // Load the preset friend request type wanted.
        if(requestType.equals(FriendRequestEnum.INCOMING)) {
            incomingFriendRequests.setBackgroundColor(0xFF3700B3);
            loadFriendRequests(FriendRequestEnum.INCOMING);
        }
        else if(requestType.equals(FriendRequestEnum.OUTGOING)){
            outgoingFriendRequests.setBackgroundColor(0xFF3700B3);
            loadFriendRequests(FriendRequestEnum.OUTGOING);
        }

        incomingFriendRequests.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                loadFriendRequests(FriendRequestEnum.INCOMING);
            }
        });

        outgoingFriendRequests.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                loadFriendRequests(FriendRequestEnum.OUTGOING);
            }
        });
    }

    /**
     * loadFriendRequests loads all the friend requests of the given type that a user is associated
     * with. It puts all the requests into a list displated to the user.
     *
     * @param requestType - friend request type INCOMING or OUTGOING.
     */
    private void loadFriendRequests(FriendRequestEnum requestType) {

        // Does not allow sending similar requests if the button was clicked already.
        if (selectedMenu.equals(requestType)) {
            return;
        }

        selectedMenu = requestType;

        if (selectedMenu.equals(FriendRequestEnum.INCOMING)) {
            incomingFriendRequests.setBackgroundColor(0xFF3700B3);
            outgoingFriendRequests.setBackgroundColor(0xFF985AFF);
            GetFriendRequests request =
                    new GetFriendRequests(TAG, this, user, friendRequestList, FriendRequestEnum.INCOMING);
            String URL = Const.URL_INCOMING_FRIEND_REQUESTS + "/" + user.getUID().toString();
            request.sendRequest(Request.Method.GET, URL);
        } else {
            outgoingFriendRequests.setBackgroundColor(0xFF3700B3);
            incomingFriendRequests.setBackgroundColor(0xFF985AFF);
            GetFriendRequests request =
                    new GetFriendRequests(TAG, this, user, friendRequestList, FriendRequestEnum.OUTGOING);
            String URL = Const.URL_OUTGOING_FRIEND_REQUESTS + "/" + user.getUID().toString();
            request.sendRequest(Request.Method.GET, URL);
        }
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
}