package com.group28.cs160.noms4two;

import com.group28.cs160.shared.NutritionFacts;

/**
 * Created by eviltwin on 4/29/16.
 */
public class FakeData {
    public static void addFakeData(NutrientsTracker nutrientsTracker) {
        // Add some fake data.
        // Pasta
        NutritionFacts pasta = new NutritionFacts("Pasta", 600);
        pasta.protein=15;
        pasta.iron=10;
        pasta.potassium=500;
        pasta.fiber=5;
        pasta.calcium=100;

        nutrientsTracker.log(pasta);

        // TODO(prad): Maybe log with timestamps.

        NutritionFacts wrap = new NutritionFacts("Wrap", 300);
        wrap.protein=0;
        pasta.iron=20;
        pasta.potassium=1000;
        pasta.fiber=10;
        pasta.calcium=300;

        nutrientsTracker.log(wrap);
    }
}
