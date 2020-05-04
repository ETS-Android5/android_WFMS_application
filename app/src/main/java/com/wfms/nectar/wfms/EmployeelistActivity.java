package com.wfms.nectar.wfms;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.IntentSender;
import android.graphics.Color;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.appcompat.widget.Toolbar;

import android.os.Looper;
import android.provider.Settings;
import android.text.Html;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.text.method.LinkMovementMethod;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

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
import com.karumi.dexter.Dexter;
import com.karumi.dexter.PermissionToken;
import com.karumi.dexter.listener.PermissionDeniedResponse;
import com.karumi.dexter.listener.PermissionGrantedResponse;
import com.karumi.dexter.listener.single.PermissionListener;
import com.wfms.nectar.Adapter.Employe_Adapter;
import com.wfms.nectar.jsonModelResponses.Fuel.EmployeData;
import com.wfms.nectar.jsonModelResponses.Fuel.EmployeResponse;
import com.wfms.nectar.jsonModelResponses.signup.SignUpResponse;
import com.wfms.nectar.listener.RecyclerTouchListener;
import com.wfms.nectar.presenter.presenterImpl.GetAlEmployePresenterImpl;
import com.wfms.nectar.presenter.presenterImpl.SignUpPresenterImpl;
import com.wfms.nectar.sql.DatabaseHelper;
import com.wfms.nectar.utils.AppConstants;
import com.wfms.nectar.utils.NetworkUtil;
import com.wfms.nectar.utils.PrefUtils;
import com.wfms.nectar.viewstate.EmployeView;
import com.wfms.nectar.viewstate.SignUpView;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;

/**
 * Created by Nectar on 24-09-2018.
 */

public class EmployeelistActivity extends AppCompatActivity implements EmployeView,SignUpView {

