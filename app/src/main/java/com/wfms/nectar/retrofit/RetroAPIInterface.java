package com.wfms.nectar.retrofit;

import com.google.gson.JsonObject;
import com.wfms.nectar.utils.AppConstants;
import com.wfms.nectar.wfms.LoginActivity;
import com.wfms.nectar.wfms.SplashActivity;

import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.Headers;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;

/**
 * Created by abhishek on 25/07/17.
 */

public interface RetroAPIInterface {
    String BASE_URL = SplashActivity.validurl;

// jsonschema2pojo.org
    /**
     * User Login and Registration
     */

    @FormUrlEncoded
 //   @Headers("user-key: "+ LoginActivity.auth)
    @POST( AppConstants.BASE_URL+"login")
    Call<JsonObject> callLoginAPI(@Field("username") String username, @Field("password") String password, @Field("tokenid") String tokenid, @Field("clientname") String clientname);

    @FormUrlEncoded
    @POST( "attendance/in")
    Call<JsonObject> callTimeInPI(@Field("user_id") String userid, @Field("in_location") String address, @Field("in_time") String time, @Field("in_location_url") String locationurl, @Field("in_mac_address") String in_mac_address, @Field("client_id") String client_id,@Field("api_token") String api_token);


    @FormUrlEncoded
    @POST( "attendance/out")
    Call<JsonObject> callTimeOutAPI(@Field("user_id") String userid, @Field("out_location") String address, @Field("out_time") String time, @Field("out_location_url") String locationurl, @Field("out_mac_address") String out_mac_address, @Field("client_id") String client_id,@Field("api_token") String api_token);

    @FormUrlEncoded
    @POST( "user/reset")
    Call<JsonObject> callResetPinAPI(@Field("old_pin") String username, @Field("new_pin") String password, @Field("user_id") String userid, @Field("client_id") String client_id,@Field("api_token") String api_token);

    @FormUrlEncoded
    @POST("test")
    Call<JsonObject> callNotificationAPI(@Field("userid") String user_id, @Field("token") String token, @Field("TimeIn") String TimeIn, @Field("client_id") String client_id,@Field("api_token") String api_token);

    @FormUrlEncoded
    @POST("admin_notification")
    Call<JsonObject> callAdminNotificationAPI(@Field("user_id") String token, @Field("user_name") String TimeIn, @Field("client_id") String client_id,@Field("api_token") String api_token);


    @FormUrlEncoded
    @POST("attendance/list")
    Call<JsonObject> callEmployeListAPI(@Field("user_id") String user_id, @Field("last_id") String last_id, @Field("client_id") String client_id,@Field("api_token") String api_token);

    @FormUrlEncoded
    @POST("datageofence")
    Call<JsonObject> callGeofenceListAPI(@Field("user_id") String user_id, @Field("client_id") String client_id,@Field("admin_id") String admin_id,@Field("api_token") String api_token);

    @FormUrlEncoded
    @POST("usergeofence")
    Call<JsonObject> callEmployeGeofenceListAPI(@Field("user_id") String user_id, @Field("client_id") String client_id,@Field("api_token") String api_token);
    @FormUrlEncoded
    @POST("attendance_filter")
    Call<JsonObject> callEmployeFilterListAPI(@Field("user_id") String user_id, @Field("date") String date, @Field("last_id") String last_id, @Field("client_id") String client_id,@Field("api_token") String api_token);

    @FormUrlEncoded

    @POST("attendance_status")
    Call<JsonObject> callEmployeattandenceAPI(@Field("user_id") String user_id, @Field("client_id") String client_id, @Field("api_token") String api_token);

    @FormUrlEncoded
    @POST("user/reset_token")
    Call<JsonObject> callLogoutAPI(@Field("user_id") String user_id, @Field("client_id") String client_id, @Field("api_token") String api_token);

    @FormUrlEncoded
    @POST("client_detail")
    Call<JsonObject> callCheckClientAPI(@Field("clientname") String clientname);

    @FormUrlEncoded
    @POST("isnotification")
    Call<JsonObject> callNotificationtAPI(@Field("user_id") String user_id, @Field("client_id") String client_id, @Field("isnotification") String isnotification,@Field("api_token") String api_token);

    @FormUrlEncoded
    @POST("allgeonotification")
    Call<JsonObject> callGeofenceNotificationtAPI(@Field("admin_id") String user_id, @Field("status") String isnotification,@Field("api_token") String api_token);


    @FormUrlEncoded
    @POST("piechart")
    Call<JsonObject> callAttandenceAPI(@Field("user_id") String user_id, @Field("client_id") String client_id, @Field("month_id") String month_id, @Field("year_id") String year_id,@Field("api_token") String api_token,@Field("selected_id") String selected_id);

    @FormUrlEncoded
    @POST("user/toadmin_detail")
    Call<JsonObject> callUserAPI(@Field("client_id") String client_id,@Field("api_token") String api_token);

    @FormUrlEncoded
    @POST("pending_leaves")
    Call<JsonObject> callpendingleavesAPI(@Field("email") String email,@Field("api_token") String api_token);

    @FormUrlEncoded
    @POST("pending_leaves")
    Call<JsonObject> callpendingleavesAPI1(@Field("email") String email);

    @FormUrlEncoded
    @POST("approved_leaves")
    Call<JsonObject> callApprovedleavesAPI(@Field("email") String email,@Field("api_token") String api_token);

    @FormUrlEncoded
    @POST("approved_leaves")
    Call<JsonObject> callApprovedleavesAPI1(@Field("email") String email);

    @FormUrlEncoded
    @POST("canceled_leaves")
    Call<JsonObject> callCancelleavesAPI(@Field("email") String email);

