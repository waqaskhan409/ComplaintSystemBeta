package com.example.complaintsystembeta.ui.profile;


import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.ContextThemeWrapper;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import com.example.complaintsystembeta.BaseActivity;
import com.example.complaintsystembeta.R;
import com.example.complaintsystembeta.constants.Constants;
import com.example.complaintsystembeta.constants.RestApi;
import com.example.complaintsystembeta.interfaace.JsonApiHolder;
import com.example.complaintsystembeta.model.AllComplains;
import com.example.complaintsystembeta.model.Consumer;
import com.example.complaintsystembeta.model.TestClas;
import com.example.complaintsystembeta.ui.MainActivity;
import com.example.complaintsystembeta.ui.complaints.AllComplainsFragment;
import com.example.complaintsystembeta.ui.complaints.Compliants;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

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

import static android.app.Activity.RESULT_OK;

/**
 * A simple {@link Fragment} subclass.
 */
public class ProfileVerification extends Fragment {
    private static final String TAG = "AllComplainsFragment";
    private String accountNumber, name;

    private ArrayList<AllComplains> valuesForPending = new ArrayList<>();
    private ArrayList<AllComplains> valuesForNew = new ArrayList<>();
    private ArrayList<AllComplains> valuesForResolved = new ArrayList<>();
    private Uri imageUriForFront, imageUriForBack, imageUriForWasaBill;
    private ProgressDialog dialog;

    private LinearLayout linearLayoutAllComplains, linearLayoutNewComplains, linearLayoutPendingComplains, linearLayoutResolvedComplains;
    private TextView all, pending, newC, resolved, topBarVerificatiedUser;
    private View view;
    private Unbinder unbinder;

    public ProfileVerification() {
        // Required empty public constructor
    }

    public ProfileVerification(String accountNumber, String name) {
        this.accountNumber = accountNumber;
        this.name = name;
    }
    @BindView(R.id.cnicFront)
    ImageView frontImage;


    @BindView(R.id.cnicBack)
    ImageView backImage;

    @BindView(R.id.submit)
    Button submit;





    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment

        final Context contextThemeWrapper = new ContextThemeWrapper(getActivity(), R.style.AppThemeS);

        // clone the inflater using the ContextThemeWrapper
        LayoutInflater  localInflater = inflater.cloneInContext(contextThemeWrapper);
        view = localInflater.inflate(R.layout.fragment_profile_verification, container, false);
        unbinder = ButterKnife.bind(this, view);




