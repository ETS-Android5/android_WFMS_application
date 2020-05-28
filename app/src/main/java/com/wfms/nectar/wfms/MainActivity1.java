/*
 * Copyright (C) 2016 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

// Modified by Gaurav on Feb 23, 2018

package com.wfms.nectar.wfms;

import android.Manifest;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.content.res.Configuration;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.Point;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.HandlerThread;
import android.util.Log;
import android.view.Display;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.StringRes;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.DialogFragment;


import com.google.android.cameraview.CameraView;
import com.tzutalin.dlib.Constants;
import com.tzutalin.dlib.FaceRec;
import com.tzutalin.dlib.VisionDetRet;
import com.wfms.nectar.jsonModelResponses.Fuel.EmployeData;
import com.wfms.nectar.jsonModelResponses.signup.SignUpResponse;
import com.wfms.nectar.presenter.presenterImpl.SignUpPresenterImplnotification;
import com.wfms.nectar.presenter.presenterImpl.SignUpadminPresenterImpl;
import com.wfms.nectar.sql.DatabaseHelper;
import com.wfms.nectar.utils.AppConstants;
import com.wfms.nectar.utils.Config;
import com.wfms.nectar.utils.PrefUtils;
import com.wfms.nectar.viewstate.SignUpViewUpdate;
import com.wfms.nectar.viewstate.SignUpViewadminnotification;
import com.wfms.nectar.viewstate.SignUpViewnotification;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Pattern;

// This demo app uses dlib face recognition based on resnet
public class MainActivity1 extends AppCompatActivity implements
        ActivityCompat.OnRequestPermissionsResultCallback, SignUpViewadminnotification, SignUpViewUpdate, SignUpViewnotification {
    private Context myContext;
    private static final String TAG = "MainActivity";
    private static final int INPUT_SIZE = 500;
    String userid;
    private static final int[] FLASH_OPTIONS = {
            CameraView.FLASH_AUTO,
            CameraView.FLASH_OFF,
            CameraView.FLASH_ON,
    };

    private static final int[] FLASH_ICONS = {
            R.drawable.ic_flash_auto,
            R.drawable.ic_flash_off,
            R.drawable.ic_flash_on,
    };

    private static final int[] FLASH_TITLES = {
            R.string.flash_auto,
            R.string.flash_off,
            R.string.flash_on,
    };

    private int mCurrentFlash;





    private CameraView mCameraView;

    private Handler mBackgroundHandler;

    private View.OnClickListener mOnClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            switch (v.getId()) {
                case R.id.take_picture:
                    if (mCameraView != null) {
                        mCameraView.takePicture();
                    }
                    break;
                case R.id.add_person:
                    Intent i = new Intent(MainActivity1.this, AddPerson.class);
                    startActivity(i);
                    finish();
                    break;
            }
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        Log.d(TAG, "onCreate called");
        super.onCreate(savedInstanceState);
        setContentView(R.layout.face);
        checkPermissions();
        userid = PrefUtils.getKey(MainActivity1.this, AppConstants.UserID);
        mCameraView = (CameraView) findViewById(R.id.camera);

        if (mCameraView != null) {
            mCameraView.addCallback(mCallback);
        }
        Button fab = (Button) findViewById(R.id.take_picture);
        if (fab != null) {
            fab.setOnClickListener(mOnClickListener);
        }
        Button add_person = (Button) findViewById(R.id.add_person);
        if (add_person != null) {
            add_person.setOnClickListener(mOnClickListener);
        }
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayShowTitleEnabled(false);
        }

    }

    private FaceRec mFaceRec;

    private void changeProgressDialogMessage(final ProgressDialog pd, final String msg) {
        Runnable changeMessage = new Runnable() {
            @Override
            public void run() {
                pd.setMessage(msg);
            }
        };
        runOnUiThread(changeMessage);
    }

    @Override
    public void onSignUpUpdateSuccess(SignUpResponse signUpResponse) {

    }

    @Override
    public void onSignUpUpdateFailure(String msg) {

    }

    @Override
    public void onSignUpSuccessadminnotification(SignUpResponse signUpResponse) {

    }

    @Override
    public void onSignUpFailureadminnotification(String msg) {

    }

    @Override
    public void onSignUpSuccessnotification(SignUpResponse signUpResponse) {

    }

    @Override
    public void onSignUpFailurenotification(String msg) {

    }

    private class initRecAsync extends AsyncTask<Void, Void, Void> {
        ProgressDialog dialog = new ProgressDialog(MainActivity1.this);

        @Override
        protected void onPreExecute() {
            Log.d(TAG, "initRecAsync onPreExecute called");
           /* dialog.setMessage("Initializing...");
            dialog.setCancelable(false);
            dialog.show();*/
            super.onPreExecute();
        }

        protected Void doInBackground(Void... args) {
            // create dlib_rec_example directory in sd card and copy model files
            File folder = new File(Constants.getDLibDirectoryPath());
            boolean success = false;
            if (!folder.exists()) {
                success = folder.mkdirs();
            }
            if (success) {
                File image_folder = new File(Constants.getDLibImageDirectoryPath());
                image_folder.mkdirs();
                if (!new File(Constants.getFaceShapeModelPath()).exists()) {
                    FileUtils.copyFileFromRawToOthers(MainActivity1.this, R.raw.shape_predictor_5_face_landmarks, Constants.getFaceShapeModelPath());
                }
                if (!new File(Constants.getFaceDescriptorModelPath()).exists()) {
                    FileUtils.copyFileFromRawToOthers(MainActivity1.this, R.raw.dlib_face_recognition_resnet_model_v1, Constants.getFaceDescriptorModelPath());
                }
            } else {
                //Log.d(TAG, "error in setting dlib_rec_example directory");
            }
            mFaceRec = new FaceRec(Constants.getDLibDirectoryPath());
          //  changeProgressDialogMessage(dialog, "Adding people...");
            mFaceRec.train();
            return null;
        }

        protected void onPostExecute(Void result) {
           /* if(dialog != null && dialog.isShowing()){
                dialog.dismiss();
            }*/
        }
    }

    String[] permissions = new String[]{
            Manifest.permission.CAMERA,
            Manifest.permission.WRITE_EXTERNAL_STORAGE,
    };

    private boolean checkPermissions() {
        int result;
        List<String> listPermissionsNeeded = new ArrayList<>();
        for (String p : permissions) {
            result = ContextCompat.checkSelfPermission(this, p);
            if (result != PackageManager.PERMISSION_GRANTED) {
                listPermissionsNeeded.add(p);
            }
        }
        if (!listPermissionsNeeded.isEmpty()) {
            ActivityCompat.requestPermissions(this, listPermissionsNeeded.toArray(new String[listPermissionsNeeded.size()]), 100);
            return false;
        }
        return true;
    }


    @Override
    protected void onResume() {
        Log.d(TAG, "onResume called");
        super.onResume();

        if (ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA)
                == PackageManager.PERMISSION_GRANTED) {
            mCameraView.start();
            int facing = mCameraView.getFacing();
            mCameraView.setFacing(facing == CameraView.FACING_FRONT ?
                    CameraView.FACING_BACK : CameraView.FACING_FRONT);

        }
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE)
                == PackageManager.PERMISSION_GRANTED) {
            Bundle b=getIntent().getExtras();
        //    if(b!=null)
         //   {
                new initRecAsync().execute();
         //  }

        }
    }

    protected void onPause() {
        Log.d(TAG, "onPause called");
        mCameraView.stop();
        super.onPause();
    }

    @Override
    protected void onDestroy() {
        Log.d(TAG, "onDestroy called");
        super.onDestroy();
        if (mFaceRec != null) {
            mFaceRec.release();
        }
        if (mBackgroundHandler != null) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR2) {
                mBackgroundHandler.getLooper().quitSafely();
            } else {
                mBackgroundHandler.getLooper().quit();
            }
            mBackgroundHandler = null;
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions,
            @NonNull int[] grantResults) {
        if (requestCode == 100) {
            if (grantResults.length > 0
                    && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                // do something
            }
            return;
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.drawer, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.switch_flash:
                if (mCameraView != null) {
                    mCurrentFlash = (mCurrentFlash + 1) % FLASH_OPTIONS.length;
                    item.setTitle(FLASH_TITLES[mCurrentFlash]);
                    item.setIcon(FLASH_ICONS[mCurrentFlash]);
                    mCameraView.setFlash(FLASH_OPTIONS[mCurrentFlash]);
                }
                return true;
            case R.id.switch_camera:
                if (mCameraView != null) {
                    int facing = mCameraView.getFacing();
                    mCameraView.setFacing(facing == CameraView.FACING_FRONT ?
                            CameraView.FACING_BACK : CameraView.FACING_FRONT);
                }
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private Handler getBackgroundHandler() {
        if (mBackgroundHandler == null) {
            HandlerThread thread = new HandlerThread("background");
            thread.start();
            mBackgroundHandler = new Handler(thread.getLooper());
        }
        return mBackgroundHandler;
    }

    private String getResultMessage(ArrayList<String> names) {
        String msg = new String();
        if (names.isEmpty()) {
            msg = "No face detected or Unknown person";

        } else {
            for(int i=0; i<names.size(); i++) {
                msg += names.get(i).split(Pattern.quote("."))[0];
                if (i!=names.size()-1) msg+=", ";
            }
            msg+=" found!";
           // entrytime();
        }
        return msg;
    }
    public void entrytime() {

        DatabaseHelper databaseHelper = new DatabaseHelper(MainActivity1.this);
        EmployeData data = new EmployeData();

        data.setUser_id(PrefUtils.getKey(MainActivity1.this, AppConstants.UserID));
        data.setIn_location(MainActivity.address);
        data.setIn_time(MainActivity.currenttime);
        data.setIn_location_url(MainActivity.locationURL);
        data.setIn_mac_address(MainActivity.deviceiD);
        data.setIsupload("false");
        data.setIslogin(PrefUtils.getKey(MainActivity1.this, AppConstants.IsLogin));
        databaseHelper.TimeIn(data);

        // Snack Bar to show success message that record is wrong

        if (PrefUtils.getKey(MainActivity1.this, AppConstants.IsLogin).equals("0")) {
            PrefUtils.storeKey(MainActivity1.this, "date", MainActivity.currentdate);
            PrefUtils.storeKey(MainActivity1.this, "time", MainActivity.currenttime);
            PrefUtils.storeKey(MainActivity1.this, "address", MainActivity.address);
            PrefUtils.storeKey(MainActivity1.this, "locationurl", MainActivity.locationURL);
            MainActivity.timein_text.setText("Time Out");
            MainActivity. timein_img.setBackgroundResource(R.drawable.time_out);
            PrefUtils.storeKey(MainActivity1.this, AppConstants.IsLogin, "1");
            storetimeinvalue();
            SharedPreferences pref = MainActivity1.this.getSharedPreferences(Config.SHARED_PREF, 0);
            String regId = pref.getString("regId", null);
            initnotificationAPIResources(userid, regId, MainActivity.currenttime, PrefUtils.getKey(MainActivity1.this, AppConstants.Clientid),PrefUtils.getKey(MainActivity1.this, AppConstants.Api_Token));
            initadminnotificationAPIResources(userid, PrefUtils.getKey(MainActivity1.this, AppConstants.Name), PrefUtils.getKey(MainActivity1.this, AppConstants.Clientid),PrefUtils.getKey(MainActivity1.this, AppConstants.Api_Token));
            Log.d("userid",userid);
            Log.d("name",PrefUtils.getKey(MainActivity1.this, AppConstants.Name));
            Log.d("clientid",PrefUtils.getKey(MainActivity1.this, AppConstants.Clientid));
            Log.d("apitoken",PrefUtils.getKey(MainActivity1.this, AppConstants.Api_Token));


        } else if (PrefUtils.getKey(MainActivity1.this, AppConstants.IsLogin).equals("1")) {
            MainActivity.timein_text.setText("Time In");
            MainActivity.timein_img.setBackgroundResource(R.drawable.time_in);
            PrefUtils.storeKey(MainActivity1.this, AppConstants.IsLogin, "0");
            storetimeOutvalue();

        }
        MainActivity.mTimeInButton.setClickable(false);
        MainActivity.mTimeInButton.setEnabled(false);

    }
    //timeout data store in Localdatabase
    private void storetimeOutvalue() {
        DatabaseHelper databaseHelper = new DatabaseHelper(MainActivity1.this);
        EmployeData data = new EmployeData();

        data.setUser_id(PrefUtils.getKey(MainActivity1.this, AppConstants.UserID));
        data.setUsername(PrefUtils.getKey(MainActivity1.this, AppConstants.Name));
        data.setAttendance_id("50000");
        data.setOut_date(MainActivity.currentdate);
        data.setOut_time(MainActivity.currenttime);
        data.setOut_location(MainActivity.address);
        data.setOut_location_url(MainActivity.locationURL);
        databaseHelper.UserupdateRecords(data);

    }
    //timein data store in Localdatabase
    private void storetimeinvalue() {
        DatabaseHelper databaseHelper = new DatabaseHelper(MainActivity1.this);
        EmployeData data = new EmployeData();
        data.setUser_id(PrefUtils.getKey(MainActivity1.this, AppConstants.UserID));
        data.setUsername(PrefUtils.getKey(MainActivity1.this, AppConstants.Name));
        data.setAttendance_id("50000");
        data.setIn_date(MainActivity.currentdate);
        data.setIn_time(MainActivity.currenttime);
        data.setIn_location(MainActivity.address);
        data.setIn_location_url(MainActivity.locationURL);
        data.setOut_date("null");
        data.setOut_time(null);
        data.setOut_location(null);
        data.setOut_location_url(null);
        databaseHelper.UserRecords(data);
    }

    private void initnotificationAPIResources(String userid, String regId, String currenttime,String clientid,String apitoken) {
        SignUpPresenterImplnotification loginPresenter = new SignUpPresenterImplnotification(MainActivity1.this);
        loginPresenter.callApi(AppConstants.Notification, userid, regId, currenttime,clientid,apitoken);

    }

    private void initadminnotificationAPIResources(String userid, String username,String clientid,String apitoken) {
        SignUpadminPresenterImpl loginPresenter = new SignUpadminPresenterImpl(MainActivity1.this);
        loginPresenter.callApi(AppConstants.AdminNotification, userid, username,clientid,apitoken);

    }
    private class recognizeAsync extends AsyncTask<Bitmap, Void, ArrayList<String>> {
        ProgressDialog dialog = new ProgressDialog(MainActivity1.this);
        private int mScreenRotation = 0;
        private boolean mIsComputing = false;
        private Bitmap mCroppedBitmap = Bitmap.createBitmap(INPUT_SIZE, INPUT_SIZE, Bitmap.Config.ARGB_8888);

        @Override
        protected void onPreExecute() {
            dialog.setMessage("Recognizing...");
            dialog.setCancelable(false);
            dialog.show();
            super.onPreExecute();
        }

        protected ArrayList<String> doInBackground(Bitmap... bp) {

            drawResizedBitmap(bp[0], mCroppedBitmap);
            Log.d(TAG, "byte to bitmap");

            long startTime = System.currentTimeMillis();
            List<VisionDetRet> results;
            results = mFaceRec.recognize(mCroppedBitmap);
            long endTime = System.currentTimeMillis();
            Log.d(TAG, "Time cost: " + String.valueOf((endTime - startTime) / 1000f) + " sec");

            ArrayList<String> names = new ArrayList<>();
            for(VisionDetRet n:results) {
                names.add(n.getLabel());
            }
            return names;
        }

        protected void onPostExecute(ArrayList<String> names) {
            if(dialog != null && dialog.isShowing()){
                dialog.dismiss();
                AlertDialog.Builder builder1 = new AlertDialog.Builder(MainActivity1.this);
                builder1.setMessage(getResultMessage(names));
                builder1.setCancelable(true);
                AlertDialog alert11 = builder1.create();
                alert11.show();
                Intent i=new Intent(MainActivity1.this,MainActivity.class);
                startActivity(i);

            }
        }

        private void drawResizedBitmap(final Bitmap src, final Bitmap dst) {
            Display getOrient = ((WindowManager) getSystemService(Context.WINDOW_SERVICE)).getDefaultDisplay();
            int orientation = Configuration.ORIENTATION_UNDEFINED;
            Point point = new Point();
            getOrient.getSize(point);
            int screen_width = point.x;
            int screen_height = point.y;
            Log.d(TAG, String.format("screen size (%d,%d)", screen_width, screen_height));
            if (screen_width < screen_height) {
                orientation = Configuration.ORIENTATION_PORTRAIT;
                mScreenRotation = 0;
            } else {
                orientation = Configuration.ORIENTATION_LANDSCAPE;
                mScreenRotation = 0;
            }

         //   Assert.assertEquals(dst.getWidth(), dst.getHeight());
            final float minDim = Math.min(src.getWidth(), src.getHeight());

            final Matrix matrix = new Matrix();

            // We only want the center square out of the original rectangle.
            final float translateX = -Math.max(0, (src.getWidth() - minDim) / 2);
            final float translateY = -Math.max(0, (src.getHeight() - minDim) / 2);
            matrix.preTranslate(translateX, translateY);

            final float scaleFactor = dst.getHeight() / minDim;
            matrix.postScale(scaleFactor, scaleFactor);

            // Rotate around the center if necessary.
            if (mScreenRotation != 0) {
                matrix.postTranslate(-dst.getWidth() / 2.0f, -dst.getHeight() / 2.0f);
                matrix.postRotate(mScreenRotation);
                matrix.postTranslate(dst.getWidth() / 2.0f, dst.getHeight() / 2.0f);
            }

            final Canvas canvas = new Canvas(dst);
            canvas.drawBitmap(src, matrix, null);
        }
    }


    private CameraView.Callback mCallback
            = new CameraView.Callback() {

        @Override
        public void onCameraOpened(CameraView cameraView) {
            Log.d(TAG, "onCameraOpened");
        }

        @Override
        public void onCameraClosed(CameraView cameraView) {
            Log.d(TAG, "onCameraClosed");
        }

        @Override
        public void onPictureTaken(CameraView cameraView, final byte[] data) {
            Log.d(TAG, "onPictureTaken " + data.length);
            Toast.makeText(cameraView.getContext(), R.string.picture_taken, Toast.LENGTH_SHORT)
                    .show();
            Bitmap bp = BitmapFactory.decodeByteArray(data, 0, data.length);
            new recognizeAsync().execute(bp);
        }

    };

    public static class ConfirmationDialogFragment extends DialogFragment {

        private static final String ARG_MESSAGE = "message";
        private static final String ARG_PERMISSIONS = "permissions";
        private static final String ARG_REQUEST_CODE = "request_code";
        private static final String ARG_NOT_GRANTED_MESSAGE = "not_granted_message";


        public static ConfirmationDialogFragment newInstance(@StringRes int message,
                String[] permissions, int requestCode, @StringRes int notGrantedMessage) {
            ConfirmationDialogFragment fragment = new ConfirmationDialogFragment();
            Bundle args = new Bundle();
            args.putInt(ARG_MESSAGE, message);
            args.putStringArray(ARG_PERMISSIONS, permissions);
            args.putInt(ARG_REQUEST_CODE, requestCode);
            args.putInt(ARG_NOT_GRANTED_MESSAGE, notGrantedMessage);
            fragment.setArguments(args);
            return fragment;
        }

        @NonNull
        @Override
        public Dialog onCreateDialog(Bundle savedInstanceState) {
            final Bundle args = getArguments();
            return new AlertDialog.Builder(getActivity())
                    .setMessage(args.getInt(ARG_MESSAGE))
                    .setPositiveButton(android.R.string.ok,
                            new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    String[] permissions = args.getStringArray(ARG_PERMISSIONS);
                                    if (permissions == null) {
                                        throw new IllegalArgumentException();
                                    }
                                    ActivityCompat.requestPermissions(getActivity(),
                                            permissions, args.getInt(ARG_REQUEST_CODE));
                                }
                            })
                    .setNegativeButton(android.R.string.cancel,
                            new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    Toast.makeText(getActivity(),
                                            args.getInt(ARG_NOT_GRANTED_MESSAGE),
                                            Toast.LENGTH_SHORT).show();
                                }
                            })
                    .create();
        }

    }

}