    @FormUrlEncoded
    @POST("canceled_leaves")
    Call<JsonObject> callCancelleavesAPI1(@Field("email") String email);


    @FormUrlEncoded
    @POST("available_leaves")
    Call<JsonObject> callavailableleavesAPI(@Field("email") String email,@Field("api_token") String api_token);

    @FormUrlEncoded
    @POST("available_leaves")
    Call<JsonObject> callavailableleavesAPI1(@Field("email") String email);


    @FormUrlEncoded
    @POST("rejected_leaves")
    Call<JsonObject> callRejectedleavesAPI(@Field("email") String email,@Field("api_token") String api_token);


    @FormUrlEncoded
    @POST("rejected_leaves")
    Call<JsonObject> callRejectedleavesAPI1(@Field("email") String email);

    @FormUrlEncoded
    @POST("cancelling_leave")
    Call<JsonObject> calCancelLeaveAPI(@Field("leaveid") String leaveid,@Field("api_token") String api_token,@Field("email") String email);

    @FormUrlEncoded
    @POST("cancelling_leave")
    Call<JsonObject> calCancelLeaveAPI1(@Field("leaveid") String leaveid);

    @Multipart
    @POST("user_detail")
    Call<JsonObject> callUploadInvoiceAPi(@Part("user_id") RequestBody userId, @Part("lattitude") RequestBody lattitude,
                                          @Part("longitude") RequestBody longitude,
                                          @Part("client_id") RequestBody client_id,
                                          @Part MultipartBody.Part file,
                                          @Part("api_token") RequestBody apitoken
    );

    @FormUrlEncoded
    @POST("leaverequest")
    Call<JsonObject> callAppliedeavesAPI(@Field("email") String email, @Field("available_leaves") String available_leaves,
                                         @Field("leave_type") String leave_type,
                                         @Field("reason") String reason,
                                         @Field("from") String from,
                                         @Field("to") String to,
                                         @Field("leave_for") String leave_for,
                                         @Field("day") String day,
                                         @Field("rep_manager") String rep_manager,
                                         @Field("appliedleavescount") String appliedleavescount,
                                         @Field("api_token") String api_token

    );



    @FormUrlEncoded
    @POST("leaverequest")
    Call<JsonObject> callAppliedeavesAPI1(@Field("email") String email, @Field("available_leaves") String available_leaves,
                                          @Field("leave_type") String leave_type,
                                          @Field("reason") String reason,
                                          @Field("from") String from,
                                          @Field("to") String to,
                                          @Field("leave_for") String leave_for,
                                          @Field("day") String day,
                                          @Field("rep_manager") String rep_manager,
                                          @Field("appliedleavescount") String appliedleavescount

    );

    @FormUrlEncoded
    @POST("addgeofence")
    Call<JsonObject> callAddGeofenceAPI1(@Field("geofencename") String geofencename, @Field("latitude") String latitude,
                                         @Field("longitude") String longitude,
                                         @Field("radius") String radius,
                                         @Field("address") String address,
                                         @Field("type") String type,
                                         @Field("client_id") String client_id,
                                         @Field("user_id") String user_id,
                                         @Field("dest_latitude") String dest_latitude,
                                         @Field("dest_longitude") String dest_longitude,
                                         @Field("dest_address") String dest_address,
                                         @Field("api_token") String api_token);



    @FormUrlEncoded
    @POST("removegeofence")
    Call<JsonObject> callRemoveGeofencetAPI(@Field("geofence_id") String clientname,
                                              @Field("api_token") String api_token,
                                              @Field("user_id") String user_id);
    @FormUrlEncoded
    @POST("updategeofence")
    Call<JsonObject> callUpdateGeofenceAPI1(@Field("geofencename") String geofencename, @Field("latitude") String latitude,
                                            @Field("longitude") String longitude,
                                            @Field("radius") String radius,
                                            @Field("address") String address,
                                            @Field("type") String type,
                                            @Field("client_id") String client_id,
                                            @Field("user_id") String user_id,
                                            @Field("geofenceid") String geofenceid,
                                            @Field("dest_latitude") String dest_latitude,
                                            @Field("dest_longitude") String dest_longitude,
                                            @Field("dest_address") String dest_address,
                                            @Field("api_token") String api_token
    );

    @FormUrlEncoded
    @POST("assigngeofence")
    Call<JsonObject> callAssigneGeofenceAPI1(@Field("user_id") String geofencename, @Field("client_id") String latitude,
                                             @Field("geofence_id") String longitude,
                                             @Field("admin_id") String radius,
                                             @Field("api_token") String api_token


    );
    @FormUrlEncoded
    @POST("unassigngeofence")
    Call<JsonObject> callDAssigneGeofenceAPI1(@Field("user_id") String geofencename, @Field("client_id") String latitude,
                                             @Field("geofence_id") String longitude,
                                             @Field("admin_id") String radius, @Field("api_token") String api_token


    );
    @FormUrlEncoded
    @POST("notificationgeo")
    Call<JsonObject> callAdminGeofenceNotificatrionAPI1(@Field("user_id") String user_id, @Field("geofence_id") String geofence_id,
                                                        @Field("admin_id") String admin_id,
                                                        @Field("type") String type,
                                                        @Field("api_token") String api_token
    );

    @FormUrlEncoded
    @POST("singlegeonotification")
    Call<JsonObject> callSingleGeofenceNotificatrionAPI1(@Field("geofence_id") String geofence_id, @Field("status") String status,
                                                         @Field("admin_id") String admin_id,
                                                         @Field("api_token") String api_token
    );

    @FormUrlEncoded
    @POST("notificationhistry")
    Call<JsonObject> callGeofencehistoryApiAPI1(@Field("admin_id") String admin_id, @Field("user_id") String user_id,
                                              @Field("api_token") String api_token    );


}