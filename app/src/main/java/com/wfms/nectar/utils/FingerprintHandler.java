package com.wfms.nectar.utils;

/**
 * Created by Nectar on 18-04-2019.
 */

import android.Manifest;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.hardware.fingerprint.FingerprintManager;
import android.os.Build;
import android.os.CancellationSignal;
import android.os.Handler;
import androidx.annotation.RequiresApi;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.util.Log;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.wfms.nectar.jsonModelResponses.Fuel.EmployeData;
import com.wfms.nectar.jsonModelResponses.signup.SignUpResponse;
import com.wfms.nectar.presenter.presenterImpl.SignUpPresenterImplnotification;
import com.wfms.nectar.presenter.presenterImpl.SignUpadminPresenterImpl;
import com.wfms.nectar.sql.DatabaseHelper;
import com.wfms.nectar.viewstate.SignUpView;
import com.wfms.nectar.viewstate.SignUpViewOut;
import com.wfms.nectar.viewstate.SignUpViewadminnotification;
import com.wfms.nectar.viewstate.SignUpViewnotification;
import com.wfms.nectar.wfms.R;


/**
 * Created by whit3hawks on 11/16/16.
 */
@RequiresApi(api = Build.VERSION_CODES.M)
public class FingerprintHandler extends FingerprintManager.AuthenticationCallback implements SignUpView,SignUpViewOut,SignUpViewnotification,SignUpViewadminnotification {

    ProgressDialog ProgressDialog;
    private Context context;
    TextView textView,timein_text;
    public static  boolean isyou=false;
    Dialog dialog;
    Handler handler=new Handler();
    String userid,address,currenttime,locationURL,deviceiD,currentdate;
    RelativeLayout mTimeInButton;
    public boolean istimein=false;
    ImageView timein_img;
    // Constructor
    public FingerprintHandler(Context mContext, TextView textView, Dialog dialog, String userid, String address, String currenttime, String locationURL, String deviceiD, RelativeLayout mTimeInButton, String currentdate, TextView timein_text, ImageView timein_img) {
        context = mContext;
        this.textView=textView;
        this.dialog=dialog;
        this.address=address;
        this.currenttime=currenttime;
        this.locationURL=locationURL;
        this.deviceiD=deviceiD;
        this.userid=userid;
        this.currentdate=currentdate;
        this.mTimeInButton=mTimeInButton;
        this.timein_text=timein_text;
        this.timein_img=timein_img;

    }

    public void startAuth(FingerprintManager manager, FingerprintManager.CryptoObject cryptoObject) {
        CancellationSignal cancellationSignal = new CancellationSignal();
        if (ActivityCompat.checkSelfPermission(context, Manifest.permission.USE_FINGERPRINT) != PackageManager.PERMISSION_GRANTED) {
            return;
        }
        manager.authenticate(cryptoObject, cancellationSignal, 0, this, null);
    }


    @Override
    public void onAuthenticationError(int errMsgId, CharSequence errString) {
        this.update("Fingerprint Authentication error\n" + errString, false);
    }


    @Override
    public void onAuthenticationHelp(int helpMsgId, CharSequence helpString) {
        this.update("Fingerprint Authentication help\n" + helpString, false);
    }


    @Override
    public void onAuthenticationFailed() {
        this.update("Fingerprint Authentication failed.", false);
    }


    @Override
    public void onAuthenticationSucceeded(FingerprintManager.AuthenticationResult result) {
        this.update("Fingerprint Authentication succeeded.", true);
        entrytime();
    }


    public void update(String e, Boolean success){
     //   TextView textView = (TextView) ((Activity)context).findViewById(R.id.errorText);
       // dialog.dismiss();
        textView.setText(e);
        isyou=success;
        if(success){
            textView.setTextColor(ContextCompat.getColor(context,R.color.colorPrimaryDark));

        }
    }

