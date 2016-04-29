package com.group28.cs160.noms4two;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.group28.cs160.shared.CenteredImageFragmentv4;
import com.group28.cs160.shared.NutritionFacts;

public class NutritionFragment extends Fragment {

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup parent,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_nutrition, parent, false);

        // Add the cards for individual nutrients.
        NutritionFacts dailyTotals = ((MainActivity) getActivity()).nutrientsTracker.getNutritionToday();
        NutritionFacts dailyGoals = ((MainActivity) getActivity()).nutrientsTracker.getDailyGoals();

        MainActivity mainActivity = new MainActivity();

        replaceFragment(createGoalCircle(getContext(), mainActivity, dailyGoals, dailyTotals, -1), R.id.caloriesCircle);

        replaceFragment(createGoalCircle(getContext(), mainActivity, dailyGoals, dailyTotals, 0), R.id.proteinCircle);

        replaceFragment(createGoalCircle(getContext(), mainActivity, dailyGoals, dailyTotals, 1), R.id.calciumCircle);

        replaceFragment(createGoalCircle(getContext(), mainActivity, dailyGoals, dailyTotals, 2), R.id.fiberCircle);

        replaceFragment(createGoalCircle(getContext(), mainActivity, dailyGoals, dailyTotals, 3), R.id.ironCircle);

        replaceFragment(createGoalCircle(getContext(), mainActivity, dailyGoals, dailyTotals, 4), R.id.potassiumCircle);

        return rootView;
    }

    public void replaceFragment(Fragment newFragment, int fragment_container) {
        // TODO(prad): The highlighted item in the bottomBar should also change on reversing a transaction.
        FragmentManager fragmentManager = getFragmentManager();
        FragmentTransaction transaction = fragmentManager.beginTransaction();
        transaction.replace(fragment_container, newFragment);
        transaction.commit();
    }

    public static CenteredImageFragmentv4 createGoalCircle(final Context context, final MainActivity activity, NutritionFacts goal, NutritionFacts info, final int position) {
        String description;
        double value, goalValue;
        int border, borderHighlight;
        int iconRes;
        switch (position) {
            case -1:
                iconRes = R.drawable.calories;
                value = info.calories;
                goalValue = goal.calories;
                description = "Calories";
                border = Color.parseColor("#FFB267");
                borderHighlight = Color.parseColor("#CC4E02");
                break;
            case 0:
                iconRes = R.drawable.protein;
                value = info.protein;
                goalValue = goal.protein;
                description = "Protein";
                border = Color.parseColor("#4F751C");
                borderHighlight = Color.parseColor("#C0FF6C");
                break;
            case 1:
                iconRes = R.drawable.calcium;
                value = info.calcium;
                goalValue = goal.calcium;
                description = "Calcium";
                border = Color.parseColor("#4F751C");
                borderHighlight = Color.parseColor("#C0FF6C");
                break;
            case 2:
                iconRes = R.drawable.fiber;
                value = info.fiber;
                goalValue = goal.fiber;
                description = "Fiber";
                border = Color.parseColor("#4F751C");
                borderHighlight = Color.parseColor("#C0FF6C");
                break;
            case 3:
                iconRes = R.drawable.iron;
                value = info.iron;
                goalValue = goal.iron;
                description = "Iron";
                border = Color.parseColor("#4F751C");
                borderHighlight = Color.parseColor("#C0FF6C");
                break;
            case 4:
                iconRes = R.drawable.potassium;
                value = info.potassium;
                goalValue = goal.potassium;
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
                Intent chartIntent = new Intent(context, HistoryActivity.class);
                chartIntent.putExtra(HistoryActivity.NUTRIENT, 1);
                context.startActivity(chartIntent);
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
