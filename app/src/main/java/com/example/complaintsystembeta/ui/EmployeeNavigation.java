package com.example.complaintsystembeta.ui;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;

import com.example.complaintsystembeta.BaseActivity;
import com.example.complaintsystembeta.R;
import com.example.complaintsystembeta.Repository.PermanentLoginRepository;
import com.example.complaintsystembeta.constants.Constants;
import com.example.complaintsystembeta.model.PermanentLogin;
import com.example.complaintsystembeta.ui.about.About;
import com.example.complaintsystembeta.ui.complaints.AllCatigoryComplainsFragment;
import com.example.complaintsystembeta.ui.complaints.AllComplainsFragment;
import com.example.complaintsystembeta.ui.login.LoginActivity;
import com.example.complaintsystembeta.ui.profile.Profile;
import com.google.android.material.navigation.NavigationView;

import java.util.List;

import butterknife.BindView;
import io.github.inflationx.viewpump.ViewPumpContextWrapper;

public class EmployeeNavigation extends BaseActivity
        implements NavigationView.OnNavigationItemSelectedListener{
    private static final String TAG = "MainActivity";
    int count = 1;
    View view;
    private PermanentLoginRepository dao;
    String userName;
    String desId;
    String employeeId;
    String accountNumber;
    Bundle data;
    private PermanentLoginRepository permanentLoginRepository;

    @BindView(R.id.imageView) ImageView imageView;
    @BindView(R.id.name) TextView name;
    @BindView(R.id.email) TextView email;
    private String cnic;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_employee_navigation);


        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        dao = new PermanentLoginRepository(getApplication());
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView;
        navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
        view = navigationView.getHeaderView(0);
        ImageView image = view.findViewById(R.id.imageView);
        TextView name = view.findViewById(R.id.name);
        TextView email = view.findViewById(R.id.email);
        data = getIntent().getExtras();


        data = getIntent().getExtras();
        cnic = data.getString(getString(R.string.permanentlogin_cnic));
        userName = data.getString(getString(R.string.permanentlogin_name));
        desId = data.getString(Constants.PREVELDGES_ON_FORWARD);
        employeeId = data.getString(getString(R.string.permanentlogin_id));

        name.setText(userName);
//        email.setText(accountNumber);

        getSupportFragmentManager().beginTransaction().replace(R.id.flContent, new AllCatigoryComplainsFragment("",employeeId,userName,employeeId)).commit();




    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        }
//        else {
//            dialogShowForExit();
//
//        }

    }

    private void dialogShowForExit() {

    }

    /*@Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }*/

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();
        if (id == R.id.complaints) {
//            getSupportFragmentManager().beginTransaction().replace(R.id.flContent, new AllComplainsFragment(accountNumber, nameS)).commit();
            getSupportFragmentManager().beginTransaction().replace(R.id.flContent, new AllCatigoryComplainsFragment("",employeeId,userName,employeeId)).commit();
        } else if (id == R.id.profile) {
            getSupportFragmentManager().beginTransaction().replace(R.id.flContent, new Profile(userName,cnic,employeeId)).commit();
        }   else if (id == R.id.logout) {
//            dao.updateUser(new PermanentLogin(cnicS, "",false, nameS, false));
//              getDataFromSqlite();
              Intent intent = new Intent(this, LoginActivity.class);
              intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
              intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
              startActivity(intent);
        } else if (id == R.id.about) {
            getSupportFragmentManager().beginTransaction().replace(R.id.flContent, new About()).commit();
        }
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }
    private void getDataFromSqlite() {


        permanentLoginRepository = new PermanentLoginRepository(getApplication());
        List<PermanentLogin> list = permanentLoginRepository.getIsLoggedIn();
        Log.d(TAG, "getDataFromSqlite: " + list.size());
        for(PermanentLogin permanentLogin : list){
            Log.d(TAG, "getDataFromSqlite: " + permanentLogin.getCNIC());
            Log.d(TAG, "getDataFromSqlite: " + permanentLogin.getLoggedIn());
            Log.d(TAG, "getDataFromSqlite: " + permanentLogin.getUserName());

        }

//        if(checkCon) {
//            showSnackBar(getString(R.string.need_registered), "");
//            checkCon = false;
//        }

    }
    @Override
    public void onPointerCaptureChanged(boolean hasCapture) {

    }
    @Override
    protected void attachBaseContext(Context newBase) {
        super.attachBaseContext(ViewPumpContextWrapper.wrap(newBase));
    }

}
