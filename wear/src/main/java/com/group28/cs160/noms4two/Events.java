package com.group28.cs160.noms4two;

import android.app.Fragment;
import android.app.FragmentManager;
import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.support.wearable.view.FragmentGridPagerAdapter;
import android.util.Log;
import android.view.View;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by eviltwin on 4/20/16.
 */
public class Events extends FragmentGridPagerAdapter {
    private Context mContext;
    private List mRows;

    private ArrayList<Fragment> _fragment = new ArrayList<>();
    private ArrayList<Drawable> _backgrounds = new ArrayList<>();

    public Events(Context ctx, FragmentManager fm) {
        super(fm);
        mContext = ctx;
        ClickableCardFragment fragment;

        fragment = new ClickableCardFragment();
        fragment.setTitle("2 Month 3 Weeks");
        _fragment.add(fragment);
        _backgrounds.add(mContext.getResources().getDrawable(R.drawable.home_screen_fake));

        fragment = new ClickableCardFragment();
        fragment.setTitle("Events This Week");
        _fragment.add(fragment);
        _backgrounds.add(mContext.getResources().getDrawable(R.drawable.calendar_event));

        fragment = new ClickableCardFragment();
        fragment.setTitle("Ultrasound");
        fragment.setOnClickListener(new OnFragmentClick("Ultrasound"));
        _fragment.add(fragment);
        _backgrounds.add(mContext.getResources().getDrawable(R.drawable.ultrasound_event));
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
        return _fragment.get(col);
    }

    // Obtain the background image for the specific page
    @Override
    public Drawable getBackgroundForPage(int row, int col) {
        return _backgrounds.get(col);
    }

    // Obtain the number of pages (vertical)
    @Override
    public int getRowCount() {
        return 1;
    }

    // Obtain the number of pages (horizontal)
    @Override
    public int getColumnCount(int rowNum) {
        return _fragment.size();
    }
}
