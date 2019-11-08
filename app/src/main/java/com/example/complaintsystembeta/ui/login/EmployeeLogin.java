package com.example.complaintsystembeta.ui.login;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.complaintsystembeta.BaseActivity;
import com.example.complaintsystembeta.R;
import com.example.complaintsystembeta.Repository.PermanentLoginRepository;
import com.example.complaintsystembeta.model.PermanentLogin;
import com.example.complaintsystembeta.ui.MainActivity;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;

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
        Toast.makeText(this, "asdasdasdasda", Toast.LENGTH_SHORT).show();
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
            //TODO make Retrofit2 request to local server to post the credentials of users
            //TODO Also, retrieve the local server response
            //Implement login request here
            Log.d(TAG, "varificationValues:  User Login");
//            getDataThroughRetrofit2();
            permanentLoginRepository.updateUser(new PermanentLogin(cnicS, true, ""));
            checkCon = true;
            getDataFromSqlite();
            

        }

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

}
