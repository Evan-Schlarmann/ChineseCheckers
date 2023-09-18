
// Java Program to Illustrate UserController File

// Importing package module to this code

package com.chinesecheckers.serverside.controller;


import com.chinesecheckers.serverside.Stats;
import com.chinesecheckers.serverside.UserPair;
import com.chinesecheckers.serverside.entity.Friendship;
import com.chinesecheckers.serverside.entity.User;
import com.chinesecheckers.serverside.repository.UserRepository;
import com.chinesecheckers.serverside.service.FriendshipService;
import com.chinesecheckers.serverside.service.UserService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;
import java.util.Objects;

// Annotation
@Api(value = "UserController", description = "REST APIs related to User Entity")
@RestController

// Class
public class UserController {

    // Annotation
    @Autowired
    private UserService userService;

    @Autowired
    private FriendshipService friendshipService;

    // Save operation
    @ApiOperation(value = "Create a new user", response = User.class)
    @PostMapping("/users/new")
    public User saveUser( @RequestBody User user) {
        if (fetchUser(user.getUsername()).getUsername() != "failed") {
            User error = new User();
            error.setRole("failed");
            error.setUsername("failed");
            error.setPassword("failed");
            return error;
        }
        return userService.saveUser(user);
    }

    @ApiOperation(value = "Logs a user in when provided their credentials", response = User.class)
    @PostMapping("/users/login")
    public User checkLogin( @RequestBody User user) {
        if(user.getUsername()==null){ return null; } //basic error checking (is inputs set correctly)
        if(user.getPassword()==null){ return null; }

        for (User searchUser : userService.fetchUserList()) {
            if(user.getUsername().equals(searchUser.getUsername()) && user.getPassword().equals(searchUser.getPassword())) {
                return searchUser;
            }
        }
        User error = new User();
        error.setUsername("denied");error.setRole("denied");error.setPassword("denied");error.setSecret("denied");
        return error;
    }

    // Read operations
    @ApiOperation(value = "Fetches a list of all existing users", response = Iterable.class)
    @GetMapping("/users/all")
    public List<User> fetchUserList() {
        return userService.fetchUserList();
    }

    @ApiOperation(value = "Fetches a specific user by their respective id", response = User.class)
    @GetMapping("/users/id/{id}")
    public User fetchUserById( @PathVariable("id") Long id) {
        for (User user : userService.fetchUserList()) {
            try {
                if (user.getUserId().equals(id))
                    return user;
            }
            catch(Exception e) {}

        }
        User error = new User();
        error.setRole("failed");
        error.setUsername("failed");
        error.setPassword("failed");
        return error;
    }

    @ApiOperation(value = "Fetches a specific user by their username", response = User.class)
    @GetMapping("/users/username/{username}")
    public User fetchUser( @PathVariable("username") String username) {
        for (User user : userService.fetchUserList()) {
            try {
                if (user.getUsername().equals(username))
                    return user;
            }
            catch(Exception e) {}

        }
        User error = new User();
        error.setRole("failed");
        error.setUsername("failed");
        error.setPassword("failed");
        return error;
    }

    @ApiOperation(value = "Fetches the stats of a specific user", response = Stats.class)
    @GetMapping("/users/stats/{id}")
    public Stats fetchStatsById(@PathVariable("id") Long id) {
        Stats fail = new Stats();
        fail.setMatches(-1);
        fail.setOpponents(-1);
        fail.setWins(-1);

        for (User user : userService.fetchUserList()) {
            try {
                if (user.getUserId().equals(id)) {
                    Stats userStats = new Stats();
                    fail.setMatches(user.getMatches());
                    fail.setOpponents(user.getOpponents());
                    fail.setWins(user.getWins());
                    return userStats;
                }
            }
            catch(Exception e) {
                return fail;
            }
        }
        return fail;
    }

    // Returns the user's (of id) elo as a long, -1 if failure occurs
    @ApiOperation(value = "Fetches the ELO of a specific user", response = Long.class)
    @GetMapping("/users/elo/{id}")
    public Long fetchEloById(@PathVariable("id") Long id) {
        if (Objects.isNull(id)) return Long.valueOf(-1);
        try { return userService.fetchUserById(id).getElo(); }
        catch (Exception e) { return Long.valueOf(-1); }
    }

