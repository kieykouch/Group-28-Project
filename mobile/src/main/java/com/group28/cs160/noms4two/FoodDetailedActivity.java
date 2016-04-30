package com.group28.cs160.noms4two;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.graphics.RectF;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.group28.cs160.shared.NutritionFacts;

import java.util.ArrayList;

public class FoodDetailedActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_food_detailed);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        assert toolbar != null;
        setSupportActionBar(toolbar);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        assert fab != null;
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // TODO Add to summary
                NutrientsTracker nutrientsTracker = new NutrientsTracker(getBaseContext());
                nutrientsTracker.log(facts);
                setResult(RESULT_OK);
                finish();
            }
        });

        facts = (NutritionFacts) getIntent().getExtras().get("nutrient_facts");
        ArrayList<String> allergens = getIntent().getStringArrayListExtra("allergens");
        byte[] arr = getIntent().getByteArrayExtra("image");
        assert facts != null;
        ActionBar actionBar = getSupportActionBar();
        assert actionBar != null;
        actionBar.setTitle(facts.getName());

        if (arr != null) {
            BitmapFactory.Options options = new BitmapFactory.Options();
            options.outHeight = toolbar.getHeight();
            options.outWidth = toolbar.getWidth();
            Bitmap newBitmap = BitmapFactory.decodeByteArray(arr, 0, arr.length, options);
            ViewGroup group = (ViewGroup) findViewById(R.id.app_bar);
            assert group != null;
//            group.setBackground(new BitmapDrawable(getResources(), newBitmap));
        }
        TextView calories = (TextView) findViewById(R.id.calories_amount);
        assert calories != null;
        String displayCalories = String.valueOf(
                (int)facts.getAmount(NutritionFacts.Nutrient.CALORIES)) + " Calories";
        calories.setText(displayCalories);
        inflateList(facts);
        if (allergens != null) {
            inflateAllergens(allergens);
        }
    }

    private void inflateList(NutritionFacts nutritionFacts) {
        LinearLayout ingredientsList = (LinearLayout) findViewById(R.id.ingredients_list);
        LayoutInflater inflater = (LayoutInflater)getSystemService(LAYOUT_INFLATER_SERVICE);
        assert ingredientsList != null;
        for (NutritionFacts.Nutrient nutrient : NutritionFacts.Nutrient.values()) {
            Log.d("FoodDetailed", String.format("inflating nutrient %s", NutritionFacts.nutrientToString(nutrient)));
            String ingredientName = NutritionFacts.nutrientToString(nutrient);
            double amount = nutritionFacts.getAmount(nutrient);
            if (amount == 0)
                continue;
            Bitmap bitmap = BitmapFactory.decodeResource(getResources(),
                    NutritionFacts.nutrientToResource(nutrient));
            Matrix m = new Matrix();
            m.setRectToRect(new RectF(0, 0, bitmap.getWidth(), bitmap.getHeight()),
                    new RectF(0, 0, 400, 400), Matrix.ScaleToFit.CENTER);
            bitmap = Bitmap.createBitmap(bitmap, 0, 0, bitmap.getWidth(), bitmap.getHeight(), m, false);
            View item = inflater.inflate(R.layout.food_detailed_list_item, ingredientsList, false);
            TextView textView = (TextView) item.findViewById(R.id.ingredient);
            textView.setText(ingredientName);
            textView.setCompoundDrawablesWithIntrinsicBounds(null, null, null, new BitmapDrawable(getResources(), bitmap));
            ingredientsList.addView(item);
        }
    }

    private void inflateAllergens(ArrayList<String> allergens) {
        LinearLayout ingredientsList = (LinearLayout) findViewById(R.id.ingredients_list);
        LayoutInflater inflater = (LayoutInflater)getSystemService(LAYOUT_INFLATER_SERVICE);
        assert ingredientsList != null;
        for (String allergen : allergens) {
            Log.d("FoodDetailed", String.format("inflating allergen %s", allergen));
            int resourceID = findAllergenResource(allergen);
            Bitmap bitmap = BitmapFactory.decodeResource(getResources(), resourceID);
            Matrix m = new Matrix();
            m.setRectToRect(new RectF(0, 0, bitmap.getWidth(), bitmap.getHeight()),
                    new RectF(0, 0, 350, 350), Matrix.ScaleToFit.CENTER);
            bitmap = Bitmap.createBitmap(bitmap, 0, 0, bitmap.getWidth(), bitmap.getHeight(), m, false);
            View item = inflater.inflate(R.layout.food_detailed_list_item_red, ingredientsList, false);
            TextView textView = (TextView) item.findViewById(R.id.ingredient);
            textView.setText(allergen);
            textView.setCompoundDrawablesWithIntrinsicBounds(null, null, null, new BitmapDrawable(getResources(), bitmap));
            ingredientsList.addView(item);
        }
    }

    private void inflateCausious(ArrayList<String> cautious) {
    }

    private int findAllergenResource(String allergen) {
        int resourceID = R.drawable.cancel;
        switch (allergen) {
            case "allergen_contains_milk":
                resourceID = R.drawable.milk;
                break;
            case "allergen_contains_eggs":
                resourceID = R.drawable.egg;
                break;
            case "allergen_contains_fish":
                resourceID = R.drawable.fish;
                break;
            case "allergen_contains_shellfish":
                resourceID = R.drawable.shrimp;
                break;
            case "allergen_contains_peanuts":
                resourceID = R.drawable.peanuts;
                break;
            case "allergen_contains_soybeans":
                resourceID = R.drawable.soybean;
                break;
            default:
                break;
        }
        return resourceID;
    }

    private NutritionFacts facts;
}
