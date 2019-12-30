package com.rifat.qrscanner.activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.FileProvider;

import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.MultiFormatWriter;
import com.google.zxing.WriterException;
import com.google.zxing.common.BitMatrix;
import com.journeyapps.barcodescanner.BarcodeEncoder;
import com.rifat.qrscanner.R;

import java.io.File;
import java.io.FileOutputStream;

public class GenerateActivity extends AppCompatActivity {
    private EditText text;
    private Button generate;
    private ImageView imageQR, imageBC;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_generate);

        init();
        generate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String input = text.getText().toString();
                if (input != null) {
                    getCode(input);
                }
            }
        });
    }

    private void init() {
        text = findViewById(R.id.inputText);
        imageQR = findViewById(R.id.outputBitmapQR);
        imageBC = findViewById(R.id.outputBitmapBC);
        generate = findViewById(R.id.generateBtn);
    }
    private void getCode(String input) {
        try {
            qrCode(input);
            barCode(input);
        } catch (Exception e) {
            e.printStackTrace();

        }
    }

    MultiFormatWriter multiFormatWriter = new MultiFormatWriter();

    private void qrCode(String input) throws WriterException {
        BitMatrix bitMatrix = multiFormatWriter.encode(input, BarcodeFormat.QR_CODE, 350, 300);
        BarcodeEncoder barcodeEncoder = new BarcodeEncoder();
        Bitmap bitmap = barcodeEncoder.createBitmap(bitMatrix);
        imageQR.setImageBitmap(bitmap);

    }

    private void barCode(String input) throws WriterException {
        BitMatrix bitMatrix = multiFormatWriter.encode(input, BarcodeFormat.CODE_128, 350, 170, null);
        BarcodeEncoder barcodeEncoder = new BarcodeEncoder();
        Bitmap bitmap = barcodeEncoder.createBitmap(bitMatrix);
        imageBC.setImageBitmap(bitmap);
    }

    private void save(){
        imageQR.setDrawingCacheEnabled(true);
        Bitmap bitmap=imageQR.getDrawingCache();
        File file=new File(Environment.getExternalStorageDirectory(),"bar_code.jpg");
        try {
            file.createNewFile();
            FileOutputStream fileOutputStream=new FileOutputStream(file);
            bitmap.compress(Bitmap.CompressFormat.JPEG,100,fileOutputStream);
            fileOutputStream.close();
            //shareingFile

            Intent intent=new Intent(Intent.ACTION_SEND);
            if(Build.VERSION.SDK_INT>=Build.VERSION_CODES.N){
                intent.setFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
                intent.putExtra(Intent.EXTRA_STREAM, FileProvider.getUriForFile(GenerateActivity.this,"com.example.androidbarcode",file));
            }
            else{
                intent.putExtra(Intent.EXTRA_STREAM, Uri.fromFile(file));
            }
            intent.setType("image/*");
            startActivity(intent);
        }
        catch (Exception e){
            e.printStackTrace();
        }
    }

}
