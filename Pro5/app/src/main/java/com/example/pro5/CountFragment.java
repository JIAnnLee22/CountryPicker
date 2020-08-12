package com.example.pro5;

import android.annotation.SuppressLint;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;

import static android.content.ContentValues.TAG;

public class CountFragment extends Fragment {

    private PieChartView chartView;
    List<Float> list = new ArrayList<>();

    //各个控件
    private TextView turnover;//营业额
    private TextView effective;//有效订单
    private TextView exporing;//曝光客户
    private TextView storeRate;//进店转换率
    private TextView orderRate;//下单转换率

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_count, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        initView(view);
    }

    @Override
    public void onResume() {
        super.onResume();
        initData();
        initPlate1();
    }

    private void initData() {
        list.add(567f);
        list.add(456f);
        list.add(345f);
        list.add(234f);
        list.add(123f);
        chartView.setList(list);
    }

    private void initView(View view) {
        chartView = view.findViewById(R.id.piechart);
        turnover = view.findViewById(R.id.turnover);
        effective = view.findViewById(R.id.effective);
        exporing = view.findViewById(R.id.exposing);
        storeRate = view.findViewById(R.id.store_rate);
        orderRate = view.findViewById(R.id.order_rate);
    }

    //第一个块
    @SuppressLint("SetTextI18n")
    private void initPlate1() {

        DecimalFormat decimalFormat = new DecimalFormat("0.0");
        DecimalFormat decimalFormatInt = new DecimalFormat("0");

        if (list == null) {
            Log.d(TAG, "initPlate1: null");
        }

        List<TextView> textViews = new ArrayList<>();
        textViews.add(turnover);
        textViews.add(effective);
        textViews.add(exporing);
        textViews.add(storeRate);
        textViews.add(orderRate);

        for (TextView t : textViews) {
            Float str = list.get(textViews.indexOf(t));
            switch (textViews.indexOf(t)) {
                case 0:
                    t.setText(str.toString());
                    break;
                case 1:
                case 2:
                    t.setText(decimalFormatInt.format(str));
                    break;
                case 3:
                case 4:
                    t.setText(decimalFormat.format(str / chartView.account() * 100) + "%");
                    break;
            }
        }
    }

}