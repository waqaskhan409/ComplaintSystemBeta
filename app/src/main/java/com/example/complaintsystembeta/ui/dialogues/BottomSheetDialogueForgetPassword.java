package com.example.complaintsystembeta.ui.dialogues;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.example.complaintsystembeta.BaseActivity;
import com.example.complaintsystembeta.R;
import com.example.complaintsystembeta.constants.Constants;
import com.example.complaintsystembeta.interfaace.JsonApiHolder;
import com.example.complaintsystembeta.model.AllComplains;
import com.example.complaintsystembeta.model.TestClas;
import com.example.complaintsystembeta.ui.complaints.ComposeComplaints;
import com.example.complaintsystembeta.ui.login.LoginActivity;
import com.google.android.material.bottomsheet.BottomSheetDialogFragment;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class BottomSheetDialogueForgetPassword extends BottomSheetDialogFragment {
    private static final String TAG = "BottomSheetDialogueCompose";
    private View view;
    public EditText emailEd;
    public Button submit;



    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.forget_password_layout, container, false);

        emailEd = view.findViewById(R.id.email);
        submit = view.findViewById(R.id.submitForMail);

        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                validate();
            }
        });



        return view;
    }

    @SuppressLint("LongLogTag")
    private void validate() {
        String email = emailEd.getText().toString();
        if(email.equals("") || email ==null){
            emailEd.setError(getString(R.string.prompt_email_error));
            emailEd.requestFocus();
        }else if(!BaseActivity.isEmailValid(email)){

            emailEd.setError(getString(R.string.prompt_email_error));
            emailEd.requestFocus();
        }else {

            ((LoginActivity)getActivity()).getDataFromServer(email);
            dismiss();
        }
    }



}
