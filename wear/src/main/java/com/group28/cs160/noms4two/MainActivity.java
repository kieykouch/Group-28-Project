package com.group28.cs160.noms4two;

import android.content.Intent;
import android.os.Bundle;
import android.support.wearable.activity.WearableActivity;
import android.support.wearable.view.GridViewPager;

public class MainActivity extends WearableActivity {

//    public static final String INFO = "com.prad.cs160.represent.INFO";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
//        setAmbientEnabled();

        GridViewPager pager = (GridViewPager) findViewById(R.id.pager);
        pager.setAdapter(new Events(this, getFragmentManager()));
        startService(new Intent(this, WatchListenerService.class));
    }
}
