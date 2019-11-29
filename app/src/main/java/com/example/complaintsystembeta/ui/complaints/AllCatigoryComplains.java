package com.example.complaintsystembeta.ui.complaints;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.CheckBox;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.complaintsystembeta.R;
import com.example.complaintsystembeta.constants.Constants;
import com.example.complaintsystembeta.interfaace.JsonApiHolder;
import com.example.complaintsystembeta.model.AllComplains;
import com.example.complaintsystembeta.model.Forwards;
import com.squareup.picasso.Picasso;

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

public class AllCatigoryComplains extends AppCompatActivity {

    private static final String TAG = "AllCatigoryComplains";
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
    private Toolbar toolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_all_catigory_complains);

        toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        if(unbinder == null){
            unbinder = ButterKnife.bind(this);
        }
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        data = getIntent().getExtras();
//        cnic = data.getString(getString(R.string.permanentlogin_cnic));
        userName = data.getString(getString(R.string.permanentlogin_name));
        desId = data.getString(Constants.PREVELDGES_ON_FORWARD);
        employeeId = data.getString(getString(R.string.permanentlogin_id));


        Log.d(TAG, "onResponseForwards: oncreate" + desId);
        Log.d(TAG, "onResponseForwards: oncreate" + employeeId);

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
        Intent intent = new Intent(this, ManagingComplaints.class);
        intent.putExtra(getString(R.string.complain_redirect), Constants.ALL_COMPLAINS);
        intent.putExtra(Constants.PREVELDGES_ON_FORWARD, desId);
        intent.putExtra(getString(R.string.permanentlogin_name), userName);
        startActivity(intent);

    }

    @OnClick(R.id.newComplains)
    public void redirectToNewComplains(){
        Intent intent = new Intent(this, ManagingComplaints.class);
        intent.putExtra(getString(R.string.complain_redirect), Constants.NEW_COMPLAINS);
        intent.putExtra(Constants.PREVELDGES_ON_FORWARD, desId);
        intent.putExtra(getString(R.string.permanentlogin_name), userName);
        startActivity(intent);
    }

    @OnClick(R.id.pendingComplains)
    public void redirectToPendingComplains(){
        Intent intent = new Intent(this, ManagingComplaints.class);
        intent.putExtra( getString(R.string.complain_redirect), Constants.PENDING_COMPLAINS);
        intent.putExtra(Constants.PREVELDGES_ON_FORWARD, desId);
//        intent.putExtra(getString(R.string.permanentlogin_name), userName);
        startActivity(intent);
    }

    @OnClick(R.id.resolvedComplains)
    public void redirectToResolvedComplains(){

        Intent intent = new Intent(this, ManagingComplaints.class);
        intent.putExtra( getString(R.string.complain_redirect), Constants.RESOLVED_COMPLAINS);
        intent.putExtra(Constants.PREVELDGES_ON_FORWARD, desId);
//        intent.putExtra(getString(R.string.permanentlogin_name), userName);

        startActivity(intent);
    }

    @OnClick(R.id.forwardComplains)
    public void redirectToEmployeeComplains(){
        Intent intent = new Intent(this, ManagingComplaints.class);
        intent.putExtra(getString(R.string.complain_redirect), Constants.FORWARD_COMPLAINS);
        intent.putExtra(Constants.PREVELDGES_ON_FORWARD, employeeId);
        intent.putExtra(getString(R.string.permanentlogin_name), userName);
        startActivity(intent);
    }

    @OnClick(R.id.complainAnalytics)
    public void redirectToComplainsAnalytics(){

        Intent intent = new Intent(this, DescriptionAndrGraphActivity.class);
//        intent.putExtra(Constants.ALL_COMPLAINS, getString(R.string.complain_redirect));
        startActivity(intent);
    }

    @Override
    protected void onStart() {
        super.onStart();
        valuesForNew.clear();
        valuesForPending.clear();
        valuesForResolved.clear();
        fetchComplains();
        totalForwards();
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
                        } else if (all.getComplain_status().equals(Constants.COMPLAINS_PENDING)) {
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

    private void getArrayListForGraphInteger() {
        allComplainsT.setText(String.valueOf(valuesForNew.size() + valuesForPending.size() + valuesForResolved.size()));
        newComplainsT.setText(String.valueOf(valuesForNew.size()));
        pendingComplainsT.setText(String.valueOf(valuesForPending.size()));
        resolvedComplainsT.setText(String.valueOf(valuesForResolved.size()));


    }
}
