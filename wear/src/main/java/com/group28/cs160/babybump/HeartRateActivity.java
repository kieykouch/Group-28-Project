package com.group28.cs160.babybump;

import android.content.Intent;
import android.os.Bundle;
import android.support.wearable.activity.WearableActivity;
import android.support.wearable.view.BoxInsetLayout;
import android.support.wearable.view.GridViewPager;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class HeartRateActivity extends WearableActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_heart_rate);
        setAmbientEnabled();
        int resource = getIntent().getIntExtra("resources", -1);
        if (resource != -1) {
            ImageView imageView = (ImageView) findViewById(R.id.heart_image);
            imageView.setImageResource(resource);
            return;
        }
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    Thread.sleep(3000);

                    Intent intent = new Intent(getApplicationContext(), WatchToPhoneService.class);
                    intent.putExtra(WatchToPhoneService.HEART_RATE, "86");
                    startService(intent);

                    Intent activityIntent = new Intent(getApplicationContext(), HeartRateActivity.class);
                    activityIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
                    activityIntent.putExtra("resources", R.drawable.heart_rate_recorded_fake);
                    startActivity(activityIntent);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }).start();

//        GridViewPager pager = (GridViewPager) findViewById(R.id.pager);
//        pager.setAdapter(new Events(this, getFragmentManager()));
    }
}
