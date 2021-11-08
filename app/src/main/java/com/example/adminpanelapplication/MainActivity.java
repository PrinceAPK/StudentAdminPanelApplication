package com.example.adminpanelapplication;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import android.annotation.SuppressLint;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    CardView uploadNotice;
    CardView uploadImage;
    CardView uploadPdf;
    CardView updateFaculty;
    CardView deleteNotice;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //assign and upload PDF button click feature
        uploadNotice = findViewById(R.id.addNotice);
        uploadNotice.setOnClickListener(this);

        //assign and upload Image button click feature
        uploadImage = findViewById(R.id.addGalleryImage);
        uploadImage.setOnClickListener(this);

        //upload PDF feature button click
        uploadPdf = findViewById(R.id.addPdf);
        uploadPdf.setOnClickListener(this);

        //update Faculty button click feature
        updateFaculty = findViewById(R.id.addFaculty);
        updateFaculty.setOnClickListener(this);

        //delete Notice button click feature
        deleteNotice = findViewById(R.id.deleteNotice);
        deleteNotice.setOnClickListener(this);

    }

    @SuppressLint("NonConstantResourceId")
    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.addNotice:
                Intent intent = new Intent(MainActivity.this, UploadNotice.class);
                startActivity(intent);
                break;

            case R.id.addPdf:
                intent = new Intent(MainActivity.this, UploadPdf.class);
                startActivity(intent);
                break;

            case R.id.addFaculty:
                intent = new Intent(MainActivity.this, UpdateFaculty.class);
                startActivity(intent);
                break;

            case R.id.deleteNotice:
                intent = new Intent(MainActivity.this, DeleteNotice.class);
                startActivity(intent);
                break;

            case R.id.addImage:
            default:
                intent = new Intent(MainActivity.this, UploadImages.class);
                startActivity(intent);
        }
    }


    @Override
    public void onBackPressed() {
        finish();
        super.onBackPressed();
    }
}