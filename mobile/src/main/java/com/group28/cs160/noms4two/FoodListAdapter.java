package com.group28.cs160.noms4two;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.TextView;
import android.widget.Toast;

import com.group28.cs160.shared.NutritionFacts;


import java.util.ArrayList;
import java.util.Collections;
import java.util.Map;
import java.util.TreeMap;

/**
 * Created by phoebevu on 4/28/16.
 */
public class FoodListAdapter extends ArrayAdapter{
    private final Context context;
    private ArrayList<String> foodnames;
    private ArrayList<Long> foodIds;
    public FoodListAdapter(Context context, ArrayList<Long> foodIds, ArrayList<String> foodnames) {
        super(context, R.layout.food_row, foodIds);
        this.context = context;
        this.foodIds = foodIds;
        this.foodnames = foodnames;

    }


    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        LayoutInflater inflater = (LayoutInflater) context
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        if (convertView == null) {
            convertView = inflater.inflate(R.layout.food_row, parent, false);
        }

        String foodname = foodnames.get(position);
        final Long foodId = foodIds.get(position);
        TextView foodItem = (TextView) convertView.findViewById(R.id.food);
        Button delBttn = (Button) convertView.findViewById(R.id.delBttn);

        // Delete the food when button is clicked
        delBttn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                foodIds.remove(position);
                foodnames.remove(position);
                ((HistoryActivity) context).nutrientsTracker.delete(foodId);
                ((HistoryActivity) context).nutrientsTracker.writeToFile();
                notifyDataSetChanged();
            }
        });

        foodItem.setText(foodname);

        return  convertView;
    }

    public void clear() {
        foodIds.clear();
        foodnames.clear();
        notifyDataSetChanged();
    }

}
