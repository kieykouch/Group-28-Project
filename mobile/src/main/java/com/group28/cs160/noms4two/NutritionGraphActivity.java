package com.group28.cs160.noms4two;

import android.app.Activity;
import android.graphics.Color;
import android.os.Bundle;

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
import java.util.List;
import java.util.Map;

public class NutritionGraphActivity extends Activity {

    public static String NUTRIENT = "com.group28.cs160.NUTRIENT";

    private CombinedChart mChart;
    private NutrientsTracker nutrientsTracker;
    NutritionFacts.Nutrient nutrient;
    private final int itemcount = 7;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_nutrition_graph);
        Bundle bundle = getIntent().getExtras();
        nutrient = NutritionFacts.Nutrient.values()[bundle.getInt(NUTRIENT)];
        nutrientsTracker = new NutrientsTracker(getBaseContext());

        mChart = (CombinedChart) findViewById(R.id.chart1);
        mChart.setDescription(NutritionFacts.nutrientToString(nutrient));
        mChart.setBackgroundColor(Color.WHITE);
        mChart.setDrawGridBackground(false);
        mChart.setDrawBarShadow(true);

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

        List<String> days = new ArrayList<String>();
        days.add("Monday");
        days.add("Tuesday");
        days.add("Wednesday");
        days.add("Thursday");
        days.add("Friday");
        days.add("Saturday");
        days.add("Sunday");
        CombinedData data = new CombinedData(days);

        data.setData(generateLineData());
        data.setData(generateBarData());

        mChart.setData(data);
        mChart.invalidate();
    }

    private LineData generateLineData() {

        LineData d = new LineData();

        ArrayList<Entry> entries = new ArrayList<Entry>();

        NutritionFacts goals = nutrientsTracker.getDailyGoals();

        for (int index = 0; index < itemcount; index++)
            entries.add(new Entry((float) goals.getAmount(nutrient), index));

        LineDataSet set = new LineDataSet(entries, "Line DataSet");
        set.setColor(Color.rgb(240, 238, 70));
        set.setLineWidth(2.5f);
        set.setCircleColor(Color.rgb(240, 238, 70));
        set.setCircleRadius(5f);
        set.setFillColor(Color.rgb(240, 238, 70));
        set.setDrawCubic(true);
        set.setDrawValues(true);
        set.setValueTextSize(10f);
        set.setValueTextColor(Color.rgb(240, 238, 70));

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

        // Breakdown food by day.
        for (int index = 0; index < itemcount; index++)
            entries.add(new BarEntry(nutrientHistory.get(index).floatValue(), index));

        BarDataSet set = new BarDataSet(entries, "Bar DataSet");
        set.setColor(Color.rgb(60, 220, 78));
        set.setValueTextColor(Color.rgb(60, 220, 78));
        set.setValueTextSize(10f);
        d.addDataSet(set);

        set.setAxisDependency(YAxis.AxisDependency.LEFT);

        return d;
    }
}
