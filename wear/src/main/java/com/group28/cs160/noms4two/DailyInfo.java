package com.group28.cs160.noms4two;

import android.app.Fragment;
import android.app.FragmentManager;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.support.wearable.view.FragmentGridPagerAdapter;

import com.group28.cs160.shared.NutritionFacts;

import java.util.ArrayList;

/**
 * Created by eviltwin on 4/20/16.
 */
public class DailyInfo extends FragmentGridPagerAdapter {
    private Context context;

    private ArrayList<Fragment> fragments = new ArrayList<>();

    public DailyInfo(Context ctx, FragmentManager fm, NutritionFacts goals, NutritionFacts info) {
        super(fm);
        context = ctx;
        fragments.add(GoalCircle.createGoalCircle(goals, info, -1));
        fragments.add(GoalCircle.createGoalCircle(goals, info, 0));
        fragments.add(GoalCircle.createGoalCircle(goals, info, 1));
        fragments.add(GoalCircle.createGoalCircle(goals, info, 2));
        fragments.add(GoalCircle.createGoalCircle(goals, info, 3));
        fragments.add(GoalCircle.createGoalCircle(goals, info, 4));
    }

    // Override methods in FragmentGridPagerAdapter
    // Obtain the UI fragment at the specified position
    @Override
    public Fragment getFragment(int row, int col) {
        return fragments.get(col);
    }

    // Obtain the background image for the specific page
    @Override
    public Drawable getBackgroundForPage(int row, int col) {
        return new ColorDrawable(Color.TRANSPARENT);
    }

    // Obtain the number of pages (vertical)
    @Override
    public int getRowCount() {
        return 1;
    }

    // Obtain the number of pages (horizontal)
    @Override
    public int getColumnCount(int rowNum) {
        return fragments.size();
    }
}
