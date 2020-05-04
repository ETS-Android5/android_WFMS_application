package com.wfms.nectar.wfms;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.suke.widget.SwitchButton;
import com.wfms.nectar.jsonModelResponses.signup.SignUpResponse;
import com.wfms.nectar.presenter.presenterImpl.SignUpPresenterImpl;
import com.wfms.nectar.presenter.presenterImpl.SignUpPresenterImplOut;
import com.wfms.nectar.utils.AppConstants;
import com.wfms.nectar.utils.PrefUtils;
import com.wfms.nectar.viewstate.SignUpView;
import com.wfms.nectar.viewstate.SignUpViewOut;

/**
 * Created by Nectar on 17-06-2019.
 */

public class SettingActivity extends AppCompatActivity implements SignUpView, SignUpViewOut {
    SwitchButton notification_enable,geofence_enable;
    String isnotication,isgeofencenotication;
    TextView notification_text,geofence_text;
    RelativeLayout back;
    AlertDialog alertDialog;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.setting_layout);
        notification_enable=(SwitchButton)findViewById(R.id.notification_enable);
        geofence_enable=(SwitchButton)findViewById(R.id.geofence_enable);
        notification_text=(TextView)findViewById(R.id.notification_text);
        geofence_text=(TextView)findViewById(R.id.geofence_text);
        notification_enable.setShadowEffect(true);
        notification_enable.setEnableEffect(true);
        geofence_enable.setShadowEffect(true);
        geofence_enable.setEnableEffect(true);
        back = (RelativeLayout) findViewById(R.id.back_layout);



        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(PrefUtils.getKey(SettingActivity.this, AppConstants.UserID).equalsIgnoreCase("1"))
                {
                    finish();
                }
                else
                {
                    Intent i = new Intent(SettingActivity.this, MainActivity.class);
                    startActivity(i);
                    finish();
                }
            }
        });
        if(PrefUtils.getKey(SettingActivity.this, AppConstants.Isnotification)!=null)
        {
            if(PrefUtils.getKey(SettingActivity.this, AppConstants.Isnotification).equals("1"))
            {
                notification_text.setText("On");
                notification_enable.setChecked(true);
            }else
            {
                notification_text.setText("Off");
                notification_enable.setChecked(false);
            }
        }else
        {
            notification_text.setText("On");
            notification_enable.setChecked(true);
        }
        if(PrefUtils.getKey(SettingActivity.this, AppConstants.Isgeofencenotification)!=null)
        {
            if(PrefUtils.getKey(SettingActivity.this, AppConstants.Isgeofencenotification).equals("1"))
            {
                geofence_text.setText("On");
                geofence_enable.setChecked(true);


            }else
            {
                geofence_text.setText("Off");
                geofence_enable.setChecked(false);
            }
        }else
        {
            geofence_text.setText("On");
            geofence_enable.setChecked(true);
        }
        notification_enable.setOnCheckedChangeListener(new SwitchButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(SwitchButton view, boolean isChecked) {
                //TODO do your job
                 //openconfirmationdialog();

                if(notification_enable.isChecked())
                {
                    initnotificationenableAPIResources(PrefUtils.getKey(SettingActivity.this, AppConstants.UserID), PrefUtils.getKey(SettingActivity.this, AppConstants.Clientid), "1",PrefUtils.getKey(SettingActivity.this, AppConstants.Api_Token));
                    isnotication="1";
                }
                else
                {
                    initnotificationenableAPIResources(PrefUtils.getKey(SettingActivity.this, AppConstants.UserID), PrefUtils.getKey(SettingActivity.this, AppConstants.Clientid), "0",PrefUtils.getKey(SettingActivity.this, AppConstants.Api_Token));
                    isnotication="0";
                }
            }
        });

        geofence_enable.setOnCheckedChangeListener(new SwitchButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(SwitchButton view, boolean isChecked) {
                //TODO do your job
               // openconfirmationdialog();
                Log.d("isChecked",""+isChecked);
                if(geofence_enable.isChecked())
                {
                   // inigeofencetnotificationenableAPIResources("2", "on");
                    inigeofencetnotificationenableAPIResources(PrefUtils.getKey(SettingActivity.this, AppConstants.UserID), "on",PrefUtils.getKey(SettingActivity.this, AppConstants.Api_Token));
                    isgeofencenotication="1";
                }
                else
                {
                 //   inigeofencetnotificationenableAPIResources("2", "off");
                    inigeofencetnotificationenableAPIResources(PrefUtils.getKey(SettingActivity.this, AppConstants.UserID), "off",PrefUtils.getKey(SettingActivity.this, AppConstants.Api_Token));
                    isgeofencenotication="0";
                }
            }
        });
    }

   /* private void openconfirmationdialog() {
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(this);
        alertDialogBuilder.setMessage("Are you sure, You wanted to reset all geofence");
        alertDialogBuilder.setPositiveButton("yes",
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface arg0, int arg1) {

                        if(geofence_enable.isChecked())
                        {
                            Log.d("11111","1111111");
                            inigeofencetnotificationenableAPIResources(PrefUtils.getKey(SettingActivity.this, AppConstants.UserID), "on");
                            isgeofencenotication="on";
                        }
                        else
                        {
                            Log.d("22222","222222222");
                            inigeofencetnotificationenableAPIResources(PrefUtils.getKey(SettingActivity.this, AppConstants.UserID), "off");
                            isgeofencenotication="off";
                        }

                    }


                });

        alertDialogBuilder.setNegativeButton("No",new DialogInterface.OnClickListener() {

            public void onClick(DialogInterface dialog, int which) {
                alertDialog.dismiss();
            }
        });

        alertDialog = alertDialogBuilder.create();
        alertDialog.show();

                  *//* geofencelist.remove(position);
                      saveArray();*//*
    }
*/


    private void initnotificationenableAPIResources(String userid, String clientid, String isnotification,String apitoken) {
        SignUpPresenterImpl notificationPresenter = new SignUpPresenterImpl(SettingActivity.this);
        notificationPresenter.callApi(AppConstants.NotificationEnable, userid, clientid,isnotification,apitoken);
}

    private void inigeofencetnotificationenableAPIResources(String userid, String isnotification,String apitoken) {
        SignUpPresenterImplOut notificationPresenter = new SignUpPresenterImplOut(SettingActivity.this);
        notificationPresenter.callApi(AppConstants.GeofenceNotificationEnable, userid,isnotification,apitoken);
    }

    @Override
    public void onSignUpSuccess(SignUpResponse signUpResponse) {
        if(isnotication.equals("1"))
        {
            notification_text.setText("On");
            notification_enable.setChecked(true);
        }
        else
        {
            notification_text.setText("Off");
            notification_enable.setChecked(false);
        }

        PrefUtils.storeKey(SettingActivity.this, AppConstants.Isnotification,isnotication);
    }

    @Override
    public void onSignUpFailure(String msg) {

    }

    @Override
    public void onSignUpSuccessOut(SignUpResponse signUpResponse) {
        if(isgeofencenotication.equals("1"))
        {
            geofence_text.setText("on");
            geofence_enable.setChecked(true);


        }
        else
        {
            geofence_text.setText("Off");
            geofence_enable.setChecked(false);
        }
       // alertDialog.dismiss();
        Toast.makeText(getApplicationContext(), "All geofence are reset", Toast.LENGTH_LONG).show();
        PrefUtils.storeKey(SettingActivity.this, AppConstants.Isgeofencenotification,isgeofencenotication);
        Log.d("fencenotification1111",PrefUtils.getKey(SettingActivity.this, AppConstants.Isgeofencenotification));
    }

    @Override
    public void onSignUpFailureOut(String msg) {
        //alertDialog.dismiss();
    }
}

