package com.example.complaintsystembeta.ui.complaints;


import android.content.Intent;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.complaintsystembeta.R;
import com.example.complaintsystembeta.constants.Constants;
import com.example.complaintsystembeta.interfaace.JsonApiHolder;
import com.example.complaintsystembeta.model.AllComplains;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

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
    private TextView all, pending, newC, resolved;
    private View view;

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
    }

    private void initializingWidgets() {
        linearLayoutAllComplains = view.findViewById(R.id.allComplains);
        linearLayoutNewComplains = view.findViewById(R.id.newComplains);
        linearLayoutPendingComplains = view.findViewById(R.id.pendingComplains);
        linearLayoutResolvedComplains = view.findViewById(R.id.resolvedComplains);

        all = view.findViewById(R.id.allComplainsT);
        newC = view.findViewById(R.id.newComplainsT);
        pending = view.findViewById(R.id.pendingComplainsT);
        resolved = view.findViewById(R.id.resolvedComplainsT);
    }

    private void fetchComplains() {
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(Constants.REST_API)
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        JsonApiHolder service = retrofit.create(JsonApiHolder.class);

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
                        } else if (all.getComplain_status().equals(Constants.COMPLAINS_PENDING)) {
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

    private void getArrayListForGraphInteger() {
        all.setText(String.valueOf(valuesForNew.size() + valuesForPending.size() + valuesForResolved.size()));
        newC.setText(String.valueOf(valuesForNew.size()));
        pending.setText(String.valueOf(valuesForPending.size()));
        resolved.setText(String.valueOf(valuesForResolved.size()));

    }
}
