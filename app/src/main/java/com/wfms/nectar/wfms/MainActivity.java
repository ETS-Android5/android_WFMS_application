package com.wfms.nectar.wfms;

import android.Manifest;
import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.app.Dialog;
import android.app.KeyguardManager;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.IntentSender;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.hardware.fingerprint.FingerprintManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Looper;
import android.provider.MediaStore;
import android.provider.Settings;
import android.security.keystore.KeyGenParameterSpec;
import android.security.keystore.KeyPermanentlyInvalidatedException;
import android.security.keystore.KeyProperties;
import android.text.Html;
import android.text.TextUtils;
import android.util.Log;
import android.util.SparseArray;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.widget.AppCompatButton;
import androidx.appcompat.widget.Toolbar;
import androidx.core.app.ActivityCompat;
import androidx.core.content.FileProvider;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.common.api.ResolvableApiException;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.location.LocationSettingsRequest;
import com.google.android.gms.location.LocationSettingsResponse;
import com.google.android.gms.location.LocationSettingsStatusCodes;
import com.google.android.gms.location.SettingsClient;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.gms.vision.Frame;
import com.google.android.gms.vision.face.Face;
import com.google.android.gms.vision.face.FaceDetector;
import com.google.android.gms.vision.face.Landmark;
import com.google.android.material.snackbar.Snackbar;
import com.google.gson.JsonObject;
import com.karumi.dexter.BuildConfig;
import com.karumi.dexter.Dexter;
import com.karumi.dexter.PermissionToken;
import com.karumi.dexter.listener.PermissionDeniedResponse;
import com.karumi.dexter.listener.PermissionGrantedResponse;
import com.karumi.dexter.listener.single.PermissionListener;
import com.wfms.nectar.jsonModelResponses.Fuel.EmployeData;
import com.wfms.nectar.jsonModelResponses.signup.SignUpResponse;
import com.wfms.nectar.presenter.presenterImpl.SignUpPresenterImpl;
import com.wfms.nectar.presenter.presenterImpl.SignUpPresenterImplnotification;
import com.wfms.nectar.presenter.presenterImpl.SignUpUpdatePresenterImpl;
import com.wfms.nectar.presenter.presenterImpl.SignUpadminPresenterImpl;
import com.wfms.nectar.retrofit.CallbackWithRetry;
import com.wfms.nectar.sql.DatabaseHelper;
import com.wfms.nectar.utils.AppConstants;
import com.wfms.nectar.utils.Config;
import com.wfms.nectar.utils.FileUtils;
import com.wfms.nectar.utils.FingerprintHandler;
import com.wfms.nectar.utils.GalleryCameraUtils;
import com.wfms.nectar.utils.NetworkUtil;
import com.wfms.nectar.utils.PrefUtils;
import com.wfms.nectar.utils.ProgressRequestBody;
import com.wfms.nectar.viewstate.SignUpView;
import com.wfms.nectar.viewstate.SignUpViewOut;
import com.wfms.nectar.viewstate.SignUpViewUpdate;
import com.wfms.nectar.viewstate.SignUpViewadminnotification;
import com.wfms.nectar.viewstate.SignUpViewnotification;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.KeyStore;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.NoSuchProviderException;
import java.security.UnrecoverableKeyException;
import java.security.cert.CertificateException;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import javax.crypto.Cipher;
import javax.crypto.KeyGenerator;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.SecretKey;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import droidninja.filepicker.FilePickerConst;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Response;

