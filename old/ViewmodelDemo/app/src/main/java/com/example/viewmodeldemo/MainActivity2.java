package com.example.viewmodeldemo;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;


public class MainActivity2 extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main_activity);
        if (savedInstanceState == null) {
            getSupportFragmentManager().beginTransaction()
                    .commitNow();
        }
    }
}