package com.group28.cs160.noms4two;

/**
 * Created by eviltwin on 4/20/16.
 */
import android.content.Intent;
import android.util.Log;

import com.google.android.gms.wearable.MessageEvent;
import com.google.android.gms.wearable.WearableListenerService;

public class MobileListenerService extends WearableListenerService {

    // WearableListenerServices don't need an iBinder or an onStartCommand: they just need an onMessageReceieved.
    private static final String TIMELINE = "/timeline";
    private static final String HEART_RATE = "/heart";

    @Override
    public void onMessageReceived(MessageEvent messageEvent) {

        Log.d("MobileListenerService", "in MobileListenerService, got: " + messageEvent.getPath());
        if (messageEvent.getPath().equalsIgnoreCase(TIMELINE) ) {
            // Value contains the String we sent over in WatchToPhoneService, "good job"
            String event =  messageEvent.getData().toString();

            Intent intent = new Intent(this, MainActivity.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            //you need to add this flag since you're starting a new activity from a service
            intent.putExtra("EVENT_OBJECT", event);
            Log.d("MobileListenerService", "about to start watch MainActivity with Event: "+ event);
            startActivity(intent);
        } else if (messageEvent.getPath().equalsIgnoreCase(HEART_RATE)){
            String heartRate =  messageEvent.getData().toString();

            Intent intent = new Intent(this, MainActivity.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            //you need to add this flag since you're starting a new activity from a service
            intent.putExtra("EVENT_OBJECT", heartRate);
            Log.d("MobileListenerService", "about to start watch MainActivity with Event: "+ heartRate);
            startActivity(intent);
        } else {
            super.onMessageReceived(messageEvent);
        }
    }
}
