

// Java Program to Illustrate MoveServiceImpl File

// Importing package module to this code

package com.chinesecheckers.serverside.service;

import com.chinesecheckers.serverside.entity.Game;
import com.chinesecheckers.serverside.entity.Lobby;
import com.chinesecheckers.serverside.entity.Move;
import com.chinesecheckers.serverside.entity.User;
import com.chinesecheckers.serverside.service.MoveService;
import com.chinesecheckers.serverside.service.UserService;
import com.chinesecheckers.serverside.repository.GameRepository;
import com.chinesecheckers.serverside.repository.LobbyRepository;
import com.chinesecheckers.serverside.repository.MoveRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Objects;

// Annotation
@Service

// Class

public class GameServiceImpl implements GameService {

    @Autowired
    private UserService userService;
    @Autowired
    private MoveService moveService;

    @Autowired
    private GameRepository gameRepository;
    @Autowired
    private MoveRepository moveRepository;
    @Autowired
    private LobbyRepository lobbyRepository;

    @Override
    public Game getGame(Long gameId){
        List<Game> allGames = gameRepository.findAll();
        Game curGame = null;
        for(Game game : allGames) {
            if (game.getGameId().equals(gameId)) {
                curGame = game;
            }
        }
        if (curGame == null){
            curGame.setTurnNumber(-1);
            curGame.setPlayerCount(-1);
            curGame.setGameType("failed");
            return curGame;
        }
        return curGame;
    }

    // Save operation
    @Override
    public Game saveGame(Game game) {
        return gameRepository.save(game);
    }

    @Override
    public Game createFromLobby(Lobby lobby){

        Lobby sourcelobby = lobbyRepository.findById(lobby.getLobbyId()).get();

        Game newGame = new Game();
        newGame.setLobby(sourcelobby);//connect lobby to game
        newGame.setGameType(sourcelobby.getGameType());//copy gametype
        newGame.setTurnNumber(0);//set turn number to start (0)
        newGame.setPlayerCount(sourcelobby.getPlayerCount()); //copy playercount

        Game createdGame = gameRepository.save(newGame);//save the newly created game
        sourcelobby.setGame(createdGame);//connect game to lobby
        lobbyRepository.save(sourcelobby);//save updated lobby
        return createdGame;
    }

    // Read operation
    @Override
    public List<Game> fetchGameList() {
        return (List<Game>)
                gameRepository.findAll();
    }

    @Override
    public String makeMove(Move newMove, Long gameId){
        List<Game> curGames = fetchGameList();//search for the game with the correct ID
        Game curGame = null;
        for(Game game : curGames) {
            if (game.getGameId().equals(gameId)){
                curGame = game;
            }
        }
        if (curGame == null){ return "-3"; }

        newMove.setPlayer(userService.fetchUserById(newMove.getPlayer().getUserId()));

        if( newMove.getTurnCount() == curGame.getTurnNumber() ){ //if it is the next move

            //check if game id matches
            if( !curGame.getGameId().equals(newMove.getGame().getGameId()) ){
                return "-2";
            }

            Move savedMove = moveService.saveMove(newMove); //save move
            if(savedMove.getTurnCount().intValue() < 0){ //if it saved correctly
                return savedMove.getTurnCount().toString();
            }
            curGame.setTurnNumber(curGame.getTurnNumber()+1);//increment turn number
            saveGame(curGame);
            return String.valueOf(curGame.getTurnNumber());
        }
        return "-1";
    };

    @Override
    public Move getMove(Long turnNumber, Long gameId){
            List<Move> allMoves = moveRepository.findAll();

            //loop through each move ever made, if it is the right game and turn number, return it
            for (Move eachMove : allMoves){
                if (eachMove.getTurnCount().longValue() == turnNumber) {
                    if (eachMove.getGame().getGameId().longValue() == gameId) {
                        return eachMove;
                    }
                }
            }
            //this is if nothing is found
            Move nothingFound = new Move();
            nothingFound.setTurnCount(-1);
            nothingFound.setXStartIndex(-1);
            nothingFound.setYStartIndex(-1);
            nothingFound.setXEndIndex(-1);
            nothingFound.setYEndIndex(-1);
            return nothingFound;
    };

    // Update operation
    @Override
    public Game updateGame(Game game, Long gameId) {

        Game gameDB = gameRepository.findById(gameId).get();

        if (Objects.nonNull(game.getGameType()) && !"".equalsIgnoreCase(game.getGameType())) {
            gameDB.setGameType(game.getGameType());
        }
        if (Objects.nonNull(game.getPlayerCount())) {
            gameDB.setPlayerCount(game.getPlayerCount());
        }

        return gameRepository.save(gameDB);
    }

    // Delete operation
    @Override
    public void deleteGameById(Long gameId) {
        for (Move move : moveService.fetchMoveList()) {
            if (move.getGame().equals(getGame(gameId)))
                moveService.deleteMoveById(move.getMoveId());
        }
        gameRepository.deleteById(gameId);
    }
}
