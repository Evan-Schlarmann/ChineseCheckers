//  Java Program to Illustrate Move File

// Importing package module to code fragment
package com.chinesecheckers.serverside.entity;
// Importing required classes

import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.ObjectIdGenerator;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;

// Annotations
@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder

// Class
@JsonIdentityInfo(generator = ObjectIdGenerators.PropertyGenerator.class, property = "gameId")
public class Game {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @ApiModelProperty(notes = "ID of the Game",name="gameId",required=false,value="Auto-generated ID for Game")
    private Long gameId;
    @ApiModelProperty(notes = "Count of Players in the game",name="playerCount",required=true,value="Count of associated players")
    private int playerCount;
    @ApiModelProperty(notes = "String of Gametype",name="gameType",required=true,value="Competitive, Casual, or Private")
    private String gameType; // Competitive, casual, or private?

    @ApiModelProperty(notes = "Current turn number of the game",name="turnNumber",required=true,value="0-indexed")
    private int turnNumber;
    @ApiModelProperty(notes = "The Lobby the game is attached to",name="lobby",required=true,value="The Lobby the game is attached to")
    @OneToOne(mappedBy = "game")
    private Lobby lobby;

}
