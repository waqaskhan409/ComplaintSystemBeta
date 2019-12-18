package com.example.complaintsystembeta.ui.complaints;


import android.content.Intent;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.complaintsystembeta.R;
import com.example.complaintsystembeta.constants.Constants;
import com.example.complaintsystembeta.interfaace.JsonApiHolder;
import com.example.complaintsystembeta.model.AllComplains;
import com.example.complaintsystembeta.model.DelayEmployees;
import com.example.complaintsystembeta.model.Employee;
import com.example.complaintsystembeta.model.Forwards;
import com.example.complaintsystembeta.ui.EmployeeNavigation;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * A simple {@link Fragment} subclass.
 */
public class AllCatigoryComplainsFragment extends Fragment {
    private static final String TAG = "AllCatigoryComplainsFra";
    private View view;
    private Unbinder unbinder;
    private ArrayList<AllComplains> valuesForPending = new ArrayList<>();
    private ArrayList<AllComplains> valuesForNew = new ArrayList<>();
    private ArrayList<AllComplains> valuesForResolved = new ArrayList<>();
    private Bundle data;
    private String cnic, desId, userName, employeeId;


    @BindView(R.id.allComplains)
    LinearLayout linearLayoutAllComplains;

    @BindView(R.id.newComplains)
    LinearLayout linearLayoutNewComplains;

    @BindView(R.id.pendingComplains)
    LinearLayout linearLayoutpendingComplains;

    @BindView(R.id.resolvedComplains)
    LinearLayout linearLayoutResolvedComplains;

    @BindView(R.id.forwardComplains)
    LinearLayout linearLayoutEmployeeComplains;

    @BindView(R.id.complainAnalytics)
    LinearLayout linearLayoutComplainAnalytics;

    @BindView(R.id.delay)
    LinearLayout delay;

    @BindView(R.id.forwardFrom)
    LinearLayout forwardFrom;

    @BindView(R.id.complainAnalyticsT)
    TextView complainsAnalyticsT;

    @BindView(R.id.forwardComplainsT)
    TextView forwardComplainsT;

    @BindView(R.id.resolvedComplainsT)
    TextView resolvedComplainsT;

    @BindView(R.id.pendingComplainsT)
    TextView pendingComplainsT;

    @BindView(R.id.newComplainsT)
    TextView newComplainsT;

    @BindView(R.id.allComplainsT)
    TextView allComplainsT;

    @BindView(R.id.forwardFromT)
    TextView forwardFromT;

    @BindView(R.id.delayT)
    TextView delayT;


    public AllCatigoryComplainsFragment(String cnic, String desId, String userName, String employeeId) {
        this.cnic = cnic;
        this.desId = desId;
        this.userName = userName;
        this.employeeId = employeeId;
    }

