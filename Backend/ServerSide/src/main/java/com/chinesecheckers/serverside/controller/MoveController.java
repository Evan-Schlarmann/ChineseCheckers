
// Java Program to Illustrate MoveController File

// Importing package module to this code

package com.chinesecheckers.serverside.controller;


import com.chinesecheckers.serverside.entity.Lobby;
import com.chinesecheckers.serverside.entity.Move;
import com.chinesecheckers.serverside.service.MoveService;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

//import javax.validation.Valid;
import java.util.List;

// Annotation
@RestController

// Class

public class MoveController {


    // Annotation
    @Autowired
    private MoveService moveService;

    // Save operation
    @ApiOperation(value = "Saves a new Move", response = Move.class)
    @PostMapping("/moves/save")
    public Move saveMove( @RequestBody Move move)
    {
        return moveService.saveMove(move);
    }

    // Read operation
    @ApiOperation(value = "Gets all Movees in the system", response = Iterable.class)
    @GetMapping("/moves/getall")
    public List<Move> fetchMoveList()
    {
        return moveService.fetchMoveList();
    }

    // Update operation
    @ApiOperation(value = "Updates a Move", response = Move.class)
    @PutMapping("/moves/update/{id}")
    public Move updateMove(@RequestBody Move move,  @PathVariable("id") Long moveId) {
        return moveService.updateMove(move, moveId);
    }

    // Delete operation
    @ApiOperation(value = "Deletes a move", response = Move.class)
    @DeleteMapping("/moves/{id}")
    public String deleteMoveById(@PathVariable("id") Long moveId) {
        moveService.deleteMoveById(moveId);
        return "Deleted Successfully";
    }
}