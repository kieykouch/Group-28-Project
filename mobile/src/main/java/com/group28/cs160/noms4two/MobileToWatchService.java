package com.group28.cs160.noms4two;

import android.app.Service;
import android.content.Intent;
import android.os.Bundle;
import android.os.IBinder;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.Log;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.PendingResult;
import com.google.android.gms.wearable.DataApi;
import com.google.android.gms.wearable.NodeApi;
import com.google.android.gms.wearable.PutDataRequest;
import com.google.android.gms.wearable.Wearable;
import com.group28.cs160.shared.NutritionFacts;

public class MobileToWatchService extends Service implements
        GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener {
    public static String GOALS = "/com.group28.cs160.goals";
    public static String INFO = "/com.group28.cs160.info";

    public void onCreate() {
        super.onCreate();
        apiClient = new GoogleApiClient.Builder(this)
                .addApi(Wearable.API)
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .build();
        apiClient.connect();
    }

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        final Bundle extras = intent.getExtras();
        new Thread(new Runnable() {
            @Override
            public void run() {
                NodeApi.GetConnectedNodesResult nodes = Wearable.NodeApi.getConnectedNodes(apiClient).await();
                Log.d("MobileToWatch", "Syncing with watch");
                if (nodes.getNodes().size() == 0) Log.d("MobileToWatch", "Error sending data to watch");
                if (extras.containsKey(GOALS)) {
                    PutDataRequest putDataRequest = PutDataRequest.create(GOALS);
                    putDataRequest.setData(NutritionFacts.serialize((NutritionFacts) extras.get(GOALS)));
                    PendingResult<DataApi.DataItemResult> pendingResult =
                            Wearable.DataApi.putDataItem(apiClient, putDataRequest);
                }
                if (extras.containsKey(INFO)) {
                    PutDataRequest putDataRequest = PutDataRequest.create(INFO);
                    putDataRequest.setData(NutritionFacts.serialize((NutritionFacts) extras.get(INFO)));
                    PendingResult<DataApi.DataItemResult> pendingResult =
                            Wearable.DataApi.putDataItem(apiClient, putDataRequest);
                }
            }
        }).start();
        return START_STICKY;
    }

    @Override
    public void onConnected(@Nullable Bundle bundle) {
        Log.d("MobileToWatch", "Connected to watch");
    }

    @Override
    public void onConnectionSuspended(int i) {

    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {

    }

    private GoogleApiClient apiClient;
}
