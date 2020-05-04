package com.wfms.nectar.wfms;

import android.app.ProgressDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Toast;

import com.google.firebase.messaging.FirebaseMessaging;
import com.wfms.nectar.jsonModelResponses.signup.SignUpResponse;
import com.wfms.nectar.presenter.presenterImpl.SignUpPresenterImpl;
import com.wfms.nectar.retrofit.RetroAPIInterface;
import com.wfms.nectar.utils.AppConstants;
import com.wfms.nectar.utils.Config;
import com.wfms.nectar.utils.NetworkUtil;
import com.wfms.nectar.utils.PrefUtils;
import com.wfms.nectar.viewstate.SignUpView;

/**
 * Created by Nectar on 22-04-2019.
 */

public class ConfigActivity extends AppCompatActivity implements SignUpView {
    EditText url;
    Button submit;
    public static RetroAPIInterface mRetroClient;
    public static  String clientname="";
    public  boolean isserver=false;
    CheckBox saveurl;
    String validurl="http://wfms.timesheet.nectarinfotel.com/";
    private BroadcastReceiver mRegistrationBroadcastReceiver;
    ProgressDialog dialog;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.config_layout);
        url = (EditText) findViewById(R.id.enter_url);
        submit = (Button) findViewById(R.id.submit_button);
        saveurl = (CheckBox) findViewById(R.id.checkbox);
        PrefUtils.storeKey(getApplicationContext(), AppConstants.Isupdated, "false");
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

        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {


                clientname = url.getText().toString();
                clientname = clientname.replace(" ", "");
                clientname=clientname.trim();

                if (clientname.length() > 0) {
                    checkclientname();

                } else {
                    Toast.makeText(getApplicationContext(), "Please enter organization name", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    private void checkclientname() {
        if (NetworkUtil.isOnline(getApplicationContext())) {
            dialog = new ProgressDialog(ConfigActivity.this, R.style.AppCompatAlertDialogStyle);
            dialog.setMessage(getResources().getString(R.string.loading));
            dialog.show();
            initClientAPIResources(clientname);
        }
        else
        {
            Toast.makeText(getApplicationContext(), getResources().getString(R.string.internet_connection), Toast.LENGTH_SHORT).show();
        }
    }

    private void initClientAPIResources(String clientname) {
        SignUpPresenterImpl loginPresenter = new SignUpPresenterImpl(ConfigActivity.this);
        loginPresenter.callApi(AppConstants.Client, clientname);

    }

    private void displayFirebaseRegId() {
        SharedPreferences pref = getApplicationContext().getSharedPreferences(Config.SHARED_PREF, 0);
        String regId = pref.getString("regId", null);
        if (regId != null)
            Log.e("Firebase reg id: ", "" + regId);
          PrefUtils.storeKey(ConfigActivity.this,AppConstants.TokenID,regId);
    }

    @Override
    public void onSignUpSuccess(SignUpResponse signUpResponse) {
        dialog.dismiss();
        if(signUpResponse.getMsg()!=null)
        {
        if(signUpResponse.getMsg().equals("expired"))
        {
            Toast.makeText(ConfigActivity.this,"Your date is expired",Toast.LENGTH_SHORT).show();
        }}
        else {
            PrefUtils.storeKey(ConfigActivity.this, AppConstants.Clientid, signUpResponse.getClient_id());
            PrefUtils.storeKey(ConfigActivity.this, AppConstants.ClientName, clientname);
            PrefUtils.storeKey(ConfigActivity.this, AppConstants.ClientInTime, signUpResponse.getClient_intime());
            Intent i = new Intent(ConfigActivity.this, LoginActivity.class);
            startActivity(i);
            finish();
        }
    }

    @Override
    public void onSignUpFailure(String msg) {
        dialog.dismiss();
        Toast.makeText(ConfigActivity.this,"pLease try again",Toast.LENGTH_SHORT).show();
    }
}
