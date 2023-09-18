
// Java Program to Illustrate DepartmentService File

// Importing package to this code fragment
package com.chinesecheckers.serverside.service;

import com.chinesecheckers.serverside.entity.Friendship;
import com.chinesecheckers.serverside.entity.User;

import java.util.List;

// Interface

public interface FriendshipService {

    // Utility
    List<Friendship> ignoreRequests(List<Friendship> friendships);
    List<Friendship> onlyRequests(List<Friendship> friendships);

    // Save operation
    Friendship saveFriendship(Friendship friendship);

    // Read operations
    List<Friendship> fetchAllFriendships();
    List<Friendship> fetchFriendshipsWith(User user);
    List<User> fetchFriends(User user);

    // Update operation
    Friendship updateFriendship(Friendship friendship, Long friendshipId);

    // Delete operations
    void deleteFriendshipById(Long friendshipId);
    void deleteFriendshipsOfUserId(Long userId);
}
