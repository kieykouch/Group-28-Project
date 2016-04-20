package com.group28.cs160.babybump;

/**
 * Created by eviltwin on 4/20/16.
 */
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

public class DetailedEventFragment extends android.support.v4.app.Fragment {

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup parent,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View rootView = inflater.inflate(R.layout.fragment_detailed_event, parent, false);
        return rootView;
    }

}
