package com.group28.cs160.babybump;

import android.os.Bundle;
import android.support.annotation.IdRes;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;

import com.roughike.bottombar.BottomBar;
import com.roughike.bottombar.OnMenuTabClickListener;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        mFragmentManager = getSupportFragmentManager();

        mBottomBar = BottomBar.attach(this, savedInstanceState);
        mBottomBar.useFixedMode();
        mBottomBar.setItemsFromMenu(R.menu.bottombar_menu, new OnMenuTabClickListener() {
            @Override
            public void onMenuTabSelected(@IdRes int menuItemId) {
                FragmentTransaction transaction = mFragmentManager.beginTransaction();
                switch (menuItemId) {
                    case R.id.calendar_icon:
                        transaction.replace(R.id.fragment_container, new CalendarFragment());
                        break;
                    case R.id.weight_icon:
                        transaction.replace(R.id.fragment_container, new WeightFragment());
                        break;
                    case R.id.home_icon:
                        break;
                    case R.id.heartrate_icon:
                        transaction.replace(R.id.fragment_container, new HeartRateFragment());
                        break;
                    case R.id.nearby_icon:
                        transaction.replace(R.id.fragment_container, new NearbyLocationsFragment());
                        break;
                }
                transaction.addToBackStack(null);
                transaction.commit();
                mFragmentManager.executePendingTransactions();
            }

            @Override
            public void onMenuTabReSelected(@IdRes int menuItemId) {

            }
        });
        mBottomBar.mapColorForTab(0, "#7B1FA2");
        mBottomBar.mapColorForTab(1, "#7B1FA2");
        mBottomBar.mapColorForTab(2, "#FF5252");
        mBottomBar.mapColorForTab(3, "#7B1FA2");
        mBottomBar.mapColorForTab(4, "#7B1FA2");
    }

    private BottomBar mBottomBar;
    private FragmentManager mFragmentManager;
}
