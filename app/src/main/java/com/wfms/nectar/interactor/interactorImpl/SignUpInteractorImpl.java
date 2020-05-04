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


public class SignUpInteractorImpl implements Interactor {
    private static final String TAG = SignUpInteractorImpl.class.getSimpleName();

    @Override
    public void callApi(ApiInteractor apiInteractor, Object... args) {
        String title = (String) args[0];

        if (title.equalsIgnoreCase(AppConstants.LOGIN)) {
            callLoginAPI((String) args[1], (String) args[2], (String) args[3], (String) args[4], apiInteractor);

        } else if (title.equalsIgnoreCase(AppConstants.TimeIN)) {
            callTimeINAPI((String) args[1], (String) args[2], (String) args[3], (String) args[4], (String) args[5], (String) args[6], (String) args[7], apiInteractor);

        } else if (title.equalsIgnoreCase(AppConstants.TimeOut)) {
            callTimeOutAPI((String) args[1], (String) args[2], (String) args[3], (String) args[4], (String) args[5], (String) args[6], (String) args[7], apiInteractor);

        } else if (title.equalsIgnoreCase(AppConstants.Reset_Pin)) {
            callResetPinAPI((String) args[1], (String) args[2], (String) args[3], (String) args[4], (String) args[5],apiInteractor);

        } else if (title.equalsIgnoreCase(AppConstants.Notification)) {
            callNotificationAPI((String) args[1], (String) args[2], (String) args[3], (String) args[4],(String) args[5], apiInteractor);


        } else if (title.equalsIgnoreCase(AppConstants.AdminNotification)) {
            callAdminNotificationAPI((String) args[1], (String) args[2], (String) args[3],(String) args[4], apiInteractor);

        } else if (title.equalsIgnoreCase(AppConstants.updated)) {
            callattandenceupdateAPI((String) args[1], (String) args[2], (String) args[3],apiInteractor);


        } else if (title.equalsIgnoreCase(AppConstants.Logout)) {
            calllogoutAPI((String) args[1], (String) args[2],(String) args[3], apiInteractor);

        } else if (title.equalsIgnoreCase(AppConstants.Client)) {
            callCheckClientAPI((String) args[1], apiInteractor);

        } else if (title.equalsIgnoreCase(AppConstants.NotificationEnable)) {
            callNotificationEnableAPI((String) args[1],(String) args[2],(String) args[3],(String) args[4], apiInteractor);

        }
        else if (title.equalsIgnoreCase(AppConstants.GeofenceNotificationEnable)) {
            callGeofenceNotificationEnableAPI((String) args[1],(String) args[2],(String) args[3], apiInteractor);

        }
        else if (title.equalsIgnoreCase(AppConstants.Attandence)) {
            callAttandenceAPI((String) args[1],(String) args[2],(String) args[3],(String) args[4],(String) args[5],(String) args[6], apiInteractor);

        }
        else if (title.equalsIgnoreCase(AppConstants.Available_Leaves)) {
            callAvailableleavesAPI((String) args[1],(String) args[2],apiInteractor);

        }
        else if (title.equalsIgnoreCase(AppConstants.Available_Leaves1)) {
            callAvailableleavesAPI1((String) args[1],apiInteractor);

        }
        else if (title.equalsIgnoreCase(AppConstants.Applyleaves)) {
            callApplyleavesAPI((String) args[1],(String) args[2],(String) args[3],(String) args[4],(String) args[5],(String) args[6],(String) args[7],(String) args[8],(String) args[9],(String) args[10],(String) args[11], apiInteractor);
        }
        else if (title.equalsIgnoreCase(AppConstants.Leave_Cancel)) {
            callCancelleavesAPI((String) args[1],(String) args[2],(String) args[3],apiInteractor);
        }
        else if (title.equalsIgnoreCase(AppConstants.Applyleaves1)) {
            callApplyleavesAPI1((String) args[1],(String) args[2],(String) args[3],(String) args[4],(String) args[5],(String) args[6],(String) args[7],(String) args[8],(String) args[9],(String) args[10], apiInteractor);
        }
        else if (title.equalsIgnoreCase(AppConstants.Leave_Cancel1)) {
            callCancelleavesAPI1((String) args[1],apiInteractor);
        }
        else if (title.equalsIgnoreCase(AppConstants.CreateGeofence)) {
            callAddGeofencesAPI1((String) args[1],(String) args[2],(String) args[3],(String) args[4],(String) args[5],(String) args[6],(String) args[7],(String) args[8],(String) args[9],(String) args[10],(String) args[11],(String) args[12], apiInteractor);
        }
        else if (title.equalsIgnoreCase(AppConstants.EditGeofence)) {
            callEditGeofencesAPI1((String) args[1],(String) args[2],(String) args[3],(String) args[4],(String) args[5],(String) args[6],(String) args[7],(String) args[8], (String) args[9],(String) args[10],(String) args[11],(String) args[12],(String) args[13],apiInteractor);
        }
        else if (title.equalsIgnoreCase(AppConstants.RemoveGeofence)) {
            callRemoveGeofenceAPI((String) args[1],(String) args[2],(String) args[3], apiInteractor);

        }
        else if (title.equalsIgnoreCase(AppConstants.AssignGeofence)) {
            callAssignGeofenceAPI((String) args[1],(String) args[2],(String) args[3],(String) args[4],(String) args[5], apiInteractor);
        }
        else if (title.equalsIgnoreCase(AppConstants.DAssignGeofence)) {
            callDAssignGeofenceAPI((String) args[1],(String) args[2],(String) args[3],(String) args[4],(String) args[5], apiInteractor);
        }
        else if (title.equalsIgnoreCase(AppConstants.GeofenceNotification)) {
            callAdminGeofenceNotificationAPI((String) args[1], (String) args[2], (String) args[3],(String) args[4],(String) args[5], apiInteractor);
        }
        else if (title.equalsIgnoreCase(AppConstants.SingleGeofenceNotification)) {
            callSingleGeofenceNotificationAPI((String) args[1], (String) args[2], (String) args[3],(String) args[4], apiInteractor);

        }


    }



