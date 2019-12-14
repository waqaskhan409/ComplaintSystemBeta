package com.example.complaintsystembeta.ui.profile;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.complaintsystembeta.R;
import com.example.complaintsystembeta.model.Employee;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;

public class DetailProfile extends AppCompatActivity {
    private static final String TAG = "DetailProfile";
    private Employee employee;
    private Unbinder unbinder;
    private Bundle data;


    @BindView(R.id.deepee)
    ImageView deepee;

    @BindView(R.id.accountNumber)
    TextView cnicTv;

    @BindView(R.id.name)
    TextView nameTv;


    @BindView(R.id.email)
    TextView emailEd;

    @BindView(R.id.fatherName)
    TextView fatherName;

    @BindView(R.id.appointedDate)
    TextView appointeddate;

    @BindView(R.id.birthDate)
    TextView birthDate;

    @BindView(R.id.gender)
    TextView gender;

    @BindView(R.id.local)
    TextView local;

    @BindView(R.id.lastUpdate)
    TextView lastUpdate;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail_profile);
        unbinder = ButterKnife.bind(this);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        data = getIntent().getExtras();

        employee = (Employee) data.getSerializable(getString(R.string.detail_employee));

    }

    @Override
    protected void onStart() {
        super.onStart();
        settingValues();

    }

    private void settingValues() {
        local.setText(employee.getLocal());
        cnicTv.setText(employee.getCnic());
        lastUpdate.setText(employee.getLast_update_ts());
        gender.setText(employee.getGender());
        birthDate.setText(employee.getBirth_date());
        appointeddate.setText(employee.getAppointment_date());
        fatherName.setText(employee.getFather_name());
        emailEd.setText(employee.getEmail());
        nameTv.setText(employee.getFull_name());
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                onBackPressed();
                return true;
        }



        return super.onOptionsItemSelected(item);

    }
}
