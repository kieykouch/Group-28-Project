package com.group28.cs160.noms4two;

import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.CheckBox;
import android.widget.EditText;

import com.group28.cs160.shared.NutritionFacts;

import java.io.FileOutputStream;
import java.io.ObjectOutputStream;
import java.util.HashMap;
import java.util.Map;
import android.widget.ArrayAdapter;
import android.widget.Spinner;

public class LoginScreen extends AppCompatActivity {

    private static final String LOGIN_FILE = "LOGIN_FILE";
    public String pheebs;

    Map<String, String> account_info;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login_screen);

        //
    }


    public void onCheckboxClicked(View view) {

        CheckBox checkbox2 = (CheckBox) findViewById(R.id.checkbox2);
        CheckBox checkbox1 = (CheckBox) findViewById(R.id.checkbox1);
        switch(view.getId()) {

            case R.id.checkbox1:

                checkbox2.setChecked(false);
                pheebs = "N";

                break;

            case R.id.checkbox2:

                checkbox1.setChecked(false);
                pheebs = "Y";

                break;

        }
    }


    public void sendNextScreen2 (View v) {


        Map<String, String> account_info = new HashMap<String, String>();
        EditText textOne = (EditText) findViewById(R.id.editText0);
        final String username = textOne.getText().toString();

        EditText textTwo = (EditText) findViewById(R.id.editText1);
        final String date = textTwo.getText().toString();

        EditText textThree = (EditText)findViewById(R.id.editText2);
        final String weight = textThree.getText().toString();


        account_info.put("username", username);
        account_info.put("date", date);
        account_info.put("weight", weight);
        account_info.put("expecting", pheebs);
        // hi


    try {
        System.out.println(LOGIN_FILE);
       FileOutputStream fileStream = getBaseContext().openFileOutput(LOGIN_FILE, Context.MODE_PRIVATE);
        ObjectOutputStream objectStream = new ObjectOutputStream(fileStream);
        objectStream.writeObject(account_info);
        objectStream.close();
        fileStream.close();
    } catch (Exception e) {
        Log.d("yooooo", "Exception writing to file: " + e.toString());
    }
        Intent sendIntent4 = new Intent(LoginScreen.this, MainActivity.class);
       // sendIntent4.putExtra("LOC_NAME", url);
        startActivity(sendIntent4);


    }



    
}

