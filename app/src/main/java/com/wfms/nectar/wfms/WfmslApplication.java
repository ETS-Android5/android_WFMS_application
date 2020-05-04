package com.wfms.nectar.wfms;

import android.app.Application;
import android.content.Context;
import android.content.Intent;
import android.os.Environment;
import android.util.Log;
import com.crashlytics.android.Crashlytics;
import com.wfms.nectar.retrofit.RetroAPIInterface;
import com.wfms.nectar.retrofit.RetrofitClient;
import com.wfms.nectar.utils.LocaleHelper;
import com.wfms.nectar.utils.LocationService;
import com.wfms.nectar.utils.PrefManager;

import io.fabric.sdk.android.Fabric;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;

/**
 * Created by Abhishek on 4/8/2018.
 */

public class WfmslApplication extends Application {

    private static final String TAG = WfmslApplication.class.getSimpleName();
    public static RetroAPIInterface mRetroClient;

    public static Context getmCurrentContext() {
        return mCurrentContext;
    }

    public static void setmCurrentContext(Context mCurrentContext) {
        WfmslApplication.mCurrentContext = mCurrentContext;
    }

    public static Context mCurrentContext = null;
    private static WfmslApplication mInstance;

    @Override
    public void onCreate() {
        super.onCreate();
       // Fabric.with(this, new Crashlytics());
     //   Fabric.with(this, new Crashlytics());
        //Crashlytics.getInstance().crash();
        mInstance = this;
        setmCurrentContext(getApplicationContext());
        initializeClients();

    }

    public static synchronized WfmslApplication getInstance() {
        return mInstance;
    }

    // Initialize sharedPref and retrofit client once.
    private void initializeClients(){
        try {

            PrefManager.getActiveInstance(this);
          //  Crashlytics.log(Log.VERBOSE, "MyApp", "Higgs-Boson detected! Bailing out...");
            geturl();
        }
        catch (Exception e){
            Log.e(TAG,e.getMessage().toString());
        }
    }
    @Override
    protected void attachBaseContext(Context base) {
        super.attachBaseContext(LocaleHelper.onAttach(base, "en"));
    }

    public  static void geturl(){
        mRetroClient = RetrofitClient.getClient().create(RetroAPIInterface.class);
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
}