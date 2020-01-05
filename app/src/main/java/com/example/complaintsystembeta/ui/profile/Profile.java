package com.example.complaintsystembeta.ui.profile;


import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.fragment.app.Fragment;

import android.provider.MediaStore;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.complaintsystembeta.R;
import com.example.complaintsystembeta.constants.Constants;
import com.example.complaintsystembeta.constants.RestApi;
import com.example.complaintsystembeta.interfaace.JsonApiHolder;
import com.example.complaintsystembeta.model.AllComplains;
import com.example.complaintsystembeta.model.Consumer;
import com.example.complaintsystembeta.model.Employee;
import com.example.complaintsystembeta.model.PermanentLogin;
import com.example.complaintsystembeta.model.TestClas;
import com.example.complaintsystembeta.ui.MainActivity;
import com.example.complaintsystembeta.ui.login.LoginActivity;
import com.example.complaintsystembeta.ui.registration.Registration;
import com.squareup.picasso.Picasso;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;
import io.github.inflationx.viewpump.ViewPumpContextWrapper;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

import static android.app.Activity.RESULT_OK;


/**
 * A simple {@link Fragment} subclass.
 */
public class Profile extends Fragment {
    private static final String TAG = "Profile";
    private View view;
    private String cnic, userName, email, mobileNumber, address, password, confirmPassword, gender;
    private String name, accountNumber;
    private Unbinder unbinder;
    private Uri imageUriForFront, imageUriForBack, imageUriForWasaBill;

//    TextView nameTv, cnicTv, emailEd, addressEd, phoneEd;

    public Profile(String name, String accountNumber, String CNIC){
        this.name = name;
        this.accountNumber = accountNumber;
        this.cnic = CNIC;
    }


    public Profile() {
        // Required empty public constructor
    }


    @BindView(R.id.deepee)
    ImageView deepee;

    @BindView(R.id.accountNumber)
    TextView account;

    @BindView(R.id.name)
    TextView nameTv;

    @BindView(R.id.topbarVerifiedUser)
    TextView topbarVerification;

    @BindView(R.id.wasaBillImage)
    ImageView wasaBill;
    @BindView(R.id.cnicFront)
    ImageView cnicFront;
    @BindView(R.id.cnicBack)
    ImageView cnicBack;
    @BindView(R.id.editSubmit)
    Button editSubmit;
    @BindView(R.id.email)
    EditText emailEd;
    @BindView(R.id.contactNumber)
    EditText contactNumber;
    @BindView(R.id.contactAddress)
    EditText contactAddress;




    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_profile, container, false);



        unbinder = ButterKnife.bind(this, view);
       /* nameTv = view.findViewById(R.id.name);
        cnicTv = view.findViewById(R.id.accountNumber);
        emailEd = view.findViewById(R.id.email);
        addressEd = view.findViewById(R.id.contactAddress);
        phoneEd = view.findViewById(R.id.contactNumber);
*/
        Log.d(TAG, "onCreateView: " + accountNumber);


        fetchConsumer(accountNumber);

