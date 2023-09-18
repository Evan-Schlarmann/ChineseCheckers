package com.example.chinesecheckers;

import static com.example.chinesecheckers.utils.VolleyProgressDialog.dismissProgressDialog;
import static com.example.chinesecheckers.utils.VolleyProgressDialog.showProgressDialog;

import androidx.appcompat.app.AppCompatActivity;


import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.JsonArrayRequest;
import com.example.chinesecheckers.app.AppController;
import com.example.chinesecheckers.models.UserModel;
import com.example.chinesecheckers.utils.Const;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Objects;

/**
 * UserList displays all users in a scrollable list where it displays the user's
 * username and clicking on a row send you to their profile page.
 */
public class UserList extends AppCompatActivity {

    private final String TAG = UserList.class.getSimpleName();
    // Use these tags to cancel requests
    private final String tag_json_userList = "json_userList";

    private ArrayList<UserModel> userList;
    private ProgressDialog pDialog;
    private ListView userListView;
    private UserModel user;


    /**
     * onCreate links the XML view and calls the make user list request.
     *
     * @param savedInstanceState - contains all saved instances and information.
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_list);

        userListView = findViewById(R.id.userList);
        userList = new ArrayList<>();
        user = (UserModel) getIntent().getSerializableExtra("user");

        pDialog = new ProgressDialog(this);
        pDialog.setMessage("Loading...");
        pDialog.setCancelable(false);

        Objects.requireNonNull(getSupportActionBar()).setDisplayHomeAsUpEnabled(true);

        makeUserListReq();
    }

    /**
     * this method calls the servers and gets all users back in an array list. It then
     * populates the array into an adapter that is displayed to the user.
     */
    private void makeUserListReq() {
        showProgressDialog(pDialog);
        JsonArrayRequest req = new JsonArrayRequest(Const.URL_USERS_ALL,
                new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray response) {
                        Log.d(TAG, response.toString());
                        
                        try {
                            for(int i = 0; i < response.length(); i++){
                                JSONObject tempUser = response.getJSONObject(i);

                                UserModel newUser = new UserModel();
                                newUser.parseJson(tempUser);

                                userList.add(newUser);
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

                        ArrayAdapter<UserModel> arrayAdapter = new ArrayAdapter<>(getApplicationContext(),
                                androidx.appcompat.R.layout.support_simple_spinner_dropdown_item, userList);

                        userListView.setAdapter(arrayAdapter);
                        userListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                            @Override
                            public void onItemClick(AdapterView<?> adapterView, View view, int pos, long id) {
                                UserModel chosenUser = (UserModel) userListView.getItemAtPosition(pos);

                                if(user.getRole().equals("admin")){
                                    Intent i = new Intent(UserList.this, AdminUserProfilePage.class);
                                    i.putExtra("user", user);
                                    i.putExtra("chosenUser", chosenUser);
                                    startActivity(i);
                                }
                                else{
                                    Intent i = new Intent(UserList.this, UserProfile.class);
                                    i.putExtra("user", user);
                                    i.putExtra("chosenUser", chosenUser);
                                    startActivity(i);
                                }
                            }

                        });
                        dismissProgressDialog(pDialog);
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                VolleyLog.d(TAG, "Error: " + error.getMessage());
                dismissProgressDialog(pDialog);
            }
        });

        // Adding request to request queue
        AppController.getInstance().addToRequestQueue(req,
                tag_json_userList);

        // Cancelling request
        // ApplicationController.getInstance().getRequestQueue().cancelAll(tag_json_arry);
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

    /**
     * When the page is visible again reload the page to get possible changes.
     */
    @Override
    protected void onRestart () {
        super.onRestart();
        this.finish();
        this.startActivity(this.getIntent());
    }
}
