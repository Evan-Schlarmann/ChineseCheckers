

// Java Program to Illustrate MoveServiceImpl File

// Importing package module to this code

package com.chinesecheckers.serverside.service;

import com.chinesecheckers.serverside.entity.Lobby;
import com.chinesecheckers.serverside.entity.Move;
import com.chinesecheckers.serverside.entity.User;
import com.chinesecheckers.serverside.entity.Game;
import com.chinesecheckers.serverside.repository.MoveRepository;
import com.chinesecheckers.serverside.repository.UserRepository;
import com.chinesecheckers.serverside.repository.GameRepository;

        // Importing required classes

import java.util.List;
import java.util.Objects;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

// Annotation
@Service

// Class

public class MoveServiceImpl implements MoveService {


    @Autowired
    private MoveRepository moveRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private GameRepository gameRepository;

    // Save operation
    @Override
    public Move saveMove(Move move)
    {
        Move saveMove = new Move();

        //legal moves are %
        // %#%##
        // #%%##
        // %%@%%
        // ##%%#
        // ##%#%
        int deltaX = move.getXEndIndex().intValue() - move.getXStartIndex().intValue();
        int deltaY = move.getYEndIndex().intValue() - move.getYStartIndex().intValue();
        //check if it's a legal move
        if(deltaX == -1 && deltaY == 1){}//top left
        else if(deltaX == 0 && deltaY ==1){}//top
        else if(deltaX == -1 && deltaY == 0){}//left
        else if(deltaX == 1 && deltaY == 0){}//right
        else if(deltaX == 0 && deltaY == -1){}//down
        else if(deltaX == 1 && deltaY == -1){}//dwn right
        else if(deltaX == -2 && deltaY == 2){}//jump up left
        else if(deltaX == 0 && deltaY == 2){}//jump up
        else if(deltaX == -2 && deltaY == 0){}//jump left
        else if(deltaX == 2 && deltaY == 0){}//jump right
        else if(deltaX == 0 && deltaY == -2){}//jump down
        else if(deltaX == 2 && deltaY == -2){}//jump down right
        else{
            //not legal move
            //saveMove.setTurnCount(-11);
            //return saveMove;
            //disabled as frontend now does move checking
        }

        //check for move inside the board
        //will add code here later

        User player = userRepository.findById(move.getPlayer().getUserId()).get();
        Game sourcegame = gameRepository.findById(move.getGame().getGameId()).get();

        //check to see if the player moved on their turn
        int numPlayers = sourcegame.getPlayerCount();
        int curPlayer = 0;
        User players[] = new User[6];
        if(sourcegame.getLobby().getPlayer1() != null){ players[curPlayer]=sourcegame.getLobby().getPlayer1(); curPlayer++;}
        if(sourcegame.getLobby().getPlayer2() != null){ players[curPlayer]=sourcegame.getLobby().getPlayer2(); curPlayer++;}
        if(sourcegame.getLobby().getPlayer3() != null){ players[curPlayer]=sourcegame.getLobby().getPlayer3(); curPlayer++;}
        if(sourcegame.getLobby().getPlayer4() != null){ players[curPlayer]=sourcegame.getLobby().getPlayer4(); curPlayer++;}
        if(sourcegame.getLobby().getPlayer5() != null){ players[curPlayer]=sourcegame.getLobby().getPlayer5(); curPlayer++;}
        if(sourcegame.getLobby().getPlayer6() != null){ players[curPlayer]=sourcegame.getLobby().getPlayer6(); curPlayer++;}

        curPlayer = sourcegame.getTurnNumber() % numPlayers; //calculate what # player should be taking the turn

        if(!players[curPlayer].getUserId().equals(player.getUserId())) {//if not equal
            saveMove.setTurnCount(-12);
            return saveMove;
        }

        saveMove.setPlayer(player);
        saveMove.setGame(sourcegame);
        saveMove.setXStartIndex(move.getXStartIndex());
        saveMove.setYStartIndex(move.getYStartIndex());
        saveMove.setXEndIndex(move.getXEndIndex());
        saveMove.setYEndIndex(move.getYEndIndex());
        saveMove.setTurnCount(move.getTurnCount());

        return moveRepository.save(saveMove);

    }


    // Read operation
    @Override
    public List<Move> fetchMoveList() {
        return (List<Move>)moveRepository.findAll();
    }

    // Update operation
    @Override
    public Move updateMove(Move move, Long moveId) {

        Move moveDB = moveRepository.findById(moveId).get();

        //Update game if provided
        if (Objects.nonNull(move.getGame()) && Objects.nonNull(move.getGame().getGameId())) {
            Game curGame = gameRepository.findById(move.getGame().getGameId()).get();
            moveDB.setGame(curGame);
        }

        //update player if provided
        if (Objects.nonNull(move.getPlayer()) && Objects.nonNull(move.getPlayer().getUserId())) {
            User player = userRepository.findById(move.getPlayer().getUserId()).get();
            moveDB.setPlayer(player);
        }

        //set x start
        if (Objects.nonNull(move.getXStartIndex())) { moveDB.setXStartIndex(move.getXStartIndex()); }

        //set y start
        if (Objects.nonNull(move.getYStartIndex())) { moveDB.setYStartIndex(move.getYStartIndex()); }

        //set x end
        if (Objects.nonNull(move.getXEndIndex())) { moveDB.setXStartIndex(move.getXEndIndex()); }

        //set y end
        if (Objects.nonNull(move.getYEndIndex())) { moveDB.setYStartIndex(move.getYEndIndex()); }

        return moveRepository.save(moveDB);
    }


    // Delete operation

    @Override
    public void deleteMoveById(Long moveId)
    {
        moveRepository.deleteById(moveId);
    }
}
