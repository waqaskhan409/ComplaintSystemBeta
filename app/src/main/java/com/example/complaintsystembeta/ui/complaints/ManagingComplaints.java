package com.example.complaintsystembeta.ui.complaints;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ProgressBar;
import android.widget.Spinner;

import com.example.complaintsystembeta.BaseActivity;
import com.example.complaintsystembeta.R;
import com.example.complaintsystembeta.adapter.AllComplainsAdapter;
import com.example.complaintsystembeta.adapter.AutoCompleteAdapter;
import com.example.complaintsystembeta.adapter.ConsumerComplaints;
import com.example.complaintsystembeta.adapter.OnlyForwardAdapter;
import com.example.complaintsystembeta.adapter.SwipeToArchiveCallback;
import com.example.complaintsystembeta.constants.Constants;
import com.example.complaintsystembeta.constants.RestApi;
import com.example.complaintsystembeta.interfaace.JsonApiHolder;
import com.example.complaintsystembeta.model.AllComplains;
import com.example.complaintsystembeta.model.Employee;
import com.example.complaintsystembeta.ui.dialogues.BottomSheetDialogueCompose;
import com.example.complaintsystembeta.ui.dialogues.BottomSheetDialogueFilterSearch;
import com.google.android.gms.vision.text.Line;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.DayOfWeek;
import java.time.temporal.WeekFields;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;
import io.github.inflationx.viewpump.ViewPumpContextWrapper;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class ManagingComplaints extends BaseActivity {
    private static final String TAG = "ManagingComplaints";
    private List<AllComplains> allComplains = new ArrayList<>();
    private List<Employee> allEmployee = new ArrayList<>();
    private List<AllComplains> allComplainsFilter = new ArrayList<>();
    private List<AllComplains> allComplainsFilterMore = new ArrayList<>();
    private RecyclerView recyclerView;
    private String complainData = "", desId, userName;
    private Bundle data;
    private DatePickerDialog.OnDateSetListener mDateListenerTo, mDateListenerFrom;

    private Unbinder unbinder;
    private SearchView searchView;
    private String decisionForwardToOrFrom;


    @BindView(R.id.complainsBar)
    ProgressBar complainsBar;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_managing_complaints);
        if(unbinder == null){
            unbinder = ButterKnife.bind(this);
        }
        data = getIntent().getExtras();
        complainData = data.getString(getString(R.string.complain_redirect));
        userName = data.getString(getString(R.string.permanentlogin_name));
        desId = data.getString(Constants.PREVELDGES_ON_FORWARD);
        Log.d(TAG, "onCreate: Designation id" + desId);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);



        recyclerView = findViewById(R.id.recyclerviewForComplains);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        linearLayoutManager.setOrientation(RecyclerView.VERTICAL);
        recyclerView.setLayoutManager(linearLayoutManager);



        Log.d(TAG, "onCreate: " + Constants.COMPLAINS_NEW);
        Log.d(TAG, "onCreate: " + Constants.COMPLAINS_PENDING);
        Log.d(TAG, "onCreate: " + Constants.COMPLAINS_RESOLVED);
        Log.d(TAG, "onCreate: " + complainData);

