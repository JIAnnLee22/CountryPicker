package com.example.mydemo.customView;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.Point;
import android.graphics.RectF;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;

import androidx.annotation.Nullable;

import java.util.ArrayList;
import java.util.List;

import static android.content.ContentValues.TAG;

public class PieChartView extends View {

    private static final float PI = 3.1415f;
    private List<Integer> colors = new ArrayList<>();
    private List<Float> list = new ArrayList<>();
    private List<String> lableList = new ArrayList<>();
    private float account = 0;

    private Paint paint;
    private Paint polyPaint;
    private float startAngle = -90;
    private RectF mRectF;
    private RectF arcRectF;

    private int num;


    public PieChartView(Context context) {
        super(context, null);
    }

    public PieChartView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs, 0);
    }

    public PieChartView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initData();
        init();
    }

    private void initData() {
        //设置模拟数据
        list.add(311f);
        list.add(534f);
        list.add(205f);
        list.add(350f);
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
        polyPaint = new Paint();

        polyPaint.setStyle(Paint.Style.STROKE);
        paint.setColor(Color.RED);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        for (int i = 0; i < list.size(); i++) {
            setNum(i);
            paint.setColor(colors.get(i));
            drawArc(canvas, list.get(i));
            drawRoundRect(canvas, i);
        }
        drawCenterCirCle(canvas);
    }

    //画扇形
    private void drawArc(Canvas canvas, float i) {
        paint.setStyle(Paint.Style.FILL);
        Log.d(TAG, "drawArc: " + mRectF.bottom);
        float account = account();
        float sweepAngle = 360 * i / account;
        canvas.drawArc(arcRectF, startAngle, sweepAngle, true, paint);
        drawPolyline(canvas, startAngle + sweepAngle / 2);
        startAngle = startAngle + sweepAngle;
    }

    //画中间的圆形
    private void drawCenterCirCle(Canvas canvas) {
        paint.setStyle(Paint.Style.FILL);
        paint.setColor(Color.WHITE);
        canvas.drawCircle(arcRectF.centerX(), arcRectF.centerY(), arcRectF.width() * 0.3f, paint);
    }

    //画圆角矩形与标签
    private void drawRoundRect(Canvas canvas, int i) {


        RectF rectF = new RectF();
        switch (i) {
            case 0:
                rectF.left = mRectF.width() / 12;
                rectF.top = arcRectF.bottom * 13 / 12;
                break;
            case 1:
                rectF.left = mRectF.width() * 5 / 12;
                rectF.top = arcRectF.bottom * 13 / 12;
                break;
            case 2:
                rectF.left = mRectF.width() * 9 / 12;
                rectF.top = arcRectF.bottom * 13 / 12;
                break;
            case 3:
                rectF.left = mRectF.width() / 12;
                rectF.top = arcRectF.bottom * 14 / 12;
                break;
            case 4:
                rectF.left = mRectF.width() * 5 / 12;
                rectF.top = arcRectF.bottom * 14 / 12;
                break;
        }

        //有了起始坐标加上宽高就是终点坐标，将圆角矩形设置成32X32
        rectF.right = rectF.left + 32;
        rectF.bottom = rectF.top + 32;

        canvas.drawRoundRect(rectF, 9, 9, paint);

        //改成字体的颜色
        paint.setColor(Color.DKGRAY);
        paint.setStyle(Paint.Style.FILL);
        paint.setTextSize(rectF.bottom - rectF.top);

        //改变颜色之后将标签写到圆角矩形的旁边
        canvas.drawText(lableList.get(i), rectF.right + 16, rectF.bottom, paint);
    }

    //画折线
    private void drawPolyline(Canvas canvas, float centerAngle) {
        paint.setStyle(Paint.Style.STROKE);

        //每个扇形的弧上中点
        float x1 = (float) (arcRectF.centerX() + arcRectF.width() / 2 * Math.cos(centerAngle * PI / 180));
        float y1 = (float) (arcRectF.centerY() + arcRectF.width() / 2 * Math.sin(centerAngle * PI / 180));

        //原点到中点的延长点
        float x2 = (float) (arcRectF.centerX() + (arcRectF.width() * 7 / 13) * Math.cos(centerAngle * PI / 180));
        float y2 = (float) (arcRectF.centerY() + (arcRectF.width() * 7 / 13) * Math.sin(centerAngle * PI / 180));

        //在左边往左移，在右边往右移
        float x3;
        float y3 = y2;
        if (x1 <= arcRectF.centerX()) {
            x3 = x2 - arcRectF.width() / 13;
        } else {
            x3 = x2 + arcRectF.width() / 13;
        }

        //将点连成路径
        Path polyPath = new Path();
        polyPath.moveTo(x1, y1);
        polyPath.lineTo(x2, y2);
        polyPath.lineTo(x3, y3);

        //画出路径
        paint.setStrokeWidth(3);
        canvas.drawPath(polyPath, paint);

        //在路径的终点画点（point）
        paint.setStyle(Paint.Style.FILL);
        canvas.drawCircle(x3, y3, 6, paint);

        //将终点的坐标传到画字的方法
        Point point = new Point((int) x3, (int) y3);
        drawText(canvas, point);
    }

    //画字
    private void drawText(Canvas canvas, Point point) {
        polyPaint.setColor(Color.DKGRAY);
        polyPaint.setTextSize(paint.getTextSize());
        if (point.x < arcRectF.centerX()) {
            point.x -= polyPaint.measureText(lableList.get(getNum())) + 16;
        } else {
            point.x += 16;
        }
        canvas.drawText(lableList.get(getNum()), point.x, point.y, polyPaint);
        canvas.drawText(lableList.get(getNum()).substring(0, 1), point.x, point.y + polyPaint.getTextSize(), polyPaint);
    }

    //计算总数
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
        arcRectF = new RectF(mRectF.width() * 0.25f, mRectF.width() * 0.05f,
                mRectF.width() * 0.75f, mRectF.width() * 0.55f);
        Log.d(TAG, "onMeasure: " + getMeasuredWidth() + "\t" + getMeasuredHeight());
        Log.d(TAG, "onMeasure: " + mRectF.left + "\t" + mRectF.top + "\t" + mRectF.right + "\t" + mRectF.bottom);
        setMeasuredDimension(getMeasuredWidth(), (int) (arcRectF.bottom * 15 / 12));
    }

    //正在绘制的下标
    private int getNum() {
        return num;
    }

    private void setNum(int num) {
        this.num = num;
    }

}
