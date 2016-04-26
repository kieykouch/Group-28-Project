package com.group28.cs160.noms4two;

import android.Manifest;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.annotation.IdRes;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;

import com.roughike.bottombar.BottomBar;
import com.roughike.bottombar.OnMenuTabClickListener;

public class MainActivity extends AppCompatActivity {

    public static final String EVENT_OBJECT="caller";
    public static final String HEART_RATE = "heart";

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
                switch (menuItemId) {
                    case R.id.calendar_icon:
                        replaceFragment(new CalendarFragment());
                        break;
                    case R.id.weight_icon:
                        replaceFragment(new CalendarFragment());
                        break;
                    case R.id.home_icon:
                        replaceFragment(new CalendarFragment());
                        break;
                    case R.id.heartrate_icon:
                        replaceFragment(new CalendarFragment());
                        break;
                    case R.id.nearby_icon:
                        replaceFragment(new CalendarFragment());
                        break;
                    default:
                        break;
                }
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

        mBottomBar.selectTabAtPosition(2, false);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

    public void replaceFragment(Fragment newFragment) {
        FragmentTransaction transaction = mFragmentManager.beginTransaction();
        transaction.replace(R.id.fragment_container, newFragment);
        transaction.addToBackStack(null);
        transaction.commit();
        mFragmentManager.executePendingTransactions();
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if (requestCode == 0) {
            if (permissions[0].equals(Manifest.permission.ACCESS_FINE_LOCATION) &&
                    grantResults[0] == PackageManager.PERMISSION_GRANTED) {
            }
        }
    }

    private BottomBar mBottomBar;
    private FragmentManager mFragmentManager;
}
