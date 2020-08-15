package com.example.tmpdemo;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.PaintFlagsDrawFilter;
import android.graphics.Path;
import android.util.AttributeSet;
import android.view.View;

import androidx.annotation.Nullable;

public class TmpView extends View {
    public TmpView(Context context) {
        this(context, null);
    }

    public TmpView(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public TmpView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    private Paint paint;
    private Path path;
    private PaintFlagsDrawFilter paintFlagsDrawFilter;

    private void init() {
        paint = new Paint();
        paint.setColor(Color.RED);
        paint.setStrokeWidth(5);

        path = new Path();
        float x = 400;
        float y = 400;
        path.moveTo(x, y);
//        path.cubicTo(50, 200, 50, 400, 200, 400);
//        path.quadTo(300, 400, 350, 300);
//        path.moveTo(200,200);
//        path.quadTo(300, 200, 350, 300);

        path.quadTo(x-60, y - 80, x - 100, y - 80);
        path.moveTo(x, y);
        path.quadTo(x-60, y + 80, x - 100, y + 80);
        path.cubicTo(x - 200, y + 80, x - 200, y - 80, x - 100, y - 80);
        //抗锯齿设置
        paintFlagsDrawFilter = new PaintFlagsDrawFilter(0, Paint.FILTER_BITMAP_FLAG | Paint.ANTI_ALIAS_FLAG);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        path.moveTo(0, 0);

        //抗锯齿
        canvas.setDrawFilter(paintFlagsDrawFilter);

        canvas.drawPath(path, paint);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        setMeasuredDimension(getMeasuredWidth(), getMeasuredWidth());
    }
}
