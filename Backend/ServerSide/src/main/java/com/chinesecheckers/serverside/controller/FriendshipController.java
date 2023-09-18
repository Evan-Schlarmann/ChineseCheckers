
// Java Program to Illustrate FriendshipController File

// Importing package module to this code

package com.chinesecheckers.serverside.controller;


import com.chinesecheckers.serverside.UserPair;
import com.chinesecheckers.serverside.entity.Friendship;
import com.chinesecheckers.serverside.entity.User;
import com.chinesecheckers.serverside.service.FriendshipService;
import com.chinesecheckers.serverside.service.UserService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

// Annotation
@Api(value = "FriendshipController", description = "REST APIs related to Friendship Entity")
@RestController

// Class
public class FriendshipController {

    // Annotation
    @Autowired
    private UserService userService;

    @Autowired
    private FriendshipService friendshipService;

    // Save operation
    @ApiOperation(value = "Create a new friendship (or request)", response = Friendship.class)
    @PostMapping("/friendships/new")
    public Friendship saveFriendship(@RequestBody Friendship friendship) {
        try {
            // Save is users exist
            return friendshipService.saveFriendship(friendship);
        }
        catch (Exception e) {
            return null;
        }
    }

    // Read operation
    @ApiOperation(value = "Fetches a list of all existing friendships no matter the status", response = Iterable.class)
    @GetMapping("/friendships/all")
    public List<Friendship> fetchAllFriendships() {
        return friendshipService.fetchAllFriendships();
    }

    @ApiOperation(value = "Fetch a specific friendship by its id", response = Friendship.class)
    @GetMapping("/friendships/id/{id}")
    public Friendship fetchFriendshipById( @PathVariable("id") Long id) {
        if (Objects.isNull(id)) return null;
        for (Friendship friendship : friendshipService.fetchAllFriendships()) {
            if (friendship.getFriendshipId().equals(id))
                return friendship;
        }
        return null;
    }

    @ApiOperation(value = "Fetches a list of friendships that contain a specific user", response = Iterable.class)
    @GetMapping("/friendships/user/{id}")
    public List<Friendship> fetchFriendshipsByUser( @PathVariable("id") Long id) {
        for (User user : userService.fetchUserList()) {
            if (user.getUserId().equals(id))
                return friendshipService.fetchFriendshipsWith(user);
        }
        return null;
    }

    @ApiOperation(value = "Fetches a list of friends as users (accepted friend requests) of a specific user", response = Iterable.class)
    @GetMapping("/friendships/user/friends/{id}/users")
    public List<User> fetchFriendsOfUser( @PathVariable("id") Long id) {
        for (User user : userService.fetchUserList()) {
            if (user.getUserId().equals(id))
                return friendshipService.fetchFriends(user);
        }
        return null;
    }

    @ApiOperation(value = "Fetches a list of friendship entities that have been accepted relating to a specific user", response = Iterable.class)
    @GetMapping("/friendships/user/friends/{id}")
    public List<Friendship> fetchAcceptedRequests( @PathVariable("id") Long id) {
        for (User user : userService.fetchUserList()) {
            if (user.getUserId().equals(id))
                return friendshipService.ignoreRequests(friendshipService.fetchFriendshipsWith(user));
        }
        return null;
    }

    @ApiOperation(value = "Fetches a list of outgoing friend requests from a specific user", response = Iterable.class)
    @GetMapping("/friendships/user/outgoing/{id}")
    public List<Friendship> fetchOutgoingRequests( @PathVariable("id") Long id) {
        for (User user : userService.fetchUserList()) {
            if (user.getUserId().equals(id)) {
                List<Friendship> requests = friendshipService.onlyRequests(friendshipService.fetchFriendshipsWith(user));
                List<Friendship> outgoing = new ArrayList<>();

                for (Friendship friendship : requests) {
                    if (friendship.getRequester().equals(user))
                        outgoing.add(friendship);
                }
                return outgoing;
            }
        }
        return null;
    }

    @ApiOperation(value = "Fetches a list of incoming friend requests to a specific user", response = Iterable.class)
    @GetMapping("/friendships/user/incoming/{id}")
    public List<Friendship> fetchIncomingRequest( @PathVariable("id") Long id) {
        for (User user : userService.fetchUserList()) {
            if (user.getUserId().equals(id)) {
                List<Friendship> requests = friendshipService.onlyRequests(friendshipService.fetchFriendshipsWith(user));
                List<Friendship> incoming = new ArrayList<>();

                for (Friendship friendship : requests) {
                    if (friendship.getAccepter().equals(user))
                        incoming.add(friendship);
                }
                return incoming;
            }
        }
        return null;
    }

    // Update friendships
    @ApiOperation(value = "Updates a frienship's status", response = Friendship.class)
    @PutMapping("/friendships/update")
    public Friendship updateFriendship(@RequestBody Friendship friendship) {
        if (Objects.isNull(fetchFriendshipById(friendship.getFriendshipId()))) return null;
        return friendshipService.updateFriendship(friendship, friendship.getFriendshipId());
    }

    // Delete operations
    @ApiOperation(value = "Deletes a single, specific friendship", response = String.class)
    @PutMapping("/friendships/delete/{id}")
    public String deleteFriendshipById(@PathVariable("id") Long friendshipId, @RequestBody User sender) {
        if (Objects.isNull(friendshipId) || Objects.isNull(sender)) return "failed";
        Friendship friendship = fetchFriendshipById(friendshipId);
        if (Objects.isNull(friendship)) return "failed";

        for (User user : userService.fetchUserList()) {
            if (user.getUserId().equals(sender.getUserId())) {
                if (user.getRole().equals("admin") ||
                        friendship.getRequester().equals(user) || friendship.getAccepter().equals(user)) {
                    friendship.setRequester(null);
                    friendship.setAccepter(null);
                    friendshipService.deleteFriendshipById(friendshipId);
                    return "Deleted Successfully";
                }
                return "denied";
            }
        }
        return "failed";
    }

    // Automatically occurs when deleting a user
    @ApiOperation(value = "Deletes all friendships relating to a specific user", response = Friendship.class)
    @DeleteMapping("/friendships/delete/user/{id}")
    public String deleteFriendshipsOfUser(@PathVariable("id") Long userId, @RequestBody User sender) {
        if (Objects.isNull(userId) || Objects.isNull(sender)) return "failed";
        User repoSender;
        try {
            // Check for valid users
            if (Objects.isNull(userService.fetchUserById(userId))) return null;
            repoSender = userService.fetchUserById(sender.getUserId());
            if (Objects.isNull(repoSender)) return null;

            // Attempt Deletion
            if (repoSender.getRole().equals("admin") || repoSender.getUserId().equals(userId)) {
                friendshipService.deleteFriendshipsOfUserId(userId);
                return "Deleted Successfully";
            }
        }
        catch (Exception e) {
            return "failed";
        }

        return "denied";
    }
}