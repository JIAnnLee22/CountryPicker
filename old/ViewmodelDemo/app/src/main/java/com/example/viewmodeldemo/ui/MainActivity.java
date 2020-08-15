package com.example.viewmodeldemo.ui;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.NavigationUI;


import android.Manifest;
import android.app.DatePickerDialog;
import android.content.pm.ActivityInfo;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.view.KeyEvent;

import com.example.viewmodeldemo.MainViewModel;
import com.example.viewmodeldemo.R;
import com.example.viewmodeldemo.databinding.ActivityMainBinding;


import java.util.ArrayList;

import java.util.Calendar;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    private static final String TAG = "x";
    private ActivityMainBinding binding;
    private List<String> nameList = new ArrayList<>();
    private Boolean iSelect = false;
    //    private float mDensity;
    private int mHiddenViewMeasuredHeight;
    private DatePickerDialog datePickerDialog;
    private Calendar mcalendar;
    private MainViewModel mainViewModel;
    private NavController navController;
    private final int REQUEST_CODE_CAMERA = 1001;
    private static final int REQUEST_CODE_WRITE_EXTERNAL_STORAGE = 1002;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        navController = Navigation.findNavController(this, R.id.nav_graph_fragment);
//        NavigationUI.setupActionBarWithNavController(this, navController);
        requestPermission();
    }

    @Override
    public boolean onSupportNavigateUp() {
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        return navController.navigateUp();
        //return super.onSupportNavigateUp();
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (getRequestedOrientation() == ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE) {
            setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
            return navController.navigateUp();
        } else {
            setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
            return super.onKeyDown(keyCode, event);
        }
    }

    public void requestPermission() {

        // checkSelfPermission 判断是否已经申请了此权限
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA)
                != PackageManager.PERMISSION_GRANTED ||
                ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE)
                        != PackageManager.PERMISSION_GRANTED) {
            //如果应用之前请求过此权限但用户拒绝了请求，shouldShowRequestPermissionRationale将返回 true。
            if (ActivityCompat.shouldShowRequestPermissionRationale(this,
                    Manifest.permission.CAMERA)) {

            } else {
                // ActivityCompat.requestPermissions(this,new String[]{Manifest.permission.CAMERA}, REQUEST_CODE_CAMERA);
                ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.CAMERA}, REQUEST_CODE_WRITE_EXTERNAL_STORAGE);
            }
        }
    }
}