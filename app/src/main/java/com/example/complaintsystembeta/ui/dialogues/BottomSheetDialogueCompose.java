package com.example.complaintsystembeta.ui.dialogues;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.example.complaintsystembeta.R;
import com.example.complaintsystembeta.ui.complaints.ComposeComplaints;
import com.google.android.material.bottomsheet.BottomSheetDialogFragment;

public class BottomSheetDialogueCompose extends BottomSheetDialogFragment {
    private static final String TAG = "BottomSheetDialogueCompose";
    private View view;
    public LinearLayout linearLayoutCamera, linearLayoutGallery;


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.attachement_layout, container, false);

        linearLayoutCamera = view.findViewById(R.id.cameralayout);
        linearLayoutGallery = view.findViewById(R.id.galleryLayout);

        linearLayoutGallery.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ((ComposeComplaints)getActivity()).gallery();
                dismiss();
            }
        });
        linearLayoutCamera.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ((ComposeComplaints)getActivity()).camera();
                dismiss();
            }
        });


        return view;
    }
}
