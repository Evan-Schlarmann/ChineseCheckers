package com.example.buttonsanddisplay;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.SeekBar;
import android.widget.TextView;

import java.util.Random;

public class MainActivity extends AppCompatActivity {

    Button nextPage;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Button rollNumber = findViewById(R.id.rollNumber);
        TextView resultsText = findViewById(R.id.resultsText);
        SeekBar seekBar1 = findViewById(R.id.seekBar);
        nextPage = findViewById(R.id.nextPage);

        rollNumber.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v)
            {
                int rand = new Random().nextInt(seekBar1.getProgress());
                resultsText.setText(String.valueOf(rand));
            }

        });

        nextPage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(MainActivity.this, ToastPage.class);
                startActivity(i);
            }
        });
    }
}