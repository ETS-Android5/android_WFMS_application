package com.wfms.nectar.wfms;

import android.Manifest;
import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.app.ActivityManager;
import android.app.PendingIntent;
import android.app.ProgressDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentSender;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.content.res.Resources;
import android.location.Location;
import android.location.LocationManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Looper;
import android.provider.Settings;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import com.google.android.material.textfield.TextInputLayout;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;
import android.telephony.TelephonyManager;
import android.text.Html;
import android.text.TextUtils;
import android.text.method.LinkMovementMethod;
import android.util.Log;
import android.view.WindowManager;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;


import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.PendingResult;
import com.google.android.gms.common.api.ResolvableApiException;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.location.LocationSettingsRequest;
import com.google.android.gms.location.LocationSettingsResponse;
import com.google.android.gms.location.LocationSettingsResult;
import com.google.android.gms.location.LocationSettingsStatusCodes;
import com.google.android.gms.location.SettingsClient;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.messaging.FirebaseMessaging;
import com.karumi.dexter.Dexter;
import com.karumi.dexter.PermissionToken;
import com.karumi.dexter.listener.PermissionDeniedResponse;
import com.karumi.dexter.listener.PermissionGrantedResponse;
import com.karumi.dexter.listener.single.PermissionListener;
import com.wfms.nectar.jsonModelResponses.signup.SignUpResponse;
import com.wfms.nectar.presenter.presenterImpl.SignUpPresenterImpl;
import com.wfms.nectar.sql.DatabaseHelper;
import com.wfms.nectar.utils.AppConstants;
import com.wfms.nectar.utils.Config;
import com.wfms.nectar.utils.LocationTrack;
import com.wfms.nectar.utils.NetworkUtil;
import com.wfms.nectar.utils.PrefUtils;
import com.wfms.nectar.utils.SensorService;
import com.wfms.nectar.viewstate.SignUpView;

import java.text.DateFormat;
import java.util.ArrayList;
import java.util.Date;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import static android.Manifest.permission.ACCESS_COARSE_LOCATION;
import static android.Manifest.permission.ACCESS_FINE_LOCATION;
import static android.Manifest.permission.CAMERA;
import static android.Manifest.permission.READ_PHONE_STATE;
import static android.Manifest.permission.WRITE_EXTERNAL_STORAGE;

/**
 * Created by Nectar on 03-09-2018.
 */

