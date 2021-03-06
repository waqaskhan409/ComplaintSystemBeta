package com.example.complaintsystembeta.ui.complaints;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.widget.Toolbar;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.core.content.FileProvider;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
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
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.view.animation.AlphaAnimation;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import com.example.complaintsystembeta.BaseActivity;
import com.example.complaintsystembeta.BuildConfig;
import com.example.complaintsystembeta.R;
import com.example.complaintsystembeta.constants.Constants;
import com.example.complaintsystembeta.constants.RestApi;
import com.example.complaintsystembeta.interfaace.JsonApiHolder;
import com.example.complaintsystembeta.model.TestClas;
import com.example.complaintsystembeta.ui.MainActivity;
import com.example.complaintsystembeta.ui.dialogues.BottomSheetDialogueCompose;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.firestore.FirebaseFirestore;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
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

import static java.security.AccessController.getContext;

public class ComposeComplaints extends BaseActivity {
    private static final String TAG = "ComposeComplaints";
    private String checkEmployee, complian, lat, lng;
    private Unbinder unbinder;
    private Boolean boolForAudio = true;
    private ArrayList<Uri> uriArrayList = new ArrayList<>();
    private FirebaseFirestore dbConsumer = FirebaseFirestore.getInstance();
    private FirebaseFirestore dbEmployee = FirebaseFirestore.getInstance();
    private AlphaAnimation buttonClick = new AlphaAnimation(1F, 0.5F);
    String AudioSavePathInDevice = null;
    private LocationManager mLocationManager;

    MediaRecorder mediaRecorder ;
    int i = 0;
    Random random ;
    String RandomAudioFileName = "ABCDEFGHIJKLMNOP";
    Handler handler;
    public static final int RequestPermissionCode = 1;
    private static final String LOG_TAG = "AudioRecordTest";
    private static final int REQUEST_RECORD_AUDIO_PERMISSION = 200;
    private static String fileName = null;

    private ImageButton recordButton = null;
    private MediaRecorder recorder = null;

    private ImageButton play = null;
    private MediaPlayer player = null;
    TextView textView;
    private Uri cameraImage = null;
    private Bundle data;
    private String account, name;
    Toolbar toolbar;
    private boolean mLocationPermissionGranted = false;


    // Requesting permission to RECORD_AUDIO
    private boolean permissionToRecordAccepted = false;
    private String [] permissions = {Manifest.permission.RECORD_AUDIO};




//    @BindView(R.id.toolbar)
//    Toolbar toolbar;

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


    @SuppressLint("NewApi")
    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_compose_complaints);
        unbinder = ButterKnife.bind(this);
//        setSupportActionBar(toolbar);
//        ActivityCompat.requestPermissions(this, permissions, REQUEST_RECORD_AUDIO_PERMISSION);
        /*if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            isStoragePermissionGranted();
            isImagePermissionGranted();
        }*/

        data = getIntent().getExtras();
        account = data.getString(getString(R.string.account_number));
        name = data.getString(getString(R.string.userName));
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);


        mLocationManager = (LocationManager) getSystemService(LOCATION_SERVICE);

        if (checkSelfPermission(Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && checkSelfPermission(Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    Activity#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for Activity#requestPermissions for more details.
            return;
        }
        mLocationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, Constants.LOCATION_REFRESH_TIME,
                Constants.LOCATION_REFRESH_DISTANCE, mLocationListener);
        checkMapServices();

    }
    private final LocationListener mLocationListener = new LocationListener() {
        @Override
        public void onLocationChanged(final Location location) {
            //your code here
            lat = String.valueOf(location.getLatitude());
            lng = String.valueOf(location.getLongitude());
            Log.d(TAG, "onLocationChanged: latitude: " + lat);
            Log.d(TAG, "onLocationChanged: longitude: " + lng);
        }

        @Override
        public void onStatusChanged(String provider, int status, Bundle extras) {

        }

        @Override
        public void onProviderEnabled(String provider) {

        }

        @Override
        public void onProviderDisabled(String provider) {

        }
    };
    @Override
    protected void onStart() {
        super.onStart();
        checkConnection();
        if(!checkWifiOnAndConnected()  && !checkMobileDataOnAndConnected()){
            showSnackBarWifi(getString(R.string.wifi_message));
        }else {
            checkConnection();
        }
    }
    private boolean checkMapServices() {
        if (isMapsEnabled()) {
                return true;
        }
        return false;
    }

    private void buildAlertMessageNoGps() {
        final AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage("This application requires GPS to work properly, do you want to enable it?")
                .setCancelable(false)
                .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    public void onClick(@SuppressWarnings("unused") final DialogInterface dialog, @SuppressWarnings("unused") final int id) {
                        Intent enableGpsIntent = new Intent(android.provider.Settings.ACTION_LOCATION_SOURCE_SETTINGS);
                        startActivityForResult(enableGpsIntent, Constants.PERMISSIONS_REQUEST_ENABLE_GPS);
                    }
                });
        final AlertDialog alert = builder.create();
        alert.show();
    }

    public boolean isMapsEnabled() {
        final LocationManager manager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);

        if (!manager.isProviderEnabled(LocationManager.GPS_PROVIDER)) {
            buildAlertMessageNoGps();
            return false;
        }
        return true;
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

    @OnClick(R.id.attachement)
    public void attachement(){
        attachement.startAnimation(buttonClick);
//        functionForImages();
        BottomSheetDialogueCompose bottomSheetDialogueCompose = new BottomSheetDialogueCompose();
        bottomSheetDialogueCompose.show(getSupportFragmentManager(), Constants.TAG_DIALOGUE_BOTTOM_SHEET);

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
            case Constants.MY_PERMISSIONS_REQUEST_CAMERA:
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    Toast.makeText(getApplicationContext(), "Permission Granted", Toast.LENGTH_SHORT).show();

                    // main logic
                } else {
                    Toast.makeText(getApplicationContext(), "Permission Denied", Toast.LENGTH_SHORT).show();}
            case Constants.PERMISSIONS_REQUEST_ACCESS_FINE_LOCATION: {
                // If request is cancelled, the result arrays are empty.
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    mLocationPermissionGranted = true;
                }
            }
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
    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                onBackPressed();
                return true;
        }
        return super.onOptionsItemSelected(item);

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

