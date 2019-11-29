package com.example.complaintsystembeta.adapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.complaintsystembeta.R;
import com.example.complaintsystembeta.constants.Constants;
import com.example.complaintsystembeta.model.AllComplains;
import com.example.complaintsystembeta.ui.complaints.SingleComplainDetails;

import java.util.List;

public class ConsumerComplaints extends RecyclerView.Adapter<ConsumerComplaints.ConsumerComplaintViewHolder> {

    private static final String TAG = "ConsumerComplaints";
    private List<AllComplains> allComplains;
    private Context context;
    private String name;

    public ConsumerComplaints(List<AllComplains> allComplains, Context context, String name) {
        this.allComplains = allComplains;
        this.context = context;
        this.name = name;
    }


    @NonNull
    @Override
    public ConsumerComplaintViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.layout_for_all_consumer_complains,parent, false);

        return new ConsumerComplaintViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ConsumerComplaintViewHolder holder, int position) {
        holder.complainsStatus.setText( allComplains.get(position).getComplain_status());
        holder.complainsDate.setText("10/10/1000");
//        holder.complainsDate.setText( allComplains.get(position).getCreated_us());
        holder.complainsBody.setText( allComplains.get(position).getComplain_body());
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(holder.itemView.getContext(), SingleComplainDetails.class);
                intent.putExtra(holder.itemView.getContext().getString(R.string.complains_id), allComplains.get(position).getComplain_id());
                intent.putExtra(Constants.PREVELDGES_ON_FORWARD, "normal_user");
                holder.itemView.getContext().startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return allComplains.size();
    }

    class ConsumerComplaintViewHolder extends RecyclerView.ViewHolder{
        private TextView complainsDate, complainsBody, complainsStatus;

        public ConsumerComplaintViewHolder(@NonNull View itemView) {
            super(itemView);
            complainsBody = itemView.findViewById(R.id.complaintsBody);
            complainsDate = itemView.findViewById(R.id.complainCreatedDate);
            complainsStatus = itemView.findViewById(R.id.complaintsStatus);

        }
    }
}
