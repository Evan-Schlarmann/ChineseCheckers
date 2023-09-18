package com.example.chinesecheckers;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.example.chinesecheckers.app.AppController;
import com.example.chinesecheckers.models.BoardModel;
import com.example.chinesecheckers.models.LobbyModel;
import com.example.chinesecheckers.models.PublicLobbyModel;
import com.example.chinesecheckers.models.UserModel;
import com.example.chinesecheckers.utils.Const;
import com.example.chinesecheckers.utils.Coordinate;
import com.example.chinesecheckers.utils.PegMove;

import org.java_websocket.client.WebSocketClient;
import org.java_websocket.drafts.Draft;
import org.java_websocket.drafts.Draft_6455;
import org.java_websocket.handshake.ServerHandshake;
import org.json.JSONException;
import org.json.JSONObject;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.ArrayList;

/**
 * GamePage displays a the game the user currently occupies, and communicates with the server to
 * make and receive moves with other players. Allows users to quit and return to the lobby.
 */
public class GamePage extends AppCompatActivity {
    private final String TAG = GamePage.class.getSimpleName();
    private BoardModel internalBoard;
    private Button select;
    private Button setDestination;
    private Button sendMove;
    private Button resetBoard;
    private Button leaveGame;
    private Coordinate selected;
    private  Coordinate destination;
    private TextView clickedLocation;
    private TextView mockGameBored;
    //other inherited stuff like user/lobby/game id
    private LobbyModel lobby;
    private UserModel user;
    private Long gameId;
    private Long[] players;
    private int myPlayer;
    private TextView gameNumber;
    private Button refresh;

    private WebSocketClient lobbyChatWebSocket;
    private WebSocketClient serviceTunnelWebSocket;
    private GameBoardFrag frag;

