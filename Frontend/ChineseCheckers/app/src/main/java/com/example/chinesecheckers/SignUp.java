package com.example.chinesecheckers;

import static com.example.chinesecheckers.utils.VolleyProgressDialog.dismissProgressDialog;
import static com.example.chinesecheckers.utils.VolleyProgressDialog.showProgressDialog;

import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.JsonObjectRequest;
import com.example.chinesecheckers.app.AppController;
import com.example.chinesecheckers.utils.Const;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * The Sign Up Page is the page a user sees when they want to make a new account,
 * where they are requested to provide login credentials for the new account.
 * After successfully creating an account they are directed to the Login page to login with the new credentials.
 */
public class SignUp extends AppCompatActivity {

    private final String TAG = SignUp.class.getSimpleName();
    // Use these tags to cancel requests
    private final String tag_json_signup = "json_signup";

    private Button signUpButton;
    private Button cancelSignUp;
    private EditText usernameText;
    private EditText passwordText;
    private EditText confirmPasswordText;
    private Spinner roleSpinner;
    private ProgressDialog pDialog;

    /**
     * On Create sets up the View with on click listeners that redirect the user
     * to the home page or sign up page.  It requires the user to enter a new accounts login credentials.
     *
     * It creates buttons to send a user to sign in, or to request a new account from the server
     *
     * @param savedInstanceState - contains all saved instances and information.
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);

        signUpButton = findViewById(R.id.signUpButton);
        cancelSignUp = findViewById(R.id.cancelSignUp);
        usernameText = findViewById(R.id.usernameText2);
        passwordText = findViewById(R.id.passwordText2);
        confirmPasswordText = findViewById(R.id.confirmPasswordText);
        roleSpinner = findViewById(R.id.roleSpinner);

        // Populate the spinner with roles.
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this,
                R.array.role_array, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        roleSpinner.setAdapter(adapter);

        pDialog = new ProgressDialog(this);
        pDialog.setMessage("Loading...");
        pDialog.setCancelable(false);

        signUpButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                boolean validSignup = true;
                String pass1 = passwordText.getText().toString();
                String pass2 = confirmPasswordText.getText().toString();
                String name = usernameText.getText().toString();

                if(!(pass1.equals(pass2))){
                    passwordText.setError("Passwords must match");
                    confirmPasswordText.setError("Passwords must match");
                    validSignup = false;
                }
                // Overwrite the non equal error if password doesn't meet the requirements.
                if(pass1.isEmpty()){
                    passwordText.setError("password required");
                    validSignup = false;
                }
                if(name.isEmpty()){
                    usernameText.setError("username required");
                    validSignup = false;
                }
                if(validSignup)
                    makeJsonSignupReq();
            }
        });

        cancelSignUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(SignUp.this, LoginPage.class);
                startActivity(i);
            }
        });
    }

    /**
     * sends the entered login credentials to the server, displays an error if not accepted, if accepted directs the user to the login page to sign in
     */
    private void makeJsonSignupReq() {
        showProgressDialog(pDialog);

        JSONObject userParam = new JSONObject();
        try {
            //input your API parameters
            userParam.put("role", roleSpinner.getSelectedItem().toString());
            userParam.put("username", usernameText.getText().toString());
            userParam.put("password", passwordText.getText().toString());
        } catch (JSONException e) {
            e.printStackTrace();
        }

        JsonObjectRequest jsonObjReq = new JsonObjectRequest(Request.Method.POST,
                Const.URL_USERS_NEW, userParam,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        Log.d(TAG, response.toString());
                        try{
                            dismissProgressDialog(pDialog);
                            if(response.getString("role").equals("failed")) {
                                usernameText.setError("username is taken");
                            }else{
                                Intent i = new Intent(SignUp.this, LoginPage.class);
                                startActivity(i);

                            }
                        } catch(JSONException e){
                            e.printStackTrace();
                        }
                    }
                }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {
                VolleyLog.d(TAG, "Error: " + error.getMessage());

                dismissProgressDialog(pDialog);
            }
        });

        // Adding request to request queue
        AppController.getInstance().addToRequestQueue(jsonObjReq,
                tag_json_signup);

        // Cancelling request
        // ApplicationController.getInstance().getRequestQueue().cancelAll(tag_json_obj);
    }
}