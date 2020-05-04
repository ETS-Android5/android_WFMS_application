package com.wfms.nectar.retrofit;

import android.util.Log;

import retrofit2.Call;
import retrofit2.Callback;

/**
 * Created by aashita on 25/07/17.
 * This abstract class send requests to server upto total retries.
 */

public abstract class CallbackWithRetry<T> implements Callback<T> {

    private static final int TOTAL_RETRIES = 3;
    private static final String TAG = CallbackWithRetry.class.getSimpleName();
    private final Call<T> call;
    private RetrofitResponseHandler<T> mHandler = null;
    private int retryCount = 0;

    public CallbackWithRetry(Call<T> call, RetrofitResponseHandler<T> responseHandler) {
        this.call = call;
        // this.mHandler = responseHandler;
    }

    public CallbackWithRetry(Call<T> call) {
        this.call = call;
    }

    private void retry() {
        call.clone().enqueue(this);
    }

    @Override
    public void onFailure(Call<T> call, Throwable t) {
        Log.e(TAG, t.getLocalizedMessage());
        if (retryCount++ < TOTAL_RETRIES) {
            Log.v(TAG, "Retrying... (" + retryCount + " out of " + TOTAL_RETRIES + ")");
            retry();
        }

    }

    public boolean onFailureResponse(Call<T> call, Throwable t) {
        // Log.e(TAG, t.getLocalizedMessage());
        if (retryCount++ < TOTAL_RETRIES) {
            Log.v(TAG, "Retrying... (" + retryCount + " out of " + TOTAL_RETRIES + ")");
            retry();
            return true;
        }
        return false;

    }


}