    /**
     * Sets up the XML for the activity and saves it's instance state
     * @param savedInstanceState
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game_page);
    //    clickedLocation = findViewById(R.id.clickedCoordinate);
//        leaveGame = findViewById(R.id.exitButton);
//        refresh = findViewById(R.id.refreshButton);
//        resetBoard = findViewById(R.id.ResetButton); //testing only
        lobby = (LobbyModel) getIntent().getSerializableExtra("lobby"); //upcast
        user = (UserModel) getIntent().getSerializableExtra("user");
        gameId = (Long) getIntent().getSerializableExtra("gameId");
        setPlayer();
        /*
        refresh.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                refresh();
            }
        });
        leaveGame.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                leave();
            }
        }); */
        internalBoard = new BoardModel();
        getMoves();
        frag =  findViewById(R.id.gameBoardFrag2);
        frag.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent event) {
                int y = (int)(event.getY() * 17 / frag.getHeight());
                int x = -2 + (int)(( (event.getX() * 26 / frag.getWidth()) + y) / 2);

                Coordinate clicked = new Coordinate(x, y);

                if( x < 0 || x > 16 || y < 0 || y > 16) {
                    clicked = new Coordinate(0, 0);
                }
               // clickedLocation.setText(clicked.getPiece(internalBoard.getRawBoard()) + "( " + clicked.getX() + " , " + clicked.getY() + " ) P:" + myPlayer);
                fragUser(clicked);
                return false;
            }
        });
        gameWebSocket();
    }

    private ArrayList<Coordinate> potentials = new ArrayList<Coordinate>();
    /**
     * handler for when the user clicks a given coordinate on the board
     * @param c
     */
    private void fragUser(Coordinate c) {
        /* TESTING ONLY / // Add * to test
        if(internalBoard.getRawBoard()[c.getX()][c.getY()] == 0 && !frag.getSelected().equals(new Coordinate(0,0))) {
            PegMove move = new PegMove(myPlayer, frag.getSelected(), c);
            //internalBoard.forceSwap(frag.getSelected(), c); //move any piece locally
            //internalBoard.makeMove(move); //move only your pieces locally
            serverSendMove(move); //move only your pieces when server allows
            frag.deselect(internalBoard);
        }
        /* TESTING ONLY */


        if (internalBoard.getRawBoard()[c.getX()][c.getY()] == myPlayer){
            potentials = PegMove.getValidEndPositions(internalBoard.getRawBoard(), c);
            frag.select(c, internalBoard);
            return;
        }
        else if(internalBoard.getRawBoard()[c.getX()][c.getY()] == 0 && !frag.getSelected().equals(new Coordinate(0,0)) && potentials.contains(c)){
            // clickedLocation is empty AND you have a piece selected AND the move is valid
            PegMove move = new PegMove(myPlayer, frag.getSelected(), c);
            /* testing */
            //internalBoard.forceSwap(frag.getSelected(), c); //move any piece
            //internalBoard.makeMove(move); //move only your pieces
            serverSendMove(move); //move only your pieces when server allows
            /* testing */
            frag.deselect(internalBoard);

        } else {
            potentials = new ArrayList<Coordinate>();
            frag.deselect(internalBoard);
            return;
        }
    }


    /**
     * sets local game player order based on the order in the server
     */
    private void setPlayer() {
        JsonObjectRequest jsonObjReq = new JsonObjectRequest (Request.Method.GET, Const.URL_LOBBY + lobby.getLobbyId(), null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                Log.d(TAG, response.toString());
                try {
                    //gameId = (Long) response.getLong("gameId");
                    JSONObject game = (JSONObject) response.get("game");
                    int playerCount = game.getInt("playerCount");
                    players = new Long[playerCount];
                    for(int i = 1; i <= playerCount; i++){
                        if(i == 1){
                            JSONObject player = (JSONObject) response.get("player1");
                            players[i-1] = player.getLong("userId");
                            if(user.getUID().equals(players[i-1])){myPlayer = 1;}
                        }
                        if(i == 2 && response.get("player2") != null){
                            JSONObject player = (JSONObject) response.get("player2");
                            players[i-1] = player.getLong("userId");
                            if(user.getUID().equals(players[i-1])){myPlayer = 4;}
                        }
                        if(i == 3 && response.get("player3") != null){
                            JSONObject player = (JSONObject) response.get("player3");
                            players[i-1] = player.getLong("userId");
                            if(user.getUID().equals(players[i-1])){myPlayer = 2;}
                        }
                        if(i == 4 && response.get("player4") != null){
                            JSONObject player = (JSONObject) response.get("player4");
                            players[i-1] = player.getLong("userId");
                            if(user.getUID().equals(players[i-1])){myPlayer = 5;}
                        }
                        if(i == 5 && response.get("player5") != null){
                            JSONObject player = (JSONObject) response.get("player5");
                            players[i-1] = player.getLong("userId");
                            if(user.getUID().equals(players[i-1])){myPlayer = 3;}
                        }
                        if(i == 6 && response.get("player6") != null){
                            JSONObject player = (JSONObject) response.get("player6");
                            players[i-1] = player.getLong("userId");
                            if(user.getUID().equals(players[i-1])){myPlayer = 6;}
                        }
//                        String text = gameId.toString() + "\n" + myPlayer + "\n";
//                        for(int j = 0; j < players.length - 1; j++){
//                            text += players[j] + " ";
//                        }
//                        gameNumber.setText(text);
                    }
                }
                catch (JSONException e) {e.printStackTrace();}
                catch (ClassCastException e) {e.printStackTrace();}
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                error.printStackTrace();
            }
        });
        AppController.getInstance().addToRequestQueue(jsonObjReq,"getLobby");
    }

    /**
     * leaves the game and closes the activity page
     */
    private void leave(){
        lobbyChatWebSocket.close();
        Intent i = new Intent(GamePage.this, HomePage.class);
        i.putExtra("user", user);
        this.finish();
    }
    private PegMove sentPegMove;

    /**
     * sends a move to the server and makes it locally after it has been received
     * @param move - move to be sent to the server
     */
    private void serverSendMove(PegMove move){
        sentPegMove = move;
        JSONObject params = new JSONObject();
        try {
            JSONObject userjson = new JSONObject();
            try {
                userjson.put("userId", user.getUID());
                userjson.put("username", user.getUsername());
            }catch (JSONException e) {
                e.printStackTrace();
            }
            JSONObject lobbyjson = new JSONObject();
            try {
                lobbyjson.put("lobbyId", lobby.getLobbyId());
            }catch (JSONException e) {
                e.printStackTrace();
            }
            JSONObject gamejson = new JSONObject();
            try {
                gamejson.put("gameId", gameId);
            }catch (JSONException e) {
                e.printStackTrace();
            }
            params.put("player", userjson); //String?
            params.put("lobby", lobbyjson);
            params.put("game", gamejson);
            params.put("gameId", gameId);
            params.put("turnCount", internalBoard.getTurn());
            params.put("xstartIndex", move.getStart().getX());
            params.put("ystartIndex", move.getStart().getY());
            params.put("xendIndex", move.getEnd().getX());
            params.put("yendIndex", move.getEnd().getY());
            //add other needed params if required
        } catch (JSONException e) {
            e.printStackTrace();
        }
        Log.d("MOVE SENT",params.toString());
        StringRequest jsonObjReq =
                new StringRequest(Request.Method.POST, Const.URL_GAME + gameId +"/makeMove",
                        new Response.Listener<String>() {
                            @Override
                            public void onResponse(String response) {
                                Log.d("NEXT TURN IS TURN", response.toString());
                                try{
                                    if(Integer.valueOf(response) == internalBoard.getTurn() + 1){
                                        getMove(Integer.valueOf(response)-1);
                                        lobbyChatWebSocket.send(response);
                                    } else if(Integer.valueOf(response) > 0){
                                        getMoves(); //refresh state bc of DeSync
                                    }
                                } catch (Exception e){
                                    Log.d("ERROR", e.getMessage());
                                }
                            }
                        }, new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        VolleyLog.d(TAG, "Error: " + error.getMessage());
                    }
                }){
                    @Override
                    public byte[] getBody() throws AuthFailureError {
                        return params.toString().getBytes();
                    }

                    @Override
                    public String getBodyContentType() {
                        return "application/json; charset=utf-8";
                    }
                };
        AppController.getInstance().addToRequestQueue(jsonObjReq, "sendMove");
    }
 //DEPENDENCY ON GETMOVE

    /**
     * Retrieves a move from the server and makes the move on the board
     * @param turn - turn of the move to be retrieved
     */
    private void getMove(int turn){
        Log.d("TURN REQUESTED", String.valueOf(turn));
        //Retrieving a move can be done through a get request to /games/{gameId}/getMove/{turnnumber} This will return the move on that turn
        JsonObjectRequest jsonObjReq = new JsonObjectRequest (Request.Method.GET, Const.URL_GAME_MOVE(gameId, turn), null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                Log.d("MOVE RECEIVED", response.toString());
                try {
                    int x = response.getInt("xstartIndex");
                    int y = response.getInt("ystartIndex");
                    Coordinate start = new Coordinate(x, y);
                    x = response.getInt("xendIndex");
                    y = response.getInt("yendIndex");
                    Coordinate end = new Coordinate(x, y);
                    int turn = response.getInt("turnCount");
                    Log.d("MOVE START", start.toString());
                    Log.d("MOVE END", start.toString());
                    PegMove move = new PegMove(internalBoard.getRawBoard()[start.getX()][start.getY()], start, end);
                    /* TESTING ONLY /
                    internalBoard.forceSwap(move.getStart(), move.getEnd());
                    /* TESTING ONLY */
                    internalBoard.makeMove(move);
                    frag.deselect(internalBoard);
                    Log.d("Have You Won Yet?", String.valueOf(internalBoard.playerWin(myPlayer)));
                    if(internalBoard.playerWin(myPlayer)){
                        sendWinNotification();
                    }
//                    JSONObject player = (JSONObject) response.get("player");
//                    Long id = player.getLong("userId");
//                    for(int i = 0; i < players.length; i++){
//                        if(players[i] == id){
//                            if(turn == internalBoard.getTurn() + 1){
//                            PegMove getMovePegMove = new PegMove(i + 1, start, end);
//                            internalBoard.makeMove(getMovePegMove);
//                            frag.deselect(internalBoard);}
//                        }
//                    }
                }
                catch (JSONException e) {
                    e.printStackTrace();}
                catch (ClassCastException c) {
                    c.printStackTrace();}
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                error.printStackTrace();
            }
        });
        AppController.getInstance().addToRequestQueue(jsonObjReq,"getGame");
    }

    /**
     * brings the board to the current server state
     */
    private void getMoves(){
        Log.d("BOARD HAS DESYNCED", "REFRESHING");
        JsonObjectRequest jsonObjReq = new JsonObjectRequest (Request.Method.GET, Const.URL_LOBBY + lobby.getLobbyId(), null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                Log.d(TAG, response.toString());
                try {
                    JSONObject object = (JSONObject) response.get("game");
                    int turnNumber = object.getInt("turnNumber");
                    internalBoard.resetBoard(); //reset state
                    for (int i = 0; i < turnNumber; i++){
                        getMove(i);
                    }
                }
                catch (JSONException e) {e.printStackTrace();}
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                error.printStackTrace();
            }
        });
        AppController.getInstance().addToRequestQueue(jsonObjReq,"getGame");
    }

    /**
     * background websocket to notify players when a turn has been made
     */
    private void gameWebSocket() {
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
                    message = message.substring(message.indexOf(" ") + 1);
                    if(message.contains(" has Joined the Lobby")){
                        return;
                    }
                    //case 1, player wins -> end game
                    if(message.contains("has Won the GAME!")){
                        leave();; //end game and close websocket
                    }
                    //move should be move index
                    else if (Integer.valueOf(message) == internalBoard.getTurn() + 1){
                        getMove(internalBoard.getTurn());
                    } else {
                        getMoves(); //refresh to get rid of desync
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
     * tells the server and other players that the user has won the game
     */
    private void sendWinNotification(){
        JSONObject params = new JSONObject();
        try{
            params.put("Nothing", "Empty");
            params.put("lobbyId", lobby.getLobbyId());
        }catch (Exception e){}
        Log.d("WIN SENT",params.toString());
        StringRequest jsonObjReq =
                new StringRequest(Request.Method.POST, Const.URL_LOBBY + lobby.getLobbyId() + "/win-game/" + user.getUID(),
                        new Response.Listener<String>() {
                            @Override
                            public void onResponse(String response) {
                                Log.d("GAME WON", response.toString());
                                try {
                                    lobbyChatWebSocket.send("has Won the GAME!");
                                } catch (Exception e){

                                }
                            }
                        }, new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        VolleyLog.d(TAG, "Error: " + error.getMessage());
                    }
                }){
                    @Override
                    public byte[] getBody() throws AuthFailureError {
                        return params.toString().getBytes();
                    }
                    @Override
                    public String getBodyContentType() {
                        return "application/json; charset=utf-8";
                    }
                };
        AppController.getInstance().addToRequestQueue(jsonObjReq, "sendWin");

    }
}

