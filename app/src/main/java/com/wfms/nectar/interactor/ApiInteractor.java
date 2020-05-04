package com.wfms.nectar.interactor;


import com.google.gson.JsonObject;

/**
 * Created by aashita on 27-Jul-17.
 */

public interface ApiInteractor {
    void onSuccess(JsonObject json);
    void onFailure(String value);
}
