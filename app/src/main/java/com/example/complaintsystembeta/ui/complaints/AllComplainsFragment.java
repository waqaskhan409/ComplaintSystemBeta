package com.example.complaintsystembeta.ui.complaints;


import android.content.Intent;
import android.os.Build;
import android.os.Bundle;

import androidx.annotation.RequiresApi;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.complaintsystembeta.R;
import com.example.complaintsystembeta.constants.Constants;
import com.example.complaintsystembeta.constants.RestApi;
import com.example.complaintsystembeta.interfaace.JsonApiHolder;
import com.example.complaintsystembeta.model.AllComplains;
import com.example.complaintsystembeta.model.Consumer;
import com.example.complaintsystembeta.ui.login.LoginActivity;
import com.example.complaintsystembeta.ui.profile.ProfileVerification;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

import static java.lang.Thread.sleep;

/**
 * A simple {@link Fragment} subclass.
 */
public class AllComplainsFragment extends Fragment {
    private static final String TAG = "AllComplainsFragment";
    private String accountNumber, name;

    private ArrayList<AllComplains> valuesForPending = new ArrayList<>();
    private ArrayList<AllComplains> valuesForNew = new ArrayList<>();
    private ArrayList<AllComplains> valuesForResolved = new ArrayList<>();

    private LinearLayout linearLayoutAllComplains, linearLayoutNewComplains, linearLayoutPendingComplains, linearLayoutResolvedComplains;
    private TextView all, pending, newC, resolved, topBarVerificatiedUser;
    private View view;
    boolean verify = false;

    public AllComplainsFragment() {
        // Required empty public constructor
    }

    public AllComplainsFragment(String accountNumber, String name) {
        this.accountNumber = accountNumber;
        this.name = name;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_all_complains, container, false);
        initializingWidgets();
        fetchComplains();
        listeners();
        fetchConsumer(accountNumber);
        return view;
    }

    private void listeners() {
        linearLayoutAllComplains.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), Compliants.class);
                intent.putExtra(Constants.ACCOUNT_NUMBER, accountNumber);
                intent.putExtra(Constants.NAME, name);
                intent.putExtra(Constants.COMPLAIN_FILTER, Constants.ALL_COMPLAINS);
                startActivity(intent);
            }
        });
        linearLayoutNewComplains.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), Compliants.class);
                intent.putExtra(Constants.ACCOUNT_NUMBER, accountNumber);
                intent.putExtra(Constants.NAME, name);
                intent.putExtra(Constants.COMPLAIN_FILTER, Constants.COMPLAINS_NEW);
                startActivity(intent);
            }
        });
        linearLayoutPendingComplains.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), Compliants.class);
                intent.putExtra(Constants.ACCOUNT_NUMBER, accountNumber);
                intent.putExtra(Constants.NAME, name);
                intent.putExtra(Constants.COMPLAIN_FILTER, Constants.PENDING_COMPLAINS);
                startActivity(intent);
            }
        });
        linearLayoutResolvedComplains.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), Compliants.class);
                intent.putExtra(Constants.ACCOUNT_NUMBER, accountNumber);
                intent.putExtra(Constants.NAME, name);
                intent.putExtra(Constants.COMPLAIN_FILTER, Constants.COMPLAINS_RESOLVED);
                startActivity(intent);
            }
        });

        topBarVerificatiedUser.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                    getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.flContent, new ProfileVerification(accountNumber, name)).commit();
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
                if(response.body().getIs_verified().equals("0")){
                    topBarVerificatiedUser.setVisibility(View.VISIBLE);
                    Animation anim = new AlphaAnimation(0.0f, 1.0f);
                    anim.setDuration(1000); //You can manage the blinking time with this parameter
                    anim.setStartOffset(20);
                    anim.setRepeatMode(Animation.REVERSE);
                    anim.setRepeatCount(Animation.INFINITE);
                    topBarVerificatiedUser.startAnimation(anim);
//                    verify = false;
//                    checkUpdate();
                }else {
                    topBarVerificatiedUser.setVisibility(View.GONE);
                    verify  = true;

                }

            }

            @Override
            public void onFailure(Call<Consumer> call, Throwable t) {
                Log.d(TAG, "onFailure: " + t.getMessage());
            }
        });

    }

    private void initializingWidgets() {
        linearLayoutAllComplains = view.findViewById(R.id.allComplains);
        linearLayoutNewComplains = view.findViewById(R.id.newComplains);
        linearLayoutPendingComplains = view.findViewById(R.id.pendingComplains);
        linearLayoutResolvedComplains = view.findViewById(R.id.resolvedComplains);
        topBarVerificatiedUser = view.findViewById(R.id.topbarVerifiedUser);

        all = view.findViewById(R.id.allComplainsT);
        newC = view.findViewById(R.id.newComplainsT);
        pending = view.findViewById(R.id.pendingComplainsT);
        resolved = view.findViewById(R.id.resolvedComplainsT);
    }

    private void fetchComplains() {
        JsonApiHolder service = RestApi.getApi();
        Call<List<AllComplains>> call = service.getComplains();

        call.enqueue(new Callback<List<AllComplains>>() {
            @Override
            public void onResponse(Call<List<AllComplains>> call, Response<List<AllComplains>> response) {
                if(!response.isSuccessful()){
                    Log.d(TAG, "onResponse: Failed!");
                    return;
                }
                valuesForPending.clear();
                valuesForResolved.clear();
                valuesForNew.clear();
                Log.d(TAG, "onResponse: "+ response.body());
                List<AllComplains>  list = response.body();
                for(AllComplains all: list){
                    if(all.getAccount_number().equals(accountNumber)) {
                        if (all.getComplain_status().equals(Constants.COMPLAINS_RESOLVED)) {
                            valuesForResolved.add(all);
                        } else if (all.getComplain_status().equals(Constants.COMPLAINS_PENDING) ||
                                all.getComplain_status().equals(Constants.COMPLAINS_IN_PROCESS)) {
                            valuesForPending.add(all);
                        } else if (all.getComplain_status().equals(Constants.COMPLAINS_NEW)) {
                            valuesForNew.add(all);
                        }
                    }
                }
                getArrayListForGraphInteger();

            }

            @Override
            public void onFailure(Call<List<AllComplains>> call, Throwable t) {

            }
        });

    }

    public void checkUpdate() {
        getActivity().runOnUiThread(new Runnable() {
            @Override
            public void run() {
                try {
                    int i = 0;
                    while (true) {
                        sleep(1000);
                        if (!verify) {
                            if (i % 2 == 0) {
                                topBarVerificatiedUser.setVisibility(View.VISIBLE);
                                i++;
                            } else if (i % 1 == 1) {
                                topBarVerificatiedUser.setVisibility(View.GONE);
                                i++;
                            }
                        } else {
                            break;
                        }
                    }
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        });
    }

    private void getArrayListForGraphInteger() {
        all.setText(String.valueOf(valuesForNew.size() + valuesForPending.size() + valuesForResolved.size()));
        newC.setText(String.valueOf(valuesForNew.size()));
        pending.setText(String.valueOf(valuesForPending.size()));
        resolved.setText(String.valueOf(valuesForResolved.size()));

    }
}
