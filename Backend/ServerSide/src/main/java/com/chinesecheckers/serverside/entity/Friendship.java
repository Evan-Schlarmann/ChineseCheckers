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

// Annotations
@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder

// Class
@JsonIdentityInfo(generator = ObjectIdGenerators.PropertyGenerator.class, property = "friendshipId")
public class Friendship {

    @ApiModelProperty(notes = "ID of the friendship", name = "friendshipId", required = true, value = "A uniquely generated id for each friendship entity")
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long friendshipId;

    @ApiModelProperty(notes = "User requesting the friendship", name = "requester", required = true, value = "User requesting the friendship")
    @ManyToOne(cascade = CascadeType.ALL)//, fetch = FetchType.LAZY)
    @JoinColumn(name="requester_user_id", referencedColumnName = "userId")
    private User requester;
    @ApiModelProperty(notes = "User accepting the friendship", name = "accepter", required = true, value = "User accepting the friendship")
    @ManyToOne(cascade = CascadeType.ALL)//, fetch = FetchType.LAZY)
    @JoinColumn(name="accepter_user_id", referencedColumnName = "userId")
    private User accepter;

    @ApiModelProperty(notes = "Status of the friendship", name = "accepted", required = true, value = "Whether the friend request was accepted or not")
    private Boolean accepted;
}
