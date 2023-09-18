package com.example.chinesecheckers;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import android.content.Intent;
import android.os.Bundle;
import android.text.method.ScrollingMovementMethod;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.example.chinesecheckers.models.PrivateLobbyModel;
import com.example.chinesecheckers.models.UserModel;
import com.example.chinesecheckers.utils.Const;

import org.java_websocket.client.WebSocketClient;
import org.java_websocket.drafts.Draft;
import org.java_websocket.drafts.Draft_6455;
import org.java_websocket.handshake.ServerHandshake;

import java.net.URI;
import java.net.URISyntaxException;

/**
 * A simple {@link Fragment} subclass.
 * <p>
 * This fragment holds the private lobby chat and allows users
 * in a lobby to communicate to each other. It also joins the game
 * when all players have readied up.
 */
public class PrivateLobbyChatFrag extends Fragment {

    private UserModel user;
    private PrivateLobbyModel lobby;
    Button sendMessageLobbyChat;
    EditText messageLobbyChat;
    TextView messageListLobbyChat;

    private WebSocketClient lobbyChatWebSocket;
    private WebSocketClient serviceTunnelWebSocket;

    public PrivateLobbyChatFrag() {
        super(R.layout.activity_private_lobby_chat_frag);
    }

    /**
     * This method takes the created view and links all the components. It allows the
     * user to send messages and go back the private lobby fragment.
     *
     * @param view               The View returned by {@link #onViewCreated(View, Bundle)}.
     * @param savedInstanceState If non-null, this fragment is being re-constructed
     *                           from a previous saved state as given here.
     */
    @Override
    public void onViewCreated(@NonNull View view, Bundle savedInstanceState) {

        user = (UserModel) requireArguments().getSerializable("user");
        lobby = (PrivateLobbyModel) requireArguments().getSerializable("lobby");

        sendMessageLobbyChat = view.findViewById(R.id.SendMessageLobbyChat);
        messageLobbyChat = view.findViewById(R.id.MessageLobbyChat);
        messageListLobbyChat = view.findViewById(R.id.MessageListLobbyChat);
        messageListLobbyChat.setMovementMethod(new ScrollingMovementMethod());
        Button leaveLobbyChat = view.findViewById(R.id.leaveLobbyChatButton);

        // Join the websockets.
        joinLobbyWebSocket();
        joinServiceTunnel();

        sendMessageLobbyChat.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    lobbyChatWebSocket.send(messageLobbyChat.getText().toString());
                } catch (Exception e) {
                    Log.d("ExceptionSendMessage:", e.getMessage().toString());
                }
            }
        });

        leaveLobbyChat.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                // Load the private lobby fragment and detach this fragment
                if (savedInstanceState == null) {
                    lobbyChatWebSocket.close();
                    serviceTunnelWebSocket.close();

                    Bundle bundle = new Bundle();
                    bundle.putSerializable("user", user);
                    bundle.putSerializable("lobby", lobby);

                    requireActivity().getSupportFragmentManager().beginTransaction()
                            .replace(R.id.privateLobbyActivity, PrivateLobbyFrag.class, bundle)
                            .commit();
                }
            }
        });
    }

    /**
     * join service tunnel creates a web socket that monitors if the game
     * has started and if so join the game activity.
     */
    private void joinServiceTunnel() {
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
                    if (messageType.length < 4) {
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
                    if (isLobby && lobbyId == lobby.getLobbyId() && isGame) {

                        // Move into the game activity.
                        Intent i = new Intent(getActivity(), GamePage.class);
                        i.putExtra("user", user);
                        i.putExtra("lobby", lobby);
                        i.putExtra("gameId", gameId);

                        startActivity(i);
                        requireActivity().finish();
                        lobbyChatWebSocket.close();
                        serviceTunnelWebSocket.close();
                        return;
                    }

                    // Check for a ready up message.
                    String messageUser = messageType[2];
                    boolean readyUp = messageType[3].equals("ready");
                    if (isLobby && lobbyId == lobby.getLobbyId() && readyUp) {
                        for (int i = 1; i <= lobby.getCurrentPlayerCount(); i++) {

                            // update the user's ready status
                            if (lobby.getPlayer(i).getUsername().equals(messageUser)) {
                                lobby.changePlayerStatus(i, true);
                            }
                        }
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
     * people join, leave, and ready up. It also displays the private lobby chat with what
     * messages other users are sending.
     */
    private void joinLobbyWebSocket() {
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

                    // the message was sent by a user to the chat.
                    if (message.contains(":")) {
                        String s = messageListLobbyChat.getText().toString();

                        messageListLobbyChat.setText(s + "\n" + message);

                        return;
                    }

                    // otherwise the user is joining or leaving.
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

                    // when a new player joins the lobby.
                    if (isNewPlayer) {

                        // add the new player to the lobby.
                        UserModel newUser = new UserModel();
                        newUser.setUsername(username);

                        lobby.addPlayer(newUser);

                    } else if (userAction.equals("has Left the Lobby")) { // a player left the lobby.

                        UserModel tempUser = new UserModel();
                        tempUser.setUsername(username);
                        lobby.removePlayer(tempUser);
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
}