package com.example.complaintsystembeta.ui.login;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.complaintsystembeta.BaseActivity;
import com.example.complaintsystembeta.R;
import com.example.complaintsystembeta.Repository.PermanentLoginRepository;
import com.example.complaintsystembeta.interfaace.JsonApiHolder;
import com.example.complaintsystembeta.model.PermanentLogin;
import com.example.complaintsystembeta.model.PostResponse;
import com.example.complaintsystembeta.model.SignUpData;
import com.example.complaintsystembeta.model.TestClas;
import com.example.complaintsystembeta.ui.MainActivity;
import com.example.complaintsystembeta.ui.registration.Registration;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class LoginActivity extends BaseActivity {
    private static final String TAG = "LoginActivity";
    private Unbinder unbinder;
    private PermanentLoginRepository permanentLoginRepository;
    private List<PermanentLogin> list;
    String cnicS, passwordS;
    private PermanentLoginRepository dao;
    boolean checkCon = false;


    @BindView(R.id.username)
    EditText userName;

    @BindView(R.id.password)
    EditText password;

    @BindView(R.id.loginSubmit)
    Button loginSubmit;

    @BindView(R.id.registration)
    TextView registration;

    @BindView(R.id.forgetPassword)
    TextView forgetPassword;

    @BindView(R.id.isEmployee)
    TextView employee;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        unbinder = ButterKnife.bind(this);

        dao = new PermanentLoginRepository(getApplication());

        userName.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if(s.length() == 5){
                    userName.append("-");
                }else if(s.length() == 13){
                    userName.append("-");
                }else if(s.length() > 15){
                    Toast.makeText(LoginActivity.this, "CNIC maximum 15 chracters", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });


    }

    @Override
    protected void onStart() {
        super.onStart();
        getDataFromSqlite();
    }

    @OnClick(R.id.registration)
    public void redirectToRegistrationActivity(){
        Log.d(TAG, "redirectToRegistrationActivity: Redirected");
        Intent intent = new Intent(this, Registration.class);
        startActivity(intent);

    }

    @OnClick({R.id.loginSubmit})
    public void submit(){
        Log.d(TAG, "submitData: Clicked submit");
        showProgressDialogue(getString(R.string.login_title), getString(R.string.login_message));
        gettingValues();
        verificationValues();
    }

    @OnClick(R.id.isEmployee)
    public void redirectToEmployeeLogin(){
            Intent intent = new Intent(this, EmployeeLogin.class);

            startActivity(intent);
    }

    private void verificationValues() {
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
        else if(!checkCNICFormat(cnicS)){
            this.userName.setError(getString(R.string.error_password));
            this.userName.requestFocus();
            Log.d(TAG, "varificationValues:  User Login Failed");
            return;
        }
        else if(!checkPasswordType(passwordS)){
            this.password.setError(getString(R.string.error_password));
            this.password.requestFocus();
            Log.d(TAG, "varificationValues:  User Login Failed");
            return;
        }
        else {
            Log.d(TAG, "varificationValues:  User Login");
            getDataThroughRetrofit2();
//            permanentLoginRepository.updateUser(new PermanentLogin(cnicS, "" ,true, "", false));
//            checkCon = true;
//            getDataFromSqlite();

        }

    }

    private void getDataThroughRetrofit2() {
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("http://192.168.43.31:3000/api/")
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        JsonApiHolder service = retrofit.create(JsonApiHolder.class);

        Call<List<SignUpData>> call = service.getPost();
        call.enqueue(new Callback<List<SignUpData>>() {
            @Override
            public void onResponse(Call<List<SignUpData>> call, Response<List<SignUpData>> response) {
                if(!response.isSuccessful()){
                    Log.d(TAG, "onResponseUnsuccefull: " + response.message());
                    return;
                }
                Log.d(TAG, "onResponse: " + response.message());
                List<SignUpData>  list = response.body();
                for (SignUpData l : list){
                    Log.d(TAG, "onResponse: " + l.getUser_cnic());
                    Log.d(TAG, "onResponse: " + l.getUser_password());
                    if(l.getUser_cnic().equals(cnicS) && l.getUser_password().equals(passwordS)){
                        permanentLoginRepository.updateUser(new PermanentLogin(cnicS,"", true, "", false));
                        dissmissProgressDialogue();
                        if(getDataFromSqlite()){
                            dao.insert(new PermanentLogin(l.getUser_cnic(), l.getAccount_number(), true, l.getUser_name(), false));
                        }
                    }
                }
            }

            @Override
            public void onFailure(Call<List<SignUpData>> call, Throwable t) {
                Log.d(TAG, "onFailure: " + t.getMessage());
            }
        });
    }

    private Boolean getDataFromSqlite() {


        permanentLoginRepository = new PermanentLoginRepository(getApplication());
        list = permanentLoginRepository.getIsLoggedIn();
        Log.d(TAG, "getDataFromSqlite: " + list.size());
        for(PermanentLogin permanentLogin : list){
            Log.d(TAG, "getDataFromSqlite: " + permanentLogin.getCNIC());
            Log.d(TAG, "getDataFromSqlite: " + permanentLogin.getLoggedIn());
            Log.d(TAG, "getDataFromSqlite: " + permanentLogin.getUserName());
            if(permanentLogin.getLoggedIn()){
                signInWithExtra(permanentLogin);
                finish();
            }
        }

        if(checkCon) {
            showSnackBar(getString(R.string.need_registered), "");
            checkCon = false;
        }
        return true;
    }

    private void signInWithExtra(PermanentLogin permanentLogin) {
        Intent intent = new Intent(this, MainActivity.class);
        intent.putExtra(getString(R.string.permanentlogin_name), permanentLogin.getUserName());
        intent.putExtra(getString(R.string.account_number), permanentLogin.getAccountNumber());
        intent.putExtra(getString(R.string.permanentlogin_cnic), permanentLogin.getCNIC());
        intent.putExtra(getString(R.string.permanentlogin_id), permanentLogin.getId());
        startActivity(intent);
    }


    private void gettingValues() {
        cnicS = userName.getText().toString();
        passwordS = password.getText().toString();

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        unbinder.unbind();
    }
}
