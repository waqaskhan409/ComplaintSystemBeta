package com.example.complaintsystembeta.ui.complaints;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.Manifest;
import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.MediaPlayer;
import android.media.MediaRecorder;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
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
import android.widget.TextView;
import android.widget.Toast;

import com.example.complaintsystembeta.R;
import com.example.complaintsystembeta.constants.Constants;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Random;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;

public class ComposeComplaints extends AppCompatActivity {
    private static final String TAG = "ComposeComplaints";
    private String checkEmployee, complian;
    private Unbinder unbinder;
    private ArrayList<Uri> uriArrayList = new ArrayList<>();
    private AlphaAnimation buttonClick = new AlphaAnimation(1F, 0.5F);
    String AudioSavePathInDevice = null;
    MediaRecorder mediaRecorder ;
    Random random ;
    String RandomAudioFileName = "ABCDEFGHIJKLMNOP";
    public static final int RequestPermissionCode = 1;
    private static final String LOG_TAG = "AudioRecordTest";
    private static final int REQUEST_RECORD_AUDIO_PERMISSION = 200;
    private static String fileName = null;

    private ImageButton recordButton = null;
    private MediaRecorder recorder = null;

    private ImageButton play = null;
    private MediaPlayer   player = null;

    // Requesting permission to RECORD_AUDIO
    private boolean permissionToRecordAccepted = false;
    private String [] permissions = {Manifest.permission.RECORD_AUDIO};

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

    private void stopRecording() {
        recorder.stop();
        recorder.release();
        recorder = null;
    }


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

    @BindView(R.id.composeComplain)
    TextView compliantTv;


    @BindView(R.id.submitCompliant)
    Button submit;


    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_compose_complaints);
        unbinder = ButterKnife.bind(this);
//        setSupportActionBar(toolbar);
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

    private void gettingValues() {
        complian = compliantTv.getText().toString();
//        if(employeeCheck.isChecked()){
//            checkEmployee = getString(R.string.employee);
//        }else {
//            checkEmployee = getString(R.string.not_employee);
//        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode){
            case Constants.RESULT_LOAD_FOR_AUDIO_TO_TEXT:
                if(resultCode == RESULT_OK && data != null){
                    ArrayList<String> result = data.getStringArrayListExtra(RecognizerIntent.EXTRA_RESULTS);
                    compliantTv.setText(compliantTv.getText().toString() + " " + result.get(0));
                }

            case Constants.RESULT_LOAD_FOR_IMAGES_FOR_PROVE:
                if(resultCode == RESULT_OK && data != null){
                    uriArrayList.add(data.getData());
                    setupOnImagesLinearLayout();
                }
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
        final boolean[] playBool = {true};
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setCancelable(false);
        dialog.setContentView(R.layout.dialog_audio);

        ImageButton play, pause, stop;
        TextView textView = dialog.findViewById(R.id.minutesForAudio);
        play = dialog.findViewById(R.id.play);
        stop = dialog.findViewById(R.id.stop);
//        pause = dialog.findViewById(R.id.pause);
        play.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                    if(playBool[0]) {
                        playBool[0] = false;
                        textView.setText("00:00");
                        play.setImageDrawable(getDrawable(R.drawable.pause_24dp));
                    }else {
                        playBool[0] = true;
                        textView.setText("Time count");

                        play.setImageDrawable(getDrawable(R.drawable.play));
                    }
                }
            }
        });
        stop.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });


//        dialogButton.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                dialog.dismiss();
//            }
//        });

        dialog.show();

    }


}
