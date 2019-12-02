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
import android.widget.Spinner;

import com.example.complaintsystembeta.R;
import com.example.complaintsystembeta.adapter.AllComplainsAdapter;
import com.example.complaintsystembeta.adapter.AutoCompleteAdapter;
import com.example.complaintsystembeta.adapter.ConsumerComplaints;
import com.example.complaintsystembeta.adapter.OnlyForwardAdapter;
import com.example.complaintsystembeta.adapter.SwipeToArchiveCallback;
import com.example.complaintsystembeta.constants.Constants;
import com.example.complaintsystembeta.interfaace.JsonApiHolder;
import com.example.complaintsystembeta.model.AllComplains;
import com.example.complaintsystembeta.model.Employee;
import com.google.android.gms.vision.text.Line;

import java.util.ArrayList;
import java.util.Calendar;
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

public class ManagingComplaints extends AppCompatActivity {
    private static final String TAG = "ManagingComplaints";
    private List<AllComplains> allComplains = new ArrayList<>();
    private List<Employee> allEmployee = new ArrayList<>();
    private List<AllComplains> allComplainsFilter = new ArrayList<>();
    private RecyclerView recyclerView;
    private String complainData = "", desId, userName;
    private Bundle data;
    private DatePickerDialog.OnDateSetListener mDateListenerTo, mDateListenerFrom;

    private Unbinder unbinder;






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


    }


    @Override
    protected void onStart() {
        super.onStart();
        redirectAdapters();
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

            default:
                Log.d(TAG, "redirectAdapters: No Data louded");
                break;
        }
    }

    private void fetchNewComplains() {
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(Constants.REST_API)
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        JsonApiHolder service = retrofit.create(JsonApiHolder.class);

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
            }

            @Override
            public void onFailure(Call<List<AllComplains>> call, Throwable t) {

            }
        });

    }


    private void fetchPendingComplains() {
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(Constants.REST_API)
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        JsonApiHolder service = retrofit.create(JsonApiHolder.class);

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
            }

            @Override
            public void onFailure(Call<List<AllComplains>> call, Throwable t) {

            }
        });

    }


    private void fetchResolvedComplains() {
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("http://192.168.43.31:3000/api/")
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        JsonApiHolder service = retrofit.create(JsonApiHolder.class);

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
            }

            @Override
            public void onFailure(Call<List<AllComplains>> call, Throwable t) {

            }
        });

    }


    private void fetchForwardComplains() {
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("http://192.168.43.31:3000/api/")
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        JsonApiHolder service = retrofit.create(JsonApiHolder.class);

        Call<List<AllComplains>> call = service.getTotalForwardsComplains(desId);

        call.enqueue(new Callback<List<AllComplains>>() {
            @Override
            public void onResponse(Call<List<AllComplains>> call, Response<List<AllComplains>> response) {
                if(response.isSuccessful()){
                    allComplains = response.body();
                    Log.d(TAG, "onResponse: " + allComplains.size());
                    setupAdapterForward();
                }
            }

            @Override
            public void onFailure(Call<List<AllComplains>> call, Throwable t) {

            }
        });




    }

    private void setupAdapterForward() {
        OnlyForwardAdapter allComplainsAdapter  = new OnlyForwardAdapter(allComplains, this, desId, userName);
        recyclerView.setHasFixedSize(true);
        recyclerView.setAdapter(allComplainsAdapter);
    }

    private void fetchAllComplains() {
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(Constants.REST_API)
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        JsonApiHolder service = retrofit.create(JsonApiHolder.class);

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
            }

            @Override
            public void onFailure(Call<List<AllComplains>> call, Throwable t) {

            }
        });

    }
    private void setupAdapter(List<AllComplains> allComplains) {
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
            showDialog(this, "");
        }


        return super.onOptionsItemSelected(item);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_search, menu);
        MenuItem menuItem = menu.findItem(R.id.search);


        SearchView searchView = (SearchView) menuItem.getActionView();

        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                newText = newText.toLowerCase();
                allComplainsFilter.clear();
                for (int i = 0; i < allComplains.size(); i++) {
                    if(allComplains.get(i).getComplain_status().toLowerCase().contains(newText)
                            || allComplains.get(i).getComplain_status().toLowerCase().contains(newText)
                            || allComplains.get(i).getCreated_us().toLowerCase().contains(newText)
                            || allComplains.get(i).getComplain_body().toLowerCase().contains(newText)
                    ){
                        allComplainsFilter.add(allComplains.get(i));
                    }
                }
                setupAdapter(allComplainsFilter);
                return false;
            }
        });


        return super.onCreateOptionsMenu(menu);
    }

    private void getDataFromServer(String dateTo, String dateFrom, String status) {
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(Constants.REST_API)
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        JsonApiHolder service = retrofit.create(JsonApiHolder.class);

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
