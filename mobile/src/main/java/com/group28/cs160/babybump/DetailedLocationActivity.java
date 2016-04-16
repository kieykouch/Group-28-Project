package com.group28.cs160.babybump;

import android.app.Activity;
import android.os.Bundle;
import android.view.Window;

public class DetailedLocationActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_detailed_location);
    }
}
