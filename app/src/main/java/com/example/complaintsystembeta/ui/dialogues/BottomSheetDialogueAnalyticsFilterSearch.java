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
import com.example.complaintsystembeta.interfaace.JsonApiHolder;
import com.example.complaintsystembeta.model.AllComplains;
import com.example.complaintsystembeta.ui.complaints.DescriptionAndrGraphActivity;
import com.example.complaintsystembeta.ui.complaints.ManagingComplaints;
import com.google.android.material.bottomsheet.BottomSheetDialogFragment;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import butterknife.ButterKnife;
import butterknife.Unbinder;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class BottomSheetDialogueAnalyticsFilterSearch extends BottomSheetDialogFragment {
    private static final String TAG = "BottomSheetDialogueCompose";
    private View view;
    public LinearLayout linearLayoutCamera, linearLayoutGallery;
    private Unbinder unbinder;
    private DatePickerDialog.OnDateSetListener mDateListenerTo, mDateListenerFrom;


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.dialog_for_analytics_filters, container, false);


        Spinner spinnerAdvance = view.findViewById(R.id.spinnerAdvance);

        List<String> list = new ArrayList<>();
        list.add(Constants.ENGINEERING);
        list.add(Constants.ADMINISTRATION);
        list.add(Constants.ACCOUNT);
        list.add(Constants.REVENUE);
        list.add(Constants.TECHNICAL);
        list.add(Constants.SANITATION);
        list.add(Constants.ENGINA);
        ArrayAdapter adapter = new ArrayAdapter(getContext(), android.R.layout.simple_list_item_1, list);
        spinnerAdvance.setAdapter(adapter);

        Button cancel, submit, thisMonth;
        cancel = view.findViewById(R.id.cancel);
        submit = view.findViewById(R.id.submit);
        thisMonth = view.findViewById(R.id.thisMonth);


        EditText edTo, edFrom;

        edTo = view.findViewById(R.id.dateTo);
        edFrom = view.findViewById(R.id.dateFrom);

        edTo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                datePickerTo();
            }
        });

        edFrom.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                datePickerFrom();
            }
        });

        thisMonth.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String department;
                department = spinnerAdvance.getSelectedItem().toString();
                int currentYear =  Calendar.getInstance().get(Calendar.YEAR);
                int currentMonth =  Calendar.getInstance().get(Calendar.MONTH)+1;

                int startDate = 1;
                int startEnd = 31;
                String dateTo = String.valueOf(currentYear + "-" + currentMonth + "-" + startDate);
                String dateFrom = String.valueOf(currentYear + "-" + currentMonth + "-" + startEnd);
                ((DescriptionAndrGraphActivity) getActivity()).fetchComplains(department, dateTo, dateFrom);
                dismiss();
            }
        });

        submit.setOnClickListener(new View.OnClickListener() {
            @SuppressLint("LongLogTag")
            @Override
            public void onClick(View v) {
                Log.d(TAG, "onClick: Submited");
                String dateTo, dateFrom, department;
                dateTo = edTo.getText().toString().trim();
                dateFrom = edFrom.getText().toString().trim();
                department = spinnerAdvance.getSelectedItem().toString();
                if (dateTo.equals("") || dateFrom.equals("")) {
                    Log.d(TAG, "onClick: dates is not selected");
                    dismiss();
                } else {
                    ((DescriptionAndrGraphActivity) getActivity()).fetchComplains(department, dateTo, dateFrom);
                    dismiss();
                }

            }
        });

        cancel.setOnClickListener(new View.OnClickListener() {
            @SuppressLint("LongLogTag")
            @Override
            public void onClick(View v) {
                Log.d(TAG, "onClick: cancel");

                dismiss();
            }
        });

        mDateListenerTo = new DatePickerDialog.OnDateSetListener() {
            @SuppressLint("LongLogTag")
            @Override
            public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                month = month + 1;
                Log.d(TAG, "onDateSet: " + year + "-" + month + "-" + dayOfMonth);
                edTo.setText(year + "-" + month + "-" + dayOfMonth);
            }
        };
        mDateListenerFrom = new DatePickerDialog.OnDateSetListener() {
            @SuppressLint("LongLogTag")
            @Override
            public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                month = month + 1;
                Log.d(TAG, "onDateSet: " + year + "-" + month + "-" + dayOfMonth);
                edFrom.setText(year + "-" + month + "-" + dayOfMonth);
            }
        };


        return view;
}
    public void datePickerFrom() {
        Calendar calendar = Calendar.getInstance();
        int year = calendar.get(Calendar.YEAR);
        int month = calendar.get(Calendar.MONTH);
        int day = calendar.get(Calendar.DAY_OF_MONTH);

        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.N) {
            DatePickerDialog dialog = new DatePickerDialog(getActivity(),
                    android.R.style.Theme_Holo_Light_Dialog_MinWidth,
                    mDateListenerFrom,
                    year, month, day
            );

            dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
            dialog.show();

        }
    }


    public void datePickerTo(){
        Calendar calendar = Calendar.getInstance();
        int year = calendar.get(Calendar.YEAR);
        int month = calendar.get(Calendar.MONTH);
        int day = calendar.get(Calendar.DAY_OF_MONTH);

        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.N) {
            DatePickerDialog dialog = new DatePickerDialog(getActivity(),
                    android.R.style.Theme_Holo_Light_Dialog_MinWidth,
                    mDateListenerTo,
                    year, month, day
            );

            dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
            dialog.show();

        }

    }


}
