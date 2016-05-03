package com.group28.cs160.noms4two;

import android.content.Context;
import android.util.Log;

import java.io.FileInputStream;
import java.io.ObjectInputStream;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;
import java.util.TimeZone;

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

    public int[] getTimeline() {
        DateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy", Locale.US);
        dateFormat.setTimeZone(TimeZone.getDefault());
        //get current date time with Date()
        Date date = new Date();
        String now = dateFormat.format(date);
        int duration = 0;
        try {
            Date currDate = dateFormat.parse(now);
            Date dDate = dateFormat.parse(dueDate);
            duration = daysBetween(currDate, dDate);
            Log.d("Me Fragment", "duration: " + duration);
        } catch (Exception e) {
            Log.d("Me Fragment:", "cannot parse date");
        }

        if (duration == 0) {
            return new int[] {0, 0, 0};
        } else if (duration < 7) {
            return new int[] {0, 0, duration};
        } else if (duration < 30) {
            return new int[] {0, duration / 7, duration % 7};
        } else {
            return new int[] {duration / 30, (duration % 30) / 7, (duration % 30) % 7};
        }
    }

    public static Calendar getDatePart(Date date){
        Calendar cal = Calendar.getInstance();       // get calendar instance
        cal.setTime(date);
        cal.set(Calendar.HOUR_OF_DAY, 0);            // set hour to midnight
        cal.set(Calendar.MINUTE, 0);                 // set minute in hour
        cal.set(Calendar.SECOND, 0);                 // set second in minute
        cal.set(Calendar.MILLISECOND, 0);            // set millisecond in second

        return cal;                                  // return the date part
    }

    public static int daysBetween(Date startDate, Date endDate) {
        Calendar sDate = getDatePart(startDate);
        Calendar eDate = getDatePart(endDate);

        int daysBetween = 0;
        while (sDate.before(eDate)) {
            sDate.add(Calendar.DAY_OF_MONTH, 1);
            daysBetween++;
        }
        return daysBetween;
    }
}