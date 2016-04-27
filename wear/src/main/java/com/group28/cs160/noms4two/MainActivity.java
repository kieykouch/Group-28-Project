package com.group28.cs160.noms4two;

import android.content.Intent;
import android.os.Bundle;
import android.support.wearable.activity.WearableActivity;
import android.support.wearable.view.GridViewPager;

public class MainActivity extends WearableActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        GridViewPager pager = (GridViewPager) findViewById(R.id.pager);
        pager.setAdapter(new DailyInfo(this, getFragmentManager()));
        startService(new Intent(this, WatchListenerService.class));
    }
}
