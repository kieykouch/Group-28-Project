package com.group28.cs160.noms4two;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.GridView;

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

        replaceFragment(GoalCircle.createGoalCircle(getContext(), mainActivity, dailyGoals, dailyTotals, -1), R.id.caloriesCircle);

        GridView gridview = (GridView) rootView.findViewById(R.id.gridview);
        gridview.setAdapter(new DailyInfo(getContext(), getFragmentManager(), (MainActivity) getActivity(), inflater, dailyGoals, dailyTotals));

        gridview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            public void onItemClick(AdapterView<?> parent, View v,
                                    int position, long id) {
                Log.d("NutritionFragment", "Received click.");
            }
        });

        gridview.setOnTouchListener(new View.OnTouchListener(){
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if(event.getAction() == MotionEvent.ACTION_MOVE){
                    return true;
                }
                return false;
            }

        });
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
