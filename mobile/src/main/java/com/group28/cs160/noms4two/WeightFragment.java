package com.group28.cs160.noms4two;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;

public class WeightFragment extends Fragment {

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup parent,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View rootView = inflater.inflate(R.layout.fragment_weight, parent, false);

        ImageButton camBt = (ImageButton)rootView.findViewById(R.id.weight_fake);
        camBt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ((MainActivity) getActivity()).replaceFragment(new HeartRateStatsFragment());
            }
        });

        return rootView;
    }
}
