package com.example.chinesecheckers;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.android.volley.Request;
import com.example.chinesecheckers.models.UserModel;
import com.example.chinesecheckers.utils.Const;
import com.example.chinesecheckers.utils.Requests.LobbyRequests.CreatePrivateLobbyReq;
import com.example.chinesecheckers.utils.Requests.LobbyRequests.JoinPrivateLobbyReq;
import com.example.chinesecheckers.utils.Requests.LobbyRequests.JoinPublicLobbyReq;

/**
 * HomePage is the main activity for Chinese Checkers. It allows access to the users settings, users list, game matchmaking,
 * and admin access controls to high level users.
 */
public class HomePage extends AppCompatActivity {

    private final String tag_json_joinLobby = "json_join";
    private final String TAG = HomePage.class.getSimpleName();
    private UserModel user;
    private Dialog privateLobbyDialog;

    /**
     * On Create sets up the View with on click listeners that redirect the user
     * to different pages. It requires the user from the previous intent in order
     * to have the user's information.
     *
     * It creates buttons to send a user into a game lobby, their settings page,
     * a user list page, and if admin, a admin controls page.
     *
     * @param savedInstanceState - contains all saved instances and information.
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home_page);

        //get user info from prev login
        user = (UserModel) getIntent().getSerializableExtra("user");
        ImageButton profileSettingsButton = findViewById(R.id.profileSettingsButton);
        ImageButton userListButton = findViewById(R.id.userListButton);
        Button joinPrivateLobby = findViewById(R.id.joinPrivateLobby);
        ImageButton adminHomePageButton = findViewById(R.id.adminHomePageButton);
        Button casualLobbyButton = findViewById(R.id.joinCasualLobbyButton);
        Button competitiveLobbyButton = findViewById(R.id.joinCompetitiveLobbyButton);
        Button globalChat = findViewById(R.id.joinGlobalChat);

        ProgressDialog pDialog = new ProgressDialog(this);
        pDialog.setMessage("Joining...");
        pDialog.setCancelable(false);

        profileSettingsButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(HomePage.this, ActiveUserProfile.class);
                i.putExtra("user", user);
                startActivity(i);
            }
        });

        userListButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(HomePage.this, UserList.class);
                i.putExtra("user", user);
                startActivity(i);
            }
        });

        // Only display the admin button to admins.
        if(user.getRole().equals("admin")) {
            adminHomePageButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent i = new Intent(HomePage.this, AdminHomePage.class);
                    i.putExtra("user", user);
                    startActivity(i);
                }
            });
        }
        else{
            adminHomePageButton.setVisibility(View.INVISIBLE);
        }

        casualLobbyButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                JoinPublicLobbyReq lobbyReq = new JoinPublicLobbyReq(TAG, user, HomePage.this);
                lobbyReq.createRequestObject(user);
                lobbyReq.sendRequest(Request.Method.POST, Const.URL_JOIN_CASUAL_LOBBY);
            }
        });

        competitiveLobbyButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                JoinPublicLobbyReq lobbyReq = new JoinPublicLobbyReq(TAG, user, HomePage.this);
                lobbyReq.createRequestObject(user);
                lobbyReq.sendRequest(Request.Method.POST, Const.URL_JOIN_COMPETITIVE_LOBBY);
            }
        });
            
        joinPrivateLobby.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // display a dialog to input lobby join code.
                showJoinPrivateLobbyDialog();
            }
        });

        globalChat.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(HomePage.this, GlobalChatPage.class);
                i.putExtra("user", user);
                startActivity(i);
            }
        });
    }

    /*
    Dialog method to join private lobbies by inputting a join code.
     */
    private void showJoinPrivateLobbyDialog() {
        privateLobbyDialog = new Dialog(HomePage.this);

        // Set dialog features and layout.
        privateLobbyDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        privateLobbyDialog.setCancelable(true);
        privateLobbyDialog.setContentView(R.layout.join_lobby_dialog);

        // Create components.
        Button joinLobby = privateLobbyDialog.findViewById(R.id.confirmJoinPrivateLobby);
        Button cancelJoin = privateLobbyDialog.findViewById(R.id.cancelJoinPrivateLobby);
        TextView joinCode = privateLobbyDialog.findViewById(R.id.textPrivateLobbyPasscode);
        Button createPrivateLobby = privateLobbyDialog.findViewById(R.id.createPrivateLobby);

        createPrivateLobby.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // create and join a private lobby.
                CreatePrivateLobbyReq createPrivateLobbyReq = new CreatePrivateLobbyReq(TAG, user, HomePage.this);
                createPrivateLobbyReq.createRequestObject(user);
                createPrivateLobbyReq.sendRequest(Request.Method.POST, Const.URL_CREATE_PRIVATE_LOBBY);
                privateLobbyDialog.dismiss();
            }
        });

        joinLobby.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(joinCode.getText().toString().equals("")){
                    joinCode.setError("Empty Join Code");
                }else {

                    JoinPrivateLobbyReq joinReq = new JoinPrivateLobbyReq(TAG, user, HomePage.this, privateLobbyDialog, joinCode);
                    joinReq.createRequestObject(user);
                    joinReq.sendRequest(Request.Method.POST, Const.URL_JOIN_PRIVATE_LOBBY + joinCode.getText().toString());
                }
            }
        });

        cancelJoin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                privateLobbyDialog.dismiss();
            }
        });

        // Display the dialog.
        privateLobbyDialog.show();
    }
}
