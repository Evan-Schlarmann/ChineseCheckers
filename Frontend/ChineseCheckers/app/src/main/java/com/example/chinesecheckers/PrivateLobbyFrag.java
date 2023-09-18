package com.example.chinesecheckers;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.android.volley.Request;
import com.example.chinesecheckers.models.PrivateLobbyModel;
import com.example.chinesecheckers.models.UserModel;
import com.example.chinesecheckers.utils.Const;
import com.example.chinesecheckers.utils.Requests.LobbyRequests.LeaveLobbyReq;
import com.example.chinesecheckers.utils.Requests.LobbyRequests.LobbyGameReq;

import org.java_websocket.client.WebSocketClient;
import org.java_websocket.drafts.Draft;
import org.java_websocket.drafts.Draft_6455;
import org.java_websocket.handshake.ServerHandshake;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.ArrayList;

/**
 * A simple {@link Fragment} subclass.
 * <p>
 * This fragment displays the private lobby and everyone in it.
 * It also allows traversal to the private lobby chat. It creates a game
 * and loads the activity when all players have readied up.
 */
public class PrivateLobbyFrag extends Fragment {

    private final String TAG = PrivateLobbyPage.class.getSimpleName();

    private UserModel user;
    private PrivateLobbyModel lobby;
    private ArrayList<TextView> playerList;
    private ArrayList<ImageView> playerStatusList;
    private WebSocketClient lobbyChatWebSocket;
    private WebSocketClient serviceTunnelWebSocket;

    public PrivateLobbyFrag() {
        super(R.layout.fragment_private_lobby);
    }

