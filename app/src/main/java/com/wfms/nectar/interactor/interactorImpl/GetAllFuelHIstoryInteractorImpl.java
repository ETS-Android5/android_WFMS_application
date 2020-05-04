package com.wfms.nectar.interactor.interactorImpl;

import android.util.Log;

import com.google.gson.JsonObject;
import com.wfms.nectar.interactor.ApiInteractor;
import com.wfms.nectar.interactor.Interactor;
import com.wfms.nectar.retrofit.CallbackWithRetry;
import com.wfms.nectar.utils.AppConstants;
import com.wfms.nectar.wfms.WfmslApplication;

import retrofit2.Call;
import retrofit2.Response;

public class GetAllFuelHIstoryInteractorImpl implements Interactor {
    private static final String TAG = GetAllFuelHIstoryInteractorImpl.class.getSimpleName();

    @Override
    public void callApi(ApiInteractor apiInteractor, Object... args) {
        String title = (String)args[0];
        if(title.equalsIgnoreCase(AppConstants.Employee_List)) {
            callGetAllEmployeAPI((String)args[1],(String)args[2],(String)args[3],(String)args[4],apiInteractor);
        }
        else  if(title.equalsIgnoreCase(AppConstants.Filter_Employee_List)) {
            callGetAllEmployeFilterAPI((String)args[1],(String)args[2],(String)args[3],(String)args[4],(String)args[5],apiInteractor);
        }
        else  if(title.equalsIgnoreCase(AppConstants.User_List)) {
            callGetAllUserAPI((String)args[1],(String)args[2],apiInteractor);
        }
        else  if(title.equalsIgnoreCase(AppConstants.Pending_List)) {
            callGetAlPendingAPI((String)args[1],(String)args[2],apiInteractor);
        }
        else  if(title.equalsIgnoreCase(AppConstants.Pending_List1)) {
            callGetAlPendingAPI1((String)args[1],apiInteractor);
        }
        else  if(title.equalsIgnoreCase(AppConstants.Approved_List)) {
            callGetAlApprovedAPI((String)args[1],(String)args[2],apiInteractor);
        }
        else  if(title.equalsIgnoreCase(AppConstants.Approved_List1)) {
            callGetAlApprovedAPI1((String)args[1],apiInteractor);
        }
        else  if(title.equalsIgnoreCase(AppConstants.Rejected_List)) {
            callGetAlRejectedAPI((String)args[1],(String)args[2],apiInteractor);
        }
        else  if(title.equalsIgnoreCase(AppConstants.Rejected_List1)) {
            callGetAlRejectedAPI1((String)args[1],apiInteractor);
        }
        else  if(title.equalsIgnoreCase(AppConstants.Geofence_List)) {
            callGetAlGeofenceAPI1((String)args[1],(String)args[2],(String)args[3],(String)args[4],apiInteractor);
        }
        else  if(title.equalsIgnoreCase(AppConstants.Employe_Geofence_List)) {
            callGetAlEmployeGeofenceAPI1((String)args[1],(String)args[2],(String)args[3],apiInteractor);
        }
        else if (title.equalsIgnoreCase(AppConstants.GeofenceHistory)) {
            callGeofenceHIstoryAPI((String) args[1], (String) args[2], (String) args[3],(String) args[4], apiInteractor);

        }
    }
    private void callGeofenceHIstoryAPI(String adminid, String userid, String clientid, String apitoken, ApiInteractor apiInteractor) {
        Call<JsonObject> call = WfmslApplication.mRetroClient.callGeofencehistoryApiAPI1(adminid,userid,apitoken);
        requestCall(call,apiInteractor);
    }
    private void callGetAlGeofenceAPI1(String userID, String clientid,String adminid,String apitoken, ApiInteractor apiInteractor) {
        Call<JsonObject> call = WfmslApplication.mRetroClient.callGeofenceListAPI(userID,clientid,adminid,apitoken);
        requestCall(call,apiInteractor);
    }

    private void callGetAlEmployeGeofenceAPI1(String userID, String clientid, String apitoken,ApiInteractor apiInteractor) {
        Call<JsonObject> call = WfmslApplication.mRetroClient.callEmployeGeofenceListAPI(userID,clientid,apitoken);
        requestCall(call,apiInteractor);
    }

    public void callGetAllEmployeAPI(String userID, String AttandenceID, String clientid,String apitoken, final ApiInteractor mListener) {

        Call<JsonObject> call = WfmslApplication.mRetroClient.callEmployeListAPI(userID,AttandenceID,clientid,apitoken);
        requestCall(call,mListener);
    }
    public void callGetAlPendingAPI(String email,String apitoken, final ApiInteractor mListener) {

        Call<JsonObject> call = WfmslApplication.mRetroClient.callpendingleavesAPI(email,apitoken);
        requestCall(call,mListener);
    }

    public void callGetAlPendingAPI1(String email, final ApiInteractor mListener) {

        Call<JsonObject> call = WfmslApplication.mRetroClient.callpendingleavesAPI1(email);
        requestCall(call,mListener);
    }
    public void callGetAlRejectedAPI(String email,String apitoken, final ApiInteractor mListener) {

        Call<JsonObject> call = WfmslApplication.mRetroClient.callRejectedleavesAPI(email,apitoken);
        requestCall(call,mListener);
    }
    public void callGetAlRejectedAPI1(String email, final ApiInteractor mListener) {

        Call<JsonObject> call = WfmslApplication.mRetroClient.callRejectedleavesAPI1(email);
        requestCall(call,mListener);
    }
    public void callGetAlApprovedAPI(String email,String apitoken, final ApiInteractor mListener) {

        Call<JsonObject> call = WfmslApplication.mRetroClient.callApprovedleavesAPI(email,apitoken);
        requestCall(call,mListener);
    }

    public void callGetAlApprovedAPI1(String email, final ApiInteractor mListener) {

    }
    public void callGetAllUserAPI(String clientid, String apitoken,final ApiInteractor mListener) {

        Call<JsonObject> call = WfmslApplication.mRetroClient.callUserAPI(clientid,apitoken);
        requestCall(call,mListener);
    }
    public void callGetAlCancelAPI(String email, final ApiInteractor mListener) {

        Call<JsonObject> call = WfmslApplication.mRetroClient.callCancelleavesAPI(email);
        requestCall(call,mListener);
    }
    public void callGetAllEmployeFilterAPI(String userID, String date, String attandenceid, String clientid,String apitoken, final ApiInteractor mListener) {

        Call<JsonObject> call = WfmslApplication.mRetroClient.callEmployeFilterListAPI(userID,date,attandenceid,clientid,apitoken);
        requestCall(call,mListener);
    }

    private void requestCall(Call<JsonObject> call, final ApiInteractor mListener){
        Log.d(TAG, call.request().toString());
        call.enqueue(new CallbackWithRetry<JsonObject>(call) {
            @Override
            public void onResponse(Call<JsonObject> call, Response<JsonObject> response) {

                if (response.isSuccessful()) {
                    Log.d(TAG, "onResponse: " + response.body());
                    mListener.onSuccess(response.body());
                } else {
                    if (response.errorBody() != null) {
                        mListener.onFailure("");
                    } else {
                        onFailure(call, new Throwable());
                    }
                }
            }

            @Override
            public void onFailure(Call<JsonObject> call, Throwable t) {
                if (!onFailureResponse(call, t)) {
                    mListener.onFailure("");
                }
            }
        });
    }
}