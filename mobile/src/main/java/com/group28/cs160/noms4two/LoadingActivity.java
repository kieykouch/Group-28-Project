package com.group28.cs160.noms4two;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;

import com.group28.cs160.shared.NutritionFacts;

import java.io.FileInputStream;
import java.io.ObjectInputStream;
import java.util.HashMap;
import java.util.Map;
import android.content.Context;

import org.apache.commons.lang3.ObjectUtils;

/* This activity represents our brand.
 * It shows up for a couple of seconds when the app is launched. */
public class LoadingActivity extends AppCompatActivity {

    private static final String LOGIN_FILE = "LOGIN_FILE";
    public int val;
    Map <String, String> food_logged;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_loading);
        final Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            public void run() throws NullPointerException {
                    LoginInfo loginInfo = new LoginInfo(getBaseContext());
                    if (!loginInfo.userLoggedIn()) {
                        Intent intentOne = new Intent(LoadingActivity.this, LoginScreen.class);
                        intentOne.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                        Log.d("LoadingActivity", "Starting application.");
                        startActivity(intentOne);
                    }
                    else {
                        Intent intent = new Intent(LoadingActivity.this, MainActivity.class);
                        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                        Log.d("LoadingActivity", "Starting application.");
                        startActivity(intent);

                    }



            }
        }, 3000);
    }
}
