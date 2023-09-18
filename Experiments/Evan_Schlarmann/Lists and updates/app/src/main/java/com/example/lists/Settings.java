package com.example.lists;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.google.android.material.chip.Chip;

public class Settings extends AppCompatActivity {

    Button saveSettings;
    EditText usernameText;
    Chip over18;
    Bundle extras;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);

        saveSettings = findViewById(R.id.saveSettingsButton);
        usernameText = findViewById(R.id.changeUsernameText);
        over18 = findViewById(R.id.over18Chip);
        extras = getIntent().getExtras();

        if(extras != null){
             if(extras.getBoolean("age")){
                 over18.performClick();
             }

             usernameText.setText(extras.getString("username"));
        }

        saveSettings.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if(usernameText.getText().toString().isEmpty()){
                    usernameText.setError("must have username.");
                }
                else{
                    // Start new activity to show the results.
                    Intent i = new Intent(Settings.this, FriendList.class);
                    i.putExtra("username", usernameText.getText().toString());
                    i.putExtra("age", over18.isChecked());
                    startActivity(i);
                }
            }
        });
    }
}