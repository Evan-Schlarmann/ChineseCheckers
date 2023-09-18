package com.example.board_pegs;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentActivity;

import android.os.Bundle;

public class MainActivity extends AppCompatActivity {

    BlankFragment game;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
         game = BlankFragment.newInstance();

    }
    int arr[][] = new int[17][17];

    public void createBoardView(){

    }
    /*
    Assumed Dimensions of Container are 26x30, multiply scalar by width to get placing coordinates
     */
    public static double[] indexToCoordScalar(int[] coordinate){
        double[] coords = new double[2];
        //convert X
        coords[0] += (2*(coordinate[0] + 3)) - 2;
        //convert Y
        coords[0] += -(coordinate[1]);
        coords[1] = Math.sqrt(3) * (coords[1]);

        coords[0] /= 26;
        coords[1] /= 30;
        return coords;
    }
}