//    @Override
//    public void onBackPressed() {
//        super.onBackPressed();
//        getSupportFragmentManager().beginTransaction().replace(R.id.flContent, new Compliants(account, "", )).commit();
//
//    }

    private void stopRecording() {
        recorder.stop();
        recorder.release();
        recorder = null;
    }

    private void gettingValues() {
        complian = compliantTv.getEditText().getText().toString();
        String complainId = UUID.randomUUID().toString();
        if(lat == null || lng == null || lat.isEmpty() || lng.isEmpty()){
            showSnackBar(getString(R.string.enable_locations), "");
        }else if(complian.equals("") || complian == null){
            compliantTv.setError(getString(R.string.complain_body_error));
            compliantTv.requestFocus();
        }else if(uriArrayList.size() == 0){
            showSnackBar(getString(R.string.attach_evidence), "");
        }else {
            showProgressDialogue(getString(R.string.complains_title), getString(R.string.login_message));
            submitComplainBody(complainId,complian);
//            submitFileToAttachment(complainId);
        }
    }

    private void submitAudioToAttachment(String complianId) {
        if(fileName == null){
            return;
        }
        JsonApiHolder service = RestApi.getApi();
        String uniqueID = UUID.randomUUID().toString();
        File attachAudio = new File(getRealPathFromURI(Uri.parse(Uri.decode(fileName))));
        RequestBody requestBodyFront = RequestBody.create(MediaType.parse("audio/3gp"), attachAudio);
        MultipartBody.Part fileuploadAttach = MultipartBody.Part.createFormData("attachment", attachAudio.getName(), requestBodyFront);
        RequestBody filenameAttach = RequestBody.create(MediaType.parse("text/plain"), attachAudio.getName());
        RequestBody fileTypeAttach = RequestBody.create(MediaType.parse("text/plain"), "audio/3gp");
        RequestBody complainId = RequestBody.create(MediaType.parse("text/plain"), complianId);
        RequestBody attchmentId = RequestBody.create(MediaType.parse("text/plain"), uniqueID);
        Call<TestClas> call = service.postAttachment(fileuploadAttach, attchmentId, filenameAttach, fileTypeAttach, complainId);
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

    private void submitComplainBody(String complainId, String complainBody) {
        JsonApiHolder service = RestApi.getApi();

        RequestBody complainBodyRqst = RequestBody.create(MediaType.parse("text/plain"), complainBody);
        RequestBody complainIdRqst = RequestBody.create(MediaType.parse("text/plain"), complainId);
        RequestBody cnicRqst = RequestBody.create(MediaType.parse("text/plain"), account);
        RequestBody complainStatusRqst = RequestBody.create(MediaType.parse("text/plain"), Constants.COMPLAINS_NEW);
        RequestBody complainLatRqst = RequestBody.create(MediaType.parse("text/plain"), lat);
        RequestBody complainLngRqst = RequestBody.create(MediaType.parse("text/plain"), lng);
        Call<TestClas> call = service.postComplain(cnicRqst, complainIdRqst, complainStatusRqst, complainLatRqst, complainLngRqst, complainBodyRqst);
        call.enqueue(new Callback<TestClas>() {
            @Override
            public void onResponse(Call<TestClas> call, Response<TestClas> response) {
                if(response.isSuccessful()){
                    Map<String, Object> user = new HashMap<>();
                    user.put("notification_message",complainBody);
//                    user.put("device_token", token[0]);
//                    dbConsumer.collection("users")
//                            .document(Constants.CONSUMER)
//                            .collection("notification")
//                            .document(account)
//                            .update(user)
//                            .addOnCompleteListener(new OnCompleteListener<Void>() {
//                                @Override
//                                public void onComplete(@NonNull Task<Void> task) {
//                                    if(task.isSuccessful()){
//                                        Log.d(TAG, "onComplete: posted");
//                                    }else {
//                                        Log.d(TAG, "onError: Error");
//                                    }
//                                }
//                            });
                    dbEmployee.collection("users")
                            .document(Constants.EMPLOYEES)
                            .collection("notification")
                            .document("50")
                            .update(user)
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
                Log.d(TAG, "onResponse: userName:" + response.body().getSuccess());
                    submitAudioToAttachment(complainId);
                    submitFileToAttachment(complainId);
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

    private void submitFileToAttachment(String complain_id) {
        if(uriArrayList.size() == 0){
            dissmissProgressDialogue();
            redirectToAllComplains();
            return;
        }
        JsonApiHolder service = RestApi.getApi();

        for (int j = 0; j < uriArrayList.size(); j++) {
            String uniqueID = UUID.randomUUID().toString();
            File attachImage = new File(getRealPathFromURI(uriArrayList.get(j)));
            RequestBody requestBodyFront = RequestBody.create(MediaType.parse("image/jpg"), attachImage);
            MultipartBody.Part fileuploadAttach = MultipartBody.Part.createFormData("attachment", attachImage.getName(), requestBodyFront);
            RequestBody filenameAttach = RequestBody.create(MediaType.parse("text/plain"), attachImage.getName());
            RequestBody fileTypeAttach = RequestBody.create(MediaType.parse("text/plain"), "image/jpg");
            RequestBody complainId = RequestBody.create(MediaType.parse("text/plain"), complain_id);
            RequestBody attchmentId = RequestBody.create(MediaType.parse("text/plain"), uniqueID);
            Call<TestClas> call = service.postAttachment(fileuploadAttach, attchmentId, filenameAttach, fileTypeAttach, complainId);
            call.enqueue(new Callback<TestClas>() {
                @Override
                public void onResponse(Call<TestClas> call, Response<TestClas> response) {
                    if(response.isSuccessful()){
                        Log.d(TAG, "onResponse: userName:" + response.body().getSuccess());
                        Toast.makeText(ComposeComplaints.this, response.body().getSuccess(), Toast.LENGTH_SHORT).show();

                    }else {
                        dissmissProgressDialogue();
                        Log.d(TAG, "onResponse: Failed!");
                        Toast.makeText(ComposeComplaints.this, "Failed", Toast.LENGTH_SHORT).show();
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
        Intent intent = new Intent(this, MainActivity.class);
        intent.putExtra(getString(R.string.permanentlogin_name), name);
        intent.putExtra(getString(R.string.account_number), account);
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
        Log.d(TAG, "onActivityResult: " + cameraImage);
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
            case Constants.PERMISSIONS_REQUEST_ENABLE_GPS: {
                if (mLocationPermissionGranted) {
//                    getChatrooms();
                } else {
                    getLocationPermission();
                }
            }
        }

    }

    private void getLocationPermission() {
        /*
         * Request location permission, so that we can get the location of the
         * device. The result of the permission request is handled by a callback,
         * onRequestPermissionsResult.
         */
        if (ContextCompat.checkSelfPermission(this.getApplicationContext(),
                android.Manifest.permission.ACCESS_FINE_LOCATION)
                == PackageManager.PERMISSION_GRANTED) {
            mLocationPermissionGranted = true;
//            getChatrooms();
        } else {
            ActivityCompat.requestPermissions(this,
                    new String[]{android.Manifest.permission.ACCESS_FINE_LOCATION},
                    Constants.PERMISSIONS_REQUEST_ACCESS_FINE_LOCATION);
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
            if(recorder != null) {
                onRecord(false);
            }
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
