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
import com.example.complaintsystembeta.model.Employee;
import com.example.complaintsystembeta.model.ReportForward;
import com.example.complaintsystembeta.ui.complaints.SingleForwardRecordDetail;

import java.util.List;

public class ForwardAdapter  extends RecyclerView.Adapter<ForwardAdapter.ForwardAdapterViewHolder>{

    private static final String TAG = "AllComplainsAdapter";
    public List<ReportForward> allReports;
    private List<Employee> allEmployees;
    private Context context;
    private String name;
    private String desId;

    public ForwardAdapter(List<ReportForward> allReports, Context context, String desId, String name, List<Employee> listEmployees) {
        this.allReports = allReports;
        this.context = context;
        this.name = name;
        this.desId = desId;
        this.allEmployees = listEmployees;

    }

    @NonNull
    @Override
    public ForwardAdapterViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.layout_for_reporting_complains,parent, false);

        return new ForwardAdapterViewHolder(view);
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    public void onBindViewHolder(@NonNull ForwardAdapterViewHolder holder, int position) {
        holder.complainsDate.setText("Suggested Date: " + allReports.get(position).getSuggested_date_reply());
        holder.forwardTo.setText("Forwarded to: " + getTextFromEmployee(allReports.get(position).getForwards_to()));
        holder.forwardFrom.setText("Forwarded from: " + getTextFromEmployee(allReports.get(position).getForwards_by()));
        if(allReports.get(position).getIs_seen() !=null ) {
            if (allReports.get(position).getIs_seen().equals("1")) {
                holder.imageView.setImageDrawable(context.getDrawable(R.drawable.seen));
            }
        }
        if(allReports.get(position).getIs_seen() !=null && allReports.get(position).getIs_acknowledged() !=null ) {
            if (allReports.get(position).getIs_seen().equals("1") && allReports.get(position).getIs_acknowledged().equals("1")) {
                holder.imageView.setImageDrawable(context.getDrawable(R.drawable.acknowledged));
            }
        }
        if(allReports.get(position).getIs_public().equals("1") || name.equals("admin")){
            holder.complainBody.setText(allReports.get(position).getForwards_message());
        }else {
            holder.complainBody.setText("Private message");

        }

//
//        if((position%6) == 0){
//            holder.textView1.setBackground(context.getDrawable(R.drawable.all_complains_drawables));
//            holder.textView3.setBackground(context.getDrawable(R.drawable.all_complains_drawables));
//            holder.textView2.setBackground(context.getDrawable(R.drawable.all_complains_drawables));
//            holder.imageViewCircle1.setBackground(context.getDrawable(R.drawable.all_complains_drawables));
//            holder.imageViewCircle2.setBackground(context.getDrawable(R.drawable.all_complains_drawables));
//        }else if((position%6) == 1){
//            holder.textView1.setBackground(context.getDrawable(R.drawable.new_complains_drawables));
//            holder.textView3.setBackground(context.getDrawable(R.drawable.new_complains_drawables));
//            holder.textView2.setBackground(context.getDrawable(R.drawable.new_complains_drawables));
//            holder.imageViewCircle1.setBackground(context.getDrawable(R.drawable.new_complains_drawables));
//            holder.imageViewCircle2.setBackground(context.getDrawable(R.drawable.new_complains_drawables));
//        }else if((position%6) == 2){
//            holder.textView1.setBackground(context.getDrawable(R.drawable.pending_complains_drawables));
//            holder.textView3.setBackground(context.getDrawable(R.drawable.pending_complains_drawables));
//            holder.textView2.setBackground(context.getDrawable(R.drawable.pending_complains_drawables));
//            holder.imageViewCircle1.setBackground(context.getDrawable(R.drawable.pending_complains_drawables));
//            holder.imageViewCircle2.setBackground(context.getDrawable(R.drawable.pending_complains_drawables));
//        }else if((position%6) == 3){
//            holder.textView1.setBackground(context.getDrawable(R.drawable.resloved_complains_drawables));
//            holder.textView3.setBackground(context.getDrawable(R.drawable.resloved_complains_drawables));
//            holder.textView2.setBackground(context.getDrawable(R.drawable.resloved_complains_drawables));
//            holder.imageViewCircle1.setBackground(context.getDrawable(R.drawable.resloved_complains_drawables));
//            holder.imageViewCircle2.setBackground(context.getDrawable(R.drawable.resloved_complains_drawables));
//        }else if((position%6) == 4){
//            holder.textView1.setBackground(context.getDrawable(R.drawable.complain_forward_drawable));
//            holder.textView3.setBackground(context.getDrawable(R.drawable.complain_forward_drawable));
//            holder.textView2.setBackground(context.getDrawable(R.drawable.complain_forward_drawable));
//            holder.imageViewCircle1.setBackground(context.getDrawable(R.drawable.complain_forward_drawable));
//            holder.imageViewCircle2.setBackground(context.getDrawable(R.drawable.complain_forward_drawable));
//        }else if((position%6) == 5){
//            holder.textView1.setBackground(context.getDrawable(R.drawable.complains_analytics_drawable));
//            holder.textView3.setBackground(context.getDrawable(R.drawable.complains_analytics_drawable));
//            holder.textView2.setBackground(context.getDrawable(R.drawable.complains_analytics_drawable));
//            holder.imageViewCircle1.setBackground(context.getDrawable(R.drawable.complains_analytics_drawable));
//            holder.imageViewCircle2.setBackground(context.getDrawable(R.drawable.complains_analytics_drawable));
//        }
//



        Log.d(TAG, "onBindViewHolder: "+allReports.get(position).getComplains_reporting_id());
        Log.d(TAG, "onBindViewHolder: "+allReports.get(position).getForwards_by());
        Log.d(TAG, "onBindViewHolder: "+allReports.get(position).getForwards_to());
        Log.d(TAG, "onBindViewHolder: testing"+desId);

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, SingleForwardRecordDetail.class);
                intent.putExtra(Constants.REPORTING_ID, allReports.get(position).getComplains_reporting_id());
                intent.putExtra(Constants.DESIGNATION_TITLE, allReports.get(position).getForwards_to()+"/"
                        + getNameFromEmployee(allReports.get(position).getForwards_to()) +"/"
                        + getTextFromEmployee(allReports.get(position).getForwards_to()) +"/"
                        +getDeptFromEmployee(allReports.get(position).getForwards_to()));
                intent.putExtra(Constants.FORWARD_TO, allReports.get(position).getForwards_to());
                intent.putExtra(Constants.FORWARD_FROM, allReports.get(position).getForwards_by());
                intent.putExtra(Constants.FORWARD_FROM_NAME_ID_DES, allReports.get(position).getForwards_by()+"/"
                        + getNameFromEmployee(allReports.get(position).getForwards_by()) + "/"
                        + getTextFromEmployee(allReports.get(position).getForwards_by()) + "/"
                        + getDeptFromEmployee(allReports.get(position).getForwards_by()));
                intent.putExtra(Constants.REMARKS_BODY, holder.complainBody.getText().toString());
                intent.putExtra(Constants.SUGGEST_DATE, allReports.get(position).getSuggested_date_reply());
                intent.putExtra(context.getString(R.string.complains_id), allReports.get(position).getComplain_id());
                intent.putExtra(Constants.SUGGEST_DATE, allReports.get(position).getSuggested_date_reply());
                intent.putExtra(Constants.PREVELDGES_ON_FORWARD, name);
                intent.putExtra(Constants.IS_CURRENT, allReports.get(position).isIs_current());
                intent.putExtra(Constants.IS_REPLY, allReports.get(position).isIs_reply());
                intent.putExtra(context.getString(R.string.permanentlogin_name), desId);
                context.startActivity(intent);
            }
        });

    }

    private String getTextFromEmployee(String employeeId) {
        for (int i = 0; i < allEmployees.size(); i++) {
            Log.d(TAG, "getTextFromEmployee: " + allEmployees.get(i).getEmployee_id());
            if(allEmployees.get(i).getEmployee_id() != null){
                if(allEmployees.get(i).getEmployee_id().equals(employeeId)){
                    return allEmployees.get(i).getDes_title();
                }
            }
        }


        return "";
    }
    private String getDeptFromEmployee(String employeeId) {
        for (int i = 0; i < allEmployees.size(); i++) {
            Log.d(TAG, "getTextFromEmployee: " + allEmployees.get(i).getEmployee_id());
            if(allEmployees.get(i).getEmployee_id() != null){
                if(allEmployees.get(i).getEmployee_id().equals(employeeId)){
                    return allEmployees.get(i).getDepartment_name();
                }
            }
        }


        return "";
    }

    private String getNameFromEmployee(String employeeId) {
        for (int i = 0; i < allEmployees.size(); i++) {
            Log.d(TAG, "getTextFromEmployee: " + allEmployees.get(i).getEmployee_id());
            if(allEmployees.get(i).getEmployee_id() != null){
                if(allEmployees.get(i).getEmployee_id().equals(employeeId)){
                    return allEmployees.get(i).getFull_name();
                }
            }
        }


        return "";
    }

    @Override
    public int getItemCount() {
        return allReports.size();
    }

    static class ForwardAdapterViewHolder extends RecyclerView.ViewHolder{
        private TextView complainBody, complainsDate, forwardTo, forwardFrom;
        private ImageView imageView;

        private ImageView imageViewCircle1, imageViewCircle2;
        private TextView textView1, textView2, textView3;
        public ForwardAdapterViewHolder(@NonNull View itemView) {
            super(itemView);
            complainBody = itemView.findViewById(R.id.complaintsBody);
            complainsDate = itemView.findViewById(R.id.expectedDate);
            forwardTo = itemView.findViewById(R.id.forwardTo);
            forwardFrom = itemView.findViewById(R.id.forwardBy);
            imageView = itemView.findViewById(R.id.imageForSeen);

            imageViewCircle1 = itemView.findViewById(R.id.circle1);
            imageViewCircle2 = itemView.findViewById(R.id.circle2);
            textView1 = itemView.findViewById(R.id.textView3);
            textView2 = itemView.findViewById(R.id.textView2);
            textView3 = itemView.findViewById(R.id.textView3);


        }
    }


}
