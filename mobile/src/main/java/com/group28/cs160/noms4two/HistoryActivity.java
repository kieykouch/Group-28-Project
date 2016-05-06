package com.group28.cs160.noms4two;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;

import com.group28.cs160.shared.NutritionFacts;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Map;
import java.util.TreeMap;

public class HistoryActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_history);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        // Load Nutrition Data from disk.
        nutrientsTracker = new NutrientsTracker(getBaseContext());

        // Get last week's food.
        long now = System.currentTimeMillis() - 7*24*60*60*1000;
        Map<Long, NutritionFacts> foodList = nutrientsTracker.getRecent(now);

        // Sort food list
        Map<Long, NutritionFacts> sortedFoodList = new TreeMap<Long, NutritionFacts>(Collections.reverseOrder());
        sortedFoodList.putAll(foodList);

        // Get list of foods' names and timestamps
        ArrayList<Long> foodIds = new ArrayList<Long>();
        ArrayList<String> foodnames = new ArrayList<String>();
        for (Map.Entry<Long, NutritionFacts> entry : sortedFoodList.entrySet()) {
            foodIds.add(entry.getKey());
            foodnames.add(entry.getValue().name);
        }

        // Setting Food List View
        ListView foodListView = (ListView) findViewById(R.id.foodList);
        final FoodListAdapter foodListAdapter = new FoodListAdapter(this, foodIds, foodnames);
        assert foodListView != null;
        foodListView.setAdapter(foodListAdapter);

        // Clear History
        Button histClear = (Button) findViewById(R.id.historyClear);
        assert histClear != null;
        histClear.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                foodListAdapter.clear();
            }
        });
    }

    // Public and used in adapter.
    public NutrientsTracker nutrientsTracker;
}
