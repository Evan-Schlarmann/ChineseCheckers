//  Java Program to Illustrate Move File

// Importing package module to code fragment
package com.chinesecheckers.serverside.entity;

// Importing required classes

import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.List;

// Annotations
@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder

// Class
@JsonIdentityInfo(generator = ObjectIdGenerators.PropertyGenerator.class, property = "lobbyId")
public class Lobby {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @ApiModelProperty(notes = "ID of the Lobby",name="lobbyId",required=false,value="Auto-generated ID for Lobby")
    private Long lobbyId;
    @ApiModelProperty(notes = "Count of Players in the game",name="playerCount",required=true,value="Count of associated players")
    private int playerCount;
    @ApiModelProperty(notes = "String of Gametype",name="gameType",required=true,value="Competitive, Casual, or Private")
    private String gameType; // Competitive, casual, or private?
    @ApiModelProperty(notes = "lobby status",name="lobbyStatus",required=true,value="Status of the lobby (Pending, Running, Complete)")

    private String lobbyStatus; //Pending, Running, Complete
    @ApiModelProperty(notes = "lobby joinCode",name="joinCode",required=true,value="Public or private join code")
    private String joinCode;

    @ApiModelProperty(notes = "lobby game",name="game",required=true,value="The game tied to a lobby")

    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "game_id", referencedColumnName = "gameId")
    private Game game;

    // Player slots
    @ApiModelProperty(notes = "player1_id",name="player1_id",required=true,value="The player1 id")
    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name="player1_id", referencedColumnName = "userId")
    private User player1;

    @ApiModelProperty(notes = "player2_id",name="player2_id",required=true,value="The player2 id")
    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name="player2_id", referencedColumnName = "userId")
    private User player2;

    @ApiModelProperty(notes = "player3_id",name="player3_id",required=true,value="The player3 id")
    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name="player3_id", referencedColumnName = "userId")
    private User player3;

    @ApiModelProperty(notes = "player4_id",name="player4_id",required=true,value="The player4 id")
    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name="player4_id", referencedColumnName = "userId")
    private User player4;

    @ApiModelProperty(notes = "player5_id",name="player5_id",required=true,value="The player5 id")
    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name="player5_id", referencedColumnName = "userId")
    private User player5;

    @ApiModelProperty(notes = "player6_id",name="player6_id",required=true,value="The player6 id")
    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name="player6_id", referencedColumnName = "userId")
    private User player6;

}
