package com.example.lists;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;

public class MainActivity extends AppCompatActivity {

    Button loginButton;
    Button boardButton;
    EditText username;
    EditText password;
    Bundle extras;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        loginButton = findViewById(R.id.loginButton);
        boardButton = findViewById(R.id.boardButton);
        username = findViewById(R.id.username);
        password = findViewById(R.id.password);
        extras = getIntent().getExtras();


        if(extras != null){
            username.setText(extras.getString("username"));
        }

        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if((username.getText().toString().equals("test") && password.getText().toString().equals("123"))){
                    username.setError("Incorrect username or password");

                }
                else {
                    Intent i = new Intent(MainActivity.this, FriendList.class);
                    i.putExtra("username",username.getText().toString());
                    startActivity(i);
                }
            }
        });

        boardButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(MainActivity.this, GameBoard.class);
                i.putExtra("username", username.getText().toString());
                startActivity(i);
            }
        });
    }
}