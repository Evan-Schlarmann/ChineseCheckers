package com.example.dragdrop;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.DragEvent;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ImageView;

public class MainActivity extends AppCompatActivity {

    ImageView circle;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        circle = (ImageView) findViewById(R.id.theCircle);
        circle.startDragAndDrop();
/*
//        circle.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//            }
//        });

//        circle.setOnContextClickListener(new View.OnContextClickListener() {
//            @Override
//            public boolean onContextClick(View view) {
//                return false;
//            }
//        });

        circle.setOnDragListener(new View.OnDragListener() {
            float x, y, dx, dy;
            @Override
            public boolean onDrag(View view, DragEvent event) {

                if(event.getAction() == DragEvent.ACTION_DRAG_ENTERED || event.getAction() == DragEvent.ACTION_DRAG_STARTED){
                    x = event.getX();
                    y = event.getY();
                    while(!(event.getAction() == DragEvent.ACTION_DRAG_ENDED ||  event.getAction() == DragEvent.ACTION_DRAG_EXITED)){
                        dx = event.getX() - x;
                        dy = event.getX() - y;

                        circle.setX(circle.getX() + dx);
                        circle.setY(circle.getY() + dy);
                    }
                }
                return false;
                //action is move/drag
//                if(event.getAction() == DragEvent.ACTION_DRAG_LOCATION){
//                    dx = event.getX() - x;
//                    dy = event.getX() - y;
//
//                    circle.setX(circle.getX() + dx);
//                    circle.setY(circle.getY() + dy);
//
//                    x = event.getX();
//                    y = event.getY();
//                    circle.setX(x);
//                    circle.setY(y);
//                }
            }
        });
*/
/*        circle.setOnTouchListener(new View.OnTouchListener() {
            float x, y, dx, dy;
            @Override
            public boolean onTouch(View view, MotionEvent event) {
                //if the event action is push down
                if(event.getAction() == MotionEvent.ACTION_DOWN){
                    x = event.getX();
                    y = event.getY();
                }
                //action is move/drag
                if(event.getAction() == MotionEvent.ACTION_MOVE){
                    dx = event.getX() - x;
                    dy = event.getX() - y;

                    circle.setX(circle.getX() + dx);
                    circle.setY(circle.getY() + dy);

                    x = event.getX();
                    y = event.getY();
                    circle.setX(x);
                    circle.setY(y);
                }
                return false;
            }
        });*/
    }
        //floats for img cords : x,y is origional, dx,dy are changes to said cords
        float x, y, dx, dy;
        @Override
        public boolean onTouchEvent(MotionEvent event) {
            circle.drag
            if (true) {
                //if the event action is push down
                if (event.getAction() == MotionEvent.ACTION_DOWN) {
                    x = event.getX();
                    y = event.getY();
                }
                //action is move/drag
                if (event.getAction() == MotionEvent.ACTION_MOVE) {
                    dx = event.getX() - x;
                    dy = event.getX() - y;

                    circle.setX(circle.getX() + dx);
                    circle.setY(circle.getY() + dy);

                    x = event.getX();
                    y = event.getY();
                    circle.setX(x);
                    circle.setY(y);
                }
                return super.onTouchEvent(event);
            }
            return false;
        }

}