package com.group28.cs160.noms4two;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;

import com.group28.cs160.shared.NutritionFacts;

import java.util.ArrayList;

public class DetailedActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detailed);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        assert toolbar != null;
        setSupportActionBar(toolbar);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        assert fab != null;
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // TODO Add to summary
                Snackbar.make(view, "Added to My Summary", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });

        NutritionFacts facts = (NutritionFacts) getIntent().getExtras().get("nutrient_facts");
        ArrayList<String> strings = getIntent().getStringArrayListExtra("allergens");
        assert strings != null;
        assert facts != null;
        Log.d("Detailed", strings.get(0));
        getSupportActionBar().setTitle(facts.getName());

    }
}
