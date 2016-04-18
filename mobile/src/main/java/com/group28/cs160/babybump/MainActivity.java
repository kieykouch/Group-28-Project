package com.group28.cs160.babybump;

import android.os.Bundle;

public class MainActivity extends BabyBumpActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        setupAppBar();
    }
}
