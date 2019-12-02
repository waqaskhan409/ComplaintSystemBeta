package com.example.complaintsystembeta.adapter;

import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
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

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    public void onBindViewHolder(@NonNull ConsumerComplaintViewHolder holder, int position) {
        holder.complainsStatus.setText( allComplains.get(position).getComplain_status());
        holder.complainsDate.setText(allComplains.get(position).getCreated_us());
//        holder.complainsDate.setText( allComplains.get(position).getCreated_us());
        holder.complainsBody.setText( allComplains.get(position).getComplain_body());

        if(allComplains.get(position).getComplain_status().equals(Constants.COMPLAINS_RESOLVED)){
            holder.complainsStatus.setBackground(context.getDrawable(R.drawable.resloved_complains_drawables));
            holder.complainsStatus.setPadding(50, 7, 50, 7);
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                holder.complainsStatus.setTextColor(context.getColor(R.color.white));
            }
        }else  if(allComplains.get(position).getComplain_status().equals(Constants.COMPLAINS_PENDING)){
            holder.complainsStatus.setBackground(context.getDrawable(R.drawable.pending_complains_drawables));
            holder.complainsStatus.setPadding(50, 7, 50, 7);
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                holder.complainsStatus.setTextColor(context.getColor(R.color.white));
            }
        }else  if(allComplains.get(position).getComplain_status().equals(Constants.COMPLAINS_NEW)){
            holder.complainsStatus.setBackground(context.getDrawable(R.drawable.new_complains_drawables));
            holder.complainsStatus.setPadding(50, 7, 50, 7);
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                holder.complainsStatus.setTextColor(context.getColor(R.color.white));
            }
        }





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
        private TextView complainTitle, complainBar;
        public ConsumerComplaintViewHolder(@NonNull View itemView) {
            super(itemView);
            complainsBody = itemView.findViewById(R.id.complaintsBody);
            complainsDate = itemView.findViewById(R.id.complainCreatedDate);
            complainsStatus = itemView.findViewById(R.id.complaintsStatus);
            complainTitle = itemView.findViewById(R.id.complaintsTitle);
            complainBar = itemView.findViewById(R.id.complainBar);
        }
    }
}
