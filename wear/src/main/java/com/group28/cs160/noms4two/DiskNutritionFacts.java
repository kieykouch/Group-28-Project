package com.group28.cs160.noms4two;

import android.content.Context;
import android.util.Log;

import com.group28.cs160.shared.NutritionFacts;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

/**
 * Created by eviltwin on 4/28/16.
 */
public class DiskNutritionFacts {
    public static String GOALS_FILE = "GOALS_FILE";
    public static String INFO_FILE = "INFO_FILE";

    public static NutritionFacts readFromFile(Context context, String file) {
        NutritionFacts facts = new NutritionFacts("Goals", 0);
        try {
            FileInputStream fileStream = context.openFileInput(file);
            ObjectInputStream objectStream = new ObjectInputStream(fileStream);
            facts = (NutritionFacts) objectStream.readObject();
            objectStream.close();
            fileStream.close();
        } catch (Exception e) {
            Log.d("NutrientsTracker", "Exception reading from file: " + e.toString());
        }
        return facts;
    }

    public static void writeToFile(Context context, String file, NutritionFacts facts) {
        try {
            FileOutputStream fileStream = context.openFileOutput(file, Context.MODE_PRIVATE);
            ObjectOutputStream objectStream = new ObjectOutputStream(fileStream);
            objectStream.writeObject(facts);
            objectStream.close();
            fileStream.close();
        } catch (Exception e) {
            Log.d("NutrientsTracker", "Exception writing to file: " + e.toString());
        }
    }

}
