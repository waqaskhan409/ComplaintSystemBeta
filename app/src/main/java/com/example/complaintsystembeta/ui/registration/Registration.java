package com.example.complaintsystembeta.ui.registration;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.core.app.ActivityCompat;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.MenuItem;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.Toast;

import com.example.complaintsystembeta.BaseActivity;
import com.example.complaintsystembeta.R;
import com.example.complaintsystembeta.Repository.PermanentLoginRepository;
import com.example.complaintsystembeta.constants.Constants;
import com.example.complaintsystembeta.constants.RestApi;
import com.example.complaintsystembeta.interfaace.JsonApiHolder;
import com.example.complaintsystembeta.model.PermanentLogin;
import com.example.complaintsystembeta.model.SignUpData;
import com.example.complaintsystembeta.model.TestClas;
import com.example.complaintsystembeta.ui.login.LoginActivity;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.ml.vision.FirebaseVision;
import com.google.firebase.ml.vision.common.FirebaseVisionImage;
import com.google.firebase.ml.vision.text.FirebaseVisionText;
import com.google.firebase.ml.vision.text.FirebaseVisionTextRecognizer;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class Registration extends BaseActivity {
    private static final String TAG = "Registration";


    private Unbinder unbinder;
    private String cnic, userName, email, mobileNumber, address, password, confirmPassword, gender, account;
    private Uri imageUriForFront, imageUriForBack, imageUriForWasaBill;
    private PermanentLoginRepository dao;
    private ArrayList<String> strings;



    @BindView(R.id.cnic)
    TextInputLayout cnicED;

    @BindView(R.id.userName)
    TextInputLayout userNameED;

    @BindView(R.id.userEmail)
    TextInputLayout userEmailED;

    @BindView(R.id.mobileNumber)
    TextInputLayout mobileNumberED;

    @BindView(R.id.address)
    TextInputLayout addressEd;

    @BindView(R.id.password)
    TextInputLayout passwordED;

    @BindView(R.id.confirmPassword)
    TextInputLayout confirmPasswordED;

    @BindView(R.id.account)
    TextInputLayout accountED;

    @BindView(R.id.submit)
    Button submit;

//    @BindView(R.id.cnicFrontImage)
//    ImageView imageForFrontCNIC;
//
//    @BindView(R.id.cnicBackImage)
//    ImageView imageForbackCNIC;

    @BindView(R.id.wasaBillImage)
    ImageView imageForWasaBill;

    @BindView(R.id.spinnerGender)
    Spinner spinnerGender;




    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registration);

        if(unbinder == null){
            unbinder = ButterKnife.bind(this);
        }
        dao = new PermanentLoginRepository(getApplication());
