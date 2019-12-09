package com.example.complaintsystembeta.ui.complaints;


import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.app.ActivityCompat;
import androidx.core.content.FileProvider;

import android.Manifest;
import android.app.Activity;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.media.MediaPlayer;
import android.media.MediaRecorder;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.provider.MediaStore;
import android.speech.RecognizerIntent;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.view.animation.AlphaAnimation;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.SeekBar;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.example.complaintsystembeta.BaseActivity;
import com.example.complaintsystembeta.R;
import com.example.complaintsystembeta.adapter.AutoCompleteAdapter;
import com.example.complaintsystembeta.constants.Constants;
import com.example.complaintsystembeta.interfaace.JsonApiHolder;
import com.example.complaintsystembeta.model.Department;
import com.example.complaintsystembeta.model.Designation;
import com.example.complaintsystembeta.model.Employee;
import com.example.complaintsystembeta.model.PermanentLogin;
import com.example.complaintsystembeta.model.SignUpData;
import com.example.complaintsystembeta.model.TestClas;
import com.example.complaintsystembeta.ui.EmployeeNavigation;
import com.example.complaintsystembeta.ui.MainActivity;
import com.example.complaintsystembeta.ui.dialogues.BottomSheetDialogueCompose;
import com.example.complaintsystembeta.ui.dialogues.BottomSheetDialogueForward;
import com.example.complaintsystembeta.ui.login.LoginActivity;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Random;
import java.util.UUID;

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

public class ComplainForwarding extends BaseActivity {
    private static final String TAG = "ComposeComplaints";
    private String previouseReportId = "new", complainId, user, complianBody, suggestedDate, forwardTo, forwardToId, forwardFrom, forwardFromId, status, employeeId;
    private Unbinder unbinder;
    private Boolean boolForAudio = true;
    private ArrayList<Uri> uriArrayList = new ArrayList<>();
    private List<Designation>  listForDes = new ArrayList<>();
    private AlphaAnimation buttonClick = new AlphaAnimation(1F, 0.5F);
    private String AudioSavePathInDevice = null;
    private MediaRecorder mediaRecorder ;
    private Bundle data;
    int publicMessage = 0;
    int i = 0;
    private Random random ;
    private String RandomAudioFileName = "ABCDEFGHIJKLMNOP";
    private Handler handler;
    public static final int RequestPermissionCode = 1;
    private static final String LOG_TAG = "AudioRecordTest";
    private static final int REQUEST_RECORD_AUDIO_PERMISSION = 200;
    private static String fileName = null;

    private ImageButton recordButton = null;
    private MediaRecorder recorder = null;

    private ImageButton play = null;
    private MediaPlayer player = null;
    private TextView textView;
    private DatePickerDialog.OnDateSetListener mDateListener;
    private ArrayList<String> arrayDesignations = new ArrayList<>();
    private ArrayList<String> arrayDepartment = new ArrayList<>();
    private List<Employee> designations = new ArrayList<>();
    String[] ara;
    private int replyBoolean = 0;


//    private String account, name;


    // Requesting permission to RECORD_AUDIO
    private boolean permissionToRecordAccepted = false;
    private String [] permissions = {Manifest.permission.RECORD_AUDIO};




//    @BindView(R.id.toolbar)
//    Toolbar toolbar;


    @BindView(R.id.confirmStatusSpinner)
    Spinner confirmStatusSpinner;

    @BindView(R.id.confirmPublicCheckbox)
    CheckBox confirmPublicCheckbox;

    @BindView(R.id.confirmStatusLayout)
    LinearLayout confirmStatusLayout;


    @BindView(R.id.suggestedDateReply)
    EditText suggestDateReplay;

    @BindView(R.id.department)
    AutoCompleteTextView department;

    @BindView(R.id.imagesLayout)
    LinearLayout imagesLayout;

    @BindView(R.id.audioToText)
    ImageView audioToText;


    @BindView(R.id.audio)
    ImageView audio;


    @BindView(R.id.attachement)
    ImageView attachement;

    @BindView(R.id.composeComplains)
    TextInputLayout compliantTv;


    @BindView(R.id.submitCompliant)
    Button submit;

    @BindView(R.id.audioPlay)
    ImageButton audioPlay;

    @BindView(R.id.seekBarAudio)
    SeekBar audioListening;

