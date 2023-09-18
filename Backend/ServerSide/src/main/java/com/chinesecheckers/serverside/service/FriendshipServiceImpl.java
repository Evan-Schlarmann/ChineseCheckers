

// Java Program to Illustrate MoveServiceImpl File

// Importing package module to this code

package com.chinesecheckers.serverside.service;

import com.chinesecheckers.serverside.entity.Friendship;
import com.chinesecheckers.serverside.entity.User;
import com.chinesecheckers.serverside.repository.FriendshipRepository;
import com.chinesecheckers.serverside.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Random;

// Annotation
@Service

// Class

public class FriendshipServiceImpl implements FriendshipService {

    @Autowired
    private FriendshipRepository friendshipRepository;

    @Autowired
    private UserRepository userRepository;

    // Utility
    @Override
    public List<Friendship> ignoreRequests(List<Friendship> friendships) {
        List<Friendship> nonRequests = new ArrayList<>();
        for(Friendship friendship : friendships) {
            if (friendship.getAccepted())
                nonRequests.add(friendship);
        }
        return nonRequests;
    }

    @Override
    public List<Friendship> onlyRequests(List<Friendship> friendships) {
        List<Friendship> requests = new ArrayList<>();
        for(Friendship friendship : friendships) {
            if (!friendship.getAccepted())
                requests.add(friendship);
        }
        return requests;
    }

    // Save operation
    @Override
    public Friendship saveFriendship(Friendship friendship) {
        if (Objects.isNull(friendship.getRequester()) || Objects.isNull(friendship.getAccepter()))
            return null;
        if (Objects.isNull(userRepository.findById(friendship.getRequester().getUserId()))
                || Objects.isNull(userRepository.findById(friendship.getAccepter().getUserId())))
            return null;
        if (friendship.getRequester().getUserId().equals(friendship.getAccepter().getUserId()))
            return null;

        friendship.setRequester(userRepository.getById(friendship.getRequester().getUserId()));
        friendship.setAccepter(userRepository.getById(friendship.getAccepter().getUserId()));

        if (Objects.isNull(friendship.getAccepted()))
            friendship.setAccepted(false);

        List<Friendship> existing = fetchFriendshipsWith(friendship.getRequester());
        for (Friendship requesterFriend : existing) {
            if ((requesterFriend.getRequester().getUserId().equals(friendship.getRequester().getUserId())
                    && requesterFriend.getAccepter().getUserId().equals(friendship.getAccepter().getUserId()))
                    || (requesterFriend.getAccepter().getUserId().equals(friendship.getRequester().getUserId())
                    && requesterFriend.getRequester().getUserId().equals(friendship.getAccepter().getUserId())))
                return null;
        }

        return friendshipRepository.save(friendship);
    }


    // Read operations
    @Override
    public List<Friendship> fetchAllFriendships() {
        return (List<Friendship>) friendshipRepository.findAll();
    }

    @Override
    public List<Friendship> fetchFriendshipsWith(User user) {
        List<Friendship> related = new ArrayList<>();
        for (Friendship friendship : fetchAllFriendships()) {
            if (friendship.getRequester().getUserId().equals(user.getUserId())
                    || friendship.getAccepter().getUserId().equals(user.getUserId())) {
                related.add(friendship);
            }
        }
        return related;
    }

    @Override
    public List<User> fetchFriends(User user) {
        List<User> friends = new ArrayList<>();
        for (Friendship friendship : ignoreRequests(fetchFriendshipsWith(user))) {
            if (friendship.getAccepter().equals(user))
                friends.add(friendship.getRequester());
            else
                friends.add(friendship.getAccepter());
        }
        return friends;
    }

    // Update operation
    @Override
    public Friendship updateFriendship(Friendship friendship, Long friendshipId) {

        Friendship friendshipDB = friendshipRepository.findById(friendshipId).get();

        if (Objects.nonNull(friendship.getAccepted())) {
            friendshipDB.setAccepted(friendship.getAccepted());
        }

        return friendshipRepository.save(friendshipDB);
    }

    // Delete Operations
    @Override
    public void deleteFriendshipById(Long friendshipId) {
        friendshipRepository.deleteById(friendshipId);
    }

    @Override
    public void deleteFriendshipsOfUserId(Long userId) {
        for(Friendship friendship : fetchFriendshipsWith(userRepository.getById(userId))) {
            friendship.setAccepter(null);
            friendship.setRequester(null);
            updateFriendship(friendship, friendship.getFriendshipId());
            friendshipRepository.delete(friendship);
        }
    }
}
