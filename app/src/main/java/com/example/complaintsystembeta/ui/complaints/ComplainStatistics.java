package com.example.complaintsystembeta.ui.complaints;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.widget.TextView;

import com.example.complaintsystembeta.BaseActivity;
import com.example.complaintsystembeta.R;
import com.example.complaintsystembeta.adapter.ForwardAdapter;
import com.example.complaintsystembeta.adapter.TimelineAdapter;
import com.example.complaintsystembeta.constants.Constants;
import com.example.complaintsystembeta.interfaace.JsonApiHolder;
import com.example.complaintsystembeta.model.Employee;
import com.example.complaintsystembeta.model.ReportForward;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;
import io.github.inflationx.viewpump.ViewPumpContextWrapper;
import okhttp3.MediaType;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class ComplainStatistics extends BaseActivity {
    private static final String TAG = "ComplainStatistics";
    private Bundle data;
    private String complainsId, user, userName = "", status;
    private List<ReportForward> listForwardBy, listForwardTo;
    private Unbinder unbinder;





    @BindView(R.id.recyclerViewForTree)
    RecyclerView treeRecycler;
    private List<ReportForward> list;
    private List<Employee> listEmployees;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_complain_statistics);
        if(unbinder == null){
            unbinder = ButterKnife.bind(this);
        }
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        linearLayoutManager.setOrientation(RecyclerView.VERTICAL);

        treeRecycler.setLayoutManager(linearLayoutManager);
//        userName = data.getString(getString(R.string.permanentlogin_name));



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
        data = getIntent().getExtras();
        user = data.getString(Constants.PREVELDGES_ON_FORWARD);
        complainsId = data.getString(getString(R.string.complains_id));
        status = data.getString(Constants.STATUS_COMPLAIN);
        Log.d(TAG, "onStart: " + status);
        getSingleComplainForwardingDetail(complainsId);
    }

    private void get_forward_by(String complainId) {
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(Constants.REST_API)
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        JsonApiHolder service = retrofit.create(JsonApiHolder.class);

        Call<List<ReportForward>> listCall = service.get_forward_by(complainId);

        listCall.enqueue(new Callback<List<ReportForward>>() {
            @Override
            public void onResponse(Call<List<ReportForward>> call, Response<List<ReportForward>> response) {
                if(response.isSuccessful()){
                    Log.d(TAG, "onResponse: Succefull");

                    listForwardBy = response.body();
//                    get_forward_to(complainsId);



                }else {
                    Log.d(TAG, "onResponse: Failed!");
                }
            }

            @Override
            public void onFailure(Call<List<ReportForward>> call, Throwable t) {
                Log.d(TAG, "onResponse: Failed! :" + t.getMessage());

            }
        });
    }
    private void getEmployee(String complainId) {
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(Constants.REST_API)
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        RequestBody accountRqst = RequestBody.create(MediaType.parse("text/plain"), complainId);
        JsonApiHolder service = retrofit.create(JsonApiHolder.class);

        Call<List<Employee>> listCall = service.getEmployee();

        listCall.enqueue(new Callback<List<Employee>>() {
            @Override
            public void onResponse(Call<List<Employee>> call, Response<List<Employee>> response) {
                if(response.isSuccessful()){
                    Log.d(TAG, "onResponse: Succefull");

                    listEmployees = response.body();
                    Log.d(TAG, "onResponse: " + listEmployees.size());

                    setupAdapter(list, listEmployees);



                }else {
                    Log.d(TAG, "onResponse: Failed!");
                }
            }

            @Override
            public void onFailure(Call<List<Employee>> call, Throwable t) {
                Log.d(TAG, "onResponse: Failed! :" + t.getMessage());

            }
        });
    }

    private void setupAdapter(List<ReportForward> list, List<Employee> listEmployees) {
        TimelineAdapter timelineAdapter  = new TimelineAdapter(list, this, userName, user, listEmployees, status);
        treeRecycler.setHasFixedSize(true);
        treeRecycler.setAdapter(timelineAdapter);
    }

    private void getSingleComplainForwardingDetail(String complainId) {
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(Constants.REST_API)
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        RequestBody accountRqst = RequestBody.create(MediaType.parse("text/plain"), complainId);
        JsonApiHolder service = retrofit.create(JsonApiHolder.class);

        Call<List<ReportForward>> listCall = service.getSingleComplainDetailForwarding(complainId);

        listCall.enqueue(new Callback<List<ReportForward>>() {
            @Override
            public void onResponse(Call<List<ReportForward>> call, Response<List<ReportForward>> response) {
                if(response.isSuccessful()){
                    Log.d(TAG, "onResponse: Succefull");

                    list = response.body();
                    getEmployee(complainId);
                    Log.d(TAG, "onResponse: " + list.size());


                }else {
                    Log.d(TAG, "onResponse: Failed!");
                }
            }

            @Override
            public void onFailure(Call<List<ReportForward>> call, Throwable t) {
                Log.d(TAG, "onResponse: Failed! :" + t.getMessage());

            }
        });
    }

/*
    private void get_forward_to(String complainId) {
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(Constants.REST_API)
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        JsonApiHolder service = retrofit.create(JsonApiHolder.class);

        Call<List<ReportForward>> listCall = service.get_forward_to(complainId);

        listCall.enqueue(new Callback<List<ReportForward>>() {
            @Override
            public void onResponse(Call<List<ReportForward>> call, Response<List<ReportForward>> response) {
                if(response.isSuccessful()){
                    Log.d(TAG, "onResponse: Succefull");

                    listForwardTo = response.body();
                    setupTree();


                }else {
                    Log.d(TAG, "onResponse: Failed!");
                }
            }

            @Override
            public void onFailure(Call<List<ReportForward>> call, Throwable t) {
                Log.d(TAG, "onResponse: Failed! :" + t.getMessage());

            }
        });
    }
*/

    private void setupTree() {
        int size =0;
        if(listForwardBy.size() < listForwardTo.size()){
            size = listForwardBy.size();
        }else {
            size = listForwardTo.size();
        }
        for (int i = 0; i < size; i++) {
//            tree.append(listForwardBy.get(i).getFull_name() + "/" + listForwardBy.get(i).getDes_title() + "->->" +listForwardTo.get(i).getFull_name()+ "/" + listForwardTo.get(i).getDes_title() + "\n");
        }
    }
    @Override
    protected void attachBaseContext(Context newBase) {
        super.attachBaseContext(ViewPumpContextWrapper.wrap(newBase));
    }
}
