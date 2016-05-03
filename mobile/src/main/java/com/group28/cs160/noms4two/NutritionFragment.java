package com.group28.cs160.noms4two;

import android.content.Context;
import android.content.Intent;
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

    public static String ANIMATE_DIFF = "com.group28.cs160.ANIMATE_DIFF";

    NutritionFacts dailyGoals, dailyTotals;
    boolean animateDiff = false;
    Context context;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup parent,
                             Bundle savedInstanceState) {
        if (getArguments() != null) {
            if (getArguments().containsKey(ANIMATE_DIFF)) {
                animateDiff = getArguments().getBoolean(ANIMATE_DIFF);
            }
        }
        View rootView = inflater.inflate(R.layout.fragment_nutrition, parent, false);

        // Add the cards for individual nutrients.
        dailyTotals = ((MainActivity) getActivity()).nutrientsTracker.getNutritionToday();
        dailyGoals = ((MainActivity) getActivity()).nutrientsTracker.getDailyGoals();

        context = getContext();

        replaceFragment(createGoalCircle(NutritionFacts.Nutrient.CALORIES), R.id.caloriesCircle);

        replaceFragment(createGoalCircle(NutritionFacts.Nutrient.PROTEIN), R.id.proteinCircle);

        replaceFragment(createGoalCircle(NutritionFacts.Nutrient.CALCIUM), R.id.calciumCircle);

        replaceFragment(createGoalCircle(NutritionFacts.Nutrient.FIBER), R.id.fiberCircle);

        replaceFragment(createGoalCircle(NutritionFacts.Nutrient.IRON), R.id.ironCircle);

        replaceFragment(createGoalCircle(NutritionFacts.Nutrient.POTASSIUM), R.id.potassiumCircle);

        replaceFragment(createGoalCircle(NutritionFacts.Nutrient.VITAMINC), R.id.potassiumCircle);

        return rootView;
    }

    public void replaceFragment(Fragment newFragment, int fragment_container) {
        FragmentManager fragmentManager = getFragmentManager();
        FragmentTransaction transaction = fragmentManager.beginTransaction();
        // Do not add fragment changes to back stack. Bottom bar is always visible.
        transaction.replace(fragment_container, newFragment);
        transaction.commit();
    }

    public CenteredImageFragmentv4 createGoalCircle(final NutritionFacts.Nutrient nutrient) {
        CenteredImageFragmentv4 fragment = new CenteredImageFragmentv4();

        float angle = (float) (dailyTotals.getAmount(nutrient) / dailyGoals.getAmount(nutrient) * 360);
        fragment.setAngle(angle);

        if (animateDiff) {
            NutrientsTracker tracker = new NutrientsTracker(context);
            float oldAngle = (float) ((dailyTotals.getAmount(nutrient) - tracker.getMostRecent().getAmount(nutrient)) / dailyGoals.getAmount(nutrient) * 360);
            fragment.setOldAngle(oldAngle);
        }

        View.OnClickListener onClick = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent chartIntent = new Intent(context, NutritionGraphActivity.class);
                chartIntent.putExtra(NutritionGraphActivity.NUTRIENT, nutrient.toString());
                chartIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                context.startActivity(chartIntent);
            }
        };
        fragment.setOnClickListener(onClick);

        fragment.setImage(NutritionFacts.nutrientToResource(nutrient));

        fragment.setDescription(NutritionFacts.nutrientToString(nutrient));

        fragment.setColor(NutritionFacts.nutrientToRingColor(nutrient), NutritionFacts.nutrientToColor(nutrient));

        return fragment;
    }
}
