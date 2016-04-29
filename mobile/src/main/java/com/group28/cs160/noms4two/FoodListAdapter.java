package com.group28.cs160.noms4two;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Map;
import java.util.TreeMap;

/**
 * Created by phoebevu on 4/28/16.
 */
public class FoodListAdapter extends ArrayAdapter{
    private final Context context;
    private ArrayList<String> foods;
    public FoodListAdapter(Context context, Map<Long, NutritionFacts> foodlist) {
        super(context, R.layout.food_row);
        this.context = context;
        getFoods(foodlist);

    }

    public void getFoods(Map<Long, NutritionFacts> foodList) {
        // Sort the list in decreasing order
        Map<Long, NutritionFacts> sortedFoodList = new TreeMap<Long, NutritionFacts>(Collections.reverseOrder());
        sortedFoodList.putAll(foodList);

        this.foods = new ArrayList<>();
        for (Map.Entry<Long, NutritionFacts> entry : sortedFoodList.entrySet()) {
            foods.add(entry.getValue().name);
        }
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        LayoutInflater inflater = (LayoutInflater) context
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View rowView = inflater.inflate(R.layout.food_row, parent, false);

        String foodname = foods.get(position);
        TextView foodItem = (TextView) rowView.findViewById(R.id.food);
        CheckBox cb = (CheckBox) rowView.findViewById(R.id.checkBox);

        foodItem.setText(foodname);

        return  rowView;
    }

}
