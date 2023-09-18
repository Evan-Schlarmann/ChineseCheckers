package com.example.chinesecheckers.utils;


import android.app.Activity;

/**
 * Const holds all HTTP request URLS that are used.
 */
public class Const {
    public static final String URL_LOGIN = "http://localhost:8080/users/login";
    public static final String URL_USERS_ALL = "http://localhost:8080/users/all";
    public static final String URL_USERS_NEW = "http://localhost:8080/users/new";
    public static final String URL_USERS_UPDATE = "http://localhost:8080/users/update";
    public static final String URL_GET_USER_BYUSERNAME = "http://localhost:8080/users/username/";
    public static final String URL_GET_USER_BYID = "http://localhost:8080/users/id/";
    public static final String URL_LEAVE_LOBBY = "http://localhost:8080/lobbys/leave";
    public static final String URL_JOIN_LOBBY = "http://localhost:8080/lobbys/join";
    public static final String URL_KICK_USER = "http://localhost:8080/lobbys/kick/";
    public static final String URL_CREATE_FRIEND_REQUEST = "http://localhost:8080/friendships/new";
    public static final String URL_OUTGOING_FRIEND_REQUESTS = "http://localhost:8080/friendships/user/outgoing";
    public static final String URL_INCOMING_FRIEND_REQUESTS = "http://localhost:8080/friendships/user/incoming";
    public static final String URL_ACCEPT_FRIEND_REQUEST = "http://localhost:8080/friendships/update";
    public static final String URL_DENY_FRIEND_REQUEST = "http://localhost:8080/friendships/delete";
    public static final String URL_GET_FRIENDS = "http://localhost:8080/friendships/user/friends";
    public static final String URL_GET_FRIENDSHIPS = "http://localhost:8080/friendships/user";
    public static final String URL_STATS = "http://localhost:8080/users/stats/";
    public static final String URL_BASE = "http://localhost:8080/";

    public static final String URL_CREATE_GAME = "http://localhost:8080/games/createfromlobby";
    public static final String URL_LOBBY = "http://localhost:8080/lobbys/";
    public static final String URL_GAME = "http://localhost:8080/games/";
    public static final String URL_CREATE_PRIVATE_LOBBY = "http://localhost:8080/lobbys/newprivate";
    public static final String URL_JOIN_PRIVATE_LOBBY = "http://localhost:8080/lobbys/join/";
    public static final String URL_JOIN_CASUAL_LOBBY = "http://localhost:8080/lobbys/join-casual";
    public static final String URL_JOIN_COMPETITIVE_LOBBY = "http://localhost:8080/lobbys/join-competitive";
    public static final String URL_PRIVATE_LOBBY_CHAT_WEBSOCKET = "http://localhost:8080/chat/lobby/";
    public static final String URL_SERVICE_TUNNEL_WEBSOCKET = "http://localhost:8080/servicetunnel/";
    public static final String URL_MUTE_USER = "http://localhost:8080/users/mute/";
    public static final String URL_UNMUTE_USER = "http://localhost:8080/users/unmute/";
    public static final String URL_GLOBAL_CHAT_WEBSOCKET = "http://localhost:8080/chat/global/";

    public static String URL_PRIVATE_LOBBY_CHAT(long lobbyID, long userID){
        return URL_PRIVATE_LOBBY_CHAT_WEBSOCKET + lobbyID + "/" + userID;
    }


    /**
     * Formats the URL of a 'get game move' request
     * @param gameID : id number of the Game
     * @param turnNumber : turn number of the requested move
     * @return url of the address for the request
     */
    public static String URL_GAME_MOVE(long gameID, int turnNumber){
        return URL_GAME + gameID + "/getMove/" + turnNumber;
    }

    /**
     * finishes the activity selected so that a user cannot navigate back to it.
     * @param a
     */
    public static void killActivity(Activity a){
        a.finish();
    }
}
