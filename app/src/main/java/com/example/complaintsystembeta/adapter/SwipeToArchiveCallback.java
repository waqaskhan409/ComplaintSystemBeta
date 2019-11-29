package com.example.complaintsystembeta.adapter;

import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.RecyclerView;

import com.example.complaintsystembeta.constants.Constants;

public class SwipeToArchiveCallback  extends ItemTouchHelper.SimpleCallback {
    private AllComplainsAdapter mAdapter;

    public SwipeToArchiveCallback(AllComplainsAdapter adapter) {
        super(0, ItemTouchHelper.LEFT | ItemTouchHelper.RIGHT);
        mAdapter = adapter;
    }

    @Override
    public boolean onMove(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder, @NonNull RecyclerView.ViewHolder target) {
        int position = viewHolder.getAdapterPosition();
        if(mAdapter.allComplains.get(position).equals(Constants.COMPLAINS_RESOLVED)){
            mAdapter.deleteItem(position);
        }else{
            Toast.makeText(mAdapter.context, "This complaint is not resolved yet!", Toast.LENGTH_SHORT).show();

        }
        return false;
    }

    @Override
    public void onSwiped(@NonNull RecyclerView.ViewHolder viewHolder, int direction) {


    }
}