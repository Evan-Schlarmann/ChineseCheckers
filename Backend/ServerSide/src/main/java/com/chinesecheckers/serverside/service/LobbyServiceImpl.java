

// Java Program to Illustrate MoveServiceImpl File

// Importing package module to this code

package com.chinesecheckers.serverside.service;

import com.chinesecheckers.serverside.entity.Lobby;
import com.chinesecheckers.serverside.entity.User;
import com.chinesecheckers.serverside.repository.GameRepository;
import com.chinesecheckers.serverside.repository.LobbyRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Objects;

// Annotation
@Service

// Class

public class LobbyServiceImpl implements LobbyService {


    @Autowired
    private LobbyRepository lobbyRepository;

    @Autowired
    private GameRepository gameRepository;

    @Autowired
    private UserService userService;


    // Save operation
    @Override
    public Lobby saveLobby(Lobby lobby) {
        return lobbyRepository.save(lobby);
    }


    // Read operation
    @Override
    public List<Lobby> fetchLobbyList() {
        return (List<Lobby>)
                lobbyRepository.findAll();
    }

    // Update operation
    @Override
    public Lobby updateLobby(Lobby lobby, Long lobbyId) {

        Lobby lobbyDB = lobbyRepository.findById(lobbyId).get();

        if (Objects.nonNull(lobby.getJoinCode()) && !"".equalsIgnoreCase(lobby.getJoinCode())) {
            lobbyDB.setJoinCode(lobby.getJoinCode());
        }
        if (Objects.nonNull(lobby.getGameType()) && !"".equalsIgnoreCase(lobby.getGameType())) {
            if(lobby.getGameType() != "private") {
                if(lobby.getGameType() != "competitive") {
                    lobbyDB.setGameType("casual");
                } else {
                    lobbyDB.setGameType("competitive");
                }
            } else {
                lobbyDB.setGameType("private");
            }
        }
        if (Objects.nonNull(lobby.getLobbyStatus()) && !"".equalsIgnoreCase(lobby.getLobbyStatus())) {
            lobbyDB.setLobbyStatus(lobby.getLobbyStatus());
        }
        if (Objects.nonNull(lobby.getPlayerCount())) {
            lobbyDB.setPlayerCount(lobby.getPlayerCount());
        }

        return lobbyRepository.save(lobbyDB);
    }

    public Lobby updatePlayers(Lobby lobby, Long lobbyId) {

        Lobby lobbyDB = lobbyRepository.findById(lobbyId).get();

        if (Objects.nonNull(lobby.getPlayer1()) && !"".equals(lobby.getPlayer1())) {
            lobbyDB.setPlayer1(lobby.getPlayer1());
        }
        if (Objects.nonNull(lobby.getPlayer2()) && !"".equals(lobby.getPlayer2())) {
            lobbyDB.setPlayer2(lobby.getPlayer2());
        }
        if (Objects.nonNull(lobby.getPlayer3()) && !"".equals(lobby.getPlayer3())) {
            lobbyDB.setPlayer3(lobby.getPlayer3());
        }
        if (Objects.nonNull(lobby.getPlayer4()) && !"".equals(lobby.getPlayer4())) {
            lobbyDB.setPlayer4(lobby.getPlayer4());
        }
        if (Objects.nonNull(lobby.getPlayer5()) && !"".equals(lobby.getPlayer5())) {
            lobbyDB.setPlayer5(lobby.getPlayer5());
        }
        if (Objects.nonNull(lobby.getPlayer6()) && !"".equals(lobby.getPlayer6())) {
            lobbyDB.setPlayer6(lobby.getPlayer6());
        }

        return lobbyRepository.save(lobbyDB);
    }

    // Delete operation

    @Override
    public void deleteLobbyById(Long lobbyId) {
        lobbyRepository.deleteById(lobbyId);
    }

    @Override
    public void deleteGameByLobby(Long lobbyId)  {
        Long gameId = lobbyRepository.findById(lobbyId).get().getGame().getGameId();
        gameRepository.deleteById(gameId);
    }

    // Utility
    @Override
    public Long maxElo(Lobby lobby) {
        Long max = Long.valueOf(-1);
        if (Objects.nonNull(lobby.getPlayer1()))
            max = lobby.getPlayer1().getElo();
        if (Objects.nonNull(lobby.getPlayer2()))
            max = lobby.getPlayer2().getElo().longValue() > max.longValue() ? lobby.getPlayer2().getElo() : max;
        if (Objects.nonNull(lobby.getPlayer3()))
            max = lobby.getPlayer3().getElo().longValue() > max.longValue() ? lobby.getPlayer3().getElo() : max;
        if (Objects.nonNull(lobby.getPlayer4()))
            max = lobby.getPlayer4().getElo().longValue() > max.longValue() ? lobby.getPlayer4().getElo() : max;
        if (Objects.nonNull(lobby.getPlayer5()))
            max = lobby.getPlayer5().getElo().longValue() > max.longValue() ? lobby.getPlayer5().getElo() : max;
        if (Objects.nonNull(lobby.getPlayer6()))
            max = lobby.getPlayer6().getElo().longValue() >  max.longValue() ? lobby.getPlayer6().getElo() : max;

        return max;
    }

    @Override
    public Long avgElo(Lobby lobby) {
        Long avg = Long.valueOf(-1);
        if (Objects.nonNull(lobby.getPlayer1()))
            avg += lobby.getPlayer1().getElo();
        if (Objects.nonNull(lobby.getPlayer2()))
            avg += lobby.getPlayer2().getElo().longValue();
        if (Objects.nonNull(lobby.getPlayer3()))
            avg += lobby.getPlayer3().getElo().longValue();
        if (Objects.nonNull(lobby.getPlayer4()))
            avg += lobby.getPlayer4().getElo().longValue();
        if (Objects.nonNull(lobby.getPlayer5()))
            avg += lobby.getPlayer5().getElo().longValue();
        if (Objects.nonNull(lobby.getPlayer6()))
            avg += lobby.getPlayer6().getElo().longValue();

        return avg / lobby.getPlayerCount();
    }

}

