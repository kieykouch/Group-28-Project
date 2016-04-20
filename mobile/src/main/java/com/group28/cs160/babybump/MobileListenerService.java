package com.group28.cs160.babybump;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;

public class MobileListenerService extends Service {
    public MobileListenerService() {
    }

    @Override
    public IBinder onBind(Intent intent) {
        // TODO: Return the communication channel to the service.
        throw new UnsupportedOperationException("Not yet implemented");
    }
}
