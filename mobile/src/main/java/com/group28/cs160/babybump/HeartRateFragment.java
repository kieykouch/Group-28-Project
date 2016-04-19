package com.group28.cs160.babybump;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

public class HeartRateFragment extends Fragment {

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup parent,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View rootView = inflater.inflate(R.layout.fragment_heart_rate, parent, false);
        return rootView;
    }

    public void onImageClick(View v) {
//        Intent intent = new Intent(getContext(), HeartRateStatsActivity.class);
//        startActivity(intent);
        FragmentManager manager = ((AppCompatActivity) getActivity()).getSupportFragmentManager();
//        manager.beginTransaction()
    }
}
