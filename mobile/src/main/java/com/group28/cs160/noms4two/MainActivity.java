package com.group28.cs160.noms4two;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.annotation.IdRes;
import android.support.design.widget.Snackbar;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;

import com.roughike.bottombar.BottomBar;
import com.roughike.bottombar.OnMenuTabClickListener;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        fragmentManager = getSupportFragmentManager();

        bottomBar = BottomBar.attach(this, savedInstanceState);
        bottomBar.useFixedMode();
        bottomBar.setItemsFromMenu(R.menu.bottombar_menu, new OnMenuTabClickListener() {
            @Override
            public void onMenuTabSelected(@IdRes int menuItemId) {
                switch (menuItemId) {
                    case R.id.nutrition_icon:
                        replaceFragment(new NutritionFragment());
                        break;
                    case R.id.barcode_icon:
                        replaceFragment(barcodeFragment);
                        return;
                    case R.id.search_icon:
                        replaceFragment(new SearchFragment());
                        break;
                    case R.id.me_icon:
                        replaceFragment(new MeFragment());
                        break;
                    default:
                        break;
                }
            }

            @Override
            public void onMenuTabReSelected(@IdRes int menuItemId) {
            }
        });
        bottomBar.mapColorForTab(0, ContextCompat.getColor(this, R.color.bottombar));
        bottomBar.mapColorForTab(1, ContextCompat.getColor(this, R.color.bottombar));
        bottomBar.mapColorForTab(2, ContextCompat.getColor(this, R.color.bottombar));
        bottomBar.mapColorForTab(3, ContextCompat.getColor(this, R.color.bottombar));

        // Load Nutrition Data from disk.
        nutrientsTracker = new NutrientsTracker(getBaseContext());
        // Add some fake nutrition data.
        fake_nutrition_data();
        Log.d("MainActivity", "Total Calories: " + nutrientsTracker.getNutritionToday().calories);
    }

    private void fake_nutrition_data() {
        // TODO(prad): Delete this before submitting.
        // Reset all nutrition data and add some fake stuff.
        nutrientsTracker.reset();
        FakeData.addFakeData(nutrientsTracker);
    }

    @Override
    protected void onDestroy() {
        nutrientsTracker.writeToFile();
        super.onDestroy();
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        nutrientsTracker.readFromFile();
        replaceFragment(new NutritionFragment());
        Snackbar.make(findViewById(R.id.container), "Added to My Summary", Snackbar.LENGTH_LONG)
                .setAction("Action", null).show();
        bottomBar.selectTabAtPosition(0, false);
    }

    public void replaceFragment(Fragment newFragment) {
        // TODO(prad): The highlighted item in the bottomBar should also change on reversing a transaction.
        FragmentTransaction transaction = fragmentManager.beginTransaction();
        transaction.replace(R.id.fragment_container, newFragment);
        transaction.addToBackStack(null);
        transaction.commit();
        fragmentManager.executePendingTransactions();
    }

    private BottomBar bottomBar;
    private FragmentManager fragmentManager;
    public NutrientsTracker nutrientsTracker;
    private final Fragment barcodeFragment = BarcodeFragment.newInstance();

}
