package com.example.lists;

import java.util.ArrayList;

public class board {

    private location[][] grid;
    private location locationSelected;
    private int height;
    private int[] rowWidths;
    private ArrayList<location> validMoves;

    public board() {
        height = 17;
        rowWidths = new int[]{5, 6, 7, 8, 13, 12, 11, 10, 9, 10, 11, 12, 13, 8, 7, 6, 5};

        grid = new location[17][];
        for (int i = 0; i < height; i++) {
            grid[i] = new location[rowWidths[i]];
        }

        locationSelected = null;
        validMoves = new ArrayList<>();
    }

    public void setLocation(location loc){
        grid[loc.getY()][loc.getX()] = loc;
    }

    public void movePiece(location newLoc) {
        for(location loc : validMoves){
            loc.changeImageView(R.drawable.circle);
        }
        validMoves.clear();

        newLoc.changePiece(locationSelected.getPiece());
        newLoc.changeImageView(R.drawable.blue_filled_circle);
        locationSelected.getImageView().invalidate();

        locationSelected.changePiece(PieceType.NONE);
        locationSelected.changeImageView(R.drawable.circle);
        locationSelected.getImageView().invalidate();
    }

    public void selectLocation(location l, PieceType type) {

        if (l.hasPiece() && type == PieceType.BLUE) {
            locationSelected = l;
            locationSelected.changeImageView(R.drawable.outlined_filled_circle);

            getPossibleMoves();
        }
    }

    private void getPossibleMoves(){

        int currentY = locationSelected.getY();
        int currentX = locationSelected.getX();

        checkVertically(currentX, currentY, 1);
        checkSides(currentX, currentY, 0);
        checkVertically(currentX, currentY, -1);
    }

    private void checkVertically(int currentX, int currentY, int offsetY){
        int newY = currentY + offsetY;

        if(newY < 0 || newY >= height){
            return;
        }

        // check if row above is smaller/larger.
        if(rowWidths[newY] >= rowWidths[currentY]){

            for(int i = 0; i <= 1; i++){
                verifyLocations(currentX, i, currentY, offsetY);
            }
        }
        else{

            for(int i = -1; i <= 0; i++){
                verifyLocations(currentX, i, currentY, offsetY);
            }
        }
    }

    private void checkSides(int currentX, int currentY, int offsetY){
        for(int i = -1; i <= 1; i++){
            verifyLocations(currentX, i, currentY, offsetY);
        }
    }

    private void verifyLocations(int currentX, int offsetX, int currentY, int offsetY){
        int newY = currentY + offsetY;
        int newX = currentX + offsetX;

        if(!inGrid(newX, newY)){
            return;
        }

        location potentialLocation = grid[newY][newX];
        isLocationFree(potentialLocation);

        if(potentialLocation.hasPiece()){

            // If there is a piece to jump.
            newY = currentY + (offsetY * 2);
            newX = currentX + (offsetX * 2);

            // special case for vertical traversal to an equal length row.
            if(rowWidths[newY] == rowWidths[currentY] && offsetY != 0)
                newX++;

            if(!inGrid(newX, newY)){
                return;
            }

            isLocationFree(grid[newY][newX]);
        }
    }

    private void isLocationFree(location l){
        if(!l.hasPiece()){
            l.changeImageView(R.drawable.outlined_circle);
            validMoves.add(l);
        }
    }

    private boolean inGrid(int x, int y){
        if(y < 0 || y >= height)
            return false;

        if( x < 0 || x >= rowWidths[y]){
            return false;
        }

        if(grid[y][x] == null)
            return false;

        return true;
    }

    public Boolean validMove(location newLocation) {
        if (locationSelected.getX() == newLocation.getX() &&
                locationSelected.getY() == newLocation.getY()) {

            // Since the position is the same deselect the location.
            for(location loc : validMoves){
                loc.changeImageView(R.drawable.circle);
            }
            validMoves.clear();

            locationSelected.changeImageView(R.drawable.blue_filled_circle);
            locationSelected = null;

            return false;
        }

        if(validMoves.contains(newLocation)){
            return true;
        }

        return false;
    }

    public Boolean isPieceSelected() {
        if (locationSelected == null)
            return false;

        return locationSelected.hasPiece();
    }

    public int getHeight(){
        return height;
    }

    public int getWidth(int height){
        return rowWidths[height];
    }

    public location[][] getGrid(){
        return grid;
    }

    public int offset(int y){

        // This correct the position math of the top and bottom player triangles.
        if(y <= 12 && y >= 4)
            return 0;

        return 4;
    }
}