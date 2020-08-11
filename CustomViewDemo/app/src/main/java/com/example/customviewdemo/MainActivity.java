package com.example.customviewdemo;

import androidx.appcompat.app.AppCompatActivity;

import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;

import com.example.customviewdemo.custom.ScanningView;
import com.example.customviewdemo.databinding.ActivityMainBinding;

public class MainActivity extends AppCompatActivity {

    private static final String TAG = "MainActivity";
        private ActivityMainBinding mainBinding;
    private Button topBtn;
    private Button bottomBtn;
    private ScanningView centerView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mainBinding = ActivityMainBinding.inflate(LayoutInflater.from(this));
        setContentView(mainBinding.getRoot());
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
    }

    @Override
    public void onWindowFocusChanged(boolean hasFocus) {
        super.onWindowFocusChanged(hasFocus);
        mainBinding.centerView.setVisibility(View.GONE);
        mainBinding.centerView.setmTop(mainBinding.topBtn.getBottom());
        mainBinding.centerView.setmBottom(mainBinding.bottomBtn.getTop());
        mainBinding.centerView.setParentWidth(mainBinding.getRoot().getWidth());
        mainBinding.centerView.setVisibility(View.VISIBLE);
        mainBinding.centerView.requestLayout();
    }
}