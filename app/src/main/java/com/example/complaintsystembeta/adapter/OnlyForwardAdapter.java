package com.example.complaintsystembeta.adapter;

import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.recyclerview.widget.RecyclerView;

import com.example.complaintsystembeta.R;
import com.example.complaintsystembeta.constants.Constants;
import com.example.complaintsystembeta.model.AllComplains;
import com.example.complaintsystembeta.ui.complaints.SingleComplainDetails;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

public class OnlyForwardAdapter extends RecyclerView.Adapter<OnlyForwardAdapter.OnlyForwardViewHolder> {

    private static final String TAG = "AllComplainsAdapter";
    private final String userName;
    private List<AllComplains> allComplains;
    private Context context;
    private String employeId;
    private String decisionForwardToOrFrom;



    public OnlyForwardAdapter(List<AllComplains> allComplains, Context context, String employId, String userName, String decisionForwardToOrFrom) {
        this.allComplains = allComplains;
        this.context = context;
        this.employeId = employId;
        this.userName = userName;
        this.decisionForwardToOrFrom = decisionForwardToOrFrom;
    }


    @NonNull
    @Override
    public OnlyForwardViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.layout_for_all_complains,parent, false);

        return new OnlyForwardViewHolder(view);
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    public void onBindViewHolder(@NonNull OnlyForwardViewHolder holder, int position) {
        holder.complainerName.setText(allComplains.get(position).getAccount_number());
        holder.complainsDate.setText(allComplains.get(position).getCreated_us());
        holder.complainsStatus.setText(allComplains.get(position).getComplain_status());
        holder.complainsBody.setText(allComplains.get(position).getComplain_body());
        try {
            holder.dayElapsed.setText(getDays(allComplains.get(position).getCreated_us()));
        } catch (ParseException e) {
            e.printStackTrace();
        }

//        holder.complainerName.setText(allC);
        Log.d(TAG, "onBindViewHolder: f" + allComplains.get(position).getComplain_id());
        Log.d(TAG, "onBindViewHolder: f" + employeId);
        if(allComplains.get(position).getComplain_status().equals(Constants.COMPLAINS_NEW)) {
            holder.complainsStatus.setBackground(context.getDrawable(R.drawable.new_complains_drawables));
            holder.complainsStatus.setPadding(50, 7, 50, 7);
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                holder.complainsStatus.setTextColor(context.getColor(R.color.white));
            }
        }else if(allComplains.get(position).getComplain_status().equals(Constants.COMPLAINS_RESOLVED)){
            holder.complainsStatus.setBackground(context.getDrawable(R.drawable.resloved_complains_drawables));
            holder.complainsStatus.setPadding(50, 7, 50, 7);
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                holder.complainsStatus.setTextColor(context.getColor(R.color.white));
            }
        } else {
            holder.complainsStatus.setBackground(context.getDrawable(R.drawable.pending_complains_drawables));
            holder.complainsStatus.setPadding(50, 7, 50, 7);
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                holder.complainsStatus.setTextColor(context.getColor(R.color.white));
            }
        }


        Log.d(TAG, "onBindViewHolder: userName:" + allComplains.get(position).getComplain_id());
//        Log.d(TAG, "onBindViewHolder: emplyee Id:" + allComplains.get(position).g);

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(holder.itemView.getContext(), SingleComplainDetails.class);
                intent.putExtra(holder.itemView.getContext().getString(R.string.complains_id), allComplains.get(position).getComplain_id());
                intent.putExtra(Constants.PREVELDGES_ON_FORWARD, "admin");
                intent.putExtra(Constants.ONLY_FORWARDED_COMPLAIN_SHOW, "admin");
                intent.putExtra(Constants.DESIGNATION_ID, userName);
                intent.putExtra(Constants.EMPLOYEE_ID, employeId);
                intent.putExtra(context.getString(R.string.permanentlogin_name), employeId);
                intent.putExtra(Constants.CHOICE, decisionForwardToOrFrom);
                holder.itemView.getContext().startActivity(intent);
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


    @Override
    public int getItemCount() {
        return allComplains.size();
    }

    class OnlyForwardViewHolder extends RecyclerView.ViewHolder{
        private TextView complainerName, complainsDate, complainsBody, complainsStatus, dayElapsed;
        private ImageView imageViewCircle1, imageViewCircle2;
        private TextView textView1, textView2, textView3;
        public OnlyForwardViewHolder(@NonNull View itemView) {
            super(itemView);
            complainerName = itemView.findViewById(R.id.complaintsId);
            complainsDate = itemView.findViewById(R.id.complainCreatedDate);
            complainsBody = itemView.findViewById(R.id.complaintsBody);
            complainsStatus = itemView.findViewById(R.id.complaintsStatus);
            dayElapsed = itemView.findViewById(R.id.complainDate);

//
//            imageViewCircle1 = itemView.findViewById(R.id.circle1);
//            imageViewCircle2 = itemView.findViewById(R.id.circle2);
//            textView1 = itemView.findViewById(R.id.textView3);
//            textView2 = itemView.findViewById(R.id.textView2);
//            textView3 = itemView.findViewById(R.id.textView3);

        }
    }
}
