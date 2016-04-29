package com.group28.cs160.noms4two;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

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

        replaceFragment(GoalCircle.createGoalCircle(mainActivity, dailyGoals, dailyTotals, -1), R.id.caloriesCircle);

        replaceFragment(GoalCircle.createGoalCircle(mainActivity, dailyGoals, dailyTotals, 0), R.id.proteinCircle);

        replaceFragment(GoalCircle.createGoalCircle(mainActivity, dailyGoals, dailyTotals, 1), R.id.calciumCircle);

        replaceFragment(GoalCircle.createGoalCircle(mainActivity, dailyGoals, dailyTotals, 2), R.id.fiberCircle);

        replaceFragment(GoalCircle.createGoalCircle(mainActivity, dailyGoals, dailyTotals, 3), R.id.ironCircle);

        replaceFragment(GoalCircle.createGoalCircle(mainActivity, dailyGoals, dailyTotals, 4), R.id.potassiumCircle);

        return rootView;
    }

    public void replaceFragment(Fragment newFragment, int fragment_container) {
        // TODO(prad): The highlighted item in the bottomBar should also change on reversing a transaction.
        FragmentManager fragmentManager = getFragmentManager();
        FragmentTransaction transaction = fragmentManager.beginTransaction();
        transaction.replace(fragment_container, newFragment);
        transaction.commit();
    }

}
