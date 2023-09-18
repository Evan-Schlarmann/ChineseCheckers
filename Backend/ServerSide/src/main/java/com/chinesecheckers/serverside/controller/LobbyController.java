
// Java Program to Illustrate LobbyController File

// Importing package module to this code

package com.chinesecheckers.serverside.controller;


import com.chinesecheckers.serverside.entity.Game;
import com.chinesecheckers.serverside.repository.UserRepository;
import com.chinesecheckers.serverside.service.GameService;
import com.chinesecheckers.serverside.service.UserService;
import com.chinesecheckers.serverside.entity.Lobby;
import com.chinesecheckers.serverside.entity.User;
import com.chinesecheckers.serverside.service.LobbyService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

import java.util.Objects;
import java.util.Random;

import static java.lang.Math.abs;

// Annotation
@Api(value = "LobbyController",description = "Controller for Lobby Activities")
@RestController

// Class

public class LobbyController {


    // Annotation
    @Autowired
    private LobbyService lobbyService;

    @Autowired
    private GameService gameService;

    @Autowired
    private UserService userService;

    @Autowired
    private UserRepository userRepository;

    // Save operation
    @ApiOperation(value = "Saves a new lobby", response = Lobby.class)
    @PostMapping("/lobbys")
    public Lobby saveLobby( @RequestBody Lobby lobby)
    {
        return lobbyService.saveLobby(lobby);
    }

    // Join Competitive
    @ApiOperation(value = "Joins a competitive lobby", response = Lobby.class)
    @PostMapping("/lobbys/join-competitive")
    public Lobby joinCompetitive( @RequestBody User userJoiningRaw) {
        // Init best lobby to join and actual user
        User userJoining = userRepository.findById(userJoiningRaw.getUserId()).get();
        Lobby bestLobby = null;

        // Search through lobbies
        for (Lobby lobby : lobbyService.fetchLobbyList()) {
            // Lobby fits criteria
            if (lobby.getLobbyStatus() != null && lobby.getLobbyStatus().equals("Pending") && lobby.getGameType().equals("competitive")) {//is waiting for more players
                // Lobby is not full
                if (numPlayers(lobby) < lobby.getPlayerCount()) {
                    // Lobby is the first possible option else is a candidate
                    if (Objects.isNull(bestLobby)) {
                        bestLobby = lobby;
                    }
                    else {
                        long bestLobbyElo = lobbyService.maxElo(bestLobby).longValue();
                        long currLobbyElo = lobbyService.maxElo(lobby).longValue();
                        if (Objects.isNull(userJoining.getElo())) userJoining.setElo(userService.getStartingElo());
                        // Compare maximum Elos of each option: if closer to own elo, prefer new lobby
                        if (abs(bestLobbyElo - userJoining.getElo().longValue()) > abs(currLobbyElo - userJoining.getElo().longValue())) {
                            bestLobby = lobby;
                        }
                    }
                }
            }
        }

        // A lobby was found to join
        if(Objects.nonNull(bestLobby)) {
            System.out.println("numPlayers: "+String.valueOf(numPlayers(bestLobby)));
            System.out.println("playerCount: "+String.valueOf(bestLobby.getPlayerCount()));
            if(bestLobby.getPlayer1() == null){ bestLobby.setPlayer1(userJoining);}
            else if(bestLobby.getPlayer2() == null){ bestLobby.setPlayer2(userJoining);}
            else if(bestLobby.getPlayer3() == null){ bestLobby.setPlayer3(userJoining);}
            else if(bestLobby.getPlayer4() == null){ bestLobby.setPlayer4(userJoining);}
            else if(bestLobby.getPlayer5() == null){ bestLobby.setPlayer5(userJoining);}
            else if(bestLobby.getPlayer6() == null){ bestLobby.setPlayer6(userJoining);}

            updatePlayers(bestLobby, bestLobby.getLobbyId());
            return bestLobby;
        }else{
            System.out.println("No open lobbies");
        }

        // Create a new lobby
        Random rand = new Random();

        Lobby newLobby = new Lobby();
        newLobby.setLobbyStatus("Pending");

        newLobby.setPlayer1(userRepository.findById(userJoining.getUserId()).get());
        newLobby.setPlayerCount(2);
        newLobby.setJoinCode(String.valueOf(100000+rand.nextInt(899999)));
        newLobby.setGameType("competitive");

        saveLobby(newLobby);
        return newLobby;
    }