    private void callSingleGeofenceNotificationAPI(String id, String status, String adminid,String apitoken, ApiInteractor apiInteractor) {
        Call<JsonObject> call = WfmslApplication.mRetroClient.callSingleGeofenceNotificatrionAPI1(id,status,adminid,apitoken);
        requestCall(call,apiInteractor);
    }

    private void callAdminGeofenceNotificationAPI(String userid, String geofenceid, String adminid,String type,String apitoken, ApiInteractor apiInteractor) {
        Call<JsonObject> call = WfmslApplication.mRetroClient.callAdminGeofenceNotificatrionAPI1(userid,geofenceid,adminid,type,apitoken);
        requestCall(call,apiInteractor);
    }

    private void callAssignGeofenceAPI(String employid, String clientid, String geofenceid, String userid,String apitoken, ApiInteractor apiInteractor) {
        Call<JsonObject> call = WfmslApplication.mRetroClient.callAssigneGeofenceAPI1(employid,clientid,geofenceid,userid,apitoken);
        requestCall(call,apiInteractor);
    }
    private void callDAssignGeofenceAPI(String employid, String clientid, String geofenceid, String userid,String apitoken, ApiInteractor apiInteractor) {
        Call<JsonObject> call = WfmslApplication.mRetroClient.callDAssigneGeofenceAPI1(employid,clientid,geofenceid,userid,apitoken);
        requestCall(call,apiInteractor);
    }
    private void callEditGeofencesAPI1(String geofencename, String lat, String longi, String radius, String addreess, String area, String ClientName, String userid, String geofenceid,String end_lat,String end_long,String end_addres, String apitoken,ApiInteractor apiInteractor) {
        Call<JsonObject> call = WfmslApplication.mRetroClient.callUpdateGeofenceAPI1(geofencename,lat,longi,radius,addreess,area,ClientName,userid,geofenceid,end_lat,end_long,end_addres,apitoken);
        requestCall(call,apiInteractor);
    }
    private void callAddGeofencesAPI1(String geofencename, String lat, String longi, String radius, String addreess, String area, String ClientName, String userid,String end_lat, String end_long, String end_address,String apitoken, ApiInteractor apiInteractor) {
        Call<JsonObject> call = WfmslApplication.mRetroClient.callAddGeofenceAPI1(geofencename,lat,longi,radius,addreess,area,ClientName,userid,end_lat,end_long,end_address,apitoken);
        requestCall(call,apiInteractor);
    }
    private void callCancelleavesAPI(String leaveid,String apitoken,String email, ApiInteractor apiInteractor) {
        Call<JsonObject> call = WfmslApplication.mRetroClient.calCancelLeaveAPI(leaveid,apitoken,email);
        requestCall(call,apiInteractor);
    }
    private void callCancelleavesAPI1(String leaveid, ApiInteractor apiInteractor) {
        Call<JsonObject> call = WfmslApplication.mRetroClient.calCancelLeaveAPI1(leaveid);
        requestCall(call,apiInteractor);
    }

    private void callNotificationEnableAPI(String userid, String clientid, String isnotification,String apitoken, ApiInteractor mListener) {

    Call<JsonObject> call = WfmslApplication.mRetroClient.callNotificationtAPI(userid,clientid,isnotification,apitoken);
    requestCall(call,mListener);
    }


    private void callGeofenceNotificationEnableAPI(String userid, String isnotification, String apitoken,ApiInteractor mListener) {

        Call<JsonObject> call = WfmslApplication.mRetroClient.callGeofenceNotificationtAPI(userid,isnotification,apitoken);
        requestCall(call,mListener);
    }
    private void callAvailableleavesAPI(String username,String appitoken, ApiInteractor mListener) {

        Call<JsonObject> call = WfmslApplication.mRetroClient.callavailableleavesAPI(username,appitoken);
        requestCall(call,mListener);
    }

