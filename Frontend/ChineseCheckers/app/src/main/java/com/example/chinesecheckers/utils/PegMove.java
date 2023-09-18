package com.example.chinesecheckers.utils;

import com.example.chinesecheckers.models.BoardModel;

import java.util.ArrayList;
import java.util.List;

/**
 * Represents a given arbitrary move, moving a peg from one space to another
 */
public class PegMove {
    int player;
    Coordinate start;
    Coordinate end;

    int turnNumber;



    /**
    move type used to test moves / legacy constructor
     */
    public PegMove(int player, Coordinate start, Coordinate end){
        this.player = player;
        this.start = start;
        this.end = end;
    }

    /**
     * Used to establish an official move sent to the server
     * @param player
     * @param start
     * @param end
     * @param turnNum
     */
    public PegMove(int player, Coordinate start, Coordinate end, int turnNum){
        this.player = player;
        this.start = start;
        this.end = end;
        this.turnNumber = turnNum;
    }

    /**
     * @return - starting or origin coordinate of the pegmove
     */
    public Coordinate getStart(){
        return start;
    }

    /**
     *
     * @return
     */
    public Coordinate getEnd(){
        return end;
    }

    public int getPlayer(){
        return player;
    }

    /**
     * Checks if the current move is valid given the current state of the board
     * @param board
     * @param move
     * @return true if the move is valid
     */
    public static boolean checkMoveValidity(int[][] board, PegMove move) {
        if (move.getPlayer() != board[move.getStart().getX()][move.getStart().getY()]) {
            return false; //player cannot move a piece other than their own
        }
        return getValidEndPositions(board, move.getStart()).contains(move.getEnd());
    }

    private static ArrayList<Coordinate> validMoves;

    /**
     * Based on a given board state and starting position, finds all possible valid end positions for the piece in a single turn
     * @param board - current state of the game board
     * @param startPosition - the starting coordinate to the piece to potentially be moved
     * @return list of all possible valid end positions for the piece in a single turn
     */
    public static ArrayList<Coordinate> getValidEndPositions(int[][] board, Coordinate startPosition){
        validMoves = new ArrayList<Coordinate>();
        if(board[startPosition.getX()][startPosition.getY()] <= 0){
            return validMoves; //place to move does not exist
        }
        for (SingleMoveDirection dir : SingleMoveDirection.values()) {
            Coordinate c = adjacentMove(startPosition, dir);
            if(c.getX() < 0 || c.getX() > 16 || c.getY() < 0 || c.getX() > 16){
                continue;
            }
            if( isOpen(board, c) && !validMoves.contains(c) ){
                validMoves.add(c);
            } else {
                c = jumpMove(startPosition, dir);
                if(c.getX() < 0 || c.getX() > 16 || c.getY() < 0 || c.getY() > 16){
                    continue;
                }
                if( isOpen(board, c) && !validMoves.contains(c) && isTaken(board, adjacentMove(startPosition, dir))){
                    validMoves.add(c);
                    getValidEndPositionsRecursive(board, c);
                }
            }
        }
        //might have repeat positions??? but hopefully not
        if(validMoves.contains(startPosition)){validMoves.remove(startPosition);} //redundancy to remove selected piece
        return validMoves;
    }

    /**
     * Based on a given board state and starting position, finds all possible jump positions for the piece in a single turn
     * @param board - current state of the game board
     * @param startPosition - the starting coordinate to the piece to potentially be moved
     * @return list of all possible valid jump positions for the piece in a single turn
     */
    private static ArrayList<Coordinate> getValidEndPositionsRecursive(int[][] board, Coordinate startPosition){
        //see if another jump is possible
        for (SingleMoveDirection dir : SingleMoveDirection.values()) {
            Coordinate c = jumpMove(startPosition, dir);
            if(c.getX() < 0 || c.getX() > 16 || c.getY() < 0 || c.getX() > 16){
                continue;
            }
            if( isOpen(board, c) && !validMoves.contains(c) && isTaken(board, adjacentMove(startPosition, dir))){
                validMoves.add(c);
                getValidEndPositionsRecursive(board, c);
            }
        }
        return validMoves; //all unique viable coordinates
    }

    /**
     * Gives the projected destination of a move from a given location on the board
     * @param c - starting coordinate of the move
     * @param direction - direction for the piece to potentially be moved in
     * @return new potential coordinate of the piece
     */
    public static Coordinate adjacentMove(Coordinate c, SingleMoveDirection direction) {
        int x = c.getX();
        int y = c.getY();

        if(direction == SingleMoveDirection.E || direction == SingleMoveDirection.SE){
            x++;
        } else if(direction == SingleMoveDirection.W || direction == SingleMoveDirection.NW) {
            x--;
        }
        if(direction == SingleMoveDirection.SE || direction == SingleMoveDirection.SW) {
            y++;
        } else if (direction == SingleMoveDirection.NE || direction == SingleMoveDirection.NW){
            y--;
        }
        return new Coordinate(x,y);
    }

    /**
     * Gives the projected destination of a jump move from a given location on the board
     * @param c - starting coordinate of the move
     * @param direction - direction for the piece to potentially be moved in
     * @return new potential coordinate of the piece after a jump move
     */
    public static Coordinate jumpMove(Coordinate c, SingleMoveDirection direction) {
        return adjacentMove(adjacentMove(c, direction), direction);
    }

    /**
     * Checks is a piece cannot move to a given coordinate on the board
     * @param board - current state of the board
     * @param c - coordinate of the board position being checked
     * @return true : the board position is either taken by another piece or is invalid
     */
    public static boolean isTaken(int[][] board, Coordinate c){
        if(c.getX() < 0 || c.getX() > 16 || c.getY() < 0 || c.getX() > 16){
            return true;
        }
        return board[c.getX()][c.getY()] != 0;
    }
    /**
     * Checks is a piece can move to a given coordinate on the board
     * @param board - current state of the board
     * @param c - coordinate of the board position being checked
     * @return true : the board position a valid position not taken by a piece
     */
    public static boolean isOpen(int[][] board, Coordinate c){
        if(c.getX() < 0 || c.getX() > 16 || c.getY() < 0 || c.getX() > 16){
            return false;
        }
        return (board[c.getX()][c.getY()] == 0);
    }

}
