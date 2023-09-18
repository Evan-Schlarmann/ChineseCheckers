package com.example.chinesecheckers;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.android.volley.Request;
import com.example.chinesecheckers.models.PublicLobbyModel;
import com.example.chinesecheckers.utils.Requests.LobbyRequests.LeaveLobbyReq;
import com.example.chinesecheckers.utils.Requests.LobbyRequests.LobbyGameReq;
import com.example.chinesecheckers.models.UserModel;
import com.example.chinesecheckers.utils.Const;

import org.java_websocket.client.WebSocketClient;
import org.java_websocket.drafts.Draft;
import org.java_websocket.drafts.Draft_6455;
import org.java_websocket.handshake.ServerHandshake;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.ArrayList;

/**
 *  LobbyPage is the user 'hub' where users are grouped before a game begins.
 *  Users can navigate to either leave the lobby or wait for the game to start.
 */
public class LobbyPage extends AppCompatActivity {
    private final String tag_json_leaveLobby = "json_leave";
    private final String TAG = LobbyPage.class.getSimpleName();
    private Button leaveLobby;
    private TextView lobbyNumber;
    private PublicLobbyModel lobby;
    private UserModel user;
    private ArrayList<TextView> playerList;
    private WebSocketClient lobbyChatWebSocket;

     /**
     * On Create sets up the View with a websocket that listens for when other
      * players join or leave updating the screen to display the lobby. The game
      * activity starts when the lobby is full.
     *
     * It creates buttons to send a user into a game, or to return them to the home page
     *
     * @param savedInstanceState - contains all saved instances and information.
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lobby_page);
        leaveLobby = findViewById(R.id.leavePublicLobby);
        lobbyNumber = findViewById(R.id.publicLobbyId);
        lobby = (PublicLobbyModel) getIntent().getSerializableExtra("lobby");
        user = (UserModel) getIntent().getSerializableExtra("user");

        lobbyNumber.setText("Lobby: " + lobby.getLobbyId());

        loadPlayerViews();
        joinStatusWebSocket();

        if(lobby.getPlayerCount() == lobby.getCurrentPlayerCount()){
            // send the request to generate a game
            // that request sends a message to everyone in the lobby websocket to join.
            LobbyGameReq gameReq = new LobbyGameReq(TAG, lobbyChatWebSocket);
            gameReq.createRequestObject(lobby);
            gameReq.sendRequest(Request.Method.POST, Const.URL_CREATE_GAME);
        }

        //button to leave lobby and go back to home page
        leaveLobby.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                lobbyChatWebSocket.close();

                LeaveLobbyReq leaveLobbyReq = new LeaveLobbyReq(TAG, LobbyPage.this);
                leaveLobbyReq.createRequestObject(user);
                leaveLobbyReq.sendRequest(Request.Method.POST, Const.URL_LEAVE_LOBBY);
                LobbyPage.this.finish();
            }
        });
    }

    /**
     * join status web socket joins the private lobby chat web socket and monitors when
     * people join, leave, and ready up to refresh the page and display up to date information.
     */
    private void joinStatusWebSocket() {
        Draft[] drafts = {new Draft_6455()};

        /**
         * If running this on an android device, make sure it is on the same network as your
         * computer, and change the ip address to that of your computer.
         * If running on the emulator, you can use localhost.
         */
        String w = Const.URL_PRIVATE_LOBBY_CHAT(lobby.getLobbyId(), user.getUID());

        try {
            Log.d("Socket:", "Trying socket");
            lobbyChatWebSocket = new WebSocketClient(new URI(w), (Draft) drafts[0]) {

                @Override
                public void onMessage(String message) {
                    Log.d("", "run() returned: " + message);

                    // a user is joining or leaving.
                    String username = message.substring(0, message.indexOf(" "));
                    String userAction = message.substring(message.indexOf(" ") + 1);
                    boolean isNewPlayer = true;
                    for (int i = 1; i <= lobby.getCurrentPlayerCount(); i++) {

                        // Look and see if it is a new user and add them
                        // or return if they are not a new user to the lobby.
                        if (lobby.getPlayer(i).getUsername().equals(username)) {
                            isNewPlayer = false;
                        }
                    }

                    // when a new player joins the lobby
                    if (isNewPlayer && userAction.equals("has Joined the Lobby")) {

                        // add the user to the lobby
                        UserModel tempUser = new UserModel();
                        tempUser.setUsername(username);
                        lobby.addPlayer(tempUser);

                        // reload the page.
                        loadPlayerViews();
                    } else if (userAction.equals("has Left the Lobby")) { // a player left the lobby.

                        UserModel tempUser = new UserModel();
                        tempUser.setUsername(username);
                        lobby.removePlayer(tempUser);
                        // reload the page.
                        loadPlayerViews();
                    }

                    String[] messageInfo = userAction.split(" ");
                    if(messageInfo.length == 4){

                        boolean isLobby = messageInfo[0].equals("lobbyId");
                        if(!isLobby)
                            return;

                        long lobbyId = Long.parseLong(messageInfo[1]);
                        long gameId = Long.parseLong(messageInfo[3]);

                        if(lobbyId != lobby.getLobbyId())
                            return;

                        // start the game activity.
                        Intent i = new Intent(LobbyPage.this, GamePage.class);
                        i.putExtra("user", user);
                        i.putExtra("lobby", lobby);
                        i.putExtra("gameId", gameId);

                        lobbyChatWebSocket.close();
                        startActivity(i);
                        LobbyPage.this.finish();
                    }
                }

                @Override
                public void onOpen(ServerHandshake handshake) {
                    Log.d("OPEN", "run() returned: " + "is connecting");

                }

                @Override
                public void onClose(int code, String reason, boolean remote) {
                    Log.d("CLOSE", "onClose() returned: " + reason);
                }

                @Override
                public void onError(Exception e) {
                    Log.d("Exception:", e.toString());
                }
            };
        } catch (URISyntaxException e) {
            Log.d("Exception:", e.getMessage().toString());
            e.printStackTrace();
        }
        lobbyChatWebSocket.connect();
    }


    /**
     * load player views updates the private lobby page to display players
     * and their ready up status.
     */
    private void loadPlayerViews() {

        LobbyPage.this.runOnUiThread(new Runnable() {

            @Override
            public void run() {

                playerList = new ArrayList<>();

                playerList.add(findViewById(R.id.publicLobbyPlayer1));
                playerList.add(findViewById(R.id.publicLobbyPlayer2));
                playerList.add(findViewById(R.id.publicLobbyPlayer3));
                playerList.add(findViewById(R.id.publicLobbyPlayer4));
                playerList.add(findViewById(R.id.publicLobbyPlayer5));
                playerList.add(findViewById(R.id.publicLobbyPlayer6));

                // disable all views that are not necessary for smaller game sizes.
                // reset all the needed views to their default.
                for (int i = 0; i < 6; i++) {
                    if (i <= lobby.getPlayerCount() - 1) {
                        playerList.get(i).setText("Waiting...");
                        continue;
                    }

                    playerList.get(i).setVisibility(View.GONE);
                }

                // Update all the shown views with correct usernames and statuses.
                for (int i = 1; i <= lobby.getPlayerCount(); i++) {
                    if (lobby.getCurrentPlayerCount() < i)
                        return;

                    UserModel player = lobby.getPlayer(i);
                    playerList.get(i - 1).setText(player.getUsername());
                }
            }
        });
    }
}