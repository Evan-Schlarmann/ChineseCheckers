package com.example.buttonsanddisplay;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import org.w3c.dom.Text;

public class ToastPage extends AppCompatActivity {

    Button toastButton;
    Button setToastButton;
    EditText toastText;
    Button backButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_toast_page);

        toastButton = findViewById(R.id.toastButton);
        setToastButton = findViewById(R.id.sendToast);
        toastText = findViewById(R.id.toastText);
        backButton = findViewById(R.id.backButton);

        toastButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(getApplicationContext(), "You Clicked the button!", Toast.LENGTH_LONG).show();
            }
        });

        setToastButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(getApplicationContext(), toastText.getText().toString(), Toast.LENGTH_LONG).show();
            }
        });

        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(ToastPage.this, MainActivity.class);
                startActivity(i);
            }
        });
    }

}