package com.group28.cs160.noms4two;

import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.widget.TextView;

import com.github.mikephil.charting.charts.CombinedChart;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.data.CombinedData;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.group28.cs160.shared.NutritionFacts;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.List;
import java.util.Map;

public class NutritionGraphActivity extends AppCompatActivity {

    public static String NUTRIENT = "com.group28.cs160.NUTRIENT";

    private CombinedChart mChart;
    private NutrientsTracker nutrientsTracker;
    NutritionFacts.Nutrient nutrient;
    private final int itemcount = 7;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_nutrition_graph);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        Bundle bundle = getIntent().getExtras();
        nutrient = NutritionFacts.Nutrient.valueOf(bundle.getString(NUTRIENT));
        Log.d("NutritionGraph", "Opening Graph for " + nutrient.toString());
        nutrientsTracker = new NutrientsTracker(getBaseContext());

        mChart = (CombinedChart) findViewById(R.id.chart1);
        mChart.setDrawGridBackground(false);
        mChart.setDrawBarShadow(false);

        // draw bars behind lines
        mChart.setDrawOrder(new CombinedChart.DrawOrder[] {CombinedChart.DrawOrder.BAR, CombinedChart.DrawOrder.LINE});

        YAxis rightAxis = mChart.getAxisRight();
        rightAxis.setDrawGridLines(false);
        rightAxis.setAxisMinValue(0f); // this replaces setStartAtZero(true)

        YAxis leftAxis = mChart.getAxisLeft();
        leftAxis.setDrawGridLines(false);
        leftAxis.setAxisMinValue(0f); // this replaces setStartAtZero(true)

        XAxis xAxis = mChart.getXAxis();
        xAxis.setPosition(XAxis.XAxisPosition.BOTH_SIDED);
        xAxis.setDrawGridLines(false);
        xAxis.setLabelsToSkip(0);

        List<String> days = new ArrayList<String>();

        Calendar calendar = Calendar.getInstance();
        int dayOfWeek = calendar.get(Calendar.DAY_OF_WEEK) - 1; // 1 indexed
        List<String> dayNames = new ArrayList<>();
        dayNames.add("Sun");
        dayNames.add("Mon");
        dayNames.add("Tues");
        dayNames.add("Wed");
        dayNames.add("Thur");
        dayNames.add("Fri");
        dayNames.add("Sat");

        // Get names of last 7 days. Today is today.
        days.add("Today");
        for (int i=1; i<7; i++) {
            int day = dayOfWeek - i;
            day = day < 0? day + 7: day;
            days.add(dayNames.get(day));
        }
        Collections.reverse(days);
        CombinedData data = new CombinedData(days);

        data.setData(generateLineData());
        data.setData(generateBarData());

        mChart.setData(data);
        mChart.invalidate();

        TextView title = (TextView) findViewById(R.id.graphtitle);
        title.setText(NutritionFacts.nutrientToString(nutrient) + " History");
    }

    private LineData generateLineData() {

        LineData d = new LineData();

        ArrayList<Entry> entries = new ArrayList<Entry>();

        NutritionFacts goals = nutrientsTracker.getDailyGoals();

        for (int index = 0; index < itemcount; index++)
            entries.add(new Entry((float) goals.getAmount(nutrient), index));

        LineDataSet set = new LineDataSet(entries, "Goal");
        set.setColor(Color.DKGRAY);
        set.setLineWidth(6f);
        set.setCircleRadius(0f);
        set.setDrawCubic(true);
        set.setValueTextSize(0f);

        set.setAxisDependency(YAxis.AxisDependency.LEFT);
        d.addDataSet(set);
        return d;
    }

    private BarData generateBarData() {

        BarData d = new BarData();

        ArrayList<BarEntry> entries = new ArrayList<BarEntry>();

        long millisInDay = 24*60*60*1000;
        Calendar c = Calendar.getInstance();
        long now = c.getTimeInMillis();
        c.set(Calendar.HOUR_OF_DAY, 0);
        c.set(Calendar.MINUTE, 0);
        c.set(Calendar.SECOND, 0);
        c.set(Calendar.MILLISECOND, 0);
        long midnightTimestamp = c.getTimeInMillis();

        long oneWeekAgo = midnightTimestamp - 7*millisInDay;
        Map<Long, NutritionFacts> recent_foods = nutrientsTracker.getRecent(oneWeekAgo);

        ArrayList<Double> nutrientHistory = new ArrayList<>();
        for (int i =0; i<7; i++) nutrientHistory.add(0.0);

        for (Map.Entry<Long, NutritionFacts> entry : recent_foods.entrySet()) {
            // breakdown food by day.
            for (int i = 0; i < 7; i++) {
                if (entry.getKey() > midnightTimestamp - i*millisInDay) {
                    nutrientHistory.set(i, nutrientHistory.get(0) + entry.getValue().getAmount(nutrient));
                    break;
                }
            }
        }

        // Put today to the right.
        Collections.reverse(nutrientHistory);

        // Breakdown food by day.
        for (int index = 0; index < itemcount; index++)
            entries.add(new BarEntry(nutrientHistory.get(index).floatValue(), index));

        BarDataSet set = new BarDataSet(entries, NutritionFacts.nutrientToString(nutrient) + " History");
        set.setColor(NutritionFacts.nutrientToColor(nutrient));
        set.setValueTextColor(Color.BLACK);
        set.setValueTextSize(10f);
        d.addDataSet(set);

        set.setAxisDependency(YAxis.AxisDependency.LEFT);

        return d;
    }
}
