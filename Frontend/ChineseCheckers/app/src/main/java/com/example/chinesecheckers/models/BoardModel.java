package com.example.chinesecheckers.models;

import android.util.Log;

import com.example.chinesecheckers.utils.Coordinate;
import com.example.chinesecheckers.utils.PegMove;

import java.util.ArrayList;

/**
 * Models a game board for Chinese Checkers
 * Allows a simulation of the game by defining
 * the boundry of the board, and making valid moves
 */
public class BoardModel {
    int[][] board;
    PegMove previousMove;
    int turn;

    /**
     * Creates a new board and sets it to the initial state of
     */
    public BoardModel(){
        board = new int[17][17]; //create empty board
        turn = 0;
        resetBoard(); //set to default state
    }

    /**
     * @return the current turn of the board
     */
    public int getTurn(){ return turn;}

    //used for actual display
    /**
     * Returns the raw array representing board
     * @return int[][] representation of the board
     */
    public int[][] getRawBoard(){
        return board;
    }
    public PegMove getPreviousMove(){
        return previousMove;
    }

    /**
     * sets the board to the state before a game begins
     */
    public void resetBoard(){
        for(int i = 0; i < 17; i++){
            for(int j = 0; j < 17; j++) {
                board[i][j] = playerStart(new Coordinate(i,j));
            }
        }
        turn = 0;
    }

    /**
     * Gives the default state of a given coordinate on the board
     * @param c : coordinate to identify
     * @return identified default coordinate value
     */
    private int playerStart(Coordinate c){
        //player 1 (red squares)
        if(     (4 <= c.getX() && c.getX() <= 7) &&
                (0 <= c.getY() && c.getY() <= 3) &&
                -(c.getX() - 3) + (c.getY() + 1) >= 0){
            return 1;
        }
        //player 2 (yellow squares)
        if(     (9 <= c.getX() && c.getX() <= 12) &&
                (4 <= c.getY() && c.getY() <=  7) &&
                -(c.getX() - 8) + (c.getY() - 3) <= 0){
            return 2;
        }
        //player 3 (black squares)
        if(     (13 <= c.getX() && c.getX() <= 16) &&
                ( 9 <= c.getY() && c.getY() <= 12) &&
                -(c.getX() - 12) + (c.getY() - 8) >= 0){
            return 3;
        }
        //player 4 (blue squares)
        if(     ( 9 <= c.getX() && c.getX() <= 12) &&
                (13 <= c.getY() && c.getY() <= 16) &&
                -(c.getX() - 8) + (c.getY() - 12) <= 0){
            return 4;
        }
        //player 5 (green squares)
        if(     (4 <= c.getX() && c.getX() <=  7) &&
                (9 <= c.getY() && c.getY() <= 12) &&
                -(c.getX() - 3) + (c.getY() - 8) >= 0){
            return 5;
        }
        //player 6 (white squares)
        if(     (0 <= c.getX() && c.getX() <= 3) &&
                (4 <= c.getY() && c.getY() <= 7) &&
                -(c.getX() + 1) + (c.getY() - 3) <= 0){
            return 6;
        }
        //play area (orange squares)
        if(4 <= c.getX() && c.getX() <= 12 && 4 <= c.getY() && c.getY() <= 12){
            return 0;
        }
        return -1; //(gray squares)
    }

    /**
     * Reads the board to see if the given player has fufilled the win condition
     * @param player : the player number to check win condition
     * @return : if the player has won the game or not
     */
    public boolean playerWin(int player){
        boolean taken = false;
        boolean filled = true;
        switch(player){
            case 4 :
                for(int x = 4; x <= 7; x++) {
                    for (int y = 0; y <= 3; y++) {
                        if(-(x - 3) + (y + 1) >= 0){
                            if(board[x][y] == player){
                                taken = true;
                            }
                            if(board[x][y] <= 0){
                                filled = false;
                            }
                        }
                    }
                }
                if(taken && filled) {return true;}
                break;
            case 5 :
                for(int x = 9; x <= 12; x++) {
                    for (int y = 4; y <= 7; y++) {
                        if(-(x - 8) + (y - 3) <= 0){
                            if(board[x][y] == player){
                                taken = true;
                            }
                            if(board[x][y] <= 0){
                                filled = false;
                            }
                        }
                    }
                }
                if(taken && filled) {return true;}
                break;
            case 6 :
                for(int x = 13; x <= 16; x++) {
                    for (int y = 9; y <= 12; y++) {
                        if(-(x - 12) + (y - 8) >= 0){
                            if(board[x][y] == player){
                                taken = true;
                            }
                            if(board[x][y] <= 0){
                                filled = false;
                            }
                        }
                    }
                }
                if(taken && filled) {return true;}
                break;
            case 1 :
                for(int x = 9; x <= 12; x++) {
                    for (int y = 13; y <= 16; y++) {
                        if(-(x - 8) + (y - 12) <= 0){
                            if(board[x][y] == player){
                                taken = true;
                            }
                            if(board[x][y] <= 0){
                                filled = false;
                            }
                        }
                    }
                }
                if(taken && filled) {return true;}
                break;
            case 2 :
                for(int x = 4; x <= 7; x++) {
                    for (int y = 9; y <= 12; y++) {
                        if(-(x - 3) + (y - 8) >= 0){
                            if(board[x][y] == player){
                                taken = true;
                            }
                            if(board[x][y] <= 0){
                                filled = false;
                            }
                        }
                    }
                }
                if(taken && filled) {return true;}
                break;
            case 3 :
                for(int x = 0; x <= 3; x++) {
                    for (int y = 4; y <= 7; y++) {
                        if(-(x + 1) + (y - 3) <= 0){
                            if(board[x][y] == player){
                                taken = true;
                            }
                            if(board[x][y] <= 0){
                                filled = false;
                            }
                        }
                    }
                }
                if(taken && filled) {return true;}
                break;
            default : break;
        }
        return false;
    }

    /**
     * makes the specified move on the board as long as it is valid.
     * @param move
     */
    public void makeMove(PegMove move){
       if (PegMove.checkMoveValidity(board, move)) { //should be static reference, but jank
            board[move.getStart().getX()][move.getStart().getY()] = 0;
            board[move.getEnd().getX()][move.getEnd().getY()] = move.getPlayer();
            previousMove = move;
            turn++;
        } else {
            //do something to throw error
            System.out.println("ERROR HAS OCCURRED MOVE IS NOT VALID");
            return;
        }
    }

    public void forceSwap(Coordinate a, Coordinate b){
        int c = board[a.getX()][a.getY()];
        board[a.getX()][a.getY()] = board[b.getX()][b.getY()];
        board[b.getX()][b.getY()] = c;
    }


    /**
     * brings a board to a predefined state
     * @param moveHistory : list of moves
     */
    public void setBoard(ArrayList<PegMove> moveHistory){
        resetBoard();
        for(int count = 0; count < moveHistory.size()-1; count++){
            makeMove(moveHistory.get(count));
        }
        //moveHistory.forEach(move -> makeMove(move)); had errors w/ syntax
    }

    /**
     * Outputs a String representing the current board position
     * @return Board
     */
    public String toString(){ //custom spacing and rules for textview
        String str = "";
        for (int i = 0; i < 17; i++){
            for (int j = 0; j < 17; j++){
                if(board[j][i] == -1){
                    str += "  "; //two spaces
                } else{
                    str += board[j][i]; //single digit number
                }
                str += "   "; //indentation, varies based on text size/view size
            }
            str += "\n";
            if(i > 12){str += " ";} //for spacing purposes
        }
        return str;
    }

}

