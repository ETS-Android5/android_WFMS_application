package com.wfms.nectar.presenter.presenterImpl;

import android.text.TextUtils;
import android.util.Log;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.wfms.nectar.interactor.ApiInteractor;
import com.wfms.nectar.interactor.Interactor;
import com.wfms.nectar.interactor.interactorImpl.GetAllFuelHIstoryInteractorImpl;
import com.wfms.nectar.jsonModelResponses.Fuel.LocationResponse;
import com.wfms.nectar.presenter.Presenter;
import com.wfms.nectar.viewstate.LocationView;


public class GetAllLocationPresenterImpl implements Presenter,ApiInteractor {

    private static final String TAG = GetAllLocationPresenterImpl.class.getSimpleName();
    private final LocationView mView;
    private Interactor mSupplierInteractor;

    public GetAllLocationPresenterImpl(LocationView view) {
        this.mView = view;
        mSupplierInteractor = new GetAllFuelHIstoryInteractorImpl();
    }


    @Override
    public void callApi(Object... args) {
        mSupplierInteractor.callApi(this,args);
    }


    @Override
    public void onSuccess(JsonObject json) {

        try {
            if (!TextUtils.isEmpty(json.toString())) {

                LocationResponse locationResponse = new Gson().fromJson(json, LocationResponse.class);

                if (locationResponse != null && locationResponse.getStatus().equalsIgnoreCase("success")) {
                    mView.onGetLocationListSuccess(locationResponse);
                    return;
                }else {
                    mView.onGetlocationListFailure(locationResponse.getMsg());
                    return;
                }
            }
        } catch (Exception e) {
            Log.d(TAG, e.getMessage().toString());
        }
    }

    @Override
    public void onFailure(String value) {
        mView.onGetlocationListFailure("");
    }
}
