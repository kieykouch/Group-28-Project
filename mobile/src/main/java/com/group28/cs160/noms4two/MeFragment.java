package com.group28.cs160.noms4two;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;


public class MeFragment extends Fragment {

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup parent,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_me, parent, false);

        // History Button
        Button histBttn = (Button) rootView.findViewById(R.id.history);
        histBttn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ((MainActivity) getActivity()).replaceFragment(new HistoryFragment());
            }
        });

        // Log Out Button
        Button logoutBttn = (Button) rootView.findViewById(R.id.logout);
        logoutBttn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ((MainActivity) getActivity()).nutrientsTracker.clear();
                Intent intent = new Intent(getContext(), LoadingActivity.class);
                startActivity(intent);
            }
        });

        return rootView;
    }




    private Button logoutBttn;
    private FragmentManager fragmentManager;
}