//        simpleSearchView.setSuggestionsAdapter(new AutoCompleteAdapter(this, allEmployee));
        redirectAdapters();


    }


    @Override
    protected void onStart() {
        super.onStart();
        checkConnection();
        if(!checkWifiOnAndConnected()  && !checkMobileDataOnAndConnected()){
            showSnackBarWifi(getString(R.string.wifi_message));
        }else {
            checkConnection();
        }
    }

    private void redirectAdapters() {
        switch (complainData){
            case Constants.ALL_COMPLAINS:
                fetchAllComplains();
                break;

            case Constants.NEW_COMPLAINS:
                fetchNewComplains();
                break;

            case Constants.PENDING_COMPLAINS:
                fetchPendingComplains();
                break;

            case Constants.RESOLVED_COMPLAINS:
                fetchResolvedComplains();
                break;

            case Constants.FORWARD_COMPLAINS:
                fetchForwardComplains();

                break;

            case Constants.FORWARD_FROM:
                fetchForwardFromComplains();
                break;

            case Constants.DELAY:
                fetchTotalDelays();
                break;

            default:
                Log.d(TAG, "redirectAdapters: No Data loaded");
                break;
        }
    }

    private void fetchNewComplains() {
        JsonApiHolder service = RestApi.getApi();
        Call<List<AllComplains>> call = service.getComplains();

        call.enqueue(new Callback<List<AllComplains>>() {
            @Override
            public void onResponse(Call<List<AllComplains>> call, Response<List<AllComplains>> response) {
                allComplains.clear();
                if(!response.isSuccessful()){
                    Log.d(TAG, "onResponse: Failed!");
                    return;
                }
                Log.d(TAG, "onResponse: "+ response.body());
                List<AllComplains>  list = response.body();

                for(AllComplains all: list){
                    if(all.getComplain_status().equals(Constants.COMPLAINS_NEW)){
                        allComplains.add(all);
                    }
                }

                setupAdapter(allComplains);
                complainsBar.setVisibility(View.GONE);
            }

            @Override
            public void onFailure(Call<List<AllComplains>> call, Throwable t) {

            }
        });

    }


    private void fetchPendingComplains() {
        JsonApiHolder service = RestApi.getApi();
        Call<List<AllComplains>> call = service.getComplains();

        call.enqueue(new Callback<List<AllComplains>>() {
            @Override
            public void onResponse(Call<List<AllComplains>> call, Response<List<AllComplains>> response) {
                allComplains.clear();
                if(!response.isSuccessful()){
                    Log.d(TAG, "onResponse: Failed!");
                    return;
                }
                Log.d(TAG, "onResponse: "+ response.body());
                List<AllComplains>  list = response.body();

                for(AllComplains all: list){
                    if(all.getComplain_status().equals(Constants.COMPLAINS_PENDING) ||
                            all.getComplain_status().equals(Constants.COMPLAINS_IN_PROCESS)
                    ){
                        allComplains.add(all);
                    }
                }

                setupAdapter(allComplains);
                complainsBar.setVisibility(View.GONE);

            }

            @Override
            public void onFailure(Call<List<AllComplains>> call, Throwable t) {

            }
        });

    }


    private void fetchResolvedComplains() {
        JsonApiHolder service = RestApi.getApi();


        Call<List<AllComplains>> call = service.getComplains();

        call.enqueue(new Callback<List<AllComplains>>() {
            @Override
            public void onResponse(Call<List<AllComplains>> call, Response<List<AllComplains>> response) {
                allComplains.clear();
                if(!response.isSuccessful()){
                    Log.d(TAG, "onResponse: Failed!");
                    return;
                }
                Log.d(TAG, "onResponse: "+ response.body());
                List<AllComplains>  list = response.body();
                for(AllComplains all: list){
                    if(all.getComplain_status().equals(Constants.COMPLAINS_RESOLVED)){
                        allComplains.add(all);
                    }
                }

                allComplains.toArray();
                setupAdapter(allComplains);
                complainsBar.setVisibility(View.GONE);

            }

            @Override
            public void onFailure(Call<List<AllComplains>> call, Throwable t) {

            }
        });

    }


    private void fetchForwardComplains() {
        JsonApiHolder service = RestApi.getApi();
        Call<List<AllComplains>> call = service.getTotalForwardsComplains(desId);
        call.enqueue(new Callback<List<AllComplains>>() {
            @Override
            public void onResponse(Call<List<AllComplains>> call, Response<List<AllComplains>> response) {
                if(response.isSuccessful()){
                    allComplains = response.body();
                    Log.d(TAG, "onResponse: " + allComplains.size());
                    decisionForwardToOrFrom = Constants.FORWARD_TO;
                    setupAdapterForward((ArrayList<AllComplains>) allComplains);
                    complainsBar.setVisibility(View.GONE);

                }
            }

            @Override
            public void onFailure(Call<List<AllComplains>> call, Throwable t) {

            }
        });




    }
    private void fetchForwardFromComplains() {

        JsonApiHolder service = RestApi.getApi();
        Call<List<AllComplains>> call = service.getTotalForwardsFromComplains(desId);

        call.enqueue(new Callback<List<AllComplains>>() {
            @Override
            public void onResponse(Call<List<AllComplains>> call, Response<List<AllComplains>> response) {
                if(response.isSuccessful()){
                    allComplains = response.body();
                    Log.d(TAG, "onResponse: " + allComplains.size());
                    decisionForwardToOrFrom = Constants.FORWARD_FROM;
                    setupAdapterForward((ArrayList<AllComplains>) allComplains);
                    complainsBar.setVisibility(View.GONE);

                }
            }

            @Override
            public void onFailure(Call<List<AllComplains>> call, Throwable t) {

            }
        });
    }

    private void fetchTotalDelays() {
        JsonApiHolder service = RestApi.getApi();
        Call<List<AllComplains>> call = service.getTotalForwardsFromComplainsAllDelays(desId);

        call.enqueue(new Callback<List<AllComplains>>() {
            @Override
            public void onResponse(Call<List<AllComplains>> call, Response<List<AllComplains>> response) {
                if(!response.isSuccessful()){
                    Log.d(TAG, "onResponse: Failed!");
                    return;
                }
                Log.d(TAG, "onResponse: "+ response.body());
                List<AllComplains> list = response.body();
                decisionForwardToOrFrom = Constants.DELAY;
                setupAdapterForward((ArrayList<AllComplains>) list);
                complainsBar.setVisibility(View.GONE);

            }

            @Override
            public void onFailure(Call<List<AllComplains>> call, Throwable t) {

            }
        });

    }

    private void setupAdapterForward(ArrayList<AllComplains> allComplainsFilter) {
        for (AllComplains allComplains1: allComplainsFilter) {
            try {
                Log.d(TAG, "setupAdapter: " + allComplains1.getCreated_us());
                if(allComplains1.getCreated_us() !=  null) {
                    String days = getDays(allComplains1.getCreated_us());
                    if(days.equals("0")){

                        allComplains1.setDays("Today");
                    }else if(days.equals("1")){
                        allComplains1.setDays("Yesterday");

                    }else {
                        allComplains1.setDays(getDays(allComplains1.getCreated_us()) + " Days ago");
                    }
                }
            } catch (ParseException e) {
                e.printStackTrace();
            }
        }
        OnlyForwardAdapter allComplainsAdapter  = new OnlyForwardAdapter(allComplainsFilter, this, desId, userName, decisionForwardToOrFrom);
        recyclerView.setHasFixedSize(true);
        recyclerView.setAdapter(allComplainsAdapter);
    }

    private void fetchAllComplains() {
        JsonApiHolder service = RestApi.getApi();
        Call<List<AllComplains>> call = service.getComplains();
        call.enqueue(new Callback<List<AllComplains>>() {
            @Override
            public void onResponse(Call<List<AllComplains>> call, Response<List<AllComplains>> response) {
                allComplains.clear();
                if(!response.isSuccessful()){
                    Log.d(TAG, "onResponse: Failed!");
                    return;
                }
                Log.d(TAG, "onResponse: "+ response.body());
                List<AllComplains>  list = response.body();
                allComplains = list;
                setupAdapter(allComplains);
                complainsBar.setVisibility(View.GONE);

            }

            @Override
            public void onFailure(Call<List<AllComplains>> call, Throwable t) {

            }
        });

    }

    private String getDays(String created_us) throws ParseException {
        String[] arr = created_us.split("T");
       /* int year = Integer.parseInt(createdDate[0]);
        int month = Integer.parseInt(createdDate[1]);
        int day = Integer.parseInt(createdDate[2]);

        int currentMonth =  Calendar.getInstance().get(Calendar.MONTH)+1;
        int currentDay =  Calendar.getInstance().get(Calendar.DAY_OF_MONTH);
        int currentYear =  Calendar.getInstance().get(Calendar.YEAR);*/

        Date dateEarly=new SimpleDateFormat("yyyy-MM-dd").parse(arr[0]);
        Date dateLater = new Date();

        long a = (dateLater.getTime() - dateEarly.getTime()) / (24 * 60 * 60 * 1000);
        return String.valueOf(a);
    }

    private void setupAdapter(List<AllComplains> allComplains) {
        for (AllComplains allComplains1: allComplains) {
            try {
                Log.d(TAG, "setupAdapter: " + allComplains1.getCreated_us());
                if(allComplains1.getCreated_us() !=  null) {
                    String days = getDays(allComplains1.getCreated_us());
                    if(days.equals("0")){

                        allComplains1.setDays("Today");
                    }else if(days.equals("1")){
                        allComplains1.setDays("Yesterday");

                    }else {
                    allComplains1.setDays(getDays(allComplains1.getCreated_us()) + " Days ago");
                    }
                }
            } catch (ParseException e) {
                e.printStackTrace();
            }
        }
        AllComplainsAdapter allComplainsAdapter  = new AllComplainsAdapter(allComplains, this, "");
        recyclerView.setHasFixedSize(true);
        recyclerView.setAdapter(allComplainsAdapter);
        ItemTouchHelper itemTouchHelper = new
                ItemTouchHelper(new SwipeToArchiveCallback(allComplainsAdapter));
        itemTouchHelper.attachToRecyclerView(recyclerView);
    }

    public void datePickerFrom(){
        Calendar calendar = Calendar.getInstance();
        int year = calendar.get(Calendar.YEAR);
        int month = calendar.get(Calendar.MONTH);
        int day = calendar.get(Calendar.DAY_OF_MONTH);

        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.N) {
            DatePickerDialog dialog = new DatePickerDialog(this,
                    android.R.style.Theme_Holo_Light_Dialog_MinWidth,
                    mDateListenerFrom,
                    year, month, day
            );

            dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
            dialog.show();

        }

    }


    public void datePickerTo(){
        Calendar calendar = Calendar.getInstance();
        int year = calendar.get(Calendar.YEAR);
        int month = calendar.get(Calendar.MONTH);
        int day = calendar.get(Calendar.DAY_OF_MONTH);

        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.N) {
            DatePickerDialog dialog = new DatePickerDialog(this,
                    android.R.style.Theme_Holo_Light_Dialog_MinWidth,
                    mDateListenerTo,
                    year, month, day
            );

            dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
            dialog.show();

        }

    }


    public void showDialog(Activity activity, String msg){
        final Dialog dialog = new Dialog(activity);
        dialog.setContentView(R.layout.dialog_advance_search);

        dialog.setTitle(msg);

        Spinner spinnerAdvance = dialog.findViewById(R.id.spinnerAdvance);

        List<String> list = new ArrayList<>();
        list.add(Constants.ALL_COMPLAINS);
        list.add(Constants.COMPLAINS_NEW);
        list.add(Constants.COMPLAINS_PENDING);
        list.add(Constants.COMPLAINS_RESOLVED);
        ArrayAdapter adapter = new ArrayAdapter(this, android.R.layout.simple_list_item_1, list);
        spinnerAdvance.setAdapter(adapter);

        Button cancel, submit;
        cancel = dialog.findViewById(R.id.cancel);
        submit = dialog.findViewById(R.id.submit);


        EditText edTo, edFrom;

        edTo = dialog.findViewById(R.id.dateTo);
        edFrom = dialog.findViewById(R.id.dateFrom);

        edTo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                datePickerTo();
            }
        });

        edFrom.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                datePickerFrom();
            }
        });


        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d(TAG, "onClick: Submited");
                String dateTo, dateFrom, status;
                dateTo = edTo.getText().toString().trim();
                dateFrom = edFrom.getText().toString().trim();
                status = spinnerAdvance.getSelectedItem().toString();
                if(dateTo.equals("") || dateFrom.equals("")){
                    Log.d(TAG, "onClick: dates is not selected");
                    dialog.dismiss();
                }else {
                    getDataFromServer(dateTo, dateFrom, status);
                    dialog.dismiss();
                }

            }
        });

        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d(TAG, "onClick: cancel");

                dialog.dismiss();
            }
        });
        dialog.show();

        mDateListenerTo = new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                month = month + 1;
                Log.d(TAG, "onDateSet: " + year + "-" + month + "-" + dayOfMonth);
                edTo.setText(year + "-" + month + "-" + dayOfMonth);
            }
        };
        mDateListenerFrom = new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                month = month + 1;
                Log.d(TAG, "onDateSet: " + year + "-" + month + "-" + dayOfMonth);
                edFrom.setText(year + "-" + month + "-" + dayOfMonth);
            }
        };

    }


    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {

        switch (item.getItemId()) {
            case android.R.id.home:
                onBackPressed();
                return true;
        }

        if(item.getItemId() == R.id.advanceSearch){
//            showDialog(this, "");
            BottomSheetDialogueFilterSearch bottomSheetDialogueFilterSearch = new BottomSheetDialogueFilterSearch();
            bottomSheetDialogueFilterSearch.show(getSupportFragmentManager(), Constants.TAG_DIALOGUE_BOTTOM_SHEET);

        }else if(item.getItemId() == R.id.today){
            filterListToday();
        }else if(item.getItemId() == R.id.weeklyComplains){
            filterListWeekly();
        }else if(item.getItemId() == R.id.monthlyComlains){
            filterListMonthly();

        }


        return super.onOptionsItemSelected(item);
    }

    private void filterListMonthly() {
        allComplainsFilter.clear();
        int currentMonth =  Calendar.getInstance().get(Calendar.MONTH)+1;
        int currentYear =  Calendar.getInstance().get(Calendar.YEAR);
        Log.d(TAG, "filterListMonthly: " + currentMonth);
        String startWeekly, endWeekly;
        startWeekly = currentYear + "-" + currentMonth + "-1" ;
        endWeekly = currentYear + "-" + currentMonth +"-31" ;

        if(decisionForwardToOrFrom != null) {
            if (decisionForwardToOrFrom.equals(Constants.FORWARD_TO) || decisionForwardToOrFrom.equals(Constants.FORWARD_FROM)) {
//                searchViewMethod();
                searchView.setQuery(currentYear + "-" + currentMonth + "-", true);
                return;
            }
        }
        Log.d(TAG, "filterListMonthly: " + startWeekly);
        Log.d(TAG, "filterListMonthly: " + endWeekly);

        switch (complainData){
            case Constants.ALL_COMPLAINS:
                getDataFromServer(startWeekly, endWeekly, Constants.ALL_COMPLAINS);
                break;

            case Constants.NEW_COMPLAINS:
                getDataFromServer(startWeekly, endWeekly, Constants.COMPLAINS_NEW);
                break;

            case Constants.PENDING_COMPLAINS:
                getDataFromServer(startWeekly, endWeekly, Constants.COMPLAINS_PENDING);
                break;

            case Constants.RESOLVED_COMPLAINS:
                getDataFromServer(startWeekly, endWeekly, Constants.COMPLAINS_RESOLVED);
                break;

            default:
                Log.d(TAG, "redirectAdapters: No Data loaded");
                break;
        }



    }

    private void filterListWeekly() {
//        int week = cal.get(Calendar.WEEK_OF_YEAR);
//        Calendar cal = Calendar.getInstance();
//        int currentMonth =  Calendar.getInstance().get(Calendar.YEAR);
//        int currentYear =  Calendar.getInstance().get(Calendar.MONTH);
//        int currentDay =  Calendar.getInstance().get(Calendar.DAY_OF_MONTH);
//
//        cal.set(currentYear, currentMonth, currentDay);
//
//        // "calculate" the start date of the week
//        Calendar first = (Calendar) cal.clone();
//        first.add(Calendar.DAY_OF_WEEK,
//                first.getFirstDayOfWeek() - first.get(Calendar.DAY_OF_WEEK));
//
//        // and add six days to the end date
//        Calendar last = (Calendar) first.clone();
//        last.add(Calendar.DAY_OF_YEAR, 6);
//
//        // print the result
//        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd");
//        Log.d(TAG, "filterListWeekly: "+ df.format(first.getTime()) + " -> " +
//                df.format(last.getTime()));
//        System.out.println();


        Date date = new Date();
        Calendar c = Calendar.getInstance();
        c.setTime(date);
        int dayOfWeek = c.get(Calendar.DAY_OF_WEEK) - c.getFirstDayOfWeek();
        c.add(Calendar.DAY_OF_MONTH, -dayOfWeek);

        Date weekStart = c.getTime();
// we do not need the same day a week after, that's why use 6, not 7
        c.add(Calendar.DAY_OF_MONTH, 6);
        Date weekEnd = c.getTime();
        DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        String startWeekly, endWeekly;
        startWeekly = dateFormat.format(weekStart);
        endWeekly = dateFormat.format(weekEnd);
        if(decisionForwardToOrFrom != null) {
            if (decisionForwardToOrFrom.equals(Constants.FORWARD_TO) || decisionForwardToOrFrom.equals(Constants.FORWARD_FROM)) {
                searchView.setQuery(startWeekly,true);
                return;
            }
        }
        Log.d(TAG, "filterListWeekly: start day:" + startWeekly);
        Log.d(TAG, "filterListWeekly: start End:" + endWeekly);

        switch (complainData){
            case Constants.ALL_COMPLAINS:
                getDataFromServer(startWeekly, endWeekly, Constants.ALL_COMPLAINS);

                break;

            case Constants.NEW_COMPLAINS:
                getDataFromServer(startWeekly, endWeekly, Constants.COMPLAINS_NEW);

                break;

            case Constants.PENDING_COMPLAINS:
                getDataFromServer(startWeekly, endWeekly, Constants.COMPLAINS_PENDING);
                break;

            case Constants.RESOLVED_COMPLAINS:
                getDataFromServer(startWeekly, endWeekly, Constants.COMPLAINS_RESOLVED);

                break;
            default:
                Log.d(TAG, "redirectAdapters: No Data loaded");
                break;
        }


    }

    private void filterListToday() {
        allComplainsFilter.clear();
        DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        Date date = new Date();
        String dateString = dateFormat.format(date);
        Log.d(TAG, "filterListToday: " + dateString);
        searchView.setQuery(dateString,true);
//        searchViewMethod(dateString);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_search, menu);
        MenuItem menuItem = menu.findItem(R.id.search);


        searchView = (SearchView) menuItem.getActionView();

        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                newText = newText.toLowerCase();
                searchViewMethod(newText);
                return false;
            }
        });


        return super.onCreateOptionsMenu(menu);
    }

    private void searchViewMethod(String newText) {
        allComplainsFilter.clear();
        for (int i = 0; i < allComplains.size(); i++) {
            if(allComplains.get(i).getComplain_status().toLowerCase().contains(newText)
                    || allComplains.get(i).getAccount_number().toLowerCase().contains(newText)
                    || allComplains.get(i).getCreated_us().toLowerCase().contains(newText)
                    || allComplains.get(i).getComplain_body().toLowerCase().contains(newText)
                    || allComplains.get(i).getDays().toLowerCase().contains(newText)
            ){
                allComplainsFilter.add(allComplains.get(i));
            }
        }
        if(decisionForwardToOrFrom != null) {
            if (decisionForwardToOrFrom.equals(Constants.FORWARD_FROM) || decisionForwardToOrFrom.equals(Constants.FORWARD_TO)) {
                setupAdapterForward((ArrayList<AllComplains>) allComplainsFilter);
            } else {
                setupAdapter(allComplainsFilter);
            }
        }else {
            setupAdapter(allComplainsFilter);
        }
    }


    public void getDataFromServer(String dateTo, String dateFrom, String status) {
        JsonApiHolder service = RestApi.getApi();
        Call<List<AllComplains>> call = service.getSortedComplainsAgainstDateAndStatus(dateTo, dateFrom, status);

        call.enqueue(new Callback<List<AllComplains>>() {
            @Override
            public void onResponse(Call<List<AllComplains>> call, Response<List<AllComplains>> response) {
                allComplains.clear();
                if(!response.isSuccessful()){
                    Log.d(TAG, "onResponse: Failed!");
                    return;
                }
                Log.d(TAG, "onResponse: "+ response.body());
                List<AllComplains>  list = response.body();
                allComplains = list;
                setupAdapter(allComplains);
            }

            @Override
            public void onFailure(Call<List<AllComplains>> call, Throwable t) {

            }
        });
    }
    @Override
    protected void attachBaseContext(Context newBase) {
        super.attachBaseContext(ViewPumpContextWrapper.wrap(newBase));
    }

}
