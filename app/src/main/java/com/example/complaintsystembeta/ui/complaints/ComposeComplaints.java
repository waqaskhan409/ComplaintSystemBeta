package com.example.complaintsystembeta.ui.complaints;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.app.ActivityCompat;

import android.Manifest;
import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.MediaPlayer;
import android.media.MediaRecorder;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.provider.MediaStore;
import android.speech.RecognizerIntent;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.animation.AlphaAnimation;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import com.example.complaintsystembeta.BaseActivity;
import com.example.complaintsystembeta.R;
import com.example.complaintsystembeta.constants.Constants;
import com.example.complaintsystembeta.interfaace.JsonApiHolder;
import com.example.complaintsystembeta.model.TestClas;
import com.example.complaintsystembeta.ui.MainActivity;
import com.example.complaintsystembeta.ui.login.LoginActivity;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Random;
import java.util.UUID;

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

public class ComposeComplaints extends BaseActivity {
    private static final String TAG = "ComposeComplaints";
    private String checkEmployee, complian;
    private Unbinder unbinder;
    private Boolean boolForAudio = true;
    private ArrayList<Uri> uriArrayList = new ArrayList<>();
    private AlphaAnimation buttonClick = new AlphaAnimation(1F, 0.5F);
    String AudioSavePathInDevice = null;
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
    private MediaPlayer   player = null;
    TextView textView;
    private Bundle data;
    private String account, name;


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


    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_compose_complaints);
        unbinder = ButterKnife.bind(this);
//        setSupportActionBar(toolbar);
        ActivityCompat.requestPermissions(this, permissions, REQUEST_RECORD_AUDIO_PERMISSION);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            isStoragePermissionGranted();
        }
        data = getIntent().getExtras();
        account = data.getString(getString(R.string.account_number));
        name = data.getString(getString(R.string.userName));

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

    @OnClick(R.id.attachement)
    public void attachement(){
        attachement.startAnimation(buttonClick);
        functionForImages();
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
        if(complian.equals("") || complian == null){
            compliantTv.setError(getString(R.string.complain_body_error));
            compliantTv.requestFocus();
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
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(Constants.REST_API)
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
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(Constants.REST_API)
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        RequestBody complainBodyRqst = RequestBody.create(MediaType.parse("text/plain"), complainBody);
        RequestBody complainIdRqst = RequestBody.create(MediaType.parse("text/plain"), complainId);
        RequestBody cnicRqst = RequestBody.create(MediaType.parse("text/plain"), account);
        RequestBody complainStatusRqst = RequestBody.create(MediaType.parse("text/plain"), Constants.COMPLAINS_NEW);
        JsonApiHolder service = retrofit.create(JsonApiHolder.class);
        Call<TestClas> call = service.postComplain(cnicRqst, complainIdRqst, complainStatusRqst, complainBodyRqst);
        call.enqueue(new Callback<TestClas>() {
            @Override
            public void onResponse(Call<TestClas> call, Response<TestClas> response) {
                if(response.isSuccessful()){
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
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(Constants.REST_API)
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        for (int j = 0; j < uriArrayList.size(); j++) {
            String uniqueID = UUID.randomUUID().toString();
            File attachImage = new File(getRealPathFromURI(uriArrayList.get(j)));
            RequestBody requestBodyFront = RequestBody.create(MediaType.parse("image/jpg"), attachImage);
            MultipartBody.Part fileuploadAttach = MultipartBody.Part.createFormData("attachment", attachImage.getName(), requestBodyFront);
            RequestBody filenameAttach = RequestBody.create(MediaType.parse("text/plain"), attachImage.getName());
            RequestBody fileTypeAttach = RequestBody.create(MediaType.parse("text/plain"), "image/jpg");
            RequestBody complainId = RequestBody.create(MediaType.parse("text/plain"), complain_id);
            RequestBody attchmentId = RequestBody.create(MediaType.parse("text/plain"), uniqueID);
            JsonApiHolder service = retrofit.create(JsonApiHolder.class);
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
}
