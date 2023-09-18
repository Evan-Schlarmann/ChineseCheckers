//  Java Program to Illustrate Move File

// Importing package module to code fragment
package com.chinesecheckers.serverside.entity;
// Importing required classes

import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

// Annotations
@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder

// Class

public class User {

    @ApiModelProperty(notes = "ID of the user", name = "userId", required = true, value = "A uniquely generated id for each user entity")
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long userId;
    @ApiModelProperty(notes = "Role of the user", name = "role", required = true, value = "The role and permissions of a user (default user, moderator, or admin)")
    private String role;
    @ApiModelProperty(notes = "Username of the user", name = "username", required = true, value = "The username of a user")
    private String username;
    @ApiModelProperty(notes = "Password of the user", name = "password", required = true, value = "The password of a user")
    private String password;
    @ApiModelProperty(notes = "Secret of the user", name = "secret", required = true, value = "A uniquely generated secret for each user for security purposes")
    private String secret;

    // Elo
    @ApiModelProperty(notes = "Elo of the user", name = "elo", required = true, value = "An ELO the reflects the skill of a user and is used for matchmaking (starts at 100)")
    private Long elo;

    // Stats
    @ApiModelProperty(notes = "Match count of the user", name = "matches", required = true, value = "The number of matches a user played in")
    private int matches;
    @ApiModelProperty(notes = "Opponents faced by the user", name = "opponents", required = true, value = "The number of opponents a user faced")
    private int opponents;
    @ApiModelProperty(notes = "Wins gained by the user", name = "wins", required = true, value = "Games won by the user")
    private int wins;

    @ApiModelProperty(notes = "chat mute permissions", name = "muted", required = false, value = "Whether the user is chat muted")
    private int muted;
}
