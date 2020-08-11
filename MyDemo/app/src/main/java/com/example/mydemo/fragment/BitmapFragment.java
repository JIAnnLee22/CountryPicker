package com.example.mydemo.fragment;

import android.annotation.SuppressLint;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.mydemo.R;
import com.example.mydemo.databinding.FragmentBitmapBinding;

import static android.content.ContentValues.TAG;

public class BitmapFragment extends Fragment {

    private FragmentBitmapBinding binding;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentBitmapBinding.inflate(inflater);
        // Inflate the layout for this fragment
        return binding.getRoot();
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        //        Bitmap bitmap2 = ((BitmapDrawable)getResources().getDrawable(R.drawable.ic_head)).getBitmap();
//        Log.d(TAG, "onActivityCreated: "+bitmap2);
    }

    @Override
    public void onResume() {
        super.onResume();
        Log.d(TAG, "onResume: "+R.drawable.ic_head);
        Bitmap bitmap = BitmapFactory.decodeResource(getResources(),R.drawable.ic_head);
        Log.d(TAG, "onActivityCreated: "+bitmap);
    }
}