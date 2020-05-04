package com.wfms.nectar.wfms;

import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.wfms.nectar.jsonModelResponses.signup.SignUpResponse;
import com.wfms.nectar.presenter.presenterImpl.SignUpPresenterImpl;
import com.wfms.nectar.utils.AppConstants;
import com.wfms.nectar.utils.NetworkUtil;
import com.wfms.nectar.utils.PrefUtils;
import com.wfms.nectar.viewstate.SignUpView;

public class ProfileActivity extends AppCompatActivity implements SignUpView {
    Toolbar toolbar;
    TextView resetpin,geofence,showmap;
    RelativeLayout profile_back_layout;
    ProgressDialog dialog;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.profile_layout);

      /*  toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        toolbar.setTitleTextColor(Color.WHITE);
        getSupportActionBar().setTitle("Profile");*/
        resetpin=(TextView)findViewById(R.id.reset_pin);
        geofence=(TextView)findViewById(R.id.setgeofence);
        showmap=(TextView)findViewById(R.id.showmap);
        profile_back_layout=(RelativeLayout) findViewById(R.id.profile_back_layout);

        if(PrefUtils.getKey(ProfileActivity.this, AppConstants.UserID).equalsIgnoreCase("1"))
        {
            geofence.setText("Set Geofence");
        }
        else
        {
            geofence.setText("Geofence");
        }
        resetpin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i6 = new Intent(ProfileActivity.this, ResetPinActivity.class);
                startActivity(i6);
            }
        });

        geofence.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i6 = new Intent(ProfileActivity.this, CreateGeofenceActivity.class);
                startActivity(i6);
            }
        });

        showmap.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i7 = new Intent(ProfileActivity.this, MapsActivity.class);
                startActivity(i7);
            }
        });

        profile_back_layout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
               finish();
            }
        });
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

                    dialog = new ProgressDialog(ProfileActivity.this, R.style.AppCompatAlertDialogStyle);
                    dialog.setMessage(getResources().getString(R.string.loading));
                    dialog.setCanceledOnTouchOutside(false);
                    dialog.show();

                    initlogoutOutAPIResources(PrefUtils.getKey(ProfileActivity.this, AppConstants.UserID),PrefUtils.getKey(ProfileActivity.this,AppConstants.Clientid));

                }
                else
                {
                    Intent i = new Intent(ProfileActivity.this, LoginActivity.class);
                    i.putExtra("logout", "logout");
                    i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
                    i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    startActivity(i);
                }
                return true;
            case R.id.reset:
                Intent i1 = new Intent(ProfileActivity.this, ResetPinActivity.class);
                startActivity(i1);
                return true;
            case R.id.setting:
                Intent i2 = new Intent(ProfileActivity.this, SettingActivity.class);
                startActivity(i2);
                return true;
            case R.id.dashboard:
                Intent i3 = new Intent(ProfileActivity.this, DashboardActivity.class);
                startActivity(i3);
                return true;
            case R.id.leave:
                Intent i4 = new Intent(ProfileActivity.this, LeaveActivity.class);
                startActivity(i4);
                return true;
            case R.id.leave_summary:
                Intent i5 = new Intent(ProfileActivity.this, LeaveSummaryActivity.class);
                startActivity(i5);
                return true;
            case R.id.profile:
                Intent i6 = new Intent(ProfileActivity.this, ProfileActivity.class);
                startActivity(i6);
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    private void initlogoutOutAPIResources(String userid,String clientid) {
        SignUpPresenterImpl loginPresenter = new SignUpPresenterImpl(ProfileActivity.this);
        loginPresenter.callApi(AppConstants.Logout, userid,clientid,PrefUtils.getKey(ProfileActivity.this, AppConstants.Api_Token));

    }

    @Override
    public void onSignUpSuccess(SignUpResponse signUpResponse) {
        dialog.dismiss();
        Intent i = new Intent(ProfileActivity.this, LoginActivity.class);
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
        Toast.makeText(ProfileActivity.this,msg,Toast.LENGTH_SHORT).show();
    }
}
