package com.group28.cs160.babybump;

import android.app.Service;
import android.content.Intent;
import android.os.Binder;
import android.os.IBinder;

public class FoodAlertService extends Service {
    public FoodAlertService() {
    }

    @Override
    public IBinder onBind(Intent intent) {
        // Pull initial location
        // Get all nearby stores
        // Compare distance to nearby stores
        // Send alert to watch if near store

        // if not, create processes
        // Pulls location every 10 seconds
        // Check distance
        return mBinder;
    }

    public class LocalBinder extends Binder {
        FoodAlertService getService(){
            return FoodAlertService.this;
        }
    }

    private final IBinder mBinder = new LocalBinder();
}
