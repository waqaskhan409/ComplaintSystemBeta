package com.example.complaintsystembeta.ui.complaints;


import android.app.Application;
import android.content.Intent;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.speech.RecognitionService;
import android.speech.RecognizerIntent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.example.complaintsystembeta.R;
import com.example.complaintsystembeta.Repository.PermanentLoginRepository;
import com.example.complaintsystembeta.interfaace.JsonApiHolder;
import com.example.complaintsystembeta.interfaace.PermanentLoginDao;
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
public class Compliants extends Fragment {
    private static final String TAG = "Compliants";
    private View view;
    private PermanentLoginRepository permanentLoginRepository;
    private List<PermanentLogin> list ;
    Unbinder unbinder;


    @BindView(R.id.fp_btn)
    FloatingActionButton button;

//    @BindView(R.id.text)
    TextView textView;

    public Compliants() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_compliants, container, false);
//        unbinder = ButterKnife.bind(getActivity(), view);
//        getDataFromRestApi();
//        insertData();
//        getDataFromSqlite();
        FloatingActionButton floatingActionButton = view.findViewById(R.id.fp_btn);
                textView = view.findViewById(R.id.text);
        floatingActionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), ComposeComplaints.class);
                startActivity(intent);
            }
        });

        return view;
    }

    //    @OnClick(R.id.fp_btn)
    public void changeActivity(){
        Intent intent = new Intent(getActivity(), ComposeComplaints.class);
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
        PermanentLoginRepository dao = new PermanentLoginRepository(getActivity().getApplication()) ;

        dao.insert(new PermanentLogin("12312312312", true, "Soud"));
        dao.insert(new PermanentLogin("12312312312", false, "Soud"));
        dao.insert(new PermanentLogin("12312312312", false, "Soud"));

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
                    Log.d(TAG, "onResponse: " + l.getSign_up_id());
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
