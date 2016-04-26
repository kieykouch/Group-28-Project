package com.group28.cs160.noms4two;

import android.content.Intent;
import android.util.Log;

import com.google.android.gms.wearable.DataEventBuffer;
import com.google.android.gms.wearable.MessageApi;
import com.google.android.gms.wearable.MessageEvent;
import com.google.android.gms.wearable.WearableListenerService;

public class WatchListenerService extends WearableListenerService implements
        MessageApi.MessageListener{

    @Override
    public void onCreate() {
        super.onCreate();
        Log.d("WatchListenerService", "Created!!");
    }

    @Override
    public void onDataChanged(DataEventBuffer dataEvents) {
        // Use DataMap for events
        Log.d("WatchListenerService", "Data received");
    }

    @Override
    public void onMessageReceived(MessageEvent messageEvent) {
        Log.d("WatchListenerService", "Message receives");
        if(messageEvent.getPath().equalsIgnoreCase(INFO)) {
            String info = messageEvent.getData().toString();
            Intent intent = new Intent(this, MainActivity.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            //you need to add this flag since you're starting a new activity from a service
//            intent.putExtra(MainActivity.INFO, info);
            startActivity(intent);
        } else {
            super.onMessageReceived(messageEvent);
        }
    }

    final String INFO = "/heart";

}
