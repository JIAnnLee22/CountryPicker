package com.example.viewmodeldemo.ui.fragment;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.ValueAnimator;
import android.annotation.SuppressLint;
import android.os.Build;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CalendarView;
import android.widget.Toast;

import com.example.viewmodeldemo.ui.adapter.MyAdapter;
import com.example.viewmodeldemo.ui.custom.CalendarDialog;
import com.example.viewmodeldemo.MainViewModel;
import com.example.viewmodeldemo.R;
import com.example.viewmodeldemo.databinding.FragmentMainBinding;
//import com.haibin.calendarview.CalendarView;

import org.jetbrains.annotations.NotNull;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;


public class MainFragment extends Fragment {

    private FragmentMainBinding binding;
    private static final String TAG = "x";
    private List<String> nameList = new ArrayList<>();
    private Boolean iSelect = false;
    private float mDensity;
    private int mHiddenViewMeasuredHeight;
    private MainViewModel mainViewModel;

    private int day = 0;
    private int month = 0;
    private int year = 0;

    private Handler dateHandler;

    @Override
    public View onCreateView(@NotNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        binding = FragmentMainBinding.inflate(inflater, container, false);
        return binding.getRoot();
//        return inflater.inflate(R.layout.fragment_main,container);
    }


    @RequiresApi(api = Build.VERSION_CODES.N)
    @SuppressLint("SetTextI18n")
    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        mDensity = getResources().getDisplayMetrics().density;
        binding.gonecard.measure(0, 0);
        mHiddenViewMeasuredHeight = (int) (binding.gonecard.getMeasuredHeight() + 0.5);

        LinearLayoutManager layoutManager = new LinearLayoutManager(binding.getRoot().getContext());
        mainViewModel = new ViewModelProvider(this).get(MainViewModel.class);

        binding.recyclerView.setLayoutManager(layoutManager);
        binding.recyclerView.setAdapter(new MyAdapter(mainViewModel.getUsersList()));

        //统计人数显示在textview上
        binding.acount.setText("(共" + nameList.size() + "人)");

//        //格式化时间并显示当前时间
//        @SuppressLint("SimpleDateFormat") SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");// yyyy-MM-dd
//        Date date = new Date(System.currentTimeMillis());
//        binding.date.setText(simpleDateFormat.format(date));
        if (year == 0) {
            setDate();
            binding.date.setText(year + "-" + month + "-" + day);
            Log.d(TAG, "mainAcitivity: " + year + month + day);
        } else {
            binding.date.setText(year + "-" + month + "-" + day);
        }


        click();
    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    private void click() {
        //点击展开开具发票，默认不展开
        binding.select.setOnClickListener(view -> {
            //默认不展开发票
            if (!iSelect) {
                animationOpen(binding.gonecard);
                binding.select.setImageResource(R.drawable.ic_activity_release_select);
                iSelect = true;
            } else {
                animateClose(binding.gonecard);
                binding.select.setImageResource(R.drawable.ic_activity_release_unselect);
                iSelect = false;
            }
        });

        //打开title
        binding.right.setOnClickListener(view -> Navigation.findNavController(view).navigate(R.id.to_title));

        //创建日期选择器
        CalendarDialog calendarDialog = new CalendarDialog(binding.getRoot().getContext(), year, month, day);

        //选择日期，打开日期选择器
        binding.calendar.setOnClickListener(view -> {
            calendarDialog.showCalenderDialog(binding.date);
            year = calendarDialog.getNowYear();
            month = calendarDialog.getNowMonth();
            day = calendarDialog.getNowDay();
        });

        //点击上传资料
        binding.upload.setOnClickListener(view -> {
            Toast.makeText(binding.getRoot().getContext(), "点了上传资料按钮", Toast.LENGTH_SHORT).show();
            Navigation.findNavController(view).navigate(R.id.to_upload);
        });

        //点击添加申请人
        binding.add.setOnClickListener(view -> Navigation.findNavController(binding.getRoot()).navigate(R.id.to_add));

        //点击支付
        binding.pay.setOnClickListener(view -> {
            Toast.makeText(binding.getRoot().getContext(), "点了立即支付按钮", Toast.LENGTH_SHORT).show();
            Navigation.findNavController(binding.getRoot()).navigate(R.id.to_pay);
        });

        //点击地址
        binding.address.setOnClickListener(view -> {
            Toast.makeText(binding.getRoot().getContext(), "点了邮寄地址按钮", Toast.LENGTH_SHORT).show();
            Navigation.findNavController(binding.getRoot()).navigate(R.id.to_address);
        });

        //点击使用优惠
        binding.discount.setOnClickListener(view -> {
            Toast.makeText(binding.getRoot().getContext(), "点了使用优惠按钮", Toast.LENGTH_SHORT).show();
            Navigation.findNavController(binding.getRoot()).navigate(R.id.to_discount);
        });
    }

    private void setDate() {
        Calendar c = Calendar.getInstance();
        year = c.get(Calendar.YEAR);
        month = c.get(Calendar.MONTH) + 1;
        day = c.get(Calendar.DAY_OF_MONTH);
    }

    //创建动画
    private ValueAnimator createDropAnimator(final View v, int start, int end) {
        ValueAnimator animator = ValueAnimator.ofInt(start, end);
        animator.addUpdateListener(arg0 -> {
            int value = (int) arg0.getAnimatedValue();
            ViewGroup.LayoutParams layoutParams = v.getLayoutParams();
            layoutParams.height = value;
            v.setLayoutParams(layoutParams);

        });
        return animator;
    }

    //隐藏布局打开动画
    private void animationOpen(View v) {
        v.setVisibility(View.VISIBLE);
        ValueAnimator animator = createDropAnimator(v, 0,
                mHiddenViewMeasuredHeight);
        animator.start();
    }

    //隐藏布局隐藏动画
    private void animateClose(final View view) {
        int origHeight = view.getHeight();
        ValueAnimator animator = createDropAnimator(view, origHeight, 0);
        animator.addListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animation) {
                view.setVisibility(View.GONE);
            }

        });
        animator.start();
    }

}