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

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

public class ForwardAdapter  extends RecyclerView.Adapter<ForwardAdapter.ForwardAdapterViewHolder>{

    private static final String TAG = "AllComplainsAdapter";
    public List<ReportForward> allReports;
    private List<Employee> allEmployees;
    private Context context;
    private String name;
    private String desId;
    private String decisionForwardToOrFrom;

    public ForwardAdapter(List<ReportForward> allReports, Context context, String desId, String name, List<Employee> listEmployees, String decisionForwardToOrFrom) {
        this.allReports = allReports;
        this.context = context;
        this.name = name;
        this.desId = desId;
        this.allEmployees = listEmployees;
        this.decisionForwardToOrFrom = decisionForwardToOrFrom;

    }

    @NonNull
    @Override
    public ForwardAdapterViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.layout_for_reporting_complains,parent, false);

        return new ForwardAdapterViewHolder(view);
    }

    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    public void onBindViewHolder(@NonNull ForwardAdapterViewHolder holder, int position) {
        holder.complainsDate.setText("Suggested Date: " + allReports.get(position).getSuggested_date_reply());
        holder.forwardTo.setText("Forwarded to: " + getTextFromEmployee(allReports.get(position).getForwards_to()));
        holder.forwardFrom.setText("Forwarded from: " + getTextFromEmployee(allReports.get(position).getForwards_by()));

        try {
            holder.days.setText(getDays(allReports.get(position).getForwards_date()));
        } catch (ParseException e) {
            e.printStackTrace();
        }

        if(allReports.get(position).getIs_delay().equals("1")){
            holder.itemView.setBackground(context.getDrawable(R.drawable.complains_analytics_drawable));
            holder.complainBody.setTextColor(context.getColor(R.color.white));
        }
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
                intent.putExtra(Constants.CHOICE, decisionForwardToOrFrom);
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
        private TextView complainBody, complainsDate, forwardTo, forwardFrom, days;
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
            days = itemView.findViewById(R.id.days);

//            imageViewCircle1 = itemView.findViewById(R.id.circle1);
//            imageViewCircle2 = itemView.findViewById(R.id.circle2);
//            textView1 = itemView.findViewById(R.id.textView3);
//            textView2 = itemView.findViewById(R.id.textView2);
//            textView3 = itemView.findViewById(R.id.textView3);


        }
    }


}
