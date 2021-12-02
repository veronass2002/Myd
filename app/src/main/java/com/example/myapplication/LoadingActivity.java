package com.example.myapplication;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;

import com.android.volley.toolbox.Volley;

public class LoadingActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        new Handler().postDelayed((Runnable) () -> {
            startActivity(new Intent(this, RegisterActivity.class));
            finish();
        }, 1100); // Миллисекунды)
    }


}