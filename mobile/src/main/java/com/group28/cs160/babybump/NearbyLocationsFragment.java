package com.group28.cs160.babybump;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;

public class NearbyLocationsFragment extends Fragment {

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup parent,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View rootView = inflater.inflate(R.layout.fragment_nearby_locations, parent, false);
        ImageButton camBt = (ImageButton)rootView.findViewById(R.id.nearby_locations_fake);
        camBt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ((MainActivity) getActivity()).replaceFragment(new HeartRateStatsFragment());
            }
        });

        return rootView;
    }
}