//        isStoragePermissionGranted();
        isGenderListNull();
        testData();

        accountED.getEditText().addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                if(s.length() == 3){
                    accountED.getEditText().append("-");
                }else if(s.length() == 6){
                    accountED.getEditText().append("-");
                }
            }
        });

        cnicED.getEditText().addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                if(s.length() == 5){
                    cnicED.getEditText().append("-");
                }else if(s.length() == 13){
                    cnicED.getEditText().append("-");
                }else if(s.length() > 15){
                    Toast.makeText(Registration.this, "CNIC maximum 15 chracters", Toast.LENGTH_SHORT).show();
                }
            }
        });


    }


    @Override
    protected void onStart() {
        super.onStart();
        checkConnection();

    }

    private void isGenderListNull() {
        if(strings == null){
            strings = new ArrayList<>();
            strings.add("male");
            strings.add("female");
            ArrayAdapter adapter =  new ArrayAdapter(this, android.R.layout.simple_list_item_1, strings);
            spinnerGender.setAdapter(adapter);
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

    @OnClick(R.id.submit)
    public void submitData(){
        gettingValues();
        verificationValues();
    }

    @SuppressLint("ResourceType")
    private void verificationValues() {

        if(cnic == null ||
                address == null ||
                password == null ||
                confirmPassword ==  null ||
                userName == null ||
                mobileNumber == null ||
                account == null
        ){
            showSnackBar(getString(R.string.error_message_registration), "");
            Log.d(TAG, "varificationValues:  User Registration failed");
            return;
        }
        if(cnic.equals("") || cnic.isEmpty()){
            cnicED.setError(getString(R.string.error_cnic));
            cnicED.requestFocus();
            Log.d(TAG, "varificationValues:  User Registration failed");
            return;
        }else if(account.equals("") || account.isEmpty()){
            userNameED.setError(getString(R.string.error_account));
            userNameED.requestFocus();
            Log.d(TAG, "varificationValues:  User Registration failed");
            return;
        }else if(userName.equals("") || userName.isEmpty()){
            userNameED.setError(getString(R.string.error_username));
            userNameED.requestFocus();
            Log.d(TAG, "varificationValues:  User Registration failed");
            return;
        }else if(mobileNumber.equals("") || mobileNumber.isEmpty()){
            mobileNumberED.setError(getString(R.string.error_mobile_number));
            mobileNumberED.requestFocus();
            Log.d(TAG, "varificationValues:  User Registration failed");
            return;
        }else if(address.equals("") || address.isEmpty()){
            addressEd.setError(getString(R.string.error_address));
            addressEd.requestFocus();
            Log.d(TAG, "varificationValues:  User Registration failed");
            return;
        }else if(password.equals("") || password.isEmpty()){
            passwordED.setError(getString(R.string.error_password));
            passwordED.requestFocus();
            Log.d(TAG, "varificationValues:  User Registration failed");
            return;
        }else if(!checkAccountNumber(account)){
            cnicED.setError(getString(R.string.error_account_syntax));
            cnicED.requestFocus();
            Log.d(TAG, "varificationValues:  User Registration failed");
            return;
        } else if(!checkCNICFormat(cnic)){
            cnicED.setError(getString(R.string.error_cnic_check));
            cnicED.requestFocus();
            Log.d(TAG, "varificationValues:  User Registration failed");
            return;
        }else if(!checkPasswordType(password)){
            passwordED.setError(getString(R.string.error_password));
            passwordED.requestFocus();
            Log.d(TAG, "varificationValues:  User Registration failed");
            return;
        }else if(!password.equals(confirmPassword)){
            passwordED.setError(getString(R.string.error_equal_password));
            passwordED.requestFocus();
            Log.d(TAG, "varificationValues:  User Registration failed");
            return;
        }
        else {

            showProgressDialogue(getString(R.string.registration_title), getString(R.string.login_message));
            getDataFromRestApi();
//
        }
    }

    public void scanTextFromUri(Uri uri){
        final String[] scanedText = {""};
        try{
            FirebaseVisionImage image = FirebaseVisionImage.fromFilePath(Registration.this, uri);
            FirebaseVisionTextRecognizer textRecognizer =
                    FirebaseVision.getInstance().getOnDeviceTextRecognizer();
            textRecognizer.processImage(image).addOnSuccessListener(new OnSuccessListener<FirebaseVisionText>() {
                @Override
                public void onSuccess(FirebaseVisionText firebaseVisionText) {
                    Log.d(TAG, "onSuccess: " + firebaseVisionText.getText());
                    scanedText[0] = firebaseVisionText.getText();
                    checkCnicAgainstCard(scanedText[0]);
                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    Log.d(TAG, "onFailure: Failed loading Firebase ML kit");
                    showSnackBar("Cannot load ML model", "");
                }
            });
        }catch (Exception e){
            e.printStackTrace();
        }
    }
    void checkCnicAgainstCard(String scannedAccount){
        String accountNumberString = accountED.getEditText().getText().toString();
        if(scannedAccount.contains(accountNumberString)){
            Toast.makeText(this, "Bill Image correct", Toast.LENGTH_SHORT).show();
            Log.d(TAG, "scanTextFromUri: Correct" + cnic);
        }else {
            showSnackBar("You must have to upload original cnic image with correct format","");
        }
    }

    void testData(){
        JsonApiHolder service = RestApi.getApi();
//        Call<SignUpData> call = service.postData(fileuploadFront, fileuploadBack, fileWasaBillUpload, cnicRqst, nameRqst, emailRqst, passRqst, mobileRqst, addressRqst, genderRqst
//        );
        Call<SignUpData> call = service.testData(new SignUpData("id",
                "54401-6275270-3",
                "Waqas",
                "kwaas@gmail,com",
                "123123123",
                "123123123",
                "123123123",
                "123123123",
                "asdasdasd"));

        call.enqueue(new Callback<SignUpData>() {
            @Override
            public void onResponse(Call<SignUpData> call, Response<SignUpData> response) {
                if(response.isSuccessful()){
                    Toast.makeText(getApplicationContext(), response.body().getUser_cnic(), Toast.LENGTH_LONG).show();
                    Log.d(TAG, "onResponse: cnic:" + response.body().getUser_cnic());
                    Log.d(TAG, "onResponse: email:" + response.body().getUser_email());
                    Log.d(TAG, "onResponse: userName:" + response.body().getUser_name());
                    Log.d(TAG, "onResponse: userName:" + response.body().getUser_name());
//                    Log.d(TAG, "onResponse: userName:" + response.body().getSign_up_id());

                }
            }

            @Override
            public void onFailure(Call<SignUpData> call, Throwable t) {
                Toast.makeText(getApplicationContext(), t.getMessage(), Toast.LENGTH_LONG).show();
            }
        });
    }


    private void getDataFromRestApi() {
        JsonApiHolder service = RestApi.getApi();

//        File frontImage = new File(getRealPathFromURI(imageUriForFront));
//        File backImage = new File(getRealPathFromURI(imageUriForBack));
        File wasaBill = new File(getRealPathFromURI(imageUriForWasaBill));
//            Log.d(TAG, "getDataFromRestApi: " + frontImage);
//            Log.d(TAG, "getDataFromRestApi: " + backImage);
        if(/*frontImage == null || backImage == null ||*/ wasaBill == null){
            showSnackBar("Kindly, attach the bill as a evidence", "");
            return;
        }
       /* RequestBody requestBodyFront = RequestBody.create(MediaType.parse("image/jpg"), frontImage);
        MultipartBody.Part fileuploadFront = MultipartBody.Part.createFormData("front", frontImage.getName(), requestBodyFront);
        RequestBody filenameFront = RequestBody.create(MediaType.parse("text/plain"), frontImage.getName());

        RequestBody requestBodyBack = RequestBody.create(MediaType.parse("image/jpg"), backImage);
        MultipartBody.Part fileuploadBack = MultipartBody.Part.createFormData("back", backImage.getName(), requestBodyBack);
        RequestBody filenameBack = RequestBody.create(MediaType.parse("text/plain"), backImage.getName());
*/
        RequestBody requestWasaBill = RequestBody.create(MediaType.parse("image/jpg"), wasaBill);
        MultipartBody.Part fileWasaBillUpload = MultipartBody.Part.createFormData("wasa", wasaBill.getName(), requestWasaBill);
        RequestBody filenameWasaBill = RequestBody.create(MediaType.parse("text/plain"), wasaBill.getName());

        RequestBody accountRqst = RequestBody.create(MediaType.parse("text/plain"), account);
        RequestBody cnicRqst = RequestBody.create(MediaType.parse("text/plain"), cnic);
        RequestBody nameRqst = RequestBody.create(MediaType.parse("text/plain"), userName);
        RequestBody emailRqst = RequestBody.create(MediaType.parse("text/plain"), email);
        RequestBody passRqst = RequestBody.create(MediaType.parse("text/plain"), password);
        RequestBody addressRqst = RequestBody.create(MediaType.parse("text/plain"), address);
        RequestBody mobileRqst = RequestBody.create(MediaType.parse("text/plain"), mobileNumber);
        RequestBody genderRqst = RequestBody.create(MediaType.parse("text/plain"), gender);

        Call<TestClas> call = service.postData(fileWasaBillUpload, accountRqst,cnicRqst, nameRqst, emailRqst, passRqst, mobileRqst, addressRqst, genderRqst);
//        Call<SignUpData> call = service.testData(new SignUpData("id",
//                "54401-6275270-3",
//                "Waqas",
//                "kwaas@gmail,com",
//                "123123123",
//                "123123123",
//                "123123123",
//                "123123123",
//                "asdasdasd"));

        call.enqueue(new Callback<TestClas>() {
            @Override
            public void onResponse(Call<TestClas> call, Response<TestClas> response) {
                if(response.isSuccessful()){
                    dissmissProgressDialogue();
                    Toast.makeText(getApplicationContext(), response.body().getSuccess(), Toast.LENGTH_LONG).show();
                    Log.d(TAG, "onResponse: userName:" + response.body().getSuccess());
                    if(response.body().getSuccess().equals("new user registered")){
                    dao.insert(new PermanentLogin(cnic, account, true, userName, false, "null"));
                    Intent intent = new Intent(Registration.this, LoginActivity.class);
                    intent.putExtra(getString(R.string.permanentlogin_name), userName);
                    intent.putExtra(getString(R.string.account_number), account);
                    intent.putExtra(getString(R.string.permanentlogin_cnic), cnic);
                    startActivity(intent);
                    }

                }else {
                    dissmissProgressDialogue();
                    showSnackBar(response.body().getError(), "");

                }
            }

            @Override
            public void onFailure(Call<TestClas> call, Throwable t) {
                dissmissProgressDialogue();
                showSnackBar(t.getMessage(), "");
            }
        });
    }

    private void gettingValues() {
        cnic = cnicED.getEditText().getText().toString();
        userName = userNameED.getEditText().getText().toString();
        email = userEmailED.getEditText().getText().toString();
        mobileNumber = mobileNumberED.getEditText().getText().toString();
        address = addressEd.getEditText().getText().toString();
        password = passwordED.getEditText().getText().toString();
        confirmPassword = confirmPasswordED.getEditText().getText().toString();
        gender = spinnerGender.getSelectedItem().toString();
        account =accountED.getEditText().getText().toString();


    }


    @OnClick(R.id.wasaBillImage)
    public void gettingImageofWasaBill(){
        functionForBothImageFrontAndBack(Constants.RESULT_LOAD_IMG_FOR_WASA_BILL);

    }
//    @OnClick(R.id.cnicFrontImage)
    public void gettingImageofFrontCnic(){
        functionForBothImageFrontAndBack(Constants.RESULT_LOAD_IMG_FOR_FRONT);
    }

//    @OnClick(R.id.cnicBackImage)
    public void gettingImageofBackCnic(){
        functionForBothImageFrontAndBack(Constants.RESULT_LOAD_IMG_FOR_BACK);
    }

    public void functionForBothImageFrontAndBack(int f) {
        Intent photoPickerIntent = new Intent(Intent.ACTION_PICK);
        photoPickerIntent.setType("image/*");
        startActivityForResult(photoPickerIntent, f);
    }

    private String getRealPathFromURI(Uri contentURI) {
        String result;
        Cursor cursor = getContentResolver().query(contentURI, null, null, null, null);
        if (cursor == null) { // Source is Dropbox or other similar local file path
            result = contentURI.getPath();
        } else {
            cursor.moveToFirst();
            int idx = cursor.getColumnIndex(MediaStore.Images.ImageColumns.DATA);
            result = cursor.getString(idx);
            cursor.close();
        }
        return result;
    }

    @SuppressLint("ResourceType")
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            try {
                if(requestCode == Constants.RESULT_LOAD_IMG_FOR_FRONT){
                    imageUriForFront = data.getData();
                    File i = new File(getRealPathFromURI(imageUriForFront));
                    Log.d(TAG, "onActivityResult: " + i);
                    final InputStream imageStream = getContentResolver().openInputStream(imageUriForFront);
                    final Bitmap selectedImage = BitmapFactory.decodeStream(imageStream);
//                    imageForFrontCNIC.setImageBitmap(selectedImage);
                    scanTextFromUri(imageUriForFront);
                }else if(requestCode == Constants.RESULT_LOAD_IMG_FOR_BACK){
                    imageUriForBack = data.getData();
                    final InputStream imageStream = getContentResolver().openInputStream(imageUriForBack);
                    final Bitmap selectedImage = BitmapFactory.decodeStream(imageStream);
//                    imageForbackCNIC.setImageBitmap(selectedImage);
//                    scanTextFromUri(imageUriForBack);
                }else if(requestCode == Constants.RESULT_LOAD_IMG_FOR_WASA_BILL){
                    imageUriForWasaBill = data.getData();
                    final InputStream imageStream = getContentResolver().openInputStream(imageUriForWasaBill);
                    final Bitmap selectedImage = BitmapFactory.decodeStream(imageStream);
                    imageForWasaBill.setImageBitmap(selectedImage);
                    scanTextFromUri(imageUriForWasaBill);
                }
            } catch (FileNotFoundException e) {
                e.printStackTrace();
                showSnackBar(getString(R.string.error_upload_images), "");
            }

        }else {
            showSnackBar(getString(R.string.error_upload_images), "");
        }
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
