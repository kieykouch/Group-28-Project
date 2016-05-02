package com.group28.cs160.noms4two;

import android.util.Log;

import com.group28.cs160.shared.NutritionFacts;

import java.util.Calendar;
import java.util.Random;

/**
 * Created by eviltwin on 4/29/16.
 */
public class FakeData {
    public static void addFakeData(NutrientsTracker nutrientsTracker) {
        long millisInDay = 24*60*60*1000;
        Calendar c = Calendar.getInstance();
        long now = c.getTimeInMillis();
        c.set(Calendar.HOUR_OF_DAY, 0);
        c.set(Calendar.MINUTE, 0);
        c.set(Calendar.SECOND, 0);
        c.set(Calendar.MILLISECOND, 0);
        long todayTimestamp = c.getTimeInMillis() + 100;

        String[] fake_food_names = {"Pasta", "Chicken Soup", "Pho", "BLT Sandwich", "Samosas", "Falafel Wrap", "Pepperoni Pizza"};

        // Add partial fake data for today.
        NutritionFacts today = getFakeNutrition(nutrientsTracker.getDailyGoals(), 0.5);
        today.name = fake_food_names[0];
        nutrientsTracker.log(today);
        for (int i=1; i < 7; i++) {
            long timestamp = todayTimestamp - i*millisInDay;
            NutritionFacts food = getFakeNutrition(nutrientsTracker.getDailyGoals(), 1);
            food.name = fake_food_names[i];
            nutrientsTracker.log(food,timestamp);
        }
    }

    private static NutritionFacts getFakeNutrition(NutritionFacts goals, double percentage) {
        NutritionFacts fake_food = new NutritionFacts("fake", 0);
        // Return an object that meets the given percentage of the goal +- 20% randomly.

        Random rand = new Random();
        Log.d("FakeData", "Float :" + rand.nextFloat());
        percentage = percentage * (1 + (rand.nextFloat() * 0.4 - 0.3));
        Log.d("FakeData", "Percentage :" + percentage);

        fake_food.calories = Math.round(goals.calories * percentage);
        fake_food.vitaminC =  Math.round(goals.vitaminC * percentage);
        fake_food.fiber = Math.round(goals.fiber * percentage);
        fake_food.calcium = Math.round(goals.calcium * percentage);
        fake_food.potassium = Math.round(goals.potassium * percentage);
        fake_food.iron = Math.round(goals.iron * percentage);
        fake_food.protein = Math.round(goals.protein * percentage);
        return fake_food;
    }
}
