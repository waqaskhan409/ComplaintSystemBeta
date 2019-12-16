package com.example.complaintsystembeta.adapter;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.os.Bundle;
import android.os.Parcelable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.content.res.AppCompatResources;
import androidx.core.graphics.drawable.DrawableCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.example.complaintsystembeta.R;
import com.example.complaintsystembeta.model.Employee;
import com.example.complaintsystembeta.ui.profile.DetailProfile;

import java.io.Serializable;
import java.util.ArrayList;

public class AllEmployeesAdapter extends RecyclerView.Adapter<AllEmployeesAdapter.AllEmployeeViewHolder> {
    private static final String TAG = "AllEmployeesAdapter";
    private ArrayList<Employee> allEmployees = new ArrayList();

    public AllEmployeesAdapter(ArrayList<Employee> allEmployees) {
        this.allEmployees = allEmployees;
    }

    @NonNull
    @Override
    public AllEmployeeViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.layout_for_all_employees,parent, false);
        Log.d(TAG, "onCreateViewHolder: called!");

        return new AllEmployeeViewHolder(view);
    }

    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    public void onBindViewHolder(@NonNull AllEmployeeViewHolder holder, int position) {
        Log.d(TAG, "onBindViewHolder: called!");
        if(allEmployees.get(position).getTotal() != null){
            holder.delayTotal.setVisibility(View.VISIBLE);
            holder.delayTotal.append(allEmployees.get(position).getTotal());
        }
        holder.employeeName.setText(allEmployees.get(position).getFull_name());
        holder.employeeDesignation.setText(allEmployees.get(position).getDes_title());
        holder.employeeTag.setText(allEmployees.get(position).getFull_name().toUpperCase().charAt(0)+"" );
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(holder.itemView.getContext(), DetailProfile.class);
                intent.putExtra(holder.itemView.getContext().getString(R.string.detail_employee),  allEmployees.get(position));
                holder.itemView.getContext().startActivity(intent);
            }
        });



        if(position%6 == 0){
            Drawable unwrappedDrawable = AppCompatResources.getDrawable(holder.itemView.getContext(), R.drawable.all_employees);
            Drawable wrappedDrawable = DrawableCompat.wrap(unwrappedDrawable);
            DrawableCompat.setTint(wrappedDrawable, holder.itemView.getContext().getColor(R.color.all));
        }else if(position%6 == 1){
            Drawable unwrappedDrawable = AppCompatResources.getDrawable(holder.itemView.getContext(), R.drawable.all_employees);
            Drawable wrappedDrawable = DrawableCompat.wrap(unwrappedDrawable);
            DrawableCompat.setTint(wrappedDrawable, holder.itemView.getContext().getColor(R.color.new_complains));

        }else if(position%6 == 2){
            Drawable unwrappedDrawable = AppCompatResources.getDrawable(holder.itemView.getContext(), R.drawable.all_employees);
            Drawable wrappedDrawable = DrawableCompat.wrap(unwrappedDrawable);
            DrawableCompat.setTint(wrappedDrawable, holder.itemView.getContext().getColor(R.color.pending));

        }else if(position%6 == 3){
            Drawable unwrappedDrawable = AppCompatResources.getDrawable(holder.itemView.getContext(), R.drawable.all_employees);
            Drawable wrappedDrawable = DrawableCompat.wrap(unwrappedDrawable);
            DrawableCompat.setTint(wrappedDrawable, holder.itemView.getContext().getColor(R.color.resolved));

        }else if(position%6 == 4){
            Drawable unwrappedDrawable = AppCompatResources.getDrawable(holder.itemView.getContext(), R.drawable.all_employees);
            Drawable wrappedDrawable = DrawableCompat.wrap(unwrappedDrawable);
            DrawableCompat.setTint(wrappedDrawable, holder.itemView.getContext().getColor(R.color.analytics));

        }else if(position%6 == 5){
            Drawable unwrappedDrawable = AppCompatResources.getDrawable(holder.itemView.getContext(), R.drawable.all_employees);
            Drawable wrappedDrawable = DrawableCompat.wrap(unwrappedDrawable);
            DrawableCompat.setTint(wrappedDrawable, holder.itemView.getContext().getColor(R.color.forward));

        }
    }

    @Override
    public int getItemCount() {
        return allEmployees.size();
    }

    class AllEmployeeViewHolder extends RecyclerView.ViewHolder{
        private TextView employeeName, employeeDesignation, employeeTag, delayTotal;

        public AllEmployeeViewHolder(@NonNull View itemView) {
            super(itemView);
            employeeName = itemView.findViewById(R.id.employeeName);
            employeeDesignation = itemView.findViewById(R.id.employeeDesignation);
            employeeTag = itemView.findViewById(R.id.employeeTag);
            delayTotal = itemView.findViewById(R.id.delayTotal);
        }
    }

}