    /**
     * This method loads all the view components and creates on click listeners.
     * It also gets the passed parameters from the private lobby page.
     *
     * @param view               The View returned by {@link #onViewCreated(View, Bundle)}.
     * @param savedInstanceState If non-null, this fragment is being re-constructed
     *                           from a previous saved state as given here.
     */
    @Override
    public void onViewCreated(@NonNull View view, Bundle savedInstanceState) {

        TextView lobbyIdView = view.findViewById(R.id.privateLobbyId);
        TextView joinCodeView = view.findViewById(R.id.privateLobbyJoinCode);
        Button openChatWebsocket = view.findViewById(R.id.chatWebsocketButton);
        Button readyUp = view.findViewById(R.id.privateLobbyReadyUp);
        Button leaveLobby = view.findViewById(R.id.leavePrivateLobby);
        user = (UserModel) requireArguments().getSerializable("user");
        lobby = (PrivateLobbyModel) requireArguments().getSerializable("lobby");

        // Lobby: {id}
        String tmpText = "Lobby: " + lobby.getLobbyId();
        lobbyIdView.setText(tmpText);

        // Join Code: {joinCode}
        tmpText = "Join Code: " + lobby.getJoinCode();
        joinCodeView.setText(tmpText);

        // Load the player views and join both web sockets.
        loadPlayerViews();
        joinStatusWebSocket();
        joinServiceTunnel();

        readyUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                boolean everyoneReady = true;
                if(lobby.getCurrentPlayerCount() != lobby.getPlayerCount()) { everyoneReady = false; }

                for (int i = 1; i <= lobby.getCurrentPlayerCount(); i++) {

                    if(lobby.getPlayer(i).getUsername().equals(user.getUsername())) {
                        lobby.changePlayerStatus(i, true);
                    }

                    // check if all players are ready
                    if (lobby.getPlayerStatus(i).equals(false))
                        everyoneReady = false;
                }

                if (everyoneReady) {
                    // send the request to generate a game
                    // that request sends a message to everyone in the lobby websocket to join.
                    LobbyGameReq gameReq = new LobbyGameReq(TAG, serviceTunnelWebSocket);
                    gameReq.createRequestObject(lobby);
                    gameReq.sendRequest(Request.Method.POST, Const.URL_CREATE_GAME);
                }else{
                    // send the ready up to all other players if not everyone was ready.
                    serviceTunnelWebSocket.send("lobbyId " + lobby.getLobbyId() + " " + user.getUsername() + " ready");
                }
            }
        });

        leaveLobby.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                lobbyChatWebSocket.close();
                serviceTunnelWebSocket.close();

                LeaveLobbyReq leaveLobbyReq = new LeaveLobbyReq(TAG, getActivity());
                leaveLobbyReq.createRequestObject(user);
                leaveLobbyReq.sendRequest(Request.Method.POST, Const.URL_LEAVE_LOBBY);
                requireActivity().finish();
            }
        });

        openChatWebsocket.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                // Load the chat fragment and detach this fragment
                if (savedInstanceState == null) {
                    lobbyChatWebSocket.close();
                    serviceTunnelWebSocket.close();

                    lobby.changePlayerStatus(user, false);

                    Bundle bundle = new Bundle();
                    bundle.putSerializable("user", user);
                    bundle.putSerializable("lobby", lobby);

                    requireActivity().getSupportFragmentManager().beginTransaction()
                            .replace(R.id.privateLobbyActivity, PrivateLobbyChatFrag.class, bundle)
                            .commit();
                }
            }
        });
    }

    /**
     * join service tunnel creates a web socket that monitors if the game
     * has started and if so join the game activity.
     */
    private void joinServiceTunnel(){
        Draft[] drafts = {new Draft_6455()};

        /**
         * If running this on an android device, make sure it is on the same network as your
         * computer, and change the ip address to that of your computer.
         * If running on the emulator, you can use localhost.
         */
        String w = Const.URL_SERVICE_TUNNEL_WEBSOCKET + user.getUID();

        try {
            Log.d("Socket:", "Trying socket");
            serviceTunnelWebSocket = new WebSocketClient(new URI(w), (Draft) drafts[0]) {

                @Override
                public void onMessage(String message) {
                    Log.d("", "run() returned: " + message);

                    // if receiving this lobbies lobbyId and a gameID join the game
                    String[] messageType = message.split(" ");

                    // if less than 4 the message can't be parsed.
                    if(messageType.length < 4) {
                        return;
                    }

                    boolean isLobby = messageType[0].equals("lobbyId");
                    long lobbyId = Long.parseLong(messageType[1]);
                    boolean isGame = messageType[2].equals("gameId");
                    long gameId = -1;

                    if (isGame) {
                        gameId = Long.parseLong(messageType[3]);
                    }

                    // the message is for this lobby load the new game.
                    if(isLobby && lobbyId == lobby.getLobbyId() && isGame){

                        // Move into the game activity.
                        Intent i = new Intent(getActivity(), GamePage.class);
                        i.putExtra("user", user);
                        i.putExtra("lobby", lobby);
                        i.putExtra("gameId", gameId);

                        lobbyChatWebSocket.close();
                        serviceTunnelWebSocket.close();
                        startActivity(i);
                        requireActivity().finish();
                        return;
                    }

                    // Check for a ready up message.
                    String messageUser = messageType[2];
                    boolean readyUp = messageType[3].equals("ready");
                    if(isLobby && lobbyId == lobby.getLobbyId() && readyUp){
                        for (int i = 1; i <= lobby.getCurrentPlayerCount(); i++) {

                            // update the user's ready status
                            if (lobby.getPlayer(i).getUsername().equals(messageUser)) {
                                lobby.changePlayerStatus(i, true);
                            }
                        }
                        loadPlayerViews();
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
        serviceTunnelWebSocket.connect();
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

                    // otherwise a user is joining or leaving.
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
                    e.printStackTrace();
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

        requireActivity().runOnUiThread(new Runnable() {

            @Override
            public void run() {

                playerList = new ArrayList<>();
                playerStatusList = new ArrayList<>();

                playerList.add(requireView().findViewById(R.id.privateLobbyPlayer1));
                playerList.add(requireView().findViewById(R.id.privateLobbyPlayer2));
                playerList.add(requireView().findViewById(R.id.privateLobbyPlayer3));
                playerList.add(requireView().findViewById(R.id.privateLobbyPlayer4));
                playerList.add(requireView().findViewById(R.id.privateLobbyPlayer5));
                playerList.add(requireView().findViewById(R.id.privateLobbyPlayer6));

                playerStatusList.add(requireView().findViewById(R.id.privateLobbyPlayerStatus1));
                playerStatusList.add(requireView().findViewById(R.id.privateLobbyPlayerStatus2));
                playerStatusList.add(requireView().findViewById(R.id.privateLobbyPlayerStatus3));
                playerStatusList.add(requireView().findViewById(R.id.privateLobbyPlayerStatus4));
                playerStatusList.add(requireView().findViewById(R.id.privateLobbyPlayerStatus5));
                playerStatusList.add(requireView().findViewById(R.id.privateLobbyPlayerStatus6));

                // disable all views that are not necessary for smaller game sizes.
                // reset all the needed views to their default.
                for (int i = 0; i < 6; i++) {
                    if (i <= lobby.getPlayerCount() - 1) {
                        playerList.get(i).setText("Player" + (i + 1));
                        playerStatusList.get(i).setImageResource(android.R.drawable.btn_dialog);
                        continue;
                    }

                    playerList.get(i).setVisibility(View.GONE);
                    playerStatusList.get(i).setVisibility(View.GONE);
                }

                // Update all the shown views with correct usernames and statuses.
                for (int i = 1; i <= lobby.getCurrentPlayerCount(); i++) {

                    UserModel player = lobby.getPlayer(i);
                    playerList.get(i - 1).setText(player.getUsername());

                    Log.d("user", player.getUsername());
                    Log.d("status", "" + lobby.getPlayerStatus(i));

                    Boolean status = lobby.getPlayerStatus(i);
                    if (status.equals(true)) {
                        playerStatusList.get(i - 1).setImageResource(R.drawable.player_ready);
                    } else {
                        playerStatusList.get(i - 1).setImageResource(R.drawable.player_not_ready);

                    }
                    playerStatusList.get(i - 1).invalidate();
                }
            }
        });
    }
}