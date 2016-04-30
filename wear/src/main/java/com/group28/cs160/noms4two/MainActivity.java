package com.group28.cs160.noms4two;

import android.content.Intent;
import android.os.Bundle;
import android.support.wearable.activity.WearableActivity;
import android.support.wearable.view.GridViewPager;
import android.util.Log;

import com.group28.cs160.shared.NutritionFacts;

public class MainActivity extends WearableActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        startService(new Intent(this, WatchListenerService.class));

        NutritionFacts goals = DiskNutritionFacts.readFromFile(getBaseContext(), DiskNutritionFacts.GOALS_FILE);
        NutritionFacts info = DiskNutritionFacts.readFromFile(getBaseContext(), DiskNutritionFacts.INFO_FILE);

        if (goals.calories != 0) {
            GridViewPager pager = (GridViewPager) findViewById(R.id.pager);
            pager.setAdapter(new DailyInfo(getFragmentManager(), goals, info));
        } else {
            // TODO(prad): Nice toast for error message.
            Log.d("MainActivity", "No nutrition info found.");
        }
    }
}
