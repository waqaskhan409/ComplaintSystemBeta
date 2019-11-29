package com.example.complaintsystembeta.ui.complaints;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;

import com.example.complaintsystembeta.BaseActivity;
import com.example.complaintsystembeta.R;
import com.example.complaintsystembeta.constants.Constants;
import com.example.complaintsystembeta.interfaace.JsonApiHolder;
import com.example.complaintsystembeta.model.ReportForward;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;
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
    private String complainsId;
    private List<ReportForward> listForwardBy, listForwardTo;
    private Unbinder unbinder;



    @BindView(R.id.tree)
    TextView tree;




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_complain_statistics);
        if(unbinder == null){
            unbinder = ButterKnife.bind(this);
        }
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
    }


    @Override
    protected void onStart() {
        super.onStart();
        data = getIntent().getExtras();
        complainsId = data.getString(getString(R.string.complains_id));
        get_forward_by(complainsId);
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
                    get_forward_to(complainsId);



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

    private void setupTree() {
        int size =0;
        if(listForwardBy.size() < listForwardTo.size()){
            size = listForwardBy.size();
        }else {
            size = listForwardTo.size();
        }
        for (int i = 0; i < size; i++) {
            tree.append(listForwardBy.get(i).getFull_name() + "/" + listForwardBy.get(i).getDes_title() + "->->" +listForwardTo.get(i).getFull_name()+ "/" + listForwardTo.get(i).getDes_title() + "\n");
        }
    }

}
