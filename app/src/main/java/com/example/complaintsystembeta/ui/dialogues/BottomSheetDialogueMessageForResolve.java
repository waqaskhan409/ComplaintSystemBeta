package com.example.complaintsystembeta.ui.dialogues;

import android.annotation.SuppressLint;
import android.app.DatePickerDialog;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Spinner;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.example.complaintsystembeta.R;
import com.example.complaintsystembeta.constants.Constants;
import com.example.complaintsystembeta.constants.RestApi;
import com.example.complaintsystembeta.interfaace.JsonApiHolder;
import com.example.complaintsystembeta.model.TestClas;
import com.example.complaintsystembeta.ui.complaints.ManagingComplaints;
import com.example.complaintsystembeta.ui.complaints.SingleComplainDetails;
import com.google.android.material.bottomsheet.BottomSheetDialogFragment;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
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

public class BottomSheetDialogueMessageForResolve extends BottomSheetDialogFragment {
    private static final String TAG = "BottomSheetDialogueCompose";
    private View view;
    private Unbinder unbinder;
    private String resolveMessage;
    private String complainId, employeeId;


    public BottomSheetDialogueMessageForResolve(String complainId, String employeeId) {
        this.complainId = complainId;
        this.employeeId = employeeId;
    }



    @BindView(R.id.submit)
    Button submit;
    @BindView(R.id.cancel)
    Button cancel;
    @BindView(R.id.resolveBody)
    EditText resolveBody;


    @SuppressLint("LongLogTag")
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.dialog_for_resolve_message, container, false);
        if (unbinder == null) {
            unbinder = ButterKnife.bind(this, view);
        }
        Log.d(TAG, "onCreateView: " + complainId);
        Log.d(TAG, "onCreateView: " + employeeId);

        return view;
    }

    @OnClick(R.id.cancel)
    public void dismissFragmentDialogue() {

        ((SingleComplainDetails)getActivity()).confirmStatusSpinner.setSelection(0);
        dismiss();
    }

    @OnClick(R.id.submit)
    public void submitDataToServer(){
        validateAndSendData();
    }

    private void validateAndSendData() {
        resolveMessage = resolveBody.getText().toString().trim();
        if(resolveMessage.equals("") || resolveMessage.isEmpty()){
            resolveBody.setError("Please put some here");
            resolveBody.requestFocus();
        }else {
            sendDataToServer();
        }
    }

    private void sendDataToServer() {
        String resolveId = UUID.randomUUID().toString();

        JsonApiHolder service = RestApi.getApi();
            RequestBody resolveMsgeRqst = RequestBody.create(MediaType.parse("text/plain"), resolveMessage);
            RequestBody complainIdRqst = RequestBody.create(MediaType.parse("text/plain"), complainId);
            RequestBody resolveIdRqst = RequestBody.create(MediaType.parse("text/plain"), resolveId);
            RequestBody employeIdRqst = RequestBody.create(MediaType.parse("text/plain"), employeeId);
            Call<TestClas> call = service.setResolveComplain(resolveIdRqst, complainIdRqst, employeIdRqst, resolveMsgeRqst);
            call.enqueue(new Callback<TestClas>() {
                @SuppressLint("LongLogTag")
                @Override
                public void onResponse(Call<TestClas> call, Response<TestClas> response) {
                    if(response.isSuccessful()){
                        Log.d(TAG, "onResponse: userName:" + response.body().getSuccess());
                        ((SingleComplainDetails)getActivity()).updateStatus();
                        dismiss();
                    }else {
                        dismissFragmentDialogue();
                        Log.d(TAG, "onResponse: Failed!");
                    }

                }

                @SuppressLint("LongLogTag")
                @Override
                public void onFailure(Call<TestClas> call, Throwable t) {
                    dismissFragmentDialogue();
                    Log.d(TAG, "onFailure: " + t.getMessage());
                }
            });


    }


}
