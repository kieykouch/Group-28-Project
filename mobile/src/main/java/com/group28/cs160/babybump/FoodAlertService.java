package com.group28.cs160.babybump;

import android.Manifest;
import android.app.Service;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.os.AsyncTask;
import android.os.Binder;
import android.os.Bundle;
import android.os.IBinder;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.util.Base64;
import android.util.Log;
import android.util.Xml;

import com.github.scribejava.core.builder.ServiceBuilder;
import com.github.scribejava.core.model.OAuth1AccessToken;
import com.github.scribejava.core.model.OAuthRequest;
import com.github.scribejava.core.model.Response;
import com.github.scribejava.core.model.Verb;
import com.github.scribejava.core.oauth.OAuth10aService;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.server.converter.StringToIntConverter;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;

import org.json.JSONException;
import org.json.JSONObject;

import java.net.URL;
import java.security.SignatureException;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;
import javax.net.ssl.HttpsURLConnection;

public class FoodAlertService extends Service implements GoogleApiClient.ConnectionCallbacks,
        GoogleApiClient.OnConnectionFailedListener, LocationListener {
    public FoodAlertService() {
    }

    @Override
    public void onCreate() {
        super.onCreate();
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startid) {
        mGoogleClient = new GoogleApiClient.Builder(this)
                .addApi(LocationServices.API)
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .build();
        mGoogleClient.connect();
        Log.d("Food", "Food acivity is started");
        return START_STICKY;
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onConnected(@Nullable Bundle bundle) {
        if (mRequestingLocation) {
            LocationRequest mLocationRequest = new LocationRequest();
            mLocationRequest.setInterval(10000);
            mLocationRequest.setFastestInterval(5000);
            mLocationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY); // Can change to low power
            try {
                LocationServices.FusedLocationApi.requestLocationUpdates(mGoogleClient,
                        mLocationRequest, FoodAlertService.this);
            } catch (SecurityException e) {
                Log.e("Food", e.getMessage());
            }
        }

    }

    @Override
    public void onConnectionSuspended(int i) {

    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {

    }

    @Override
    public void onLocationChanged(Location location) {
        Log.d("Location",
                String.format("%f, %f", location.getLongitude(), location.getLatitude()));
        if (prev_loc == null) {
            prev_loc = location;
            return;
        }
        double prev_lon, prev_lat, lon, lat, delta;
        prev_lon = prev_loc.getLongitude();
        prev_lat = prev_loc.getLatitude();
        lon = location.getLongitude();
        lat = location.getLatitude();
        delta = Math.sqrt(Math.pow(prev_lon - lon, 2) + Math.pow(prev_lat - lat, 2));
        if (delta < 0.1) {
            return;
        }
        prev_loc = location;
        final OAuth10aService service = new ServiceBuilder()
                .apiKey(consumerKey)
                .apiSecret(consumerSecret)
                .build(YelpApi.instance());
        final OAuthRequest request = new OAuthRequest(Verb.GET,
                "https://api.yelp.com/v2/search", service);
        request.addParameter("term", "food");
        request.addParameter("ll", String.format("%s,%s",
                String.valueOf(lat), String.valueOf(lon)));
        new AsyncTask<OAuthRequest, String, String>() {

            @Override
            protected String doInBackground(OAuthRequest... params) {
                OAuth1AccessToken auth1AccessToken = new OAuth1AccessToken(token, tokenSecret);
                service.signRequest(auth1AccessToken, request);
//                Response response = request.send();
//                String body = response.getBody();
                String body = "{}";
                try {
                    JSONObject obj = new JSONObject(body);
                    Log.d("Food", "received and parsed the Yelp response");
                    // TODO do something with the result. Only dummy code here
                    Intent intent = new Intent(getApplicationContext(), MobileToWatchService.class);
                    intent.putExtra("service", MobileToWatchService.FOOD_NOTIFICATION);
                    intent.putExtra("avoid", "raw fish");
                    Log.d("Food", "starting notification");
                    startService(intent);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                return null;
            }

        }.execute();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        LocationServices.FusedLocationApi.removeLocationUpdates(mGoogleClient, this);
    }

    private GoogleApiClient mGoogleClient;
    private boolean mRequestingLocation = true;
    private String consumerKey = "8fE5WCe1AXDodBfIOUNOeg";
    private String consumerSecret = "TAVonzH-IxmkXtJLuKel38jGHnY";
    private String token = "X9wD4FTBMfeqP_UJiD0KpDuIkB9yUX3G";
    private String tokenSecret = "7BAUpUB-PVjwUHhneWoSeoYPKWY";
    private Location prev_loc = null;
}
