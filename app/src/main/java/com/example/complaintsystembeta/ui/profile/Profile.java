package com.example.complaintsystembeta.ui.profile;


import android.content.Context;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.complaintsystembeta.R;
import com.example.complaintsystembeta.constants.Constants;
import com.example.complaintsystembeta.interfaace.JsonApiHolder;
import com.example.complaintsystembeta.model.AllComplains;
import com.example.complaintsystembeta.model.Employee;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;


/**
 * A simple {@link Fragment} subclass.
 */
public class Profile extends Fragment {
    private static final String TAG = "Profile";
    private View view;
    private String name, cnic, email, address, phone, employeeId;
    TextView nameTv, cnicTv, emailEd, addressEd, phoneEd;

    public Profile(String name, String cnic, String employeeId) {
        this.name = name;
        this.cnic = cnic;
        this.employeeId = employeeId;
        fetchComplains(employeeId);

    }

    public Profile() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_profile, container, false);
        nameTv = view.findViewById(R.id.name);
        cnicTv = view.findViewById(R.id.accountNumber);
        emailEd = view.findViewById(R.id.email);
        addressEd = view.findViewById(R.id.contactAddress);
        phoneEd = view.findViewById(R.id.contactNumber);

        Log.d(TAG, "onCreateView: " + employeeId);

//        settingValues();
        fetchComplains(employeeId);
        return view;
    }

    private void settingValues() {
        nameTv.setText(name);
        cnicTv.setText(cnic);
        emailEd.setText(email);
        addressEd.setText(address);
        phoneEd.setText(phone);
    }

    private void fetchComplains(String employeeId) {
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(Constants.REST_API)
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        JsonApiHolder service = retrofit.create(JsonApiHolder.class);

        Call<Employee> call = service.getSingleEmployeeForProfile(employeeId);

        call.enqueue(new Callback<Employee>() {
            @Override
            public void onResponse(Call<Employee> call, Response<Employee> response) {
                if(!response.isSuccessful()){
                    Log.d(TAG, "onResponse: Failed!");
                    return;
                }
                Log.d(TAG, "onResponse: "+ response.body().getEmail());
                Log.d(TAG, "onResponse: "+ response.body().getFather_name());
                Log.d(TAG, "onResponse: "+ response.body().getFull_name());
                email = response.body().getEmail();
//                phone = response.body().get();
//                email = response.body().getEmail();
                settingValues();

            }

            @Override
            public void onFailure(Call<Employee> call, Throwable t) {
                Log.d(TAG, "onFailure: " + t.getMessage());
            }
        });

    }

}
