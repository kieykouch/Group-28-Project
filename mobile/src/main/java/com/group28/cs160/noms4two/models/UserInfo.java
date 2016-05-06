package com.group28.cs160.noms4two.models;

import android.content.Context;
import android.util.Log;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;
import java.util.TimeZone;

/**
 * Created by eviltwin on 5/2/16.
 */
public class UserInfo {
    private static final String LOGIN_FILE = "LOGIN_FILE";
    Context context;
    Map<String, String> user_info;

    public UserInfo(Context context) {
        this.context = context;
        readFromFile();
    }

    public void logout() {
        context.deleteFile(LOGIN_FILE);
        user_info.clear();
    }

    public boolean userLoggedIn() {
        for (String file : context.fileList()) {
            Log.d("LoginInfo", "Found File " + file);
            if (file.equals(LOGIN_FILE)) return true;
        }
        return false;
    }

    public void readFromFile() {
        Map<String, String> map = new HashMap<String, String>();
        try {
            FileInputStream fileStream = context.openFileInput(LOGIN_FILE);
            ObjectInputStream objectStream = new ObjectInputStream(fileStream);
            map = (HashMap<String, String>) objectStream.readObject();
            objectStream.close();
            fileStream.close();
        } catch (Exception e) {
            Log.d("NutrientsTracker", "Exception reading from file: " + e.toString());
        }
        user_info = map;
    }

    public void writeToFile() {
        try {
            FileOutputStream fileStream = context.openFileOutput(LOGIN_FILE, Context.MODE_PRIVATE);
            ObjectOutputStream objectStream = new ObjectOutputStream(fileStream);
            objectStream.writeObject(user_info);
            objectStream.close();
            fileStream.close();
        } catch (Exception e) {
            Log.d("NutrientsTracker", "Exception writing to file: " + e.toString());
        }
    }

    public void setUserName(String userName) {
        user_info.put("Username", userName);
    }

    public void setDueDate(String dueDate) {
        user_info.put("Due Date", dueDate);
    }

    public void setTwins(String twins) {
        user_info.put("Twins", twins);
    }

    public void setWeight(String weight) {
        user_info.put("Weight", weight);
    }

    public double getWeight() {
        return Double.valueOf(user_info.get("Weight"));
    }

    public String getUserName() {
        return user_info.get("Username");
    }

    public String getDueDate() {
        return user_info.get("Due Date");
    }

    public boolean getTwins() {
        return Boolean.valueOf(user_info.get("Twins"));
    }

    public int getTrimester() {
        int[] days_left = getTimeline();
        int month = days_left[0];
        int trimester = 1;
        if (month > 3) {
            trimester = 2;
            if (month > 6) trimester = 3;
        }
        return trimester;
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
            Date dDate = dateFormat.parse(getDueDate());
            duration = daysBetween(currDate, dDate);
            Log.d("User Info", "duration: " + duration);
        } catch (Exception e) {
            Log.d("User Info:", "cannot parse date");
        }

        // Convert it into an array of month, date and year.
        if (duration == 0) {
            return new int[]{0, 0, 0};
        } else if (duration < 7) {
            return new int[]{0, 0, duration};
        } else if (duration < 30) {
            return new int[]{0, duration / 7, duration % 7};
        } else {
            return new int[]{duration / 30, (duration % 30) / 7, (duration % 30) % 7};
        }
    }

    private Calendar getDatePart(Date date) {
        Calendar cal = Calendar.getInstance();       // get calendar instance
        cal.setTime(date);
        cal.set(Calendar.HOUR_OF_DAY, 0);            // set hour to midnight
        cal.set(Calendar.MINUTE, 0);                 // set minute in hour
        cal.set(Calendar.SECOND, 0);                 // set second in minute
        cal.set(Calendar.MILLISECOND, 0);            // set millisecond in second
        return cal;                                  // return the date part
    }

    private int daysBetween(Date startDate, Date endDate) {
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