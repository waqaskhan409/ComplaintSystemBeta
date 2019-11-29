package com.example.complaintsystembeta.ui.complaints;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.widget.TextView;

import androidx.annotation.RequiresApi;
import androidx.appcompat.widget.Toolbar;

import com.example.complaintsystembeta.BaseActivity;
import com.example.complaintsystembeta.R;
import com.example.complaintsystembeta.constants.Constants;
import com.example.complaintsystembeta.interfaace.JsonApiHolder;
import com.example.complaintsystembeta.model.AllComplains;
import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.components.AxisBase;
import com.github.mikephil.charting.components.Description;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.data.PieEntry;
import com.github.mikephil.charting.formatter.IAxisValueFormatter;
import com.github.mikephil.charting.formatter.IValueFormatter;
import com.github.mikephil.charting.formatter.IndexAxisValueFormatter;
import com.github.mikephil.charting.formatter.ValueFormatter;
import com.github.mikephil.charting.listener.ChartTouchListener;
import com.github.mikephil.charting.listener.OnChartGestureListener;
import com.github.mikephil.charting.utils.ColorTemplate;
import com.github.mikephil.charting.utils.ViewPortHandler;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class DescriptionAndrGraphActivity extends BaseActivity {
    private static final String TAG = "DescriptionAndrGraphAct";
    private String name, description;
    private Bundle data;
    private TextView mText;
    private ArrayList<Integer> arrayListForGraph = new ArrayList<>();
    private ArrayList<BarEntry> barEntries = new ArrayList<>();
    private ArrayList<String> list  = new ArrayList<>();
    private BarChart mBarChart ;
    private PieChart mPieChart ;
    private Toolbar toolbar;
    private ArrayList<Integer> valuesForGraphs = new ArrayList<>();
    private ArrayList<AllComplains> valuesForPending = new ArrayList<>();
    private ArrayList<AllComplains> valuesForNew = new ArrayList<>();
    private ArrayList<AllComplains> valuesForResolved = new ArrayList<>();
    private ArrayList<AllComplains> valuesForEmployee = new ArrayList<>();




    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_description_andr_graph);
        mBarChart = (BarChart) findViewById(R.id.graphBarChart);
        mPieChart = (PieChart) findViewById(R.id.graphPieChart);
        mText = findViewById(R.id.description);
        toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        mBarChart.setOnChartGestureListener(new OnChartGestureListener() {
            @Override
            public void onChartGestureStart(MotionEvent me, ChartTouchListener.ChartGesture lastPerformedGesture) {

            }

            @Override
            public void onChartGestureEnd(MotionEvent me, ChartTouchListener.ChartGesture lastPerformedGesture) {

            }

            @Override
            public void onChartLongPressed(MotionEvent me) {
                Log.d(TAG, "onChartLongPressed: zoom in");
            }

            @Override
            public void onChartDoubleTapped(MotionEvent me) {
                Log.d(TAG, "onChartDoubleTapped: zoom in");

                String[] values = {"Jan", "Feb", "March" , "April" , "May", "June", "July", "August", "September", "October", "November", "December"};
                XAxis xAxis = mBarChart.getXAxis();
                xAxis.setValueFormatter(new MyAxisValueFormatter(values));


            }

            @Override
            public void onChartSingleTapped(MotionEvent me) {
                Log.d(TAG, "onChartSingleTapped: zoom in");
            }

            @Override
            public void onChartFling(MotionEvent me1, MotionEvent me2, float velocityX, float velocityY) {
                Log.d(TAG, "onChartFling: zoom in");
            }

            @Override
            public void onChartScale(MotionEvent me, float scaleX, float scaleY) {
                Log.d(TAG, "onChartScale: zoom in");
                Log.d(TAG, "onChartScale: zoom in" + scaleX);
                Log.d(TAG, "onChartScale: zoom in" + scaleY);
                if(scaleX == 1.0){
                    settingBarGraph();
                }else {

                }

            }

            @Override
            public void onChartTranslate(MotionEvent me, float dX, float dY) {
                Log.d(TAG, "onChartTranslate: zoom in");
            }
        });

        mBarChart.setOnScrollChangeListener(new View.OnScrollChangeListener() {
            @Override
            public void onScrollChange(View v, int scrollX, int scrollY, int oldScrollX, int oldScrollY) {
                Log.d(TAG, "onScrollChange: Scroll in");
            }
        });


        settingupValues();
    }

    private void settingPieGraph() {
        List<PieEntry> value  = new ArrayList<>();

        value.add(new PieEntry(valuesForGraphs.get(0), "Resolved"));
        value.add(new PieEntry(valuesForGraphs.get(1), "Pending"));
        value.add(new PieEntry(valuesForGraphs.get(2), "New"));

        PieDataSet pieDataSet = new PieDataSet(value, "Complains");
        PieData pieData = new PieData(pieDataSet);

        pieDataSet.setColors(ColorTemplate.COLORFUL_COLORS);

        mPieChart.setData(pieData);

    }

    private void settingBarGraph() {
        Log.d(TAG, "settingGraph: " + arrayListForGraph);
        for (int i = 0; i < valuesForGraphs.size(); i++) {
            barEntries.add(new BarEntry(i+1, Float.parseFloat(String.valueOf(valuesForGraphs.get(i))), "lopa"));
            Log.d(TAG, "settingBarGraph: " + i);
        }
        String[] values = {"Year", "months", "days"};
        new BarEntry(0f, 0);
        final ArrayList<String> xAxisLabel = new ArrayList<>();
        xAxisLabel.add("Year");

        BarDataSet dataSetRevenue = new BarDataSet(barEntries, "Complains");
        dataSetRevenue.setColors(ColorTemplate.COLORFUL_COLORS);

//        BarDataSet dataSetMonth = new BarDataSet(months, "date");
        mBarChart.setTouchEnabled(true);
        mBarChart.setScaleEnabled(true);
//        mBarChart.setDragEnabled(true);
        BarData data = new BarData(dataSetRevenue);
        mBarChart.setData(data);
//        mBarChart.setFitBars(true);
        mBarChart.setDragEnabled(false);



        Description description = new Description();
        description.setText(getString(R.string.description));
        mBarChart.setDescription(description);
//        mBarChart.getBarData().setValueFormatter();
        mBarChart.invalidate();
//        XAxis xAxis = mBarChart.getXAxis();
//        xAxis.setValueFormatter(new MyAxisValueFormatter(values));
    }

    private void settingBarGraphForDept() {
        Log.d(TAG, "settingGraph: " + arrayListForGraph);
        for (int i = 0; i < valuesForGraphs.size(); i++) {
            barEntries.add(new BarEntry(i, Float.parseFloat(String.valueOf(valuesForGraphs.get(i))), list.get(i)));
        }

        new BarEntry(0f, 0);

        BarDataSet dataSetRevenue = new BarDataSet(barEntries, "Complains");
        dataSetRevenue.setColors(ColorTemplate.COLORFUL_COLORS);

//        BarDataSet dataSetMonth = new BarDataSet(months, "date");
        mBarChart.setTouchEnabled(true);
        mBarChart.setScaleEnabled(true);
        mBarChart.setDragEnabled(true);
        BarData data = new BarData(dataSetRevenue);
        mBarChart.setData(data);
        mBarChart.setFitBars(true);


        Description description = new Description();
        description.setText(getString(R.string.description));
        mBarChart.setDescription(description);
//        mBarChart.getBarData().setValueFormatter();
        mBarChart.invalidate();
    }


    private void settingupValues() {
        fetchComplains();
        data = getIntent().getExtras();
        name = "Complaints Charts";
        description = "Complaints";
        for (int i = 0; i < 10; i++) {
            list.add(String.valueOf(i));
        }
        getSupportActionBar().setTitle(name);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        mText.setText(description);
    }



    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }

    private void fetchComplains() {
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(Constants.REST_API)
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        JsonApiHolder service = retrofit.create(JsonApiHolder.class);

        Call<List<AllComplains>> call = service.getComplains();

        call.enqueue(new Callback<List<AllComplains>>() {
            @Override
            public void onResponse(Call<List<AllComplains>> call, Response<List<AllComplains>> response) {
                if(!response.isSuccessful()){
                    Log.d(TAG, "onResponse: Failed!");
                    return;
                }
                Log.d(TAG, "onResponse: "+ response.body());
                List<AllComplains>  list = response.body();
                for(AllComplains all: list){
                    if(all.getComplain_status().equals(Constants.COMPLAINS_RESOLVED)){
                        valuesForResolved.add(all);
                    }else if(all.getComplain_status().equals(Constants.COMPLAINS_PENDING)) {
                        valuesForPending.add(all);
                    }else if(all.getComplain_status().equals(Constants.COMPLAINS_NEW)) {
                        valuesForNew.add(all);
                    }
                }
                getArrayListForGraphInteger();

            }

            @Override
            public void onFailure(Call<List<AllComplains>> call, Throwable t) {

            }
        });

    }

    private void getArrayListForGraphInteger() {
        valuesForGraphs.clear();
        valuesForGraphs.add(valuesForResolved.size());
        valuesForGraphs.add(valuesForPending.size());
        valuesForGraphs.add(valuesForNew.size());
        settingPieGraph();
        settingBarGraph();


    }

    public class MyAxisValueFormatter extends ValueFormatter {
        private String values[];

        public MyAxisValueFormatter(String[] values) {
            this.values = values;
        }

        @Override
        public String getFormattedValue(float value) {
            return values[(int) value];
        }
    }



}
