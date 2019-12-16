package com.example.complaintsystembeta.ui.dialogues;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.example.complaintsystembeta.R;
import com.example.complaintsystembeta.constants.Constants;
import com.example.complaintsystembeta.interfaace.JsonApiHolder;
import com.example.complaintsystembeta.model.Resolve;
import com.example.complaintsystembeta.model.TestClas;
import com.example.complaintsystembeta.ui.complaints.SingleComplainDetails;
import com.google.android.material.bottomsheet.BottomSheetDialogFragment;

import java.util.UUID;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;
import okhttp3.MediaType;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class BottomSheetDialogueForResolveMessage extends BottomSheetDialogFragment {
    private static final String TAG = "BottomSheetDialogueCompose";
    private View view;
    private Unbinder unbinder;
    private String resolveMessage;
    private String complainId;


    public BottomSheetDialogueForResolveMessage(String complainId) {
        this.complainId = complainId;
    }

    @BindView(R.id.resolveRemarks)
    TextView resolveRemarks;

    @BindView(R.id.resolveEmployee)
    TextView resolveEmployee;

    @BindView(R.id.date)
    TextView date;


    @SuppressLint("LongLogTag")
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.dialog_for_complain_resolve, container, false);
        if (unbinder == null) {
            unbinder = ButterKnife.bind(this, view);
        }
        Log.d(TAG, "onCreateView: " + complainId);
        sendDataToServer();
        return view;
    }





    private void sendDataToServer() {

        Retrofit retrofit = new Retrofit.Builder()
                    .baseUrl(Constants.REST_API)
                    .addConverterFactory(GsonConverterFactory.create())
                    .build();
        JsonApiHolder service = retrofit.create(JsonApiHolder.class);
        Call<Resolve> call = service.getComplainResolve(complainId);
        call.enqueue(new Callback<Resolve>() {
                @SuppressLint("LongLogTag")
                @Override
                public void onResponse(Call<Resolve> call, Response<Resolve> response) {
                    if(response.isSuccessful()){
                        Log.d(TAG, "onResponse: userName:" + response.body().getResolve_message());
                        settingValues(response.body());

                    }else {
                        Log.d(TAG, "onResponse: Failed!");
                    }

                }

                @SuppressLint("LongLogTag")
                @Override
                public void onFailure(Call<Resolve> call, Throwable t) {

                }
            });


    }

    private void settingValues(Resolve body) {
        date.setText("Date: " + body.getCreated_at());
        resolveEmployee.setText("Employee: " + body.getFull_name() +"/"+ body.getDes_title() + "/" +body.getDepartment_name());
        resolveRemarks.setText("Remarks: " + body.getResolve_message());
    }


}
