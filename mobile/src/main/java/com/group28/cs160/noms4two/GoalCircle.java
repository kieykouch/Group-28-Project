package com.group28.cs160.noms4two;

import android.content.Context;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;

import com.group28.cs160.shared.CenteredImageFragmentv4;
import com.group28.cs160.shared.NutritionFacts;

/**
 * Created by eviltwin on 4/28/16.
 */
public class GoalCircle {
    public static CenteredImageFragmentv4 createGoalCircle(Context context, final MainActivity activity, NutritionFacts goal, NutritionFacts info, final int position) {
        String description;
        double value, goalValue;
        int border, borderHighlight;
        int iconRes;
        switch (position) {
            case -1:
                iconRes = R.drawable.calories;
                value = info.calories;
                goalValue = info.calories;
                description = "Calories";
                border = Color.parseColor("#FFB267");
                borderHighlight = Color.parseColor("#CC4E02");
                break;
            case 0:
                iconRes = R.drawable.protein;
                value = info.protein;
                goalValue = info.protein;
                description = "Protein";
                border = Color.parseColor("#4F751C");
                borderHighlight = Color.parseColor("#C0FF6C");
                break;
            case 1:
                iconRes = R.drawable.calcium;
                value = info.calcium;
                goalValue = info.calcium;
                description = "Calcium";
                border = Color.parseColor("#4F751C");
                borderHighlight = Color.parseColor("#C0FF6C");
                break;
            case 2:
                iconRes = R.drawable.fiber;
                value = info.fiber;
                goalValue = info.fiber;
                description = "Fiber";
                border = Color.parseColor("#4F751C");
                borderHighlight = Color.parseColor("#C0FF6C");
                break;
            case 3:
                iconRes = R.drawable.iron;
                value = info.iron;
                goalValue = info.iron;
                description = "Iron";
                border = Color.parseColor("#4F751C");
                borderHighlight = Color.parseColor("#C0FF6C");
                break;
            case 4:
                iconRes = R.drawable.potassium;
                value = info.potassium;
                goalValue = info.potassium;
                description = "Potassium";
                border = Color.parseColor("#4F751C");
                borderHighlight = Color.parseColor("#C0FF6C");
                break;
            default:
                iconRes = R.drawable.calories;
                value = 0;
                goalValue = 100;
                description = "Not Found";
                border = Color.parseColor("#4F751C");
                borderHighlight = Color.parseColor("#C0FF6C");
                break;
        }
        float angle = (float) (value / goalValue * 360);
        View.OnClickListener onClick = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DetailedNutritionFragment f = new DetailedNutritionFragment();
                Bundle args = new Bundle();
                args.putInt("position", position);
                f.setArguments(args);

                activity.replaceFragment(f);
            }
        };
        CenteredImageFragmentv4 fragment = new CenteredImageFragmentv4();
        fragment.setImage(iconRes);
        fragment.setDescription(description);
        fragment.setAngle(angle);
        fragment.setColor(border, borderHighlight);
        fragment.setOnClickListener(onClick);
        return fragment;
    }

}