    private void callAvailableleavesAPI1(String username, ApiInteractor mListener) {

        Call<JsonObject> call = WfmslApplication.mRetroClient.callavailableleavesAPI1(username);
        requestCall(call,mListener);
    }

    private void callApplyleavesAPI(String email, String available_leaves, String leave_type, String reason, String from, String to, String leave_for,String day, String rep_manager, String appliedleavescount,String apitoken,ApiInteractor mListener) {

        Call<JsonObject> call = WfmslApplication.mRetroClient.callAppliedeavesAPI(email,available_leaves,leave_type,reason,from,to,leave_for,day,rep_manager,appliedleavescount,apitoken);
        requestCall(call,mListener);
    }

    private void callApplyleavesAPI1(String email, String available_leaves, String leave_type, String reason, String from, String to, String leave_for,String day, String rep_manager, String appliedleavescount,ApiInteractor mListener) {

        Call<JsonObject> call = WfmslApplication.mRetroClient.callAppliedeavesAPI1(email,available_leaves,leave_type,reason,from,to,leave_for,day,rep_manager,appliedleavescount);
        requestCall(call,mListener);
    }

    public void callTimeINAPI(String userid, String address, String time, String locationurl,String deviceID, String clientid,String apitoken,final ApiInteractor mListener) {

      Call<JsonObject> call = WfmslApplication.mRetroClient.callTimeInPI(userid,address,time,locationurl,deviceID,clientid,apitoken);
      requestCall(call,mListener);
  }
    public void callTimeOutAPI(String userid, String address, String time,String locationurl, String deviceID, String clientid,String apitoken,final ApiInteractor mListener) {

        Call<JsonObject> call = WfmslApplication.mRetroClient.callTimeOutAPI(userid,address,time,locationurl,deviceID,clientid,apitoken);
        requestCall(call,mListener);
    }
    public void callResetPinAPI(String oldpassword, String password, String userid,String clientid,String apitoken,final ApiInteractor mListener) {

        Call<JsonObject> call = WfmslApplication.mRetroClient.callResetPinAPI(oldpassword,password,userid,clientid,apitoken);
        requestCall(call,mListener);
    }
    public void callLoginAPI( String name, String password,String tokenid,String clientname, final ApiInteractor mListener) {

        Call<JsonObject> call = WfmslApplication.mRetroClient.callLoginAPI(name,password,tokenid,clientname);
        requestCall(call,mListener);
    }
    public void callNotificationAPI(String userid,String tokenid, String currenttime,String clientid,String apitoken,final ApiInteractor mListener) {

        Call<JsonObject> call = WfmslApplication.mRetroClient.callNotificationAPI(userid,tokenid,currenttime,clientid,apitoken);
        requestCall(call,mListener);
    }

    public void callAdminNotificationAPI(String userid, String username,String clientid,String apitoken,final ApiInteractor mListener) {

        Call<JsonObject> call = WfmslApplication.mRetroClient.callAdminNotificationAPI(userid,username,clientid,apitoken);
        requestCall(call,mListener);
    }
    public void callattandenceupdateAPI(String userid,String clientid,String apitoken,final ApiInteractor mListener) {

        Call<JsonObject> call = WfmslApplication.mRetroClient.callEmployeattandenceAPI(userid,clientid,apitoken);
        requestCall(call,mListener);
    }
    public void calllogoutAPI(String userid,String clienttid,String apitoken,final ApiInteractor mListener) {

        Call<JsonObject> call = WfmslApplication.mRetroClient.callLogoutAPI(userid,clienttid,apitoken);
        requestCall(call,mListener);
    }
    public void callCheckClientAPI( String clientname, final ApiInteractor mListener) {

        Call<JsonObject> call = WfmslApplication.mRetroClient.callCheckClientAPI(clientname);
        requestCall(call,mListener);
    }
    public void callRemoveGeofenceAPI( String geofenceid,String apitoken,String userid, final ApiInteractor mListener) {

        Call<JsonObject> call = WfmslApplication.mRetroClient.callRemoveGeofencetAPI(geofenceid,apitoken,userid);
        requestCall(call,mListener);
    }
    private void callAttandenceAPI(String userid, String clientid, String monthid,String year, String apitoken,String selecteduserid,ApiInteractor mListener) {

        Call<JsonObject> call = WfmslApplication.mRetroClient.callAttandenceAPI(userid,clientid,monthid,year,apitoken,selecteduserid);
        requestCall(call,mListener);
    }
  private void requestCall(Call<JsonObject> call, final ApiInteractor mListener){
      Log.d(TAG, call.request().toString());
      call.enqueue(new CallbackWithRetry<JsonObject>(call) {
          @Override
          public void onResponse(Call<JsonObject> call, Response<JsonObject> response) {
              Log.d(TAG, "onResponse: " + response);
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


