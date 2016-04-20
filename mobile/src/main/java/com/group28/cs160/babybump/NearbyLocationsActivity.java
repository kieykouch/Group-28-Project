package com.group28.cs160.babybump;

import android.Manifest;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Criteria;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationManager;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.util.Log;
import android.widget.EditText;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GoogleApiAvailability;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.GoogleMap.OnCameraChangeListener;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;

import java.util.List;
import java.util.Locale;

public class NearbyLocationsActivity extends FragmentActivity implements OnMapReadyCallback {

    private GoogleMap mMap;
    private EditText zipcode;
    private Geocoder gcoder;

    int PLAY_SERVICES_RESOLUTION_REQUEST = 9000;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_nearby_locations);
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

        gcoder = new Geocoder(this, Locale.getDefault());

        zipcode = (EditText) findViewById(R.id.zip_code);
    }

    /**
     * Manipulates the map once available.
     * This callback is triggered when the map is ready to be used.
     * This is where we can add markers or lines, add listeners or move the camera.
     */
    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        //show error dialog if GoolglePlayServices not available
        if (!isGooglePlayServicesAvailable()) {
            finish();
        }

        // Default to current GPS position.
        // Get away with not checking this.
        Location bestLocation = null;
        if (checkPermission(Manifest.permission.ACCESS_FINE_LOCATION, android.os.Process.myPid(), android.os.Process.myUid()) == PackageManager.PERMISSION_GRANTED) {
            mMap.setMyLocationEnabled(true);
            LocationManager locationManager = (LocationManager) getSystemService(LOCATION_SERVICE);
            Criteria criteria = new Criteria();
            bestLocation = locationManager.getLastKnownLocation(locationManager.getBestProvider(criteria, true));
        }
        // Direct to berkeley by default.
        LatLng latLng = new LatLng(37.87, -122.27);
        if (bestLocation != null) {
            Log.d("MapsActivity", "Using GPS location");
            latLng = new LatLng(bestLocation.getLatitude(), bestLocation.getLongitude());
        }

        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(latLng, 14.0f));
        setZipcode(latLng);

        /*LatLng berkeley = new LatLng(37.87, -122.27);
        googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(berkeley, 14.0f));
        setZipcode(berkeley);*/

        mMap.setOnCameraChangeListener(new OnCameraChangeListener() {
            @Override
            public void onCameraChange(CameraPosition cameraPosition) {
                LatLng newCenter = mMap.getCameraPosition().target;
                setZipcode(newCenter);
            }
        });

        mMap.
    }

    private void setZipcode(LatLng location) {
        List<Address> addresses;
        try {
            addresses = gcoder.getFromLocation(location.latitude, location.longitude, 1);
            zipcode.setText(addresses.get(0).getPostalCode());
        } catch (Exception e) {
            // Do nothing.
            Log.d("MapsActivity", e.toString());
        }
    }

    private boolean isGooglePlayServicesAvailable() {
        GoogleApiAvailability googleAPI = GoogleApiAvailability.getInstance();
        int result = googleAPI.isGooglePlayServicesAvailable(this);
        if(result != ConnectionResult.SUCCESS) {
            if(googleAPI.isUserResolvableError(result)) {
                googleAPI.getErrorDialog(this, result,
                        PLAY_SERVICES_RESOLUTION_REQUEST).show();
            }
            return false;
        }
        return true;
    }
}
