package com.example.pro4;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.LinearSmoothScroller;
import androidx.recyclerview.widget.RecyclerView;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;

import com.example.pro4.databinding.ActivityMainBinding;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    private static final String TAG = "MainActivity";
    final LinearLayoutManager leftManager = new LinearLayoutManager(this);
    final LinearLayoutManager rightManager = new LinearLayoutManager(this);
    private ActivityMainBinding binding;
    private RecyclerView leftListView;
    private RecyclerView rightListView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    @SuppressLint("WrongConstant")
    @Override
    protected void onResume() {
        super.onResume();
        initRecyclerView();
    }

    private void initRecyclerView() {
        List<LeftBean> leftLsit = new ArrayList<>();
        final List<RightBean> rightLsit = new ArrayList<>();

        for (int j = 0; j < 20; ++j) {
            leftLsit.add(new LeftBean("" + j, "type" + j));
            for (int i = 0; i < 5; ++i) {
                rightLsit.add(new RightBean("" + j, "food" + i));
            }
        }

        final LeftAdapter leftAdapter = new LeftAdapter(leftLsit);
        final RightAdapter rightAdapter = new RightAdapter(rightLsit);

        configRecyclerView();
        leftListView = findViewById(R.id.left_list);
        rightListView = findViewById(R.id.right_list);

        leftListView.setLayoutManager(leftManager);
        rightListView.setLayoutManager(rightManager);

        leftListView.setAdapter(leftAdapter);
        rightListView.setAdapter(rightAdapter);

        leftAdapter.setiItem(new LeftAdapter.IItem() {
            @Override
            public void setItem(int position) {
                Log.d(TAG, "setItem: "+position);

            }
        });

        rightListView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(@NonNull RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
            }

            @Override
            public void onScrolled(@NonNull RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                //获取右边列表里的第一项的tag
                int first = rightManager.findFirstVisibleItemPosition();
                Log.d(TAG, "onScrollStateChanged: " + rightAdapter.getTag(first));
                leftAdapter.setmTag(rightAdapter.getTag(first));
                leftAdapter.notifyDataSetChanged();

                //左边列表相同tag滑倒最上面
                LinearSmoothScroller ls = new LeftAdapter.TopSmoothScroller(getApplicationContext());
                ls.setTargetPosition(leftAdapter.getmPosition());
                leftManager.startSmoothScroll(ls);

            }
        });

    }

    private void configRecyclerView() {

    }
}