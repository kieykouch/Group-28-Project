package com.group28.cs160.noms4two;

import android.app.Fragment;
import android.app.FragmentManager;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.support.wearable.view.FragmentGridPagerAdapter;
import android.util.Log;
import android.view.View;

import java.util.ArrayList;

/**
 * Created by eviltwin on 4/20/16.
 */
public class DailyInfo extends FragmentGridPagerAdapter {
    private Context context;

    private ArrayList<Fragment> fragments = new ArrayList<>();
    private ArrayList<Drawable> backgrounds = new ArrayList<Drawable>();

    public DailyInfo(Context ctx, FragmentManager fm) {
        super(fm);
        context = ctx;
        CenteredImageFragment fragment;

        fragment = new CenteredImageFragment();
        fragment.setImage(R.drawable.calories);
        fragment.setDescription("2 Month 3 Weeks");
        fragments.add(fragment);
        backgrounds.add(PercentageBitmap.getBackgroundForPage(35.0, Color.parseColor("#CC4E02")));

        fragment = new CenteredImageFragment();
        fragment.setImage(R.drawable.calcium);
        fragment.setDescription("DailyInfo This Week");
        fragments.add(fragment);
        backgrounds.add(PercentageBitmap.getBackgroundForPage(67.0, Color.parseColor("#4F751C")));

        fragment = new CenteredImageFragment();
        fragment.setImage(R.drawable.calories);
        fragment.setDescription("Ultrasound");
        //fragment.setOnClickListener(new OnFragmentClick("Ultrasound"));
        fragments.add(fragment);
        backgrounds.add(PercentageBitmap.getBackgroundForPage(50.0, Color.parseColor("#CC4E02")));
    }

    private class OnFragmentClick implements View.OnClickListener {
        String event;
        public OnFragmentClick(String event) {
            this.event = event;
        }
        @Override
        public void onClick(View v) {
            Log.d("Event", "Recorded click.");
            Intent sendIntent = new Intent(v.getContext().getApplicationContext(), WatchToMobileService.class);
            sendIntent.putExtra(WatchToMobileService.EVENT_OBJECT, this.event);
            v.getContext().startService(sendIntent);
        }
    };

    // Override methods in FragmentGridPagerAdapter
    // Obtain the UI fragment at the specified position
    @Override
    public Fragment getFragment(int row, int col) {
        return fragments.get(col);
    }

    // Obtain the background image for the specific page
    @Override
    public Drawable getBackgroundForPage(int row, int col) {
        return backgrounds.get(col);
    }

    // Obtain the number of pages (vertical)
    @Override
    public int getRowCount() {
        return 1;
    }

    // Obtain the number of pages (horizontal)
    @Override
    public int getColumnCount(int rowNum) {
        return fragments.size();
    }
}
