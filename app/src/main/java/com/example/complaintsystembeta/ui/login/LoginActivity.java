package com.example.complaintsystembeta.ui.login;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.example.complaintsystembeta.BaseActivity;
import com.example.complaintsystembeta.R;
import com.example.complaintsystembeta.Repository.PermanentLoginRepository;
import com.example.complaintsystembeta.constants.Constants;
import com.example.complaintsystembeta.constants.RestApi;
import com.example.complaintsystembeta.interfaace.JsonApiHolder;
import com.example.complaintsystembeta.model.PermanentLogin;
import com.example.complaintsystembeta.model.SignUpData;
import com.example.complaintsystembeta.model.TestClas;
import com.example.complaintsystembeta.ui.EmployeeNavigation;
import com.example.complaintsystembeta.ui.MainActivity;
import com.example.complaintsystembeta.ui.dialogues.BottomSheetDialogueForgetPassword;
import com.example.complaintsystembeta.ui.registration.Registration;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.iid.InstanceIdResult;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

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

public class LoginActivity extends BaseActivity {
    private static final String TAG = "LoginActivity";
    private Unbinder unbinder;
    private PermanentLoginRepository permanentLoginRepository;
    private List<PermanentLogin> list;
    private FirebaseFirestore db = FirebaseFirestore.getInstance();
    String cnicS, passwordS;
    private PermanentLoginRepository dao;
    boolean checkCon = false;
    // Requesting permission to RECORD_AUDIO
    private boolean permissionToRecordAccepted = false;
    private String [] permissions = {Manifest.permission.RECORD_AUDIO, Manifest.permission.CAMERA,
            Manifest.permission.ACCESS_FINE_LOCATION,
            Manifest.permission.ACCESS_COARSE_LOCATION
            ,Manifest.permission.WRITE_EXTERNAL_STORAGE};

    public static final int RequestPermissionCode = 1;
    private static final String LOG_TAG = "AudioRecordTest";
    private static final int REQUEST_RECORD_AUDIO_PERMISSION = 200;

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

    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        if(unbinder == null) {
            unbinder = ButterKnife.bind(this);
        }
        dao = new PermanentLoginRepository(getApplication());

        ActivityCompat.requestPermissions(this, permissions, REQUEST_RECORD_AUDIO_PERMISSION);
//        isStoragePermissionGranted();
//        isImagePermissionGranted();

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
    private void isImagePermissionGranted() {
        if (ContextCompat.checkSelfPermission(this,
                Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {

            if (ActivityCompat.shouldShowRequestPermissionRationale(this, Manifest.permission.CAMERA)) {


            } else {
                ActivityCompat.requestPermissions(this,
                        new String[]{Manifest.permission.CAMERA},
                        Constants.MY_PERMISSIONS_REQUEST_CAMERA);
            }

        }
    }

    @RequiresApi(api = Build.VERSION_CODES.M)
    public boolean isStoragePermissionGranted () {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (checkSelfPermission(android.Manifest.permission.WRITE_EXTERNAL_STORAGE)
                    == PackageManager.PERMISSION_GRANTED) {
                Log.v(TAG, "Permission is granted");
                return true;
            } else {

                Log.v(TAG, "Permission is revoked");
                ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, 1);
                return false;
            }
        } else { //permission is automatically granted on sdk<23 upon installation
            Log.v(TAG, "Permission is granted");
            return true;
        }
    }

    @Override
    protected void onStart() {
        super.onStart();
        if(!checkWifiOnAndConnected()  && !checkMobileDataOnAndConnected()){
            showSnackBarWifi(getString(R.string.wifi_message));

        }else {
            checkConnection();
        }
        getDataFromSqlite();

    }


