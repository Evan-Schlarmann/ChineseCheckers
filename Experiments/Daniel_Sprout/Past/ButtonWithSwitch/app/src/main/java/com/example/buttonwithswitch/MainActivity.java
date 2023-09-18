package com.example.buttonwithswitch;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Switch;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity {

    Button inc;
    Button reset;
    TextView count;
    Switch addorsub;
    Button nextButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Button inc = findViewById(R.id.inc);
        Switch addorsub = findViewById(R.id.addorsub);
        Button reset = findViewById(R.id.reset);
        TextView count = findViewById(R.id.count);
        Button nextButton = findViewById(R.id.nextButton);

        updateCount(count);
        inc.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v)
            {
                //read switch state and increment
                if(addorsub.isChecked()){
                    Counter.incrementCount(-1);
                }else {
                    Counter.incrementCount(1);
                }
                //display count
               updateCount(count);

            }

        });

        reset.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v)
            {
               Counter.resetCount();
               updateCount(count);
            }
        });

        nextButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(MainActivity.this, SecondActivity.class);
                startActivity(i);
                callFinish();
            }
        });
    }
    private void updateCount(TextView count){
        if(Counter.getCount() < 0){
            count.setText(" " + String.valueOf(Counter.getCount()));
        }else{
            count.setText("  " + String.valueOf(Counter.getCount()));
        }
    }

    //used to close current activity
    private void callFinish(){ this.finish(); }
}