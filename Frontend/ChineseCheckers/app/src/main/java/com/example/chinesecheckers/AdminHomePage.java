package com.example.chinesecheckers;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;

import com.example.chinesecheckers.models.UserModel;

import java.util.Objects;

/**
 * AdminHomePage creates a list of controls an admin can use and redirect to inorder to use.
 */
public class AdminHomePage extends AppCompatActivity {
    private UserModel user;

    /**
     * onCreate creates the view for AdminHomePage displaying buttons of the controls
     * an admin has access to. It requires the user from the previous intent in order
     * to have the user's information.
     *
     * @param savedInstanceState - contains all saved instances and information.
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_home_screen);

        Button userControls = findViewById(R.id.adminUserControls);
        user = (UserModel) getIntent().getSerializableExtra("user");

        // This creates the back button.
        Objects.requireNonNull(getSupportActionBar()).setDisplayHomeAsUpEnabled(true);

        userControls.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(AdminHomePage.this, UserList.class);
                i.putExtra("user", user);
                startActivity(i);
            }
        });
    }

    /**
     * onOptionsItemSelected finishes the activity when the support action bar back button is pressed.
     *
     * @param item - item that is selected.
     * @return boolean
     */
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            finish();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}