    private void entrytime() {

           dialog.dismiss();

            DatabaseHelper    databaseHelper = new DatabaseHelper(context);
            EmployeData data=new EmployeData();

            data.setUser_id(PrefUtils.getKey(context, AppConstants.UserID));
            data.setIn_location(address);
            data.setIn_time(currenttime);
            data.setIn_location_url(locationURL);
            data.setIn_mac_address(deviceiD);
            data.setIsupload("false");
            data.setIslogin(PrefUtils.getKey(context, AppConstants.IsLogin));
            databaseHelper.TimeIn(data);



           // Toast.makeText(context,"Your internet is off so your data save in local database",Toast.LENGTH_LONG).show();
            // Snack Bar to show success message that record is wrong
            // Snackbar.make(mainLayout, "Please Check Internet Connection", Snackbar.LENGTH_LONG).show();

            Log.d("timeout",PrefUtils.getKey(context, AppConstants.IsLogin));
            if(PrefUtils.getKey(context, AppConstants.IsLogin).equals("0"))
            {
                Log.d("timein","timein");
                PrefUtils.storeKey(context, "date",currentdate);
                PrefUtils.storeKey(context, "time",currenttime);
                PrefUtils.storeKey(context, "address",address);
                PrefUtils.storeKey(context, "locationurl",locationURL);
                istimein=true;
                timein_text.setText("Time Out");
                timein_img.setBackgroundResource(R.drawable.time_out);
                PrefUtils.storeKey(context, AppConstants.IsLogin, "1");
                storetimeinvalue();
                SharedPreferences pref = context.getSharedPreferences(Config.SHARED_PREF, 0);
                String regId = pref.getString("regId", null);

                initnotificationAPIResources(userid,regId,currenttime,PrefUtils.getKey(context,AppConstants.Clientid),PrefUtils.getKey(context, AppConstants.Api_Token));
                initadminnotificationAPIResources(userid,PrefUtils.getKey(context,AppConstants.Name),PrefUtils.getKey(context,AppConstants.Clientid),PrefUtils.getKey(context, AppConstants.Api_Token));
            }
            else   if(PrefUtils.getKey(context, AppConstants.IsLogin).equals("1"))
            {
                Log.d("timeout","timeout");
                istimein=false;
                timein_text.setText("Time In");
                timein_img.setBackgroundResource(R.drawable.time_in);
                PrefUtils.storeKey(context, AppConstants.IsLogin,"0");
                storetimeOutvalue();

            }

        mTimeInButton.setClickable(false);
        mTimeInButton.setEnabled(false);
        //}
    }

    private void storetimeinvalue() {
            DatabaseHelper  databaseHelper = new DatabaseHelper(context);
            EmployeData data=new EmployeData();
                data.setUser_id(PrefUtils.getKey(context, AppConstants.UserID));
                data.setUsername(PrefUtils.getKey(context, AppConstants.Name));
                data.setAttendance_id("50000");
                data.setIn_date(currentdate);
                data.setIn_time(currenttime);
                data.setIn_location(address);
               data.setIn_mac_address(deviceiD);
                data.setIn_location_url(locationURL);
                data.setOut_date("null");
                data.setOut_time(null);
                data.setOut_location(null);
                data.setOut_location_url(null);
                databaseHelper.UserRecords(data);


    }


    private void storetimeOutvalue() {
        DatabaseHelper  databaseHelper = new DatabaseHelper(context);
        EmployeData data=new EmployeData();

        data.setUser_id(PrefUtils.getKey(context, AppConstants.UserID));
        data.setUsername(PrefUtils.getKey(context, AppConstants.Name));
        data.setAttendance_id("50000");
        data.setOut_date(currentdate);
        data.setOut_time(currenttime);
        data.setOut_location(address);
        data.setOut_location_url(locationURL);
        databaseHelper.UserupdateRecords(data);

    }


    private void initadminnotificationAPIResources(String userid, String username,String clientid,String apitoken) {
        SignUpadminPresenterImpl loginPresenter = new SignUpadminPresenterImpl(this);
        loginPresenter.callApi(AppConstants.AdminNotification,userid, username,clientid,apitoken);
        Log.d("in", "in");
    }
    @Override
    public void onSignUpSuccess(SignUpResponse signUpResponse) {
     //   ProgressDialog.dismiss();
        timein_text.setText("Time Out");
        timein_img.setBackgroundResource(R.drawable.time_out);
        dialog.dismiss();
        PrefUtils.storeKey(context, AppConstants.IsLogin,"1");
        SharedPreferences pref = context.getSharedPreferences(Config.SHARED_PREF, 0);
        String regId = pref.getString("regId", null);


   }

    @Override
    public void onSignUpFailure(String msg) {
        Log.d("fail",msg);
      //  ProgressDialog.dismiss();
        dialog.dismiss();
        Toast.makeText(context,"Something is wrong,Please try again",Toast.LENGTH_SHORT).show();
    }

    private void initnotificationAPIResources(String userid,String regId, String currenttime,String clientid,String apitoken) {
        SignUpPresenterImplnotification loginPresenter = new SignUpPresenterImplnotification(this);
        loginPresenter.callApi(AppConstants.Notification,userid,regId, currenttime,clientid,apitoken);
        Log.d("in", "in");
    }
    @Override
    public void onSignUpSuccessOut(SignUpResponse signUpResponse) {
      //  ProgressDialog.dismiss();
        dialog.dismiss();
        timein_text.setText("Time In");
        timein_img.setBackgroundResource(R.drawable.time_in);
        PrefUtils.storeKey(context, AppConstants.IsLogin,"0");
    }

    @Override
    public void onSignUpFailureOut(String msg) {
        Log.d("msg",msg);
       // ProgressDialog.dismiss();
        dialog.dismiss();
        Toast.makeText(context,"Something is wrong,Please try again",Toast.LENGTH_SHORT).show();

    }

    @Override
    public void onSignUpSuccessnotification(SignUpResponse signUpResponse) {
        Log.d("success","success");


    }

    @Override
    public void onSignUpFailurenotification(String msg) {

        Log.d("success1","success1");

    }

    @Override
    public void onSignUpSuccessadminnotification(SignUpResponse signUpResponse) {

    }

    @Override
    public void onSignUpFailureadminnotification(String msg) {

    }
}
