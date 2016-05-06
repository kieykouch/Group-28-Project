package com.group28.cs160.noms4two;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.graphics.RectF;
import android.graphics.drawable.BitmapDrawable;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.group28.cs160.noms4two.models.NutrientsTracker;
import com.group28.cs160.shared.NutritionFacts;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.URL;
import java.net.URLEncoder;
import java.util.ArrayList;

import javax.net.ssl.HttpsURLConnection;

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
                NutrientsTracker nutrientsTracker = new NutrientsTracker(getBaseContext());
                nutrientsTracker.log(facts);
                setResult(RESULT_OK);
                finish();
            }
        });

        facts = (NutritionFacts) getIntent().getExtras().get("nutrient_facts");
        ArrayList<String> allergens = getIntent().getStringArrayListExtra("allergens");
        assert facts != null;

        String displayName = facts.getName().split("[,]")[0];
        TextView view = (TextView) findViewById(R.id.title);
        TextView calories = (TextView) findViewById(R.id.calories_amount);
        assert view != null;
        assert calories != null;
        view.setText(displayName);
        String displayCalories = String.valueOf(
                (int)facts.getAmount(NutritionFacts.Nutrient.CALORIES)) + " Calories";
        calories.setText(displayCalories);
        inflateList(facts);
        if (allergens != null) {
            inflateAllergens(allergens);
        }
        changeToolbarBackground(facts.getName());
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

    private void changeToolbarBackground(String name) {
        new AsyncTask<String, String, Bitmap>() {

            @Override
            protected Bitmap doInBackground(String... params) {
                String name = params[0];
                String url = "https://www.googleapis.com/customsearch/v1";
                String query = null;
                try {
                    query = String.format("q=%s+food&searchType=image&imageSize=medium&num=1",
                            URLEncoder.encode(name, "UTF-8"));
                } catch (UnsupportedEncodingException e) {
                    e.printStackTrace();
                }
                String key = "cx=003632327980625916958:qyyugsukqsy&key=AIzaSyBHJRRq2iQ7jH1lIefRxH_H0HCYyKpOzLw";
                Bitmap bitmap = null;
                if (FAKE) {
                    try {
                        URL profile = new URL("https://upload.wikimedia.org/wikipedia/commons/4/44/Bananas_white_background_DS.jpg");
                        bitmap = BitmapFactory.decodeStream(profile.openStream());
                        Matrix m = new Matrix();
                        m.setRectToRect(new RectF(0, 0, bitmap.getWidth(), bitmap.getHeight()),
                                new RectF(0, 0, 1500, 1500), Matrix.ScaleToFit.CENTER);
                        bitmap = Bitmap.createBitmap(bitmap, 0, 0, bitmap.getWidth(), bitmap.getHeight(), m, false);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                } else {
                    JSONObject result = getFromURL(url, query, key);
                    try {
                        assert result != null;
                        String link = result.optJSONArray("items").getJSONObject(0).getString("link");
                        URL profile = new URL(link);
                        bitmap = BitmapFactory.decodeStream(profile.openStream());
                    } catch (JSONException | IOException e) {
                        e.printStackTrace();
                    }
                }
                return bitmap;
            }

            @Override
            protected void onPostExecute(Bitmap result) {
                ImageView view = (ImageView) findViewById(R.id.food_image);
                assert view != null;
                view.setImageDrawable(new BitmapDrawable(getResources(), result));
            }
        }.execute(name);
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

    private JSONObject getFromURL(String url, String query, String apikey) {
        Log.d("Info", String.format("loading url %s?%s&%s", url, query, apikey));
        JSONObject json = null;
        try {
            HttpsURLConnection connection =
                    (HttpsURLConnection) new URL(String.format("%s?%s&%s",
                            url, query, apikey)).openConnection();
            if (connection.getResponseCode() == 404) {
                Log.e("FoodDetailed", connection.getResponseMessage());
                return null;
            }
            BufferedReader reader = new BufferedReader(new InputStreamReader(
                    connection.getInputStream(), "UTF-8"));
            StringBuilder builder = new StringBuilder();
            String line;
            while ((line = reader.readLine()) != null) {
                builder.append(line);
            }
            json = new JSONObject(builder.toString());
        } catch (IOException |JSONException e) {
            Log.e("Info", e.getMessage());
        }
        return json;
    }

    private NutritionFacts facts;
    private static final boolean FAKE = false;
}
