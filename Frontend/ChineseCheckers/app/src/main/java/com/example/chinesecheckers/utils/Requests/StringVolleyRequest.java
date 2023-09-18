package com.example.chinesecheckers.utils.Requests;

import android.util.Log;

import com.android.volley.AuthFailureError;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.example.chinesecheckers.Interfaces.ISimpleRequest;
import com.example.chinesecheckers.app.AppController;

import org.json.JSONObject;

/**
 * Generic volley request that sends a null object to the given URL using the
 * HTTP request type expecting a string as a response.
 *
 * This class circumvents the DELETE method and that it returns a string
 * but we still need to send a body object to the server.
 */
public class StringVolleyRequest implements ISimpleRequest {
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
    public StringVolleyRequest(String tag, String tag_json) {
        TAG = tag;
        tag_json_request = tag_json;
    }

    /**
     * Creates a null JSON array for the volley request.
     */
    @Override
    public void createRequestObject() {
        requestObject = new JSONObject();
    }

    /**
     * Change this method when needed in inheriting requests. This method can help modify
     * given views in an activity.
     *
     * @param response - string response from the HTTP request
     */
    protected void handleResponse(String response) {
        Log.d(TAG, response);
    }

    /**
     * Make the request with the given method and URL. This uses a string request
     * because DELETE method returns a string and this gets around that.
     *
     * @param method - HTTP method type
     * @param URL - URL of the request
     */
    @Override
    public void sendRequest(int method, String URL) {

        //make request
        StringRequest jsonObjReq =
                new StringRequest(method, URL,
                        new Response.Listener<String>() {
                            @Override
                            public void onResponse(String response) {
                                handleResponse(response);

                            }
                        }, new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        VolleyLog.d(TAG, "Error: " + error.getMessage());
                    }
                }){
                    @Override
                    public byte[] getBody() throws AuthFailureError {
                        return requestObject.toString().getBytes();
                    }

                    @Override
                    public String getBodyContentType() {
                        return "application/json; charset=utf-8";
                    }
                };

        AppController.getInstance().addToRequestQueue(jsonObjReq,
                tag_json_request);
    }
}
