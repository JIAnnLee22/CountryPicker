package com.example.cardviewdemo;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.Typeface;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;

import androidx.annotation.Nullable;
import androidx.core.view.MotionEventCompat;

import static android.content.ContentValues.TAG;

class IndexView extends View {

    private static final String[] LETTERS = new String[]{"A", "B", "C", "D",
            "E", "F", "G", "H", "I", "J", "K", "L", "M", "N", "O", "P", "Q",
            "R", "S", "T", "U", "V", "W", "X", "Y", "Z", "#"
    };
    //画笔
    private Paint mPaint;
    //每个字母的宽高
    private float cellWidth;
    private float cellHeight;
    //回调接口
    private OnLetterUpdateListener mOnLetterUpdateListener;

    //暴露一个字母的监听
    public interface OnLetterUpdateListener {
        void letterUpdate(String letter);
    }

    public OnLetterUpdateListener getOnLetterUpdateListener() {
        return mOnLetterUpdateListener;
    }

    //设置字母监听更新
    public void setOnLetterUpdateListener(OnLetterUpdateListener mOnLetterUpdateListener) {
        this.mOnLetterUpdateListener = mOnLetterUpdateListener;
    }

    public IndexView(Context context) {
        super(context, null);
    }

    public IndexView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs, 0);
    }

    public IndexView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
//        initPaint();
    }

//    private void initPaint() {
//        mPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
//        mPaint.setColor(Color.GRAY);
//        mPaint.setTypeface(Typeface.DEFAULT_BOLD);
//    }

    @Override
    protected void onDraw(Canvas canvas) {
        mPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        mPaint.setColor(Color.GRAY);
        mPaint.setTypeface(Typeface.DEFAULT_BOLD);

        for (int i = 0; i < LETTERS.length; i++) {
            String text = LETTERS[i];

            // 计算坐标
            int x = (int) (cellWidth / 2.0f - mPaint.measureText(text) / 2.0f);
            // 获取文本高度
            Rect bounds = new Rect();
            //设置字体大小为cellHeight的三分之二大小
            mPaint.setTextSize(cellHeight/1.5f);
            mPaint.getTextBounds(text, 0, text.length(), bounds);
            int textHeight = bounds.height();
            int y = (int) (cellHeight / 2.0f + textHeight / 2.0f + i
                    * cellHeight);

            // 根据按下的字母, 设置画笔颜色
            mPaint.setColor(touchIndex == i ? Color.BLACK : Color.GRAY);
            canvas.drawText(text, x, y, mPaint);
        }

//        super.onDraw(canvas);

    }

    int touchIndex = -1;

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        int index = -1;
        switch (MotionEventCompat.getActionMasked(event)) {
            case MotionEvent.ACTION_DOWN:
                index = (int) (event.getY() / cellHeight);
                if (index >= 0 && index < LETTERS.length) {
                    if (index != touchIndex) {
                        mOnLetterUpdateListener.letterUpdate(LETTERS[index]);
                    }
                    touchIndex = index;
                }
                break;
            case MotionEvent.ACTION_UP:
                index = -1;
                touchIndex = -1;
                break;
            case MotionEvent.ACTION_MOVE:
                index = (int) (event.getY() / cellHeight);
                if (index >= 0 && index < LETTERS.length) {
                    if (index != touchIndex) {
                        mOnLetterUpdateListener.letterUpdate(LETTERS[index]);
                        Log.d("TAG", LETTERS[index]);
                    }
                    touchIndex = index;
                }
                break;
            default:
                break;
        }
        invalidate();

        return true;
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        cellWidth = getMeasuredWidth();
        int mHeight = getMeasuredHeight();
        cellHeight = mHeight * 1.0f / LETTERS.length;
    }
}

