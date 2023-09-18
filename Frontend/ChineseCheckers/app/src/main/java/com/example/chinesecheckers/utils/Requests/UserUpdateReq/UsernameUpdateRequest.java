package com.example.chinesecheckers.utils.Requests.UserUpdateReq;

import android.app.Activity;
import android.app.Dialog;
import android.content.Intent;
import android.util.Log;
import android.widget.TextView;

import com.example.chinesecheckers.models.UserModel;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * This class overrides the user update volley request for changing usernames.
 * It sends the request and then updates the activities view to display the changes.
 *
 * <br>
 * <br>Example:
 * <br>UsernameUpdateRequest nameReq = new UsernameUpdateRequest(TAG, updateField, updateDialog, ActiveUserProfile.this);
 * <br>nameReq.createRequestObject("username", updateField.getText().toString(), user, user);
 * <br>nameReq.sendRequest(Request.Method.PUT, Const.URL_USERS_UPDATE);
 */
public class UsernameUpdateRequest extends UpdateUser {

    private TextView updateField;
    private Dialog updateDialog;
    private Activity activity;

    /**
     * Creates the HTTP request object and the views that need to be updated.
     *
     * @param tag - activity identifier
     * @param tView - text view of the username field
     * @param dialog - single input dialog of the username field
     * @param activity - activity sending request
     */
    public UsernameUpdateRequest(String tag, TextView tView, Dialog dialog, Activity activity) {
        super(tag);

        updateField = tView;
        updateDialog = dialog;
        this.activity = activity;
    }

    /**
     * Parses the JSON object response and then displays whether the username is
     * taken, the request failed or got denied, or if a success refresh the activity
     * with the updated username.
     *
     * @param response - json object response from the HTTP request
     */
    @Override
    protected void handleResponse(JSONObject response) {
        Log.d(TAG, response.toString());

        try {
            Boolean success = true;
            UserModel updatedUser = new UserModel();
            String status = response.getString("role");

            updatedUser.parseJson(response);

            if (status.equals("taken")) {
                updateField.setError("Username is taken.");
                success = false;
            }

            if (status.equals("failed")) {
                updateField.setError("Error changing username.");
                success = false;
            }
            if (status.equals("denied")) {
                updateField.setError("Request denied.");
                success = false;
            }

            if (success) {
                updateDialog.dismiss();
                activity.finish();

                activity.overridePendingTransition(0, 0);
                Intent i = activity.getIntent();
                i.putExtra("updatedUser", updatedUser);
                activity.startActivity(i);
                activity.overridePendingTransition(0, 0);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }
}
