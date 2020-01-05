package com.example.complaintsystembeta.ui.complaints;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager.widget.ViewPager;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.text.Html;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.SeekBar;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.example.complaintsystembeta.BaseActivity;
import com.example.complaintsystembeta.R;
import com.example.complaintsystembeta.adapter.ConsumerComplaints;
import com.example.complaintsystembeta.adapter.ForwardAdapter;
import com.example.complaintsystembeta.constants.Constants;
import com.example.complaintsystembeta.constants.RestApi;
import com.example.complaintsystembeta.interfaace.JsonApiHolder;
import com.example.complaintsystembeta.model.AllComplains;
import com.example.complaintsystembeta.model.Employee;
import com.example.complaintsystembeta.model.PostResponse;
import com.example.complaintsystembeta.model.ReportForward;
import com.example.complaintsystembeta.model.SignUpData;
import com.example.complaintsystembeta.ui.dialogues.BottomSheetDialogueMessageForResolve;
import com.example.complaintsystembeta.ui.login.EmployeeLogin;
import com.squareup.picasso.Picasso;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

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

public class SingleComplainDetails extends BaseActivity {
    private static final String TAG = "SingleComplainDetails";
    private Unbinder unbinder;
    private Bundle data;
    private String complainId, user, userName = "", employeeId, decisionForwardToOrFrom;
    private MediaPlayer   player = null;
    private ArrayList<String> arrayListStatus = new ArrayList<>();
    private ArrayList<String> uriArrayList = new ArrayList<>();
    private ArrayList<String> audioArrayList = new ArrayList<>();
    private List<ReportForward> listFilter = new ArrayList<>();

    private Handler handler;
    private SearchView searchView;

    List<ReportForward> list;
    List<Employee> listEmployees;

    private ImagesSwipe mImageSwipe;
    private TextView[] mDots;
//    @BindView(R.id.confirmStatusButton)
//    Button confirmStatusButton;

    @BindView(R.id.confirmStatusSpinner)
    public Spinner confirmStatusSpinner;


    @BindView(R.id.confirmStatusLayout)
    LinearLayout confirmStatusLayout;


    @BindView(R.id.forward)
    Button forward;


    @BindView(R.id.recyclerviewForAllRecords)
    RecyclerView recyclerView;

    @BindView(R.id.compliansBody)
    TextView complainsBody;

    @BindView(R.id.complainsStatus)
    TextView complainsStatus;

    @BindView(R.id.noFile)
    TextView noFile;




    @BindView(R.id.seekBarLayout)
    LinearLayout audioLayout;

    @BindView(R.id.forwardsLayout)
    LinearLayout forwardsLayout;

    @BindView(R.id.audioPlay)
    ImageButton audioPlay;


    @BindView(R.id.seekBarAudio)
    SeekBar seekBar;

    @BindView(R.id.imageViewPager)
    ViewPager viewPager;

    @BindView(R.id.pointing_images)
    LinearLayout linearLayoutImages;


    private Toolbar toolbar;
    private boolean boolForAudio = true;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_single_complain_details);
        toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        if (unbinder == null) {
            unbinder = ButterKnife.bind(this);
        }

        data = getIntent().getExtras();
        complainId = data.getString(getString(R.string.complains_id));
        user = data.getString(Constants.PREVELDGES_ON_FORWARD);
        employeeId = data.getString(Constants.EMPLOYEE_ID);
        decisionForwardToOrFrom = data.getString(Constants.CHOICE);
        Log.d(TAG, "onCreate: usernAME" + user);
//        desId = data.getString(Constants.DESIGNATION_ID);
        userName = data.getString(Constants.DESIGNATION_ID);