        return view;
    }


    private void updateVerification() {
        JsonApiHolder service = RestApi.getApi();

        if(imageUriForBack == null ){
//            ("Kindly, attach the bill as a evidence", "");
            Toast.makeText(getActivity(), "Kindly, attach the CNIC front image as a evidence", Toast.LENGTH_SHORT).show();
            return;
        }else if(imageUriForFront == null){
            Toast.makeText(getActivity(), "Kindly, attach the CNIC back image as a evidence", Toast.LENGTH_SHORT).show();
            return;
        }
        File frontImage = new File(getRealPathFromURI(imageUriForFront));
        File backImage = new File(getRealPathFromURI(imageUriForBack));
        Log.d(TAG, "getDataFromRestApi: " + frontImage);
        Log.d(TAG, "getDataFromRestApi: " + backImage);

        RequestBody requestBodyFront = RequestBody.create(MediaType.parse("image/jpg"), frontImage);
        MultipartBody.Part fileuploadFront = MultipartBody.Part.createFormData("front", frontImage.getName(), requestBodyFront);
        RequestBody filenameFront = RequestBody.create(MediaType.parse("text/plain"), frontImage.getName());

        RequestBody requestBodyBack = RequestBody.create(MediaType.parse("image/jpg"), backImage);
        MultipartBody.Part fileuploadBack = MultipartBody.Part.createFormData("back", backImage.getName(), requestBodyBack);
        RequestBody filenameBack = RequestBody.create(MediaType.parse("text/plain"), backImage.getName());

//        RequestBody requestWasaBill = RequestBody.create(MediaType.parse("image/jpg"), wasaBill);
//        MultipartBody.Part fileWasaBillUpload = MultipartBody.Part.createFormData("wasa", wasaBill.getName(), requestWasaBill);
//        RequestBody filenameWasaBill = RequestBody.create(MediaType.parse("text/plain"), wasaBill.getName());

        RequestBody accountRqst = RequestBody.create(MediaType.parse("text/plain"), accountNumber);
        RequestBody cnicRqst = RequestBody.create(MediaType.parse("text/plain"), "defined");
        RequestBody nameRqst = RequestBody.create(MediaType.parse("text/plain"), "defined");
        RequestBody emailRqst = RequestBody.create(MediaType.parse("text/plain"), "defined");
        RequestBody passRqst = RequestBody.create(MediaType.parse("text/plain"), "defined");
        RequestBody addressRqst = RequestBody.create(MediaType.parse("text/plain"), "defined");
        RequestBody mobileRqst = RequestBody.create(MediaType.parse("text/plain"), "defined");
        RequestBody genderRqst = RequestBody.create(MediaType.parse("text/plain"), "defined");

        Call<TestClas> call = service.verifyRegistration(fileuploadFront, fileuploadBack, accountRqst,cnicRqst, nameRqst, emailRqst, passRqst, mobileRqst, addressRqst, genderRqst);
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
                    dissmissProgressDialogue();
                    getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.flContent, new AllComplainsFragment(accountNumber, name)).commit();

                }else {
                    dissmissProgressDialogue();
                    Toast.makeText(getContext(), "Uploads error", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<TestClas> call, Throwable t) {
                Toast.makeText(getContext(), "Uploads error", Toast.LENGTH_SHORT).show();
            }
        });
    }
    @OnClick(R.id.cnicFront)
    public void gettingImageofFrontCnic(){
        functionForBothImageFrontAndBack(Constants.RESULT_LOAD_IMG_FOR_FRONT);
    }

    @OnClick(R.id.cnicBack)
    public void gettingImageofBackCnic(){
        functionForBothImageFrontAndBack(Constants.RESULT_LOAD_IMG_FOR_BACK);
    }
    @OnClick(R.id.submit)
    public void submitForVerification(){
        showProgressDialogue("Updating Registration", getString(R.string.please_wait));
        updateVerification();
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
                    frontImage.setImageBitmap(selectedImage);
//                    scanTextFromUri(imageUriForFront);
                }else if(requestCode == Constants.RESULT_LOAD_IMG_FOR_BACK){
                    imageUriForBack = data.getData();
                    final InputStream imageStream = getActivity().getContentResolver().openInputStream(imageUriForBack);
                    final Bitmap selectedImage = BitmapFactory.decodeStream(imageStream);
                    backImage.setImageBitmap(selectedImage);
//                    scanTextFromUri(imageUriForBack);
                }else if(requestCode == Constants.RESULT_LOAD_IMG_FOR_WASA_BILL){
                    imageUriForWasaBill = data.getData();
                    final InputStream imageStream = getActivity().getContentResolver().openInputStream(imageUriForWasaBill);
                    final Bitmap selectedImage = BitmapFactory.decodeStream(imageStream);
//                    wasaBill.setImageBitmap(selectedImage);
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

    @Override
    public void onStart() {
        super.onStart();
        if(!((MainActivity)getActivity()).checkWifiOnAndConnected() && !((MainActivity)getActivity()).checkMobileDataOnAndConnected()){
            ((MainActivity)getActivity()).showSnackBarWifi(getString(R.string.wifi_message));


        }else {
            ((MainActivity)getActivity()).checkConnection();
        }
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

    public void showProgressDialogue(String title, String message){
        dialog = new ProgressDialog(getContext());
        dialog.setTitle(title);
        dialog.setMessage(message);
        dialog.show();
    }
    public void dissmissProgressDialogue(){
        dialog.dismiss();
    }

}
