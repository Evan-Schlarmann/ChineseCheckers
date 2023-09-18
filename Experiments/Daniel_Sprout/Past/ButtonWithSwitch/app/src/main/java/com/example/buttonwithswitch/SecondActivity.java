package com.example.buttonwithswitch;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

public class SecondActivity extends AppCompatActivity {

    Button backButton;
    Button displayCount;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_second);

        backButton = findViewById(R.id.backButton);
        displayCount = findViewById(R.id.displayCount);

        displayCount.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //wanted to change toast text size
                //have to create custom Toast
                Toast toast = Toast.makeText(getApplicationContext(), String.valueOf(Counter.getCount()), Toast.LENGTH_SHORT);
                //cast to LinearLayout to be able to recast to textview
                LinearLayout toastLayout = (LinearLayout) toast.getView();
                //cast to textview from start of message
                TextView toastVTwo = (TextView) toastLayout.getChildAt(0);
                //change text size
                toastVTwo.setTextSize(30);
                //display
                toast.show();
            }
        });

        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(SecondActivity.this, MainActivity.class);
                startActivity(i);
                callFinish();
            }
        });


    }
    private void callFinish(){ this.finish(); }

}