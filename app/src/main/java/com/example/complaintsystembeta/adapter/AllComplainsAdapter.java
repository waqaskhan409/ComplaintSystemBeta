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

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.temporal.ChronoUnit;
import java.util.Calendar;
import java.util.Date;
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
        holder.complainerName.setText(allComplains.get(position).getAccount_number());
        Log.d(TAG, "onBindViewHolder Date: " + allComplains.get(position).getCreated_us());
        holder.dayElapsed.setText(allComplains.get(position).getDays() );
        if(allComplains.get(position).getComplain_status().equals(Constants.COMPLAINS_NEW)) {
            holder.complainsStatus.setBackground(context.getDrawable(R.drawable.new_complains_drawables));
            holder.leftborder.setBackground(context.getDrawable(R.drawable.new_complains_drawables));
            holder.complainsStatus.setPadding(50, 7, 50, 7);
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                holder.complainsStatus.setTextColor(context.getColor(R.color.white));
            }
        }else if(allComplains.get(position).getComplain_status().equals(Constants.COMPLAINS_RESOLVED)){
            holder.complainsStatus.setBackground(context.getDrawable(R.drawable.resloved_complains_drawables));
            holder.leftborder.setBackground(context.getDrawable(R.drawable.resloved_complains_drawables));
            holder.complainsStatus.setPadding(50, 7, 50, 7);
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                holder.complainsStatus.setTextColor(context.getColor(R.color.white));
            }
        } else {
            holder.complainsStatus.setBackground(context.getDrawable(R.drawable.pending_complains_drawables));
            holder.leftborder.setBackground(context.getDrawable(R.drawable.pending_complains_drawables));
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
//                if(holder.complainsStatus.getText().equals(Constants.COMPLAINS_RESOLVED)){
                    Intent intent = new Intent(context, ComplainStatistics.class);
                    intent.putExtra(holder.itemView.getContext().getString(R.string.complains_id), allComplains.get(position).getComplain_id());
                    intent.putExtra(Constants.PREVELDGES_ON_FORWARD, "only view");
                    intent.putExtra(Constants.STATUS_COMPLAIN,  holder.complainsStatus.getText().toString());
                    context.startActivity(intent);

//                }
                return false;
            }
        });

    }

    private String getDays(String created_us) throws ParseException {
       String[] arr = created_us.split("T");
        String[] createdDate = arr[0].split("-");
       /* int year = Integer.parseInt(createdDate[0]);
        int month = Integer.parseInt(createdDate[1]);
        int day = Integer.parseInt(createdDate[2]);

        int currentMonth =  Calendar.getInstance().get(Calendar.MONTH)+1;
        int currentDay =  Calendar.getInstance().get(Calendar.DAY_OF_MONTH);
        int currentYear =  Calendar.getInstance().get(Calendar.YEAR);*/

        Date dateEarly=new SimpleDateFormat("yyyy-MM-dd").parse(arr[0]);
        Date dateLater = new Date();

        long a = (dateLater.getTime() - dateEarly.getTime()) / (24 * 60 * 60 * 1000);

        Log.d(TAG, "getDays: " + a);
        if ( a == 0){
            return "Today";
        }else if( a == 1){
            return "Yesterday";
        }



        return String.valueOf(a) + " Days ago";
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
        private TextView complainerName, complainsDate, complainsBody, complainsStatus, leftborder, dayElapsed;

        public AllComplainViewHolder(@NonNull View itemView) {
            super(itemView);
            complainerName = itemView.findViewById(R.id.complaintsId);
            complainsDate = itemView.findViewById(R.id.complainCreatedDate);
            complainsBody = itemView.findViewById(R.id.complaintsBody);
            complainsStatus = itemView.findViewById(R.id.complaintsStatus);
            leftborder = itemView.findViewById(R.id.leftBorder);
            dayElapsed = itemView.findViewById(R.id.complainDate);
        }
    }
}
