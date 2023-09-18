

// Java Program to Illustrate MoveServiceImpl File

// Importing package module to this code

package com.chinesecheckers.serverside.service;

import com.chinesecheckers.serverside.Stats;
import com.chinesecheckers.serverside.entity.User;
import com.chinesecheckers.serverside.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Objects;
import java.util.Random;

// Annotation
@Service

// Class

public class UserServiceImpl implements UserService {

    // Repositories
    @Autowired
    private UserRepository userRepository;

    // Constants
    private final Long STARTING_ELO = Long.valueOf(100);

    // Save operation
    @Override
    public User saveUser(User user) {

        //if they aren't a user or mod, set as user (also cleans input)
        if( !(user.getRole().equals("admin") || user.getRole().equals("moderator")) ){
            user.setRole("user");
        }
        if( user.getPassword().contains("...")){
            return null;
        }
        if( Objects.isNull(user.getMuted()) || user.getMuted() != 1){
            user.setMuted(0);
        }
        Random rand = new Random();
        user.setSecret(String.valueOf(rand.nextInt(Integer.MAX_VALUE)));
        user.setElo(STARTING_ELO);
        return userRepository.save(user);
    }


    // Read operation
    @Override
    public List<User> fetchUserList() {
        return (List<User>)userRepository.findAll();
    }

    @Override
    public User fetchUserById(Long userId) {
        return userRepository.getById(userId);
    }

    @Override
    public User updateStats(Stats stats, Long userId) {
        if (Objects.isNull(stats) || Objects.isNull(userId)) return null;
        User user;
        try { user = userRepository.findById(userId).get(); }
        catch (Exception e) { return null; }

        if (Objects.nonNull(stats.getMatches()))
            user.setMatches(stats.getMatches());
        if (Objects.nonNull(stats.getOpponents()))
            user.setOpponents(stats.getOpponents());
        if (Objects.nonNull(stats.getWins()))
            user.setWins(stats.getWins());

        return userRepository.save(user);
    }

    @Override
    public User updateElo(Long elo, Long userId) {
        if (Objects.isNull(elo) || Objects.isNull(userId)) return null;
        User user;
        try { user = userRepository.findById(userId).get(); }
        catch (Exception e) { return null; }

        user.setElo(elo);

        return userRepository.save(user);
    }

    // Update operation
    @Override
    public User updateUser(User user, Long userId) {

        User userDB = userRepository.findById(userId).get();

        if (Objects.nonNull(user.getUsername()) && !"".equalsIgnoreCase(user.getUsername())) {
            userDB.setUsername(user.getUsername());
        }
        if (Objects.nonNull(user.getPassword()) && !"".equalsIgnoreCase(user.getPassword())) {
            userDB.setPassword(user.getPassword());
        }
        if (Objects.nonNull(user.getRole()) && !"".equalsIgnoreCase(user.getRole())) {
            if((user.getRole()=="admin") || (user.getRole()=="moderator") || (user.getRole()=="user")) {
                userDB.setRole(user.getRole());
            }
        }
        //updating secret not allowed

        return userRepository.save(userDB);
    }


    // Delete operation

    @Override
    public void deleteUserById(Long userId) {
        userRepository.deleteById(userId);
    }

    // Utility
    public Long getStartingElo() { return STARTING_ELO; }

    @Override
    public User muteUser(Long id) {
        User muteUser = fetchUserById(id);
        muteUser.setMuted(1);
        userRepository.save(muteUser);
        return (muteUser);
    }

    @Override
    public User unmuteUser(Long id) {
        User muteUser = fetchUserById(id);
        muteUser.setMuted(0);
        userRepository.save(muteUser);
        return (muteUser);
    }
}
