package com.example.chinesecheckers;


import static com.example.chinesecheckers.utils.VolleyProgressDialog.dismissProgressDialog;
import static com.example.chinesecheckers.utils.VolleyProgressDialog.showProgressDialog;

import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.JsonObjectRequest;
import com.example.chinesecheckers.app.AppController;
import com.example.chinesecheckers.models.UserModel;
import com.example.chinesecheckers.utils.Const;
import com.example.chinesecheckers.utils.Requests.UserUpdateReq.UsernameUpdateRequest;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * The Login Page is the first page a user sees, where they are requested to provide their login credentials.
 * After successfully logging in they are directed to the HomePage
 */
public class LoginPage extends AppCompatActivity {

    private final String TAG = LoginPage.class.getSimpleName();
    // Use these tags to cancel requests
    private final String tag_json_login = "json_login";

    private Button loginButton;
    private Button signUpButton;
    private EditText usernameText;
    private EditText passwordText;
    private ProgressDialog pDialog;

    private UserModel user;

    /**
     * On Create sets up the View with on click listeners that redirect the user
     * to the home page or sign up page.  It requires the user to provide their login credentials.
     *
     * It creates buttons to send a user to signup, or to their home page.
     *
     * @param savedInstanceState - contains all saved instances and information.
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        loginButton = findViewById(R.id.loginButton);
        signUpButton = findViewById(R.id.startSignUp);
        usernameText = findViewById(R.id.usernameText);
        passwordText = findViewById(R.id.passwordText);

        pDialog = new ProgressDialog(this);
        pDialog.setMessage("Loading...");
        pDialog.setCancelable(false);

        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                makeJsonLoginReq();
            }
        });

        signUpButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(LoginPage.this, SignUp.class);
                startActivity(i);
            }
        });
    }

    /**
     * checks the login credentials entered, displays an error if incorrect, if successful the user is navigated to their homepage
     */
    private void makeJsonLoginReq() {
        showProgressDialog(pDialog);

        JSONObject userParam = new JSONObject();
        try {
            //input your API parameters
            userParam.put("username", usernameText.getText().toString());
            userParam.put("password", passwordText.getText().toString());
        } catch (JSONException e) {
            e.printStackTrace();
        }

        JsonObjectRequest jsonObjReq =
                new JsonObjectRequest(Request.Method.POST,
                Const.URL_LOGIN, userParam,
                new Response.Listener<JSONObject>() {

                    @Override
                    public void onResponse(JSONObject response) {
                        Log.d(TAG, response.toString());
                        dismissProgressDialog(pDialog);

                        /*
                         * If correct login to home page otherwise show incorrect login error.
                         */
                        try {
                            if (response.getString("secret").equals("denied")) {

                                usernameText.setError("Wrong username or password");
                                passwordText.setError("Wrong username or password");
                                return;
                            }

                            user = new UserModel();
                            user.parseJson(response);
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

                        Intent i = new Intent(LoginPage.this, HomePage.class);
                        i.putExtra("user", user);
                        startActivity(i);
                    }
                }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {
                VolleyLog.d(TAG, "Error: " + error.getMessage());
                dismissProgressDialog(pDialog);
            }
        });

        // Adding request to request queue
        AppController.getInstance().addToRequestQueue(jsonObjReq, tag_json_login);

        // Cancelling request
        // ApplicationController.getInstance().getRequestQueue().cancelAll(tag_json_obj);
    }
}