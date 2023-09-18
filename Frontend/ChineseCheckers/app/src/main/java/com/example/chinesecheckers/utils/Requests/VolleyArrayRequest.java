package com.example.chinesecheckers.utils.Requests;

import android.util.Log;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.JsonArrayRequest;
import com.example.chinesecheckers.Interfaces.ISimpleRequest;
import com.example.chinesecheckers.app.AppController;

import org.json.JSONArray;

/**
 * Generic volley request that sends a null object to the given URL using the
 * HTTP request type expecting a JSON array as a response.
 */
public class VolleyArrayRequest implements ISimpleRequest {
    protected JSONArray requestArray;
    protected String TAG;
    private String tag_json_request;

    /**
     * creates the volley request to know what activity created it
     * and what the request is for.
     *
     * @param tag - activity identifier
     * @param tag_json - HTTP request identifier
     */
    public VolleyArrayRequest(String tag, String tag_json) {
        TAG = tag;
        tag_json_request = tag_json;
    }

    /**
     * Creates a null JSON array for the volley request.
     */
    @Override
    public void createRequestObject() {
        requestArray = new JSONArray();
    }

    /**
     * Change this method when needed in inheriting requests. This method can help modify
     * given views in an activity.
     *
     * @param response - json array response from the HTTP request
     */
    protected void handleResponse(JSONArray response) {
        Log.d(TAG, response.toString());
    }

    /**
     * Makes a JSON array request to the URL using the given method.
     *
     * @param method - HTTP method type
     * @param URL - URL of the request
     */
    @Override
    public void sendRequest(int method, String URL) {

        //make request
        JsonArrayRequest jsonObjReq =
                new JsonArrayRequest(method,
                        URL, requestArray,
                        new Response.Listener<JSONArray>() {
                            @Override
                            public void onResponse(JSONArray response) {
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
