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

import java.util.Collections;
import java.util.Map;
import java.util.TreeMap;


public class HistoryFragment extends Fragment {

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View rootView = inflater.inflate(R.layout.fragment_history, container, false);

        // Get today's food list
        long now = System.currentTimeMillis() - 2*24*60*60*1000;
        Log.d("HISTORY", "time "+ now);
        Map<Long, NutritionFacts> foodList = ((MainActivity) getActivity()).nutrientsTracker.getRecent(now);
        Log.d("HISTORY", "size: "+ foodList.size());

        // Setting Food List
        ListView foodListView = (ListView) rootView.findViewById(R.id.foodList);
        FoodListAdapter foodListAdapter = new FoodListAdapter(getContext(), foodList);
        foodListView.setAdapter(foodListAdapter);
        return rootView;
    }


}
