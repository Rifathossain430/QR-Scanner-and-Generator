package com.rifat.qrscanner.activity;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.Manifest;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.zxing.Result;
import com.karumi.dexter.Dexter;
import com.karumi.dexter.PermissionToken;
import com.karumi.dexter.listener.PermissionDeniedResponse;
import com.karumi.dexter.listener.PermissionGrantedResponse;
import com.karumi.dexter.listener.PermissionRequest;
import com.karumi.dexter.listener.single.PermissionListener;
import com.rifat.qrscanner.R;
import com.rifat.qrscanner.activity.model.Bar;
import com.rifat.qrscanner.activity.model.BarNew;
import com.rifat.qrscanner.activity.retrofit.ApiClient;
import com.rifat.qrscanner.activity.retrofit.RetrofitInterface;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import me.dm7.barcodescanner.zbar.ZBarScannerView;
import me.dm7.barcodescanner.zxing.ZXingScannerView;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MainActivity extends AppCompatActivity implements ZXingScannerView.ResultHandler {
    private ZXingScannerView zXingScannerView;
    private ZBarScannerView mBarScanner;
    private TextView textView;
    private Button showBtn,saveBtn,generateBtn;
    private ApiClient apiClient;
    private Bar bar;
    private RetrofitInterface retrofitInterface;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initial();
        showBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(MainActivity.this,ShowActivity.class));
            }
        });
        generateBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(MainActivity.this,GenerateActivity.class));
            }
        });

        saveBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (textView.getText().toString().equals("")){
                    Toast.makeText(MainActivity.this, "Nothing to Save", Toast.LENGTH_SHORT).show();
                }else{
                    Bar bar = new Bar("hello");
                    retrofitInterface = new ApiClient().getInstance().getApi();
                    Call<Bar> call = retrofitInterface.postData(bar);
                    call.enqueue(new Callback<Bar>() {
                        @Override
                        public void onResponse(Call<Bar> call, Response<Bar> response) {
                            Toast.makeText(MainActivity.this, "Sucessful", Toast.LENGTH_SHORT).show();

                        }

                        @Override
                        public void onFailure(Call<Bar> call, Throwable t) {
                            Toast.makeText(MainActivity.this, ""+t.getMessage(), Toast.LENGTH_SHORT).show();

                        }
                    });
                }
            }
        });


        // Request Permission
        Dexter.withActivity(this)
                .withPermission(Manifest.permission.CAMERA)
                .withListener(new PermissionListener() {
                    @Override
                    public void onPermissionGranted(PermissionGrantedResponse response) {
                        zXingScannerView.setResultHandler(MainActivity.this);
                        zXingScannerView.startCamera();
                    }
                    @Override
                    public void onPermissionDenied(PermissionDeniedResponse response) {
                        Toast.makeText(MainActivity.this, "You must accept the Permission", Toast.LENGTH_LONG).show();

                    }
                    @Override
                    public void onPermissionRationaleShouldBeShown(PermissionRequest permission, PermissionToken token) {

                    }
                })
                .check();
    }


    private void initial() {
        textView = findViewById(R.id.scantext);
        showBtn = findViewById(R.id.showBtn);
        generateBtn = findViewById(R.id.generateBtn);
        saveBtn = findViewById(R.id.saveBtn);
        zXingScannerView = findViewById(R.id.qrCodeScanner);
        bar = new Bar("mobile123");

    }

    @Override
    public void handleResult(final Result rawResult) {
        if (rawResult!=null){
            boolean isContain = containsURL(rawResult.toString());
            if (isContain==true){
                new AlertDialog.Builder(this).setTitle("QR URL Result")
                        .setMessage(rawResult.getText())
                        .setPositiveButton("Go", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                Uri uri = Uri.parse(rawResult.toString()); // missing 'http://' will cause crashed
                                Intent intent = new Intent(Intent.ACTION_VIEW, uri);
                                startActivity(intent);

                            }
                        }).setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        dialogInterface.dismiss();
                    }
                }).create().show();
            }else if (isContain==false){
                textView.setText(rawResult.toString());


            }


        }
        zXingScannerView.resumeCameraPreview(this);

    }

    @Override
    protected void onRestart() {
        zXingScannerView.startCamera();
        super.onRestart();

    }

    @Override
    protected void onResume() {
        zXingScannerView.startCamera();
        super.onResume();
    }

    @Override
    protected void onPause() {
        zXingScannerView.startCamera();
        super.onPause();
    }

    private boolean containsURL(String content){
        String REGEX = "\\b(https?|ftp|file)://[-a-zA-Z0-9+&@#/%?=~_|!:,.;]*[-a-zA-Z0-9+&@#/%=~_|]";
        Pattern p = Pattern.compile(REGEX,Pattern.CASE_INSENSITIVE);
        Matcher m = p.matcher(content);
        if(m.find()) {
            return true;
        }

        return false;
    }
    @Override
    protected void onDestroy() {
        zXingScannerView.stopCamera();
        super.onDestroy();
    }


}
