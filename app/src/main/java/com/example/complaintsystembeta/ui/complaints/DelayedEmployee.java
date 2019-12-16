package com.example.complaintsystembeta.ui.complaints;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.os.Bundle;
import android.renderscript.AllocationAdapter;
import android.util.Log;
import android.view.MenuItem;

import com.example.complaintsystembeta.R;
import com.example.complaintsystembeta.adapter.AllEmployeesAdapter;
import com.example.complaintsystembeta.constants.Constants;
import com.example.complaintsystembeta.interfaace.JsonApiHolder;
import com.example.complaintsystembeta.model.Employee;
import com.example.complaintsystembeta.ui.Employees.AllEmployee;

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

public class DelayedEmployee extends AppCompatActivity {
    private static final String TAG = "DelayedEmployee";
    private Toolbar toolbar;
    private Unbinder unbinder;
    private LinearLayoutManager linearLayoutManager;


    @BindView(R.id.delayedEmployees)
    RecyclerView recyclerViewForDelayedEmployees;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_delayed_employee);
        if(unbinder == null){
            unbinder = ButterKnife.bind(this);
        }
        linearLayoutManager = new LinearLayoutManager(this);
        linearLayoutManager.setOrientation(RecyclerView.VERTICAL);
        recyclerViewForDelayedEmployees.setLayoutManager(linearLayoutManager);
        recyclerViewForDelayedEmployees.setHasFixedSize(true);

        toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        fetchTotalDelays();

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

    @Override
    protected void onStart() {
        super.onStart();
    }

    private void fetchTotalDelays() {
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(Constants.REST_API)
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        JsonApiHolder service = retrofit.create(JsonApiHolder.class);

        Call<List<Employee>> call = service.getTotalDelays();

        call.enqueue(new Callback<List<Employee>>() {
            @Override
            public void onResponse(Call<List<Employee>> call, Response<List<Employee>> response) {
                if(!response.isSuccessful()){
                    Log.d(TAG, "onResponse: Failed!");
                    return;
                }
                Log.d(TAG, "onResponse: "+ response.body());
                List<Employee> list = response.body();
                setAdapter((ArrayList<Employee>) list);
            }

            @Override
            public void onFailure(Call<List<Employee>> call, Throwable t) {

            }
        });

    }

    private void setAdapter(ArrayList<Employee> list) {
        AllEmployeesAdapter allEmployee = new AllEmployeesAdapter(list);
        recyclerViewForDelayedEmployees.setAdapter(allEmployee);
    }
    @Override
    protected void attachBaseContext(Context newBase) {
        super.attachBaseContext(ViewPumpContextWrapper.wrap(newBase));
    }
}
