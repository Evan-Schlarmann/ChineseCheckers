package com.example.chinesecheckers;

import static com.example.chinesecheckers.utils.VolleyProgressDialog.dismissProgressDialog;
import static com.example.chinesecheckers.utils.VolleyProgressDialog.showProgressDialog;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.android.volley.Request;
import com.example.chinesecheckers.utils.Requests.UserUpdateReq.MuteUserReq;
import com.example.chinesecheckers.utils.Requests.UserUpdateReq.PasswordUpdateRequest;
import com.example.chinesecheckers.utils.Requests.UserUpdateReq.UsernameUpdateRequest;
import com.example.chinesecheckers.models.UserModel;
import com.example.chinesecheckers.utils.Const;

import java.util.Objects;

/**
 * AdminUserProfilePage displays a user's profile with extra information that an
 * admin has access to read. It also allows the admin to change the user's information.
 */
public class AdminUserProfilePage extends AppCompatActivity {

    private final String TAG = AdminUserProfilePage.class.getSimpleName();
    private TextView profileUsername;
    private TextView profilePassword;
    private ProgressDialog pDialog;
    private EditText updateField;
    private Dialog updateDialog;
    private UserModel user;
    private UserModel chosenUser;

    /**
     * onCreate makes the view for the AdminUserProfilePage. This view display the chosen user's
     * information that was passed form the previous intent. It also requires the admin user from
     * the previous intent.
     *
     * The view contains the user's profile information including their username, password, role, and id.
     * The admin has access to buttons that display a dialog to change a user's username or password.
     *
     * @param savedInstanceState - contains all saved instances and information.
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_user_profile);

        profileUsername = findViewById(R.id.adminProfileUsername);
        profilePassword = findViewById(R.id.profilePassword);
        TextView profileRole = findViewById(R.id.adminProfileRole);
        TextView profileId = findViewById(R.id.profileId);
        Button changePasswordButton = findViewById(R.id.adminUserChangePass);
        Button changeUsernameButton = findViewById(R.id.adminUserChangeUsername);
        Button muteUser = findViewById(R.id.adminMuteUser);

        user = (UserModel) getIntent().getSerializableExtra("user");
        chosenUser = (UserModel) getIntent().getSerializableExtra("chosenUser");
        UserModel updatedUser = (UserModel) getIntent().getSerializableExtra("updatedUser");

        // If there has been an updated to info then modify the chosen user be updated.
        if(updatedUser != null){
            chosenUser = updatedUser;
        }

        // This creates the back button.
        Objects.requireNonNull(getSupportActionBar()).setDisplayHomeAsUpEnabled(true);

        pDialog = new ProgressDialog(this);
        pDialog.setMessage("Updating user.");
        pDialog.setCancelable(false);

        profileUsername.setText(chosenUser.getUsername());
        profilePassword.setText(chosenUser.getPassword());
        profileRole.setText(chosenUser.getRole());
        profileId.setText(chosenUser.getUID().toString());

        if(chosenUser.getIsMuted() == 1){ // un-mute the chosen user.
            muteUser.setText("Unmute User");
            muteUser.setBackgroundColor(Color.GREEN);

            muteUser.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    MuteUserReq muteUserReq = new MuteUserReq(TAG, AdminUserProfilePage.this);
                    muteUserReq.sendRequest(Request.Method.PUT, Const.URL_UNMUTE_USER + chosenUser.getUID());
                }
            });
        }else if(chosenUser.getIsMuted() == 0){ // mute the chosen user.
            muteUser.setText("Mute User");
            muteUser.setBackgroundColor(Color.RED);

            muteUser.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    MuteUserReq muteUserReq = new MuteUserReq(TAG, AdminUserProfilePage.this);
                    muteUserReq.sendRequest(Request.Method.PUT, Const.URL_MUTE_USER + chosenUser.getUID());
                }
            });
        }

        // Don't allow changing of other admins' information.
        if(chosenUser.getRole().equals("admin")){
            changeUsernameButton.setVisibility(View.INVISIBLE);
            changePasswordButton.setVisibility(View.INVISIBLE);
        }
        else{
            changeUsernameButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    showOneInputDialog("Change Username");
                }
            });

            changePasswordButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    showOneInputDialog("Change Password");
                }
            });
        }
    }

    /**
     * ShowOneInputDialog creates a dialog to change a user's username or password.
     * The dialog requires a title that decides what field is changing. It sends the change
     * request to the server and updates the players displayed information.
     *
     * @param title - The field that is changing. e.g. "Changing Password" or "Changing Username"
     */
    private void showOneInputDialog(String title) {
        updateDialog = new Dialog(AdminUserProfilePage.this);

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
                        UsernameUpdateRequest nameReq = new UsernameUpdateRequest(TAG, updateField, updateDialog, AdminUserProfilePage.this);
                        nameReq.createRequestObject("username", updateField.getText().toString(), chosenUser, user);

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
                        PasswordUpdateRequest passReq = new PasswordUpdateRequest(TAG, updateField, updateDialog, AdminUserProfilePage.this);
                        passReq.createRequestObject("password", updateField.getText().toString(), chosenUser, user);

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