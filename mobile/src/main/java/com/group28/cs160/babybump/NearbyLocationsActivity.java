package com.group28.cs160.babybump;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.Window;

public class NearbyLocationsActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_nearby_locations);
    }

    public void onImageClick(View v) {
        Intent intent = new Intent(getBaseContext(), DetailedLocationActivity.class);
        startActivity(intent);
    }
}
