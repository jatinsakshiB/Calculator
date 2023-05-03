package com.jsb.calculator.activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.app.Activity;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.LoadAdError;
import com.google.android.gms.ads.MobileAds;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.jsb.calculator.adapter.HistoryAdapter;
import com.jsb.calculator.databinding.ActivityHistoryBinding;
import com.jsb.calculator.modules.CalculatorHistory;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class HistoryActivity extends AppCompatActivity {

    private ActivityHistoryBinding binding;
    private Activity activity;
    private SharedPreferences sharedPreferences;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityHistoryBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        activity = this;
        sharedPreferences = getSharedPreferences("com.jsb.calculator", MODE_PRIVATE);



        List<CalculatorHistory> calHisList = getCalHisList();
        Collections.reverse(calHisList);

        LinearLayoutManager llm = new LinearLayoutManager(this);
        llm.setOrientation(LinearLayoutManager.VERTICAL);
        binding.listView.setLayoutManager(llm);

        //ollections.reverse(qrCodes);

        HistoryAdapter myAppointmentsAdapter = new HistoryAdapter(activity, calHisList);
        binding.listView.setAdapter(myAppointmentsAdapter);
        binding.listView.setVisibility(View.VISIBLE);

        myAppointmentsAdapter.setOnSize0(() -> {
            binding.errorTv.setVisibility(View.VISIBLE);
            binding.listView.setVisibility(View.GONE);
        });

        showAds();

    }

    private void showAds() {
        MobileAds.initialize(this);

        AdRequest adRequest = new AdRequest.Builder().build();
        binding.adView.loadAd(adRequest);

        binding.adView.setAdListener(new AdListener(){
            @Override
            public void onAdLoaded() {
                Log.e("tasting", "onCreate: onAdLoaded" );
                super.onAdLoaded();
            }

            @Override
            public void onAdFailedToLoad(LoadAdError loadAdError) {
                Log.e("tasting", "onCreate: onAdFailedToLoad: "+loadAdError.toString() );
                super.onAdFailedToLoad(loadAdError);
            }

            @Override
            public void onAdClicked() {
                Log.e("tasting", "onCreate: onAdClicked" );
                super.onAdClicked();
            }

            @Override
            public void onAdImpression() {
                Log.e("tasting", "onCreate: onAdImpression" );
                super.onAdImpression();
            }
        });
    }



    Gson gson = new Gson();

    public List<CalculatorHistory> getCalHisList() {
        String json = sharedPreferences.getString("CalHis", null);
        if (json == null){
            return new ArrayList<>();
        }
        Type type = new TypeToken<List<CalculatorHistory>>() {
        }.getType();
        return gson.fromJson(json, type);
    }
}