package com.example.customviewdemo.custom;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.Region;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;

import androidx.annotation.Nullable;

import static android.content.ContentValues.TAG;

public class ScanningView extends View {

    private int line = 80;//线的长度
    private int origin = 0;//作为X坐标起点的一个值
    private int end = 0;//作为X坐标终点的一个值
    private int parentWidth;//定义父容器的高
    private int mWidth;//定义父容器的高
    private int mTop;
    private int mBottom;
    private Paint paintLine;//定义画线的笔
    private Paint paintAll;//定义话背景的笔
    private Context mContext;

    public ScanningView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        mContext = context;
    }


    @SuppressLint("DrawAllocation")
    @Override
    protected void onDraw(Canvas canvas) {

//        setmTop(0);
//        setmBottom(getLayoutParams().height);
        paintLine = new Paint();
//        parentWidth = getLayoutParams().width;//获取父容器的宽度
        mWidth = (int) ((getmBottom() - getmTop()) * (85.6 / 54));//根据身份证的长宽比算出扫描框的宽
        //y轴的起点和终点分别是getmTop()、getmBottom()
        origin = (int) ((getParentWidth() - mWidth) * 0.5);//算出起点
        end = origin + mWidth;//算出终点
        Log.d(TAG, "onDraw: " + getParentWidth() + "\t" + mWidth + "\n" + origin + "\t" + end);
        setBackgroundColor(Color.TRANSPARENT);
        paintLine.setColor(Color.GREEN);//画笔的颜色
        paintLine.setStrokeWidth(8);//画笔的宽度？
        paintLine.setAlpha(250);//画笔的透明度

        //涂抹整个view
        paintAll = new Paint();
        //涂抹左边部分
        canvas.drawRect(0, 0, origin, getBottom(), paintAll);
        //右边
        canvas.drawRect(origin+ mWidth, 0, getRight(), getBottom(), paintAll);
        //上面
        canvas.drawRect(origin, 0, origin + mWidth, getmTop(), paintAll);
        //下面
        canvas.drawRect(origin, getmBottom(), origin + mWidth, getBottom(), paintAll);

        //根据view的宽高来描绘框线
        //左上角两条线
        canvas.drawLine(origin, getmTop(), origin + line, getmTop(), paintLine);
        canvas.drawLine(origin, getmTop(), origin, getmTop() + line, paintLine);
        //左下角两条线
        canvas.drawLine(origin, getmBottom(), origin + line, getmBottom(), paintLine);
        canvas.drawLine(origin, getmBottom(), origin, getmBottom() - line, paintLine);
        //右上角两条线
        canvas.drawLine(end, getmTop(), end - line, getmTop(), paintLine);
        canvas.drawLine(end, getmTop(), end, getmTop() + line, paintLine);
        //右下角两条线
        canvas.drawLine(end, getmBottom(), end - line, getmBottom(), paintLine);
        canvas.drawLine(end, getmBottom(), end, getmBottom() - line, paintLine);

        super.onDraw(canvas);

    }

    private float getmBottom() {
        return mBottom;
//        return px2dp(mBottom);
    }

    public void setmBottom(int mBottom) {
        this.mBottom = mBottom;
    }

    private float getmTop() {
        return mTop;
//        return px2dp(mTop);
    }

    public void setmTop(int mTop) {
        this.mTop = mTop;
    }

    public void setParentWidth(int parentWidth) {
        this.parentWidth = parentWidth;
    }

    private float getParentWidth() {
        return parentWidth;
//        return px2dp(parentWidth);
    }

    private int px2dp(float px) {
        final float scale = mContext.getResources().getDisplayMetrics().density;
        Log.d(TAG, "px2dp: " + mContext.getResources().getDisplayMetrics().density);
        return (int) (px / scale + 0.5f);
    }

}
