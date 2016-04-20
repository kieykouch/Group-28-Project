package com.group28.cs160.babybump;

import android.app.PendingIntent;
import android.app.Service;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.os.IBinder;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.NotificationCompat;
import android.support.v4.app.NotificationManagerCompat;
import android.util.Log;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.wearable.Wearable;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;

public class MobileToWatchService extends Service implements
        GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener{
    public MobileToWatchService() {
    }

    public void onCreate() {
        super.onCreate();
        mApiClient = new GoogleApiClient.Builder(this)
                .addApi(Wearable.API)
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .build();
        mApiClient.connect();
    }

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        Log.d("MobileToWatch", "service started");
        int service_id = intent.getIntExtra("service", -1);
        if (service_id == FOOD_NOTIFICATION) {
            int notificationId = 001;
            Intent viewIntent = new Intent(this, MainActivity.class);
            viewIntent.putExtra("caller", "food-alert");
            viewIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
            PendingIntent viewPendingIntent =
                    PendingIntent.getActivity(this, 0, viewIntent, 0);

            Bitmap background = BitmapFactory.decodeResource(getApplicationContext().getResources(),
                    R.drawable.sushi);
            ByteArrayOutputStream out = new ByteArrayOutputStream();
            background.compress(Bitmap.CompressFormat.PNG, 100, out);
            background = BitmapFactory.decodeStream(new ByteArrayInputStream(out.toByteArray()));
            NotificationCompat.WearableExtender wearableExtender = new NotificationCompat.WearableExtender()
                    .setBackground(background);

            NotificationCompat.Builder notificationBuilder =
                    new NotificationCompat.Builder(this)
                            .setSmallIcon(R.mipmap.ic_launcher)
                            .setContentTitle("Don't Eat")
                            .setContentText(intent.getStringExtra("avoid"))
                            .extend(wearableExtender)
                            .setContentIntent(viewPendingIntent);

            NotificationManagerCompat notificationManager =
                    NotificationManagerCompat.from(this);
            Log.d("MobileToWatch", "sending notification");
            notificationManager.notify(notificationId, notificationBuilder.build());
        }
        return START_NOT_STICKY;
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

    private GoogleApiClient mApiClient;

    static int FOOD_NOTIFICATION = 0;
}
