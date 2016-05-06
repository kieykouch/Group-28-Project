package com.group28.cs160.noms4two;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;

import com.group28.cs160.noms4two.models.UserInfo;


/* This activity represents our brand.
 * It shows up for a couple of seconds when the app is launched. */
public class LoadingActivity extends AppCompatActivity {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_loading);
        final Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            public void run() throws NullPointerException {
                    UserInfo userInfo = new UserInfo(getBaseContext());
                    if (!userInfo.userLoggedIn()) {
                        Intent intentOne = new Intent(LoadingActivity.this, LoginScreenActivity.class);
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
        }, 2000);
    }
}
