package com.wfms.nectar.wfms;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.Dialog;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.os.Build;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesUtil;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.location.GeofencingRequest;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.wfms.nectar.Adapter.Geofencelist_Adapter;
import com.wfms.nectar.Adapter.User_Adapter;
import com.wfms.nectar.jsonModelResponses.Fuel.EmployeData;
import com.wfms.nectar.jsonModelResponses.Fuel.EmployeResponse;
import com.wfms.nectar.jsonModelResponses.signup.SignUpResponse;
import com.wfms.nectar.presenter.presenterImpl.GetAlEmployePresenterImpl;
import com.wfms.nectar.presenter.presenterImpl.SignUpPresenterImpl;
import com.wfms.nectar.utils.AppConstants;
import com.wfms.nectar.utils.DividerItemDecoration;
import com.wfms.nectar.utils.NetworkUtil;
import com.wfms.nectar.utils.PrefUtils;
import com.wfms.nectar.viewstate.EmployeView;
import com.wfms.nectar.viewstate.SignUpView;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.UUID;

public class CreateGeofenceActivity extends AppCompatActivity implements LocationListener,
        AdapterView.OnItemSelectedListener, SignUpView, EmployeView {
    RelativeLayout history_layout;
    Spinner userlist;
    String area_value,employeename;
    SwipeRefreshLayout refresh;
    PendingIntent mGeofencePendingIntent;
    private List<com.google.android.gms.location.Geofence> mGeofenceList;
    private GoogleApiClient mGoogleApiClient;
    public static final String TAG = "Activity";
    LocationRequest mLocationRequest;
    double currentLatitude = 8.5565795, currentLongitude = 76.8810227;
    FloatingActionButton addgeofence;
    int resp;
    ListView select_user_list;
    RecyclerView geofence_list;
    Geofencelist_Adapter adapter;
    EditText geofence_namme,lat_test,long_test,geofence_address,radius,end_lat_test,end_long_test,end_geofence_address;
    Spinner area;
    Button assign_geofence,dassign_geofence;
   // ArrayList<Geofence_model> geofencelist=new ArrayList<>();
   public static ArrayList<EmployeData> geofencelist=new ArrayList<>();
    ArrayList<EmployeData> geofencelist1=new ArrayList<>();
    ArrayList<EmployeData> localgeofencelist=new ArrayList<>();
    ArrayList<EmployeData> employelist=new ArrayList();
    ArrayList<EmployeData> Geofencedata=new ArrayList();
    RelativeLayout geofence_back_layout;
    String[] geofence_area = { "Select Geofence Type","Circle","SingleLine"};
     Dialog dialog;
     ProgressBar geofence_progressbar;
    @SuppressLint("RestrictedApi")
    public static double lat=0.0,longi=0.0;
    public static String employid="0";
    @SuppressLint("RestrictedApi")
    RelativeLayout user_layout,end_location_layout,user_list;
    @SuppressLint("RestrictedApi")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.add_geofence_layout);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        geofence_list=(RecyclerView)findViewById(R.id.geofence_list) ;
        addgeofence=(FloatingActionButton)findViewById(R.id.add_geofence) ;
        geofence_back_layout=(RelativeLayout) findViewById(R.id.geofence_back_layout);
        userlist=(Spinner) findViewById(R.id.userlist);
        assign_geofence=(Button)findViewById(R.id.assign_geofence);
        dassign_geofence=(Button)findViewById(R.id.dassign_geofence);
        geofence_progressbar=(ProgressBar)findViewById(R.id.geofence_progressbar);
        history_layout=(RelativeLayout)findViewById(R.id.history_layout);
        user_list=(RelativeLayout)findViewById(R.id.user_list);
        refresh=(SwipeRefreshLayout)findViewById(R.id.pullToRefresh_geofence);

        userlist.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                employid=employelist.get(i).getUser_id();
                employeename=employelist.get(i).getUsername1();
                getgeofencelist();
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        if(PrefUtils.getKey(CreateGeofenceActivity.this, AppConstants.UserID).equalsIgnoreCase("1"))
        {
            lat= EmployeelistActivity.latitude;
            longi= EmployeelistActivity.longitude;
            addgeofence.setVisibility(View.VISIBLE);
            assign_geofence.setVisibility(View.VISIBLE);
            dassign_geofence.setVisibility(View.VISIBLE);
            user_list.setVisibility(View.VISIBLE);
            history_layout.setVisibility(View.VISIBLE);
            getEmployelist();
            getgeofencelist();
        }
        else
        {
            lat= MainActivity.latitude;
            longi= MainActivity.longitude;
            addgeofence.setVisibility(View.GONE);
            user_list.setVisibility(View.GONE);
            history_layout.setVisibility(View.GONE);
            assign_geofence.setVisibility(View.GONE);
            dassign_geofence.setVisibility(View.GONE);
            getemployegeofencelist();
        }

        geofence_back_layout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
        setSupportActionBar(toolbar);

        //loadArray( );
        //Get geofence list

        if (savedInstanceState == null) {

            mGeofenceList = new ArrayList<com.google.android.gms.location.Geofence>();

            resp = GooglePlayServicesUtil.isGooglePlayServicesAvailable(this);
        }
        history_layout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i=new Intent(CreateGeofenceActivity.this,GeofenceHistoryActivity.class);
                startActivity(i);
            }
        });
        addgeofence.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog = new Dialog(CreateGeofenceActivity.this);
                dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
              //  dialog.setCancelable(false);
                dialog.setContentView(R.layout.add_new_geofence);

                 geofence_namme = (EditText) dialog.findViewById(R.id.geofence_namme);
                 lat_test = (EditText) dialog.findViewById(R.id.lat_test);
                 long_test = (EditText) dialog.findViewById(R.id.long_test);
                 radius = (EditText) dialog.findViewById(R.id.radius);
                 geofence_address = (EditText) dialog.findViewById(R.id.geofence_address);
                 area = (Spinner) dialog.findViewById(R.id.area);
                 Button set_button=(Button)dialog.findViewById(R.id.set_button);
                 Button end_set_button=(Button)dialog.findViewById(R.id.end_set_button);
                 RelativeLayout geofence_location = (RelativeLayout) dialog.findViewById(R.id.geofence_location);
                 end_location_layout=  (RelativeLayout) dialog.findViewById(R.id.end_location_layout);
                 end_lat_test = (EditText) dialog.findViewById(R.id.end_lat_test);
                 end_long_test = (EditText) dialog.findViewById(R.id.end_long_test);
                 end_geofence_address = (EditText) dialog.findViewById(R.id.end_geofence_address);
                 ArrayAdapter aa = new ArrayAdapter(CreateGeofenceActivity.this,android.R.layout.simple_spinner_item,geofence_area);
                 aa.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                //Setting the ArrayAdapter data on the Spinner
                 area.setAdapter(aa);
                 area.setOnItemSelectedListener(CreateGeofenceActivity.this);

                 Button add_button = (Button) dialog.findViewById(R.id.add_button);
                 add_button.setText("Add");
                 RelativeLayout geofence_layout = (RelativeLayout) dialog.findViewById(R.id.geofence_layout);
                 Button cancel_dialog = (Button) dialog.findViewById(R.id.cancel_dialog);
                 geofence_layout.setVisibility(View.GONE);

                 geofence_address.addTextChangedListener(new TextWatcher() {

                    @Override
                    public void onTextChanged(CharSequence cs, int arg1, int arg2, int arg3) {
                        if(geofence_address.getText().toString().length()==0)
                        {
                            lat_test.setText("");
                            long_test.setText("");
                        }
                    }

                    @Override
                    public void beforeTextChanged(CharSequence arg0, int arg1, int arg2, int arg3) {

                    }

                    @Override
                    public void afterTextChanged(Editable arg0) {

                    }
                });
                 end_geofence_address.addTextChangedListener(new TextWatcher() {

                    @Override
                    public void onTextChanged(CharSequence cs, int arg1, int arg2, int arg3) {
                        if(end_geofence_address.getText().toString().length()==0)
                        {
                            end_lat_test.setText("");
                            end_long_test.setText("");
                        }
                    }

                    @Override
                    public void beforeTextChanged(CharSequence arg0, int arg1, int arg2, int arg3) {

                    }

                    @Override
                    public void afterTextChanged(Editable arg0) {

                    }
                });
                 geofence_location.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {

                        lat_test.setText(""+lat);
                        long_test.setText(""+longi);
                        getAddress();
                    }
                });
                end_location_layout.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {

                        end_lat_test.setText(""+lat);
                        end_long_test.setText(""+longi);
                        getAddressDestination();
                    }
                });
                set_button.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        if(geofence_address.getText().toString().length()>0)
                        {
                            String  Value = geofence_address.getText().toString();

                                getLocationFromAddress(CreateGeofenceActivity.this, Value);

                        }
                        else
                        {
                            Toast.makeText(CreateGeofenceActivity.this,"Please enter address",Toast.LENGTH_LONG).show();
                        }
                    }
                });

                end_set_button.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        if(end_geofence_address.getText().toString().length()>0)
                        {
                            String  Value = end_geofence_address.getText().toString();

                            getLocationFromAddress1(CreateGeofenceActivity.this, Value);

                        }
                        else
                        {
                            Toast.makeText(CreateGeofenceActivity.this,"Please enter destination address",Toast.LENGTH_LONG).show();
                        }
                    }
                });

                add_button.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if(geofence_namme.getText().toString().length()==0)
                        {
                            Toast.makeText(getApplicationContext(),"Please enter geofence name",Toast.LENGTH_LONG).show();
                        }
                        else if(area_value.equalsIgnoreCase("Select Geofence Type"))
                        {
                            Toast.makeText(getApplicationContext(),"Please select geofence type",Toast.LENGTH_LONG).show();
                        }
                       else if(lat_test.getText().toString().length()==0)
                        {
                            Toast.makeText(getApplicationContext(),"Please enter latitude",Toast.LENGTH_LONG).show();
                        }
                        else if(lat_test.getText().toString().length()<5)
                        {
                            Toast.makeText(getApplicationContext(),"Please enter corrrect latitude ",Toast.LENGTH_LONG).show();
                        }
                        else if(isValidLatLng(Double.parseDouble(lat_test.getText().toString()))==false)
                        {
                            Toast.makeText(getApplicationContext(),"Please enter correct latitude  ",Toast.LENGTH_LONG).show();
                        }
                       else if (long_test.getText().toString().length()==0)
                        {
                            Toast.makeText(getApplicationContext(),"Please enter longitude",Toast.LENGTH_LONG).show();
                        }
                        else if(lat_test.getText().toString().length()<5)
                        {
                            Toast.makeText(getApplicationContext(),"Please enter corrrect longitude ",Toast.LENGTH_LONG).show();
                        }
                        else if(isValidLatLng1(Double.parseDouble(long_test.getText().toString()))==false)
                        {
                            Toast.makeText(getApplicationContext(),"Please enter correct  longitude ",Toast.LENGTH_LONG).show();
                        }
                        else if (radius.getText().toString().length()==0)
                        {
                            Toast.makeText(getApplicationContext(),"Please Enter Radius",Toast.LENGTH_LONG).show();
                        }
                        else if(radius.getText().toString().equalsIgnoreCase("0"))
                        {
                            Toast.makeText(getApplicationContext(),"Please enter valid radius ",Toast.LENGTH_LONG).show();
                        }
                        else if(area_value.equalsIgnoreCase("SingleLine"))
                        {
                            if(end_lat_test.getText().toString().length()==0)
                            {
                                Toast.makeText(getApplicationContext(),"Please enter destination latitude",Toast.LENGTH_LONG).show();
                            }
                            else if(end_lat_test.getText().toString().length()<5)
                            {
                                Toast.makeText(getApplicationContext(),"Please enter corrrect destination latitude ",Toast.LENGTH_LONG).show();
                            }
                            else if(isValidLatLng(Double.parseDouble(end_lat_test.getText().toString()))==false)
                            {
                                Toast.makeText(getApplicationContext(),"Please enter correct destination latitude  ",Toast.LENGTH_LONG).show();
                            }
                            else if (end_long_test.getText().toString().length()==0)
                            {
                                Toast.makeText(getApplicationContext(),"Please enter destination longitude",Toast.LENGTH_LONG).show();
                            }
                            else if(end_long_test.getText().toString().length()<5)
                            {
                                Toast.makeText(getApplicationContext(),"Please enter corrrect destination longitude ",Toast.LENGTH_LONG).show();
                            }
                            else if(isValidLatLng1(Double.parseDouble(end_long_test.getText().toString()))==false)
                            {
                                Toast.makeText(getApplicationContext(),"Please enter correct destination longitude ",Toast.LENGTH_LONG).show();
                            }   else if (lat_test.getText().toString().equalsIgnoreCase(end_lat_test.getText().toString())||long_test.getText().toString().equalsIgnoreCase(end_long_test.getText().toString()))
                            {
                                Toast.makeText(getApplicationContext(),"source and destination address should not be same",Toast.LENGTH_LONG).show();
                            }
                           else
                            {

                                if (resp == ConnectionResult.SUCCESS) {

                                    initGoogleAPIClient();
                                    createGeofences(currentLatitude, currentLongitude);

                                } else {
                                    Log.e(TAG, "Your Device doesn't support Google Play Services.");
                                }

                                // Create the LocationRequest object
                                mLocationRequest = LocationRequest.create()
                                        .setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY)
                                        .setInterval(1 * 1000)        // 10 seconds, in milliseconds
                                        .setFastestInterval(1 * 1000); // 1 second, in milliseconds
                                dialog.dismiss();
                            }
                        }else
                        {

                            if (resp == ConnectionResult.SUCCESS) {

                                initGoogleAPIClient();
                                createGeofences(currentLatitude, currentLongitude);

                            } else {
                                Log.e(TAG, "Your Device doesn't support Google Play Services.");
                            }

                            // Create the LocationRequest object
                            mLocationRequest = LocationRequest.create()
                                    .setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY)
                                    .setInterval(1 * 1000)        // 10 seconds, in milliseconds
                                    .setFastestInterval(1 * 1000); // 1 second, in milliseconds
                            dialog.dismiss();
                        }

                    }
                });
                Button cancel_button = (Button) dialog.findViewById(R.id.remove_button);
                cancel_button.setVisibility(View.GONE);
                cancel_dialog.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        dialog.dismiss();
                    }
                });
                dialog.show();
            }
        });
          }

    private void showuserdialog() {
        dialog = new Dialog(CreateGeofenceActivity.this);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.user_dialog);
        select_user_list=(ListView) dialog.findViewById(R.id.select_user_list);
        Button cancel=(Button)dialog.findViewById(R.id.cancel_button) ;
        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.dismiss();
            }
        });
        dialog.show();
    }

    private void getemployegeofencelist() {
        if (NetworkUtil.isOnline(getApplicationContext())) {
            geofence_progressbar.setVisibility(View.VISIBLE);
            initGetEmployegeofencetAPIResources(PrefUtils.getKey(CreateGeofenceActivity.this, AppConstants.UserID), PrefUtils.getKey(CreateGeofenceActivity.this, AppConstants.Clientid),PrefUtils.getKey(CreateGeofenceActivity.this, AppConstants.Api_Token));


        } else {
            Toast.makeText(getApplicationContext(), getResources().getString(R.string.internet_connection), Toast.LENGTH_SHORT).show();
        }
    }


    public boolean isValidLatLng(double lat){
        if(lat < -90 || lat > 90)
        {
            return false;
        }
        return true;
    }
    public boolean isValidLatLng1( double lng){
      if(lng < -180 || lng > 180)
        {
            return false;
        }
        return true;
    }
    public Address getAddress1(double latitude, double longitude) {
        Geocoder geocoder;
        List<Address> addresses;
        geocoder = new Geocoder(this, Locale.getDefault());

        try {
            addresses = geocoder.getFromLocation(latitude, longitude, 1); // Here 1 represent max location result to returned, by documents it recommended 1 to 5
             if(addresses.size()>0)
            return addresses.get(0);

        } catch (IOException e) {
            e.printStackTrace();
        }

        return null;

    }

    //get full address
    public void getAddress() {

        Address locationAddress = getAddress1(lat, longi);
        // Address locationAddress=getAddress(-8.923255,13.194977);
        if (locationAddress != null) {
            String address = locationAddress.getAddressLine(0);
            String address1 = locationAddress.getAddressLine(1);
            String city = locationAddress.getLocality();
            String state = locationAddress.getAdminArea();
            String country = locationAddress.getCountryName();
            String postalCode = locationAddress.getPostalCode();

            String currentLocation;

            if (!TextUtils.isEmpty(address)) {
                currentLocation = address;

                if (!TextUtils.isEmpty(address1))
                    currentLocation += "\n" + address1;

                if (!TextUtils.isEmpty(city)) {
                    currentLocation += "\n" + city;

                    if (!TextUtils.isEmpty(postalCode))
                        currentLocation += " - " + postalCode;
                } else {
                    if (!TextUtils.isEmpty(postalCode))
                        currentLocation += "\n" + postalCode;
                }

                if (!TextUtils.isEmpty(state))
                    currentLocation += "\n" + state;

                if (!TextUtils.isEmpty(country))
                    currentLocation += "\n" + country;

                Log.d("currentLocation", "" + address);

                    geofence_address.setText(address);


                // Toast.makeText(getApplicationContext(),address,Toast.LENGTH_LONG).show();
            }

        }

    }
    //get full address
    public void getAddressDestination() {

        Address locationAddress = getAddress1(lat, longi);
        // Address locationAddress=getAddress(-8.923255,13.194977);
        if (locationAddress != null) {
            String address = locationAddress.getAddressLine(0);
            String address1 = locationAddress.getAddressLine(1);
            String city = locationAddress.getLocality();
            String state = locationAddress.getAdminArea();
            String country = locationAddress.getCountryName();
            String postalCode = locationAddress.getPostalCode();

            String currentLocation;

            if (!TextUtils.isEmpty(address)) {
                currentLocation = address;

                if (!TextUtils.isEmpty(address1))
                    currentLocation += "\n" + address1;

                if (!TextUtils.isEmpty(city)) {
                    currentLocation += "\n" + city;

                    if (!TextUtils.isEmpty(postalCode))
                        currentLocation += " - " + postalCode;
                } else {
                    if (!TextUtils.isEmpty(postalCode))
                        currentLocation += "\n" + postalCode;
                }

                if (!TextUtils.isEmpty(state))
                    currentLocation += "\n" + state;

                if (!TextUtils.isEmpty(country))
                    currentLocation += "\n" + country;

                    end_geofence_address.setText(address);

            }

        }

    }
    public void initGoogleAPIClient() {
        mGoogleApiClient = new GoogleApiClient.Builder(this)
                .addApi(LocationServices.API)
                .addConnectionCallbacks(connectionAddListener)
                .addOnConnectionFailedListener(connectionFailedListener)
                .build();
        mGoogleApiClient.connect();
    }

    private GoogleApiClient.ConnectionCallbacks connectionAddListener =
            new GoogleApiClient.ConnectionCallbacks() {
                @RequiresApi(api = Build.VERSION_CODES.M)
                @Override
                public void onConnected(Bundle bundle) {
                    Log.i(TAG, "onConnected");

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
                    Location location = LocationServices.FusedLocationApi.getLastLocation(mGoogleApiClient);

                    if (location == null) {
                        LocationServices.FusedLocationApi.requestLocationUpdates(mGoogleApiClient, mLocationRequest, CreateGeofenceActivity.this);

                    } else {
                        //If everything went fine lets get latitude and longitude
                        currentLatitude = location.getLatitude();
                        currentLongitude = location.getLongitude();

                        Log.i(TAG, currentLatitude + " WORKS " + currentLongitude);

                        //createGeofences(currentLatitude, currentLongitude);
                        //registerGeofences(mGeofenceList);
                    }

                    try{
                    LocationServices.GeofencingApi.addGeofences(
                            mGoogleApiClient,
                            getGeofencingRequest(),
                            getGeofencePendingIntent()
                    ).setResultCallback(new ResultCallback<Status>() {

                        @Override
                        public void onResult(Status status) {
                            if (status.isSuccess()) {

                                if(area_value.equalsIgnoreCase("SingleLine")) {
                                    if (NetworkUtil.isOnline(getApplicationContext())) {
                                        initsavegeofenceAPIResources(geofence_namme.getText().toString(), lat_test.getText().toString(), long_test.getText().toString(), radius.getText().toString(), geofence_address.getText().toString(), area_value, end_lat_test.getText().toString(), end_long_test.getText().toString(), end_geofence_address.getText().toString());
                                    }
                                    else
                                    {
                                        Toast.makeText(getApplicationContext(),"Please check internet connection",Toast.LENGTH_SHORT).show();
                                    }
                                }
                                else
                                {
                                    if (NetworkUtil.isOnline(getApplicationContext())) {
                                     initsavegeofenceAPIResources(geofence_namme.getText().toString(), lat_test.getText().toString(), long_test.getText().toString(), radius.getText().toString(), geofence_address.getText().toString(), area_value,"0.0","0.0","");
                                    }
                                    else
                                    {
                                        Toast.makeText(getApplicationContext(),"Please check internet connection",Toast.LENGTH_SHORT).show();
                                    }
                                }

                            } else {
                                Log.e(TAG, "Registering geofence failed: " + status.getStatusMessage() +
                                        " : " + status.getStatusCode());
                            }
                        }
                    });

                } catch (SecurityException securityException) {
                    // Catch exception generated if the app does not use ACCESS_FINE_LOCATION permission.

                    Log.e(TAG, "Error");
                }
                }

                @Override
                public void onConnectionSuspended(int i) {

                    Log.e(TAG, "onConnectionSuspended");

                }
            };
    private void initsavegeofenceAPIResources(String geofencename, String lat, String longi, String radius, String addreess,String area,String end_lat, String end_longi, String end_address) {
        SignUpPresenterImpl geofencePresenter = new SignUpPresenterImpl(CreateGeofenceActivity.this);
        geofencePresenter.callApi(AppConstants.CreateGeofence, geofencename, lat, longi,radius,addreess,area, PrefUtils.getKey(CreateGeofenceActivity.this, AppConstants.Clientid), PrefUtils.getKey(CreateGeofenceActivity.this, AppConstants.UserID),end_lat,end_longi,end_address,PrefUtils.getKey(CreateGeofenceActivity.this, AppConstants.Api_Token));
    }
    public LatLng getLocationFromAddress(Context context, String strAddress) {

        Geocoder coder = new Geocoder(context);
        List<Address> address;
        LatLng p1 = null;

        try {
            // May throw an IOException
            address = coder.getFromLocationName(strAddress, 5);
            if (address == null) {
                return null;
            }
            if(address.size()>0) {
                Address location = address.get(0);
                p1 = new LatLng(location.getLatitude(), location.getLongitude());
                lat_test.setText(""+location.getLatitude());
                long_test.setText(""+location.getLongitude());
            }
            else
            {
                Toast.makeText(CreateGeofenceActivity.this,"Please enter correct address",Toast.LENGTH_LONG).show();
            }
        } catch (IOException ex) {

            ex.printStackTrace();
        }

        return p1;
    }

    public LatLng getLocationFromAddress1(Context context, String strAddress) {

        Geocoder coder = new Geocoder(context);
        List<Address> address;
        LatLng p1 = null;

        try {
            // May throw an IOException
            address = coder.getFromLocationName(strAddress, 5);
            if (address == null) {
                return null;
            }
            if(address.size()>0) {
                Address location = address.get(0);
                p1 = new LatLng(location.getLatitude(), location.getLongitude());
                end_lat_test.setText(""+location.getLatitude());
                end_long_test.setText(""+location.getLongitude());
            }
            else
            {
                Toast.makeText(CreateGeofenceActivity.this,"Please enter correct address",Toast.LENGTH_LONG).show();
            }
        } catch (IOException ex) {

            ex.printStackTrace();
        }

        return p1;
    }
    public  void saveArray()
    {
        SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(CreateGeofenceActivity.this);
        SharedPreferences.Editor mEdit1 = sp.edit();
        mEdit1.putInt("Status_size", geofencelist.size());
        for(int i=0;i<geofencelist.size();i++)
        {
            mEdit1.remove("Status_" + i);
            mEdit1.putString("Status_gname" + i, geofencelist.get(i).getGeofencename());
            mEdit1.putString("Status_lat" + i, geofencelist.get(i).getGeofence_latitude());
            mEdit1.putString("Status_long" + i, geofencelist.get(i).getGeofence_longitude());
            mEdit1.putString("Status_radius" + i, geofencelist.get(i).getRadius());
            mEdit1.putString("Status_address" + i, geofencelist.get(i).getAddress());
            mEdit1.putString("Status_geofenceid" + i, geofencelist.get(i).getId());
        }
         mEdit1.commit();
        loadArray( );
    }

    private void getEmployelist() {
        if (NetworkUtil.isOnline(getApplicationContext())) {
            geofence_progressbar.setVisibility(View.VISIBLE);
            initGetEmployeeListAPIResources(PrefUtils.getKey(CreateGeofenceActivity.this, AppConstants.Clientid),PrefUtils.getKey(CreateGeofenceActivity.this, AppConstants.Api_Token));

        } else {

            Toast.makeText(getApplicationContext(), getResources().getString(R.string.internet_connection), Toast.LENGTH_SHORT).show();
        }
    }
    private void getgeofencelist() {
        if (NetworkUtil.isOnline(getApplicationContext())) {
            geofence_progressbar.setVisibility(View.VISIBLE);
                initGetgeofencetAPIResources(employid, PrefUtils.getKey(CreateGeofenceActivity.this, AppConstants.Clientid),"1",PrefUtils.getKey(CreateGeofenceActivity.this, AppConstants.Api_Token));
        } else {
            Toast.makeText(getApplicationContext(), getResources().getString(R.string.internet_connection), Toast.LENGTH_SHORT).show();
        }
    }
    private void initGetEmployeeListAPIResources(String clientid,String apitoken) {
        GetAlEmployePresenterImpl getAlluserPresenter = new GetAlEmployePresenterImpl(CreateGeofenceActivity.this);
        getAlluserPresenter.callApi(AppConstants.User_List,clientid,apitoken);
    }
    private void initGetgeofencetAPIResources(String userid, String clientid,String adminid,String apitoken) {
        GetAlEmployePresenterImpl getAllgeofencePresenter = new GetAlEmployePresenterImpl(CreateGeofenceActivity.this);
        getAllgeofencePresenter.callApi(AppConstants.Geofence_List, userid,clientid,adminid,apitoken);
    }
    private void initGetEmployegeofencetAPIResources(String userid, String clientid, String apitoken) {
        GetAlEmployePresenterImpl getAllgeofencePresenter = new GetAlEmployePresenterImpl(CreateGeofenceActivity.this);
        getAllgeofencePresenter.callApi(AppConstants.Employe_Geofence_List, userid,clientid,apitoken);
    }
    public  void loadArray( )
    {
        localgeofencelist=new ArrayList<>();
        EmployeData geo = null;
        SharedPreferences mSharedPreference1 =  PreferenceManager.getDefaultSharedPreferences(CreateGeofenceActivity.this);
        localgeofencelist.clear();
        int size = mSharedPreference1.getInt("Status_size", 0);

        for(int i=0;i<size;i++)
        {
            geo=new EmployeData();
            geo.setGeofencename(mSharedPreference1.getString("Status_gname" + i, null));
            geo.setGeofence_latitude(mSharedPreference1.getString("Status_lat" + i, null));
            geo.setGeofence_longitude(mSharedPreference1.getString("Status_long" + i, null));
            geo.setRadius(mSharedPreference1.getString("Status_radius" + i, null));
            geo.setAddress(mSharedPreference1.getString("Status_address" + i, null));
            localgeofencelist.add(geo);
        }

    }

    private GoogleApiClient.OnConnectionFailedListener connectionFailedListener =
            new GoogleApiClient.OnConnectionFailedListener() {
                @Override
                public void onConnectionFailed(ConnectionResult connectionResult) {
                    Log.e(TAG, "onConnectionFailed");
                }
            };

    /**
     * Create a CreateGeofenceActivity list
     */

    public void createGeofences(double latitude, double longitude) {
        String id = UUID.randomUUID().toString();
        com.google.android.gms.location.Geofence fence = new com.google.android.gms.location.Geofence.Builder()
                .setRequestId(id)
                .setTransitionTypes(com.google.android.gms.location.Geofence.GEOFENCE_TRANSITION_ENTER | com.google.android.gms.location.Geofence.GEOFENCE_TRANSITION_EXIT)
                .setCircularRegion(latitude, longitude, Float.parseFloat(radius.getText().toString()))
                .setExpirationDuration(com.google.android.gms.location.Geofence.NEVER_EXPIRE)
                .build();
        mGeofenceList.add(fence);
    }

    private GeofencingRequest getGeofencingRequest() {
        GeofencingRequest.Builder builder = new GeofencingRequest.Builder();
        builder.setInitialTrigger(GeofencingRequest.INITIAL_TRIGGER_ENTER);
        builder.addGeofences(mGeofenceList);
        return builder.build();
    }

    private PendingIntent getGeofencePendingIntent() {
        // Reuse the PendingIntent if we already have it.
        if (mGeofencePendingIntent != null) {
            return mGeofencePendingIntent;
        }
        Intent intent = new Intent(this, GeofenceTransitionsIntentService.class);
        // We use FLAG_UPDATE_CURRENT so that we get the same pending intent back when
        // calling addGeofences() and removeGeofences().
        return PendingIntent.getService(this, 0, intent, PendingIntent.
                FLAG_UPDATE_CURRENT);
    }

    @Override
    public void onLocationChanged(Location location) {

        currentLatitude = location.getLatitude();
        currentLongitude = location.getLongitude();
        Log.i(TAG, "onLocationChanged");
    }

    @Override
    public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
        area_value = area.getSelectedItem().toString();
        if(area_value.equalsIgnoreCase("SingleLine"))
        {
            end_location_layout.setVisibility(View.VISIBLE);
        }
        else
        {
            end_location_layout.setVisibility(View.GONE);
        }
    }

    @Override
    public void onNothingSelected(AdapterView<?> adapterView) {

    }

    @Override
    public void onSignUpSuccess(SignUpResponse signUpResponse) {
        String geofenceid=signUpResponse.getLast_insert_id();
        dialog.dismiss();
        getgeofencelist();
        Toast.makeText(CreateGeofenceActivity.this,"Geofence added successfully",Toast.LENGTH_SHORT).show();

    }

    @Override
    public void onSignUpFailure(String msg) {
        dialog.dismiss();
    }

    @Override
    public void onGetEmployeListSuccess(EmployeResponse employerResponse) {
        geofence_progressbar.setVisibility(View.GONE);
        try {
            if (employelist == null) {
                employelist = new ArrayList<>();
            }

            int count=employelist.size();
            EmployeData userData = new EmployeData();
            if(employerResponse.getEmployerData()!=null) {
                userData.setUsername1("Select User");
                userData.setUser_id("1");
                employelist.add(userData);
                employelist.addAll(employerResponse.getEmployerData());
                User_Adapter useradapter = new User_Adapter(getApplicationContext(), employelist);
                userlist.setAdapter(useradapter);
            }
        }catch (Exception e)
        {
            e.printStackTrace();
        }

        try {
            if (geofencelist1 == null) {
                geofencelist1 = new ArrayList<>();
                geofencelist = new ArrayList<>();
                Geofencedata=new ArrayList();
            }
            geofencelist1.clear();
            geofencelist.clear();
            Geofencedata.clear();

                geofencelist1.addAll(employerResponse.getSupplierData());
                if(employerResponse.getGeofencedatarData()!=null)
                {
                    Geofencedata.addAll(employerResponse.getGeofencedatarData());
                }
                if(geofencelist1.size()>0)
                {
                    for (int i=geofencelist1.size()-1;i>=0;i--)
                    {
                        EmployeData data=new EmployeData();
                        if(geofencelist1.get(i).getIsactive().equals("1")) {
                            String gid=geofencelist1.get(i).getId();
                            data.setGeofencename(geofencelist1.get(i).getGeofencename());
                            data.setGeofence_latitude(geofencelist1.get(i).getGeofence_latitude());
                            data.setGeofence_longitude(geofencelist1.get(i).getGeofence_longitude());
                            data.setAddress(geofencelist1.get(i).getAddress());
                            data.setRadius(geofencelist1.get(i).getRadius());
                            data.setId(geofencelist1.get(i).getId());
                            data.setType(geofencelist1.get(i).getType());
                            data.setEnd_latitude(geofencelist1.get(i).getEnd_latitude());
                            data.setEnd_longitude(geofencelist1.get(i).getEnd_longitude());
                            data.setEnd_geofence_address(geofencelist1.get(i).getEnd_geofence_address());
                            data.setIsgeonotification(geofencelist1.get(i).getIsgeonotification());
                            int a=Geofencedata.size();
                           if(Geofencedata.size()>0)
                            {
                                for (int j=0;j<Geofencedata.size();j++)
                            {
                                if(gid.equalsIgnoreCase(Geofencedata.get(j).getGeofence_id())){
                                    data.setAssigngeofence("true");
                                    data.setSelected(true);
                                    break;
                                }
                                else
                                {
                                    data.setSelected(false);
                                    data.setAssigngeofence("false");
                                }
                            }

                            }
                            geofencelist.add(data);
                        }

                    }
                    if (geofencelist.size() > 0) {

                        geofence_list.setHasFixedSize(true);
                        geofence_list.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
                        geofence_list.setItemAnimator(new DefaultItemAnimator());
                        geofence_list.addItemDecoration(new DividerItemDecoration(CreateGeofenceActivity.this));
                        adapter = new Geofencelist_Adapter(CreateGeofenceActivity.this, geofencelist,geofencelist1,geofencelist,geofence_progressbar,assign_geofence,employid,employeename,dassign_geofence,Geofencedata);
                        geofence_list.setAdapter(adapter);
                        adapter.notifyDataSetChanged();
                        saveArray();
                    } else {
                        Toast.makeText(getApplicationContext(), "Geofence not found", Toast.LENGTH_LONG).show();
                    }

                } else {
                    Toast.makeText(getApplicationContext(), "Geofence not found", Toast.LENGTH_LONG).show();
                }

        } catch (Exception e) {
            Log.d("Exception", e.getMessage());
        }
    }

    @Override
    public void onGetEmployeListListFailure(String msg) {
        geofence_progressbar.setVisibility(View.GONE);
    }
}
