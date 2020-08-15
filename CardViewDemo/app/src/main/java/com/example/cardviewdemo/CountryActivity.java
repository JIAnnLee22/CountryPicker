package com.example.cardviewdemo;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.Context;
import android.content.res.AssetManager;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.LinearSmoothScroller;
import androidx.recyclerview.widget.RecyclerView;

import com.google.gson.reflect.TypeToken;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.lang.reflect.Type;
import java.text.Collator;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Locale;

import static com.example.cardviewdemo.PinyinUtil.getSpells;

public class CountryActivity extends AppCompatActivity {

    private static final String TAG = "CountryActivity";
    private List<CountryBean> countryLsit;
    private List<CountryBean> tmpList;
    private CountryAdapter countryAdapter;
    private RecyclerView recyclerView;
    private IndexView indexView;
    private EditText searchView;
    private LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_country);
    }

    @Override
    protected void onResume() {
        super.onResume();
        initView();
    }

    private void initView() {
        indexView = findViewById(R.id.indexView);
        recyclerView = findViewById(R.id.country_list);
        searchView = findViewById(R.id.country_search);

        //初始化三个控件
        initIndexView();
        initRecyclerView();
        initSearchView();
    }

    private void initIndexView() {
        indexView.setOnLetterUpdateListener(new IndexView.OnLetterUpdateListener() {
            @Override
            public void letterUpdate(String letter) {
                for (CountryBean str : tmpList) {
                    //获取第一个大写字母
                    if (getSpells(str.getZh()).substring(0, 1).equals(letter)) {

                        indexView.setmWidth(140);

                        //声明重写的滚动规则
                        LinearSmoothScroller ls = new CountryAdapter.TopSmoothScroller(getApplicationContext());
                        //传入第一个国家首字母为所点击字母的下标
                        ls.setTargetPosition(tmpList.indexOf(str));
                        linearLayoutManager.startSmoothScroll(ls);
                        indexView.requestLayout();
                        break;
                    }
                }
            }
        });
    }

    @SuppressLint("WrongConstant")
    private void initRecyclerView() {
        //获取数据
        tmpList = countryComp();
        //传入数据
        countryAdapter = new CountryAdapter(tmpList);
        //设置recyclerview
        recyclerView.setLayoutManager(linearLayoutManager);
        recyclerView.setNestedScrollingEnabled(true);
        recyclerView.setAdapter(countryAdapter);
        //列表点击触摸事件
        recyclerView.addOnItemTouchListener(new RecyclerView.OnItemTouchListener() {
            @Override
            public boolean onInterceptTouchEvent(@NonNull RecyclerView rv, @NonNull MotionEvent e) {
                //当点击recyclerview，收起键盘
                if (e.getAction() == MotionEvent.ACTION_DOWN) {
                    //获取焦点所在的view
                    View v = getCurrentFocus();
                    InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                    if (imm != null && v != null) {
                        //隐藏键盘
                        imm.hideSoftInputFromWindow(v.getWindowToken(), 0);
                        //取消输入栏的焦点
                        v.clearFocus();
                        //没有在搜索，显示首字母的布局
//                        countryAdapter.setSearch(false);
                        if (tmpList.size() == countryComp().size()) {
                            countryAdapter.notifyDataSetChanged();
                        }
                    }
                }
                return false;
            }

            @Override
            public void onTouchEvent(@NonNull RecyclerView rv, @NonNull MotionEvent e) {
            }

            @Override
            public void onRequestDisallowInterceptTouchEvent(boolean disallowIntercept) {

            }
        });
        //实现item点击监听，点击返回的是所点击item的国家名称
        countryAdapter.setOnCountryItemClickListener(new CountryAdapter.OnCountryItemClickListener() {
            @Override
            public void itemClick(String country) {
                //country的值就是国家名称
                Toast.makeText(getApplicationContext(), country, Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void initSearchView() {
        searchView.addTextChangedListener(new TextWatcher() {
            //输入框输入监听
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }

            @Override
            public void afterTextChanged(Editable editable) {
                //检测输入文字的数量，如果没有字就还是显示首字母
                if (editable.length() > 0) {
                    countryAdapter.setSearch(true);
                    //输入的时候调用查找含关键字的项
                } else {
                    //没有输入文字就当作没有在搜索
                    countryAdapter.setSearch(false);
//                    countryAdapter.notifyDataSetChanged();
                }
                findCountry(editable.toString());
            }
        });
    }

    //将国籍代码按照拼音字母排序
    private List<CountryBean> countryComp() {
        //list的类型
        Type listType = new TypeToken<List<CountryBean>>() {
        }.getType();
        //实例化CountryBean并赋值给countryList
        countryLsit = GsonUtils.fromJson(getJson(), listType);
        //设置排列的项目是中文，完成排列后返回的是按照首字母排序好的列表
        final Comparator<Object> comparator = Collator.getInstance(Locale.CHINA);
        Collections.sort(countryLsit, new Comparator<CountryBean>() {
            @Override
            public int compare(CountryBean countryBean, CountryBean t1) {
                return comparator.compare(countryBean.getZh(), t1.getZh());
            }
        });
        return countryLsit;
    }

    //获取放在assets里的json文件
    private String getJson() {
        //将json数据变成字符串
        StringBuilder stringBuilder = new StringBuilder();
        String fileName = "countryCode.json";
        try {
            //获取assets资源管理器
            AssetManager assetManager = this.getAssets();
            //通过管理器打开文件并读取
            BufferedReader bf = new BufferedReader(new InputStreamReader(
                    assetManager.open(fileName)));
            String line;
            while ((line = bf.readLine()) != null) {
                stringBuilder.append(line);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return stringBuilder.toString();
    }

    //根据关键字查找国家
    private void findCountry(String coutryName) {
        //先清空list数据
        tmpList.clear();
        if (coutryName.equals("")) {
            tmpList.addAll(countryComp());
        } else {
            for (CountryBean str : countryComp()) {
                //中文中是否包含关键词，包含添加到list中
                if (str.getZh().contains(coutryName) || str.getEn().contains(coutryName) || str.getLocale().contains(coutryName)) {
                    tmpList.add(str);
                }
            }
        }
        countryAdapter.notifyDataSetChanged();
    }

}