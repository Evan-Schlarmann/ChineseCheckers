
// Java Program to Illustrate DepartmentService File

// Importing package to this code fragment
package com.chinesecheckers.serverside.service;

import com.chinesecheckers.serverside.entity.Move;
import com.chinesecheckers.serverside.entity.Game;
import com.chinesecheckers.serverside.entity.Lobby;

import java.util.List;

// Interface

public interface GameService {

    Game getGame(Long gameId);

    // Save operation
    Game saveGame(Game game);

    Game createFromLobby(Lobby lobby);
    // Read operation
    List<Game> fetchGameList();

    String makeMove(Move newMove, Long gameId);

    Move getMove(Long turnNumber, Long gameId);

    // Update operation
    Game updateGame(Game game, Long gameId);
    // Delete operation
    void deleteGameById(Long gameId);
}