//        Log.d(TAG, "onCreate: " + desId);
        Log.d(TAG, "onCreate: userName" + userName);
        Log.d(TAG, "onCreate: complainId" + complainId);


        if(userName == null){
            userName ="";
        }
        if(employeeId == null){
            employeeId = "";
        }
        if(decisionForwardToOrFrom == null){
            decisionForwardToOrFrom = "";
        }
        getSingleComplainDetail(complainId);
        arrayListStatus.add(Constants.COMPLAIN_IN_PROCESS);
        arrayListStatus.add(Constants.COMPLAINS_RESOLVED);
        ArrayAdapter adapter = new ArrayAdapter(this, android.R.layout.simple_list_item_1, arrayListStatus);
        confirmStatusSpinner.setAdapter(adapter);
        confirmStatusSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if(confirmStatusSpinner.getSelectedItem().toString().equals(Constants.COMPLAINS_RESOLVED)){
                    BottomSheetDialogueMessageForResolve messageForResolve = new BottomSheetDialogueMessageForResolve(complainId, employeeId);
                    messageForResolve.show(getSupportFragmentManager(), "ResolveFragment");
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                if(confirmStatusSpinner.getSelectedItem().toString().equals(Constants.COMPLAINS_RESOLVED)){
                    BottomSheetDialogueMessageForResolve messageForResolve = new BottomSheetDialogueMessageForResolve(complainId, employeeId);
                    messageForResolve.show(getSupportFragmentManager(), "ResolveFragment");
                }
            }
        });

        if(userName.equals(Constants.ADMIN)){
            Log.d(TAG, "onCreate: COMPLAIN ID ADMIN" + complainId);
            Log.d(TAG, "onCreate: COMPLAIN ID ADMIN" + decisionForwardToOrFrom);
            if(decisionForwardToOrFrom.equals(Constants.FORWARD_TO)){
                getFilterSingleComplainForwardingDetail(complainId);
            }else if(decisionForwardToOrFrom.equals(Constants.FORWARD_FROM)){
                getFilterSingleComplainForwardingFromDetail(complainId);
            } else if(decisionForwardToOrFrom.equals(Constants.DELAY)){
                decisionForwardToOrFrom = Constants.FORWARD_FROM;
                getFilterSingleComplainForwardingFromDetailAllDelay(complainId);
            }
            }else {
            Log.d(TAG, "onCreate: COMPLAIN IS NOT ADMIN" + complainId);
            confirmStatusLayout.setVisibility(View.GONE);
            if(decisionForwardToOrFrom.equals(Constants.FORWARD_TO)){
                getFilterSingleComplainForwardingDetail(complainId);
            }else if(decisionForwardToOrFrom.equals(Constants.FORWARD_FROM)){
                getFilterSingleComplainForwardingFromDetail(complainId);

            }else if(decisionForwardToOrFrom.equals(Constants.DELAY)){
                decisionForwardToOrFrom = Constants.FORWARD_FROM;
                getFilterSingleComplainForwardingFromDetailAllDelay(complainId);
            }else {
                getSingleComplainForwardingDetail(complainId);

            }
        }




        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        linearLayoutManager.setOrientation(RecyclerView.VERTICAL);

        recyclerView.setLayoutManager(linearLayoutManager);
        recyclerView.setHasFixedSize(true);

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
        if(!checkWifiOnAndConnected()  && !checkMobileDataOnAndConnected()){
            showSnackBarWifi(getString(R.string.wifi_message));
        }else {
            checkConnection();
        }
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                onBackPressed();
                break;

            case R.id.timeline:
                timeline();
                break;
            default:
                Log.d(TAG, "onOptionsItemSelected: nothing found!");
        }
        return super.onOptionsItemSelected(item);
    }

    private void timeline() {
        Intent intent = new Intent(this, ComplainStatistics.class);
        intent.putExtra(getString(R.string.complains_id),complainId);
        intent.putExtra(Constants.PREVELDGES_ON_FORWARD, "only view");
        intent.putExtra(Constants.STATUS_COMPLAIN,  complainsStatus.getText().toString());
        startActivity(intent);
    }

    @OnClick(R.id.forward)
    public void forwardComplain() {
        Intent intent = new Intent(this, ComplainForwarding.class);
        intent.putExtra(getString(R.string.complains_id), complainId);
        intent.putExtra(Constants.PREVELDGES_ON_FORWARD, user);
        intent.putExtra(Constants.FORWARD_FROM, "");
        intent.putExtra(Constants.FORWARD_TO, userName);
        intent.putExtra(getString(R.string.permanentlogin_name), userName);
        startActivity(intent);
    }
