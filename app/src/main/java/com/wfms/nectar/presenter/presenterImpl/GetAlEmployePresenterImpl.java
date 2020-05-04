package com.wfms.nectar.presenter.presenterImpl;

import android.text.TextUtils;
import android.util.Log;


import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.wfms.nectar.interactor.ApiInteractor;
import com.wfms.nectar.interactor.Interactor;
import com.wfms.nectar.interactor.interactorImpl.GetAllFuelHIstoryInteractorImpl;
import com.wfms.nectar.jsonModelResponses.Fuel.EmployeResponse;
import com.wfms.nectar.presenter.Presenter;
import com.wfms.nectar.viewstate.EmployeView;


public class GetAlEmployePresenterImpl implements Presenter,ApiInteractor {

    private static final String TAG = GetAlEmployePresenterImpl.class.getSimpleName();
    private final EmployeView mView;
    private Interactor mSupplierInteractor;

    public GetAlEmployePresenterImpl(EmployeView view) {
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

                EmployeResponse supplierResponse = new Gson().fromJson(json, EmployeResponse.class);

                if (supplierResponse != null && supplierResponse.getStatus().equalsIgnoreCase("success")) {
                    mView.onGetEmployeListSuccess(supplierResponse);
                    return;
                }else {
                    mView.onGetEmployeListListFailure(supplierResponse.getMsg());
                    return;
                }
            }
        } catch (Exception e) {
            Log.d(TAG, e.getMessage().toString());
        }
    }

    @Override
    public void onFailure(String value) {
        mView.onGetEmployeListListFailure("");
    }
}
