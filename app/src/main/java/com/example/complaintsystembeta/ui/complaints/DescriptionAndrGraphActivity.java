package com.example.complaintsystembeta.ui.complaints;

import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.widget.Toolbar;
import androidx.viewpager.widget.ViewPager;

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

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;
import io.github.inflationx.viewpump.ViewPumpContextWrapper;
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
    private Unbinder unbinder;
    private Toolbar toolbar;
    private ArrayList<Integer> valuesForGraphs = new ArrayList<>();
    private ArrayList<AllComplains> valuesForPending = new ArrayList<>();
    private ArrayList<AllComplains> valuesForNew = new ArrayList<>();
    private ArrayList<AllComplains> valuesForResolved = new ArrayList<>();
    private ArrayList<AllComplains> valuesForEmployee = new ArrayList<>();
    ArrayList<String> allDept;


    @BindView(R.id.graphs)
    ViewPager viewPagerGraphs;


    @BindView(R.id.graphsDept)
    ViewPager viewPagerDept;


    @BindView(R.id.spinnerDept)
    Spinner spinnerDept;


    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_description_andr_graph);

        if(unbinder == null){
            unbinder = ButterKnife.bind(this);
        }

        GraphSwipe graphSwipe = new GraphSwipe(this);
        viewPagerGraphs.setAdapter(graphSwipe);


        toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        settingupValues();
    }




    private void settingupValues() {
//        fetchComplains();
        data = getIntent().getExtras();
        name = "Complaints Charts";
        description = "Complaints";


        allDept = new ArrayList<>();
        allDept.clear();
        allDept.add(Constants.ENGINEERING);
        allDept.add(Constants.ADMINISTRATION);
        allDept.add(Constants.ACCOUNT);
        allDept.add(Constants.REVENUE);
        allDept.add(Constants.TECHNICAL);
        allDept.add(Constants.SANITATION);
        allDept.add(Constants.ENGINA);

        ArrayAdapter adapter = new ArrayAdapter(this, android.R.layout.simple_list_item_1, allDept);
        spinnerDept.setAdapter(adapter);

        spinnerDept.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                                fetchComplains(spinnerDept.getSelectedItem().toString());
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

//        spinnerDept.setOnItemClickListener(new AdapterView.OnItemClickListener() {
//            @Override
//            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
//                fetchComplains(spinnerDept.getSelectedItem().toString());
//            }
//        });

        getSupportActionBar().setTitle(name);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
//        mText.setText(description);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                onBackPressed();
                return true;
        }
        return super.onOptionsItemSelected(item);

    }
    private void fetchComplains(String departments) {
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(Constants.REST_API)
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        JsonApiHolder service = retrofit.create(JsonApiHolder.class);

        Call<List<AllComplains>> call = service.getTotalCoplainsByDepartment(departments);

        call.enqueue(new Callback<List<AllComplains>>() {
            @Override
            public void onResponse(Call<List<AllComplains>> call, Response<List<AllComplains>> response) {
                if(!response.isSuccessful()){
                    Log.d(TAG, "onResponse: Failed!");
                    return;
                }
                Log.d(TAG, "onResponse: "+ response.body());
                List<AllComplains>  list = response.body();
                valuesForNew.clear();
                valuesForPending.clear();
                valuesForResolved.clear();
                for(AllComplains all: list){
                    if(all.getComplain_status().equals(Constants.COMPLAINS_RESOLVED)){
                        valuesForResolved.add(all);
                    }else if(all.getComplain_status().equals(Constants.COMPLAINS_NEW)) {
                        valuesForNew.add(all);
                    }else {
                        valuesForPending.add(all);
                    }
                }
                Log.d(TAG, "onResponse: sizeResolved" + valuesForResolved.size());
                Log.d(TAG, "onResponse: sizePending" + valuesForPending.size());
                Log.d(TAG, "onResponse: sizeNew" + valuesForNew.size());
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
        setupPagerAdapter(valuesForGraphs, valuesForNew, valuesForPending, valuesForResolved);


    }

    private void setupPagerAdapter(ArrayList<Integer> valuesForGraphs, ArrayList<AllComplains> valuesForNew, ArrayList<AllComplains> valuesForPending, ArrayList<AllComplains> valuesForResolved) {
        GraphSwipeForDeptEngineering adapter = new GraphSwipeForDeptEngineering(getApplicationContext(),
                valuesForGraphs,
                valuesForNew,
                valuesForPending,
                valuesForResolved
                );
        viewPagerDept.setAdapter(adapter);
    }

    @Override
    protected void attachBaseContext(Context newBase) {
        super.attachBaseContext(ViewPumpContextWrapper.wrap(newBase));
    }
}
