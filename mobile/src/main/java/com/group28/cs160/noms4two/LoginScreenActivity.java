package com.group28.cs160.noms4two;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.CheckBox;
import android.widget.EditText;

import com.group28.cs160.noms4two.models.UserInfo;

public class LoginScreenActivity extends AppCompatActivity {

    private static final String LOGIN_FILE = "LOGIN_FILE";
    public String twins;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login_screen);
    }


    public void onCheckboxClicked(View view) {

        CheckBox checkbox2 = (CheckBox) findViewById(R.id.checkbox2);
        CheckBox checkbox1 = (CheckBox) findViewById(R.id.checkbox1);
        switch(view.getId()) {
            case R.id.checkbox1:
                checkbox2.setChecked(false);
                twins = "true";
                break;
            case R.id.checkbox2:
                checkbox1.setChecked(false);
                twins = "false";
                break;
        }
    }


    public void completeLogin(View v) {
        EditText textOne = (EditText) findViewById(R.id.editText0);
        final String username = textOne.getText().toString();

        EditText textTwo = (EditText) findViewById(R.id.editText1);
        final String date = textTwo.getText().toString();

        EditText textThree = (EditText)findViewById(R.id.editText2);
        final String weight = textThree.getText().toString();

        UserInfo userInfo = new UserInfo(getBaseContext());
        userInfo.setUserName(username);
        userInfo.setDueDate(date);
        userInfo.setTwins(twins);
        userInfo.setWeight(weight);

        userInfo.writeToFile();
        Intent startApp = new Intent(LoginScreenActivity.this, MainActivity.class);
        startActivity(startApp);
    }
}