public class MainActivity extends NewPhotoPickerActivity implements ProgressRequestBody.UploadCallbacks, SignUpView,
        SignUpViewOut, SignUpViewnotification, SignUpViewadminnotification, SignUpViewUpdate {
    /**
     * ButterKnife Code
     **/
  //  @BindView(R.id.timeIn_button)
   public static  RelativeLayout mTimeInButton;
    @BindView(R.id.capture_button)
    RelativeLayout capture_button;
    @BindView(R.id.timeOut_button)
    AppCompatButton mTimeOutButton;
    @BindView(R.id.viewrecords_button)
    AppCompatButton viewrecords_button;
    @BindView(R.id.main_layout)
    RelativeLayout main_layout;
    @BindView(R.id.logout)
    ImageView logout;
    @BindView(R.id.date)
    TextView date;
    @BindView(R.id.time)
    TextView time;
    @BindView(R.id.toolbar)
    Toolbar toolbar;
   // @BindView(R.id.timein_text)
   public static TextView timein_text;
   // @BindView(R.id.timein_img)
    public static ImageView timein_img;
    /**
     * ButterKnife Code
     **/
    private Uri imageUri;
    Bitmap editedBitmap;
    private static final int CAMERA_REQUEST = 101;
    private static final int REQUEST_WRITE_PERMISSION = 200;
    private FusedLocationProviderClient mFusedLocationClient;
    private SettingsClient mSettingsClient;
    private LocationRequest mLocationRequest;
    private LocationSettingsRequest mLocationSettingsRequest;
    private LocationCallback mLocationCallback;
    private Location mCurrentLocation;
    private String mLastUpdateTime;
    private Boolean mRequestingLocationUpdates;
    ArrayList<String> filePaths = new ArrayList<>();
    private KeyStore keyStore;
    private int PICK_PDF_REQUEST = 1;
    public final int requestCode = 20;
    private static final String KEY_NAME = "androidHive";
    private Cipher cipher;
    private static final String TAG = MainActivity.class.getSimpleName();
    private final static int REQUEST_CHECK_SETTINGS = 2000;
    private Uri fileuri;
    public boolean islogin = false;
    private static final long UPDATE_INTERVAL_IN_MILLISECONDS = 10000;
    private static final long FASTEST_UPDATE_INTERVAL_IN_MILLISECONDS = 5000;
    ProgressDialog dialog;
    public static String userid, username, currenttime, currentdate, locationURL, login, olduserid, deviceiD, address;
    Context context;
    Handler handler = new Handler();
    public static double latitude=0.0;
    public static double longitude=0.0;
    String newTime;
    public static ArrayList<String> imagesEncodedList = new ArrayList<>();
    SwipeRefreshLayout pullToRefresh;
    private FaceDetector detector;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
        mTimeInButton=(RelativeLayout)findViewById(R.id.timeIn_button) ;
        timein_text=(TextView)findViewById(R.id.timein_text) ;
        timein_img=(ImageView) findViewById(R.id.timein_img) ;
        context = mTimeInButton.getContext();
        if (NetworkUtil.isOnline(getApplicationContext()))
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        toolbar.setTitleTextColor(Color.WHITE);
        getSupportActionBar().setTitle("Entry Time");

        //get shared prefernce data
        userid = PrefUtils.getKey(MainActivity.this, AppConstants.UserID);
        username = PrefUtils.getKey(MainActivity.this, AppConstants.Username);

        login = PrefUtils.getKey(MainActivity.this, AppConstants.IsLogin);
        olduserid = PrefUtils.getKey(MainActivity.this, AppConstants.OldUserID);
        deviceiD = PrefUtils.getKey(MainActivity.this, AppConstants.deviceID);

        //check user is previously timein or timeout
        if (login != null) {
            if (login.equals("1")) {
                timein_text.setText("Time Out");
                timein_img.setBackgroundResource(R.drawable.time_out);
            } else if (login.equals("0")) {
                timein_text.setText("Time In");
                timein_img.setBackgroundResource(R.drawable.time_in);
            }
        } else {
            timein_text.setText("Time In");
            timein_img.setBackgroundResource(R.drawable.time_in);
        }
         //dd-MM-yyyy_HH:mm
        //get current date and time from system
        final Handler someHandler = new Handler(getMainLooper());
        someHandler.postDelayed(new Runnable() {
            @Override
            public void run() {
                String Dtime = new SimpleDateFormat("MMMM,dd yyyy_HH:mm" +
                        "" +
                        "", Locale.US).format(new Date());
                String datetime[] = Dtime.split("_");
                time.setText(Html.fromHtml(datetime[1]));
                date.setText(Html.fromHtml(datetime[0]));
                if (newTime != null) {
                    if (datetime[1].compareTo(newTime) >= 0) {
                        mTimeInButton.setClickable(true);
                        mTimeInButton.setEnabled(true);
                    }
                }
                someHandler.postDelayed(this, 1000);

            }
        }, 10);

        //get location
        init();
        restoreValuesFromBundle(savedInstanceState);
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

    private void updateattandencevalue() {
        initupdateAPIResources( PrefUtils.getKey(MainActivity.this, AppConstants.UserID), PrefUtils.getKey(MainActivity.this, AppConstants.Clientid),PrefUtils.getKey(MainActivity.this, AppConstants.Api_Token));
    }

    private void initupdateAPIResources(String userid,String clientid,String apitoken) {
        SignUpUpdatePresenterImpl loginPresenter = new SignUpUpdatePresenterImpl(MainActivity.this);
        loginPresenter.callApi(AppConstants.updated, userid,clientid,apitoken);
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putBoolean("is_requesting_updates", mRequestingLocationUpdates);
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

            getAddress();
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
                                    rae.startResolutionForResult(MainActivity.this, REQUEST_CHECK_SETTINGS);
                                } catch (IntentSender.SendIntentException sie) {
                                    Log.i(TAG, "PendingIntent unable to execute request.");
                                }
                                break;
                            case LocationSettingsStatusCodes.SETTINGS_CHANGE_UNAVAILABLE:
                                String errorMessage = "Location settings are inadequate, and cannot be " +
                                        "fixed here. Fix in Settings.";
                                Log.e(TAG, errorMessage);

                                Toast.makeText(MainActivity.this, errorMessage, Toast.LENGTH_LONG).show();
                        }

                        updateLocationUI();
                    }
                });
    }

    @OnClick(R.id.logout)
    public void logout() {

        if (NetworkUtil.isOnline(getApplicationContext())) {

            dialog = new ProgressDialog(MainActivity.this, R.style.AppCompatAlertDialogStyle);
            dialog.setMessage(getResources().getString(R.string.loading));
            dialog.setCanceledOnTouchOutside(false);
            dialog.show();


            initlogoutOutAPIResources(userid, PrefUtils.getKey(MainActivity.this, AppConstants.Clientid));


        }
        else
        {
            Intent i = new Intent(MainActivity.this, LoginActivity.class);
            i.putExtra("logout", "logout");
            i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
            i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(i);
        }
    }

    @OnClick(R.id.capture_button)
    public void capture_Image() {
        initImagePickProcess(MainActivity.this, null);
    }

    @OnClick(R.id.viewrecords_button)
    public void onViewRecordsClick() {
        Intent i = new Intent(MainActivity.this, EmployeelistActivity.class);
        startActivity(i);
        finish();
    }
    //button click when user timein or timeout
    @OnClick(R.id.timeIn_button)
    public void onINClick() {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd_HH:mm:ss");
        String currentDateandTime = sdf.format(new Date());
        String[] datetime = currentDateandTime.split("_");
        currenttime = datetime[1];
        currentdate = datetime[0];
        SimpleDateFormat df = new SimpleDateFormat("HH:mm");
        Date d = null;
        try {
            d = df.parse(datetime[1]);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        Calendar cal = Calendar.getInstance();
        cal.setTime(d);
        cal.add(Calendar.MINUTE, 1);
        newTime = df.format(cal.getTime());

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

        //timein/timeout funtionality
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            FingerprintManager fingerprintManager = (FingerprintManager) context.getSystemService(Context.FINGERPRINT_SERVICE);
            if(fingerprintManager!=null) {
                if (!fingerprintManager.isHardwareDetected()) {
                    entrytime();
                    // Device doesn't support fingerprint authentication
                } else if (!fingerprintManager.hasEnrolledFingerprints()) {
                    entrytime();
                    // User hasn't enrolled any fingerprints to authenticate with
                } else {
                    //fingure authentication method
                    authentication();
                   // entrytime();
                }
            }
            else
            {
                entrytime();
            }
        } else {
            //timein/timeout funtionality
            entrytime();
        }

    /* Intent i=new Intent(MainActivity.this,MainActivity1.class);
     startActivity(i);*/
      /* detector = new FaceDetector.Builder(getApplicationContext())
                .setTrackingEnabled(false)
                .setLandmarkType(FaceDetector.ALL_CLASSIFICATIONS)
                .setClassificationType(FaceDetector.ALL_CLASSIFICATIONS)
                .build();
       facelogin();*/

    }

    private void facelogin() {
        ActivityCompat.requestPermissions(MainActivity.this, new
                String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE,Manifest.permission.READ_EXTERNAL_STORAGE}, REQUEST_WRITE_PERMISSION);
    }
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch (requestCode) {
            case REQUEST_WRITE_PERMISSION:
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED&& grantResults[1] == PackageManager.PERMISSION_GRANTED) {
                    startCamera();
                } else {
                    Toast.makeText(getApplicationContext(), "Permission Denied!", Toast.LENGTH_SHORT).show();
                }
        }
    }

    private void startCamera() {
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        File photo = new File(Environment.getExternalStorageDirectory(), "photo.jpg");
        imageUri = FileProvider.getUriForFile(this, "com.wfms.nectar.wfms" + ".provider", photo);
        // imageUri = Uri.fromFile(photo);
        intent.putExtra(MediaStore.EXTRA_OUTPUT, imageUri);
        startActivityForResult(intent, CAMERA_REQUEST);
    }
    public void entrytime() {

        DatabaseHelper databaseHelper = new DatabaseHelper(context);
        EmployeData data = new EmployeData();

        data.setUser_id(PrefUtils.getKey(context, AppConstants.UserID));
        data.setIn_location(address);
        data.setIn_time(currenttime);
        data.setIn_location_url(locationURL);
        data.setIn_mac_address(deviceiD);
        data.setIsupload("false");
        data.setIslogin(PrefUtils.getKey(context, AppConstants.IsLogin));
        databaseHelper.TimeIn(data);

        // Snack Bar to show success message that record is wrong

        if (PrefUtils.getKey(context, AppConstants.IsLogin).equals("0")) {
            PrefUtils.storeKey(context, "date", currentdate);
            PrefUtils.storeKey(context, "time", currenttime);
            PrefUtils.storeKey(context, "address", address);
            PrefUtils.storeKey(context, "locationurl", locationURL);
            timein_text.setText("Time Out");
            timein_img.setBackgroundResource(R.drawable.time_out);
            PrefUtils.storeKey(context, AppConstants.IsLogin, "1");
            storetimeinvalue();
            SharedPreferences pref = getApplicationContext().getSharedPreferences(Config.SHARED_PREF, 0);
            String regId = pref.getString("regId", null);
            initnotificationAPIResources(userid, regId, currenttime, PrefUtils.getKey(MainActivity.this, AppConstants.Clientid),PrefUtils.getKey(MainActivity.this, AppConstants.Api_Token));
            initadminnotificationAPIResources(userid, PrefUtils.getKey(MainActivity.this, AppConstants.Name), PrefUtils.getKey(MainActivity.this, AppConstants.Clientid),PrefUtils.getKey(MainActivity.this, AppConstants.Api_Token));
            Log.d("userid",userid);
            Log.d("name",PrefUtils.getKey(MainActivity.this, AppConstants.Name));
            Log.d("clientid",PrefUtils.getKey(MainActivity.this, AppConstants.Clientid));
            Log.d("apitoken",PrefUtils.getKey(MainActivity.this, AppConstants.Api_Token));


        } else if (PrefUtils.getKey(context, AppConstants.IsLogin).equals("1")) {
            timein_text.setText("Time In");
            timein_img.setBackgroundResource(R.drawable.time_in);
            PrefUtils.storeKey(context, AppConstants.IsLogin, "0");
            storetimeOutvalue();

        }
        mTimeInButton.setClickable(false);
        mTimeInButton.setEnabled(false);

    }

    //timein data store in Localdatabase
    private void storetimeinvalue() {
        DatabaseHelper databaseHelper = new DatabaseHelper(context);
        EmployeData data = new EmployeData();
        data.setUser_id(PrefUtils.getKey(context, AppConstants.UserID));
        data.setUsername(PrefUtils.getKey(context, AppConstants.Name));
        data.setAttendance_id("50000");
        data.setIn_date(currentdate);
        data.setIn_time(currenttime);
        data.setIn_location(address);
        data.setIn_location_url(locationURL);
        data.setOut_date("null");
        data.setOut_time(null);
        data.setOut_location(null);
        data.setOut_location_url(null);
        databaseHelper.UserRecords(data);


    }

    //timeout data store in Localdatabase
    private void storetimeOutvalue() {
        DatabaseHelper databaseHelper = new DatabaseHelper(context);
        EmployeData data = new EmployeData();

        data.setUser_id(PrefUtils.getKey(context, AppConstants.UserID));
        data.setUsername(PrefUtils.getKey(context, AppConstants.Name));
        data.setAttendance_id("50000");
        data.setOut_date(currentdate);
        data.setOut_time(currenttime);
        data.setOut_location(address);
        data.setOut_location_url(locationURL);
        databaseHelper.UserupdateRecords(data);

    }

    //get address from latitude and lonitude
    public Address getAddress1(double latitude, double longitude) {
        Geocoder geocoder;
        List<Address> addresses;
        geocoder = new Geocoder(this, Locale.getDefault());

        try {
            addresses = geocoder.getFromLocation(latitude, longitude, 1); // Here 1 represent max location result to returned, by documents it recommended 1 to 5
            return addresses.get(0);

        } catch (IOException e) {
            e.printStackTrace();
        }

        return null;

    }

    //get full address
    public void getAddress() {

        Address locationAddress = getAddress1(latitude, longitude);
        // Address locationAddress=getAddress(-8.923255,13.194977);
        if (locationAddress != null) {
            address = locationAddress.getAddressLine(0);
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

                // Toast.makeText(getApplicationContext(),address,Toast.LENGTH_LONG).show();
            }

        }

    }

    @Override
    public void onSignUpSuccess(SignUpResponse signUpResponse) {

        dialog.dismiss();
        Intent i = new Intent(MainActivity.this, LoginActivity.class);
        i.putExtra("logout", "logout");
        i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
        i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        PrefUtils.storeKey(getApplicationContext(), AppConstants.LogoutUserID, null);
        startActivity(i);

    }

    private void initnotificationAPIResources(String userid, String regId, String currenttime,String clientid,String apitoken) {
        SignUpPresenterImplnotification loginPresenter = new SignUpPresenterImplnotification(MainActivity.this);
        loginPresenter.callApi(AppConstants.Notification, userid, regId, currenttime,clientid,apitoken);

    }

    private void initadminnotificationAPIResources(String userid, String username,String clientid,String apitoken) {
        SignUpadminPresenterImpl loginPresenter = new SignUpadminPresenterImpl(MainActivity.this);
        loginPresenter.callApi(AppConstants.AdminNotification, userid, username,clientid,apitoken);

    }

    private void initlogoutOutAPIResources(String userid,String clientid) {
        SignUpPresenterImpl loginPresenter = new SignUpPresenterImpl(MainActivity.this);
        loginPresenter.callApi(AppConstants.Logout, userid,clientid,PrefUtils.getKey(MainActivity.this, AppConstants.Api_Token));

    }
    //failure method when user timein
    @Override
    public void onSignUpFailure(String msg) {
        Log.d("fail", msg);
         dialog.dismiss();
        Snackbar.make(main_layout, msg, Snackbar.LENGTH_LONG).show();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.drawer, menu);
        MenuItem item = menu.findItem(R.id. dashboard);
        if (PrefUtils.getKey(MainActivity.this, AppConstants.UserID).equals("1")) {
            item.setVisible(false);
        }
        else
        {
            item.setVisible(true);
        }
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.logout:

               if (NetworkUtil.isOnline(getApplicationContext())) {
                    dialog = new ProgressDialog(MainActivity.this, R.style.AppCompatAlertDialogStyle);
                    dialog.setMessage(getResources().getString(R.string.loading));
                    dialog.setCanceledOnTouchOutside(false);
                    dialog.show();
                    initlogoutOutAPIResources(userid, PrefUtils.getKey(MainActivity.this, AppConstants.Clientid));

                }
                else
                {
                    Intent i = new Intent(MainActivity.this, LoginActivity.class);
                    i.putExtra("logout", "logout");
                    i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
                    i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    startActivity(i);
                }
                return true;
            case R.id.reset:
                Intent i1 = new Intent(MainActivity.this, ResetPinActivity.class);
                startActivity(i1);
                return true;
            case R.id.setting:
                Intent i2 = new Intent(MainActivity.this, SettingActivity.class);
                startActivity(i2);
                return true;
            case R.id.dashboard:
                Intent i3 = new Intent(MainActivity.this, DashboardActivity.class);
                startActivity(i3);
                return true;
            case R.id.leave:
                Intent i4 = new Intent(MainActivity.this, LeaveActivity.class);
                startActivity(i4);
                return true;
            case R.id.leave_summary:
                Intent i5 = new Intent(MainActivity.this, LeaveSummaryActivity.class);
                startActivity(i5);
                return true;
            case R.id.profile:
                Intent i6 = new Intent(MainActivity.this, ProfileActivity.class);
                startActivity(i6);
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    //success method when user timeout
    @Override
    public void onSignUpSuccessOut(SignUpResponse signUpResponse) {
        dialog.dismiss();
        islogin = false;
        timein_text.setText("Time In");
        timein_img.setBackgroundResource(R.drawable.time_in);
        PrefUtils.storeKey(MainActivity.this, AppConstants.IsLogin, "0");
    }

    //failure method when user timeout
    @Override
    public void onSignUpFailureOut(String msg) {
        Log.d("msg", msg);
        dialog.dismiss();
        islogin = false;
        Snackbar.make(main_layout, "Something is wrong,Please try again", Snackbar.LENGTH_LONG).show();
    }

    //finger authentiation
    @RequiresApi(api = Build.VERSION_CODES.M)
    public void authentication() {
        final Dialog dialog = new Dialog(context);
        dialog.setContentView(R.layout.finger_auhtentication_layout);
        dialog.setTitle("Title...");

        KeyguardManager keyguardManager = (KeyguardManager) getSystemService(KEYGUARD_SERVICE);
        FingerprintManager fingerprintManager = (FingerprintManager) getSystemService(FINGERPRINT_SERVICE);

        TextView textView = (TextView) dialog.findViewById(R.id.errorText);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (!fingerprintManager.isHardwareDetected()) {
                /**
                 * An error message will be displayed if the device does not contain the fingerprint hardware.
                 * However if you plan to implement a default authentication method,
                 * you can redirect the user to a default authentication activity from here.
                 * Example:
                 * Intent intent = new Intent(this, DefaultAuthenticationActivity.class);
                 * startActivity(intent);
                 */
                textView.setText("Your Device does not have a Fingerprint Sensor");
            } else {
                // Checks whether fingerprint permission is set on manifest
                if (ActivityCompat.checkSelfPermission(this, Manifest.permission.USE_FINGERPRINT) != PackageManager.PERMISSION_GRANTED) {
                    textView.setText("Fingerprint authentication permission not enabled");
                } else {
                    // Check whether at least one fingerprint is registered
                    if (!fingerprintManager.hasEnrolledFingerprints()) {
                        textView.setText("Register at least one fingerprint in Settings");
                    } else {
                        // Checks whether lock screen security is enabled or not
                        if (!keyguardManager.isKeyguardSecure()) {
                            textView.setText("Lock screen security not enabled in Settings");
                        } else {
                            generateKey();


                            if (cipherInit()) {
                                FingerprintManager.CryptoObject cryptoObject = new FingerprintManager.CryptoObject(cipher);
                                FingerprintHandler helper = new FingerprintHandler(this, textView, dialog, userid, address, currenttime, locationURL, deviceiD, mTimeInButton, currentdate,timein_text,timein_img);
                                helper.startAuth(fingerprintManager, cryptoObject);

                                if (FingerprintHandler.isyou) {
                                    dialog.dismiss();
                                    //  Toast.makeText(MainActivity.this, "Aunthentication successs", Toast.LENGTH_SHORT).show();
                                } else {
                                    dialog.dismiss();
                                    //Toast.makeText(MainActivity.this, "Aunthentication Fail", Toast.LENGTH_SHORT).show();
                                }
                            }
                        }
                    }
                }
            }
        }
        dialog.show();
    }

    @TargetApi(Build.VERSION_CODES.M)
    protected void generateKey() {
        try {
            keyStore = KeyStore.getInstance("AndroidKeyStore");
        } catch (Exception e) {
            e.printStackTrace();
        }


        KeyGenerator keyGenerator;
        try {
            keyGenerator = KeyGenerator.getInstance(KeyProperties.KEY_ALGORITHM_AES, "AndroidKeyStore");
        } catch (NoSuchAlgorithmException | NoSuchProviderException e) {
            throw new RuntimeException("Failed to get KeyGenerator instance", e);
        }


        try {
            keyStore.load(null);
            keyGenerator.init(new
                    KeyGenParameterSpec.Builder(KEY_NAME,
                    KeyProperties.PURPOSE_ENCRYPT |
                            KeyProperties.PURPOSE_DECRYPT)
                    .setBlockModes(KeyProperties.BLOCK_MODE_CBC)
                    .setUserAuthenticationRequired(true)
                    .setEncryptionPaddings(
                            KeyProperties.ENCRYPTION_PADDING_PKCS7)
                    .build());
            keyGenerator.generateKey();
        } catch (NoSuchAlgorithmException |
                InvalidAlgorithmParameterException
                | CertificateException | IOException e) {
            throw new RuntimeException(e);
        }
    }

    @TargetApi(Build.VERSION_CODES.M)
    public boolean cipherInit() {
        try {
            cipher = Cipher.getInstance(KeyProperties.KEY_ALGORITHM_AES + "/" + KeyProperties.BLOCK_MODE_CBC + "/" + KeyProperties.ENCRYPTION_PADDING_PKCS7);
        } catch (NoSuchAlgorithmException | NoSuchPaddingException e) {
            throw new RuntimeException("Failed to get Cipher", e);
        }

        try {
            keyStore.load(null);
            SecretKey key = (SecretKey) keyStore.getKey(KEY_NAME,
                    null);
            cipher.init(Cipher.ENCRYPT_MODE, key);
            return true;
        } catch (KeyPermanentlyInvalidatedException e) {
            return false;
        } catch (KeyStoreException | CertificateException | UnrecoverableKeyException | IOException | NoSuchAlgorithmException | InvalidKeyException e) {
            throw new RuntimeException("Failed to init Cipher", e);
        }
    }

    @Override
    protected void onNewImagePickSuccess(Uri uri, File tempFile) {
        if (imagesEncodedList.size() > 0) {
            StringBuilder sb = new StringBuilder();
            String s = null;
            String filepath;

            Bitmap bitmap = BitmapFactory.decodeFile(imagesEncodedList.get(0));

            //open dialog for view and save image
            saveimagedialog(bitmap, imagesEncodedList.get(0));

        } else {
            String absoluteFilePath = GalleryCameraUtils.getRealPathFromURI(uri, getApplicationContext());
            if (absoluteFilePath != null) {
                imagesEncodedList.add(absoluteFilePath);
                //  Log.d("absoluteFilePath",absoluteFilePath);
                setSelectedImage(absoluteFilePath != null && !absoluteFilePath.isEmpty()
                        ? new File(absoluteFilePath) : tempFile);
            } else {
                Toast.makeText(getApplicationContext(), "you can not upload from here", Toast.LENGTH_LONG).show();
            }


        }

    }

    private void saveimagedialog(Bitmap bitmap, final String filepath) {
        final Dialog dialog = new Dialog(MainActivity.this);
        dialog.setContentView(R.layout.saveimage_layout);
        dialog.setTitle("Title...");
        ImageView image = (ImageView) dialog.findViewById(R.id.image);
        AppCompatButton save = (AppCompatButton) dialog.findViewById(R.id.save_button);
        AppCompatButton cancel = (AppCompatButton) dialog.findViewById(R.id.cancel_button);
        image.setImageBitmap(bitmap);
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


        save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (NetworkUtil.isOnline(getApplicationContext())) {
                    uploadimage(dialog);
                } else {
                    saveimageintodatabase(filepath);
                    dialog.dismiss();
                    Toast.makeText(getApplicationContext(), "No internet connection your data store in local", Toast.LENGTH_SHORT).show();
                }

            }
        });

        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.dismiss();
            }
        });
        dialog.show();
    }

    private void saveimageintodatabase(String filepath) {
        DatabaseHelper databaseHelper = new DatabaseHelper(context);
        EmployeData data = new EmployeData();
        data.setUser_id(PrefUtils.getKey(context, AppConstants.UserID));
        data.setLatitudee("" + latitude);
        data.setLongtitude("" + longitude);
        data.setImage(filepath);
        databaseHelper.SaveImage(data);
    }

    // api integration for save image
    private void uploadimage(final Dialog pdialog) {
        try {
            this.dialog = new ProgressDialog(this, R.style.AppCompatAlertDialogStyle);

            this.dialog.setIndeterminate(false);

            this.dialog.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);

            this.dialog.setCancelable(false);

            this.dialog.setMax(100);

            MultipartBody.Part fileToUpload = null;

            if (imagesEncodedList.size() > 0) {
                for (int i = 0; i < imagesEncodedList.size(); i++) {
                    File filePath = new File(imagesEncodedList.get(i));
                    ProgressRequestBody fileBody = new ProgressRequestBody(filePath, this);
                    fileToUpload = MultipartBody.Part.createFormData("image" +
                            "", filePath.getName(), fileBody);
                    this.dialog.setMessage("Uploading file " + filePath.getName());
                    this.dialog.show();
                }
            }

            RequestBody user_id = RequestBody.create(MediaType.parse("text/plain"), PrefUtils.getKey(MainActivity.this, AppConstants.UserID));
            RequestBody lat = RequestBody.create(MediaType.parse("text/plain"), "" + latitude);
            RequestBody longi = RequestBody.create(MediaType.parse("text/plain"), "" + longitude);
            RequestBody clientid = RequestBody.create(MediaType.parse("text/plain"), PrefUtils.getKey(MainActivity.this, AppConstants.Clientid));
            Log.d(TAG, "MainActivity: " + PrefUtils.getKey(MainActivity.this, AppConstants.Clientid));
            RequestBody apitoken = RequestBody.create(MediaType.parse("text/plain"), PrefUtils.getKey(MainActivity.this, AppConstants.Api_Token));

            Call<JsonObject> call = WfmslApplication.mRetroClient.callUploadInvoiceAPi(user_id,
                    lat, longi,clientid, fileToUpload,apitoken);

            call.enqueue(new CallbackWithRetry<JsonObject>(call) {
                @Override
                public void onResponse(Call<JsonObject> call, Response<JsonObject> response) {
                    MainActivity.this.dialog.dismiss();
                    pdialog.dismiss();
                    Log.d(TAG, "onResponse: " + response.body());

                    //   if (loadingProgressBar != null) loadingProgressBar.setVisibility(View.GONE);
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
                        Toast.makeText(getApplicationContext(), "Save Successfully", Toast.LENGTH_LONG).show();

                    } else {
                        MainActivity.this.dialog.dismiss();
                        Toast.makeText(getApplicationContext(), getResources().getString(R.string.try_again), Toast.LENGTH_LONG).show();
                    }
                }

                @Override
                public void onFailure(Call<JsonObject> call, Throwable t) {
                    Toast.makeText(getApplicationContext(), getResources().getString(R.string.try_again), Toast.LENGTH_LONG).show();
                    Log.d(TAG, "toString(): " + t.toString());
                    MainActivity.this.dialog.dismiss();


                }
            });

        } catch (Exception e) {
            Log.d(TAG, e.getMessage());
        }

    }

    public void setSelectedImage(File absolutePath) {
        if (absolutePath != null) {

        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == CAMERA_REQUEST && resultCode == RESULT_OK) {
            Log.d("1111apeksha","1111apeksha");
            launchMediaScanIntent();

            try {

                processCameraPicture();
            } catch (Exception e) {
                Toast.makeText(getApplicationContext(), "Failed to load Image", Toast.LENGTH_SHORT).show();
            }
        }
        if (requestCode == FilePickerConst.REQUEST_CODE)
            if (resultCode == RESULT_OK && data != null) {
                filePaths = data.getStringArrayListExtra(FilePickerConst.KEY_SELECTED_PHOTOS);
                //   imagesEncodedList.addAll(filePaths);
                //  Log.d("filespath", String.valueOf(imagesEncodedList.size()));

                if (filePaths.size() > 0) {
                    StringBuilder sb = new StringBuilder();
                    String s = null;
                    for (int i = 0; i < filePaths.size(); i++) {

                        String docfile = filePaths.get(i);
                        File file = new File(docfile);

                        sb.append(file.getName());
                        sb.append("\n");
                        // setSelectedImage(file);
                        s = sb.toString();
                    }
                    // text_upload_image.setText(s);
                    //delete.setVisibility(View.VISIBLE);
                }

                //use them anywhere
            } else if (requestCode == AppConstants.REQUEST_OPEN_DOCUMENTS_FOLDER) {
                if (resultCode == RESULT_OK) {

                    if (data != null) {
                        Uri uri = data.getData();
                        File f = null;
                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                            InputStream is = null;
                            try {
                                is = getApplicationContext().getContentResolver().openInputStream(uri);
                                File tempFile = FileUtils.createTempPdfFile(getApplicationContext());
                                org.apache.commons.io.FileUtils.copyInputStreamToFile(is, tempFile);
                                setSelectedImage(tempFile);
                            } catch (FileNotFoundException e) {
                                e.printStackTrace();
                            } catch (IOException e) {
                                e.printStackTrace();
                            }
                        }
                    }
                }
            } else if (requestCode == PICK_PDF_REQUEST && resultCode == RESULT_OK && data != null && data.getData() != null) {
                fileuri = data.getData();
                //File f = new File(filePath.toString());
                String filename = (fileuri.toString()).substring((fileuri.toString()).lastIndexOf("/") + 1);
                File file = new File(String.valueOf(fileuri));
                String strFileName = file.getName();
                //  text_upload_image.setText(filename);
                // delete.setVisibility(View.VISIBLE);


            }
        if (this.requestCode == requestCode && resultCode == RESULT_OK) {
            Bitmap bitmap = (Bitmap) data.getExtras().get("data");

        }


    }
    private void launchMediaScanIntent() {
        Intent mediaScanIntent = new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE);
        mediaScanIntent.setData(imageUri);
        this.sendBroadcast(mediaScanIntent);
    }
    private void processCameraPicture() throws Exception {
        Bitmap bitmap = decodeBitmapUri(this, imageUri);
        if (detector.isOperational() && bitmap != null) {
            editedBitmap = Bitmap.createBitmap(bitmap.getWidth(), bitmap
                    .getHeight(), bitmap.getConfig());
            float scale = getResources().getDisplayMetrics().density;
            Paint paint = new Paint(Paint.ANTI_ALIAS_FLAG);
            paint.setColor(Color.GREEN);
            paint.setTextSize((int) (16 * scale));
            paint.setShadowLayer(1f, 0f, 1f, Color.WHITE);
            paint.setStyle(Paint.Style.STROKE);
            paint.setStrokeWidth(6f);
            Canvas canvas = new Canvas(editedBitmap);
            canvas.drawBitmap(bitmap, 0, 0, paint);
            Frame frame = new Frame.Builder().setBitmap(editedBitmap).build();
            SparseArray<Face> faces = detector.detect(frame);
          //  txtTakenPicDesc.setText(null);
            for (int index = 0; index < faces.size(); ++index) {
                Face face = faces.valueAt(index);
                canvas.drawRect(
                        face.getPosition().x,
                        face.getPosition().y,
                        face.getPosition().x + face.getWidth(),
                        face.getPosition().y + face.getHeight(), paint);

                canvas.drawText("Face " + (index + 1), face.getPosition().x + face.getWidth(), face.getPosition().y + face.getHeight(), paint);
                entrytime();
               /* txtTakenPicDesc.setText("FACE " + (index + 1) + "\n");
                txtTakenPicDesc.setText(txtTakenPicDesc.getText() + "Smile probability:" + " " + face.getIsSmilingProbability() + "\n");
                txtTakenPicDesc.setText(txtTakenPicDesc.getText() + "Left Eye Is Open Probability: " + " " + face.getIsLeftEyeOpenProbability() + "\n");
                txtTakenPicDesc.setText(txtTakenPicDesc.getText() + "Right Eye Is Open Probability: " + " " + face.getIsRightEyeOpenProbability() + "\n\n");
*/
                for (Landmark landmark : face.getLandmarks()) {
                    int cx = (int) (landmark.getPosition().x);
                    int cy = (int) (landmark.getPosition().y);
                    canvas.drawCircle(cx, cy, 8, paint);
                }
            }

            if (faces.size() == 0) {
                Toast.makeText(MainActivity.this,"Scan Failed: Found nothing to scan",Toast.LENGTH_LONG).show();
               // txtTakenPicDesc.setText("Scan Failed: Found nothing to scan");
            } else {
                Toast.makeText(MainActivity.this,"No of Faces Detected: " + " " + String.valueOf(faces.size()),Toast.LENGTH_LONG).show();
              //  imgTakePicture.setImageBitmap(editedBitmap);
              //  txtTakenPicDesc.setText(txtTakenPicDesc.getText() + "No of Faces Detected: " + " " + String.valueOf(faces.size()));
            }
        } else {
            Toast.makeText(MainActivity.this,"Could not set up the detector!",Toast.LENGTH_LONG).show();
          //  txtTakenPicDesc.setText("Could not set up the detector!");
        }
    }

    private Bitmap decodeBitmapUri(Context ctx, Uri uri) throws FileNotFoundException {
        int targetW = 300;
        int targetH = 300;
        Log.d("uri",""+uri);
        BitmapFactory.Options bmOptions = new BitmapFactory.Options();
        bmOptions.inJustDecodeBounds = true;
        bmOptions.inSampleSize = 2;
        try{
            BitmapFactory.decodeStream(ctx.getContentResolver().openInputStream(uri), null, bmOptions);
        }catch (Exception e)
        {
            e.printStackTrace();
        }

        int photoW = bmOptions.outWidth;
        int photoH = bmOptions.outHeight;

        int scaleFactor = Math.min(photoW / targetW, photoH / targetH);
        bmOptions.inJustDecodeBounds = false;
        bmOptions.inSampleSize = scaleFactor;

        return BitmapFactory.decodeStream(ctx.getContentResolver()
                .openInputStream(uri), null, bmOptions);
    }
    // show progress when upload image
    @Override
    public void onProgressUpdate(int percentage) {
        dialog.setProgress(percentage);
    }

    @Override
    public void onError() {
    }

    @Override
    public void onFinish() {
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
    public void onSignUpUpdateSuccess(SignUpResponse signUpResponse) {
        pullToRefresh.setRefreshing(false);
        if (signUpResponse.getAttendance_status().equalsIgnoreCase("1")) {
            timein_text.setText("Time Out");
            timein_img.setBackgroundResource(R.drawable.time_out);
        } else {
            timein_text.setText("Time In");
            timein_img.setBackgroundResource(R.drawable.time_in);
        }
        PrefUtils.storeKey(MainActivity.this, AppConstants.IsLogin, signUpResponse.getAttendance_status());
    }

    @Override
    public void onSignUpUpdateFailure(String msg) {


    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
       //detector.release();
    }

}