    @BindView(R.id.seekBarLayout)
    LinearLayout layout;
    private ArrayList<String> arrayListStatus = new ArrayList<>();
    private Uri cameraImage;


    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_complain_forwarding);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        this.data = getIntent().getExtras();
        complainId = data.getString(getString(R.string.complains_id));
        user = data.getString(Constants.PREVELDGES_ON_FORWARD);

        forwardTo = data.getString(Constants.FORWARD_FROM);
        employeeId = data.getString(getString(R.string.permanentlogin_id));
        Log.d(TAG, "onCreate: " + forwardTo );

        //Sending report in stacks LIFO order
        forwardFrom = data.getString(Constants.FORWARD_TO);
        Log.d(TAG, "onCreate: FOrwardfrom " + forwardFrom);
        Log.d(TAG, "onCreate: FOrwardfrom " + forwardTo);
        Log.d(TAG, "onCreate: FOrwardfrom " + employeeId);

        unbinder = ButterKnife.bind(this);
//        setSupportActionBar(toolbar);
        ActivityCompat.requestPermissions(this, permissions, REQUEST_RECORD_AUDIO_PERMISSION);
//        data = getIntent().getExtras();
//        account = data.getString(getString(R.string.account_number));
//        name = data.getString(getString(R.string.userName));
        arrayListStatus.add(Constants.COMPLAIN_IN_PROCESS);
        arrayListStatus.add(Constants.COMPLAINS_RESOLVED);
        ArrayAdapter adapter = new ArrayAdapter(this, android.R.layout.simple_list_item_1, arrayListStatus);
        confirmStatusSpinner.setAdapter(adapter);

        mDateListener = new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                month = month + 1;
                Log.d(TAG, "onDateSet: " + year + "-" + month + "-" + dayOfMonth);
                suggestDateReplay.setText(year + "-" + month + "-" + dayOfMonth);
            }
        };



    }


    public void gallery(){
        functionForImages();

    }
    public void camera() {
        functionForCamera();
    }
    private void functionForCamera() {
        long secs = (new Date().getTime())/1000;
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        File photo = new File(Environment.getExternalStorageDirectory(),  String.valueOf(secs) + "Pic.jpg");
        cameraImage = FileProvider.getUriForFile(this, "com.example.complaintsystembeta.fileprovider", photo);
        intent.putExtra(MediaStore.EXTRA_OUTPUT,
                cameraImage);
        cameraImage = Uri.fromFile(photo);
        startActivityForResult(intent, Constants.CAMERA);

    }


    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                onBackPressed();
                return true;
        }

        if(item.getItemId() == R.id.send){
            gettingValues();
        }

        return super.onOptionsItemSelected(item);


    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.send_forward_message, menu);

        return super.onCreateOptionsMenu(menu);
    }

    @OnClick(R.id.audioToText)
    public void audioToText(){
        audioToText.startAnimation(buttonClick);
        Intent intent = new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH);
        intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL, RecognizerIntent.LANGUAGE_MODEL_FREE_FORM);
        if(intent.resolveActivity(getPackageManager()) != null){
            startActivityForResult(intent, Constants.RESULT_LOAD_FOR_AUDIO_TO_TEXT);
        }else {
            Toast.makeText(this, "Doesn't support", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    protected void onStart() {
        super.onStart();

        previouseReportId = data.getString(Constants.REPORTING_ID);
        Log.d(TAG, "onStart: " + previouseReportId);
        if(!forwardTo.equals("")){
                confirmStatusLayout.setVisibility(View.VISIBLE);
                getSingleEmployee();
                replyBoolean = 1;
                previouseReportId = data.getString(Constants.REPORTING_ID);
                department.setText(forwardTo);
                department.setEnabled(false);

        }
        designations.clear();
        getEmployees();

        Log.d(TAG, "onStart: " + previouseReportId);
    }

    @OnClick(R.id.suggestedDateReply)
    public void datePicker(){
        Calendar calendar = Calendar.getInstance();
        int year = calendar.get(Calendar.YEAR);
        int month = calendar.get(Calendar.MONTH);
        int day = calendar.get(Calendar.DAY_OF_MONTH);

        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.N) {
            DatePickerDialog dialog = new DatePickerDialog(this,
                    android.R.style.Theme_Holo_Light_Dialog_MinWidth,
                    mDateListener,
                    year, month, day
            );

            dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
            dialog.show();

        }

    }


    @OnClick(R.id.audio)
    public void audio(){
        audio.startAnimation(buttonClick);
    }
    public void functionForImages() {
        Intent photoPickerIntent = new Intent(Intent.ACTION_PICK);
        photoPickerIntent.setType("image/*");
        startActivityForResult(photoPickerIntent, Constants.RESULT_LOAD_FOR_IMAGES_FOR_PROVE);
    }

    @OnClick(R.id.submitCompliant)
    public void sendDataToRestApi(){
        gettingValues();
    }

    @OnClick(R.id.attachement)
    public void attachement(){
        attachement.startAnimation(buttonClick);
        BottomSheetDialogueForward bottomSheetDialogueForward = new BottomSheetDialogueForward();
        bottomSheetDialogueForward.show(getSupportFragmentManager(), Constants.TAG_DIALOGUE_BOTTOM_SHEET);

    }
    @OnClick(R.id.audio)
    public void audioAttachment(){
        showDialog(this, "Please record your record...");
    }


    @OnClick(R.id.audioPlay)
    public void audioPlay(){

        if(boolForAudio){
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                audioPlay.setImageDrawable(getDrawable(R.drawable.pause_24dp));
            }
            boolForAudio = false;
            onPlay(true);
            audioListening.setMax(player.getDuration());
//        audioListening
            handler = new Handler();
            handler.postDelayed(timerRunnable, 1000);
        }else {
            boolForAudio = true;
            onPlay(false);
        }

    }
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch (requestCode){
            case REQUEST_RECORD_AUDIO_PERMISSION:
                permissionToRecordAccepted  = grantResults[0] == PackageManager.PERMISSION_GRANTED;
                break;
        }
        if (!permissionToRecordAccepted ) finish();

    }

    private void onRecord(boolean start) {
        if (start) {
            startRecording();
        } else {
            stopRecording();
        }
    }

    private void onPlay(boolean start) {
        if (start) {
            startPlaying();
        } else {
            stopPlaying();
        }
    }

    private void startPlaying() {
        player = new MediaPlayer();
        try {
            player.setDataSource(fileName);
            player.prepare();
            player.start();
        } catch (IOException e) {
            Log.e(LOG_TAG, "prepare() failed");
        }
    }

    private void stopPlaying() {
        player.release();
        player = null;
    }

    private void startRecording() {
        recorder = new MediaRecorder();
        recorder.setAudioSource(MediaRecorder.AudioSource.MIC);
        recorder.setOutputFormat(MediaRecorder.OutputFormat.THREE_GPP);
        recorder.setOutputFile(fileName);
        recorder.setAudioEncoder(MediaRecorder.AudioEncoder.AMR_NB);

        try {
            recorder.prepare();
        } catch (IOException e) {
            Log.e(LOG_TAG, "prepare() failed");
        }

        recorder.start();
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
//        getSupportFragmentManager().beginTransaction().replace(R.id.flContent, new Compliants(account, "")).commit();

    }

    private void stopRecording() {
        recorder.stop();
        recorder.release();
        recorder = null;
    }
    private void gettingValues() {
        complianBody = compliantTv.getEditText().getText().toString();
        forwardTo = department.getText().toString();
        ara = forwardTo.split("/");
        suggestedDate = suggestDateReplay.getText().toString();
        Log.d(TAG, "gettingValues: " + ara[0]);
        Log.d(TAG, "gettingValues: " + ara);
        Log.d(TAG, "gettingValues: " + arrayDepartment);
        if(ara.length > 0){
            forwardToId = ara[0];
            Log.d(TAG, "gettingValues: " + forwardToId);
        }
        if(confirmPublicCheckbox.isChecked()){
            publicMessage = 1;
        }

        status = confirmStatusSpinner.getSelectedItem().toString();
//        forwardFromId = "51";

        String complainId = UUID.randomUUID().toString();
         if(complianBody.equals("") || complianBody == null){
            compliantTv.setError(getString(R.string.complain_body_error));
            compliantTv.requestFocus();
        } else if(forwardTo.equals("") || forwardTo == null){
            department.setError(getString(R.string.complain_body_error));
            department.requestFocus();
        } else if(suggestedDate.equals("") || suggestedDate == null){
            suggestDateReplay.setError(getString(R.string.complain_body_error));
            suggestDateReplay.requestFocus();
        } else if(forwardToId.equals("") || forwardToId == null){
            department.setError(getString(R.string.designation_not_found));
            department.requestFocus();
        } else if(forwardToId.equals(forwardFrom)){
             department.setError("Sender and reciever should not be same ");
             department.requestFocus();
         }else if(status.equals(Constants.NONE)){
             showSnackBar("Please select status","");
         } else {
            Log.d(TAG, "gettingValues: gotIt");
            Log.d(TAG, "gettingValues: " + forwardToId);

            showProgressDialogue(getString(R.string.complains_title), getString(R.string.login_message));
            submitForwardTo();
        }
    }

    private String getForwardToId(String forwardTo) {
        String returnValue = "";
        for (int i = 0; i < designations.size(); i++) {
            if(designations.get(i).getFull_name().equals(forwardTo)){
                returnValue = designations.get(i).getDes_id();
                break;
            }
        }
        return returnValue;
    }
    private void getSingleEmployee() {
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(Constants.REST_API)
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        JsonApiHolder service = retrofit.create(JsonApiHolder.class);

        Call<List<Employee>> call = service.getSingleEmployee(forwardTo);
        call.enqueue(new Callback<List<Employee>>() {
            @Override
            public void onResponse(Call<List<Employee>> call, Response<List<Employee>> response) {
                if(!response.isSuccessful()){
                    Log.d(TAG, "onResponseUnsuccefull: " + response.message());
                    return;
                }else {
//                   department.setText(response.body().get(0).getFull_name()+"/"+response.body().get(0).getDes_title()+"/"+response.body().get(0).getDepartment_name());
//                    department.setEnabled(false);x
                    compliantTv.getEditText().setText("Re:");
                }
            }

            @Override
            public void onFailure(Call<List<Employee>> call, Throwable t) {
                Log.d(TAG, "onFailure: " + t.getMessage());
            }
        });
    }

    private void getEmployees() {
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
                }else {
                    Log.d(TAG, "onResponse AutoTextView: " + response.message());
                    List<Employee> list = response.body();
                    designations = list;
                    if (list.size() != 0) {
                        Log.d(TAG, "onResponse: " + list.size());
                        setAutoCompleteTextOnAllEmployees(list);
                    }
                }
            }

            @Override
            public void onFailure(Call<List<Employee>> call, Throwable t) {
                Log.d(TAG, "onFailure: " + t.getMessage());
            }
        });
    }

    private void setAutoCompleteTextOnAllEmployees(List<Employee> list) {
        Log.d(TAG, "onResponse: " + list.size());

        AutoCompleteAdapter adapter = new AutoCompleteAdapter(this, list);
        department.setAdapter(adapter);
    }


    private void submitAudioToAttachment(String complianId) {
        if(fileName == null){
            return;
        }
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("http://192.168.43.31:3000/api/")
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        String uniqueID = UUID.randomUUID().toString();
        File attachAudio = new File(getRealPathFromURI(Uri.parse(Uri.decode(fileName))));
        RequestBody requestBodyFront = RequestBody.create(MediaType.parse("audio/3gp"), attachAudio);
        MultipartBody.Part fileuploadAttach = MultipartBody.Part.createFormData("attachment", attachAudio.getName(), requestBodyFront);
        RequestBody filenameAttach = RequestBody.create(MediaType.parse("text/plain"), attachAudio.getName());
        RequestBody fileTypeAttach = RequestBody.create(MediaType.parse("text/plain"), "audio/3gp");
        RequestBody complainId = RequestBody.create(MediaType.parse("text/plain"), complianId);
        RequestBody attchmentId = RequestBody.create(MediaType.parse("text/plain"), uniqueID);
        JsonApiHolder service = retrofit.create(JsonApiHolder.class);
        Call<TestClas> call = service.reportingAttachment( fileuploadAttach,attchmentId, filenameAttach, fileTypeAttach, complainId);
        call.enqueue(new Callback<TestClas>() {
            @Override
            public void onResponse(Call<TestClas> call, Response<TestClas> response) {
                if(response.isSuccessful()){
                    Log.d(TAG, "onResponse: userName:" + response.body().getSuccess());
//                    submitFileToAttachment(complainId);
                }else {
                    Log.d(TAG, "onResponse: Failed!");
                }
            }
            @Override
            public void onFailure(Call<TestClas> call, Throwable t) {

            }
        });
    }

    private void submitForwardTo() {

        String complainReportingId = UUID.randomUUID().toString();
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(Constants.REST_API)
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        RequestBody complain_id = RequestBody.create(MediaType.parse("text/plain"), complainId);
        RequestBody complains_reporting_id = RequestBody.create(MediaType.parse("text/plain"), complainReportingId);
        RequestBody forwards_to = RequestBody.create(MediaType.parse("text/plain"), forwardToId);
        RequestBody forwards_by = RequestBody.create(MediaType.parse("text/plain"), forwardFrom);
        RequestBody forwards_message = RequestBody.create(MediaType.parse("text/plain"), complianBody);
        RequestBody suggested_date_reply = RequestBody.create(MediaType.parse("text/plain"), suggestedDate);
        RequestBody employeRqst = RequestBody.create(MediaType.parse("text/plain"), ara[0]);
        RequestBody statusRqst = RequestBody.create(MediaType.parse("text/plain"), status);
        JsonApiHolder service = retrofit.create(JsonApiHolder.class);
        Call call = service.postReportingComplain(previouseReportId,complain_id, complains_reporting_id, forwards_to, forwards_by, forwards_message,suggested_date_reply, employeRqst, replyBoolean, statusRqst, 1, 0, 0 , publicMessage);
        call.enqueue(new Callback<TestClas>() {
            @Override
            public void onResponse(Call<TestClas> call, Response<TestClas> response) {
                if(response.isSuccessful()){
                    Log.d(TAG, "onResponse: userName:" + response.body().getSuccess());
                    submitAudioToAttachment(complainReportingId);
                    submitFileToAttachment(complainReportingId);
                }else {
                    dissmissProgressDialogue();
                    Log.d(TAG, "onResponse: Failed!");
                }

            }

            @Override
            public void onFailure(Call<TestClas> call, Throwable t) {
                dissmissProgressDialogue();
                Log.d(TAG, "onFailure: " + t.getMessage());
            }
        });

    }

    private void submitFileToAttachment(String reportingId) {
        if(uriArrayList.size() == 0){
            dissmissProgressDialogue();
            redirectToAllComplains();
            return;
        }
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("http://192.168.43.31:3000/api/")
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        for (int j = 0; j < uriArrayList.size(); j++) {
            String uniqueID = UUID.randomUUID().toString();
            File attachImage = new File(getRealPathFromURI(uriArrayList.get(j)));
            RequestBody requestBodyFront = RequestBody.create(MediaType.parse("image/jpg"), attachImage);
            MultipartBody.Part fileuploadAttach = MultipartBody.Part.createFormData("attachment", attachImage.getName(), requestBodyFront);
            RequestBody filenameAttach = RequestBody.create(MediaType.parse("text/plain"), attachImage.getName());
            RequestBody fileTypeAttach = RequestBody.create(MediaType.parse("text/plain"), "image/jpg");
            RequestBody reporting_id = RequestBody.create(MediaType.parse("text/plain"), reportingId);
            RequestBody attchmentId = RequestBody.create(MediaType.parse("text/plain"), uniqueID);
            JsonApiHolder service = retrofit.create(JsonApiHolder.class);
            Call<TestClas> call = service.reportingAttachment(fileuploadAttach, attchmentId, filenameAttach, fileTypeAttach, reporting_id);
            call.enqueue(new Callback<TestClas>() {
                @Override
                public void onResponse(Call<TestClas> call, Response<TestClas> response) {
                    if(response.isSuccessful()){
                        Log.d(TAG, "onResponse: userName:" + response.body().getSuccess());
                    }else {
                        dissmissProgressDialogue();
                        Log.d(TAG, "onResponse: Failed!");
                    }

                }

                @Override
                public void onFailure(Call<TestClas> call, Throwable t) {
                    dissmissProgressDialogue();

                }
            });
        }
        dissmissProgressDialogue();
        redirectToAllComplains();

    }

    private void redirectToAllComplains() {
        Intent intent = new Intent(this, EmployeeNavigation.class);
        intent.putExtra(getString(R.string.complains_id), complainId);
        intent.putExtra(getString(R.string.permanentlogin_name), user);
        intent.putExtra(Constants.PREVELDGES_ON_FORWARD, forwardFrom);
        intent.putExtra(getString(R.string.permanentlogin_id), forwardFrom);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(intent);
        finish();
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
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode){
            case Constants.RESULT_LOAD_FOR_AUDIO_TO_TEXT:
                if(resultCode == RESULT_OK && data != null){
                    ArrayList<String> result = data.getStringArrayListExtra(RecognizerIntent.EXTRA_RESULTS);
                    compliantTv.getEditText().setText(compliantTv.getEditText().getText().toString() + " " + result.get(0));
                }
                break;

            case Constants.RESULT_LOAD_FOR_IMAGES_FOR_PROVE:
                if(resultCode == RESULT_OK && data != null){
                    uriArrayList.add(data.getData());
                    setupOnImagesLinearLayout();
                }
                break;

            case Constants.CAMERA:
                Log.d(TAG, "onActivityResult: Executed");
                if(resultCode == RESULT_OK){
                    Log.d(TAG, "onActivityResult: " + cameraImage);
                    uriArrayList.add(cameraImage);
                    setupOnImagesLinearLayout();
                }
                break;
        }

    }

    private void setupOnImagesLinearLayout() {
        imagesLayout.removeAllViews();
        for(int i =0; i < uriArrayList.size(); i++){
            ImageView imageView = new ImageView(this);
            final InputStream imageStream;
            try {
                imageStream = getContentResolver().openInputStream(uriArrayList.get(i));
                final Bitmap selectedImage = BitmapFactory.decodeStream(imageStream);
                imageView.setImageBitmap(selectedImage);
                LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(300, 300);
                imageView.setLayoutParams(layoutParams);
                imageView.setMaxHeight(200);
                imageView.setMaxWidth(200);
                imageView.setScaleType(ImageView.ScaleType.FIT_XY);
                imagesLayout.addView(imageView);

                Log.d(TAG, "setupOnImagesLinearLayout: Loaded images" );
                Log.d(TAG, "setupOnImagesLinearLayout: Loaded images with uri: " + uriArrayList.toString());
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }

        }
    }

    public void showDialog(Activity activity, String msg){
        final Dialog dialog = new Dialog(activity);
        final boolean[] playBool = {true, true};
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setCancelable(false);
        dialog.setContentView(R.layout.dialog_audio);

        ImageButton play, pause, stop, record;
        textView = dialog.findViewById(R.id.minutesForAudio);
        play = dialog.findViewById(R.id.play);
        stop = dialog.findViewById(R.id.stop);
        record = dialog.findViewById(R.id.record);
        fileName = getExternalCacheDir().getAbsolutePath();
        fileName += "/audiorecordtest.3gp";
//        pause = dialog.findViewById(R.id.pause);
        play.setOnClickListener(v -> {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                if(playBool[0]) {
                    playBool[0] = false;
                    textView.setText("Audio starts...");
                    play.setImageDrawable(getDrawable(R.drawable.pause_24dp));
                    onPlay(true);
                }else {
                    playBool[0] = true;
                    textView.setText("Audio stop...");
                    play.setImageDrawable(getDrawable(R.drawable.play));
                    onPlay(false);
                    i = 0;
                }
            }
        });
        record.setOnClickListener(v -> {
            if(playBool[1]){
                playBool[1] = false;
                textView.setText("Recording starts...");
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                    record.setImageDrawable(getDrawable(R.drawable.record_off));
                }
                onRecord(true);
            }else {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                    playBool[1] = true;
                    textView.setText("Recording stop");
                    record.setImageDrawable(getDrawable(R.drawable.ic_mic_black_24dp));
                    onRecord(false);
                    i = 0;
                }

            }
        });

        stop.setOnClickListener(v -> {
            visibleAudio();
            dialog.dismiss();
        });


        dialog.show();

    }

    private void visibleAudio() {
        layout.setVisibility(View.VISIBLE);
    }
    Runnable timerRunnable = new Runnable() {

        public void run() {
            // Get mediaplayer time and set the value

            try {
                audioListening.setProgress(player.getCurrentPosition());
                if (audioListening.getProgress() == player.getDuration()) {
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                        boolForAudio = true;
                        audioPlay.setImageDrawable(getDrawable(R.drawable.play));
                    }

                }
                // This will trigger itself every one second.
                handler.postDelayed(this, 1000);
            }catch (Exception e){
                e.printStackTrace();
            }
        }
    };
    @Override
    protected void onStop() {
        super.onStop();
        if (recorder != null) {
            recorder.release();
            recorder = null;
        }

        if (player != null) {
            player.release();
            player = null;
        }
    }
    @Override
    protected void attachBaseContext(Context newBase) {
        super.attachBaseContext(ViewPumpContextWrapper.wrap(newBase));
    }

}
