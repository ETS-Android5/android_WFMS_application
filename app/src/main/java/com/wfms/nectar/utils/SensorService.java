package com.wfms.nectar.utils;

import android.Manifest;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Build;
import android.os.Bundle;
import android.os.IBinder;
import android.os.Looper;
import android.preference.PreferenceManager;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.core.app.ActivityCompat;

import com.google.gson.JsonObject;
import com.wfms.nectar.jsonModelResponses.Fuel.EmployeData;
import com.wfms.nectar.jsonModelResponses.signup.SignUpResponse;
import com.wfms.nectar.presenter.presenterImpl.SignUpPresenterImpl;
import com.wfms.nectar.presenter.presenterImpl.SignUpPresenterImplOut;
import com.wfms.nectar.presenter.presenterImpl.SignUpUpdatePresenterImpl;
import com.wfms.nectar.retrofit.CallbackWithRetry;
import com.wfms.nectar.sql.DatabaseHelper;
import com.wfms.nectar.viewstate.SignUpView;
import com.wfms.nectar.viewstate.SignUpViewOut;
import com.wfms.nectar.viewstate.SignUpViewUpdate;
import com.wfms.nectar.viewstate.SignUpViewadminnotification;
import com.wfms.nectar.viewstate.SignUpViewnotification;
import com.wfms.nectar.wfms.R;
import com.wfms.nectar.wfms.WfmslApplication;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Response;

/**
 * Created by kunal on 15/01/2018.
 */
public class SensorService extends Service implements SignUpView, SignUpViewOut, SignUpViewnotification, SignUpViewadminnotification, LocationListener, SignUpViewUpdate {
    protected LocationManager locationManager;
    protected LocationListener locationListener;
    Context con;
    public double latitude=0.0, longitude=0.0;
    String id, address, currenttime, locationURL, deviceiD, isupload, islogin;
    public static final String TAG = SensorService.class.getSimpleName();
    boolean checkGPS = false;
    boolean checkNetwork = false;
    boolean canGetLocation = false;
    Location loc;
    double distance;
    private static final long MIN_DISTANCE_CHANGE_FOR_UPDATES = 10;
    private static final long MIN_TIME_BW_UPDATES = 1000 * 60 * 1;
    ArrayList<EmployeData> localgeofencelist=new ArrayList<>();
    public SensorService(Context applicationContext) {
        con = applicationContext;

    }

    public SensorService() {
    }

    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        super.onStartCommand(intent, flags, startId);
        Log.i(TAG, "here I am!");

