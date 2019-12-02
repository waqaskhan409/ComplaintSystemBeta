package com.example.complaintsystembeta.ui.login;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.complaintsystembeta.BaseActivity;
import com.example.complaintsystembeta.R;
import com.example.complaintsystembeta.Repository.PermanentLoginRepository;
import com.example.complaintsystembeta.constants.Constants;
import com.example.complaintsystembeta.interfaace.JsonApiHolder;
import com.example.complaintsystembeta.model.Employee;
import com.example.complaintsystembeta.model.PermanentLogin;
import com.example.complaintsystembeta.model.SignUpData;
import com.example.complaintsystembeta.ui.EmployeeNavigation;
import com.example.complaintsystembeta.ui.MainActivity;
import com.example.complaintsystembeta.ui.complaints.AllCatigoryComplains;

import java.util.EnumMap;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;
import io.github.inflationx.viewpump.ViewPumpContextWrapper;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class EmployeeLogin extends BaseActivity {
    private static final String TAG = "EmployeeLogin";
    private Unbinder unbinder;
    private PermanentLoginRepository permanentLoginRepository;
    private List<PermanentLogin> list;
    String cnicS, passwordS;
    boolean checkCon = false;


    @BindView(R.id.username)
    EditText userName;

    @BindView(R.id.password)
    EditText password;

    @BindView(R.id.loginSubmit)
    Button loginSubmit;

    @BindView(R.id.forgetPassword)
    TextView forgetPassword;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_employee_login);
        unbinder = ButterKnife.bind(this);

    }


    @OnClick({R.id.loginSubmit})
    public void submit(){
        Log.d(TAG, "submitData: Clicked submit");
        gettingValues();
        verificationValues();
        Toast.makeText(this, "asdasdasdasda", Toast.LENGTH_SHORT).show();
    }

    @Override
    protected void onStart() {
        super.onStart();
        getDataFromSqlite();
    }

    private void verificationValues() {
        if(cnicS.equals("admin") && passwordS.equals("admin")){
            showProgressDialogue("Loging in", "Please wait...");
            getDataThroughRetrofit2();
        }
        if(cnicS == null || passwordS == null){
            Log.d(TAG, "varificationValues:  User Login Failed");
            showSnackBar("asdasd","");
            return;
        }
        else if(cnicS.equals("")){
            userName.setError(getString(R.string.empty_username));
            userName.requestFocus();
            Log.d(TAG, "varificationValues:  User Login Failed");
            return;
        }
        else if(passwordS.equals("")){
            password.setError(getString(R.string.empty_password));
            password.requestFocus();
            Log.d(TAG, "varificationValues:  User Login Failed");
            return;
        }
//        else if(!checkCNICFormat(cnicS)){
//            this.userName.setError(getString(R.string.error_password));
//            this.userName.requestFocus();
//            Log.d(TAG, "varificationValues:  User Login Failed");
//            return;
//        }
        else if(!checkPasswordType(passwordS)){
            this.password.setError(getString(R.string.error_password));
            this.password.requestFocus();
            Log.d(TAG, "varificationValues:  User Login Failed");
            return;
        }
        else {
            //TODO make Retrofit2 request to local server to post the credentials of users
            //TODO Also, retrieve the local server response
            //Implement login request here
            Log.d(TAG, "varificationValues:  User Login");
//            if(cnicS.equals("admin") && passwordS.equals("admin")){
//                Intent intent = new Intent(this, AllCatigoryComplains.class);
//                startActivity(intent);
//            }
            showProgressDialogue("Loging in", "Please wait...");
            getDataThroughRetrofit2();
//            permanentLoginRepository.updateUser(new PermanentLogin(cnicS, true, ""));
//            checkCon = true;
//            getDataFromSqlite();
        }

    }
    private void getDataThroughRetrofit2() {
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(Constants.REST_API)
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        JsonApiHolder service = retrofit.create(JsonApiHolder.class);

        Call<List<Employee>> call = service.getEmployee();
        call.enqueue(new Callback<List<Employee>>() {
            @Override
            public void onResponse(Call<List<Employee>> call, Response<List<Employee>> response) {
                if(!response.isSuccessful()){
                    Log.d(TAG, "onResponseUnsuccefull: " + response.message());
                    return;
                }
                Log.d(TAG, "onResponse: " + response.message());
                List<Employee>  list = response.body();
                for (Employee l : list){
                    if(l.getCnic().equals(cnicS) && l.getCnic().equals(passwordS)){

                        dissmissProgressDialogue();
                        Log.d(TAG, "onResponse: " + l.getDes_id());
                        Intent intent = new Intent(EmployeeLogin.this, EmployeeNavigation.class);
                        intent.putExtra(Constants.PREVELDGES_ON_FORWARD, l.getDes_id());
                        intent.putExtra(getString(R.string.permanentlogin_name), l.getDes_title());
                        intent.putExtra(getString(R.string.permanentlogin_cnic), l.getCnic());
                        intent.putExtra(getString(R.string.permanentlogin_id), l.getEmployee_id());
                        startActivity(intent);
                        finish();
                    }
                }
            }

            @Override
            public void onFailure(Call<List<Employee>> call, Throwable t) {
                Log.d(TAG, "onFailure: " + t.getMessage());
            }
        });
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        unbinder.unbind();
    }
    private void getDataFromSqlite() {


        permanentLoginRepository = new PermanentLoginRepository(getApplication());
        list = permanentLoginRepository.getIsLoggedIn();
        Log.d(TAG, "getDataFromSqlite: " + list.size());
        for (PermanentLogin permanentLogin : list) {
            Log.d(TAG, "getDataFromSqlite: " + permanentLogin.getCNIC());
            Log.d(TAG, "getDataFromSqlite: " + permanentLogin.getLoggedIn());
            Log.d(TAG, "getDataFromSqlite: " + permanentLogin.getUserName());
            if (permanentLogin.getLoggedIn()) {
                signInWithExtra(permanentLogin);
                finish();
            }
        }
    }
    private void signInWithExtra(PermanentLogin permanentLogin) {
            Intent intent = new Intent(this, MainActivity.class);
            intent.putExtra(getString(R.string.permanentlogin_name), permanentLogin.getUserName());
            intent.putExtra(getString(R.string.permanentlogin_cnic), permanentLogin.getCNIC());
            intent.putExtra(getString(R.string.permanentlogin_id), permanentLogin.getId());
            startActivity(intent);
    }
    private void gettingValues() {
            cnicS = userName.getText().toString();
            passwordS = password.getText().toString();
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
    @Override
    protected void attachBaseContext(Context newBase) {
        super.attachBaseContext(ViewPumpContextWrapper.wrap(newBase));
    }


}
