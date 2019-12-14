package com.example.complaintsystembeta.ui.Employees;


import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.complaintsystembeta.R;
import com.example.complaintsystembeta.adapter.AllEmployeesAdapter;
import com.example.complaintsystembeta.constants.Constants;
import com.example.complaintsystembeta.interfaace.JsonApiHolder;
import com.example.complaintsystembeta.model.Employee;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * A simple {@link Fragment} subclass.
 */
public class AllEmployee extends Fragment {
    private static final String TAG = "AllEmployee";
    private View view;
    private Unbinder unbinder;
    private LinearLayoutManager linearLayoutManager;
    private ArrayList<Employee> allEmployees = new ArrayList<>();


    @BindView(R.id.recyclerForAllEmployees)
    RecyclerView recyclerView;



    public AllEmployee() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_all_employee, container, false);
        unbinder = ButterKnife.bind(this, view);
        linearLayoutManager = new LinearLayoutManager(getContext());
        linearLayoutManager.setOrientation(RecyclerView.VERTICAL);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(linearLayoutManager);
        getEmployees();
        return view;
    }


    private void getEmployees() {
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(Constants.REST_API)
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        JsonApiHolder service = retrofit.create(JsonApiHolder.class);

        Call<List<Employee>> call = service.getEmployee();
        call.enqueue(new Callback<List<Employee>>() {
            @Override
            public void onResponse(Call<List<Employee>> call, Response<List<Employee>> response) {
                if(!response.isSuccessful()){
                    Log.d(TAG, "onResponseUnsuccefull: " + response.message());
                    return;
                }else {
                    Log.d(TAG, "onResponse AutoTextView: " + response.message());
                    allEmployees = (ArrayList<Employee>) response.body();
                    Log.d(TAG, "onResponse: " + allEmployees.size());
                    setupAdaper(allEmployees);
                }
            }

            @Override
            public void onFailure(Call<List<Employee>> call, Throwable t) {
                Log.d(TAG, "onFailure: " + t.getMessage());
            }
        });
    }

    private void setupAdaper(ArrayList<Employee> allEmployees) {
        AllEmployeesAdapter allEmployeesAdapter = new AllEmployeesAdapter(allEmployees);
        recyclerView.setAdapter(allEmployeesAdapter);
    }


}
