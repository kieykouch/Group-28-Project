package com.group28.cs160.noms4two;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.graphics.RectF;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
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
                NutrientsTracker nutrientsTracker = new NutrientsTracker(getBaseContext(), 1);
                nutrientsTracker.log(facts);
                finish();
            }
        });

        facts = (NutritionFacts) getIntent().getExtras().get("nutrient_facts");
        ArrayList<String> strings = getIntent().getStringArrayListExtra("allergens");
        byte[] arr = getIntent().getByteArrayExtra("image");
        assert facts != null;
        if (strings != null && strings.size() > 0) {
            Log.d("Detailed", strings.get(0));
        }
        getSupportActionBar().setTitle(facts.getName());

        if (arr != null) {
            BitmapFactory.Options options = new BitmapFactory.Options();
            options.outHeight = toolbar.getHeight();
            options.outWidth = toolbar.getWidth();
            Bitmap newBitmap = BitmapFactory.decodeByteArray(arr, 0, arr.length, options);
            ViewGroup group = (ViewGroup) findViewById(R.id.app_bar);
            assert group != null;
            group.setBackground(new BitmapDrawable(getResources(), newBitmap));
        }
        TextView calories = (TextView) findViewById(R.id.calories_amount);
        assert calories != null;
        calories.setText(String.format("%d Calories", (int) facts.getAmount(NutritionFacts.Nutrient.CALORIES)));
        inflateList(facts);
    }

    private void inflateList(NutritionFacts nutritionFacts) {
        LinearLayout ingredientsList = (LinearLayout) findViewById(R.id.ingredients_list);
        LayoutInflater inflater = (LayoutInflater)getSystemService(LAYOUT_INFLATER_SERVICE);
        assert ingredientsList != null;
        for (NutritionFacts.Nutrient nutrient : NutritionFacts.Nutrient.values()) {
            Log.d(TAG, String.format("inflating nutrient %s", NutritionFacts.nutrientToString(nutrient)));
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

    private final String TAG = "FoodDetailed";
    private NutritionFacts facts;
}
