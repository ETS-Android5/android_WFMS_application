package com.wfms.nectar.presenter.presenterImpl;

import android.text.TextUtils;
import android.util.Log;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.wfms.nectar.interactor.ApiInteractor;
import com.wfms.nectar.interactor.Interactor;
import com.wfms.nectar.interactor.interactorImpl.GetAllFuelHIstoryInteractorImpl;
import com.wfms.nectar.jsonModelResponses.Fuel.LeaveResponse;
import com.wfms.nectar.presenter.Presenter;
import com.wfms.nectar.viewstate.LeaveView;


public class GetAlLeavePresenterImpl implements Presenter,ApiInteractor {


    private static final String TAG = GetAlLeavePresenterImpl.class.getSimpleName();
    private final LeaveView mView;
    private Interactor mSupplierInteractor;

    public GetAlLeavePresenterImpl(LeaveView view) {
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

                LeaveResponse leaveResponse = new Gson().fromJson(json, LeaveResponse.class);

                if (leaveResponse != null && leaveResponse.getStatus().equalsIgnoreCase("success")) {
                    mView.onGeLeaveListSuccess(leaveResponse);
                    return;
                }else {
                    mView.onGetLeaveListListFailure(leaveResponse.getMsg());
                    return;
                }
            }
        } catch (Exception e) {
            Log.d(TAG, e.getMessage().toString());
        }
    }

    @Override
    public void onFailure(String value) {
        mView.onGetLeaveListListFailure("");
    }
}
