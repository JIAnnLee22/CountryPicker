package com.example.cardviewdemo;

import android.annotation.SuppressLint;
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
import android.widget.LinearLayout;
import android.widget.SearchView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.LinearSmoothScroller;
import androidx.recyclerview.widget.RecyclerView;

import com.google.gson.reflect.TypeToken;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.lang.reflect.Type;
import java.text.Collator;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Locale;

public class CountryActivity extends AppCompatActivity {

    private static final String TAG = "CountryActivity";
    private List<CountryBean> countryLsit;
    private List<CountryBean> tmpList;
    private CountryAdapter countryAdapter;
    private RecyclerView recyclerView;
    private IndexView indexView;
    private EditText searchView;
    private LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
    //自动获取汉子的首字母
    static final int GB_SP_DIFF = 160;
    // 存放国标一级汉字不同读音的起始区位码
    static final int[] secPosValueList = {1601, 1637, 1833, 2078, 2274, 2302,
            2433, 2594, 2787, 3106, 3212, 3472, 3635, 3722, 3730, 3858, 4027,
            4086, 4390, 4558, 4684, 4925, 5249, 5600};
    // 存放国标一级汉字不同读音的起始区位码对应读音
    static final char[] firstLetter = {'A', 'B', 'C', 'D', 'E', 'F', 'G', 'H',
            'J', 'K', 'L', 'M', 'N', 'O', 'P', 'Q', 'R', 'S', 'T', 'W', 'X',
            'Y', 'Z'};

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
                        //获取第一个国家首字母为所点击字母的下标
                        LinearSmoothScroller ls = new CountryAdapter.TopSmoothScroller(getApplicationContext());
                        ls.setTargetPosition(tmpList.indexOf(str));
                        linearLayoutManager.startSmoothScroll(ls);
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
                    if (imm != null) {
                        assert v != null;
                        //隐藏键盘
                        imm.hideSoftInputFromWindow(v.getWindowToken(), 0);
                        //取消输入栏的焦点
                        v.clearFocus();
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
    }

    private void initSearchView() {
        searchView.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }

            @Override
            public void afterTextChanged(Editable editable) {
                //Log.d(TAG, "afterTextChanged: 输入了文字" + editable);
                findCountry(editable.toString());
            }
        });
    }

    //将国籍代码按照拼音字母排序
    private List<CountryBean> countryComp() {
        Type listType = new TypeToken<List<CountryBean>>() {
        }.getType();
        countryLsit = GsonUtils.fromJson(getJson(), listType);
        final Comparator<Object> comparator = Collator.getInstance(Locale.CHINA);
        Collections.sort(countryLsit, new Comparator<CountryBean>() {
            @Override
            public int compare(CountryBean countryBean, CountryBean t1) {
                return comparator.compare(countryBean.getZh(), t1.getZh());
            }
        });
//        for (CountryBean c:countryLsit){
//            Log.d(TAG, "countryComp: "+getSpells(c.getZh()));
//        }
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

    //根据中文查找国家
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

    public static String getSpells(String characters) {
        StringBuffer buffer = new StringBuffer();
        for (int i = 0; i < characters.length(); i++) {

            char ch = characters.charAt(i);
            if ((ch >> 7) == 0) {
                // 判断是否为汉字，如果左移7为为0就不是汉字，否则是汉字
            } else {
                char spell = getFirstLetter(ch);
                buffer.append(String.valueOf(spell));
            }
        }
        return buffer.toString();
    }

    // 获取一个汉字的首字母
    public static Character getFirstLetter(char ch) {

        byte[] uniCode = null;
        try {
            uniCode = String.valueOf(ch).getBytes("GBK");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
            return null;
        }
        if (uniCode[0] < 128 && uniCode[0] > 0) { // 非汉字
            return null;
        } else {
            return convert(uniCode);
        }
    }

    /**
     * 获取一个汉字的拼音首字母。 GB码两个字节分别减去160，转换成10进制码组合就可以得到区位码
     * 例如汉字“你”的GB码是0xC4/0xE3，分别减去0xA0（160）就是0x24/0x43
     * 0x24转成10进制就是36，0x43是67，那么它的区位码就是3667，在对照表中读音为‘n’
     */
    static char convert(byte[] bytes) {
        char result = '-';
        int secPosValue = 0;
        int i;
        for (i = 0; i < bytes.length; i++) {
            bytes[i] -= GB_SP_DIFF;
        }
        secPosValue = bytes[0] * 100 + bytes[1];
        for (i = 0; i < 23; i++) {
            if (secPosValue >= secPosValueList[i]
                    && secPosValue < secPosValueList[i + 1]) {
                result = firstLetter[i];
                break;
            }
        }
        return result;
    }

}