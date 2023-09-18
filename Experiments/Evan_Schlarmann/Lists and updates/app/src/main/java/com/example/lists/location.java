package com.example.lists;

import android.widget.ImageView;

public class location {
    private ImageView image;
    private PieceType piece;
    private int pos[];

    public location(ImageView iv, PieceType i, int[] p){
        image = iv;
        piece = i;
        pos = p;
    }

    public Boolean hasPiece(){
        return piece != PieceType.NONE;
    }

    public PieceType getPiece(){
        return piece;
    }

    public void changePiece(PieceType newPiece){
        piece = newPiece;
    }

    public ImageView getImageView(){
        return image;
    }

    public void changeImageView(int imageId){
        image.setImageResource(imageId);
        image.invalidate();
    }

    public int getX(){
        return pos[1];
    }

    public int getY(){
        return pos[0];
    }
}
