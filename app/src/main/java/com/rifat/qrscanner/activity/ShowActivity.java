package com.rifat.qrscanner.activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.rifat.qrscanner.R;
import com.rifat.qrscanner.activity.adapter.BarcodeAdapter;
import com.rifat.qrscanner.activity.model.Bar;
import com.rifat.qrscanner.activity.retrofit.ApiClient;
import com.rifat.qrscanner.activity.retrofit.RetrofitInterface;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ShowActivity extends AppCompatActivity {
    private List<Bar> barcodeList;
    private RecyclerView recyclerView;
    private BarcodeAdapter adapter;
    private RetrofitInterface retrofitInterface;
    private ProgressBar progressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show);
        init();
        getData();
    }

    private void init() {
        barcodeList = new ArrayList<>();
        recyclerView = findViewById(R.id.rv);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        progressBar = findViewById(R.id.progressBar);
    }

    private void getData() {
        retrofitInterface = new ApiClient().getInstance().getApi();
        Call<List<Bar>> call = retrofitInterface.getData();
        call.enqueue(new Callback<List<Bar>>() {
            @Override
            public void onResponse(Call<List<Bar>> call, Response<List<Bar>> response) {
                barcodeList= response.body();
                adapter = new BarcodeAdapter(barcodeList);
                progressBar.setVisibility(View.GONE);
                recyclerView.setAdapter(adapter);
                adapter.notifyDataSetChanged();
            }

            @Override
            public void onFailure(Call<List<Bar>> call, Throwable t) {
                Toast.makeText(ShowActivity.this, ""+t.getMessage(), Toast.LENGTH_LONG).show();

            }
        });
    }
}
