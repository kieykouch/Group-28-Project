package com.group28.cs160.noms4two;

import android.content.Context;
import android.content.Intent;
import android.util.Log;

import com.group28.cs160.shared.NutritionFacts;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by hudaiftekhar on 4/29/16.
 */
public class LoginInfo {

    private static final String LOGIN_FILE = "LOGIN_FILE";
    Map<String, String> account_info;
    Context context;
    public String twins;
    public String dueDate;
    public String name;
    public String weight;

    public LoginInfo(Context context) {
        this.context = context;
        account_info = readFromFile();
        twins = account_info.get("expecting");
        dueDate = account_info.get("date");
        name = account_info.get("username");
        weight = account_info.get("weight");
    }

    public boolean userLoggedIn() {
        for (String file : context.fileList()) {
            Log.d("LoginInfo", "Found File " + file);
            if (file.equals(LOGIN_FILE)) return true;
        }
        return false;
    }



    private Map<String, String> readFromFile() {
        Map<String, String> map = new HashMap<String, String>();
        try {
            FileInputStream fileStream = context.openFileInput(LOGIN_FILE);
            ObjectInputStream objectStream = new ObjectInputStream(fileStream);
            map = (HashMap<String, String>) objectStream.readObject();
            objectStream.close();
            fileStream.close();
        } catch (Exception e) {
            Log.d("Not reading file", "oh oh");
        }
        return map;
    }

    public void logout() {

        context.deleteFile("LOGIN_FILE");
    }

}