package com.example.mydemo.fragment;

import android.Manifest;
import android.content.pm.PackageManager;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.mydemo.R;
import com.example.mydemo.databinding.FragmentMainBinding;

public class MainFragment extends Fragment {

    private static final int REQUEST_CODE_WRITE_EXTERNAL_STORAGE = 1002;
    private FragmentMainBinding binding;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentMainBinding.inflate(inflater);
        // Inflate the layout for this fragment
        return binding.getRoot();
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        click();
        requestPermission();
    }

    private void click() {
        binding.CameraBtn.setOnClickListener(view -> {
            Navigation.findNavController(view).navigate(R.id.action_mainFragment_to_cameraFragment);
        });
        binding.bitmapBtn.setOnClickListener(view -> {
            Navigation.findNavController(view).navigate(R.id.action_mainFragment_to_bitmapFragment);
        });
        binding.polylineBtn.setOnClickListener(view->{
            Navigation.findNavController(view).navigate(R.id.action_mainFragment_to_polylineFragment);
        });
    }

    public void requestPermission() {

        // checkSelfPermission 判断是否已经申请了此权限
        if (ContextCompat.checkSelfPermission(requireContext(), Manifest.permission.CAMERA)
                != PackageManager.PERMISSION_GRANTED ||
                ContextCompat.checkSelfPermission(requireContext(), Manifest.permission.WRITE_EXTERNAL_STORAGE)
                        != PackageManager.PERMISSION_GRANTED) {
            //如果应用之前请求过此权限但用户拒绝了请求，shouldShowRequestPermissionRationale将返回 true。
            if (ActivityCompat.shouldShowRequestPermissionRationale(requireActivity(),
                    Manifest.permission.CAMERA)) {

            } else {
                // ActivityCompat.requestPermissions(this,new String[]{Manifest.permission.CAMERA}, REQUEST_CODE_CAMERA);
                ActivityCompat.requestPermissions(requireActivity(), new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.CAMERA}, REQUEST_CODE_WRITE_EXTERNAL_STORAGE);
            }
        }
    }

}