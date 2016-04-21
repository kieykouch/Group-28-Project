package com.group28.cs160.babybump;

/**
 * Created by eviltwin on 4/20/16.
 */
import android.app.Service;
import android.content.Intent;
import android.os.Bundle;
import android.os.IBinder;
import android.support.annotation.NonNull;
import android.util.Log;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.wearable.MessageApi;
import com.google.android.gms.wearable.Node;
import com.google.android.gms.wearable.Wearable;

import java.util.List;
import java.util.concurrent.TimeUnit;

public class WatchToPhoneService extends Service {
    public final static String EVENT_OBJECT = "com.groupd28.cs160.babybump.EVENT_OBJECT";
    public final static String HEART_RATE = "/heart";

    private GoogleApiClient mApiClient;
    private List<Node> nodes;

    @Override
    public void onCreate() {
        super.onCreate();
        //initialize the googleAPIClient for message passing
        mApiClient = new GoogleApiClient.Builder( this )
                .addApi( Wearable.API )
                .addConnectionCallbacks(new GoogleApiClient.ConnectionCallbacks() {
                    @Override
                    public void onConnected(Bundle connectionHint) {
                        Log.d("WatchToPhone", "Connected to Phone");
                    }

                    @Override
                    public void onConnectionSuspended(int cause) {
                    }
                })
                .addOnConnectionFailedListener(new GoogleApiClient.OnConnectionFailedListener() {
                    @Override
                    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {
                        Log.e("WatchToPhone", "Cannot connect");
                    }
                })
                .build();
        mApiClient.connect();

    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        mApiClient.disconnect();
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        Log.d("WatchToPhoneService", "Sending message to phone..");
        final Bundle extras = intent.getExtras();
        // Send the message with the cat name
        new Thread(new Runnable() {
            @Override
            public void run() {
                //first, connect to the apiclient
                nodes = Wearable.NodeApi.getConnectedNodes(mApiClient).await(10, TimeUnit.SECONDS).getNodes();
                if (nodes.size() == 0) {
                    Log.d("WatchToPhoneService", "No phone connected.");
                    return;
                }
                if (extras.containsKey(EVENT_OBJECT)) {
                    //now that you're connected, send a massage.
                    final String event = extras.getString(EVENT_OBJECT);
                    sendMessage("/timeline", event);
                }
                if (extras.containsKey(HEART_RATE)) {
                    final String heartRate = extras.getString(HEART_RATE);
                    sendMessage("/heart", heartRate);
                }
            }
        }).start();

        return START_STICKY;
    }

    @Override //remember, all services need to implement an IBiner
    public IBinder onBind(Intent intent) {
        return null;
    }

    private void sendMessage( final String path, final String text) {
        for(Node node : nodes) {
            Log.d("WatchToPhoneService", "Sending message to phone with path: " + path + " and text: " + text);
            //we find 'nodes', which are nearby bluetooth devices (aka emulators)
            //send a message for each of these nodes (just one, for an emulator)
            MessageApi.SendMessageResult result = Wearable.MessageApi.sendMessage(
                    mApiClient, node.getId(), path, text.getBytes() ).await();
            //4 arguments: api client, the node ID, the path (for the listener to parse),
            //and the message itself (you need to convert it to bytes.)
        }
    }
}