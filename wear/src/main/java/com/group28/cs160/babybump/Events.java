package com.group28.cs160.babybump;

import android.app.Fragment;
import android.app.FragmentManager;
import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.support.wearable.view.FragmentGridPagerAdapter;
import android.util.Log;
import android.view.View;

import java.util.List;

/**
 * Created by eviltwin on 4/20/16.
 */
public class Events extends FragmentGridPagerAdapter {
    private Context mContext;
    private List mRows;

    public Events(Context ctx, FragmentManager fm) {
        super(fm);
        mContext = ctx;
    }

    private class OnFragmentClick implements View.OnClickListener {
        String event;
        public OnFragmentClick(String event) {
            this.event = event;
        }
        @Override
        public void onClick(View v) {
            Log.d("Event", "Recorded click.");
            Intent sendIntent = new Intent(v.getContext().getApplicationContext(), WatchToPhoneService.class);
            sendIntent.putExtra(WatchToPhoneService.EVENT_OBJECT, this.event);
            v.getContext().startService(sendIntent);
        }
    };

    // Override methods in FragmentGridPagerAdapter
    // Obtain the UI fragment at the specified position
    @Override
    public Fragment getFragment(int row, int col) {
        ClickableCardFragment fragment = new ClickableCardFragment();
        if (col == 0) fragment.setTitle("Events This Week");
        if (col == 1) {
            fragment.setTitle("Ultrasound");
            fragment.setOnClickListener(new OnFragmentClick("Ultrasound"));
        }
        return fragment;
    }

    // Obtain the background image for the specific page
    @Override
    public Drawable getBackgroundForPage(int row, int col) {
        if (col == 0) return mContext.getResources().getDrawable(R.drawable.calendar_event);
        else return mContext.getResources().getDrawable(R.drawable.ultrasound_event);
    }

    // Obtain the number of pages (vertical)
    @Override
    public int getRowCount() {
        return 1;
    }

    // Obtain the number of pages (horizontal)
    @Override
    public int getColumnCount(int rowNum) {
        return 2;
    }
}