    @OnClick(R.id.forgetPassword)
    public void forgetPassword(){
        BottomSheetDialogueForgetPassword forgetPassword = new BottomSheetDialogueForgetPassword();
        forgetPassword.show(getSupportFragmentManager(), Constants.TAG_DIALOGUE_BOTTOM_SHEET);


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
            showProgressDialogue(getString(R.string.login_title), getString(R.string.login_message));
            Log.d(TAG, "varificationValues:  User Login");
            getDataThroughRetrofit2();
//            permanentLoginRepository.updateUser(new PermanentLogin(cnicS, "" ,true, "", false));
//            checkCon = true;
//            getDataFromSqlite();

        }

    }
    public void getDataFromServer(String email) {
        showProgressDialogue("Email sending", "Please wait...");
        JsonApiHolder service = RestApi.getApi();
        Call<TestClas> call = service.forget(email);

        call.enqueue(new Callback<TestClas>() {
            @SuppressLint("LongLogTag")
            @Override
            public void onResponse(Call<TestClas> call, Response<TestClas> response) {
                if(response.isSuccessful()){
                    Toast.makeText(LoginActivity.this, "Mail send", Toast.LENGTH_SHORT).show();
                    dissmissProgressDialogue();
                }else {
                    Log.d(TAG, "onResponse: Failed!");
                    showSnackBar("onFailure: SerFailed!" , "");
                    dissmissProgressDialogue();
                }

            }

            @SuppressLint("LongLogTag")
            @Override
            public void onFailure(Call<TestClas> call, Throwable t) {
                Log.d(TAG, "onFailure: Failed with message"+ t.getMessage());
                showSnackBar("onFailure: Failed with message"+ t.getMessage() , "");
                dissmissProgressDialogue();
            }
        });
    }
    private void getDataThroughRetrofit2() {
        JsonApiHolder service = RestApi.getApi();


        Call<List<SignUpData>> call = service.getPost();
        call.enqueue(new Callback<List<SignUpData>>() {
            @Override
            public void onResponse(Call<List<SignUpData>> call, Response<List<SignUpData>> response) {
                if(!response.isSuccessful()){
                    dissmissProgressDialogue();
                    Log.d(TAG, "onResponseUnsuccefull: " + response.message());
                    showSnackBar("your request to the server is failed", "");
                    return;
                }
                final String[] token = new String[1];

                Log.d(TAG, "onResponse: " + response.message());
                List<SignUpData>  list = response.body();



                for (SignUpData l : list){
                    Log.d(TAG, "onResponse: " + l.getUser_cnic());
                    Log.d(TAG, "onResponse: " + l.getUser_password());
                    if(l.getUser_cnic().equals(cnicS) && l.getUser_password().equals(passwordS)){
                        FirebaseInstanceId.getInstance().getInstanceId().addOnSuccessListener(new OnSuccessListener<InstanceIdResult>() {
                            @Override
                            public void onSuccess(InstanceIdResult instanceIdResult) {
                                token[0] = instanceIdResult.getToken();
                                Map<String, Object> user = new HashMap<>();
                                user.put("consumer", l.getAccount_number());
                                user.put("device_token", token[0]);
                                db.collection("users")
                                        .document(Constants.CONSUMER)
                                        .collection("notification")
                                        .document(l.getAccount_number())
                                        .set(user)
                                        .addOnCompleteListener(new OnCompleteListener<Void>() {
                                            @Override
                                            public void onComplete(@NonNull Task<Void> task) {
                                                if(task.isSuccessful()){
                                                    Log.d(TAG, "onComplete: posted");
                                                }else {
                                                    Log.d(TAG, "onError: Error");

                                                }
                                            }
                                        });
                            }
                        });



                        permanentLoginRepository.updateUser(new PermanentLogin(cnicS,"", true, "", false, "null"));
                        dissmissProgressDialogue();
                        if(getDataFromSqlite()){
                            dao.insert(new PermanentLogin(l.getUser_cnic(), l.getAccount_number(), true, l.getUser_name(), false, "null"));
                            getDataFromSqlite();
                        }
                        break;
                    }
                }
                dissmissProgressDialogue();
                showSnackBar("You need to register your self", "");

            }

            @Override
            public void onFailure(Call<List<SignUpData>> call, Throwable t) {
                dissmissProgressDialogue();
                Log.d(TAG, "onFailure: " + t.getMessage());
                showSnackBar("Connection Failed: "+ t.getMessage() , "");
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
                if(permanentLogin.getEmployee()){
                    signInWithExtraWithEmployee(permanentLogin);
                    finish();
                }else {
                    signInWithExtra(permanentLogin);
                    finish();
                }
            }
        }

        if(checkCon) {
            showSnackBar(getString(R.string.need_registered), "");
            checkCon = false;
        }
        return true;
    }

    private void signInWithExtraWithEmployee(PermanentLogin permanentLogin) {
        Intent intent = new Intent(LoginActivity.this, EmployeeNavigation.class);
        intent.putExtra(Constants.PREVELDGES_ON_FORWARD, permanentLogin.getAccountNumber());
        intent.putExtra(getString(R.string.permanentlogin_name), permanentLogin.getUserName());
        intent.putExtra(getString(R.string.permanentlogin_cnic), permanentLogin.getCNIC());
        intent.putExtra(getString(R.string.permanentlogin_id), permanentLogin.getEmployeeId());
        startActivity(intent);
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


    /*The below function is for Same font to the whole Activity*/
    @Override
    protected void attachBaseContext(Context newBase) {
        super.attachBaseContext(ViewPumpContextWrapper.wrap(newBase));
    }
}
