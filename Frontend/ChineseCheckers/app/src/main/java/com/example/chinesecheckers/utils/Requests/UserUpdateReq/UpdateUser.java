package com.example.chinesecheckers.utils.Requests.UserUpdateReq;

import com.example.chinesecheckers.utils.Requests.VolleyRequest;
import com.example.chinesecheckers.models.UserModel;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Update User implements the create request object that holds the target user and sending user.
 * This is to send an update request for one of a user's fields and making sure the sender has
 * the authority for the change.
 *
 * <br>
 * <br>Example:
 * <br>UpdateUser req = new UpdateUser(TAG);
 * <br>req.createRequestObject("username", updateField.getText().toString(), user, user);
 * <br>req.sendRequest(Request.Method.PUT, Const.URL_USERS_UPDATE);
 */
public class UpdateUser extends VolleyRequest {

    /**
     * Creates the HTTP object to update a user's information.
     *
     * @param tag - activity identifier
     */
    public UpdateUser(String tag) {
        super(tag, "json_update_user");
    }

    /**
     * Create a target object with the field being updated, then package it with the sending user.
     *
     * @param infoType - user info being updated
     * @param updatedInfo - the updated information
     * @param target - the user being updated
     * @param sender - the user sending the update
     */
    public void createRequestObject(String infoType, String updatedInfo, UserModel target, UserModel sender) {
        //make JSON object packet
        requestObject = new JSONObject();

        JSONObject _target = new JSONObject();
        JSONObject _sender = new JSONObject();
        try {
            // target user
            _target.put("userId", target.getUID()); //String?
            _target.put("secret", target.getSecret());
            _target.put(infoType, updatedInfo);

            // user sending request
            // This is different in the admin screen.
            _sender.put("userId", sender.getUID());
            _sender.put("secret", sender.getSecret());

            // Put target then sender into the request.
            requestObject.put("target", _target);
            requestObject.put("sender", _sender);

        } catch (JSONException e) {
            e.printStackTrace();
        }
    }
}