    public AllCatigoryComplainsFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.activity_all_catigory_complains, container, false);
        // Inflate the layout for this fragment

        unbinder = ButterKnife.bind(this, view);
        return view;
    }
    private void totalForwards() {
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(Constants.REST_API)
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        JsonApiHolder service = retrofit.create(JsonApiHolder.class);

        Call<Forwards> call = service.getTotalForwards(employeeId);

        call.enqueue(new Callback<Forwards>() {
            @Override
            public void onResponse(Call<Forwards> call, Response<Forwards> response) {
                if(response.isSuccessful()) {
                    Log.d(TAG, "onResponseForwards: " + response.body().getForward());
                    forwardComplainsT.setText(response.body().getForward());
                }else{
                    Log.d(TAG, "onResponseForwards: Failed" );

                }
            }

            @Override
            public void onFailure(Call<Forwards> call, Throwable t) {
                Log.d(TAG, "onResponseForwards: Failed" + t.getMessage() );

            }
        });

    }

    @OnClick(R.id.allComplains)
    public void redirectToAllComplains(){
        Intent intent = new Intent(getActivity(), ManagingComplaints.class);
        intent.putExtra(getString(R.string.complain_redirect), Constants.ALL_COMPLAINS);
        intent.putExtra(Constants.PREVELDGES_ON_FORWARD, desId);
        intent.putExtra(getString(R.string.permanentlogin_name), userName);
        startActivity(intent);

    }

    @OnClick(R.id.newComplains)
    public void redirectToNewComplains(){
        Intent intent = new Intent(getContext(), ManagingComplaints.class);
        intent.putExtra(getString(R.string.complain_redirect), Constants.NEW_COMPLAINS);
        intent.putExtra(Constants.PREVELDGES_ON_FORWARD, desId);
        intent.putExtra(getString(R.string.permanentlogin_name), userName);
        startActivity(intent);
    }

    @OnClick(R.id.pendingComplains)
    public void redirectToPendingComplains(){
        Intent intent = new Intent(getContext(), ManagingComplaints.class);
        intent.putExtra( getString(R.string.complain_redirect), Constants.PENDING_COMPLAINS);
        intent.putExtra(Constants.PREVELDGES_ON_FORWARD, desId);
//        intent.putExtra(getString(R.string.permanentlogin_name), userName);
        startActivity(intent);
    }

    @OnClick(R.id.resolvedComplains)
    public void redirectToResolvedComplains(){

        Intent intent = new Intent(getContext(), ManagingComplaints.class);
        intent.putExtra( getString(R.string.complain_redirect), Constants.RESOLVED_COMPLAINS);
        intent.putExtra(Constants.PREVELDGES_ON_FORWARD, desId);
//        intent.putExtra(getString(R.string.permanentlogin_name), userName);

        startActivity(intent);
    }

    @OnClick(R.id.forwardComplains)
    public void redirectToEmployeeComplains(){
        Intent intent = new Intent(getContext(), ManagingComplaints.class);
        intent.putExtra(getString(R.string.complain_redirect), Constants.FORWARD_COMPLAINS);
        intent.putExtra(Constants.PREVELDGES_ON_FORWARD, employeeId);
        intent.putExtra(getString(R.string.permanentlogin_name), userName);
        startActivity(intent);
    }

    @OnClick(R.id.complainAnalytics)
    public void redirectToComplainsAnalytics(){

        Intent intent = new Intent(getContext(), DescriptionAndrGraphActivity.class);
//        intent.putExtra(Constants.ALL_COMPLAINS, getString(R.string.complain_redirect));
        startActivity(intent);
    }
    @OnClick(R.id.forwardFrom)
    public void redirectToEmployeeComplainsForwardFrom(){
        Log.d(TAG, "redirectToEmployeeComplainsForwardFrom: Clicked");
        Intent intent = new Intent(getContext(), ManagingComplaints.class);
        intent.putExtra(getString(R.string.complain_redirect), Constants.FORWARD_FROM);
        intent.putExtra(Constants.PREVELDGES_ON_FORWARD, employeeId);
        intent.putExtra(getString(R.string.permanentlogin_name), userName);
        startActivity(intent); }

    @OnClick(R.id.delay)
    public void redirectToDelayEmployees(){
//        Intent intent = new Intent(getContext(), ManagingComplaints.class);
        Intent intent = new Intent(getContext(), DelayedEmployee.class);
        intent.putExtra(getString(R.string.complain_redirect), Constants.FORWARD_FROM);
        intent.putExtra(Constants.PREVELDGES_ON_FORWARD, employeeId);
        intent.putExtra(getString(R.string.permanentlogin_name), userName);
        startActivity(intent);
    }

    @Override
    public void onStart() {
        super.onStart();
        valuesForNew.clear();
        valuesForPending.clear();
        valuesForResolved.clear();
        ((EmployeeNavigation)getActivity()).checkConnection();
        fetchComplains();
        totalForwards();
        totalForwardsFrom();
        fetchTotalDelays();
    }

    private void totalForwardsFrom() {
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(Constants.REST_API)
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        JsonApiHolder service = retrofit.create(JsonApiHolder.class);

        Call<Forwards> call = service.getTotalForwardsFrom(employeeId);
        Log.d(TAG, "totalForwardsFrom: Called");

        call.enqueue(new Callback<Forwards>() {
            @Override
            public void onResponse(Call<Forwards> call, Response<Forwards> response) {
                if(response.isSuccessful()) {
                    Log.d(TAG, "totalForwardsFrom:From " + response.body().getForward());
                    forwardFromT.setText(response.body().getForward());
                }else{
                    Log.d(TAG, "totalForwardsFrom: Failed" );
                }
            }

            @Override
            public void onFailure(Call<Forwards> call, Throwable t) {
                Log.d(TAG, "totalForwardsFrom: Failed" + t.getMessage() );

            }
        });

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
                    if (all.getComplain_status().equals(Constants.COMPLAINS_RESOLVED)) {
                        valuesForResolved.add(all);
                    } else if (
                            all.getComplain_status().equals(Constants.COMPLAINS_PENDING) ||
                                    all.getComplain_status().equals(Constants.COMPLAINS_IN_PROCESS)

                    ) {
                        valuesForPending.add(all);
                    } else if (all.getComplain_status().equals(Constants.COMPLAINS_NEW)) {
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
    private void fetchTotalDelays() {
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(Constants.REST_API)
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        JsonApiHolder service = retrofit.create(JsonApiHolder.class);

//        Call<List<Employee>> call = service.getTotalForwardsFromWithDelay(employeeId);
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
                int count = 0;
                for(Employee all: list){
                    count = count + Integer.parseInt(all.getTotal());
                }
                delayT.setText(count + "");
            }

            @Override
            public void onFailure(Call<List<Employee>> call, Throwable t) {

            }
        });

    }

    private void getArrayListForGraphInteger() {
        allComplainsT.setText(String.valueOf(valuesForNew.size() + valuesForPending.size() + valuesForResolved.size()));
        newComplainsT.setText(String.valueOf(valuesForNew.size()));
        pendingComplainsT.setText(String.valueOf(valuesForPending.size()));
        resolvedComplainsT.setText(String.valueOf(valuesForResolved.size()));
        complainsAnalyticsT.setText(String.valueOf(valuesForNew.size() + valuesForPending.size() + valuesForResolved.size()));


    }
}
