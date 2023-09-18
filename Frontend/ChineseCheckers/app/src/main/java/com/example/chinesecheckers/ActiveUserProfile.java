package com.example.chinesecheckers;

import static com.example.chinesecheckers.utils.VolleyProgressDialog.dismissProgressDialog;
import static com.example.chinesecheckers.utils.VolleyProgressDialog.showProgressDialog;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.android.volley.Request;
import com.example.chinesecheckers.utils.Requests.GetUserProfileRequest;
import com.example.chinesecheckers.utils.Requests.UserUpdateReq.PasswordUpdateRequest;
import com.example.chinesecheckers.utils.Requests.UserUpdateReq.UsernameUpdateRequest;
import com.example.chinesecheckers.models.StatsModel;
import com.example.chinesecheckers.models.UserModel;
import com.example.chinesecheckers.utils.Const;

import java.util.Objects;

/**
 * ActiveUserProfile displays the logged in user's information and allows them to change
 * their username and password. It also connects the user to their friend list and sign out page.
 */
public class ActiveUserProfile extends AppCompatActivity {

    private final String TAG = HomePage.class.getSimpleName();
    private TextView profileUsername;
    private UserModel user;
    private ProgressDialog pDialog;
    private EditText updateField;
    private Dialog updateDialog;
    private TextView profileRole;
    private Button changeUsernameButton;
    private Button getFriendList;
    private Button signOut;
    private Button changePassword;
    private StatsModel stats;
    private TextView matchCount;
    private TextView winCount;
    private TextView ELOView;

    /**
     * onCreate builds the user's profile page allowing them to see their username, role, and stats.
     * It also creates buttons to send the user to their friend list or sign them out.
     * It requires a user sent from the previous intent in order to display the user's information.
     *
     * @param savedInstanceState - contains all saved instances and information.
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_profile_settings);

        profileUsername = findViewById(R.id.activeProfileUsername);
        profileRole = findViewById(R.id.activeProfileRole);
        changeUsernameButton = findViewById(R.id.activeUserChangeUsername);
        getFriendList = findViewById(R.id.activeUserFriendList);
        signOut = findViewById(R.id.signOutButton);
        changePassword = findViewById(R.id.activeUserChangePass);
        matchCount = findViewById(R.id.settingsMatchesPlayed);
        winCount = findViewById(R.id.settingsGamesWon);
        ELOView = findViewById(R.id.settingsPlayerELO);
        user = (UserModel) getIntent().getSerializableExtra("user");

        pDialog = new ProgressDialog(this);
        pDialog.setMessage("Updating...");
        pDialog.setCancelable(false);
        Objects.requireNonNull(getSupportActionBar()).setDisplayHomeAsUpEnabled(true);

        changeUsernameButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showOneInputDialog("Change Username");
            }
        });

        changePassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showOneInputDialog("Change Password");
            }
        });

        signOut.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(ActiveUserProfile.this, LoginPage.class);
                i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(i);
            }
        });

        GetUserProfileRequest getUserById =
                new GetUserProfileRequest(TAG, this, user, profileUsername, profileRole, getFriendList);
        getUserById.setStatsViews(matchCount, winCount, ELOView);
        getUserById.sendRequest(Request.Method.GET, Const.URL_GET_USER_BYID + user.getUID());
    }

    /**
     * ShowOneInputDialog creates a dialog to change a user's username or password.
     * The dialog requires a title that decides what field is changing. It sends the change
     * request to the server and updates the players displayed information.
     *
     * @param title - The field that is changing. e.g. "Changing Password" or "Changing Username"
     */
    private void showOneInputDialog(String title) {
        updateDialog = new Dialog(ActiveUserProfile.this);

        // Set dialog features and layout.
        updateDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        updateDialog.setCancelable(true);
        updateDialog.setContentView(R.layout.one_input_dialog);

        // Create components.
        Button confirmChange = updateDialog.findViewById(R.id.confirmUsernameDialog);
        Button cancelChange = updateDialog.findViewById(R.id.cancelUsernameDialog);
        TextView dialogTitle = updateDialog.findViewById(R.id.oneInputDialogTitle);
        dialogTitle.setText(title);
        updateField = updateDialog.findViewById(R.id.newUsernameField);

        cancelChange.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                updateDialog.dismiss();
            }
        });

        confirmChange.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                switch (title) {
                    case "Change Username":

                        if (updateField.getText().toString().isEmpty()) {
                            updateField.setError("Username needed");
                            return;
                        }

                        // Create and send the update request
                        UsernameUpdateRequest nameReq = new UsernameUpdateRequest(TAG, updateField, updateDialog, ActiveUserProfile.this);
                        nameReq.createRequestObject("username", updateField.getText().toString(), user, user);

                        showProgressDialog(pDialog);
                        nameReq.sendRequest(Request.Method.PUT, Const.URL_USERS_UPDATE);
                        dismissProgressDialog(pDialog);
                        break;
                    case "Change Password":

                        if (updateField.getText().toString().isEmpty()) {
                            updateField.setError("Password needed");
                            return;
                        }

                        // Create and send the update request
                        PasswordUpdateRequest passReq = new PasswordUpdateRequest(TAG, updateField, updateDialog, ActiveUserProfile.this);
                        passReq.createRequestObject("password", updateField.getText().toString(), user, user);

                        showProgressDialog(pDialog);
                        passReq.sendRequest(Request.Method.PUT, Const.URL_USERS_UPDATE);
                        dismissProgressDialog(pDialog);
                        break;
                    default:
                        Log.d(TAG, "confirm change error");
                }


            }
        });

        // Display the dialog.
        updateDialog.show();
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