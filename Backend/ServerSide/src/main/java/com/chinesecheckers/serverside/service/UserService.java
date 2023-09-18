
// Java Program to Illustrate DepartmentService File

// Importing package to this code fragment
package com.chinesecheckers.serverside.service;

import com.chinesecheckers.serverside.Stats;
import com.chinesecheckers.serverside.entity.User;

import java.util.List;

// Interface

public interface UserService {

    // Save operation
    User saveUser(User user);

    // Read operation
    List<User> fetchUserList();
    User fetchUserById(Long userId);

    // Update operation
    User updateUser(User user, Long userId);
    User updateStats(Stats stats, Long userId);
    User updateElo(Long elo, Long userId);
    // Delete operation
    void deleteUserById(Long userId);

    // Utility
    Long getStartingElo();

    User muteUser(Long id);
    User unmuteUser(Long id);
}
