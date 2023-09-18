package com.example.board_pegs;

public class Peg {
    static double rootThree = 1.7320508076;
    private int x;
    private int y;
    private String color;

    public Peg(int x, int y, String color) {
        this.x = x;
        this.y = y;
        this.color = color; //brown = empty, null = unplayable space
    }

    /*
    returns the index of the peg in the board array
     */
    public int getXIndex() {
        return x;
    }
    public int getYIndex() {
        return y;
    }

    public void move(MoveType move) {
        if(move.ordinal() < 3){ //Moving East (0,1,2)
            x++;
        } else { //Moving West  (3,4,5)
            x--;
        }
        if(move.ordinal()%5 == 0) { //Moving North (0,5)
            y++;
        } else if ( (move.ordinal() == 3) || (move.ordinal() == 2) ){ //Moving South (2,3)
            y--;
        }
    }

    //calls recursive function for all valid moves
    public void getAllPossibleMoves(Peg[] board, coordinate) {
        for each surrounding move
                //if valid, and jump
                // check all possible moves from that position
                // call recursive function, add all moves to list
                //add that position to list
                // if valid no jump
                // add move to list
        returns list of valid moves
    }
    public void getAllPossibleMoves(Peg[] board, coordinate, move) {
        returns list of valid moves
    }

    public static boolean isValidMove(){
        return false; //TODO
    }
}