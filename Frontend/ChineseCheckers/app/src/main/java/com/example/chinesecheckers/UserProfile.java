package com.example.chinesecheckers;

import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.TextView;

import com.android.volley.Request;
import com.example.chinesecheckers.utils.Requests.FriendRequests.GetFriendships;
import com.example.chinesecheckers.models.UserModel;
import com.example.chinesecheckers.utils.Const;

import java.util.Objects;

/**
 * UserProfile displays a user's general public information and their stats.
 * It also allows user's to see the status of a friend request, send a friend request,
 * or delete a friendship.
 */
public class UserProfile extends AppCompatActivity {

    private final String TAG = UserProfile.class.getSimpleName();
    private TextView profileUsername;
    private TextView profileRole;
    private Button friendRequestButton;
    private ProgressDialog pDialog;
    private UserModel user;
    private UserModel chosenUser;
    private TextView matchCount;
    private TextView winCount;
    private TextView ELOView;

    /**
     * onCreate links the XML view to text view to display the user information.
     * It also send a get friendships request to see if the user viewing this profile
     * is a friend of the profiles user.
     *
     * @param savedInstanceState - contains all saved instances and information.
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_profile);

        profileUsername = findViewById(R.id.profileUsername);
        profileRole = findViewById(R.id.profileRole);
        friendRequestButton = findViewById(R.id.friendRequestButton);
        user = (UserModel) getIntent().getSerializableExtra("user");
        chosenUser = (UserModel) getIntent().getSerializableExtra("chosenUser");
        matchCount = findViewById(R.id.userProfileMatchesPlayed);
        winCount = findViewById(R.id.userProfileGamesWon);
        ELOView = findViewById(R.id.userProfileELO);

        // This creates the back button.
        Objects.requireNonNull(getSupportActionBar()).setDisplayHomeAsUpEnabled(true);

        pDialog = new ProgressDialog(this);
        pDialog.setMessage("Sending Friend Request...");
        pDialog.setCancelable(false);

        profileUsername.setText(chosenUser.getUsername());
        profileRole.setText(chosenUser.getRole());
        matchCount.setText(Integer.toString(chosenUser.getStats().getMatchCount()));
        winCount.setText(Integer.toString(chosenUser.getStats().getWinCount()));
        ELOView.setText(Integer.toString(chosenUser.getStats().getELO()));

        GetFriendships getFriendships = new GetFriendships(TAG, this, user, chosenUser, friendRequestButton);
        String URL = Const.URL_GET_FRIENDSHIPS + "/" + chosenUser.getUID();
        getFriendships.sendRequest(Request.Method.GET, URL);
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