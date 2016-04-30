package com.group28.cs160.noms4two;

import android.app.Fragment;
import android.app.FragmentManager;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.support.wearable.view.FragmentGridPagerAdapter;
import android.util.Log;
import android.view.View;

import com.group28.cs160.shared.CenteredImageFragment;
import com.group28.cs160.shared.NutritionFacts;

import java.util.ArrayList;

/**
 * Created by eviltwin on 4/20/16.
 */
public class DailyInfo extends FragmentGridPagerAdapter {

    private ArrayList<Fragment> fragments = new ArrayList<>();

    public DailyInfo(FragmentManager fm, NutritionFacts goals, NutritionFacts info) {
        super(fm);
        fragments.add(createGoalCircle(goals, info, NutritionFacts.Nutrient.CALORIES));
        fragments.add(createGoalCircle(goals, info, NutritionFacts.Nutrient.CALCIUM));
        fragments.add(createGoalCircle(goals, info, NutritionFacts.Nutrient.PROTEIN));
        fragments.add(createGoalCircle(goals, info, NutritionFacts.Nutrient.IRON));
        fragments.add(createGoalCircle(goals, info, NutritionFacts.Nutrient.FIBER));
        fragments.add(createGoalCircle(goals, info, NutritionFacts.Nutrient.POTASSIUM));
        fragments.add(createGoalCircle(goals, info, NutritionFacts.Nutrient.VITAMINC));
    }

    // Override methods in FragmentGridPagerAdapter
    // Obtain the UI fragment at the specified position
    @Override
    public Fragment getFragment(int row, int col) {
        return fragments.get(row);
    }

    // Obtain the background image for the specific page
    @Override
    public Drawable getBackgroundForPage(int row, int col) {
        return new ColorDrawable(Color.TRANSPARENT);
    }

    // Obtain the number of pages (vertical)
    @Override
    public int getRowCount() {
        return fragments.size();
    }

    // Obtain the number of pages (horizontal)
    @Override
    public int getColumnCount(int rowNum) {
        return 1;
    }


    public static CenteredImageFragment createGoalCircle(NutritionFacts goal, NutritionFacts info, final NutritionFacts.Nutrient nutrient) {
        CenteredImageFragment fragment = new CenteredImageFragment();

        float angle = (float) (info.getAmount(nutrient) / goal.getAmount(nutrient) * 360);
        fragment.setAngle(angle);

        View.OnClickListener onClick = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // TODO(prad): Fix this.
                Log.d("Event", "Recorded click.");
                Intent sendIntent = new Intent(v.getContext().getApplicationContext(), WatchToMobileService.class);
                sendIntent.putExtra(WatchToMobileService.NUTRIENT, nutrient.toString());
                v.getContext().startService(sendIntent);

            }
        };
        fragment.setOnClickListener(onClick);

        fragment.setImage(NutritionFacts.nutrientToResource(nutrient));

        fragment.setDescription(NutritionFacts.nutrientToString(nutrient));

        fragment.setColor(NutritionFacts.nutrientToRingColor(nutrient), NutritionFacts.nutrientToColor(nutrient));

        return fragment;
    }

}
