package com.wfms.nectar.utils;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

/**
 * Created by kunal on 15/01/2018.
 */
public class SensorRestarterBroadcastReceiver extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
        Log.i(SensorRestarterBroadcastReceiver.class.getSimpleName(), "Broadcast Received");
        Log.i(SensorRestarterBroadcastReceiver.class.getSimpleName(), "Restarting Service");
        context.startService(new Intent(context, SensorService.class));
    }
}