public class LoginActivity extends AppCompatActivity implements SignUpView, GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener,
        LocationListener {
    /**
     * ButterKnife Code
     **/
    @BindView(R.id.nestedScrollView)
    ScrollView mNestedScrollView;
    @BindView(R.id.textInputEditTextName)
    EditText mTextInputEditTextName;
    @BindView(R.id.textInputEditTextpassword)
    EditText mTextInputEditTextpassword;
    @BindView(R.id.checkbox)
    CheckBox mCheckbox;
    @BindView(R.id.website_link)
    TextView website_link;
    @BindView(R.id.remember_me)
    TextView mRememberMe;
    @BindView(R.id.fingerprint_login)
    TextView fingerprint_login;
    @BindView(R.id.login_button)
    AppCompatButton mLoginButton;
    @BindView(R.id.username_inputlayout)
    TextInputLayout username_inputlayout;
    @BindView(R.id.password_inputlayout)
    TextInputLayout password_inputlayout;

    private static final long UPDATE_INTERVAL_IN_MILLISECONDS = 10000;
    private static final long FASTEST_UPDATE_INTERVAL_IN_MILLISECONDS = 5000;
    private final static int REQUEST_CHECK_SETTINGS = 2000;
    private final static int REQUEST_CHECK_SETTINGS_GPS = 0x1;
    private static final int MY_PERMISSIONS_REQUEST_READ_PHONE_STATE = 0;
    private final static int ALL_PERMISSIONS_RESULT = 101;

    private FusedLocationProviderClient mFusedLocationClient;
    private SettingsClient mSettingsClient;
    private LocationRequest mLocationRequest;
    private LocationSettingsRequest mLocationSettingsRequest;
    private LocationCallback mLocationCallback;
    private Location mCurrentLocation;
    private String mLastUpdateTime;
    private Boolean mRequestingLocationUpdates;
    public static final String auth = "9900a9720d31dfd5fdb4352700c";
    public static Context context;
    ProgressDialog dialog;
    Resources resources;
    TelephonyManager telephonyManager;
    public static String Userid;
    String device_unique_id, IMEI;
    // Google client to interact with Google API
    protected static final String TAG = "LocationOnOff";
    private Location mylocation;
    private GoogleApiClient googleApiClient;
    private ArrayList<String> permissionsToRequest;
    private ArrayList<String> permissionsRejected = new ArrayList<>();
    private ArrayList<String> permissions = new ArrayList<>();
    LocationTrack locationTrack;
    public static String address, locationURL;
    public static double latitude = 0.0;
    public static double longitude = 0.0;
    Intent mServiceIntent;
    private SensorService mSensorService;
    Context ctx;
    private BroadcastReceiver mRegistrationBroadcastReceiver;
    DatabaseHelper databaseHelper;
    public static String a = "0";

    public Context getCtx() {
        return ctx;
    }
    private PendingIntent geoFencePendingIntent;
    private final int GEOFENCE_REQ_CODE = 0;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);

        setContentView(R.layout.activity_login);
        ButterKnife.bind(this);
        ctx = this;
        resources = ctx.getResources();

        //enable gps
        setUpGClient();
        permissions.add(ACCESS_FINE_LOCATION);
        permissions.add(ACCESS_COARSE_LOCATION);
        permissions.add(READ_PHONE_STATE);
        permissions.add(CAMERA);
        permissions.add(WRITE_EXTERNAL_STORAGE);
        permissionsToRequest = findUnAskedPermissions(permissions);

        //allow loication permission
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {

            if (permissionsToRequest.size() > 0)
                requestPermissions(permissionsToRequest.toArray(new String[permissionsToRequest.size()]), ALL_PERMISSIONS_RESULT);
        }

        //start serveice for sync localdata to server
        mSensorService = new SensorService(getCtx());
        mServiceIntent = new Intent(getCtx(), mSensorService.getClass());

        if (!isMyServiceRunning(mSensorService.getClass())) {
            startService(mServiceIntent);
        }


        website_link.setText(Html.fromHtml("Powered by :" + "<a href='http://nectarinfotel.com/'>Nectar Infotel Solution Pvt.Ltd.</a> "));
        website_link.setMovementMethod(LinkMovementMethod.getInstance());// make it active


        //get unique device id
        telephonyManager = (TelephonyManager) getSystemService(Context.
                TELEPHONY_SERVICE);
        loadIMEI();


        //set username and password if  user check on remember me
        String email = PrefUtils.getKey(LoginActivity.this, AppConstants.Username);
        String password = PrefUtils.getKey(LoginActivity.this, AppConstants.Password);
        String userID = PrefUtils.getKey(LoginActivity.this, AppConstants.UserID);
        if (userID == null) {

        } else {
            if (email != null) {
                Log.d("username", email);
                mTextInputEditTextName.setText(email);
                mCheckbox.setChecked(true);
            }
            if (password != null) {
                Log.d("password", password);
                mTextInputEditTextpassword.setText(password);
                mCheckbox.setChecked(true);
            }
        }
        createGeofencePendingIntent();
        //register fcm id for push notification
        registerfcmID();
        init();
        restoreValuesFromBundle(savedInstanceState);
    }
    private PendingIntent createGeofencePendingIntent() {
        Log.d(TAG, "createGeofencePendingIntent");
        if (geoFencePendingIntent != null)
            return geoFencePendingIntent;

        Intent intent = new Intent(this, GeofenceTransitionsIntentService.class);
        return PendingIntent.getService(
                this, GEOFENCE_REQ_CODE, intent, PendingIntent.FLAG_UPDATE_CURRENT);
    }
    private void registerfcmID() {
        mRegistrationBroadcastReceiver = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {

                // checking for type intent filter
                if (intent.getAction().equals(Config.REGISTRATION_COMPLETE)) {
                    // gcm successfully registered
                    // now subscribe to `global` topic to receive app wide notifications
                    FirebaseMessaging.getInstance().subscribeToTopic(Config.TOPIC_GLOBAL);
                    displayFirebaseRegId();

                } else if (intent.getAction().equals(Config.PUSH_NOTIFICATION)) {
                    // new push notification is received

                    String message = intent.getStringExtra("message");

                }
            }
        };

        displayFirebaseRegId();
    }

    private void displayFirebaseRegId() {
        SharedPreferences pref = getApplicationContext().getSharedPreferences(Config.SHARED_PREF, 0);
        String regId = pref.getString("regId", null);
        Log.e("Firebase reg id: ", "" + regId);
        PrefUtils.storeKey(LoginActivity.this, AppConstants.TokenID, regId);

    }

    //login functionality
    @OnClick(R.id.login_button)
    public void onClick() {
        if (TextUtils.isEmpty(mTextInputEditTextName.getText().toString())) {
            mTextInputEditTextName.setError(resources.getString(R.string.enter_username));
            mTextInputEditTextName.requestFocus();

        } else if (TextUtils.isEmpty(mTextInputEditTextpassword.getText().toString())) {
            mTextInputEditTextpassword.setError(resources.getString(R.string.enter_password));
            mTextInputEditTextpassword.requestFocus();
        } else {
            mLoginButton.setEnabled(false);
            mLoginButton.setClickable(false);
            login();
        }
    }

    //login api integration
    private void login() {
        if (NetworkUtil.isOnline(getApplicationContext())) {
            dialog = new ProgressDialog(LoginActivity.this, R.style.AppCompatAlertDialogStyle);
            dialog.setMessage(getResources().getString(R.string.loading));
            dialog.show();
            initLoginAPIResources(mTextInputEditTextName.getText().toString(), mTextInputEditTextpassword.getText().toString());
        } else {
            if (!mTextInputEditTextName.getText().toString().equalsIgnoreCase(PrefUtils.getKey(LoginActivity.this, AppConstants.Username))) {
                mLoginButton.setEnabled(true);
                mLoginButton.setClickable(true);
                Toast.makeText(getApplicationContext(), resources.getString(R.string.internet_connection), Toast.LENGTH_SHORT).show();
            } else {
                getlocation();
                String userID = PrefUtils.getKey(LoginActivity.this, AppConstants.UserID);
                if (userID != null) {
                    if (mTextInputEditTextName.getText().toString().equalsIgnoreCase("admin")) {
                        Intent i = new Intent(LoginActivity.this, EmployeelistActivity.class);
                        startActivity(i);
                        finish();
                    } else {
                        Intent i = new Intent(LoginActivity.this, MainActivity.class);
                        startActivity(i);
                        finish();
                    }
                } else {
                    Toast.makeText(getApplicationContext(), resources.getString(R.string.internet_connection), Toast.LENGTH_SHORT).show();
                }
            }
        }
    }

    private void getlocation() {

        Dexter.withActivity(this)
                .withPermission(Manifest.permission.ACCESS_FINE_LOCATION)
                .withListener(new PermissionListener() {
                    @Override
                    public void onPermissionGranted(PermissionGrantedResponse response) {
                        mRequestingLocationUpdates = true;
                        startLocationUpdates();
                    }

                    @Override
                    public void onPermissionDenied(PermissionDeniedResponse response) {
                        if (response.isPermanentlyDenied()) {
                            // open device settings when the permission is
                            // denied permanently
                            openSettings();
                        }
                    }

                    @Override
                    public void onPermissionRationaleShouldBeShown(com.karumi.dexter.listener.PermissionRequest permission, PermissionToken token) {
                        token.continuePermissionRequest();
                    }


                }).check();
    }

    private void initLoginAPIResources(String name, String password) {
        SignUpPresenterImpl loginPresenter = new SignUpPresenterImpl(LoginActivity.this);
        loginPresenter.callApi(AppConstants.LOGIN, name, password, PrefUtils.getKey(LoginActivity.this, AppConstants.TokenID), PrefUtils.getKey(LoginActivity.this, AppConstants.ClientName));
    }

    //login success response
    @Override
    public void onSignUpSuccess(final SignUpResponse signUpResponse) {
        if (mCheckbox.isChecked()) {
            PrefUtils.storeKey(getApplicationContext(), AppConstants.Username, (mTextInputEditTextName.getText().toString()));
            PrefUtils.storeKey(getApplicationContext(), AppConstants.Password, mTextInputEditTextpassword.getText().toString());

        } else {
            PrefUtils.storeKey(getApplicationContext(), AppConstants.Username, null);
            PrefUtils.storeKey(getApplicationContext(), AppConstants.Password, null);
        }
        PrefUtils.storeKey(getApplicationContext(), AppConstants.LogoutUserID, signUpResponse.getUserId());
        PrefUtils.storeKey(getApplicationContext(), AppConstants.Api_Token, signUpResponse.getApi_token());
        PrefUtils.storeKey(getApplicationContext(), AppConstants.UserID, signUpResponse.getUserId());
        PrefUtils.storeKey(getApplicationContext(), AppConstants.Name, signUpResponse.getName());
        PrefUtils.storeKey(getApplicationContext(), AppConstants.IsLogin, signUpResponse.getFlag());
       // Log.d("Api_token",signUpResponse.getApi_token());
        if (signUpResponse.getUserId().equals("1")) {
            Intent i = new Intent(LoginActivity.this, EmployeelistActivity.class);
            startActivity(i);
            finish();
            dialog.dismiss();
        } else {
            Intent i = new Intent(LoginActivity.this, MainActivity.class);
            startActivity(i);
            finish();
            dialog.dismiss();
        }

    }

    //login failure response
    @Override
    public void onSignUpFailure(String msg) {
        mLoginButton.setEnabled(true);
        mLoginButton.setClickable(true);
        dialog.dismiss();

        if (msg.equalsIgnoreCase("Invalid Password")) {
            Toast.makeText(this, "Invalid Password", Toast.LENGTH_SHORT).show();
        } else if (msg.equalsIgnoreCase("Invalid username")) {
            Toast.makeText(this, "Invalid username", Toast.LENGTH_SHORT).show();
        }

        else if (msg.equalsIgnoreCase("expired")) {
            Toast.makeText(this, "Your date is expired", Toast.LENGTH_SHORT).show();
        }else {

            Toast.makeText(this, "Server not responding", Toast.LENGTH_LONG).show();
        }

    }

    public void loadIMEI() {

        if (ActivityCompat.checkSelfPermission(this, READ_PHONE_STATE)
                != PackageManager.PERMISSION_GRANTED) {
            if (ActivityCompat.shouldShowRequestPermissionRationale(this,
                    READ_PHONE_STATE)) {
//                get_imei_data();
            } else {
                ActivityCompat.requestPermissions(this, new String[]{READ_PHONE_STATE},
                        MY_PERMISSIONS_REQUEST_READ_PHONE_STATE);
            }
        } else {

            TelephonyManager mngr = (TelephonyManager) getSystemService(Context.TELEPHONY_SERVICE);
         //   IMEI = mngr.getDeviceId();
           device_unique_id = Settings.Secure.getString(this.getContentResolver(),
                    Settings.Secure.ANDROID_ID);
           // Log.d("deviceid", device_unique_id);
            PrefUtils.storeKey(LoginActivity.this, AppConstants.deviceID, device_unique_id);

        }
    }

    @Override
    public void onLocationChanged(Location location) {
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return;
        }
        mylocation = LocationServices.FusedLocationApi.getLastLocation(googleApiClient);
        // getMyLocation();
        if (mylocation != null) {
            latitude = mylocation.getLatitude();
            longitude = mylocation.getLongitude();
            Log.d("setoplatitude", "" + latitude);
            Log.d("setoplongitude", "" + longitude);
            //Or Do whatever you want with your location
        }
    }

    private synchronized void setUpGClient() {
        googleApiClient = new GoogleApiClient.Builder(this)
                .enableAutoManage(this, 0, this)
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .addApi(LocationServices.API)
                .build();
        googleApiClient.connect();
    }


    private void getMyLocation() {

        if (googleApiClient != null) {
            if (googleApiClient.isConnected()) {
                int permissionLocation = ContextCompat.checkSelfPermission(LoginActivity.this,
                        Manifest.permission.ACCESS_FINE_LOCATION);
                if (permissionLocation == PackageManager.PERMISSION_GRANTED) {
                    mylocation = LocationServices.FusedLocationApi.getLastLocation(googleApiClient);
                    LocationRequest locationRequest = new LocationRequest();
                    locationRequest.setInterval(3000);
                    locationRequest.setFastestInterval(3000);
                    locationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
                    LocationSettingsRequest.Builder builder = new LocationSettingsRequest.Builder()
                            .addLocationRequest(locationRequest);
                    builder.setAlwaysShow(true);
                    LocationServices.FusedLocationApi
                            .requestLocationUpdates(googleApiClient, locationRequest, this);
                    PendingResult<LocationSettingsResult> result =
                            LocationServices.SettingsApi
                                    .checkLocationSettings(googleApiClient, builder.build());
                    result.setResultCallback(new ResultCallback<LocationSettingsResult>() {

                        @Override
                        public void onResult(LocationSettingsResult result) {
                            final Status status = result.getStatus();
                            switch (status.getStatusCode()) {
                                case LocationSettingsStatusCodes.SUCCESS:
                                    // All location settings are satisfied.
                                    // You can initialize location requests here.
                                    int permissionLocation = ContextCompat
                                            .checkSelfPermission(LoginActivity.this,
                                                    Manifest.permission.ACCESS_FINE_LOCATION);
                                    if (permissionLocation == PackageManager.PERMISSION_GRANTED) {
                                        mylocation = LocationServices.FusedLocationApi
                                                .getLastLocation(googleApiClient);

                                    }
                                    break;
                                case LocationSettingsStatusCodes.RESOLUTION_REQUIRED:
                                    // Location settings are not satisfied.
                                    // But could be fixed by showing the user a dialog.
                                    try {
                                        // Show the dialog by calling startResolutionForResult(),
                                        // and check the result in onActivityResult().
                                        // Ask to turn on GPS automatically
                                        status.startResolutionForResult(LoginActivity.this,
                                                REQUEST_CHECK_SETTINGS_GPS);
                                    } catch (IntentSender.SendIntentException e) {
                                        // Ignore the error.
                                    }
                                    break;
                                case LocationSettingsStatusCodes.SETTINGS_CHANGE_UNAVAILABLE:
                                    // Location settings are not satisfied.
                                    // However, we have no way
                                    // to fix the
                                    // settings so we won't show the dialog.
                                    // finish();
                                    break;
                            }
                        }
                    });
                }
            }
        }
    }


    @Override
    public void onConnected(@Nullable Bundle bundle) {
        LocationManager service = (LocationManager) getSystemService(LOCATION_SERVICE);
        boolean enabled = service.isProviderEnabled(LocationManager.GPS_PROVIDER);
        Log.d("boolea", "" + enabled);
        if (enabled == false) {
            getMyLocation();
        }


    }

    @Override
    public void onConnectionSuspended(int i) {

    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {

    }

    private ArrayList<String> findUnAskedPermissions(ArrayList<String> wanted) {
        ArrayList<String> result = new ArrayList<String>();

        for (String perm : wanted) {
            if (!hasPermission(perm)) {
                result.add(perm);
            }
        }

        return result;
    }

    private boolean hasPermission(String permission) {
        if (canMakeSmores()) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                return (checkSelfPermission(permission) == PackageManager.PERMISSION_GRANTED);
            }
        }
        return true;
    }

    private boolean canMakeSmores() {
        return (Build.VERSION.SDK_INT > Build.VERSION_CODES.LOLLIPOP_MR1);
    }

    @TargetApi(Build.VERSION_CODES.M)
    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {

        switch (requestCode) {

            case ALL_PERMISSIONS_RESULT:
                for (String perms : permissionsToRequest) {
                    if (!hasPermission(perms)) {
                        permissionsRejected.add(perms);
                    }
                }

                if (permissionsRejected.size() > 0) {
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                        if (shouldShowRequestPermissionRationale(permissionsRejected.get(0))) {
                            showMessageOKCancel("These permissions are mandatory for the application. Please allow access.",
                                    new DialogInterface.OnClickListener() {
                                        @Override
                                        public void onClick(DialogInterface dialog, int which) {
                                            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                                                requestPermissions(permissionsRejected.toArray(new String[permissionsRejected.size()]), ALL_PERMISSIONS_RESULT);
                                            }
                                        }
                                    });
                            return;
                        } else {

                            getMyLocation();
                            Log.d("permissiongranted", "permissiongranted");
                        }
                    }

                } else {
                    int permissionLocation = ContextCompat.checkSelfPermission(LoginActivity.this,
                            Manifest.permission.ACCESS_FINE_LOCATION);
                    if (permissionLocation == PackageManager.PERMISSION_GRANTED) {

                        getMyLocation();
                        Log.d("permissiongranted11", "permissiongranted11");
                    }

                }

                break;
        }

    }

    //show dialog for enable gps
    private void showMessageOKCancel(String message, DialogInterface.OnClickListener okListener) {
        new AlertDialog.Builder(LoginActivity.this)
                .setMessage(message)
                .setPositiveButton("OK", okListener)
                .setNegativeButton("Cancel", okListener)
                .create()
                .show();
    }

    private boolean isMyServiceRunning(Class<?> serviceClass) {
        ActivityManager manager = (ActivityManager) getSystemService(Context.ACTIVITY_SERVICE);
        if (manager != null) {
            for (ActivityManager.RunningServiceInfo service : manager.getRunningServices(Integer.MAX_VALUE)) {
                if (serviceClass.getName().equals(service.service.getClassName())) {
                    Log.i("isMyServiceRunning?", true + "");
                    return true;
                }
            }
        }
        Log.i("isMyServiceRunning?", false + "");
        return false;
    }

    private void restoreValuesFromBundle(Bundle savedInstanceState) {
        if (savedInstanceState != null) {
            if (savedInstanceState.containsKey("is_requesting_updates")) {
                mRequestingLocationUpdates = savedInstanceState.getBoolean("is_requesting_updates");
            }

            if (savedInstanceState.containsKey("last_known_location")) {
                mCurrentLocation = savedInstanceState.getParcelable("last_known_location");
            }

            if (savedInstanceState.containsKey("last_updated_on")) {
                mLastUpdateTime = savedInstanceState.getString("last_updated_on");
            }
        }

        updateLocationUI();
    }

    private void init() {
        mFusedLocationClient = LocationServices.getFusedLocationProviderClient(this);
        mSettingsClient = LocationServices.getSettingsClient(this);

        mLocationCallback = new LocationCallback() {
            @Override
            public void onLocationResult(LocationResult locationResult) {
                super.onLocationResult(locationResult);
                // location is received
                mCurrentLocation = locationResult.getLastLocation();
                mLastUpdateTime = DateFormat.getTimeInstance().format(new Date());

                updateLocationUI();
            }
        };

        mRequestingLocationUpdates = false;

        mLocationRequest = new LocationRequest();
        mLocationRequest.setInterval(UPDATE_INTERVAL_IN_MILLISECONDS);
        mLocationRequest.setFastestInterval(FASTEST_UPDATE_INTERVAL_IN_MILLISECONDS);
        mLocationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);

        LocationSettingsRequest.Builder builder = new LocationSettingsRequest.Builder();
        builder.addLocationRequest(mLocationRequest);
        mLocationSettingsRequest = builder.build();
    }
    private void updateLocationUI() {
        if (mCurrentLocation != null)

        {
            mRequestingLocationUpdates = false;
            mFusedLocationClient
                    .removeLocationUpdates(mLocationCallback)
                    .addOnCompleteListener(this, new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            // Toast.makeText(getApplicationContext(), "Location updates stopped!", Toast.LENGTH_SHORT).show();

                        }
                    });
            Log.d("Latitude", "" + mCurrentLocation.getLatitude());
            Log.d("Longitude", "" + mCurrentLocation.getLongitude());
            locationURL = "https://www.google.com/maps/search/?api=1&query=" + mCurrentLocation.getLatitude() + "," + mCurrentLocation.getLongitude();
            Log.d("locationurl", locationURL);
            latitude = mCurrentLocation.getLatitude();
            longitude = mCurrentLocation.getLongitude();
        }
    }

    private void openSettings() {
        Intent intent = new Intent();
        intent.setAction(
                Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
        Uri uri = Uri.fromParts("package",
                BuildConfig.APPLICATION_ID, null);
        intent.setData(uri);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
    }


    private void startLocationUpdates() {
        mSettingsClient
                .checkLocationSettings(mLocationSettingsRequest)
                .addOnSuccessListener(this, new OnSuccessListener<LocationSettingsResponse>() {
                    @SuppressLint("MissingPermission")
                    @Override
                    public void onSuccess(LocationSettingsResponse locationSettingsResponse) {
                        Log.i(TAG, "All location settings are satisfied.");

                        //  Toast.makeText(getApplicationContext(), "Started location updates!", Toast.LENGTH_SHORT).show();

                        //noinspection MissingPermission
                        mFusedLocationClient.requestLocationUpdates(mLocationRequest,
                                mLocationCallback, Looper.myLooper());

                        updateLocationUI();
                    }
                })
                .addOnFailureListener(this, new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        int statusCode = ((ApiException) e).getStatusCode();
                        switch (statusCode) {
                            case LocationSettingsStatusCodes.RESOLUTION_REQUIRED:
                                Log.i(TAG, "Location settings are not satisfied. Attempting to upgrade " +
                                        "location settings ");
                                try {
                                    // Show the dialog by calling startResolutionForResult(), and check the
                                    // result in onActivityResult().
                                    ResolvableApiException rae = (ResolvableApiException) e;
                                    rae.startResolutionForResult(LoginActivity.this, REQUEST_CHECK_SETTINGS);
                                } catch (IntentSender.SendIntentException sie) {
                                    Log.i(TAG, "PendingIntent unable to execute request.");
                                }
                                break;
                            case LocationSettingsStatusCodes.SETTINGS_CHANGE_UNAVAILABLE:
                                String errorMessage = "Location settings are inadequate, and cannot be " +
                                        "fixed here. Fix in Settings.";
                                Log.e(TAG, errorMessage);

                                Toast.makeText(LoginActivity.this, errorMessage, Toast.LENGTH_LONG).show();
                        }

                        updateLocationUI();
                    }
                });
    }

}

