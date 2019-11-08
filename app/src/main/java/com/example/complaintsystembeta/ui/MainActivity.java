package com.example.complaintsystembeta.ui;

import android.content.Intent;
import android.os.Bundle;
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
import com.example.complaintsystembeta.model.PermanentLogin;
import com.example.complaintsystembeta.ui.about.About;
import com.example.complaintsystembeta.ui.complaints.Compliants;
import com.example.complaintsystembeta.ui.feedback.Feedback;
import com.example.complaintsystembeta.ui.login.LoginActivity;
import com.example.complaintsystembeta.ui.profile.Profile;
import com.google.android.material.navigation.NavigationView;

import butterknife.BindView;

public class MainActivity extends BaseActivity
        implements NavigationView.OnNavigationItemSelectedListener{
    private static final String TAG = "MainActivity";
    int count = 1;
    View view;
    private PermanentLoginRepository dao;
    String cnicS;
    String nameS;
    String idS;
    Bundle data;

    @BindView(R.id.imageView) ImageView imageView;
    @BindView(R.id.name) TextView name;
    @BindView(R.id.email) TextView email;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
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


        nameS = data.getString(getString(R.string.permanentlogin_name));
        idS = data.getString(getString(R.string.permanentlogin_id));
        cnicS = data.getString(getString(R.string.permanentlogin_cnic));

        name.setText(nameS);
        email.setText(cnicS);
        getSupportFragmentManager().beginTransaction().replace(R.id.flContent, new Compliants()).commit();




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
            getSupportFragmentManager().beginTransaction().replace(R.id.flContent, new Compliants()).commit();
        } else if (id == R.id.profile) {
            getSupportFragmentManager().beginTransaction().replace(R.id.flContent, new Profile()).commit();
        }   else if (id == R.id.logout) {
            dao.updateUser(new PermanentLogin(cnicS, false, nameS));
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

    @Override
    public void onPointerCaptureChanged(boolean hasCapture) {

    }
}