//    @OnClick(R.id.reply)
//    public void replyComplain(){
//        Intent intent = new Intent(this, ComplainForwarding.class);
//        intent.putExtra(getString(R.string.complains_id), complainId);
//        intent.putExtra(Constants.PREVELDGES_ON_FORWARD, user);
////        intent.putExtra(Constants.REPLY_TO_FORWARD, li);
//        startActivity(intent);
//    }
//    @OnClick(R.id.confirmStatusButton)
    public void updateStatus(){
        String newStatus;
        newStatus = confirmStatusSpinner.getSelectedItem().toString();
        if(newStatus.equals(Constants.NONE)){
            showSnackBar("Please select status", "");
        }else {
            complainsStatus.setText(newStatus);
            updateStatusOnDatabase(newStatus, complainId);
        }
    }

    private void updateStatusOnDatabase(String newStatus, String complainId) {
        JsonApiHolder service = RestApi.getApi();

        Call<PostResponse> listCall = service.updateStatus(complainId, newStatus);

        listCall.enqueue(new Callback<PostResponse>() {
            @Override
            public void onResponse(Call<PostResponse> call, Response<PostResponse> response) {
                if(response.isSuccessful()){
                    Log.d(TAG, "onResponse: Succefull");

                    Toast.makeText(SingleComplainDetails.this, response.body().getSuccess(), Toast.LENGTH_SHORT).show();
                }else {
                    Log.d(TAG, "onResponse: Failed!");
                }
            }

            @Override
            public void onFailure(Call<PostResponse> call, Throwable t) {
                Log.d(TAG, "onResponse: Failed! :" + t.getMessage());

            }
        });
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

//    @OnClick(R.id.audioPlay)
//    private void playAudio(){
//
//    }
    private void getSingleComplainDetail(String complainId) {
        JsonApiHolder service = RestApi.getApi();
        RequestBody accountRqst = RequestBody.create(MediaType.parse("text/plain"), complainId);

        Call<List<AllComplains>> listCall = service.getSingleComplainDetail(complainId);

        listCall.enqueue(new Callback<List<AllComplains>>() {
            @Override
            public void onResponse(Call<List<AllComplains>> call, Response<List<AllComplains>> response) {
                if(response.isSuccessful()){
                    Log.d(TAG, "onResponse: Succefull");

                    List<AllComplains> list = response.body();
                    for(AllComplains allComplains : list){
                        Log.d(TAG, "onResponse: forward_black: " + allComplains.getComplain_body());
                        Log.d(TAG, "onResponse: " + allComplains.getComplain_status());
                        Log.d(TAG, "onResponse: " + allComplains.getComplain_id());
                        Log.d(TAG, "onResponse: " + allComplains.getAttachment_file_type());
                        Log.d(TAG, "onResponse: " + allComplains.getAttachment_name());
                        complainsBody.setText(allComplains.getComplain_body());
                        complainsStatus.setText(allComplains.getComplain_status());
                        if(allComplains.getAttachment_id() != null){
                            if(allComplains.getAttachment_file_type().contains("3gp")){
                                audioArrayList.add(allComplains.getAttachment_name());
                            }else{
                            uriArrayList.add(allComplains.getAttachment_name());
                            }
                        }
                    }
                    if(uriArrayList.size() != 0){
//                    setupOnImagesLinearLayout();
                        noFile.setVisibility(View.GONE);
                        viewPager.setVisibility(View.VISIBLE);
                        mImageSwipe = new ImagesSwipe(SingleComplainDetails.this, uriArrayList);
                        viewPager.setAdapter(mImageSwipe);
                        addDotToLinearLayout(0);
                    }
                    if(audioArrayList.size() != 0){
                     audioLayout.setVisibility(View.VISIBLE);
                    }

                }else {
                    Log.d(TAG, "onResponse: Failed!");
                }
            }

            @Override
            public void onFailure(Call<List<AllComplains>> call, Throwable t) {
                Log.d(TAG, "onResponse: Failed! :" + t.getMessage());

            }
        });
    }

    private void addDotToLinearLayout(int position) {
        mDots = new TextView[uriArrayList.size()];
        linearLayoutImages.removeAllViews();
        for (int i = 0; i < uriArrayList.size(); i++) {
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
    private void getEmployee(String complainId) {
        JsonApiHolder service = RestApi.getApi();
        RequestBody accountRqst = RequestBody.create(MediaType.parse("text/plain"), complainId);

        Call<List<Employee>> listCall = service.getEmployee();

        listCall.enqueue(new Callback<List<Employee>>() {
            @Override
            public void onResponse(Call<List<Employee>> call, Response<List<Employee>> response) {
                if(response.isSuccessful()){
                    Log.d(TAG, "onResponse: Succefull");

                    listEmployees = response.body();
                    Log.d(TAG, "onResponse: " + listEmployees.size());
                    setupAdapter(list, listEmployees);



                }else {
                    Log.d(TAG, "onResponse: Failed!");
                }
            }

            @Override
            public void onFailure(Call<List<Employee>> call, Throwable t) {
                Log.d(TAG, "onResponse: Failed! :" + t.getMessage());

            }
        });
    }
    private void getSingleComplainForwardingDetail(String complainId) {
        JsonApiHolder service = RestApi.getApi();
        RequestBody accountRqst = RequestBody.create(MediaType.parse("text/plain"), complainId);

        Call<List<ReportForward>> listCall = service.getSingleComplainDetailForwarding(complainId);

        listCall.enqueue(new Callback<List<ReportForward>>() {
            @Override
            public void onResponse(Call<List<ReportForward>> call, Response<List<ReportForward>> response) {
                if(response.isSuccessful()){
                    Log.d(TAG, "onResponse: Succefull");

                    list = response.body();
                    getEmployee(complainId);



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
    private void getFilterSingleComplainForwardingDetail(String complainId) {
        JsonApiHolder service = RestApi.getApi();
        RequestBody accountRqst = RequestBody.create(MediaType.parse("text/plain"), complainId);

        Call<List<ReportForward>> listCall = service.getFilterSingleComplainDetailForwarding(complainId, employeeId);

        listCall.enqueue(new Callback<List<ReportForward>>() {
            @Override
            public void onResponse(Call<List<ReportForward>> call, Response<List<ReportForward>> response) {
                if(response.isSuccessful()){
                    Log.d(TAG, "onResponse: Succefull");

                    list = response.body();
                    Log.d(TAG, "onResponse: getFilterSingleComplainDetailForwarding :" + list.size());
                    getEmployee(complainId);


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
    private void getFilterSingleComplainForwardingFromDetail(String complainId) {
        JsonApiHolder service = RestApi.getApi();
        RequestBody accountRqst = RequestBody.create(MediaType.parse("text/plain"), complainId);

        Call<List<ReportForward>> listCall = service.getFilterSingleComplainDetailForwardingFrom(complainId, employeeId);

        listCall.enqueue(new Callback<List<ReportForward>>() {
            @Override
            public void onResponse(Call<List<ReportForward>> call, Response<List<ReportForward>> response) {
                if(response.isSuccessful()){
                    Log.d(TAG, "onResponse: Succefull");

                    list = response.body();
                    Log.d(TAG, "onResponse: getFilterSingleComplainDetailForwarding :" + list.size());
                    getEmployee(complainId);


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
    private void getFilterSingleComplainForwardingFromDetailAllDelay(String complainId) {
        JsonApiHolder service = RestApi.getApi();
        RequestBody accountRqst = RequestBody.create(MediaType.parse("text/plain"), complainId);

        Call<List<ReportForward>> listCall = service.getFilterSingleComplainDetailForwardingFromAllDelays(complainId, employeeId);

        listCall.enqueue(new Callback<List<ReportForward>>() {
            @Override
            public void onResponse(Call<List<ReportForward>> call, Response<List<ReportForward>> response) {
                if(response.isSuccessful()){
                    Log.d(TAG, "onResponse: Succefull");

                    list = response.body();
                    Log.d(TAG, "onResponse: getFilterSingleComplainDetailForwarding :" + list.size());
                    getEmployee(complainId);


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

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_detail, menu);
        MenuItem menuItem = menu.findItem(R.id.search);


        searchView = (SearchView) menuItem.getActionView();

        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                newText = newText.toLowerCase();
                searchViewMethod(newText);
                return false;
            }
        });

        return super.onCreateOptionsMenu(menu);
    }

    private void searchViewMethod(String newText) {
        listFilter.clear();
        for (int i = 0; i < list.size(); i++) {
            if(list.get(i).getForwards_date().toLowerCase().contains(newText)
                    || list.get(i).getForwards_by().toLowerCase().contains(newText)
                    || list.get(i).getForwards_message().toLowerCase().contains(newText)
                    || list.get(i).getForwards_to().toLowerCase().contains(newText)
                    || list.get(i).getSuggested_date_reply().toLowerCase().contains(newText)
                    || list.get(i).getStatus().toLowerCase().contains(newText)
            ){
                listFilter.add(list.get(i));
            }
        }
        setupAdapter(listFilter, listEmployees);
    }

    private void setupAdapter(List<ReportForward> list, List<Employee> listEmployees) {
        ForwardAdapter allComplainsAdapter  = new ForwardAdapter(list, this, userName, user, listEmployees, decisionForwardToOrFrom);
        recyclerView.setHasFixedSize(true);
        recyclerView.setAdapter(allComplainsAdapter);
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
            player.setDataSource(Constants.URL_IMAGES + audioArrayList.get(0));
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

    @Override
    protected void attachBaseContext(Context newBase) {
        super.attachBaseContext(ViewPumpContextWrapper.wrap(newBase));
    }
}