    // Join Casual
    @ApiOperation(value = "Joins a Casual Lobby", response = Lobby.class)
    @PostMapping("/lobbys/join-casual")
    public Lobby joinCasual( @RequestBody User userJoiningRaw) {
        User userJoining = userRepository.findById(userJoiningRaw.getUserId()).get();

        for (Lobby lobby : lobbyService.fetchLobbyList()) {

            if(lobby.getLobbyStatus()!= null && lobby.getLobbyStatus().equals("Pending") && lobby.getGameType().equals("casual")) {//is waiting for more players
                System.out.println("numPlayers: "+String.valueOf(numPlayers(lobby)));
                System.out.println("playerCount: "+String.valueOf(lobby.getPlayerCount()));
                if(numPlayers(lobby) < lobby.getPlayerCount()){ //not enough players in the lobby

                    if(lobby.getPlayer1() == null){ lobby.setPlayer1(userJoining);}
                    else if(lobby.getPlayer2() == null){ lobby.setPlayer2(userJoining);}
                    else if(lobby.getPlayer3() == null){ lobby.setPlayer3(userJoining);}
                    else if(lobby.getPlayer4() == null){ lobby.setPlayer4(userJoining);}
                    else if(lobby.getPlayer5() == null){ lobby.setPlayer5(userJoining);}
                    else if(lobby.getPlayer6() == null){ lobby.setPlayer6(userJoining);}

                    updatePlayers(lobby, lobby.getLobbyId());
                    return lobby;
                }else{
                    System.out.println("Lobby full");
                }
            }
        }
        Random rand = new Random();

        Lobby newLobby = new Lobby();
        newLobby.setLobbyStatus("Pending");

        newLobby.setPlayer1(userRepository.findById(userJoining.getUserId()).get());
        newLobby.setPlayerCount(2);
        newLobby.setJoinCode(String.valueOf(100000+rand.nextInt(899999)));
        newLobby.setGameType("casual");

        saveLobby(newLobby);
        return newLobby;
    }

    @ApiOperation(value = "Joins a lobby by joincode", response = Lobby.class)
    @PostMapping("/lobbys/join/{joincode}")
    public Lobby joinPrivateLobby(@PathVariable String joincode, @RequestBody User userJoiningRaw) {
        User userJoining = userRepository.findById(userJoiningRaw.getUserId()).get();

        for (Lobby lobby : lobbyService.fetchLobbyList()) {

            if(lobby.getLobbyStatus()!= null && lobby.getLobbyStatus().equals("Pending") && lobby.getJoinCode().equals(joincode)){//is waiting for more players
                if(numPlayers(lobby) < lobby.getPlayerCount()){ //not enough players in the lobby

                    if(lobby.getPlayer1() == null){ lobby.setPlayer1(userJoining);}
                    else if(lobby.getPlayer2() == null){ lobby.setPlayer2(userJoining);}
                    else if(lobby.getPlayer3() == null){ lobby.setPlayer3(userJoining);}
                    else if(lobby.getPlayer4() == null){ lobby.setPlayer4(userJoining);}
                    else if(lobby.getPlayer5() == null){ lobby.setPlayer5(userJoining);}
                    else if(lobby.getPlayer6() == null){ lobby.setPlayer6(userJoining);}

                    updatePlayers(lobby, lobby.getLobbyId());
                    return lobby;
                }else{
                    System.out.println("Lobby full");
                    Lobby failedLobby = new Lobby();
                    failedLobby.setLobbyStatus("failed");
                    failedLobby.setJoinCode("failed");

                    return failedLobby;
                }
            }
        }
        Lobby failedLobby = new Lobby();
        failedLobby.setLobbyStatus("failed");
        failedLobby.setJoinCode("failed");

        return failedLobby;
    }

    @ApiOperation(value = "Creates a new private lobby", response = Lobby.class)
    @PostMapping("/lobbys/newprivate")
    public Lobby newPrivateLobby( @RequestBody User userJoiningRaw ) {
        User userJoining = userRepository.findById(userJoiningRaw.getUserId()).get();

        Random rand = new Random();

        Lobby newLobby = new Lobby();
        newLobby.setLobbyStatus("Pending");

        newLobby.setPlayer1(userRepository.findById(userJoining.getUserId()).get());
        newLobby.setPlayerCount(2);
        newLobby.setJoinCode(String.valueOf(100000 + rand.nextInt(899999)));
        newLobby.setGameType("private");

        saveLobby(newLobby);
        return newLobby;
    }

