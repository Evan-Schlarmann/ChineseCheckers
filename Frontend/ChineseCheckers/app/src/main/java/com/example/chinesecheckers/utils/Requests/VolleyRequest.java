package com.example.chinesecheckers.utils.Requests;

import android.util.Log;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.JsonObjectRequest;
import com.example.chinesecheckers.Interfaces.ISimpleRequest;
import com.example.chinesecheckers.app.AppController;

import org.json.JSONObject;

/**
 * Generic volley request that sends a null object to the given URL using the
 * HTTP request type expecting a JSON object as a response.
 */
public class VolleyRequest implements ISimpleRequest {

    protected JSONObject requestObject;
    protected String TAG;
    private String tag_json_request;

    /**
     * creates the volley request to know what activity created it
     * and what the request is for.
     *
     * @param tag - activity identifier
     * @param tag_json - HTTP request identifier
     */
    public VolleyRequest(String tag, String tag_json) {
        TAG = tag;
        tag_json_request = tag_json;
    }

    /**
     * Creates a null JSON object for the volley request.
     */
    @Override
    public void createRequestObject() {
        requestObject = new JSONObject();
    }

    /**
     * Change this method when needed in inheriting requests. This method can help modify
     * given views in an activity.
     *
     * @param response - json object response from the HTTP request
     */
    protected void handleResponse(JSONObject response) {
        Log.d(TAG, response.toString());
    }

    /**
     * Makes a JSON object request to the URL using the given method.
     *
     * @param method - HTTP method type
     * @param URL - URL of the request
     */
    @Override
    public void sendRequest(int method, String URL) {

        //make request
        JsonObjectRequest jsonObjReq =
                new JsonObjectRequest(method,
                        URL, requestObject,
                        new Response.Listener<JSONObject>() {
                            @Override
                            public void onResponse(JSONObject response) {
                                handleResponse(response);

                            }
                        }, new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        VolleyLog.d(TAG, "Error: " + error.getMessage());
                    }
                });

        AppController.getInstance().addToRequestQueue(jsonObjReq,
                tag_json_request);
    }
}
