package com.wfms.nectar.utils;

import android.content.Context;
import android.content.SharedPreferences;

/**
 * Created by Abhishek on 8/26/2016.

 */
public class PrefManager {
    private static PrefManager mPref=null;
    SharedPreferences pref;
    SharedPreferences.Editor editor;
    Context _context;

    // shared pref mode
    int PRIVATE_MODE = 0;

    // Shared preferences file name
    private static final String PREF_NAME = "androidhive-welcome";

    private static final String IS_FIRST_TIME_LAUNCH = "IsFirstTimeLaunch";
    private static final String userID = "userID";
    private static final String emailid = "emailid";
    private static final String password = "password";


    public PrefManager(Context context) {
        this._context = context;
        pref = _context.getSharedPreferences(PREF_NAME, PRIVATE_MODE);
        editor = pref.edit();
    }

      /*  public void setFirstTimeLaunch(boolean isFirstTime) {
            editor.putBoolean(IS_FIRST_TIME_LAUNCH, isFirstTime);
            editor.commit();
        }*/

    public static PrefManager getActiveInstance(Context context) {
        if (mPref == null) {
            mPref = new PrefManager(context);
        }
        return mPref;
    }

    public boolean isFirstTimeLaunch() {
        return pref.getBoolean(IS_FIRST_TIME_LAUNCH, true);
    }

    public int getUserId() {
        return pref.getInt(userID, -1);
    }

    public void setUserId(int id) {
        editor.putInt(userID, id);
        editor.commit();
    }

    public String getEmailid() {
        return pref.getString(emailid, "");
    }

    public String getPassword() {
        return pref.getString(password, "");
    }


   /* public void setUseremail(String email) {
        editor.putInt(emailid, email);
        editor.commit();
    }*/
}

