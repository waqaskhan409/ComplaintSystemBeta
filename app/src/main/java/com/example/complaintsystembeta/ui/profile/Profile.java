package com.example.complaintsystembeta.ui.profile;


import android.content.Context;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.complaintsystembeta.R;


/**
 * A simple {@link Fragment} subclass.
 */
public class Profile extends Fragment {
    private static final String TAG = "Profile";
    private View view;


    public Profile() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_profile, container, false);
        return view;
    }




}
