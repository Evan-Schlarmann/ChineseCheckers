package com.example.chinesecheckers.utils;

import android.app.ProgressDialog;


/**
 * VolleyProgressDialog displays a loading screen dialog when making a HTTP request.
 */
public class VolleyProgressDialog {

    /**
     * Show the progress dialog if it is not already.
     *
     * @param pDialog
     */
    public static void showProgressDialog(ProgressDialog pDialog){
        if (!pDialog.isShowing())

            pDialog.show();
    }

    /**
     * Dismiss the dialog if it is showing.
     *
     * @param pDialog
     */
    public static void dismissProgressDialog(ProgressDialog pDialog) {
        if (pDialog.isShowing())
            pDialog.dismiss();
    }
}
