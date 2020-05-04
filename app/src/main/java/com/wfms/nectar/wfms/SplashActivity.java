package com.wfms.nectar.wfms;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.provider.Settings;
import android.text.Html;
import android.text.method.LinkMovementMethod;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.messaging.FirebaseMessaging;
import com.wfms.nectar.utils.AppConstants;
import com.wfms.nectar.utils.Config;
import com.wfms.nectar.utils.GpsUtils;
import com.wfms.nectar.utils.PrefUtils;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by Nectar on 23-07-2018.
 */

public class SplashActivity extends AppCompatActivity {
    // Splash screen timer
    private static int SPLASH_TIME_OUT = 3000;
    SharedPreferences pref;
    String lang;
    @BindView(R.id.websitelink)
    TextView websitelink;
   // public static String validurl=null;
    private BroadcastReceiver mRegistrationBroadcastReceiver;
    public String a="";
    public static String validurl="http://wfms.timesheet.nectarinfotel.com/";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
       // Fabric.with(this, new Crashlytics());
        setContentView(R.layout.splash_layout);
        ButterKnife.bind(this);
        turnGPSOn();
        new GpsUtils(this).turnGPSOn(new GpsUtils.onGpsListener() {
            @Override
            public void gpsStatus(boolean isGPSEnable) {
                // turn on GPS
                Log.d("isGPSEnable",""+isGPSEnable);
            }
        });
        pref = getApplicationContext().getSharedPreferences("MyPref", MODE_PRIVATE);

        SharedPreferences.Editor editor = pref.edit();
        websitelink.setText(Html.fromHtml("Powered by :" + "<a href='http://nectarinfotel.com/'> <font color='#FFFFFF'>Nectar Infotel Solution Pvt.Ltd.</font></a> "));
        websitelink.setMovementMethod(LinkMovementMethod.getInstance());// make it active
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


        validurl= PrefUtils.getKey(SplashActivity.this,AppConstants.Clientid);
        displayFirebaseRegId();
        new Handler().postDelayed(new Runnable() {

            /*
             * Showing splash screen with a timer. This will be useful when you
             * want to show case your app logo / company
             */

            @Override
            public void run() {
                // This method will be executed once the timer is over
                // Start your app main activity
                if(validurl!=null)
                {
                    if(PrefUtils.getKey(SplashActivity.this,AppConstants.LogoutUserID)!=null)
                    {
                        if(PrefUtils.getKey(SplashActivity.this,AppConstants.LogoutUserID).equals("1"))
                        {
                            Intent i = new Intent(SplashActivity.this, LoginActivity.class);
                            startActivity(i);
                        }
                        else
                        {
                            Intent i = new Intent(SplashActivity.this, LoginActivity.class);
                            startActivity(i);
                        }

                    }
                    else {
                        Intent i = new Intent(SplashActivity.this, LoginActivity.class);
                        startActivity(i);
                    }
                }
                else
                {
                    Intent i = new Intent(SplashActivity.this, ConfigActivity.class);
                    startActivity(i);
                }

                finish();
            }
        }, SPLASH_TIME_OUT);
    }
    private void displayFirebaseRegId() {
        SharedPreferences pref = getApplicationContext().getSharedPreferences(Config.SHARED_PREF, 0);
        String regId = pref.getString("regId", null);
        Log.e( "Firebase reg id: " ,""+ regId);
        PrefUtils.storeKey(SplashActivity.this,AppConstants.TokenID,regId);

    }
    public void forceCrash(View view) {
        throw new RuntimeException("This is a crash");
    }
    private void turnGPSOn()
    {
        String provider = Settings.Secure.getString(getContentResolver(), Settings.Secure.LOCATION_PROVIDERS_ALLOWED);
        Log.e( "provider " ,""+ provider);
        if(!provider.contains("gps"))
        { //if gps is disabled
            final Intent poke = new Intent();
            poke.setClassName("com.android.settings", "com.android.settings.widget.SettingsAppWidgetProvider");
            poke.addCategory(Intent.CATEGORY_ALTERNATIVE);
            poke.setData(Uri.parse("3"));
            sendBroadcast(poke);
        }
    }
}
