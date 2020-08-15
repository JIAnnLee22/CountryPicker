package com.example.pro5;

import android.annotation.SuppressLint;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

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
    private CircleView circleView;

    //各个控件
    private TextView turnover;//营业额
    private TextView effective;//有效订单
    private TextView exporing;//曝光客户
    private TextView storeRate;//进店转换率
    private TextView orderRate;//下单转换率

    private RecyclerView sales_list;//热销商品列表

    private List<Float> list = new ArrayList<>();//饼状图数据填充
    private List<SaleBean> saleBeanList = new ArrayList<>();//热销商品Bean数据填充

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the sales_item for this fragment
        return inflater.inflate(R.layout.fragment_count, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        //初始化控件
        initView(view);
        //初始化填充数据
        initData();
    }

    @Override
    public void onResume() {
        super.onResume();
        //处理第一块卡片布局
        initPlate1();
        //处理热销商品
        initRecyclerView();
    }

    //模拟数据
    private void initData() {
        list.add(567f);
        list.add(456f);
        list.add(345f);
        list.add(234f);
        list.add(123f);

        saleBeanList.add(new SaleBean("苹果", 999, 3f));
        saleBeanList.add(new SaleBean("香蕉", 888, 2f));
        saleBeanList.add(new SaleBean("草莓", 777, 1f));
        saleBeanList.add(new SaleBean("芒果", 666, 0.5f));
    }

    //初始化控件
    private void initView(View view) {
        chartView = view.findViewById(R.id.piechart);
        chartView.setList(list);

        circleView = view.findViewById(R.id.circleView);
        circleView.setNewC(234);
        circleView.setOldC(789);

        sales_list = view.findViewById(R.id.sales_list);

        turnover = view.findViewById(R.id.turnover);
        effective = view.findViewById(R.id.effective);
        exporing = view.findViewById(R.id.exposing);
        storeRate = view.findViewById(R.id.store_rate);
        orderRate = view.findViewById(R.id.order_rate);
    }

    //第一个块
    @SuppressLint("SetTextI18n")
    private void initPlate1() {

        //数字显示的格式
        DecimalFormat decimalFormat = new DecimalFormat("0.0");//保留一位小数
        DecimalFormat decimalFormatInt = new DecimalFormat("0");//不保留小数

        if (list.size() > 0) {
            //针对不同的文字处理数字的格式
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

    //recyclerView-->热销商品
    private void initRecyclerView() {
        //将数据放进适配器
        LinearLayoutManager llm = new LinearLayoutManager(getContext()){
            @Override
            public boolean canScrollVertically() {
                return false;
            }
        };
        SalesAdapter salesAdapter = new SalesAdapter(saleBeanList);
        sales_list.setLayoutManager(llm);
        sales_list.setAdapter(salesAdapter);
    }

}