package com.example.piechartdemo;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.RectF;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;

import androidx.annotation.Nullable;

import java.util.ArrayList;
import java.util.List;

import static android.content.ContentValues.TAG;

class PieChartView extends View {


    private List<Integer> colors = new ArrayList<>();
    private List<Float> list = new ArrayList<>();
    private List<String> lableList = new ArrayList<>();
    private float account = 0;

    private Paint paint;
    private float startAngle = -90;
    private RectF mRectF;

    public PieChartView(Context context) {
        this(context, null);
    }

    public PieChartView(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public PieChartView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
        initData();
    }

    private void initData() {
        //设置模拟数据
        list.add(100f);
        list.add(200f);
        list.add(200f);
        list.add(300f);
        list.add(300f);

        account = account();

        colors.add(Color.rgb(244, 102, 101));
        colors.add(Color.rgb(0, 214, 242));
        colors.add(Color.rgb(255, 232, 64));
        colors.add(Color.rgb(48, 138, 226));
        colors.add(Color.rgb(28, 65, 110));

        lableList.add("营业额");
        lableList.add("有效订单");
        lableList.add("曝光客户");
        lableList.add("进店转化率");
        lableList.add("下单转化率");

    }

    private void init() {
        paint = new Paint();
        paint.setStyle(Paint.Style.FILL);
        paint.setColor(Color.RED);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        for (int i = 0; i < list.size(); i++) {
            paint.setColor(colors.get(i));
            drawArc(canvas, list.get(i));
            drawRoundRect(canvas, i);
        }
        drawCenterCirCle(canvas);
    }

    private void drawArc(Canvas canvas, float i) {
        Log.d(TAG, "drawArc: " + mRectF.bottom);
        float account = account();
        float sweepAngle = 360 * i / account;
        canvas.drawArc(mRectF, startAngle, sweepAngle, true, paint);
        startAngle = startAngle + sweepAngle;
    }

    private void drawCenterCirCle(Canvas canvas) {
        paint.setColor(Color.WHITE);
        canvas.drawCircle(mRectF.centerX(), mRectF.centerY(), mRectF.width() * 0.35f, paint);
    }

    private void drawRoundRect(Canvas canvas, int i) {
        RectF rectF = new RectF();
//        RectF rectF = new RectF(100, mCenterX, 150, mCenterX + 50);
        switch (i) {
            case 0:
                rectF.left = 32;
                rectF.top = mRectF.bottom + 16;
                break;
            case 1:
                rectF.left = mRectF.width() / 3 + 32;
                rectF.top = mRectF.bottom + 16;
                break;
            case 2:
                rectF.left = mRectF.width() * 2 / 3 + 32;
                rectF.top = mRectF.bottom + 16;
                break;
            case 3:
                rectF.left = 32;
                rectF.top = mRectF.bottom + 64;
                break;
            case 4:
                rectF.left = mRectF.width() / 3 + 32;
                rectF.top = mRectF.bottom + 64;
                break;
        }
        //有了起始坐标加上宽高就是终点坐标，将圆角矩形设置成32X32
        rectF.right = rectF.left + 32;
        rectF.bottom = rectF.top + 32;
        canvas.drawRoundRect(rectF, 9, 9, paint);
        paint.setColor(Color.DKGRAY);
        paint.setTextSize(rectF.bottom - rectF.top);
        canvas.drawText(lableList.get(i), rectF.right + 16, rectF.bottom, paint);

    }

    private float account() {
        float account = 0;
        for (float i : list) {
            account = account + i;
        }
        return account;
    }

    @SuppressLint("DrawAllocation")
    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        mRectF = new RectF(0, 0, getMeasuredWidth(), getMeasuredWidth());
        Log.d(TAG, "onMeasure: " + getMeasuredWidth() + "\t" + getMeasuredHeight());
        Log.d(TAG, "onMeasure: " + mRectF.left + "\t" + mRectF.top + "\t" + mRectF.right + "\t" + mRectF.bottom);
    }
}
