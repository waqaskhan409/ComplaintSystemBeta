package com.example.complaintsystembeta.ui.feedback;


import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.complaintsystembeta.R;

/**
 * A simple {@link Fragment} subclass.
 */
public class Feedback extends Fragment {
    private static final String TAG = "Feedback";
    private View view;

    public Feedback() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_feedback, container, false);
        return view;
    }

}
