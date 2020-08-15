package com.example.pro5;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.PaintFlagsDrawFilter;
import android.graphics.Point;
import android.graphics.RectF;
import android.util.AttributeSet;
import android.view.View;

import androidx.annotation.Nullable;

import java.text.DecimalFormat;

public class CircleView extends View {

    private Paint paint;
    private RectF viewSize;
    private RectF rectF;
    private RectF rectFF;
    private RectF rectFNew;
    private RectF rectFOld;

    private Point newAndOld;

    private int newC;
    private int oldC;

    public CircleView(Context context) {
        this(context, null);
    }

    public CircleView(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public CircleView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    @SuppressLint("ResourceAsColor")
    private void init() {
        paint = new Paint();
        paint.setTextSize(DensityUtil.sp2px(14));
    }

    @SuppressLint("DrawAllocation")
    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        viewSize = new RectF(0, 0, getMeasuredWidth(), getMeasuredWidth());
        rectFF = new RectF(viewSize.centerX() - viewSize.width() / 3, 0, viewSize.centerX(), viewSize.width() / 3);
        rectF = new RectF(rectFF.left + rectFF.left * 0.15f, rectFF.left * 0.15f, viewSize.centerX() - rectFF.left * 0.15f, viewSize.width() / 3 - rectFF.left * 0.15f);
        setMeasuredDimension((int) viewSize.width(), (int) rectFF.height());

        rectFNew = new RectF();
        rectFNew.left = viewSize.centerX() + viewSize.width() / 8;
        rectFNew.top = viewSize.width() / 16;
        rectFNew.right = rectFNew.left + 30;
        rectFNew.bottom = rectFNew.top + 30;

        rectFOld = new RectF();
        rectFOld.left = rectFNew.left;
        rectFOld.top = rectFNew.bottom + paint.getTextSize() * 3;
        rectFOld.right = rectFOld.left + 30;
        rectFOld.bottom = rectFOld.top + 30;
    }

    @SuppressLint("DrawAllocation")
    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        canvas.setDrawFilter(new PaintFlagsDrawFilter(0, Paint.ANTI_ALIAS_FLAG | Paint.FILTER_BITMAP_FLAG));
        drawCircle(canvas);
        drawInCircle(canvas);
        drawLable(canvas);
    }

    private void drawCircle(Canvas canvas) {
//        canvas.drawArc();
        float startA = -90;
        float sweepA = 360 * proportion();

        paint.setColor(Color.rgb(234, 234, 234));
        canvas.drawCircle(rectFF.centerX(), rectFF.centerY(), rectFF.width() / 2, paint);
        paint.setColor(Color.rgb(0, 160, 160));
        canvas.drawArc(rectFF, startA, sweepA, true, paint);
//        canvas.drawCircle(rectFF.centerX(), rectFF.centerY(), rectFF.width() / 2, paint);
        paint.setColor(Color.WHITE);
        canvas.drawCircle(rectF.centerX(), rectF.centerY(), rectF.width() / 2, paint);
    }

    private void drawInCircle(Canvas canvas) {

        String text = "全部顾客";
        String all = String.valueOf(getNewC() + getOldC());

        paint.setColor(Color.DKGRAY);
        canvas.drawText(text, rectF.centerX() - paint.measureText(text) / 2, rectF.centerY(), paint);
        canvas.drawText(all, rectF.centerX() - paint.measureText(all) / 2, rectF.centerY() + paint.getTextSize(), paint);
    }

    private void drawLable(Canvas canvas) {
        paint.setStrokeWidth(10);
        paint.setColor(Color.rgb(0, 160, 160));
        canvas.drawCircle(rectFNew.centerX(), rectFNew.centerY(), rectFNew.width() / 2, paint);
        paint.setColor(Color.rgb(234, 234, 234));
        canvas.drawCircle(rectFOld.centerX(), rectFOld.centerY(), rectFOld.width() / 2, paint);

        paint.setColor(Color.BLACK);
        canvas.drawText("新客" + "\t" + getNewC() + "人", rectFNew.right, getBaseLine(paint, rectFNew.centerY()), paint);
        canvas.drawText("老客" + "\t" + getOldC() + "人", rectFOld.right, getBaseLine(paint, rectFOld.centerY()), paint);
        DecimalFormat dataFormat = new DecimalFormat("0.0");
        String newP = "占比" + dataFormat.format(proportion() * 100) + "%";
        String oldP = "占比" + dataFormat.format((1 - proportion()) * 100) + "%";
        paint.setColor(Color.GRAY);
        paint.setTextSize(DensityUtil.sp2px(12));
        canvas.drawText(newP, rectFNew.right, rectFNew.bottom + paint.getTextSize() * 1.5f, paint);
        canvas.drawText(oldP, rectFOld.right, rectFOld.bottom + paint.getTextSize() * 1.5f, paint);
    }

    //获取文字的基线
    private float getBaseLine(Paint paint, float centerY) {
        return centerY - (paint.getFontMetricsInt().bottom + paint.getFontMetricsInt().top) / 2;
    }

    private float proportion() {
        int account;
        account = getNewC() + getOldC();
        return (float) getNewC() / account;
    }

    public int getOldC() {
        return oldC;
    }

    public void setOldC(int oldC) {
        this.oldC = oldC;
    }

    public int getNewC() {
        return newC;
    }

    public void setNewC(int newC) {
        this.newC = newC;
    }
}
