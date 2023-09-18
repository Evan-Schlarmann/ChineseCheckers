
// Java Program to Illustrate DepartmentService File

// Importing package to this code fragment
package com.chinesecheckers.serverside.service;

import com.chinesecheckers.serverside.entity.Move;
// Importing required classes
import java.util.List;

// Interface

public interface MoveService {

    // Save operation
    Move saveMove(Move move);

    // Read operation
    List<Move> fetchMoveList();

    // Update operation
    Move updateMove(Move move, Long moveId);
    // Delete operation
    void deleteMoveById(Long moveId);
}