    // Leave
    @ApiOperation(value = "Leaves a lobby", response = Lobby.class)
    @PostMapping("/lobbys/leave")
    public Lobby leaveLobby( @RequestBody User userLeavingRaw) {
        try {
            User userLeaving = userRepository.findById(userLeavingRaw.getUserId()).get();

            boolean update = false;
            for (Lobby lobby : lobbyService.fetchLobbyList()) {
                if(lobby.getLobbyStatus()!= null) {

                    if (lobby.getPlayer1() != null && lobby.getPlayer1().equals(userLeaving)) { lobby.setPlayer1(null); update = true; }
                    else if (lobby.getPlayer2() != null && lobby.getPlayer2().equals(userLeaving)) { lobby.setPlayer2(null); update = true; }
                    else if (lobby.getPlayer3() != null && lobby.getPlayer3().equals(userLeaving)) { lobby.setPlayer3(null); update = true; }
                    else if (lobby.getPlayer4() != null && lobby.getPlayer4().equals(userLeaving)) { lobby.setPlayer4(null); update = true; }
                    else if (lobby.getPlayer5() != null && lobby.getPlayer5().equals(userLeaving)) { lobby.setPlayer5(null); update = true; }
                    else if (lobby.getPlayer6() != null && lobby.getPlayer6().equals(userLeaving)) { lobby.setPlayer6(null); update = true; }

                    if (update) {
                        Lobby newLobby = updatePlayers(lobby, lobby.getLobbyId());

                        if (newLobby.getLobbyStatus().equals("Running") && newLobby.getGame() != null) {
                            Game game = newLobby.getGame();
                            game.getLobby().setGame(null);
                            game.getLobby().setLobbyStatus("Pending");
                            gameService.deleteGameById(game.getGameId());
                        }

                        return newLobby;
                    }

                }
            }
        }
        catch (Exception e) {
            e.printStackTrace();
        }

        Lobby failedLobby = new Lobby();
        failedLobby.setLobbyStatus("failed");
        failedLobby.setJoinCode("failed");

        return failedLobby;
    }

    // Kick
    @ApiOperation(value = "Kicks a user from a lobby", response = Lobby.class)
    @PostMapping("/lobbys/kick/{userKickedRaw}")
    public Lobby kickFromLobby ( @PathVariable String userKickedRaw, @RequestBody User userKickingRaw) {
        try {
            User userKicked = userRepository.findById(Long.parseLong(userKickedRaw)).get();
            User userKicking = userRepository.findById(userKickingRaw.getUserId()).get();
            if (userKicking.getRole().equalsIgnoreCase("admin") || userKicking.getRole().equalsIgnoreCase("moderator")) {

                boolean update = false;
                for (Lobby lobby : lobbyService.fetchLobbyList()) {
                    if (lobby.getLobbyStatus() != null) {

                        if (lobby.getPlayer1() != null && lobby.getPlayer1().equals(userKicked)) { lobby.setPlayer1(null); update = true; }
                        else if (lobby.getPlayer2() != null && lobby.getPlayer2().equals(userKicked)) { lobby.setPlayer2(null); update = true; }
                        else if (lobby.getPlayer3() != null && lobby.getPlayer3().equals(userKicked)) { lobby.setPlayer3(null); update = true; }
                        else if (lobby.getPlayer4() != null && lobby.getPlayer4().equals(userKicked)) { lobby.setPlayer4(null); update = true; }
                        else if (lobby.getPlayer5() != null && lobby.getPlayer5().equals(userKicked)) { lobby.setPlayer5(null); update = true; }
                        else if (lobby.getPlayer6() != null && lobby.getPlayer6().equals(userKicked)) { lobby.setPlayer6(null); update = true; }

                        if (update) {
                            Lobby newLobby = updatePlayers(lobby, lobby.getLobbyId());

                            if (newLobby.getLobbyStatus().equals("Running") && newLobby.getGame() != null) {
                                Game game = newLobby.getGame();
                                game.getLobby().setGame(null);
                                game.getLobby().setLobbyStatus("Pending");
                                gameService.deleteGameById(game.getGameId());
                            }

                            return newLobby;
                        }

                    }
                }

                Lobby failedLobby = new Lobby();
                failedLobby.setLobbyStatus("failed");
                failedLobby.setJoinCode("failed");

                return failedLobby;
            }

            Lobby deniedLobby = new Lobby();
            deniedLobby.setLobbyStatus("denied");
            deniedLobby.setJoinCode("denied");

            return deniedLobby;
        }
        catch (Exception e) {

            Lobby failedLobby = new Lobby();
            failedLobby.setLobbyStatus("failed");
            failedLobby.setJoinCode("failed");

            return failedLobby;

        }
    }

