package com.group28.cs160.babybump;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;


/**

 */
public class HearRateRecordedFragment extends Fragment {

    @Override
    public View onCreateView(final LayoutInflater inflater, ViewGroup parent,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View rootView = inflater.inflate(R.layout.fragment_hear_rate_recorded, parent, false);

        ImageButton camBt = (ImageButton)rootView.findViewById(R.id.heartrate_fake);
        camBt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ((MainActivity) getActivity()).replaceFragment(new HeartRateStatsFragment());
            }
        });

        return rootView;

    }
}
