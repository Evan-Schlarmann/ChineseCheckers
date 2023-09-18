package com.example.chinesecheckers.models;

import androidx.annotation.NonNull;

/**
 * Models friend requests and the data it contains. It shows who created the
 * friend request, the target user, and if it has been accepted.
 */
public class FriendRequestModel {

    private long id;
    private UserModel requester;
    private UserModel accepter;
    private Boolean isAccepted;

    /**
     * Creates a friend request with all it's information.
     *
     * @param id - friend request ID
     * @param requester - user who created the friend request
     * @param accepter - target of the friend request
     * @param isAccepted - has the friend request been accepted
     */
    public FriendRequestModel(long id, UserModel requester, UserModel accepter, Boolean isAccepted){
        this.id = id;
        this.requester = requester;
        this.accepter = accepter;
        this.isAccepted = isAccepted;
    }

    /**
     * @return - friend request ID
     */
    public long getId(){
        return id;
    }

    /**
     * @return - user who created the friend request
     */
    public UserModel getRequester(){
        return requester;
    }

    /**
     * @return - target user of the friend request.
     */
    public UserModel getAccepter(){
        return accepter;
    }

    /**
     * @return - if the friend request has been accepted.
     */
    public Boolean isAccepted(){
        return isAccepted;
    }

    /**
     * @return - string to represent the target of the friend request.
     */
    @NonNull
    @Override
    public String toString() {
        return accepter.getUsername();
    }
}
