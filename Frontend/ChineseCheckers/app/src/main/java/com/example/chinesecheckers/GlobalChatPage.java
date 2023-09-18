package com.example.chinesecheckers;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.example.chinesecheckers.models.UserModel;
import com.example.chinesecheckers.utils.Const;

import org.java_websocket.client.WebSocketClient;
import org.java_websocket.drafts.Draft;
import org.java_websocket.drafts.Draft_6455;
import org.java_websocket.handshake.ServerHandshake;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.Objects;

/**
 * this activity displays global chat and allows anyone to message.
 */
public class GlobalChatPage extends AppCompatActivity {

    private UserModel user;
    private Button sendMessage;
    private EditText userMessage;
    private TextView globalChatMessages;
    private WebSocketClient globalChatWebSocket;

    /**
     * creates a global chat to send messages and view them.
     *
     * @param savedInstanceState contains all saved instances and information.
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_global_chat_page);

        user = (UserModel) getIntent().getSerializableExtra("user");
        sendMessage = findViewById(R.id.SendMessageGlobal);
        userMessage = findViewById(R.id.messageGlobalChat);
        globalChatMessages = findViewById(R.id.globalChatMessages);

        Objects.requireNonNull(getSupportActionBar()).setDisplayHomeAsUpEnabled(true);

        joinGlobalWebSocket();

        sendMessage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    globalChatWebSocket.send(userMessage.getText().toString());
                } catch (Exception e) {
                    Log.d("ExceptionSendMessage:", e.getMessage().toString());
                }
            }
        });
    }

    /**
     * join status web socket joins the private lobby chat web socket and monitors when
     * people join, leave, and ready up. It also displays the private lobby chat with what
     * messages other users are sending.
     */
    private void joinGlobalWebSocket() {
        Draft[] drafts = {new Draft_6455()};

        /**
         * If running this on an android device, make sure it is on the same network as your
         * computer, and change the ip address to that of your computer.
         * If running on the emulator, you can use localhost.
         */
        String w = Const.URL_GLOBAL_CHAT_WEBSOCKET + user.getUID();

        try {
            Log.d("Socket:", "Trying socket");
            globalChatWebSocket = new WebSocketClient(new URI(w), (Draft) drafts[0]) {

                @Override
                public void onMessage(String message) {
                    Log.d("", "run() returned: " + message);

                    // the message was sent by a user to the chat.
                    if (message.contains(":")) {
                        runOnUiThread(new Runnable() {

                            @Override
                            public void run() {
                                String s = globalChatMessages.getText().toString();
                                globalChatMessages.setText(s + "\n" + message);
                            }
                        });
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
        globalChatWebSocket.connect();
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

            globalChatWebSocket.close();
            finish();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}