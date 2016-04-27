package com.group28.cs160.noms4two;

import android.os.Bundle;
import android.support.annotation.IdRes;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.content.ContextCompat;
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
        mBottomBar.mapColorForTab(0, ContextCompat.getColor(this, R.color.bottombar));
        mBottomBar.mapColorForTab(1, ContextCompat.getColor(this, R.color.bottombar));
        mBottomBar.mapColorForTab(2, ContextCompat.getColor(this, R.color.bottombar));
        mBottomBar.mapColorForTab(3, ContextCompat.getColor(this, R.color.bottombar));
    }

    public void replaceFragment(Fragment newFragment) {
        // TODO(prad): The highlighted item in the BottomBar should also change on reversing a transaction.
        FragmentTransaction transaction = mFragmentManager.beginTransaction();
        transaction.replace(R.id.fragment_container, newFragment);
        transaction.addToBackStack(null);
        transaction.commit();
        mFragmentManager.executePendingTransactions();
    }

    private BottomBar mBottomBar;
    private FragmentManager mFragmentManager;
}