    // Update users whilst checking for role permissions and duplicate usernames
    @ApiOperation(value = "Updates a user's information when provided valid permissions and credentials", response = User.class)
    @PutMapping("/users/update")
    public User updateUser(@RequestBody UserPair updaters) {
        // Create fail-case users
        User error = new User();
        error.setRole("failed");
        error.setUsername("failed");
        error.setPassword("failed");

        User denied = new User();
        denied.setRole("denied");
        denied.setUsername("denied");
        denied.setPassword("denied");

        User taken = new User();
        taken.setRole("taken");
        taken.setUsername("taken");
        taken.setPassword("taken");

        // Check for null users
        if (updaters == null) return error;
        User target = updaters.getTarget();
        User sender = updaters.getSender();
        if (target == null || sender == null) return error;

        // Only update the user if the users exists and the one doing the sender is itself or and admin.
        // Does not allow the user to update their username to an existing username, including sender to
        // their current username as they already own it.
        try {
            if (target.getUserId().equals(sender.getUserId())) {
                if (target.getSecret().equals(sender.getSecret())) {
                    if (fetchUserById(target.getUserId()).getSecret().equals(target.getSecret()) &&
                            fetchUserById(sender.getUserId()).getSecret().equals(sender.getSecret())) {
                        if (target.getUsername() != null && fetchUser(target.getUsername()).getUsername() != "failed") return taken;
                        return userService.updateUser(target, target.getUserId());
                    } else { return error; } // Secrets attempted to be altered
                } else { return error; } // Secrets do not match
            } else if (fetchUserById(sender.getUserId()).getRole().equals("admin")) {
                if (fetchUserById(sender.getUserId()).getSecret().equals(sender.getSecret())) {
                    if (target.getUsername() != null && fetchUser(target.getUsername()).getUsername() != "failed") return taken;
                    return userService.updateUser(target, target.getUserId());
                } else { return error; } // Secrets attempted to be altered
            } else { return denied; } // Non-admin attempts to edit non-self
        }
        catch (Exception e) {
            return error; // Could not find user in argument
        }
    }

    // Update stats
    @ApiOperation(value = "Updates the stats of s specific user", response = User.class)
    @PutMapping("/users/stats/{id}")
    public User updateStatsById(@PathVariable Long id, @RequestBody Stats stats) {
        User error = new User();
        error.setRole("failed");
        error.setUsername("failed");
        error.setPassword("failed");

        User user = userService.updateStats(stats, id);
        if (Objects.nonNull(user)) return user;
        return error;
    }

    // Directly updates an ELO of a user with id, returns the updated user or error user
    @ApiOperation(value = "Manually updates the ELO of a specific user", response = User.class)
    @PutMapping("/users/elo/{id}")
    public User updateEloById(@PathVariable Long id, @RequestBody Long elo) {
        User error = new User();
        error.setRole("failed");
        error.setUsername("failed");
        error.setPassword("failed");

        if (Objects.isNull(id) ||  Objects.isNull(elo)) return error;
        User user;
        try {
            user = userService.fetchUserById(id);
            return userService.updateElo(elo, user.getUserId());
        }
        catch (Exception e) { return error; }
    }

    // Increments/adjusts ELO by increment of user id, returns incremented user or error
    @ApiOperation(value = "Manually increments the ELO of a specific user", response = User.class)
    @PutMapping("/users/adjustelo/{id}")
    public User adjustEloById(@PathVariable Long id, @RequestBody Long increment) {
        User error = new User();
        error.setRole("failed");
        error.setUsername("failed");
        error.setPassword("failed");

        if (Objects.isNull(id) ||  Objects.isNull(increment)) return error;
        User user;
        try {
            user = userService.fetchUserById(id);
            if (Objects.isNull(user.getElo())) user.setElo(Long.valueOf(userService.getStartingElo()));
            return userService.updateElo(user.getElo() + increment, user.getUserId());
        }
        catch (Exception e) { return error; }
    }

    //mute/unmute operation
    @ApiOperation(value = "Mute a User", response = User.class)
    @PutMapping("/users/mute/{id}")
    public User muteUser(@PathVariable Long id){
        return userService.muteUser(id);
    }

    @ApiOperation(value = "unMute a User", response = User.class)
    @PutMapping("/users/unmute/{id}")
    public User unmuteUser(@PathVariable Long id){
        return userService.unmuteUser(id);
    }

    // Delete operation
    @ApiOperation(value = "Deletes a user provided valid permissions and credentials", response = String.class)
    @DeleteMapping("/users/delete/{id}")
    public String deleteUserById(@PathVariable("id") Long userId, @RequestBody User sender) {
        // Check for null or nonexistent users
        if (userId == null || sender == null) return "failed";
        if (sender.getSecret() == null || sender.getUserId() == null) return "failed";
        User target = fetchUserById(userId);
        if (fetchUserById(userId).getRole().equals("failed") || fetchUserById(sender.getUserId()).equals("failed"))
            return "failed";


        // Is the target user the sender user?
        if (target.getUserId().equals(sender.getUserId())) {
            if (target.getSecret().equals(sender.getSecret())) {
                friendshipService.deleteFriendshipsOfUserId(userId);
                userService.deleteUserById(userId);
                return "Deleted Successfully";
            }
            else {
                return "failed"; // Incorrect secrets
            }
        }
        // Is the sender user an admin?
        else if (fetchUserById(sender.getUserId()).getRole().equals("admin")) {
            if (fetchUserById(sender.getUserId()).getSecret().equals(sender.getSecret())) {
                friendshipService.deleteFriendshipsOfUserId(userId);
                userService.deleteUserById(userId);
                return "Deleted Successfully";
            }
            else {
                return "failed"; // Incorrect secrets
            }
        }

        // Sender is neither the target nor admin
        return "denied";
    }
}