package com.group28.cs160.noms4two;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import org.w3c.dom.Text;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.TimeZone;


public class MeFragment extends Fragment {

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup parent,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_me, parent, false);

        // Get Account Info
        loginInfo = new LoginInfo(getContext());
        TextView name = (TextView) rootView.findViewById(R.id.username);
        name.setText(loginInfo.name);

        // Get timeline
        Log.d("ACCOUNT", "due date: " + loginInfo.dueDate);
        int[] timeline = loginInfo.getTimeline();
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
                ((MainActivity) getActivity()).nutrientsTracker.clear();
                Intent logoutIntent = new Intent(getContext(), LoginScreen.class);
                logoutIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(logoutIntent);
            }
        });

        return rootView;
    }


    private LoginInfo loginInfo;
}
