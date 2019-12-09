package com.example.complaintsystembeta.ui.complaints;

import android.content.Context;
import android.os.Build;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.viewpager.widget.PagerAdapter;

import com.example.complaintsystembeta.R;
import com.example.complaintsystembeta.constants.Constants;
import com.example.complaintsystembeta.interfaace.JsonApiHolder;
import com.example.complaintsystembeta.model.AllComplains;
import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.components.Description;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.data.PieEntry;
import com.github.mikephil.charting.formatter.IValueFormatter;
import com.github.mikephil.charting.formatter.ValueFormatter;
import com.github.mikephil.charting.interfaces.datasets.IBarDataSet;
import com.github.mikephil.charting.interfaces.datasets.ILineDataSet;
import com.github.mikephil.charting.utils.ColorTemplate;
import com.github.mikephil.charting.utils.ViewPortHandler;
import com.google.android.gms.vision.L;
import com.google.android.gms.vision.text.Line;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class GraphSwipeForDeptEngineering extends PagerAdapter {
    private static final String TAG = "GraphSwipe";
    Context mContext;
    BarChart  mBarChart;
    PieChart pieChart;
    LineChart lineChart;






    private ArrayList<BarEntry> barEntries = new ArrayList<>();
    private ArrayList<Integer> valuesForGraphs = new ArrayList<>();
    private ArrayList<AllComplains> valuesForPending = new ArrayList<>();
    private ArrayList<AllComplains> valuesForNew = new ArrayList<>();
    private ArrayList<AllComplains> valuesForResolved = new ArrayList<>();
    private ArrayList<AllComplains> valuesForEmployee = new ArrayList<>();
    private ArrayList<Integer> arrayListForGraph = new ArrayList<>();

    private List<String> urls;
    private ArrayList<String> list  = new ArrayList<>();



    GraphSwipeForDeptEngineering(Context c,
                                 ArrayList<Integer> valuesForGraphs,
                                 ArrayList<AllComplains> valuesForNew,
                                 ArrayList<AllComplains> valuesForPending,
                                 ArrayList<AllComplains> valuesForResolved
                                 ){
        this.mContext = c;
        this.valuesForGraphs = valuesForGraphs;
        this.valuesForNew = valuesForNew;
        this.valuesForPending = valuesForPending;
        this.valuesForResolved = valuesForResolved;


    }

    @Override
    public void destroyItem(@NonNull ViewGroup container, int position, @NonNull Object object) {
        container.removeView((View) object);
    }

    @RequiresApi(api = Build.VERSION_CODES.M)
    @NonNull
    @Override
    public Object instantiateItem(@NonNull ViewGroup container, int position) {
//        fetchComplains();
        if(position == 0){
            mBarChart = new BarChart(mContext);
            settingBarGraphForDept();
            container.addView(mBarChart);
            return mBarChart;
        }else if(position == 1){
            pieChart = new PieChart(mContext);
            settingPieGraph();
            container.addView(pieChart);
            return pieChart;

        }else if(position == 2){
            lineChart = new LineChart(mContext);
            settingLineChart();
            container.addView(lineChart);
            return lineChart;
        }
        return null;
    }

    @Override
    public int getCount() {
        return 3;
    }



    @Override
    public boolean isViewFromObject(@NonNull View view, @NonNull Object o) {
        return view == o;
    }
    @RequiresApi(api = Build.VERSION_CODES.M)
    private void settingBarGraphForDept() {
        Log.d(TAG, "settingGraph: " + arrayListForGraph);
//        for (int i = 0; i < 10; i++) {
//            list.add(String.valueOf(i));
//        }
//        for (int i = 0; i < valuesForGraphs.size(); i++) {
//            barEntries.add(new BarEntry(i, Float.parseFloat(String.valueOf(valuesForGraphs.get(i))), list.get(i)));
//        }

        List<BarEntry> value0  = new ArrayList<>();
        List<BarEntry> value1  = new ArrayList<>();
        List<BarEntry> value2  = new ArrayList<>();
        ArrayList<String> label  = new ArrayList<>();

        value0.add(new BarEntry(0,valuesForGraphs.get(0), "Resolved"));
        value1.add(new BarEntry(1,valuesForGraphs.get(1), "Pending"));
        value2.add(new BarEntry(2,valuesForGraphs.get(2), "New"));

        BarDataSet dataSetRevenue0 = new BarDataSet(value0, "Resolved");
        BarDataSet dataSetRevenue1 = new BarDataSet(value1, "Pending");
        BarDataSet dataSetRevenue2 = new BarDataSet(value2, "New");
        dataSetRevenue0.setColor(mContext.getColor(R.color.resolved));
        dataSetRevenue1.setColor(mContext.getColor(R.color.pending));
        dataSetRevenue2.setColor(mContext.getColor(R.color.new_complains));




        mBarChart.setTouchEnabled(true);
        mBarChart.setScaleEnabled(true);
        mBarChart.setDragEnabled(true);
        List<IBarDataSet> bars = new ArrayList<>();
        bars.add(dataSetRevenue0);
        bars.add(dataSetRevenue1);
        bars.add(dataSetRevenue2);
        BarData data = new BarData(bars);
        mBarChart.setData(data);
        mBarChart.setFitBars(true);


        Description description = new Description();
        description.setText(mContext.getString(R.string.description));
        mBarChart.setDescription(description);
        mBarChart.invalidate();
    }
    private void settingPieGraph() {
        List<PieEntry> value  = new ArrayList<>();

        value.add(new PieEntry(valuesForGraphs.get(0), "Resolved"));
        value.add(new PieEntry(valuesForGraphs.get(1), "Pending"));
        value.add(new PieEntry(valuesForGraphs.get(2), "New"));




        PieDataSet pieDataSet = new PieDataSet(value, "Complains");
        PieData pieData = new PieData(pieDataSet);
        pieDataSet.setColors(new int[]{R.color.resolved, R.color.pending, R.color.new_complains}, mContext);


        pieChart.setData(pieData);

    }

    private void settingLineChart(){
        LineDataSet lineDataSet = new LineDataSet(datavalue(), "Complaints");
        ArrayList<ILineDataSet> dataSets = new ArrayList<>();
        dataSets.add(lineDataSet);

        LineData line = new LineData(dataSets);
        lineChart.setData(line);
        lineChart.invalidate();
    }

    private ArrayList<Entry> datavalue(){
        ArrayList<Entry> dataVals = new ArrayList();
        dataVals.add(new Entry(0, valuesForGraphs.get(0)));
        dataVals.add(new Entry(1, valuesForGraphs.get(1)));
        dataVals.add(new Entry(2, valuesForGraphs.get(2)));
        return dataVals;
    }

}
