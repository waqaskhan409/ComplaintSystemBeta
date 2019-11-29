package com.example.complaintsystembeta.ui.complaints;


import android.app.Application;
import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.annotation.StringRes;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.speech.RecognitionService;
import android.speech.RecognizerIntent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.example.complaintsystembeta.BaseActivity;
import com.example.complaintsystembeta.R;
import com.example.complaintsystembeta.Repository.PermanentLoginRepository;
import com.example.complaintsystembeta.adapter.AllComplainsAdapter;
import com.example.complaintsystembeta.adapter.ConsumerComplaints;
import com.example.complaintsystembeta.constants.Constants;
import com.example.complaintsystembeta.interfaace.JsonApiHolder;
import com.example.complaintsystembeta.interfaace.OnBackPressedListener;
import com.example.complaintsystembeta.interfaace.PermanentLoginDao;
import com.example.complaintsystembeta.model.AllComplains;
import com.example.complaintsystembeta.model.PermanentLogin;
import com.example.complaintsystembeta.model.Posts;
import com.example.complaintsystembeta.model.SignUpData;
import com.example.complaintsystembeta.ui.MainActivity;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

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

import static android.app.Activity.RESULT_OK;

/**
 * A simple {@link Fragment} subclass.
 */
public class Compliants extends BaseActivity{
    private static final String TAG = "Compliants";
    private PermanentLoginRepository permanentLoginRepository;
    private List<PermanentLogin> list ;
    private Unbinder unbinder;
    private String cnic, name, complainsFilter;
    private List<AllComplains> allComplains = new ArrayList<>();
    private RecyclerView recyclerView;
    private Bundle data;


    @BindView(R.id.fp_btn)
    FloatingActionButton button;

//    @BindView(R.id.text)
    TextView textView;

    public Compliants() {
        // Required empty public constructor
    }

    public Compliants(String cnic, String name, String complainsFilter) {
        this.cnic = cnic;
        this.name = name;
        this.complainsFilter = complainsFilter;
        Log.d(TAG, "Compliants: " + cnic);
    }


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fragment_compliants);
        recyclerView = findViewById(R.id.recyclerviewForComplains);
        GridLayoutManager linearLayoutManager = new GridLayoutManager(this, 2);

        linearLayoutManager.setOrientation(RecyclerView.VERTICAL);
        recyclerView.setLayoutManager(linearLayoutManager);

        data = getIntent().getExtras();

        cnic = data.getString(Constants.ACCOUNT_NUMBER);
        name = data.getString(Constants.NAME);
        complainsFilter = data.getString(Constants.COMPLAIN_FILTER);

        Log.d(TAG, "onCreate: " +complainsFilter);


        fetchAllComplains();


        FloatingActionButton floatingActionButton = findViewById(R.id.fp_btn);
        textView = findViewById(R.id.text);
        floatingActionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Compliants.this, ComposeComplaints.class);
                intent.putExtra(getString(R.string.account_number), cnic);
                intent.putExtra(getString(R.string.userName), name);
                startActivity(intent);
            }
        });
    }


    private void fetchAllComplains() {
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
                if(complainsFilter.equals(Constants.ALL_COMPLAINS)){
                    for (AllComplains l : list){
                        Log.d(TAG, "onResponse: " + l.getAccount_number() +"==" + cnic);
                        if(l.getAccount_number().equals(cnic)){
                                allComplains.add(l);
                        }
                    }
                }else {
                for (AllComplains l : list){
                    Log.d(TAG, "onResponse: " + l.getAccount_number() +"==" + cnic);
                    if(l.getAccount_number().equals(cnic)){
                     if(complainsFilter.equals(l.getComplain_status())){
                        allComplains.add(l);
                     }
                    }
                }
                }
                setupAdapter();
            }

            @Override
            public void onFailure(Call<List<AllComplains>> call, Throwable t) {

            }
        });

    }

    private void setupAdapter() {
        ConsumerComplaints allComplainsAdapter  = new ConsumerComplaints(allComplains, this, name);
        recyclerView.setHasFixedSize(true);
        recyclerView.setAdapter(allComplainsAdapter);
    }


    //    @OnClick(R.id.fp_btn)
    public void changeActivity(){
        Intent intent = new Intent(this, ComposeComplaints.class);
        startActivity(intent);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode){
            case 20:
                if(resultCode == RESULT_OK && data != null){
                    ArrayList<String> result = data.getStringArrayListExtra(RecognizerIntent.EXTRA_RESULTS);
                    textView.setText(result.get(0));
                }
        }

    }

    void insertData(){
        PermanentLoginRepository dao = new PermanentLoginRepository(this.getApplication()) ;

//        dao.insert(new PermanentLogin("12312312312", true, "Soud"));
//        dao.insert(new PermanentLogin("12312312312", false, "Soud"));
//        dao.insert(new PermanentLogin("12312312312", false, "Soud"));

    }


    private void getDataFromRestApi() {
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("http://theoriginschoolsystem.com")
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        JsonApiHolder jsonApiHolder =  retrofit.create(JsonApiHolder.class);
        Call<List<SignUpData>> call = jsonApiHolder.getPost();
        call.enqueue(new Callback<List<SignUpData>>() {
            @Override
            public void onResponse(Call<List<SignUpData>> call, Response<List<SignUpData>> response) {
                if(!response.isSuccessful()){
                    Log.d(TAG, "onResponse: " + response.message());
                    return;
                }
                Log.d(TAG, "onResponse: " + response.message());
                List<SignUpData>  list = response.body();
                for (SignUpData l : list){
//                    Log.d(TAG, "onResponse: " + l.getSign_up_id());
                    Log.d(TAG, "onResponse: " + l.getUser_cnic());
                    Log.d(TAG, "onResponse: " + l.getUser_cnic());
                    Log.d(TAG, "onResponse: " + l.getUser_address());
                }
            }

            @Override
            public void onFailure(Call<List<SignUpData>> call, Throwable t) {

            }
        });
    }


}
