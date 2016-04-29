package com.group28.cs160.noms4two;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.Log;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.wearable.DataApi;
import com.google.android.gms.wearable.DataEvent;
import com.google.android.gms.wearable.DataEventBuffer;
import com.google.android.gms.wearable.DataItem;
import com.google.android.gms.wearable.Wearable;
import com.google.android.gms.wearable.WearableListenerService;
import com.group28.cs160.shared.NutritionFacts;

public class WatchListenerService extends WearableListenerService implements
        DataApi.DataListener, GoogleApiClient.ConnectionCallbacks,
        GoogleApiClient.OnConnectionFailedListener {

    private GoogleApiClient apiClient;

    public static String GOALS = "/com.group28.cs160.goals";
    public static String INFO = "/com.group28.cs160.info";

    @Override
    public void onCreate() {
        super.onCreate();
        Log.d("WatchListener", "Service created.");
        apiClient = new GoogleApiClient.Builder(this)
                .addApi(Wearable.API)
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .build();

        apiClient.connect();
    }

    @Override
    public void onDataChanged(DataEventBuffer dataEvents) {
        // Use DataMap for events
        Log.d("WatchListenerService", "Data received");
        for (DataEvent event : dataEvents) {
            if (event.getType() == DataEvent.TYPE_CHANGED) {
                // Store updates to a file that main activity can read.
                DataItem item = event.getDataItem();
                if (item.getUri().getPath().compareTo(INFO) == 0) {
                    NutritionFacts info = NutritionFacts.deserialize(item.getData());
                    DiskNutritionFacts.writeToFile(getBaseContext(), DiskNutritionFacts.INFO_FILE, info);
                }
                if (item.getUri().getPath().compareTo(GOALS) == 0) {
                    NutritionFacts goals = NutritionFacts.deserialize(item.getData());
                    DiskNutritionFacts.writeToFile(getBaseContext(), DiskNutritionFacts.GOALS_FILE, goals);
                }
            } else if (event.getType() == DataEvent.TYPE_DELETED) {
                // DataItem deleted
            }
        }
    }

    @Override
    public void onConnected(@Nullable Bundle bundle) {
        Log.d("WatchListener", "Connected to watch");
    }

    @Override
    public void onConnectionSuspended(int i) {

    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {

    }
}
