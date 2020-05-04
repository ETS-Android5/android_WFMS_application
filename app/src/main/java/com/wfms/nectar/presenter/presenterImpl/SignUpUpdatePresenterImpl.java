package com.wfms.nectar.presenter.presenterImpl;

import android.text.TextUtils;
import android.util.Log;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.wfms.nectar.interactor.ApiInteractor;
import com.wfms.nectar.interactor.Interactor;
import com.wfms.nectar.interactor.interactorImpl.SignUpInteractorImpl;
import com.wfms.nectar.jsonModelResponses.signup.SignUpResponse;
import com.wfms.nectar.presenter.Presenter;
import com.wfms.nectar.viewstate.SignUpView;
import com.wfms.nectar.viewstate.SignUpViewUpdate;


public class SignUpUpdatePresenterImpl implements Presenter,ApiInteractor {

    private static final String TAG = SignUpUpdatePresenterImpl.class.getSimpleName();
    private final SignUpViewUpdate mView;
    private Interactor mSignUpInteractor;

    public SignUpUpdatePresenterImpl(SignUpViewUpdate view) {
        this.mView = view;
        mSignUpInteractor = new SignUpInteractorImpl();
    }


    @Override
    public void callApi(Object... args) {
        mSignUpInteractor.callApi(this,args);
    }


    @Override
    public void onSuccess(JsonObject json) {

        try {

            if (!TextUtils.isEmpty(json.toString())) {

                SignUpResponse signUpResponse = new Gson().fromJson(json, SignUpResponse.class);

                if (signUpResponse != null && signUpResponse.getStatus().equalsIgnoreCase("success")) {
                    mView.onSignUpUpdateSuccess(signUpResponse);
                  //  PrefManager.getActiveInstance(TLoginActivity.context).setUseremail("");

                    return;
                }else {
                    mView.onSignUpUpdateFailure(signUpResponse.getMsg());
                    return;
                }
            }
        } catch (Exception e) {
            Log.d(TAG, e.getMessage().toString());
        }
    }

    @Override
    public void onFailure(String value) {
        mView.onSignUpUpdateFailure("");
    }
}
