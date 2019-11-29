package com.example.complaintsystembeta.ui.complaints;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.media.MediaPlayer;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.text.Html;
import android.util.Log;
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
import com.example.complaintsystembeta.interfaace.JsonApiHolder;
import com.example.complaintsystembeta.model.AllComplains;
import com.example.complaintsystembeta.model.ReportForward;
import com.example.complaintsystembeta.model.TestClas;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;
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
    private String isReply, isCurrent;
    private List<String> listReportingImages = new ArrayList<>();
    private List<String> listReportingAudio = new ArrayList<>();
    private List<ReportForward> list;
    private ImagesSwipe mImageSwipe;
    private TextView[] mDots;


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

    @BindView(R.id.imageViewPager)
    ViewPager viewPager;
    @BindView(R.id.pointing_images)
    LinearLayout linearLayoutImages;
    private MediaPlayer player = null;
    private boolean boolForAudio = true;
    private Handler handler;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_single_forward_record_detail);
        if(unbinder == null){
            unbinder = ButterKnife.bind(this);
        }
        toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        data = getIntent().getExtras();

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
    protected void onStart() {
        super.onStart();

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
            intent.putExtra(Constants.FORWARD_FROM, forwardFrom);
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

    private void getSingleComplainDetail(String complainId) {
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(Constants.REST_API)
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        RequestBody accountRqst = RequestBody.create(MediaType.parse("text/plain"), complainId);
        JsonApiHolder service = retrofit.create(JsonApiHolder.class);

        Call<List<ReportForward>> listCall = service.getSingleComplainDetailForwardingWithAttachment(complainId);

        listCall.enqueue(new Callback<List<ReportForward>>() {
            @Override
            public void onResponse(Call<List<ReportForward>> call, Response<List<ReportForward>> response) {
                if(response.isSuccessful()){
                    boolean bool = false;
                    Log.d(TAG, "onResponse: Successfull");
                    list = response.body();
                    if(list.get(0).getEmployee_name() != null){
                    forwardToTv.append(" Employee name : " + list.get(0).getEmployee_name());
                    }
                    Log.d(TAG, "onResponse:forward "+ list.size());

                    for(ReportForward l : list){
                        if(l.getReporting_attachment_name() != null){
                            if(l.getReporting_attachment_file_type().contains("3gp")){
                                Log.d(TAG, "onResponse:forward "+ list.size());
                                listReportingAudio.add(l.getReporting_attachment_name());
                            }else {
                                Log.d(TAG, "onResponse:forward " + l.getReporting_attachment_name());
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
                    if(listReportingAudio.size() != 0){

                            audioLayout.setVisibility(View.VISIBLE);

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
        employeeId = data.getString(getString(R.string.permanentlogin_name));
        Log.d(TAG, "settingValues: " + employeeId);


        if(user.equals("admin")){
            reply.setVisibility(View.VISIBLE);
            forward.setVisibility(View.VISIBLE);
            acknowledged.setVisibility(View.VISIBLE);
            isSeenUpdate(reportingId);
        }else {
            reply.setVisibility(View.GONE);
            forward.setVisibility(View.GONE);
            acknowledged.setVisibility(View.GONE);
        }

        remarks.setText(remarksBody);
        forwardToTv.setText(designationTitle);
        expectedDate.setText(suggestedDate);
        getSingleComplainDetail(reportingId);


    }

    private void isSeenUpdate(String reportingId) {
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(Constants.REST_API)
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        JsonApiHolder service = retrofit.create(JsonApiHolder.class);

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
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(Constants.REST_API)
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        JsonApiHolder service = retrofit.create(JsonApiHolder.class);

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
}