    // Win
    @ApiOperation(value = "Lets a user win the game b y ending the game and updating stats", response = Lobby.class)
    @PostMapping("/lobbys/{lobbyId}/win-game/{userId}")
    public String winGame(@PathVariable("lobbyId") Long lobbyId, @PathVariable("userId") Long userId, @RequestBody Lobby useless ) {
        // Safe init
        if (Objects.isNull(lobbyId) || Objects.isNull(userId)) return null;
        User winner = null;
        Lobby lobbyDB = null;
        try {
            winner = userService.fetchUserById(userId);
            lobbyDB = fetchLobby(lobbyId);
        }
        catch (Exception e) {
            System.out.println("User or lobby does not exist");
            return null;
        }
        if (Objects.isNull(winner) || lobbyDB.getLobbyStatus().equals("failed")) return null;

        // Check that user is in the lobby
        boolean validUser = false;
        for (User player : fetchPlayersInLobby(lobbyId)) {
            if (player.getUserId().equals(winner.getUserId())) {
                validUser = true;
                break;
            }
        }
        if (!validUser) {
            System.out.println("The user is not in the lobby");
            return null;
        }

        // Check and adjust lobby status
        if (!lobbyDB.getLobbyStatus().equals("Running")) {
            System.out.println("The lobby is not currently in a game");
            return null;
        }
        lobbyDB.setLobbyStatus("Pending");

        // Update ELOs and Stats
        for (User player : fetchPlayersInLobby(lobbyId)) {
            if (Objects.nonNull(player)) {
                player.setMatches(player.getMatches() + 1);
                player.setOpponents(player.getOpponents() + lobbyDB.getPlayerCount() - 1);
                if (player.getUserId().equals(winner.getUserId())) {
                    if (lobbyDB.getGameType().equals("competitive")) {
                        winner.setElo(winner.getElo() + 10);
                    }
                    winner.setWins(winner.getWins() + 1);
                } else {
                    if (lobbyDB.getGameType().equals("competitive")) {
                        player.setElo(player.getElo() - 7);
                    }
                }
            }
        }


        // End and remove the game
        try {
            Long gameId = lobbyDB.getGame().getGameId();
            lobbyDB.setGame(null);
            gameService.deleteGameById(gameId);
        }
        catch (Exception e) {
            System.out.println("Error");
        }

        return "Success";
    }

    // Read operation
    @ApiOperation(value = "Retrieves all lobbys", response = Iterable.class)
    @GetMapping("/lobbys")
    public List<Lobby> fetchLobbyList() {
        return lobbyService.fetchLobbyList();
    }

    // Get specific lobby
    @ApiOperation(value = "Retrieves a specific lobby", response = Lobby.class)
    @GetMapping("/lobbys/{id}")
    public Lobby fetchLobby( @PathVariable("id") Long id) {
        for (Lobby lobby : lobbyService.fetchLobbyList()) {
            if (lobby.getLobbyId().equals(id))
                return lobby;
        }
        Lobby failedLobby = new Lobby();
        failedLobby.setLobbyStatus("failed");
        failedLobby.setJoinCode("failed");

        return failedLobby;
    }

    // Get list of players in lobby
    @ApiOperation(value = "Gets a list of all players ina a lobby lobby", response = Iterable.class)
    @GetMapping("/lobbys/players/{id}")
    public List<User> fetchPlayersInLobby(@PathVariable("id") Long lobbyId) {
        Lobby lobby = fetchLobby(lobbyId);
        List<User> players = new ArrayList<>();
        players.add(lobby.getPlayer1());
        players.add(lobby.getPlayer2());
        players.add(lobby.getPlayer3());
        players.add(lobby.getPlayer4());
        players.add(lobby.getPlayer5());
        players.add(lobby.getPlayer6());
        return players;
    }

    // Update operation
    @ApiOperation(value = "Updates a lobby", response = Lobby.class)
    @PutMapping("/lobbys/{id}")
    public Lobby updateLobby(@RequestBody Lobby lobby,  @PathVariable("id") Long lobbyId) {
        return lobbyService.updateLobby(lobby, lobbyId);
    }

    // Update players
    @ApiOperation(value = "Updates a specific lobby", response = Lobby.class)
    public Lobby updatePlayers(@RequestBody Lobby lobby,  @PathVariable("id") Long lobbyId) {
        return lobbyService.updatePlayers(lobby, lobbyId);
    }

    // Delete operation
    @ApiOperation(value = "Deletes a specific lobby by ID", response = String.class)
    @DeleteMapping("/lobbys/{id}")
    public String deleteLobbyById(@PathVariable("id") Long lobbyId) {
        lobbyService.deleteLobbyById(lobbyId);
        return "Deleted Successfully";
    }

    // Players in lobby
    public int numPlayers(Lobby lobby){
        int players = 0;
        if(lobby.getPlayer1() != null){ players++; }
        if(lobby.getPlayer2() != null){ players++; }
        if(lobby.getPlayer3() != null){ players++; }
        if(lobby.getPlayer4() != null){ players++; }
        if(lobby.getPlayer5() != null){ players++; }
        if(lobby.getPlayer6() != null){ players++; }
        return players;
    }
}