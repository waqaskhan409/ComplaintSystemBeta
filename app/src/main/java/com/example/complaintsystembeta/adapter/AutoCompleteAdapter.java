package com.example.complaintsystembeta.adapter;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Filter;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.example.complaintsystembeta.R;
import com.example.complaintsystembeta.model.Employee;

import java.util.ArrayList;
import java.util.List;

public class AutoCompleteAdapter extends ArrayAdapter<Employee> {
    private static final String TAG = "AutoCompleteAdapter";
    private List<Employee> allEmployee ;

    public AutoCompleteAdapter(@NonNull Context context, @NonNull List<Employee> objects) {
        super(context, 0, objects);
        allEmployee = new ArrayList<>(objects);
    }


    @NonNull
    @Override
    public Filter getFilter() {
        return employeeFilter;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        if(convertView == null){
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.employee_detail, parent, false);
        }
        TextView name, designation, department;
        name = convertView.findViewById(R.id.name);
        designation = convertView.findViewById(R.id.designation);
        department = convertView.findViewById(R.id.department);

        Employee employee = getItem(position);
        if(employee != null){
            name.setText(employee.getFull_name());
            designation.setText(employee.getDes_title());
            department.setText(employee.getDepartment_name());
        }

        return convertView;
    }

    private Filter employeeFilter = new Filter() {
        @Override
        protected FilterResults performFiltering(CharSequence constraint) {
            FilterResults results = new FilterResults();
            List<Employee> suggestions = new ArrayList<>();
            Log.d(TAG, "performFiltering: " + constraint);
            Log.d(TAG, "performFiltering: " + allEmployee.size());
            Log.d(TAG, "performFiltering: " + allEmployee);
            if(constraint == null || constraint.length() == 0){
                suggestions.addAll(allEmployee);
                Log.d(TAG, "performFiltering: null");

            }else {
                String filterPattern = constraint.toString().toLowerCase().trim();

                for(Employee emplyee : allEmployee){
                    Log.d(TAG, "performFiltering: " + emplyee.getFull_name());
                    Log.d(TAG, "performFiltering: " + filterPattern);
                    if(emplyee.getFull_name().toLowerCase().contains(filterPattern)){
                        suggestions.add(emplyee);
                    }
                }
            }
            results.values = suggestions;
            results.count = suggestions.size();
            return results;
        }

        @Override
        protected void publishResults(CharSequence constraint, FilterResults results) {
            clear();
            addAll((List) results.values);
            notifyDataSetChanged();
        }

        @Override
        public CharSequence convertResultToString(Object resultValue) {
            Employee employee = (Employee) resultValue;

            String returnValue = employee.getEmployee_id()+ "/" +employee.getFull_name() + "/"+ employee.getDes_title() + "/" + employee.getDepartment_name() ;
            return returnValue;

        }

    };
}
