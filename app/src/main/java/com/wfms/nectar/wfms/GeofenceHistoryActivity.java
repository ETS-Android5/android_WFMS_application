package com.wfms.nectar.wfms;

import android.app.ProgressDialog;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.wfms.nectar.Adapter.GeofenceHistoryAdapter;
import com.wfms.nectar.Adapter.Geofencelist_Adapter;
import com.wfms.nectar.Adapter.User_Adapter;
import com.wfms.nectar.jsonModelResponses.Fuel.EmployeData;
import com.wfms.nectar.jsonModelResponses.Fuel.EmployeResponse;
import com.wfms.nectar.jsonModelResponses.Fuel.LeaveData;
import com.wfms.nectar.jsonModelResponses.Fuel.LeaveResponse;
import com.wfms.nectar.jsonModelResponses.signup.SignUpResponse;
import com.wfms.nectar.presenter.presenterImpl.GetAlEmployePresenterImpl;
import com.wfms.nectar.presenter.presenterImpl.GetAlLeavePresenterImpl;
import com.wfms.nectar.presenter.presenterImpl.SignUpPresenterImpl;
import com.wfms.nectar.utils.AppConstants;
import com.wfms.nectar.utils.DividerItemDecoration;
import com.wfms.nectar.utils.NetworkUtil;
import com.wfms.nectar.utils.PrefUtils;
import com.wfms.nectar.viewstate.EmployeView;
import com.wfms.nectar.viewstate.LeaveView;
import com.wfms.nectar.viewstate.SignUpView;

import java.util.ArrayList;

public class GeofenceHistoryActivity extends AppCompatActivity implements EmployeView, LeaveView {
    RecyclerView list;
    SwipeRefreshLayout pullToRefresh;
    Toolbar toolbar;
    ProgressBar p;
    ArrayList<EmployeData> employelist = new ArrayList();
    ArrayList<LeaveData> geofencelist = new ArrayList();
    ProgressDialog dialog;
    Spinner userlist;
    String userid,username;
    RelativeLayout back;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.geofence_history_layout);
        list=(RecyclerView)findViewById(R.id.geofencehistory_list);
        pullToRefresh=(SwipeRefreshLayout)findViewById(R.id.pullToRefresh);
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        p=(ProgressBar)findViewById(R.id.geofence_progressbar) ;
        userlist=(Spinner)findViewById(R.id.user_list) ;
        back=(RelativeLayout)findViewById(R.id.back_layout) ;
        setSupportActionBar(toolbar);
        toolbar.setTitleTextColor(Color.WHITE);
        getSupportActionBar().setTitle("History");

        list.setHasFixedSize(true);
        list.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
        list.setItemAnimator(new DefaultItemAnimator());

        getEmployelist();
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
        userlist.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                username = employelist.get(position).getUsername1();
                userid= employelist.get(position).getUser_id();
                if (!username.equalsIgnoreCase("Select User")) {
                    getgeofencehistory();
                    Log.d("usernameusername",username);
                }
                else
                {
                    Toast.makeText(GeofenceHistoryActivity.this,"Please select user",Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

    }

    private void getEmployelist() {
        if (NetworkUtil.isOnline(getApplicationContext())) {
            dialog = new ProgressDialog(GeofenceHistoryActivity.this, R.style.AppCompatAlertDialogStyle);
            dialog.setMessage(getResources().getString(R.string.loading));
            dialog.setCanceledOnTouchOutside(false);
            dialog.show();
            initGetEmployeeListAPIResources(PrefUtils.getKey(GeofenceHistoryActivity.this, AppConstants.Clientid),PrefUtils.getKey(GeofenceHistoryActivity.this, AppConstants.Api_Token));

        } else {
            Toast.makeText(getApplicationContext(), getResources().getString(R.string.internet_connection), Toast.LENGTH_SHORT).show();
        }
    }

    private void getgeofencehistory() {
        if (NetworkUtil.isOnline(getApplicationContext())) {
            p.setVisibility(View.VISIBLE);
               initGetgeofencehistoryListAPIResources(userid,PrefUtils.getKey(GeofenceHistoryActivity.this,AppConstants.Clientid),PrefUtils.getKey(GeofenceHistoryActivity.this,AppConstants.Api_Token));
        } else {
            p.setVisibility(View.GONE);
                Toast.makeText(getApplicationContext(), getResources().getString(R.string.internet_connection), Toast.LENGTH_SHORT).show();
        }
    }

    private void initGetgeofencehistoryListAPIResources(String userid, String clientid, String apitoken) {
        GetAlLeavePresenterImpl attandencePresenter = new GetAlLeavePresenterImpl(GeofenceHistoryActivity.this);
       attandencePresenter.callApi(AppConstants.GeofenceHistory, PrefUtils.getKey(GeofenceHistoryActivity.this,AppConstants.UserID),userid, clientid, apitoken);
    }


    private void initGetEmployeeListAPIResources(String clientid,String apitoken) {
        GetAlEmployePresenterImpl getAlluserPresenter = new GetAlEmployePresenterImpl(GeofenceHistoryActivity.this);
        getAlluserPresenter.callApi(AppConstants.User_List, clientid,apitoken);
    }

    @Override
    public void onGetEmployeListSuccess(EmployeResponse employeResponse) {
        p.setVisibility(View.GONE);
        try {
            if (employelist == null) {
                employelist = new ArrayList<>();
            }
            dialog.dismiss();
            int count = employelist.size();
            EmployeData userData = new EmployeData();
            userData.setUsername1("Select User");
            userData.setUser_id("0");
            employelist.add(userData);
            employelist.addAll(employeResponse.getEmployerData());
            User_Adapter useradapter = new User_Adapter(getApplicationContext(), employelist);
            userlist.setAdapter(useradapter);


        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    @Override
    public void onGetEmployeListListFailure(String msg) {

    }


    @Override
    public void onGeLeaveListSuccess(LeaveResponse leaveResponse) {
        p.setVisibility(View.GONE);
        try {
            if (geofencelist == null) {

                geofencelist = new ArrayList<>();

            }
            geofencelist.clear();

            if (leaveResponse.getGeodataData() != null) {
                geofencelist.addAll(leaveResponse.getGeodataData());
                Log.d("geofencelist", "" + geofencelist.size());
                if (geofencelist.size() > 0) {
                    GeofenceHistoryAdapter adaper = new GeofenceHistoryAdapter(getApplicationContext(), geofencelist);
                    list.setAdapter(adaper);
                } else {
                    Toast.makeText(GeofenceHistoryActivity.this, "Geofence not found", Toast.LENGTH_SHORT).show();
                }

            } else {
                Toast.makeText(GeofenceHistoryActivity.this, "Geofence not found", Toast.LENGTH_SHORT).show();
            }

        }
        catch (Exception e) {
            Log.d("Exception", e.getMessage());
        }
    }

    @Override
    public void onGetLeaveListListFailure(String msg) {

    }
}