//        settingValues();
//        fetchComplains(employeeId);
        return view;
    }

  /*  private void settingValues() {
        nameTv.setText(name);
        cnicTv.setText(cnic);
        emailEd.setText(email);
        addressEd.setText(address);
        phoneEd.setText(phone);
    }*/

    @Override
    public void onStart() {
        super.onStart();
        if(!((MainActivity)getActivity()).checkWifiOnAndConnected()  && !((MainActivity)getActivity()).checkMobileDataOnAndConnected()){
            ((MainActivity)getActivity()).showSnackBarWifi(getString(R.string.wifi_message));

        }else {
            ((MainActivity)getActivity()).checkConnection();
        }
    }

    @OnClick(R.id.editSubmit)
    public void editSubmit(){
      editAllFields();
    }

    private void editAllFields() {
        if(editSubmit.getText().equals(getString(R.string.edit_and_submit))){
            editSubmit.setText("SUBMIT");
            emailEd.setEnabled(true);
            contactAddress.setEnabled(true);
            contactNumber.setEnabled(true);
        }else {

            gettingValues();
            verificationValues();
        }

    }

    @OnClick(R.id.topbarVerifiedUser)
    public void redirectToProfile(){
        getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.flContent, new ProfileVerification(accountNumber, name)).commit();

    }


    private void fetchComplains(String employeeId) {
        JsonApiHolder service = RestApi.getApi();


        Call<Employee> call = service.getSingleEmployeeForProfile(employeeId);

        call.enqueue(new Callback<Employee>() {
            @Override
            public void onResponse(Call<Employee> call, Response<Employee> response) {
                if(!response.isSuccessful()){
                    Log.d(TAG, "onResponse: Failed!");
                    return;
                }
                Log.d(TAG, "onResponse: "+ response.body().getEmail());
                Log.d(TAG, "onResponse: "+ response.body().getFather_name());
                Log.d(TAG, "onResponse: "+ response.body().getFull_name());
//                email = response.body().getEmail();
//                phone = response.body().get();
//                email = response.body().getEmail();

            }

            @Override
            public void onFailure(Call<Employee> call, Throwable t) {
                Log.d(TAG, "onFailure: " + t.getMessage());
            }
        });

    }

    private void fetchConsumer(String accountNumber) {
        JsonApiHolder service = RestApi.getApi();


        Call<Consumer> call = service.getSingleConsumerForProfile(accountNumber);

        call.enqueue(new Callback<Consumer>() {
            @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
            @Override
            public void onResponse(Call<Consumer> call, Response<Consumer> response) {
                if(!response.isSuccessful()){
                    Log.d(TAG, "onResponse: Failed!");
                    return;
                }
                settinValue(response.body());
                if(response.body().getIs_verified().equals("0")){
                    topbarVerification.setVisibility(View.VISIBLE);
                    Animation anim = new AlphaAnimation(0.0f, 1.0f);
                    anim.setDuration(1000); //You can manage the blinking time with this parameter
                    anim.setStartOffset(20);
                    anim.setRepeatMode(Animation.REVERSE);
                    anim.setRepeatCount(Animation.INFINITE);
                    topbarVerification.startAnimation(anim);
                }else {
                    topbarVerification.setVisibility(View.GONE);
                }

            }

            @Override
            public void onFailure(Call<Consumer> call, Throwable t) {
                Log.d(TAG, "onFailure: " + t.getMessage());
            }
        });

    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    private void settinValue(Consumer body) {
        account.setText(accountNumber);
        nameTv.setText(name);
        emailEd.setText(body.getUser_email());
        contactAddress.setText(body.getUser_address());
        contactNumber.setText(body.getUser_contact());
        try {
            Picasso.get().load(Constants.URL_IMAGES + body.getUser_cnic_front_image()).placeholder(getContext().getDrawable(R.drawable.front_cnic)).into(cnicFront);
            Picasso.get().load(Constants.URL_IMAGES + body.getUser_cnic_back_image()).placeholder(getContext().getDrawable(R.drawable.back_cnic)).into(cnicBack);
            Picasso.get().load(Constants.URL_IMAGES + body.getUser_wasa_bill_image()).placeholder(getContext().getDrawable(R.drawable.wasa_bill)).into(wasaBill);

        }catch (NullPointerException e){
            e.printStackTrace();
        }
    }

    @OnClick(R.id.wasaBillImage)
    public void gettingImageofWasaBill(){
        functionForBothImageFrontAndBack(Constants.RESULT_LOAD_IMG_FOR_WASA_BILL);

    }
    @OnClick(R.id.cnicFront)
    public void gettingImageofFrontCnic(){
        functionForBothImageFrontAndBack(Constants.RESULT_LOAD_IMG_FOR_FRONT);
    }

    @OnClick(R.id.cnicBack)
    public void gettingImageofBackCnic(){
        functionForBothImageFrontAndBack(Constants.RESULT_LOAD_IMG_FOR_BACK);
    }

    public void functionForBothImageFrontAndBack(int f) {
        Intent photoPickerIntent = new Intent(Intent.ACTION_PICK);
        photoPickerIntent.setType("image/*");
        startActivityForResult(photoPickerIntent, f);
    }
    @SuppressLint("ResourceType")
    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            try {
                if(requestCode == Constants.RESULT_LOAD_IMG_FOR_FRONT){
                    imageUriForFront = data.getData();
                    File i = new File(getRealPathFromURI(imageUriForFront));
                    Log.d(TAG, "onActivityResult: " + i);
                    final InputStream imageStream = getActivity().getContentResolver().openInputStream(imageUriForFront);
                    final Bitmap selectedImage = BitmapFactory.decodeStream(imageStream);
                    cnicFront.setImageBitmap(selectedImage);
//                    scanTextFromUri(imageUriForFront);
                }else if(requestCode == Constants.RESULT_LOAD_IMG_FOR_BACK){
                    imageUriForBack = data.getData();
                    final InputStream imageStream = getActivity().getContentResolver().openInputStream(imageUriForBack);
                    final Bitmap selectedImage = BitmapFactory.decodeStream(imageStream);
                    cnicBack.setImageBitmap(selectedImage);
//                    scanTextFromUri(imageUriForBack);
                }else if(requestCode == Constants.RESULT_LOAD_IMG_FOR_WASA_BILL){
                    imageUriForWasaBill = data.getData();
                    final InputStream imageStream = getActivity().getContentResolver().openInputStream(imageUriForWasaBill);
                    final Bitmap selectedImage = BitmapFactory.decodeStream(imageStream);
                    wasaBill.setImageBitmap(selectedImage);
//                    scanTextFromUri(imageUriForWasaBill);
                }
            } catch (FileNotFoundException e) {
                e.printStackTrace();
//                showSnackBar(getString(R.string.error_upload_images), "");
            }

        }else {
//            showSnackBar(getString(R.string.error_upload_images), "");
        }
    }

    @SuppressLint("ResourceType")
    private void verificationValues() {

        if(
                address == null ||
                mobileNumber == null ||
                        email == null
        ){
            Toast.makeText(getActivity(), getString(R.string.error_message_registration), Toast.LENGTH_LONG);
            Log.d(TAG, "varificationValues null:  User Registration failed");
            return;
        }
      if(mobileNumber.equals("") || mobileNumber.isEmpty()){
            contactNumber.setError(getString(R.string.error_mobile_number));
            contactNumber.requestFocus();
            Log.d(TAG, "varificationValues mobile number mobile number:  User Registration failed");
            return;
        }else if(email.equals("") || email.isEmpty()){
          emailEd.setError(getString(R.string.error_address));
          emailEd.requestFocus();
          Log.d(TAG, "varificationValues email:  User Registration failed");
          return;
      }
      else if(address.equals("") || address.isEmpty()){
            contactAddress.setError(getString(R.string.error_address));
            contactAddress.requestFocus();
            Log.d(TAG, "varificationValues address:  User Registration failed");
            return;
        }
        else {
          editSubmit.setText(getString(R.string.edit_and_submit));
          emailEd.setEnabled(false);
          contactAddress.setEnabled(false);
          contactNumber.setEnabled(false);
//            showProgressDialogue(getString(R.string.registration_title), getString(R.string.login_message));
            getDataFromRestApi();
//
        }
    }


    private void getDataFromRestApi() {
        JsonApiHolder service = RestApi.getApi();

        if(imageUriForBack == null ){
//            ("Kindly, attach the bill as a evidence", "");
            Toast.makeText(getActivity(), "Kindly, attach the CNIC front image as a evidence", Toast.LENGTH_SHORT).show();
            return;
        }else if(imageUriForFront == null){
            Toast.makeText(getActivity(), "Kindly, attach the CNIC back image as a evidence", Toast.LENGTH_SHORT).show();
            return;
        }else if(imageUriForWasaBill == null){
            Toast.makeText(getActivity(), "Kindly, attach the bill as a evidence", Toast.LENGTH_SHORT).show();
            return;
        }
        File frontImage = new File(getRealPathFromURI(imageUriForFront));
        File backImage = new File(getRealPathFromURI(imageUriForBack));
        File wasaBill = new File(getRealPathFromURI(imageUriForWasaBill));
        Log.d(TAG, "getDataFromRestApi: " + frontImage);
        Log.d(TAG, "getDataFromRestApi: " + backImage);

        RequestBody requestBodyFront = RequestBody.create(MediaType.parse("image/jpg"), frontImage);
        MultipartBody.Part fileuploadFront = MultipartBody.Part.createFormData("front", frontImage.getName(), requestBodyFront);
        RequestBody filenameFront = RequestBody.create(MediaType.parse("text/plain"), frontImage.getName());

        RequestBody requestBodyBack = RequestBody.create(MediaType.parse("image/jpg"), backImage);
        MultipartBody.Part fileuploadBack = MultipartBody.Part.createFormData("back", backImage.getName(), requestBodyBack);
        RequestBody filenameBack = RequestBody.create(MediaType.parse("text/plain"), backImage.getName());

        RequestBody requestWasaBill = RequestBody.create(MediaType.parse("image/jpg"), wasaBill);
        MultipartBody.Part fileWasaBillUpload = MultipartBody.Part.createFormData("wasa", wasaBill.getName(), requestWasaBill);
        RequestBody filenameWasaBill = RequestBody.create(MediaType.parse("text/plain"), wasaBill.getName());

        RequestBody accountRqst = RequestBody.create(MediaType.parse("text/plain"), accountNumber);
        RequestBody cnicRqst = RequestBody.create(MediaType.parse("text/plain"), "defined");
        RequestBody nameRqst = RequestBody.create(MediaType.parse("text/plain"), "defined");
        RequestBody emailRqst = RequestBody.create(MediaType.parse("text/plain"), email);
        RequestBody passRqst = RequestBody.create(MediaType.parse("text/plain"), "defined");
        RequestBody addressRqst = RequestBody.create(MediaType.parse("text/plain"), address);
        RequestBody mobileRqst = RequestBody.create(MediaType.parse("text/plain"), mobileNumber);
        RequestBody genderRqst = RequestBody.create(MediaType.parse("text/plain"), "defined");

        Call<TestClas> call = service.updateRegisteration(fileuploadFront, fileuploadBack, fileWasaBillUpload, accountRqst,cnicRqst, nameRqst, emailRqst, passRqst, mobileRqst, addressRqst, genderRqst);
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
                    Log.d(TAG, "onResponse: " + response.body());
                    fetchConsumer(accountNumber);
                }else {

                }
            }

            @Override
            public void onFailure(Call<TestClas> call, Throwable t) {
            }
        });
    }
    private String getRealPathFromURI(Uri contentURI) {
        String result;
        Cursor cursor = getActivity().getContentResolver().query(contentURI, null, null, null, null);
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

    private void gettingValues() {
        email = emailEd.getText().toString();
        mobileNumber = contactNumber.getText().toString();
        address = contactAddress.getText().toString();


    }


}
