package com.group28.cs160.babybump;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

public class WeightFragment extends Fragment {

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup parent,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View rootView = inflater.inflate(R.layout.fragment_weight, parent, false);
        return rootView;
    }

    public void onImageClick(View v) {
//        Intent intent = new Intent(getBaseContext(), WeightStatsActivity.class);
//        startActivity(intent);
    }
}
