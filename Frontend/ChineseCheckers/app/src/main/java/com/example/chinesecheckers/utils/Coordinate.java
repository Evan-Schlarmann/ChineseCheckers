package com.example.chinesecheckers.utils;

import androidx.annotation.Nullable;

/**
 * A x and y coordinate to represnt a location in 3D space
 */
public class Coordinate {
    int x;
    int y;
    /**
     *
     * @param x - the X value of the coordinate
     * @param y - the X value of the coordinate
     */

    public Coordinate(int x, int y){
        this.x = x;
        this.y = y;
    }

    /**
     * @return - the X value of the coordinate
     */
    public int getX(){
        return this.x;
    }
    /**
     * @return - the Y value of the coordinate
     */
    public int getY(){
        return this.y;
    }

    /**
     * @param z - the new X value of the coordinate
     */
    public void setX(int z){ x = z;}
    /**
     * @param z - the new Y value of the coordinate
     */
    public void setY(int z){ y = z;}

    /**
     * @return - the new X value of the coordinate
     */
    public String toString(){
        return "x:" + this.x + ", y:" + this.y;
    }

    public int getPiece(int[][] rawBoard) {
        if(this.y >= rawBoard.length || this.x >= rawBoard.length){return -1;}
        return rawBoard[this.x][this.y];
    }

    /**
     *
     * @param obj
     * @return true : if obj is a coordinate with the same x and y values
     */
    @Override
    public boolean equals(@Nullable Object obj) {
        if (obj == this) {
            return true;
        } else if(!(obj instanceof Coordinate)){
            return false;
        }
        Coordinate c = (Coordinate) obj;
        return (c.getX() == this.x) && (c.getY() == this.y);
    }
}
