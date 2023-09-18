package com.example.chinesecheckers;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.MenuItem;

import com.android.volley.Request;
import com.example.chinesecheckers.models.PrivateLobbyModel;
import com.example.chinesecheckers.utils.Requests.LobbyRequests.LeaveLobbyReq;
import com.example.chinesecheckers.models.UserModel;
import com.example.chinesecheckers.utils.Const;

import java.util.Objects;

/**
 * private lobby page displays the private lobby fragment which is also connected
 * to the private lobby chat. This allows the activity to switch fragments and go
 * to different activities.
 */
public class PrivateLobbyPage extends AppCompatActivity {

    private final String TAG = PrivateLobbyPage.class.getSimpleName();
    private UserModel user;
    private PrivateLobbyModel lobby;

    /**
     * onCreate links the XML page to the view and displays the lobby
     * information to all users in the activity.
     *
     * @param savedInstanceState - contains all saved instances and information.
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_private_lobby_page);

        user = (UserModel) getIntent().getSerializableExtra("user");
        lobby = (PrivateLobbyModel) getIntent().getSerializableExtra("lobby");

        // Creates and loads the private lobby fragment
        if (savedInstanceState == null) {
            Bundle bundle = new Bundle();
            bundle.putSerializable("user", user);
            bundle.putSerializable("lobby", lobby);

            getSupportFragmentManager().beginTransaction()
                    .add(R.id.privateLobbyActivity, PrivateLobbyFrag.class, bundle)
                    .commit();
        }
    }
}