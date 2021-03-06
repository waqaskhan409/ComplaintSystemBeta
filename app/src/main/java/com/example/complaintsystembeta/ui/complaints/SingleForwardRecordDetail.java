package com.example.complaintsystembeta.ui.complaints;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.media.MediaPlayer;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.text.Html;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.widget.Toolbar;
import androidx.viewpager.widget.ViewPager;


import com.example.complaintsystembeta.BaseActivity;
import com.example.complaintsystembeta.R;
import com.example.complaintsystembeta.constants.Constants;
import com.example.complaintsystembeta.constants.RestApi;
import com.example.complaintsystembeta.interfaace.JsonApiHolder;
import com.example.complaintsystembeta.model.AllComplains;
import com.example.complaintsystembeta.model.ReportForward;
import com.example.complaintsystembeta.model.TestClas;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.UUID;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;
import io.github.inflationx.viewpump.ViewPumpContextWrapper;
import okhttp3.MediaType;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

import static com.github.mikephil.charting.charts.Chart.LOG_TAG;

public class SingleForwardRecordDetail extends BaseActivity {
    private static final String TAG = "SingleForwardRecordDeta";
    private Bundle data;
    private Unbinder unbinder;
    private String reportingId, designationTitle, employeeId, forwardFrom, forwardTo, remarksBody, suggestedDate, complainId, user;
    private Toolbar toolbar;
    private String forwardFromDes;
    private String decisionForwardToOrFrom;
    private String isReply, isCurrent;
    private List<String> listReportingImages = new ArrayList<>();
    private List<String> listReportingAudio = new ArrayList<>();
    private List<ReportForward> list;
    private ImagesSwipe mImageSwipe;
    private TextView[] mDots;
    private MediaPlayer player = null;
    private boolean boolForAudio = true;
    private Handler handler;




    @BindView(R.id.seekBarLayout)
    LinearLayout audioLayout;

    @BindView(R.id.audioPlay)
    ImageButton audioPlay;

    @BindView(R.id.acklowledged)
    Button acknowledged;

    @BindView(R.id.reply)
    Button reply;

    @BindView(R.id.forward)
    Button forward;

    @BindView(R.id.seekBarAudio)
    SeekBar seekBar;

    @BindView(R.id.compliansBody)
    TextView remarks;

    @BindView(R.id.noFile)
    TextView noFile;

    @BindView(R.id.forwardTo)
    TextView forwardToTv;

    @BindView(R.id.expectedDate)
    TextView expectedDate;

    @BindView(R.id.forwardFrom)
    TextView forwardFromTv;

    @BindView(R.id.imageViewPager)
    ViewPager viewPager;

    @BindView(R.id.pointing_images)
    LinearLayout linearLayoutImages;

