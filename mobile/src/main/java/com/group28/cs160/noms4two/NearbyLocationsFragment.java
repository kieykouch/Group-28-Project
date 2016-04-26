package com.group28.cs160.noms4two;

import android.Manifest;
import android.content.pm.PackageManager;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationManager;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GoogleApiAvailability;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;

import java.util.List;

public class NearbyLocationsFragment extends Fragment implements OnMapReadyCallback {

    private GoogleMap mMap;

    int PLAY_SERVICES_RESOLUTION_REQUEST = 9000;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup parent,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View rootView = inflater.inflate(R.layout.fragment_nearby_locations, parent, false);
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        // Nested Fragments need child fragment manager.
        SupportMapFragment mapFragment = (SupportMapFragment) getActivity().getSupportFragmentManager()
                .findFragmentById(R.id.fragment_container).getChildFragmentManager().findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

        return rootView;
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
            getActivity().finish();
        }

        // Default to current GPS position.
        // Get away with not checking this.
        Location bestLocation = null;
        if (getActivity().checkPermission(Manifest.permission.ACCESS_FINE_LOCATION, android.os.Process.myPid(), android.os.Process.myUid()) == PackageManager.PERMISSION_GRANTED) {
            mMap.setMyLocationEnabled(true);
            LocationManager locationManager = (LocationManager) getActivity().getSystemService(getContext().LOCATION_SERVICE);
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

        /*LatLng berkeley = new LatLng(37.87, -122.27);
        googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(berkeley, 14.0f));*/

        mMap.setOnCameraChangeListener(new GoogleMap.OnCameraChangeListener() {
            @Override
            public void onCameraChange(CameraPosition cameraPosition) {
                LatLng newCenter = mMap.getCameraPosition().target;
            }
        });

        List<Place> findPlaces = PlacesService.findPlaces(latLng.latitude, latLng.longitude, "hospital");
        for (Place p : findPlaces) {
            // Log.d("Nearby Locations", p.toString());
            p.addMarker(mMap);
        }
    }

    private boolean isGooglePlayServicesAvailable() {
        GoogleApiAvailability googleAPI = GoogleApiAvailability.getInstance();
        int result = googleAPI.isGooglePlayServicesAvailable(getContext());
        if(result != ConnectionResult.SUCCESS) {
            if(googleAPI.isUserResolvableError(result)) {
                googleAPI.getErrorDialog(getActivity(), result,
                        PLAY_SERVICES_RESOLUTION_REQUEST).show();
            }
            return false;
        }
        return true;
    }
}
