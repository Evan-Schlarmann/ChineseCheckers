package com.example.chinesecheckers;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.chinesecheckers.models.BoardModel;
import com.example.chinesecheckers.utils.Coordinate;
import com.example.chinesecheckers.utils.PegMove;

import java.util.ArrayList;

public class GameBoardFrag extends View {
    private final int boardColor;
    private final int openPegColor;
    private final int selectedPegColor;
    private final int potentialPegColor;
    private final int player1Color;
    private final int player2Color;
    private final int player3Color;
    private final int player4Color;
    private final int player5Color;
    private final int player6Color;
    private final Paint paint = new Paint();
    private int[][] board;

    private Coordinate selected;
    private ArrayList<Coordinate> potentials;

    public GameBoardFrag(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        TypedArray a = context.getTheme().obtainStyledAttributes(
                attrs, R.styleable.GameBoardFrag, 0, 0);
        try {
            boardColor = a.getInteger(R.styleable.GameBoardFrag_boardColor, 0);
            openPegColor = a.getInteger(R.styleable.GameBoardFrag_openPegColor, 0);
            selectedPegColor = a.getInteger(R.styleable.GameBoardFrag_selectedPegColor, 0);
            potentialPegColor = a.getInteger(R.styleable.GameBoardFrag_potentialPegColor, 0);
            player1Color = a.getInteger(R.styleable.GameBoardFrag_player1Color, 0);
            player2Color = a.getInteger(R.styleable.GameBoardFrag_player2Color, 0);
            player3Color = a.getInteger(R.styleable.GameBoardFrag_player3Color, 0);
            player4Color = a.getInteger(R.styleable.GameBoardFrag_player4Color, 0);
            player5Color = a.getInteger(R.styleable.GameBoardFrag_player5Color, 0);
            player6Color = a.getInteger(R.styleable.GameBoardFrag_player6Color, 0);
        } finally {
            a.recycle();
        }
        board = new BoardModel().getRawBoard(); //should be default board
        selected = new Coordinate(0,0);
        potentials = new ArrayList<Coordinate>();
    }

    public void select(Coordinate c, BoardModel board) {
        this.board = board.getRawBoard();
        selected = c;
        potentials = PegMove.getValidEndPositions(board.getRawBoard(), c);
        invalidate();
    }
    public void deselect(BoardModel board){
        this.board = board.getRawBoard();
        selected = new Coordinate(0,0);
        potentials = new ArrayList<Coordinate>();
        invalidate();
    }
    public Coordinate getSelected(){
        return selected;
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        int dimensions = Math.min(getMeasuredWidth() / 13, getMeasuredHeight() / 15);
        setMeasuredDimension(dimensions * 13, dimensions * 15);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        paint.setStyle(Paint.Style.FILL);
        paint.setAntiAlias(true);
        drawGameBoard(canvas);
    }

    private void drawGameBoard(Canvas canvas) {
        //draw initial triangles and center hexagon
        paint.setColor(boardColor);
        // draw current board state
        for (int i = 0; i < 17; i++) {
            for (int j = 0; j < 17; j++) {
                drawPeg(canvas, i, j, board[i][j]);
            }
        }
        drawHighlight(canvas, selected.getX(), selected.getY(), true);
        for (Coordinate potential : potentials) {
            drawHighlight(canvas, potential.getX(), potential.getY(), false);
        }
    }

    private void drawPeg(Canvas canvas, int x, int y, int value) {
        paint.setStyle(Paint.Style.FILL);
        switch (value) {
            case 0:
                paint.setColor(openPegColor);
                break;
            case 1:
                paint.setColor(player1Color);
                break;
            case 2:
                paint.setColor(player2Color);
                break;
            case 3:
                paint.setColor(player3Color);
                break;
            case 4:
                paint.setColor(player4Color);
                break;
            case 5:
                paint.setColor(player5Color);
                break;
            case 6:
                paint.setColor(player6Color);
                break;
            default:
                paint.setColor(boardColor);
                break;
        }
        float cx = ((2 * (x + 3) - y) - 1) * canvas.getWidth() / 26;
        float cy = (((float) Math.sqrt(3) * y) + 1) * canvas.getHeight() / 30;
        float radius = canvas.getWidth() / 30;
        canvas.drawCircle(cx, cy, radius, paint);
    }

    /**
     * Creates a Highlight for a pegspace
     * @param x
     * @param y
     * @param bool : true = is selected, false = is potential
     */
    public void drawHighlight(Canvas canvas, int x, int y, boolean bool) {
        if(board[x][y] < 0){ return; }

        paint.setStyle(Paint.Style.FILL);
        if (bool) {
            paint.setColor(selectedPegColor);
        } else {
            paint.setColor(potentialPegColor);
        }
        float cx = (((2 * (x + 3)) - y) - 1) * canvas.getWidth() / 26;
        float cy = (((float) Math.sqrt(3) * y) + 1) * canvas.getHeight() / 30;
        float radius = canvas.getWidth() / 55; //was 30
        canvas.drawCircle(cx, cy, radius, paint);
    }


}