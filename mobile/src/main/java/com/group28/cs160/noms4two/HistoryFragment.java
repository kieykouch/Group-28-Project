package com.group28.cs160.noms4two;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import com.group28.cs160.shared.NutritionFacts;


import java.util.ArrayList;
import java.util.Collections;
import java.util.Map;
import java.util.TreeMap;


public class HistoryFragment extends Fragment {

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View rootView = inflater.inflate(R.layout.fragment_history, container, false);

        // Get today's food list
        long now = System.currentTimeMillis() - 24*60*60*1000;
        Map<Long, NutritionFacts> foodList = ((MainActivity) getActivity()).nutrientsTracker.getRecent(now);

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
        ListView foodListView = (ListView) rootView.findViewById(R.id.foodList);
        FoodListAdapter foodListAdapter = new FoodListAdapter(getContext(), foodIds, foodnames);
        foodListView.setAdapter(foodListAdapter);
        return rootView;
    }


}
