package com.chinesecheckers.serverside;

import com.chinesecheckers.serverside.entity.User;
import io.swagger.annotations.ApiModelProperty;

/**
 * Pair of users for verification purposes where the target User is experiencing some change
 * and the sender User is the one responsible for it. This allows for role permissions.
 */
public class UserPair {
    @ApiModelProperty(notes = "Target user", name = "target", required = true, value = "The user being affected by a request")
    private User target;
    @ApiModelProperty(notes = "Sender user", name = "sender", required = true, value = "The user initiating the request")
    private User sender;

    public UserPair() {}

    // User
    public void setTarget(User user) {
        target = user;
    }

    public void setSender(User user) {
        sender = user;
    }

    public User getTarget() {
        return target;
    }

    public User getSender() {
        return sender;
    }

}
