
// Java Program to Illustrate DepartmentService File

// Importing package to this code fragment
package com.chinesecheckers.serverside.service;

import com.chinesecheckers.serverside.entity.Lobby;
import com.chinesecheckers.serverside.entity.User;

import java.util.List;

// Interface

public interface LobbyService {

    // Save operation
    Lobby saveLobby(Lobby lobby);

    // Read operation
    List<Lobby> fetchLobbyList();

    // Update operation
    Lobby updateLobby(Lobby lobby, Long lobbyId);
    Lobby updatePlayers(Lobby lobby, Long lobbyId);

    // Delete operation
    void deleteLobbyById(Long lobbyId);
    void deleteGameByLobby(Long lobbyId);

    // Utility
    Long maxElo(Lobby lobby);
    Long avgElo(Lobby lobby);
}
