
// Java Program to Illustrate GameController File

// Importing package module to this code

package com.chinesecheckers.serverside.controller;

import com.chinesecheckers.serverside.entity.Move;
import com.chinesecheckers.serverside.entity.Game;
import com.chinesecheckers.serverside.entity.Lobby;
import com.chinesecheckers.serverside.repository.LobbyRepository;
import com.chinesecheckers.serverside.service.MoveService;
import com.chinesecheckers.serverside.service.GameService;
import com.chinesecheckers.serverside.service.LobbyService;
import com.chinesecheckers.serverside.service.LobbyServiceImpl;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.Api;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Api(value = "GameController",description = "Controller for Game Activities?!")
// Annotation
@RestController

// Class

public class GameController {


    // Annotation
    @Autowired
    private GameService gameService;

    @Autowired
    private LobbyService lobbyService;

    @Autowired
    private MoveService moveService;

    // Save operation
    @ApiOperation(value = "Saves a new game", response = Game.class)
    @PostMapping("/games")
    public Game saveGame( @RequestBody Game game)
    {
        return gameService.saveGame(game);
    }

    // Read operation
    @ApiOperation(value = "Get all games in the system", response = Iterable.class)
    @GetMapping("/games")
    public List<Game> fetchGameList() {
        return gameService.fetchGameList();
    }

    @ApiOperation(value = "Creates a game from the lobby in the body", response = Game.class)
    @PostMapping("/games/createfromlobby")
    public Game createFromLobby(@RequestBody Lobby lobby) {
        for (Lobby lobbyDB : lobbyService.fetchLobbyList()) {
            if (lobbyDB.getLobbyId().equals(lobby.getLobbyId())) {
                lobbyDB.setLobbyStatus("Running");
            }
        }
        return gameService.createFromLobby(lobby);
    }

    @ApiOperation(value = "Makes a move in a game", response = String.class)
    @PostMapping("/games/{id}/makeMove")
    public String makeGameMove(@RequestBody Move newMove, @PathVariable("id") Long gameId){
        return gameService.makeMove(newMove, gameId);
    }

    @ApiOperation(value = "Retrieves a specific move", response = Move.class)
    @GetMapping("/games/{id}/getMove/{turnNumber}")
    public Move getGameMove(@PathVariable("turnNumber") Long turnNumber, @PathVariable("id") Long gameId){
        return gameService.getMove(turnNumber, gameId);
    }

    @ApiOperation(value = "Retrieves a game by ID", response = Game.class)
    // Get Game operation
    @GetMapping("/games/{id}")
    public Game retreiveGame(@PathVariable("id") Long gameId) {
        return gameService.getGame(gameId);
    }

    // Update operation
    @ApiOperation(value = "Updates a game by ID", response = Game.class)
    @PutMapping("/games/{id}")
    public Game updateGame(@RequestBody Game game,  @PathVariable("id") Long gameId) {
        return gameService.updateGame(game, gameId);
    }

    @ApiOperation(value = "Retrieves the turn number of a game", response = String.class)
    @GetMapping("/games/{id}/getNextTurn")
    public String getNextTurn(@PathVariable("id") Long gameId) {
        return String.valueOf(gameService.fetchGameList().get(Math.toIntExact(gameId)).getTurnNumber()); //get the turn number from the game with the ID in the URL
    }

    // Delete operation
    @ApiOperation(value = "Deletes a Game by ID", response = String.class)
    @DeleteMapping("/games/{id}")
    public String deleteGameById(@PathVariable("id") Long gameId) {
        gameService.deleteGameById(gameId);
        return "Deleted Successfully";
    }

    @ApiOperation(value = "Ends game by id", response = String.class)
    @DeleteMapping("/games/end/{id}")
    public String endGameById(@PathVariable("id") Long gameId) {
        if (gameId == null) return "failed";

        Game endingGame = null;
        for (Game game : fetchGameList()) {
            if (game.getGameId().equals(gameId)) {
                endingGame = game;
                break;
            }
        }
        if (endingGame == null) return "failed";

        Lobby lobby = endingGame.getLobby();
        lobby.setGame(null);
        lobby.setLobbyStatus("Pending");
        lobbyService.updateLobby(lobby, lobby.getLobbyId());
        gameService.deleteGameById(gameId);

        return "Delete Successfully";

    }
}