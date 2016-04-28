package com.group28.cs160.noms4two;

import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.content.ContextCompat;
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

        // Add the calorie card.
        float calorieAngle = (float) (dailyTotals.calories / dailyGoals.calories * 360);
        replaceFragment(createGoalCircle(R.drawable.calories, calorieAngle, Color.parseColor("#4F751C"), Color.parseColor("#FFFFFF"), "Calories", true), R.id.caloriesCircle);

        // Add the calcium card.
        float calciumAngle = (float) (dailyTotals.calcium / dailyGoals.calcium * 360);
        replaceFragment(createGoalCircle(R.drawable.calcium, calorieAngle, Color.parseColor("#4F751C"), Color.parseColor("#FFFFFF"), "Calcium", false), R.id.calciumCircle);

        // Add the protein card.
        float proteinAngle = (float) (dailyTotals.protein / dailyGoals.protein * 360);
        replaceFragment(createGoalCircle(R.drawable.calories, proteinAngle, Color.parseColor("#4F751C"), Color.parseColor("#FFFFFF"), "Protein", false), R.id.proteinCircle);

        return rootView;
    }

    private CenteredImageFragmentv4 createGoalCircle(int iconRes, float angle, int borderHighlight, int border, String description, boolean large) {
        CenteredImageFragmentv4 fragment = new CenteredImageFragmentv4();
        fragment.setImage(ContextCompat.getDrawable(getContext(), iconRes));
        fragment.setDescription(description);
        fragment.setAngle(angle);
        fragment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ((MainActivity) getActivity()).replaceFragment(new DetailedNutritionFragment());
            }
        });
        return fragment;
    }

    public void replaceFragment(Fragment newFragment, int fragment_container) {
        // TODO(prad): The highlighted item in the bottomBar should also change on reversing a transaction.
        FragmentManager fragmentManager = getFragmentManager();
        FragmentTransaction transaction = fragmentManager.beginTransaction();
        transaction.replace(fragment_container, newFragment);
        transaction.commit();
    }

}
