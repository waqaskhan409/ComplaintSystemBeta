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
import com.example.complaintsystembeta.ui.complaints.ComposeComplaints;
import com.example.complaintsystembeta.ui.complaints.ManagingComplaints;
import com.google.android.material.bottomsheet.BottomSheetDialogFragment;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import butterknife.ButterKnife;
import butterknife.Unbinder;

public class BottomSheetDialogueFilterSearch extends BottomSheetDialogFragment {
    private static final String TAG = "BottomSheetDialogueCompose";
    private View view;
    public LinearLayout linearLayoutCamera, linearLayoutGallery;
    private Unbinder unbinder;
    private DatePickerDialog.OnDateSetListener mDateListenerTo, mDateListenerFrom;


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.dialog_advance_search, container, false);
        if (unbinder == null) {
            unbinder = ButterKnife.bind(getActivity(), view);
        }


        Spinner spinnerAdvance = view.findViewById(R.id.spinnerAdvance);

        List<String> list = new ArrayList<>();
        list.add(Constants.ALL_COMPLAINS);
        list.add(Constants.COMPLAINS_NEW);
        list.add(Constants.COMPLAINS_PENDING);
        list.add(Constants.COMPLAINS_RESOLVED);
        ArrayAdapter adapter = new ArrayAdapter(getContext(), android.R.layout.simple_list_item_1, list);
        spinnerAdvance.setAdapter(adapter);

        Button cancel, submit;
        cancel = view.findViewById(R.id.cancel);
        submit = view.findViewById(R.id.submit);


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


        submit.setOnClickListener(new View.OnClickListener() {
            @SuppressLint("LongLogTag")
            @Override
            public void onClick(View v) {
                Log.d(TAG, "onClick: Submited");
                String dateTo, dateFrom, status;
                dateTo = edTo.getText().toString().trim();
                dateFrom = edFrom.getText().toString().trim();
                status = spinnerAdvance.getSelectedItem().toString();
                if (dateTo.equals("") || dateFrom.equals("")) {
                    Log.d(TAG, "onClick: dates is not selected");
                    dismiss();
                } else {
                    ((ManagingComplaints) getActivity()).getDataFromServer(dateTo, dateFrom, status);
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
