package com.wfms.nectar.retrofit;

import android.content.Context;
import android.content.DialogInterface;
import android.util.Log;

import androidx.appcompat.app.AlertDialog;

/**
 * Created by aashita on 25/07/17.
 */

public abstract class RetrofitResponseHandler<T> {

    private static final String TAG = RetrofitResponseHandler.class.getSimpleName();

    private static final String LINE_SEPARATOR = System.getProperty("line.separator");

    private final Context context;

    public RetrofitResponseHandler() {
        this(null);
    }

    public RetrofitResponseHandler(final Context context) {
        this.context = context;
    }


    public abstract void onResponse(final T response);


    public void onFailure(Throwable throwable) {
        if (context == null) {
            return;
        }
        Log.e(TAG, "An error occurred while invoking service", throwable);
        final AlertDialog.Builder alertBuilder = new AlertDialog.Builder(context);
        alertBuilder.setCancelable(true);
        alertBuilder.setPositiveButton("ok", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(final DialogInterface dialog, final int which) {
                dialog.dismiss();
            }
        });
        alertBuilder.show();
    }





}
