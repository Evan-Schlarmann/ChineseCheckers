package com.example.chinesecheckers.utils.FriendRequest;

/**
 * The type of a friend request.
 *
 * Default - there is no friend request.
 * Incoming - the friend request is sent to the given user.
 * Outgoing - the friend request is by the given user.
 * Accepted - the friend request has been accepted by the given user.
 */
public enum FriendRequestEnum {
    DEFAULT, INCOMING, OUTGOING, ACCEPTED
}
