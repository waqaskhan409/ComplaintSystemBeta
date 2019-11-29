package com.example.complaintsystembeta.adapter;

import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.recyclerview.widget.RecyclerView;

import com.example.complaintsystembeta.R;
import com.example.complaintsystembeta.constants.Constants;
import com.example.complaintsystembeta.model.AllComplains;
import com.example.complaintsystembeta.ui.complaints.ComplainStatistics;
import com.example.complaintsystembeta.ui.complaints.SingleComplainDetails;
import com.google.android.material.snackbar.Snackbar;

import java.util.List;

public class AllComplainsAdapter extends RecyclerView.Adapter<AllComplainsAdapter.AllComplainViewHolder> {

    private static final String TAG = "AllComplainsAdapter";
    public List<AllComplains> allComplains;
    public Context context;
    private String name;
    AllComplains allComplainsObject;
    int positionObject;
    public AllComplainsAdapter(List<AllComplains> allComplains, Context context, String name) {
        this.allComplains = allComplains;
        this.context = context;
        this.name = name;
    }

    @NonNull
    @Override
    public AllComplainViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.layout_for_all_complains,parent, false);

        return new AllComplainViewHolder(view);
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    public void onBindViewHolder(@NonNull AllComplainViewHolder holder, int position) {
        holder.complainsDate.setText(allComplains.get(position).getCreated_us());
        holder.complainsStatus.setText(allComplains.get(position).getComplain_status());
        holder.complainsBody.setText(allComplains.get(position).getComplain_body());
        holder.complainerName.setText(name);
        if(allComplains.get(position).getComplain_status().equals(Constants.COMPLAINS_NEW)) {
            holder.complainsStatus.setBackground(context.getDrawable(R.drawable.new_complains_drawables));
            holder.complainsStatus.setPadding(50, 7, 50, 7);
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                holder.complainsStatus.setTextColor(context.getColor(R.color.white));
            }
        }else if(allComplains.get(position).getComplain_status().equals(Constants.COMPLAINS_RESOLVED)){
            holder.complainsStatus.setBackground(context.getDrawable(R.drawable.pending_complains_drawables));
            holder.complainsStatus.setPadding(50, 7, 50, 7);
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                holder.complainsStatus.setTextColor(context.getColor(R.color.white));
            }} else {
            holder.complainsStatus.setBackground(context.getDrawable(R.drawable.resloved_complains_drawables));
            holder.complainsStatus.setPadding(50, 7, 50, 7);
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                holder.complainsStatus.setTextColor(context.getColor(R.color.white));
            }
        }

        Log.d(TAG, "onBindViewHolder: " + allComplains.get(position).getComplain_id());

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(holder.itemView.getContext(), SingleComplainDetails.class);
                intent.putExtra(holder.itemView.getContext().getString(R.string.complains_id), allComplains.get(position).getComplain_id());
                intent.putExtra(Constants.PREVELDGES_ON_FORWARD, "only view");
                holder.itemView.getContext().startActivity(intent);
            }
        });
        holder.itemView.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                if(holder.complainsStatus.getText().equals(Constants.COMPLAINS_RESOLVED)){
                    Intent intent = new Intent(context, ComplainStatistics.class);
                    intent.putExtra(holder.itemView.getContext().getString(R.string.complains_id), allComplains.get(position).getComplain_id());
                    intent.putExtra(Constants.PREVELDGES_ON_FORWARD, "only view");
                    context.startActivity(intent);

                }
                return false;
            }
        });

    }
    public void deleteItem(int position) {
        if(allComplains.get(position).getComplain_status().equals(Constants.COMPLAINS_RESOLVED)){
            allComplainsObject = allComplains.get(position);
            positionObject = position;
            allComplains.remove(position);
            notifyItemRemoved(position);
        }else {
            Toast.makeText(context, "This complaint is not resolved yet!", Toast.LENGTH_SHORT).show();
        }


//        showUndoSnackbar();
    }
//    private void showUndoSnackbar() {
//        View view = context.findViewById(R.id.coordinator_layout);
//        Snackbar snackbar = Snackbar.make(view, R.string.snack_bar_text,
//                Snackbar.LENGTH_LONG);
//        snackbar.setAction(R.string.snack_bar_undo, v -> undoDelete());
//        snackbar.show();
//    }
//
//    private void undoDelete() {
//        mListItems.add(mRecentlyDeletedItemPosition,
//                mRecentlyDeletedItem);
//        notifyItemInserted(mRecentlyDeletedItemPosition);
//    }

    @Override
    public int getItemCount() {
        return allComplains.size();
    }

    class AllComplainViewHolder extends RecyclerView.ViewHolder{
        private TextView complainerName, complainsDate, complainsBody, complainsStatus;

        public AllComplainViewHolder(@NonNull View itemView) {
            super(itemView);
            complainerName = itemView.findViewById(R.id.complaintsId);
            complainsDate = itemView.findViewById(R.id.complainCreatedDate);
            complainsBody = itemView.findViewById(R.id.complaintsBody);
            complainsStatus = itemView.findViewById(R.id.complaintsStatus);
        }
    }
}