    @BindView(R.id.revoke)
    Button revoke;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_single_forward_record_detail);
        if(unbinder == null){
            unbinder = ButterKnife.bind(this);
        }
        toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        data = getIntent().getExtras();

        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

        viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                addDotToLinearLayout(position);
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });

    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                onBackPressed();
                return true;
        }

        if(item.getItemId() == R.id.reply){
            complainReportReply();
        }else if(item.getItemId() == R.id.forward){
            complainReportForward();
        }
        return super.onOptionsItemSelected(item);

    }

    @Override
    protected void onStart() {
        super.onStart();
        if(!checkWifiOnAndConnected()  && !checkMobileDataOnAndConnected()){
            showSnackBarWifi(getString(R.string.wifi_message));
        }else {
            checkConnection();
        }
        settingValues();

    }

    @OnClick(R.id.audioPlay)
    public void playAudio(){

        if(boolForAudio){
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                audioPlay.setImageDrawable(getDrawable(R.drawable.pause_24dp));
            }
            boolForAudio = false;
            onPlay(true);
            seekBar.setMax(player.getDuration());
//        audioListening
            handler = new Handler();
            handler.postDelayed(timerRunnable, 1000);
        }else {
            boolForAudio = true;
            onPlay(false);
        }
    }
    @OnClick(R.id.acklowledged)
    public void updateAcknowledged(){
        isAcknowledgedUpdate(reportingId);
    }

    @OnClick(R.id.reply)
    public void complainReportReply(){
        if(isCurrent.equals("0")){
            showSnackBar("You are authorized!", "");
        }else {
            Intent intent = new Intent(this, ComplainForwarding.class);
            intent.putExtra(getString(R.string.complains_id), complainId);
            intent.putExtra(Constants.PREVELDGES_ON_FORWARD, user);
            intent.putExtra(Constants.FORWARD_FROM, forwardFromTv.getText().toString());
            intent.putExtra(Constants.FORWARD_TO, forwardTo);
            intent.putExtra(Constants.REPORTING_ID, reportingId);
            intent.putExtra(getString(R.string.permanentlogin_name), employeeId);

            startActivity(intent);
        }
    }

    @OnClick(R.id.forward)
    public void complainReportForward(){
        if(isCurrent.equals("0")){
           showSnackBar("You are authorized!", "");
        }else {
            Intent intent = new Intent(this, ComplainForwarding.class);
            intent.putExtra(getString(R.string.complains_id), complainId);
            intent.putExtra(Constants.PREVELDGES_ON_FORWARD, user);
            intent.putExtra(Constants.FORWARD_FROM, "");
            intent.putExtra(Constants.FORWARD_TO, forwardTo);
            intent.putExtra(Constants.REPORTING_ID, reportingId);
            intent.putExtra(getString(R.string.permanentlogin_name), employeeId);
            startActivity(intent);
        }
    }

    @OnClick(R.id.revoke)
    public void sendRevokeMessage(){
        String[] forwardToArray, forwardByArray;
        String forwardToInner, forwardByInner;
        forwardByArray = forwardFrom.split("/");
        forwardToArray = forwardTo.split("/");
        forwardToInner = forwardByArray[0];
        forwardByInner = forwardToArray[0];
        submitToDelay(forwardToInner, forwardByInner);



    }

    private void submitToDelay(String fToInner, String fByInner) {
        JsonApiHolder service = RestApi.getApi();
        Call call = service.updateIsDelayed(reportingId);
        call.enqueue(new Callback<TestClas>() {
            @Override
            public void onResponse(Call<TestClas> call, Response<TestClas> response) {
                if(response.isSuccessful()){
                    Log.d(TAG, "onResponse: userName:" + response.body().getSuccess());
                    submitToRevoke(fToInner, fByInner);
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

    private void getSingleComplainDetail(String complainId) {
        JsonApiHolder service = RestApi.getApi();
        RequestBody accountRqst = RequestBody.create(MediaType.parse("text/plain"), complainId);

        Call<List<ReportForward>> listCall = service.getSingleComplainDetailForwardingWithAttachment(complainId);

        listCall.enqueue(new Callback<List<ReportForward>>() {
            @Override
            public void onResponse(Call<List<ReportForward>> call, Response<List<ReportForward>> response) {
                if(response.isSuccessful()){
                    boolean bool = false;
                    Log.d(TAG, "onResponse: Successfull");
                    list = response.body();
                    /*if(list.get(0).getEmployee_name() != null){
                    forwardToTv.append(" Employee name : " + list.get(0).getEmployee_name());
                    }*/
                    Log.d(TAG, "onResponse:forward_black "+ list.size());

                    for(ReportForward l : list){
                        if(l.getReporting_attachment_name() != null){
                            if(l.getReporting_attachment_file_type().contains("3gp")){
                                Log.d(TAG, "onResponse:forward_black "+ list.size());
                                listReportingAudio.add(l.getReporting_attachment_name());
                            }else {
                                Log.d(TAG, "onResponse:forward_black " + l.getReporting_attachment_name());
                                listReportingImages.add(l.getReporting_attachment_name());
                            }
                        }
                    }

                    if(listReportingImages.size() != 0) {
                        noFile.setVisibility(View.GONE);
                        viewPager.setVisibility(View.VISIBLE);
                        mImageSwipe = new ImagesSwipe(SingleForwardRecordDetail.this, listReportingImages);
                        viewPager.setAdapter(mImageSwipe);
                        addDotToLinearLayout(0);
                    }
                    if(decisionForwardToOrFrom != null) {
                        if (!decisionForwardToOrFrom.equals("")) {
                            if (listReportingAudio.size() != 0) {

                                audioLayout.setVisibility(View.VISIBLE);

                            }
                        }
                    }


                }else {
                    Log.d(TAG, "onResponse: Failed!");
                }
            }

            @Override
            public void onFailure(Call<List<ReportForward>> call, Throwable t) {
                Log.d(TAG, "onResponse: Failed! :" + t.getMessage());

            }
        });
    }

    private void addDotToLinearLayout(int position) {
        mDots = new TextView[listReportingImages.size()];
        linearLayoutImages.removeAllViews();
        for (int i = 0; i < listReportingImages.size(); i++) {
            mDots[i] = new TextView(this);
            mDots[i].setText(Html.fromHtml("&#8226"));
            mDots[i].setTextSize(20);
            mDots[i].setTextColor(this.getResources().getColor(android.R.color.white));
            linearLayoutImages.addView(mDots[i]);
        }
        if(mDots.length > 0){
            mDots[position].setTextSize(25);
        }
    }

    private void settingValues() {

        reportingId = data.getString(Constants.REPORTING_ID);
        designationTitle = data.getString(Constants.DESIGNATION_TITLE);
        forwardTo = data.getString(Constants.FORWARD_TO);
        remarksBody = data.getString(Constants.REMARKS_BODY);
        suggestedDate = data.getString(Constants.SUGGEST_DATE);
        user = data.getString(Constants.PREVELDGES_ON_FORWARD);
        complainId = data.getString(getString(R.string.complains_id));
        forwardFrom = data.getString(Constants.FORWARD_FROM);
        isReply = data.getString(Constants.IS_REPLY);
        isCurrent = data.getString(Constants.IS_CURRENT);
        forwardFromDes = data.getString(Constants.FORWARD_FROM_NAME_ID_DES);
        employeeId = data.getString(getString(R.string.permanentlogin_name));
        decisionForwardToOrFrom = data.getString(Constants.CHOICE);
        Log.d(TAG, "settingValues: " + employeeId);

        if(decisionForwardToOrFrom != null) {
            if (decisionForwardToOrFrom.equals(Constants.FORWARD_TO)) {
                if (user.equals("admin")) {
                    reply.setVisibility(View.VISIBLE);
                    forward.setVisibility(View.VISIBLE);
                    acknowledged.setVisibility(View.VISIBLE);
//                    audioLayout.setVisibility(View.VISIBLE);

                    isSeenUpdate(reportingId);
                } else {
                    reply.setVisibility(View.GONE);
                    forward.setVisibility(View.GONE);
                    acknowledged.setVisibility(View.GONE);
                    audioLayout.setVisibility(View.GONE);
                }
            } else if (decisionForwardToOrFrom.equals(Constants.FORWARD_FROM)) {
                reply.setVisibility(View.GONE);
                forward.setVisibility(View.GONE);
                acknowledged.setVisibility(View.GONE);
//                audioLayout.setVisibility(View.VISIBLE);
                if (checkDate(suggestedDate)) {
                    revoke.setVisibility(View.VISIBLE);

                } else {
                    revoke.setVisibility(View.GONE);

                }
            }
        }

        remarks.setText(remarksBody);
        forwardToTv.setText(designationTitle);
        expectedDate.setText(suggestedDate);
        forwardFromTv.setText(forwardFromDes);
        getSingleComplainDetail(reportingId);


    }

    private boolean checkDate(String suggestedDate) {
        if(suggestedDate.equals(Constants.NOT_DECIDED)){
            return true;
        }

        String[] date = suggestedDate.split("-");
        int year = Integer.parseInt(date[0]);
        int month = Integer.parseInt(date[1]);
        int day = Integer.parseInt(date[2]);
        int currentMonth =  Calendar.getInstance().get(Calendar.MONTH)+1;
        int currentYear =  Calendar.getInstance().get(Calendar.YEAR);
        int currentDay =  Calendar.getInstance().get(Calendar.DAY_OF_MONTH);
        if(currentYear > year){
            return true;
        }else if(currentMonth > month){
            return true;
        }else if(currentDay > day){
            return true;
        }


        return false;
    }
    private void submitToRevoke(String forwardTo, String forwardBy) {

        String complainReportingId = UUID.randomUUID().toString();
        JsonApiHolder service = RestApi.getApi();
        RequestBody complain_id = RequestBody.create(MediaType.parse("text/plain"), complainId);
        RequestBody complains_reporting_id = RequestBody.create(MediaType.parse("text/plain"), complainReportingId);
        RequestBody forwards_to = RequestBody.create(MediaType.parse("text/plain"),forwardTo);
        RequestBody forwards_by = RequestBody.create(MediaType.parse("text/plain"), forwardBy);
        RequestBody forwards_message = RequestBody.create(MediaType.parse("text/plain"), getString(R.string.revoke_message));
        RequestBody suggested_date_reply = RequestBody.create(MediaType.parse("text/plain"), suggestedDate);
        RequestBody employeRqst = RequestBody.create(MediaType.parse("text/plain"), forwardTo);
        RequestBody statusRqst = RequestBody.create(MediaType.parse("text/plain"), Constants.COMPLAIN_IN_PROCESS);
        Call call = service.postReportingComplain(reportingId, complain_id, complains_reporting_id, forwards_to, forwards_by, forwards_message,suggested_date_reply, employeRqst, 1, statusRqst, 1, 0, 0 , 0);
        call.enqueue(new Callback<TestClas>() {
            @Override
            public void onResponse(Call<TestClas> call, Response<TestClas> response) {
                if(response.isSuccessful()){
                    Log.d(TAG, "onResponse: userName:" + response.body().getSuccess());
                    redirectToBackActivity();
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

    private void redirectToBackActivity() {
        onBackPressed();
    }

    private void isSeenUpdate(String reportingId) {
        JsonApiHolder service = RestApi.getApi();

        Call<TestClas> call = service.updateIsSeen(reportingId);

        call.enqueue(new Callback<TestClas>() {
            @Override
            public void onResponse(Call<TestClas> call, Response<TestClas> response) {
                if(response.isSuccessful()){
                    Log.d(TAG, "onResponse: Succesfull");
//                    Toast.makeText(SingleForwardRecordDetail.this, "You have seen the message", Toast.LENGTH_SHORT).show();
                }else {
                    Log.d(TAG, "onResponse: Failed!");
                }
            }

            @Override
            public void onFailure(Call<TestClas> call, Throwable t) {

            }
        });

    }
    private void isAcknowledgedUpdate(String reportingId) {
        JsonApiHolder service = RestApi.getApi();

        Call<TestClas> call = service.updateIsAcknowledged(reportingId);

        call.enqueue(new Callback<TestClas>() {
            @Override
            public void onResponse(Call<TestClas> call, Response<TestClas> response) {
                if(response.isSuccessful()){
                    Log.d(TAG, "onResponse: Succesfull");
                    Toast.makeText(SingleForwardRecordDetail.this, "Acknowledged", Toast.LENGTH_SHORT).show();
                }else {
                    Log.d(TAG, "onResponse: Failed!");
                }
            }

            @Override
            public void onFailure(Call<TestClas> call, Throwable t) {

            }
        });

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
            player.setDataSource(Constants.URL_IMAGES + listReportingAudio.get(0));
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

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
//        MenuInflater inflater = getMenuInflater();
//        inflater.inflate(R.menu.reply_forward_menu, menu);
        return super.onCreateOptionsMenu(menu);
    }

    Runnable timerRunnable = new Runnable() {

        public void run() {
            // Get mediaplayer time and set the value
            try {
                seekBar.setProgress(player.getCurrentPosition());
                if (seekBar.getProgress() == player.getDuration()) {
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
    protected void attachBaseContext(Context newBase) {
        super.attachBaseContext(ViewPumpContextWrapper.wrap(newBase));
    }
}
