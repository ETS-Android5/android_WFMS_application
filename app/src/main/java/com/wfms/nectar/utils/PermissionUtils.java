package com.wfms.nectar.utils;


import android.Manifest;
import android.annotation.TargetApi;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.provider.Settings;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by aashita on 25/07/17.
 */

public class PermissionUtils {

    public static final String PERMISSION_CAMERA[] = {Manifest.permission.CAMERA};
    public static final int PERMISSION_READ_STORAGE_REQ = 10001;
    public static final int PERMISSION_CAMERA_REQ = 10002;
    public static final int PERMISSION_PROVIDER_REQ = 101;
    public static final int PERMISSION_READ_STORAGE = 1003;
    public static final int REQUEST_GALLERY_PERMISSION=1008;



    public static boolean checkForPermission(AppCompatActivity context, String[] permissions, int requestCode) {
        boolean checkPermission = false;
        for (String permission : permissions) {
            if (ActivityCompat.checkSelfPermission(context, permission) != PackageManager.PERMISSION_GRANTED) {
                checkPermission = true;
                break;
            }
        }
        if (checkPermission) {
            ActivityCompat.requestPermissions(context, permissions, requestCode);
            return false;
        }
        return true;
    }

    public static boolean checkForFragmentPermission(Fragment context, String[] permissions, int requestCode) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            boolean checkPermission = false;
            for (String permission : permissions) {
                if (ActivityCompat.checkSelfPermission(context.getActivity(), permission) != PackageManager.PERMISSION_GRANTED) {
                    checkPermission = true;
                    break;
                }
            }
            if (checkPermission) {
                context.requestPermissions(permissions, requestCode);
                return false;
            }
        }
        return true;
    }

    // Check and Request Storage Permissions
    @TargetApi(Build.VERSION_CODES.JELLY_BEAN)
    public static boolean checkAndRequestStoragePermissions(Context context) {
        // Compare if permission already granted
        if (ContextCompat.checkSelfPermission(context, Manifest.permission.READ_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED) {
            // if permission not granted, request permission and return true
            return true;
        } else {
            // if permission not granted, request permission and return false
            ActivityCompat.requestPermissions((AppCompatActivity) context,
                    new String[]{Manifest.permission.READ_EXTERNAL_STORAGE},
                    PERMISSION_READ_STORAGE);
            return false;
        }
    }

    public static boolean checkAndRequestPermissions(Context context) {
        int permissionSendMessage = ContextCompat.checkSelfPermission(context,
                Manifest.permission.SEND_SMS);
        int receiveSMS = ContextCompat.checkSelfPermission(context, Manifest.permission.RECEIVE_SMS);
        int readSMS = ContextCompat.checkSelfPermission(context, Manifest.permission.READ_SMS);
        List<String> listPermissionsNeeded = new ArrayList<>();
        if (receiveSMS != PackageManager.PERMISSION_GRANTED) {
            listPermissionsNeeded.add(Manifest.permission.RECEIVE_MMS);
        }
        if (readSMS != PackageManager.PERMISSION_GRANTED) {
            listPermissionsNeeded.add(Manifest.permission.READ_SMS);
        }
        if (permissionSendMessage != PackageManager.PERMISSION_GRANTED) {
            listPermissionsNeeded.add(Manifest.permission.SEND_SMS);
        }
        if (!listPermissionsNeeded.isEmpty()) {
            ActivityCompat.requestPermissions((AppCompatActivity) context,
                    listPermissionsNeeded.toArray(new String[listPermissionsNeeded.size()]),
                    AppConstants.REQUEST_ID_MULTIPLE_PERMISSIONS);
            return false;
        }
        return true;
    }

    public static void launchAppSettings(Context context) {
        if(context != null){
            Intent intent = new Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
            Uri uri = Uri.fromParts("package", context.getPackageName(), null);
            intent.setData(uri);
            context.startActivity(intent);
        }
    }

}