        startTimer();
        startTimerforlocation();
        return START_STICKY;
    }


    @RequiresApi(api = Build.VERSION_CODES.M)
    private void getcurrentlocation() {
        locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        if (checkSelfPermission(Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && checkSelfPermission(Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    Activity#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for Activity#requestPermissions for more details.
            return;
        }
        locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 0, this);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        Log.i(TAG, "onDestroy");
        // send new broadcast when service is destroyed.
        // this broadcast restarts the service.
        Intent broadcastIntent = new Intent("uk.ac.shef.oak.ActivityRecognition.RestartSensor");
        sendBroadcast(broadcastIntent);
        stoptimertask();
    }

    private Timer timer;
    private TimerTask timerTask;
    long oldTime = 0;


    public void startTimer() {
        //set a new Timer
        timer = new Timer();
      //  getLocation();
        //initialize the TimerTask's job
        initializeTimerTask();

        //schedule the timer, to wake up every 1 second
        timer.schedule(timerTask, 1000, 1000); //
    }
    public void startTimerforlocation() {
        //set a new Timer
        timer = new Timer();
          //getLocation();
        //initialize the TimerTask's job
        initializeTimerTaskforlocation();

        //schedule the timer, to wake up every 1 second
        timer.schedule(timerTask, 1 * 60 * 1000, 1 * 60 * 1000); //
    }
    /**
     * it sets the timer to print the counter every x seconds
     */
    public void initializeTimerTask() {

        timerTask = new TimerTask() {
            public void run() {
               // getLocation();
             //   loadArray( );
                if (NetworkUtil.isOnline(getApplicationContext())) {
                    senddataTimeInfromSqlite();
                    senddataImagefromSqlite();
                }
            }
        };
    }
    public void initializeTimerTaskforlocation() {
        timerTask = new TimerTask() {
            public void run() {
              //  getLocation();
                //Toast.makeText(getApplicationContext(),"signUpResponse.getEntry()", Toast.LENGTH_LONG).show();
            }
        };
    }
    /**
     * not needed
     */
    public void stoptimertask() {
        //stop the timer, if it's not already null
        if (timer != null) {
            timer.cancel();
            timer = null;
        }
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    private void senddataTimeInfromSqlite() {

        DatabaseHelper databaseHelper = new DatabaseHelper(this);
        List<EmployeData> employdata = new ArrayList<>();
        employdata = databaseHelper.getAllTimeinData();
        for (int i = 0; i < employdata.size(); i++) {

            id = employdata.get(i).getUser_id();
            address = employdata.get(i).getIn_location();
            currenttime = employdata.get(i).getIn_time();
            locationURL = employdata.get(i).getIn_location_url();
            deviceiD = employdata.get(i).getIn_mac_address();
            isupload = employdata.get(i).getIsupload();
            islogin = employdata.get(i).getIslogin();
          //  Log.d("deviceiD", deviceiD);

            if (islogin != null) {
                if (islogin.equals("0")) {

                    initTimeInAPIResources(id, address, currenttime, locationURL, deviceiD, PrefUtils.getKey(this, AppConstants.Clientid), PrefUtils.getKey(this, AppConstants.Api_Token));


                } else if (islogin.equals("1")) {
                    initTimeOutAPIResources(id, address, currenttime, locationURL, deviceiD, PrefUtils.getKey(this, AppConstants.Clientid), PrefUtils.getKey(this, AppConstants.Api_Token));

                }


            }
        }
        if (employdata.size() > 0) {
            DatabaseHelper db = new DatabaseHelper(this);
            db.deletetimetable();

        }
    }

    private void senddataImagefromSqlite() {

        DatabaseHelper databaseHelper = new DatabaseHelper(this);
        List<EmployeData> employdata = new ArrayList<>();
        employdata = databaseHelper.getAllsaveImageData();
        //   Log.d("empploydatasize",""+employdata.size());
        for (int i = 0; i < employdata.size(); i++) {
            String id = employdata.get(i).getUser_id();
            String lat = employdata.get(i).getLatitudee();
            String longi = employdata.get(i).getLongtitude();
            String image = employdata.get(i).getImage();

            uploadimage(id, lat, longi, image);
        }
        if (employdata.size() > 0) {
            DatabaseHelper db = new DatabaseHelper(this);
            db.deleteimagetable();

        }
    }

    @Override
    public void onSignUpSuccess(SignUpResponse signUpResponse) {
        PrefUtils.storeKey(this, AppConstants.IsLogin, "1");

    }

    @Override
    public void onSignUpFailure(String msg) {


    }

    private void initTimeInAPIResources(String userid, String address, String currenttime, String locationURL, String deviceiD, String clientid,String apitoken) {
        SignUpPresenterImpl loginPresenter = new SignUpPresenterImpl(this);
        loginPresenter.callApi(AppConstants.TimeIN, userid, address, currenttime, locationURL, deviceiD, clientid,apitoken);
        Log.d("in", "in");
    }

    private void initTimeOutAPIResources(String userid, String address, String currenttime, String locationURL, String deviceiD, String clientid,String apitoken) {
        SignUpPresenterImplOut loginPresenter = new SignUpPresenterImplOut(this);
        loginPresenter.callApi(AppConstants.TimeOut, userid, address, currenttime, locationURL, deviceiD, clientid,apitoken);
    }

    @Override
    public void onSignUpSuccessOut(SignUpResponse signUpResponse) {
        PrefUtils.storeKey(this, AppConstants.IsLogin, "0");

    }

    @Override
    public void onSignUpFailureOut(String msg) {
        if (msg.equalsIgnoreCase("Record not found")) {

            initTimeOutAPIResources(id, address, currenttime, locationURL, deviceiD, PrefUtils.getKey(this, AppConstants.Clientid),PrefUtils.getKey(this, AppConstants.Api_Token));
        }
    }

    private void uploadimage(String id, String lat, String longi, String image) {
        try {


            MultipartBody.Part fileToUpload = null;

            File filePath = new File(image);
            RequestBody requestFile =
                    RequestBody.create(MediaType.parse("multipart/form-data"), filePath);
            fileToUpload = MultipartBody.Part.createFormData("image" +
                    "", filePath.getName(), requestFile);
            RequestBody user_id = RequestBody.create(MediaType.parse("text/plain"), id);
            RequestBody lati = RequestBody.create(MediaType.parse("text/plain"), "" + lat);
            RequestBody lon = RequestBody.create(MediaType.parse("text/plain"), "" + longi);

            RequestBody clientid = RequestBody.create(MediaType.parse("text/plain"), PrefUtils.getKey(this, AppConstants.Clientid));
            RequestBody apitoken = RequestBody.create(MediaType.parse("text/plain"), PrefUtils.getKey(this, AppConstants.Api_Token));


            Call<JsonObject> call = WfmslApplication.mRetroClient.callUploadInvoiceAPi(user_id,
                    lati, lon, clientid, fileToUpload,apitoken);

            call.enqueue(new CallbackWithRetry<JsonObject>(call) {
                @Override
                public void onResponse(Call<JsonObject> call, Response<JsonObject> response) {

                    Log.d(TAG, "onResponse: " + response.body());


                    if (response.isSuccessful()) {

                        Log.d(TAG, "onResponse: " + response.body());

                        JSONObject jresponse = null;
                        try {
                            jresponse = new JSONObject(response.body().toString());
                            String responseString = jresponse.getString("message");
                            Log.d("responseString", responseString);
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    } else {
                        Toast.makeText(getApplicationContext(), getResources().getString(R.string.try_again), Toast.LENGTH_LONG).show();

                    }
                }

                @Override
                public void onFailure(Call<JsonObject> call, Throwable t) {
                    Toast.makeText(getApplicationContext(), getResources().getString(R.string.try_again), Toast.LENGTH_LONG).show();
                    Log.d(TAG, "toString(): " + t.toString());


                }
            });

        } catch (Exception e) {
            Log.d(TAG, e.getMessage());
        }

    }

    @Override
    public void onSignUpSuccessnotification(SignUpResponse signUpResponse) {

    }

    @Override
    public void onSignUpFailurenotification(String msg) {

    }

    @Override
    public void onSignUpSuccessadminnotification(SignUpResponse signUpResponse) {

    }

    @Override
    public void onSignUpFailureadminnotification(String msg) {

    }

    @Override
    public void onLocationChanged(Location location) {
        latitude = location.getLatitude();
        longitude = location.getLongitude();

        Log.d("sensorlatitude", "" + latitude);
        Log.d("longitude", "" + longitude);
    }

    @Override
    public void onStatusChanged(String s, int i, Bundle bundle) {

    }

    @Override
    public void onProviderEnabled(String s) {

    }

    @Override
    public void onProviderDisabled(String s) {

    }

    private Location getLocation() {
        if (Looper.myLooper() == null)
        {
            Looper.prepare();
        }

        try {
            locationManager = (LocationManager) this
                    .getSystemService(LOCATION_SERVICE);

            // get GPS status
            checkGPS = locationManager
                    .isProviderEnabled(LocationManager.GPS_PROVIDER);

            // get network provider status
            checkNetwork = locationManager
                    .isProviderEnabled(LocationManager.NETWORK_PROVIDER);

            if (!checkGPS && !checkNetwork) {
                Toast.makeText(con, "No Service Provider is available", Toast.LENGTH_SHORT).show();
            } else {
                this.canGetLocation = true;

                // if GPS Enabled get lat/long using GPS Services
                if (checkGPS) {

                    if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                        // TODO: Consider calling
                        //    ActivityCompat#requestPermissions
                        // here to request the missing permissions, and then overriding
                        //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
                        //                                          int[] grantResults)
                        // to handle the case where the user grants the permission. See the documentation
                        // for ActivityCompat#requestPermissions for more details.
                    }
                    locationManager.requestLocationUpdates(
                            LocationManager.GPS_PROVIDER,
                            MIN_TIME_BW_UPDATES,
                            MIN_DISTANCE_CHANGE_FOR_UPDATES, this);
                    if (locationManager != null) {
                        loc = locationManager
                                .getLastKnownLocation(LocationManager.GPS_PROVIDER);
                        if (loc != null) {
                            latitude = loc.getLatitude();
                            longitude = loc.getLongitude();

                        }
                    }


                }


                if (checkNetwork) {


                    if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                        // TODO: Consider calling
                        //    ActivityCompat#requestPermissions
                        // here to request the missing permissions, and then overriding
                        //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
                        //                                          int[] grantResults)
                        // to handle the case where the user grants the permission. See the documentation
                        // for ActivityCompat#requestPermissions for more details.
                    }
                    locationManager.requestLocationUpdates(
                            LocationManager.NETWORK_PROVIDER,
                            MIN_TIME_BW_UPDATES,
                            MIN_DISTANCE_CHANGE_FOR_UPDATES, this);

                    if (locationManager != null) {
                        loc = locationManager
                                .getLastKnownLocation(LocationManager.NETWORK_PROVIDER);

                    }

                    if (loc != null) {
                        latitude = loc.getLatitude();
                        longitude = loc.getLongitude();

                    }
                }

            }
               Log.d("servicelat",""+latitude);
            Log.d("servicelong",""+longitude);
        } catch (Exception e) {
            e.printStackTrace();
        }

        return loc;
    }


    private double distance(double lat1, double lon1, double lat2, double lon2) {
        Location startPoint = new Location("locationA");
        startPoint.setLatitude(lat1);
        startPoint.setLongitude(lon1);

        Location endPoint = new Location("locationA");
        endPoint.setLatitude(lat2);
        endPoint.setLongitude(lon2);

         distance = startPoint.distanceTo(endPoint);
        Log.d("distance",""+distance);
        return distance;
    }

    public  void loadArray( )
    {
        localgeofencelist=new ArrayList<>();
        EmployeData geo = null;
        SharedPreferences mSharedPreference1 =   PreferenceManager.getDefaultSharedPreferences(this);


        localgeofencelist.clear();
        int size = mSharedPreference1.getInt("Status_size", 0);


        for(int i=0;i<size;i++)
        {
            String geofencename= mSharedPreference1.getString("Status_gname" + i, null);
            String address= mSharedPreference1.getString("Status_address" + i, null);
            double lat=Double.parseDouble(mSharedPreference1.getString("Status_lat" + i, null));
            double lon=Double.parseDouble(mSharedPreference1.getString("Status_long" + i, null));
            double radius1=Double.parseDouble(mSharedPreference1.getString("Status_radius" + i, null));
            Log.d("geofencename",""+geofencename);
            String id= mSharedPreference1.getString("Status_geofenceid" + i, null);
            if(latitude!=0.0&&longitude!=0.0)
            {
                distance(latitude,longitude,lat,lon);

            }
            if(distance<=radius1)
            {
                if(PrefUtils.getKey(this, AppConstants.Notification_status)==null)
                {
                    initsendnotificationAPIResources(PrefUtils.getKey(this, AppConstants.UserID),id,"1","entry",PrefUtils.getKey(this, AppConstants.Api_Token));
                }
                else
                if(PrefUtils.getKey(this, AppConstants.Notification_status).equalsIgnoreCase("2"))
                {
                    initsendnotificationAPIResources(PrefUtils.getKey(this, AppConstants.UserID),id,"1","entry",PrefUtils.getKey(this, AppConstants.Api_Token));

                }

            }
            else
            {
                if(PrefUtils.getKey(this, AppConstants.Notification_status)!=null) {
                    if (PrefUtils.getKey(this, AppConstants.Notification_status).equalsIgnoreCase("1")) {
                        initsendnotificationAPIResources(PrefUtils.getKey(this, AppConstants.UserID), id, "1", "exit",PrefUtils.getKey(this, AppConstants.Api_Token));
                    }
                }
            }
        }

    }

    private void initsendnotificationAPIResources(String userid, String geofenceid, String adminid, String type, String apitoken) {
        SignUpUpdatePresenterImpl geofencePresenter = new SignUpUpdatePresenterImpl(this);
        geofencePresenter.callApi(AppConstants.GeofenceNotification, userid, geofenceid, adminid,type,apitoken);

    }


    @Override
    public void onSignUpUpdateSuccess(SignUpResponse signUpResponse) {
     //   Toast.makeText(getApplicationContext(),signUpResponse.getEntry(), Toast.LENGTH_LONG).show();
        PrefUtils.storeKey(this, AppConstants.Notification_status, signUpResponse.getEntry());
    }

    @Override
    public void onSignUpUpdateFailure(String msg) {

    }
}
