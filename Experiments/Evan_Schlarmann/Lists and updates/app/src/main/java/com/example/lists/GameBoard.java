package com.example.lists;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;


public class GameBoard extends AppCompatActivity {

    board board;
    ImageView[][] images;
    LinearLayout screen;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_demo_board);

        screen = findViewById(R.id.boardLayout);


        board = new board();
        loadImages();

        for(int y = 0; y < board.getHeight(); y++){
            for(int offsetX = board.offset(y); offsetX < board.getWidth(y); offsetX++){
                int xpos = offsetX - board.offset(y);

                // filling with preset locations, will change later.
                if(xpos == 0 && y == 0)
                    board.setLocation(new location(images[y][xpos], PieceType.BLUE, new int[]{y, offsetX}));
                else if(xpos == 1 && y == 2){
                    board.setLocation(new location(images[y][xpos], PieceType.RED, new int[]{y, offsetX}));
                }
                else if(xpos == 6 && y == 5){
                    board.setLocation(new location(images[y][xpos], PieceType.RED, new int[]{y, offsetX}));
                }
                else if(xpos == 3 && y == 8){
                    board.setLocation(new location(images[y][xpos], PieceType.RED, new int[]{y, offsetX}));
                }
                else
                    board.setLocation(new location(images[y][xpos], PieceType.NONE, new int[]{y, offsetX}));
            }
        }

        for(location[] tiles: board.getGrid()){
            for(location tile : tiles) {

                if(tile == null)
                    continue;

                tile.getImageView().setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {

                        if (board.isPieceSelected()) {
                            if(!board.validMove(tile)){
                                return;
                            }
                            board.movePiece(tile);

                        } else {
                            board.selectLocation(tile, tile.getPiece());
                        }
                    }
                });
            }
        }
    }

    public void loadImages(){
        images = new ImageView[][]{{findViewById(R.id.iv1)}, // height 0
                {findViewById(R.id.iv2), findViewById(R.id.iv3)}, // height 1
                {findViewById(R.id.iv4), findViewById(R.id.iv5), findViewById(R.id.iv6)}, // height 2
                {findViewById(R.id.iv7), findViewById(R.id.iv8), findViewById(R.id.iv9), findViewById(R.id.iv10)}, // height 3
                {findViewById(R.id.iv11), findViewById(R.id.iv12), findViewById(R.id.iv13), findViewById(R.id.iv14), findViewById(R.id.iv15), // height 4
                    findViewById(R.id.iv16), findViewById(R.id.iv17), findViewById(R.id.iv18), findViewById(R.id.iv19), findViewById(R.id.iv20),
                    findViewById(R.id.iv21), findViewById(R.id.iv22), findViewById(R.id.iv23)},
                {findViewById(R.id.iv24), findViewById(R.id.iv25), findViewById(R.id.iv26), findViewById(R.id.iv27), findViewById(R.id.iv28), // height 5
                        findViewById(R.id.iv29), findViewById(R.id.iv30), findViewById(R.id.iv31), findViewById(R.id.iv32), findViewById(R.id.iv33),
                        findViewById(R.id.iv34), findViewById(R.id.iv35)},
                {findViewById(R.id.iv36), findViewById(R.id.iv37), findViewById(R.id.iv38), findViewById(R.id.iv39), findViewById(R.id.iv40), // height 6
                        findViewById(R.id.iv41), findViewById(R.id.iv42), findViewById(R.id.iv43), findViewById(R.id.iv44), findViewById(R.id.iv45),
                        findViewById(R.id.iv46)},
                {findViewById(R.id.iv47), findViewById(R.id.iv48), findViewById(R.id.iv49), findViewById(R.id.iv50), findViewById(R.id.iv51), // height 7
                        findViewById(R.id.iv52), findViewById(R.id.iv53), findViewById(R.id.iv54), findViewById(R.id.iv55), findViewById(R.id.iv56)},
                {findViewById(R.id.iv57), findViewById(R.id.iv58), findViewById(R.id.iv59), findViewById(R.id.iv60), findViewById(R.id.iv61), // height 8
                        findViewById(R.id.iv62), findViewById(R.id.iv63), findViewById(R.id.iv64), findViewById(R.id.iv65)},
                {findViewById(R.id.iv66), findViewById(R.id.iv67), findViewById(R.id.iv68), findViewById(R.id.iv69), findViewById(R.id.iv70), // height 9
                        findViewById(R.id.iv71), findViewById(R.id.iv72), findViewById(R.id.iv73), findViewById(R.id.iv74), findViewById(R.id.iv75)},
                {findViewById(R.id.iv76), findViewById(R.id.iv77), findViewById(R.id.iv78), findViewById(R.id.iv79), findViewById(R.id.iv80), // height 10
                        findViewById(R.id.iv81), findViewById(R.id.iv82), findViewById(R.id.iv83), findViewById(R.id.iv84), findViewById(R.id.iv85),
                        findViewById(R.id.iv86)},
                {findViewById(R.id.iv87), findViewById(R.id.iv88), findViewById(R.id.iv89), findViewById(R.id.iv90), findViewById(R.id.iv91), // height 11
                        findViewById(R.id.iv92), findViewById(R.id.iv93), findViewById(R.id.iv94), findViewById(R.id.iv95), findViewById(R.id.iv96),
                        findViewById(R.id.iv97), findViewById(R.id.iv98)},
                {findViewById(R.id.iv99), findViewById(R.id.iv100), findViewById(R.id.iv101), findViewById(R.id.iv102), findViewById(R.id.iv103), // height 12
                        findViewById(R.id.iv104), findViewById(R.id.iv105), findViewById(R.id.iv106), findViewById(R.id.iv107), findViewById(R.id.iv108),
                        findViewById(R.id.iv109), findViewById(R.id.iv110), findViewById(R.id.iv111)},
                {findViewById(R.id.iv112), findViewById(R.id.iv113), findViewById(R.id.iv114), findViewById(R.id.iv115)}, // height 13
                {findViewById(R.id.iv116), findViewById(R.id.iv117), findViewById(R.id.iv118)}, // height 14
                {findViewById(R.id.iv119), findViewById(R.id.iv120)}, // height 15
                {findViewById(R.id.iv121)}}; // height 16
    }

//    public void loadImages(){
//        images = new ImageView[][]{{findViewById(R.id.IV6), findViewById(R.id.IV7)}, // height 0
//                {findViewById(R.id.IV3), findViewById(R.id.IV4), findViewById(R.id.IV5)}, // height 1
//                {findViewById(R.id.IV1), findViewById(R.id.IV2)}}; // height 2;
//    }
}