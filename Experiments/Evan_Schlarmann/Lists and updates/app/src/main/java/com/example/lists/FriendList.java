package com.example.lists;

import androidx.appcompat.app.AppCompatActivity;
import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;

import com.google.android.material.snackbar.Snackbar;

import java.util.ArrayList;

public class FriendList extends AppCompatActivity {

    ListView friendsList;
    Button openDialog;
    Button logoutButton;
    ArrayList<String> nameList = new ArrayList<>();
    Bundle extras;
    TextView viewUsername;
    Button settingsButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_friend_list);

        // Initialize all components
        friendsList = findViewById(R.id.friendList);
        openDialog = findViewById(R.id.openDialog);
        logoutButton = findViewById(R.id.logoutButton);
        viewUsername = findViewById(R.id.viewUsername);
        settingsButton = findViewById(R.id.settingsButton);
        extras = getIntent().getExtras();

        if(extras != null){
            String username = extras.getString("username");
            String greeting = "Hello " + username + "!";

            Boolean over18 = extras.getBoolean("age");
            if(!over18){
                openDialog.setVisibility(View.INVISIBLE);
            }

            viewUsername.setText(greeting);
        }

        // Populate array list
        nameList.add("Bob the Builder");
        nameList.add("Scooby Doo");
        nameList.add("Dora the Explorer");
        nameList.add("Optimus Prime");

        ArrayAdapter<String> arrayAdapter = new ArrayAdapter<>(this,
                androidx.appcompat.R.layout.support_simple_spinner_dropdown_item, nameList);

        friendsList.setAdapter(arrayAdapter);
        friendsList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int pos, long id) {
                String name = (String) friendsList.getItemAtPosition(pos);
                nameList.remove(name);
                ((BaseAdapter) friendsList.getAdapter()).notifyDataSetChanged();


                Snackbar snackbar = Snackbar.make(findViewById(R.id.friendList), "removed " + name, Snackbar.LENGTH_LONG);
                snackbar.show();
            }

        });

        openDialog.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showCustomDialog();
            }
        });

        logoutButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(FriendList.this, MainActivity.class);
                i.putExtra("username", extras.getString("username"));
                startActivity(i);
            }
        });

        settingsButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(FriendList.this, Settings.class);
                i.putExtra("username", extras.getString("username"));
                i.putExtra("age", extras.getBoolean("age"));
                startActivity(i);
            }
        });
    }

    void showCustomDialog(){
        Dialog dialog = new Dialog(FriendList.this);

        // Set dialog features and layout.
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setCancelable(true);
        dialog.setContentView(R.layout.dialog2);

        // Create components.
        Button addFriend = dialog.findViewById(R.id.addButton2);
        Button cancelButton = dialog.findViewById(R.id.cancelButton2);
        EditText friendName = dialog.findViewById(R.id.addFriendName2);

        addFriend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String name = friendName.getText().toString();

                if(!name.isEmpty()){
                    nameList.add(name);
                    ((BaseAdapter) friendsList.getAdapter()).notifyDataSetChanged();

                    dialog.dismiss();
                }
                else{
                    friendName.setError("name required");
                }
            }
        });

        cancelButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.dismiss();
            }
        });

        dialog.show();
    }
}

