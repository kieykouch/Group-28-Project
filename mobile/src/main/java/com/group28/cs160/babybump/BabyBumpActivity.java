package com.group28.cs160.babybump;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.LinearLayout;

// This Activity creates the title bar and the menu bar.
// All other activities in the app should extend this one.
// see activity_main.xml for how the xml file should be created.
public class BabyBumpActivity extends AppCompatActivity {

    // TODO(prad): Give Credit for noun project resources.
    // Scale by Madeleine Bennett from the Noun Project.
    // heartbeat by Creative Stall from the Noun Project.
    // House by Oliviu Stoian from the Noun Project.
    // Location by Sam Vermette, CA.
    // Calendar by Zlatko Najdenovski from the Noun Project.

    public void setupAppBar() {
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
    }

    public void setupNavBar() {
        // Add Navigation bar at the bottom of every activity.
        LinearLayout navigationbar = (LinearLayout) findViewById(R.id.navigationbar);
    }

    public void onNavigationClick(View v) {
        Intent intent = new Intent();
        switch (v.getId()) {
            case R.id.calendar:
                intent = new Intent(getBaseContext(), CalendarActivity.class);
                break;
            case R.id.nearby:
                intent = new Intent(getBaseContext(), NearbyLocationsActivity.class);
                break;
            case R.id.home:
                intent = new Intent(getBaseContext(), MainActivity.class);
                break;
            case R.id.heartrate:
                intent = new Intent(getBaseContext(), HeartRateActivity.class);
                break;
            case R.id.weight:
                intent = new Intent(getBaseContext(), WeightActivity.class);
                break;
        }
        startActivity(intent);
    }

    // TODO(prad): Support settings page with colors.
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
