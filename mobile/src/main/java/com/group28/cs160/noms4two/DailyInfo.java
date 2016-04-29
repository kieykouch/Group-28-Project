package com.group28.cs160.noms4two;

import android.content.Context;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.LinearLayout;

import com.group28.cs160.shared.NutritionFacts;

/**
 * Created by eviltwin on 4/28/16.
 */
public class DailyInfo extends BaseAdapter {
    private Context context;
    private NutritionFacts goals;
    private NutritionFacts info;
    private FragmentManager fragmentManager;
    private MainActivity mainActivity;
    private LayoutInflater inflater;

    public DailyInfo(Context c, FragmentManager f, MainActivity m, LayoutInflater l, NutritionFacts g, NutritionFacts i) {
        context = c;
        goals = g;
        info = i;
        fragmentManager=f;
        mainActivity=m;
        inflater=l;
    }

    public int getCount() {
        return 5;
    }

    public Object getItem(int position) {
        return null;
    }

    public long getItemId(int position) {
        return position;
    }

    public View getView(int position, View convertView, ViewGroup parent) {
        if (convertView != null) {
            return convertView;
        } else {
            Log.d("DailyInfo", "creating view. " + position);
            convertView = new LinearLayout(context, null);
            // Add the calcium card.
            convertView.setId(1000 + position);
            replaceFragment(GoalCircle.createGoalCircle(context, mainActivity, goals, info, position), convertView.getId());
            return convertView;
        }
    }

    public void replaceFragment(Fragment newFragment, int fragment_container) {
        // TODO(prad): The highlighted item in the bottomBar should also change on reversing a transaction.
        FragmentTransaction transaction = fragmentManager.beginTransaction();
        transaction.replace(fragment_container, newFragment, "fragment" + fragment_container);
        transaction.commit();
    }
}