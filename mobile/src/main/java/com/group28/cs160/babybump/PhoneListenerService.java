package com.group28.cs160.babybump;

/**
 * Created by eviltwin on 4/20/16.
 */
import android.content.Intent;
import android.util.Log;

import com.google.android.gms.wearable.MessageEvent;
import com.google.android.gms.wearable.WearableListenerService;

public class PhoneListenerService extends WearableListenerService {

    // WearableListenerServices don't need an iBinder or an onStartCommand: they just need an onMessageReceieved.
    private static final String TIMELINE = "/timeline";

    @Override
    public void onMessageReceived(MessageEvent messageEvent) {

        Log.d("PhoneListenerService", "in PhoneListenerService, got: " + messageEvent.getPath());
        if (messageEvent.getPath().equalsIgnoreCase(TIMELINE) ) {
            // Value contains the String we sent over in WatchToPhoneService, "good job"
            String event =  messageEvent.getData().toString();

            Intent intent = new Intent(this, MainActivity.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            //you need to add this flag since you're starting a new activity from a service
            intent.putExtra(MainActivity.EVENT_OBJECT, event);
            Log.d("PhoneListenerService", "about to start watch MainActivity with Event: "+ event);
            startActivity(intent);
        } else {
            super.onMessageReceived(messageEvent);
        }
    }
}
