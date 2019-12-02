package com.example.complaintsystembeta.adapter;

import android.content.Context;
import android.os.Build;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Filter;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;

import com.example.complaintsystembeta.R;
import com.example.complaintsystembeta.model.Employee;

import java.util.ArrayList;
import java.util.List;

public class AutoCompleteAdapter extends ArrayAdapter<Employee> {
    private static final String TAG = "AutoCompleteAdapter";
    private List<Employee> allEmployee ;
    private Context context ;

    public AutoCompleteAdapter(@NonNull Context context, @NonNull List<Employee> objects) {
        super(context, 0, objects);
        this.context = context  ;
        allEmployee = new ArrayList<>(objects);
    }


    @NonNull
    @Override
    public Filter getFilter() {
        return employeeFilter;
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        if(convertView == null){
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.employee_detail, parent, false);
        }
        TextView name, designation, department, id, total, sideBorder;
        name = convertView.findViewById(R.id.name);
        designation = convertView.findViewById(R.id.designation);
        department = convertView.findViewById(R.id.department);
        id = convertView.findViewById(R.id.employeeId);
        total = convertView.findViewById(R.id.totalComplains);
        sideBorder = convertView.findViewById(R.id.sideBorder);

        Employee employee = getItem(position);
        if(employee != null){
            name.setText(employee.getFull_name());
            designation.setText(employee.getDes_title());
            department.setText(employee.getDepartment_name());
            id.setText(employee.getEmployee_id());
            total.setText("0");
        }

        if(position%4 == 0){
            sideBorder.setBackground(context.getDrawable(R.drawable.new_complains_drawables));
        }
        else if(position%4 == 1){
            sideBorder.setBackground(context.getDrawable(R.drawable.pending_complains_drawables));
        }
        else if(position%4 == 2){
            sideBorder.setBackground(context.getDrawable(R.drawable.resloved_complains_drawables));
        }else if(position%4 == 3){
            sideBorder.setBackground(context.getDrawable(R.drawable.complain_forward_drawable));
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
                    if(emplyee.getFull_name().toLowerCase().contains(filterPattern)||
                            emplyee.getDepartment_name().toLowerCase().contains(filterPattern)||
                            emplyee.getEmployee_id().toLowerCase().contains(filterPattern)||
                            emplyee.getDes_title().toLowerCase().contains(filterPattern)
                    ){
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
