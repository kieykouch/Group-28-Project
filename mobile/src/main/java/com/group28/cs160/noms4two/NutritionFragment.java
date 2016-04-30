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

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup parent,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_nutrition, parent, false);

        // Add the cards for individual nutrients.
        NutritionFacts dailyTotals = ((MainActivity) getActivity()).nutrientsTracker.getNutritionToday();
        NutritionFacts dailyGoals = ((MainActivity) getActivity()).nutrientsTracker.getDailyGoals();

        MainActivity mainActivity = new MainActivity();

        replaceFragment(createGoalCircle(getContext(), mainActivity, dailyGoals, dailyTotals, NutritionFacts.Nutrient.CALORIES), R.id.caloriesCircle);

        replaceFragment(createGoalCircle(getContext(), mainActivity, dailyGoals, dailyTotals, NutritionFacts.Nutrient.PROTEIN), R.id.proteinCircle);

        replaceFragment(createGoalCircle(getContext(), mainActivity, dailyGoals, dailyTotals, NutritionFacts.Nutrient.CALCIUM), R.id.calciumCircle);

        replaceFragment(createGoalCircle(getContext(), mainActivity, dailyGoals, dailyTotals, NutritionFacts.Nutrient.FIBER), R.id.fiberCircle);

        replaceFragment(createGoalCircle(getContext(), mainActivity, dailyGoals, dailyTotals, NutritionFacts.Nutrient.IRON), R.id.ironCircle);

        replaceFragment(createGoalCircle(getContext(), mainActivity, dailyGoals, dailyTotals, NutritionFacts.Nutrient.POTASSIUM), R.id.potassiumCircle);

        replaceFragment(createGoalCircle(getContext(), mainActivity, dailyGoals, dailyTotals, NutritionFacts.Nutrient.VITAMINC), R.id.potassiumCircle);

        return rootView;
    }

    public void replaceFragment(Fragment newFragment, int fragment_container) {
        // TODO(prad): The highlighted item in the bottomBar should also change on reversing a transaction.
        FragmentManager fragmentManager = getFragmentManager();
        FragmentTransaction transaction = fragmentManager.beginTransaction();
        transaction.replace(fragment_container, newFragment);
        transaction.commit();
    }

    public static CenteredImageFragmentv4 createGoalCircle(final Context context, final MainActivity activity, NutritionFacts goal, NutritionFacts info, final NutritionFacts.Nutrient nutrient) {
        CenteredImageFragmentv4 fragment = new CenteredImageFragmentv4();

        float angle = (float) (info.getAmount(nutrient) / goal.getAmount(nutrient) * 360);
        fragment.setAngle(angle);

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
