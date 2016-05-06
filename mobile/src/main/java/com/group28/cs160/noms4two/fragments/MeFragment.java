package com.group28.cs160.noms4two.fragments;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.group28.cs160.noms4two.HistoryActivity;
import com.group28.cs160.noms4two.LoginScreenActivity;
import com.group28.cs160.noms4two.MainActivity;
import com.group28.cs160.noms4two.R;
import com.group28.cs160.noms4two.models.UserInfo;


public class MeFragment extends Fragment {

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup parent,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_me, parent, false);

        // Get Account Info
        userInfo = new UserInfo(getContext());
        TextView name = (TextView) rootView.findViewById(R.id.username);
        name.setText(userInfo.getUserName());

        // Get timeline
        int[] timeline = userInfo.getTimeline();
        Log.d("ME Fragment", "timeline: " + timeline[2] + " days, " + timeline[1] + " weeks, " + timeline[0] + " months");

        TextView timelineView = (TextView) rootView.findViewById(R.id.timeline);
        timelineView.setText("Only " + timeline[0] + " months, " + timeline[1] + " weeks, " + timeline[2] + " days left until my Baby is born...");

        // History Button
        Button histBttn = (Button) rootView.findViewById(R.id.history);
        histBttn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent historyIntent = new Intent(getContext(), HistoryActivity.class);
                historyIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                Log.d("HistoryActivity", "Starting application.");
                startActivity(historyIntent);
            }
        });

        // Log Out Button
        Button logoutBttn = (Button) rootView.findViewById(R.id.logout);
        logoutBttn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Delete login info first.
                userInfo.logout();
                // Also delete all history.
                ((MainActivity) getActivity()).nutrientsTracker.reset();
                // Send to login screen.
                Intent logoutIntent = new Intent(getContext(), LoginScreenActivity.class);
                logoutIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(logoutIntent);
            }
        });

        return rootView;
    }

    private UserInfo userInfo;
}
