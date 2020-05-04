package com.wfms.nectar.utils;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.util.Log;

/**
 * Created by Abhishek on 26/04/2018
 */
public class NetworkUtil {
    public static int TYPE_WIFI = 1;
    public static int TYPE_MOBILE = 2;
    public static int TYPE_NOT_CONNECTED = 0;


    public static int getConnectivityStatus(Context context) {
        try {
            ConnectivityManager cm = (ConnectivityManager) context
                    .getSystemService(Context.CONNECTIVITY_SERVICE);

            NetworkInfo activeNetwork = cm.getActiveNetworkInfo();
            if (null != activeNetwork) {
                if (activeNetwork.getType() == ConnectivityManager.TYPE_WIFI)
                    return TYPE_WIFI;

                if (activeNetwork.getType() == ConnectivityManager.TYPE_MOBILE)
                    return TYPE_MOBILE;
            }
            return TYPE_NOT_CONNECTED;
        }
        catch (Exception e){
            Log.d("NetworkUtil",e.getMessage().toString());
        }
        return 0;
    }

    public static String getConnectivityStatusString(Context context) {
        int conn = NetworkUtil.getConnectivityStatus(context);
        String status = null;
        if (conn == NetworkUtil.TYPE_WIFI) {
            status = "Wifi enabled";
        } else if (conn == NetworkUtil.TYPE_MOBILE) {
            status = "Mobile data enabled";
        } else if (conn == NetworkUtil.TYPE_NOT_CONNECTED) {
            status = "Not connected to Internet";
        }
        return status;
    }

    public static boolean isOnline(Context context) {
        if(context == null) return true;
        boolean isConnected = getConnectivityStatus(context) != TYPE_NOT_CONNECTED;
        //boolean isConnected = isConnectingToInternet(context);

        if (isConnected) {
            return true;
        }

        /*if(context != null && context instanceof Activity) {
            if (!((Activity) context).isFinishing()) {
                AlertDialogUtils.showDialogWithIcon(context, context.getString(R.string.no_internet),
                        context.getString(R.string.internet_msg_txt),
                        context.getString(R.string.reconnect), null);
            }
        }*/
        return false;
    }


    /**
     * This method checks the internet connection in the device and returns true/false
     *
     * @param $context
     * @return boolean
     */
    public static boolean isConnectingToInternet(Context $context) {
        ConnectivityManager connectivity = (ConnectivityManager) $context.getSystemService(Context.CONNECTIVITY_SERVICE);
        if (connectivity != null) {
            NetworkInfo[] info = connectivity.getAllNetworkInfo();
            if (info != null)
                for (int i = 0; i < info.length; i++)
                    if (info[i].getState() == NetworkInfo.State.CONNECTED) {
                        return true;
                    }
        }
        return false;
    }

}