    ImageView logout;
    ArrayList<EmployeData> employelist1;
    ArrayList<EmployeData> employelist;
    ArrayList<EmployeData> filteremployelist;
    ArrayList<EmployeData> tempemployelist;
    RecyclerView employe_list;
    Employe_Adapter adapter;
    ProgressDialog dialog;
    Toolbar toolbar;
    ImageView search;
    RelativeLayout back;
    TextWatcher mSearchTw;
    EmployeData data;
    String myFormat, userid;
    DatabaseHelper databaseHelper;
    SwipeRefreshLayout pullToRefresh;
    AppCompatButton load_more;
    String ataandenceid = "0";
    RelativeLayout layout;
    boolean isrefresh=false;
    int aid;
    private static final String TAG = MainActivity.class.getSimpleName();
    private final static int REQUEST_CHECK_SETTINGS = 2000;
    boolean iscreate=true;
    public static BufferedWriter out;
    ProgressBar p;
    private FusedLocationProviderClient mFusedLocationClient;
    private SettingsClient mSettingsClient;
    private LocationRequest mLocationRequest;
    private LocationSettingsRequest mLocationSettingsRequest;
    private LocationCallback mLocationCallback;
    private Location mCurrentLocation;
    private static final long UPDATE_INTERVAL_IN_MILLISECONDS = 10000;
    private static final long FASTEST_UPDATE_INTERVAL_IN_MILLISECONDS = 5000;
    private String mLastUpdateTime,locationURL;
    private Boolean mRequestingLocationUpdates;
    public static double latitude=0.0;
    public static double longitude=0.0;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.employee_list);
        employelist = new ArrayList<>();
        tempemployelist = new ArrayList<>();
        employelist1= new ArrayList<>();
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        load_more = (AppCompatButton) findViewById(R.id.load_more);
        layout=(RelativeLayout) findViewById(R.id.layout);
        p=(ProgressBar)findViewById(R.id.progressbar) ;
        pullToRefresh = findViewById(R.id.pullToRefresh);
        setSupportActionBar(toolbar);
        toolbar.setTitleTextColor(Color.WHITE);
        getSupportActionBar().setTitle("Records");

        userid = PrefUtils.getKey(EmployeelistActivity.this, AppConstants.UserID);
        employe_list = (RecyclerView) findViewById(R.id.employe_list);
        search = (ImageView) findViewById(R.id.search_employe);
        logout = (ImageView) findViewById(R.id.logout);
        back = (RelativeLayout) findViewById(R.id.back_layout);
        employe_list.setHasFixedSize(true);
        employe_list.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
        employe_list.setItemAnimator(new DefaultItemAnimator());
        try {
            createFileOnDevice(iscreate);
        } catch (IOException e) {
            e.printStackTrace();
        }
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
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(PrefUtils.getKey(EmployeelistActivity.this,AppConstants.UserID).equalsIgnoreCase("1"))
                {
                    finish();
                }
                else
                {
                    Intent i = new Intent(EmployeelistActivity.this, MainActivity.class);
                    startActivity(i);
                    finish();
                }
            }
        });

        adapter = new Employe_Adapter(EmployeelistActivity.this, employelist);
        employe_list.setAdapter(adapter);
        employe_list.addOnItemTouchListener(new RecyclerTouchListener(this, employe_list, new RecyclerTouchListener.ClickListener() {
            @Override
            public void onClick(View view, int position) {

                // Write your code here
                final Dialog dialog = new Dialog(EmployeelistActivity.this);
                dialog.setContentView(R.layout.location_layout);
                dialog.setTitle("Title...");

                // set the custom dialog components - text, image and button
                TextView in_location = (TextView) dialog.findViewById(R.id.in_location);
                TextView out_location = (TextView) dialog.findViewById(R.id.out_location);
                TextView In_location_url = (TextView) dialog.findViewById(R.id.In_location_url);
                TextView Out_location_url = (TextView) dialog.findViewById(R.id.Out_location_url);

                if (employelist.get(position).getIn_location() != null) {
                    if (!dialog.isShowing()) {
                        dialog.show();
                    }
                    in_location.setVisibility(View.VISIBLE);
                    in_location.setText(Html.fromHtml("<b>InLocation </b>" + employelist.get(position).getIn_location()));
                } else {
                    in_location.setVisibility(View.GONE);
                }
                if (employelist.get(position).getOut_location() != null) {
                    if (!dialog.isShowing()) {
                        dialog.show();
                    }
                    out_location.setVisibility(View.VISIBLE);
                    out_location.setText(Html.fromHtml("<b>OutLocation </b>" + employelist.get(position).getOut_location()));
                } else {
                    out_location.setVisibility(View.GONE);
                }
                if (employelist.get(position).getIn_location_url() != null) {
                    if (!dialog.isShowing()) {
                        dialog.show();
                    }
                    String url = employelist.get(position).getIn_location_url();
                    In_location_url.setVisibility(View.VISIBLE);
                    In_location_url.setText(Html.fromHtml("<b>TimeIn location URL :<b>" + url));
                    In_location_url.setMovementMethod(LinkMovementMethod.getInstance());// make it active
                } else {
                    In_location_url.setVisibility(View.GONE);
                }
                if (employelist.get(position).getOut_location_url() != null) {
                    if (!dialog.isShowing()) {
                        dialog.show();
                    }
                    String url = employelist.get(position).getOut_location_url();
                    Out_location_url.setVisibility(View.VISIBLE);
                    Out_location_url.setText(Html.fromHtml("<b>TimeOut location URL :<b>" + url));
                    Out_location_url.setMovementMethod(LinkMovementMethod.getInstance());// make it active;
                } else {
                    Out_location_url.setVisibility(View.GONE);
                }
                Button dialogButton = (Button) dialog.findViewById(R.id.cancel);
                // if button is clicked, close the custom dialog
                dialogButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        dialog.dismiss();
                    }
                });
            }

            @Override
            public void onLongClick(View view, int position) {

            }
        }));

        //get all records of employee
        getEmployelist();
      //
        //  writeLogCat();
        pullToRefresh.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                employelist = new ArrayList<>();
                isrefresh=true;
                getEmployelist(); // your code

                pullToRefresh.setRefreshing(false);
            }
        });
        load_more.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                isrefresh=false;
                for (int i = 0; i < employelist.size(); i++) {
                    ataandenceid = employelist.get(i).getAttendance_id();
                }
                Log.d("ataandenceid", ataandenceid);
                getEmployelist();
            }
        });

        //add filter record according to date
        search.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                employelist=new ArrayList<>();
                pullToRefresh.setEnabled(false);
                final Calendar myCalendar = Calendar.getInstance();
                DatePickerDialog.OnDateSetListener date = new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                        // TODO Auto-generated method stub
                        load_more.setTextColor(getResources().getColor(R.color.ntv_white));
                        load_more.setBackgroundColor(getResources().getColor(R.color.colorPrimaryDark));
                         ataandenceid="0";
                        myCalendar.set(Calendar.YEAR, year);
                        myCalendar.set(Calendar.MONTH, monthOfYear);
                        myCalendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);
                        String myFormat = "yyyy-MM-dd"; // your format
                        SimpleDateFormat sdf = new SimpleDateFormat(myFormat, Locale.getDefault());
                        String date = sdf.format(myCalendar.getTime());
                        Log.d("dateddddd",date);
                        Log.d("ataandenceid",ataandenceid);
                        if (NetworkUtil.isOnline(getApplicationContext())) {
                            employelist=new ArrayList<>();
                            p.setVisibility(View.VISIBLE);
                            initGetEmployeeFilterListAPIResources(userid, date,ataandenceid,PrefUtils.getKey(EmployeelistActivity.this,AppConstants.Clientid),PrefUtils.getKey(EmployeelistActivity.this,AppConstants.Api_Token));

                        } else {

                            load_more.setTextColor(getResources().getColor(R.color.ntv_white));
                            load_more.setBackgroundColor(getResources().getColor(R.color.disable_color));
                            senddataTimeInfromSqlite();
                            String[] currentdate = date.split("_");
                            String[] currentmonth = currentdate[0].split("-");
                            filteremployelist = new ArrayList<>();
                            for (int i = 0; i < employelist.size(); i++) {
                                data = new EmployeData();
                                if (employelist.get(i).getIn_date() != null) {
                                    String ddate = employelist.get(i).getIn_date();

                                    String[] selecteddate = ddate.split("_");
                                    String[] selectedmonth = selecteddate[0].split("-");
                                    String s1 = selectedmonth[1];

                                    if (Integer.parseInt(currentmonth[1]) == Integer.parseInt(selectedmonth[1])) {
                                        if (Integer.parseInt(currentmonth[2]) == Integer.parseInt(selectedmonth[2])) {
                                            data.setUser_id(employelist.get(i).getUser_id());
                                            data.setUsername(employelist.get(i).getUsername());
                                            data.setAttendance_id(employelist.get(i).getAttendance_id());
                                            data.setIn_date(employelist.get(i).getIn_date());
                                            data.setIn_time(employelist.get(i).getIn_time());
                                            data.setOut_date(employelist.get(i).getOut_date());
                                            data.setOut_time(employelist.get(i).getOut_time());
                                            data.setIn_location(employelist.get(i).getIn_location());
                                            data.setOut_location(employelist.get(i).getOut_location());
                                            data.setTime_duration(employelist.get(i).getTime_duration());
                                            data.setIn_location_url(employelist.get(i).getIn_location_url());
                                            data.setOut_location_url(employelist.get(i).getOut_location_url());
                                            filteremployelist.add(data);
                                        }
                                    }
                                } else if (employelist.get(i).getOut_date() != null) {
                                    String ddate = employelist.get(i).getOut_date();
                                    String[] selecteddate = ddate.split("_");
                                    String[] selectedmonth = selecteddate[0].split("-");

                                    if (Integer.parseInt(currentmonth[1]) == Integer.parseInt(selectedmonth[1])) {
                                        if (Integer.parseInt(currentmonth[2]) == Integer.parseInt(selectedmonth[2])) {
                                            data.setUser_id(employelist.get(i).getUser_id());
                                            data.setUsername(employelist.get(i).getUsername());
                                            data.setAttendance_id(employelist.get(i).getAttendance_id());
                                            data.setIn_date(employelist.get(i).getIn_date());
                                            data.setIn_time(employelist.get(i).getIn_time());
                                            data.setOut_date(employelist.get(i).getOut_date());
                                            data.setOut_time(employelist.get(i).getOut_time());
                                            data.setIn_location(employelist.get(i).getIn_location());
                                            data.setOut_location(employelist.get(i).getOut_location());
                                            data.setTime_duration(employelist.get(i).getTime_duration());
                                            data.setIn_location_url(employelist.get(i).getIn_location_url());
                                            data.setOut_location_url(employelist.get(i).getOut_location_url());
                                            filteremployelist.add(data);
                                        }
                                    }
                                }
                            }
                            if (filteremployelist.size() > 0) {
                                employe_list.setVisibility(View.VISIBLE);
                                adapter = new Employe_Adapter(EmployeelistActivity.this, filteremployelist);
                                employe_list.setAdapter(adapter);
                            } else {
                                employe_list.setVisibility(View.GONE);
                                Toast.makeText(getApplicationContext(), "No Record found ", Toast.LENGTH_SHORT).show();
                            }


                        }
                    }
                };
                new DatePickerDialog(EmployeelistActivity.this, date, myCalendar.get(Calendar.YEAR), myCalendar.get(Calendar.MONTH), myCalendar.get(Calendar.DAY_OF_MONTH)).show();


            }
        });


        logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (NetworkUtil.isOnline(getApplicationContext())) {

                    dialog = new ProgressDialog(EmployeelistActivity.this, R.style.AppCompatAlertDialogStyle);
                    dialog.setMessage(getResources().getString(R.string.loading));
                    dialog.setCanceledOnTouchOutside(false);
                    dialog.show();

                    initlogoutOutAPIResources(userid,PrefUtils.getKey(EmployeelistActivity.this,AppConstants.Clientid));


                }
                else
                {
                    Intent i = new Intent(EmployeelistActivity.this, LoginActivity.class);
                    i.putExtra("logout", "logout");
                    i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
                    i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    startActivity(i);
                }

            }
        });
    }


        private void createFileOnDevice(Boolean append) throws IOException {
                /*
                 * Function to initially create the log file and it also writes the time of creation to file.
                 */
            File backupPath = Environment.getExternalStorageDirectory();

            backupPath = new File(backupPath.getPath() + "/Android/data/com.wfms.nectar.wfms/files");

            File Root = Environment.getExternalStorageDirectory();
            if(backupPath.canWrite()){
                File  LogFile = new File(backupPath, "Log.txt");
                FileWriter LogWriter = new FileWriter(LogFile, append);
                out = new BufferedWriter(LogWriter);
                Date date = new Date();

                out.write("Logged at" + String.valueOf(date.getHours() + ":" + date.getMinutes() + ":" + date.getSeconds() + "\n"));
                out.close();

            }

    }

    private void initlogoutOutAPIResources(String userid,String clientid) {
        SignUpPresenterImpl loginPresenter = new SignUpPresenterImpl(EmployeelistActivity.this);
        loginPresenter.callApi(AppConstants.Logout, userid,clientid,PrefUtils.getKey(EmployeelistActivity.this, AppConstants.Api_Token));
    }
    private void getEmployelist() {
        if (NetworkUtil.isOnline(getApplicationContext())) {
            p.setVisibility(View.VISIBLE);
            load_more.setTextColor(getResources().getColor(R.color.ntv_white));
            load_more.setBackgroundColor(getResources().getColor(R.color.colorPrimaryDark));
            if(isrefresh==true)
            {
                initGetEmployeeListAPIResources(userid, "0",PrefUtils.getKey(EmployeelistActivity.this,AppConstants.Clientid),PrefUtils.getKey(EmployeelistActivity.this,AppConstants.Api_Token));
            }
            else
            {
                initGetEmployeeListAPIResources(userid, ataandenceid,PrefUtils.getKey(EmployeelistActivity.this,AppConstants.Clientid),PrefUtils.getKey(EmployeelistActivity.this,AppConstants.Api_Token));
            }


        } else {
            p.setVisibility(View.GONE);
            if (userid != null) {
                load_more.setTextColor(getResources().getColor(R.color.ntv_white));
                load_more.setBackgroundColor(getResources().getColor(R.color.disable_color));
                senddataTimeInfromSqlite();

            } else {
                Toast.makeText(getApplicationContext(), getResources().getString(R.string.internet_connection), Toast.LENGTH_SHORT).show();
            }

        }
    }

    private void initGetEmployeeListAPIResources(String userid, String ataandenceid,String clientid,String apitoken) {
        GetAlEmployePresenterImpl getAllfuelPresenter = new GetAlEmployePresenterImpl(EmployeelistActivity.this);
        getAllfuelPresenter.callApi(AppConstants.Employee_List, userid, ataandenceid,clientid,apitoken);

    }
    private void initGetEmployeeFilterListAPIResources(String userid, String date,String attanedenceid,String clientid,String apitoken) {
        GetAlEmployePresenterImpl getAllfuelPresenter = new GetAlEmployePresenterImpl(EmployeelistActivity.this);
        getAllfuelPresenter.callApi(AppConstants.Filter_Employee_List, userid, date,attanedenceid,clientid,apitoken);

    }

    @Override
    public void onGetEmployeListSuccess(EmployeResponse supplierResponse) {
        try {
            if (employelist == null) {
                employelist = new ArrayList<>();
            }
            load_more.setBackgroundColor(getResources().getColor(R.color.colorPrimaryDark));
            employelist1.clear();
            int count=employelist.size();
            employelist.addAll(supplierResponse.getSupplierData());
            employelist1.addAll(supplierResponse.getSupplierData());
            adapter = new Employe_Adapter(EmployeelistActivity.this, employelist);
            employe_list.setAdapter(adapter);
            employe_list.setVisibility(View.VISIBLE);
            adapter.notifyDataSetChanged();
            employe_list.smoothScrollToPosition(count);
            if(isrefresh==true)
            {
                employe_list.smoothScrollToPosition(0);
            }
            else
            {
                employe_list.smoothScrollToPosition(count);
            }

            p.setVisibility(View.GONE);
            if (PrefUtils.getKey(EmployeelistActivity.this, AppConstants.Isupdated).equalsIgnoreCase("false")) {

                storelocaldatabase();

            } else {
                databaseHelper = new DatabaseHelper(EmployeelistActivity.this);

                for (int i = 0; i < employelist1.size(); i++) {
                    boolean isHead = databaseHelper.hasObject(employelist1.get(i).getAttendance_id());
                    if (isHead == true) {
                      storelocaldatabase1();

                    } else {
                        storelocaldatabase2();
                    }
                }

                if(employelist1.size()==0) {

                    employe_list.setVisibility(View.GONE);
                    Toast.makeText(getApplicationContext(), "No Record found", Toast.LENGTH_SHORT).show();
                }
            }

        } catch (Exception e) {
            Log.d("TAG", e.getMessage());
        }
    }

    @Override
    public void onGetEmployeListListFailure(String msg) {
        p.setVisibility(View.GONE);
        Toast.makeText(getApplicationContext(), getResources().getString(R.string.no_record), Toast.LENGTH_SHORT).show();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.drawer, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.logout:

                if (NetworkUtil.isOnline(getApplicationContext())) {

                    dialog = new ProgressDialog(EmployeelistActivity.this, R.style.AppCompatAlertDialogStyle);
                    dialog.setMessage(getResources().getString(R.string.loading));
                    dialog.setCanceledOnTouchOutside(false);
                    dialog.show();

                    initlogoutOutAPIResources(userid,PrefUtils.getKey(EmployeelistActivity.this,AppConstants.Clientid));

                }
                else
                {
                    Intent i = new Intent(EmployeelistActivity.this, LoginActivity.class);
                    i.putExtra("logout", "logout");
                    i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
                    i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    startActivity(i);
                }
                return true;
            case R.id.reset:
                Intent i1 = new Intent(EmployeelistActivity.this, ResetPinActivity.class);
                startActivity(i1);
                return true;
            case R.id.setting:
                Intent i2 = new Intent(EmployeelistActivity.this, SettingActivity.class);
                startActivity(i2);
                return true;
            case R.id.dashboard:
                Intent i3 = new Intent(EmployeelistActivity.this, DashboardActivity.class);
                startActivity(i3);
                return true;
            case R.id.leave:
                Intent i4 = new Intent(EmployeelistActivity.this, LeaveActivity.class);
                startActivity(i4);
                return true;
            case R.id.leave_summary:
                Intent i5 = new Intent(EmployeelistActivity.this, LeaveSummaryActivity.class);
                startActivity(i5);
                return true;
            case R.id.profile:
                Intent i6 = new Intent(EmployeelistActivity.this, ProfileActivity.class);
                startActivity(i6);
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    public void storelocaldatabase() {
        databaseHelper = new DatabaseHelper(EmployeelistActivity.this);
        EmployeData data = new EmployeData();
        try {
            for (int i = employelist.size() - 1; i >= 0; i--) {

                data.setUser_id(employelist.get(i).getUser_id());
                data.setUsername(employelist.get(i).getUsername());
                data.setAttendance_id(employelist.get(i).getAttendance_id());
                data.setIn_date(employelist.get(i).getIn_date());

                if(employelist.get(i).getOut_date().equalsIgnoreCase(null))
                {
                    data.setOut_date("null");
                }
                else
                {
                    data.setOut_date(employelist.get(i).getOut_date());
                }
                data.setIn_time(employelist.get(i).getIn_time());
                data.setOut_time(employelist.get(i).getOut_time());
                data.setIn_location(employelist.get(i).getIn_location());
                data.setOut_location(employelist.get(i).getOut_location());
                data.setTime_duration(employelist.get(i).getTime_duration());
                data.setIn_location_url(employelist.get(i).getIn_location_url());
                data.setOut_location_url(employelist.get(i).getOut_location_url());
                databaseHelper.UserRecords(data);
            }

            PrefUtils.storeKey(getApplicationContext(), AppConstants.Isupdated, "true");

        } catch (Exception e) {
            e.printStackTrace();
        }

    }
    public void storelocaldatabase2() {
        databaseHelper = new DatabaseHelper(EmployeelistActivity.this);
        EmployeData data = new EmployeData();
        try {
            for (int i = employelist1.size() - 1; i >= 0; i--) {

                data.setUser_id(employelist1.get(i).getUser_id());
                data.setUsername(employelist1.get(i).getUsername());
                data.setAttendance_id(employelist1.get(i).getAttendance_id());
                data.setIn_date(employelist1.get(i).getIn_date());

                if(employelist1.get(i).getOut_date().equalsIgnoreCase(null))
                {
                    data.setOut_date("null");
                }
                else
                {
                    data.setOut_date(employelist1.get(i).getOut_date());
                }
                data.setIn_time(employelist1.get(i).getIn_time());
                data.setOut_time(employelist1.get(i).getOut_time());
                data.setIn_location(employelist1.get(i).getIn_location());
                data.setOut_location(employelist1.get(i).getOut_location());
                data.setTime_duration(employelist1.get(i).getTime_duration());
                data.setIn_location_url(employelist1.get(i).getIn_location_url());
                data.setOut_location_url(employelist1.get(i).getOut_location_url());
                databaseHelper.UserRecords(data);
            }

            PrefUtils.storeKey(getApplicationContext(), AppConstants.Isupdated, "true");

        } catch (Exception e) {
            e.printStackTrace();
        }

    }
    public void storelocaldatabase1() {
        databaseHelper = new DatabaseHelper(EmployeelistActivity.this);
        EmployeData data = new EmployeData();
        try {
            for (int i = 0; i < employelist1.size(); i++) {


                data.setUser_id(employelist1.get(i).getUser_id());
                data.setUsername(employelist1.get(i).getUsername());
                data.setAttendance_id(employelist1.get(i).getAttendance_id());
                data.setIn_date(employelist1.get(i).getIn_date());
                data.setOut_date(employelist1.get(i).getOut_date());
                data.setIn_time(employelist1.get(i).getIn_time());
                data.setOut_time(employelist1.get(i).getOut_time());
                data.setIn_location(employelist1.get(i).getIn_location());
                data.setOut_location(employelist1.get(i).getOut_location());
                data.setTime_duration(employelist1.get(i).getTime_duration());
                data.setIn_location_url(employelist1.get(i).getIn_location_url());
                data.setOut_location_url(employelist1.get(i).getOut_location_url());
                databaseHelper.UserRecordsupdate(data);
            }
//            dialog.dismiss();
            p.setVisibility(View.GONE);
            PrefUtils.storeKey(getApplicationContext(), AppConstants.Isupdated, "true");

        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    private void senddataTimeInfromSqlite() {
        databaseHelper = new DatabaseHelper(EmployeelistActivity.this);
        List<EmployeData> employdata = new ArrayList<>();
        employdata = databaseHelper.getAllUserRecordsData();

        for (int i = 0; i < employdata.size(); i++) {
            String id = employdata.get(i).getUser_id();
            String username = employdata.get(i).getUsername();
            String a_id = employdata.get(i).getAttendance_id();
            String indate = employdata.get(i).getIn_date();
            String outdate = employdata.get(i).getOut_date();
            String intime = employdata.get(i).getIn_time();
            String inlocation = employdata.get(i).getIn_location();
            String outlocation = employdata.get(i).getOut_location();
            if (indate != null) {
                Log.d("indate", indate);
            }

        }

        if (employdata.size() > 0) {
            try {
                if (employelist == null) {
                    employelist = new ArrayList<>();
                    tempemployelist = new ArrayList<>();
                }
                employelist.clear();
                employelist.addAll(employdata);
                tempemployelist.addAll(employdata);
                adapter.notifyDataSetChanged();


            } catch (Exception e) {
                Log.d("TAG", e.getMessage());
            }
        }


    }

    @Override
    public void onBackPressed() {
        if(PrefUtils.getKey(EmployeelistActivity.this,AppConstants.UserID).equalsIgnoreCase("1"))
        {
            finish();
        }
        else
        {
            Intent i = new Intent(EmployeelistActivity.this, MainActivity.class);
            startActivity(i);
            finish();
        }

    }

    @Override
    public void onSignUpSuccess(SignUpResponse signUpResponse) {
        dialog.dismiss();
        Intent i = new Intent(EmployeelistActivity.this, LoginActivity.class);
        i.putExtra("logout", "logout");
        i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
        i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        PrefUtils.storeKey(getApplicationContext(), AppConstants.LogoutUserID, null);
        startActivity(i);
    }

    @Override
    public void onSignUpFailure(String msg) {
        Log.d("fail", msg);
        dialog.dismiss();
        Toast.makeText(EmployeelistActivity.this,msg,Toast.LENGTH_SHORT).show();

    }

    protected void writeLogCat()
    {
        try
        {
            Process process = Runtime.getRuntime().exec("logcat -d");
            BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(process.getInputStream()));
            StringBuilder log = new StringBuilder();
            String line;
            while((line = bufferedReader.readLine()) != null)
            {
                log.append(line);
                log.append("\n");
            }

            //Convert log to string
            final String logString = new String(log.toString());

            //Create txt file in SD Card
            File sdCard = Environment.getExternalStorageDirectory();
           // File dir = new File(sdCard.getAbsolutePath() +File.separator + "LogFile");
            File dir = new File(Environment.getExternalStorageDirectory() + File.separator + "Logfile/");
            if(!dir.exists())
            {
                dir.mkdirs();
            }

            File file = new File(dir, "logcat.txt");

            //To write logcat in text file
            FileOutputStream fout = new FileOutputStream(file);
            OutputStreamWriter osw = new OutputStreamWriter(fout);

            //Writing the string to file
            osw.write(logString);
            osw.flush();
            osw.close();
        }
        catch(FileNotFoundException e)
        {
            e.printStackTrace();
        }
        catch(IOException e)
        {
            e.printStackTrace();
        }
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
            latitude = mCurrentLocation.getLatitude();
            longitude = mCurrentLocation.getLongitude();

         //   getAddress();
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
                                    rae.startResolutionForResult(EmployeelistActivity.this, REQUEST_CHECK_SETTINGS);
                                } catch (IntentSender.SendIntentException sie) {
                                    Log.i(TAG, "PendingIntent unable to execute request.");
                                }
                                break;
                            case LocationSettingsStatusCodes.SETTINGS_CHANGE_UNAVAILABLE:
                                String errorMessage = "Location settings are inadequate, and cannot be " +
                                        "fixed here. Fix in Settings.";
                                Log.e(TAG, errorMessage);

                                Toast.makeText(EmployeelistActivity.this, errorMessage, Toast.LENGTH_LONG).show();
                        }

                        updateLocationUI();
                    }
                });
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
    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putBoolean("is_requesting_updates", mRequestingLocationUpdates);
    }
}