package com.group28.cs160.noms4two;

import android.os.Bundle;
import android.support.annotation.IdRes;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;

import com.group28.cs160.shared.NutritionFacts;
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

        bottomBar = bottomBar.attach(this, savedInstanceState);
        bottomBar.useFixedMode();
        bottomBar.setItemsFromMenu(R.menu.bottombar_menu, new OnMenuTabClickListener() {
            @Override
            public void onMenuTabSelected(@IdRes int menuItemId) {
                switch (menuItemId) {
                    case R.id.nutrition_icon:
                        replaceFragment(new NutritionFragment());
                        break;
                    case R.id.barcode_icon:
                        replaceFragment(new Fragment());
                        break;
                    case R.id.search_icon:
                        replaceFragment(new Fragment());
                        break;
                    case R.id.me_icon:
                        replaceFragment(new Fragment());
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
        nutrientsTracker = new NutrientsTracker(getBaseContext(), 1 /* trimester */);
        // Add some fake nutrition data.
        fake_nutrition_data();
        Log.d("MainActivity", "Total Calories: " + nutrientsTracker.getNutritionToday().calories);
    }

    private void fake_nutrition_data() {
        if (nutrientsTracker.getNutritionToday().calories == 0) {
            NutritionFacts pasta = new NutritionFacts("pasta", 600);
            nutrientsTracker.log(pasta);
        }
    }

    @Override
    protected void onDestroy() {
        nutrientsTracker.writeToFile();
        super.onDestroy();
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
}
