package com.example.piechartdemo;

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
import android.view.MotionEvent;
import android.view.View;

import androidx.annotation.Nullable;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;

import static android.content.ContentValues.TAG;

class PieChartView extends View {


    private List<Integer> colors = new ArrayList<>();
    private List<Float> list = new ArrayList<>();
    private List<String> lableList = new ArrayList<>();
    private List<Float> startList = new ArrayList<>();
    private float account = 0;
    private static final float PI = 3.1415f;

    private Paint paint;
    private Paint polyPaint;
    private float startAngle = -90;
    private RectF mRectF;
    private RectF arcRectF;
    private RectF arcRectFF;

    private int num;
    private int angleId = -1;

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

        getAngle();

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
        paint.setStyle(Paint.Style.FILL);
        paint.setColor(Color.RED);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        for (int i = 0; i < list.size(); i++) {
            paint.setColor(colors.get(i));
            drawArc(canvas, i);
            drawRoundRect(canvas, i);
        }
        drawCenterCirCle(canvas);
    }

    private void drawArc(Canvas canvas, int i) {
        float sweepAngle = 360 * list.get(i) / account;
        if (i == angleId) {
            canvas.drawArc(arcRectFF, startAngle, sweepAngle, true, paint);
        } else {
            canvas.drawArc(arcRectF, startAngle, sweepAngle, true, paint);
        }
        drawPolyline(canvas, startAngle + sweepAngle / 2);
        startAngle = startAngle + sweepAngle;
    }

    private void drawCenterCirCle(Canvas canvas) {
        paint.setColor(Color.WHITE);
        canvas.drawCircle(arcRectF.centerX(), arcRectF.centerY(), arcRectF.width() * 0.35f, paint);
    }

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
        paint.setColor(Color.DKGRAY);
        paint.setTextSize(rectF.bottom - rectF.top);
        canvas.drawText(lableList.get(i), rectF.right + 16, rectF.bottom, paint);
    }

    //画折线
    private void drawPolyline(Canvas canvas, float centerAngle) {
        paint.setStyle(Paint.Style.STROKE);

        //每个扇形的弧上中点
        float x1 = (float) (arcRectF.centerX() + arcRectF.width() / 2 * Math.cos(centerAngle * PI / 180));
        float y1 = (float) (arcRectF.centerY() + arcRectF.width() / 2 * Math.sin(centerAngle * PI / 180));

        //原点到中点的延长点
        float x2 = (float) (arcRectF.centerX() + (arcRectF.width() * 7 / 12) * Math.cos(centerAngle * PI / 180));
        float y2 = (float) (arcRectF.centerY() + (arcRectF.width() * 7 / 12) * Math.sin(centerAngle * PI / 180));

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
        DecimalFormat dataFormat = new DecimalFormat("0.0");
        //改成字体的颜色
        polyPaint.setColor(Color.DKGRAY);
        polyPaint.setStyle(Paint.Style.FILL);
        polyPaint.setTextSize(32);
        if (point.x < arcRectF.centerX()) {
            point.x -= polyPaint.measureText(lableList.get(getNum())) + 16;
        } else {
            point.x += 16;
        }
        switch (getNum()) {
            case 0:
            case 1:
            case 2:
                canvas.drawText(lableList.get(getNum()), point.x, point.y, polyPaint);
                canvas.drawText(String.valueOf(list.get(getNum())), point.x, point.y + polyPaint.getTextSize(), polyPaint);
                break;
            case 3:
            case 4:
                canvas.drawText(lableList.get(getNum()), point.x, point.y, polyPaint);
                canvas.drawText(dataFormat.format(list.get(getNum()) / account() * 100) + "%"
                        , point.x, point.y + polyPaint.getTextSize(), polyPaint);
                break;
        }
    }

    private void getAngle() {
        float is = 0;
        float startAng = -90;
//        float sweepAng = 0;
        for (int i = 0; i < list.size(); i++) {
            is += list.get(i);
            startAng = 360 * is / account() - 90;
            Log.d(TAG, "getAngle: " + startAng);
            startList.add(startAng);
        }
//        sweepAng = list.get(index) / account();
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
        arcRectF = new RectF(
                mRectF.width() * 0.25f,
                mRectF.width() * 0.25f,
                mRectF.width() * 0.75f,
                mRectF.width() * 0.75f);
        arcRectFF = new RectF(
                mRectF.width() * 0.225f,
                mRectF.width() * 0.225f,
                mRectF.width() * 0.775f,
                mRectF.width() * 0.775f);
        setMeasuredDimension(getMeasuredWidth(), (int) (arcRectF.bottom * 15 / 12));

    }

//    @Override
//    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
//        super.onSizeChanged(w, h, oldw, oldh);
//    }

    @SuppressLint("ClickableViewAccessibility")
    @Override
    public boolean onTouchEvent(MotionEvent event) {
        float x = event.getX() - arcRectF.centerX();
        float y = event.getY() - arcRectF.centerY();

        float xrad = (float) Math.atan2(y, x) / PI * 180;

        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                if (y < 0 && x < 0) {
                    xrad += 360;
                }

                for (float s : startList) {
                    if (xrad < s) {
                        angleId = 0;
                        if (startList.indexOf(s) != 0) {
                            angleId = startList.indexOf(s);
                        }
                        break;
                    }
                }
                invalidate();
                return true;

//            case MotionEvent.ACTION_MOVE:
//                if (y < 0 && x < 0) {
//                    xrad += 360;
//                }
//
//                for (float s : startList) {
//                    if (xrad < s) {
//                        angleId = 0;
//                        if (startList.indexOf(s) != 0) {
//                            angleId = startList.indexOf(s);
//                        }
//                        break;
//                    }
//                }
//                invalidate();
//
//                return true;
            case MotionEvent.ACTION_UP:
                angleId = -1;
                invalidate();
                return true;
        }

        return super.onTouchEvent(event);
    }

    //正在绘制的下标
    private int getNum() {
        return num;
    }

    private void setNum(int num) {
        this.num = num;
    }

}
