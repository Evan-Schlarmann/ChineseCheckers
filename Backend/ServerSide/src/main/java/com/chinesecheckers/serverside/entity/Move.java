//  Java Program to Illustrate Move File

// Importing package module to code fragment
package com.chinesecheckers.serverside.entity;
// Importing required classes

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

public class Move {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @ApiModelProperty(notes = "move id",name="moveId",required=false,value="Auto-generated ID for Move")
    private Long moveId;

    @ManyToOne(cascade = CascadeType.ALL)
    @ApiModelProperty(notes = "game id",name="moveId",required=false,value="Join ID for Game Move is in")
    @JoinColumn(name = "game_id", referencedColumnName = "gameId")
    private Game game;

    @ManyToOne(cascade = CascadeType.ALL)
    @ApiModelProperty(notes = "player id",name="userId",required=false,value="Join ID for Player Move is in")
    @JoinColumn(name="player_id", referencedColumnName = "userId")
    private User player;
    @ApiModelProperty(notes = "xStartIndex",name="xStartIndex",required=true,value="Start Location X index")
    private Integer xStartIndex;
    @ApiModelProperty(notes = "yStartIndex",name="yStartIndex",required=true,value="Start Location Y index")
    private Integer yStartIndex;
    @ApiModelProperty(notes = "xEndIndex",name="xEndIndex",required=true,value="End Location X index")
    private Integer xEndIndex;
    @ApiModelProperty(notes = "yEndIndex",name="yEndIndex",required=true,value="End Location Y index")
    private Integer yEndIndex;

    @ApiModelProperty(notes = "turnCount",name="turnCount",required=true,value="The turn count the move occurred on")
    private Integer turnCount;
}
