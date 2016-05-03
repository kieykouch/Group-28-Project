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
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by eviltwin on 4/26/16.
 * This class stores food logged by the user, and allows for it to be shared with the watch.
 *
 * This is not an efficient implementation, since it uses a HashMap with a Date range, but the
 * abstraction should be sufficient to improve it independently later on.
 */
public class NutrientsTracker {
    private static final String HISTORY_FILE = "HISTORY_FILE";
    Context context;
    Map <Long, NutritionFacts> food_logged;
    UserInfo userInfo;

    public NutrientsTracker(Context context) {
        this.context = context;
        userInfo = new UserInfo(context);
        readFromFile();
        sendToWatch();
    }

    public void reset() {
        context.deleteFile(HISTORY_FILE);
        food_logged.clear();
    }

    private void sendToWatch() {
        Intent sendIntent = new Intent(context, MobileToWatchService.class);
        sendIntent.putExtra(MobileToWatchService.INFO, getNutritionToday());
        sendIntent.putExtra(MobileToWatchService.GOALS, getDailyGoals());
        context.startService(sendIntent);
    }

    public void readFromFile() {
        Map<Long, NutritionFacts> map = new HashMap<Long, NutritionFacts>();
        try {
            FileInputStream fileStream = context.openFileInput(HISTORY_FILE);
            ObjectInputStream objectStream = new ObjectInputStream(fileStream);
            map = (HashMap<Long, NutritionFacts>) objectStream.readObject();
            objectStream.close();
            fileStream.close();
        } catch (Exception e) {
            Log.d("NutrientsTracker", "Exception reading from file: " + e.toString());
        }
        food_logged = map;
    }

    public void writeToFile() {
        try {
            FileOutputStream fileStream = context.openFileOutput(HISTORY_FILE, Context.MODE_PRIVATE);
            ObjectOutputStream objectStream = new ObjectOutputStream(fileStream);
            objectStream.writeObject(food_logged);
            objectStream.close();
            fileStream.close();
            // Also sync with watch everytime you write to file.
            sendToWatch();
        } catch (Exception e) {
            Log.d("NutrientsTracker", "Exception writing to file: " + e.toString());
        }
    }

    public long log(NutritionFacts nutritionFacts) {
        long now = System.currentTimeMillis();
        // Also save data somewhere.
        food_logged.put(now, nutritionFacts);
        Log.d("NutrientsTracker", "Logged: " + nutritionFacts.name + " at " + now);
        writeToFile();
        return now;
    }

    // Workaround for fake data.
    public long log(NutritionFacts nutritionFacts, long time) {
        // Also save data somewhere.
        food_logged.put(time, nutritionFacts);
        Log.d("NutrientsTracker", "Logged: " + nutritionFacts.name + " at " + time);
        writeToFile();
        return time;
    }


    public void delete(long foodId) {
        food_logged.remove(foodId);
    }

    public Map<Long, NutritionFacts> getRecent(long since) {
        // Returns a list of food eaten after the "since" timestamp.
        Map<Long, NutritionFacts> recent = new HashMap<>();
        for (Map.Entry<Long, NutritionFacts> entry : food_logged.entrySet()) {
            if (entry.getKey() > since) {
                recent.put(entry.getKey(), entry.getValue());
            }
        }
        return recent;
    }

    public NutritionFacts getMostRecent() {
        // Returns the most recent food.
        NutritionFacts recent;
        Long key = Collections.max(food_logged.keySet());
        return food_logged.get(key);
    }

    public NutritionFacts getNutritionToday() {
        // Returns a summary of the nutrition today.
        Calendar c = Calendar.getInstance();
        c.add(Calendar.DAY_OF_MONTH, 0);
        c.set(Calendar.HOUR_OF_DAY, 0);
        c.set(Calendar.MINUTE, 0);
        c.set(Calendar.SECOND, 0);
        c.set(Calendar.MILLISECOND, 0);

        Map<Long, NutritionFacts> logged_today = getRecent(c.getTimeInMillis());
        NutritionFacts daily_total = new NutritionFacts("daily total", 0);
        for (Map.Entry<Long, NutritionFacts> entry : logged_today.entrySet()) {
            daily_total.add(entry.getValue());
        }
        return daily_total;
    }

    public NutritionFacts getDailyGoals() {
        int calorieGoal = 1500 + 100 * userInfo.getTrimester();
        calorieGoal = userInfo.getTwins() ? calorieGoal + 100 : calorieGoal;
        NutritionFacts goals = new NutritionFacts("goals", calorieGoal);
        goals.protein = 60; // Ki's doc said milligrams, but that is too less. Think its grams.
        goals.calcium = 1200;
        goals.iron = 30;
        goals.fiber = 25;
        goals.potassium = 4700;
        goals.vitaminC = 500;
        return goals;
    }

    public void clear() {
        food_logged = new HashMap<Long, NutritionFacts>();
        writeToFile();
    }
}
