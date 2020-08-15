package com.example.cardviewdemo;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.PaintFlagsDrawFilter;
import android.graphics.Path;
import android.graphics.Rect;
import android.graphics.RectF;
import android.graphics.Typeface;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;

import androidx.annotation.Nullable;
import androidx.core.view.MotionEventCompat;

import static android.content.ContentValues.TAG;

public class IndexView extends View {

    private static final String[] LETTERS = new String[]{"A", "B", "C", "D",
            "E", "F", "G", "H", "J", "K", "L", "M", "N", "P", "R", "S", "T",
            "W", "X", "Y", "Z"
    };
    //画笔
    private Paint mPaint;
    //每个字母的宽高
    private float cellWidth;
    private float cellHeight;

    //字母所在的y坐标
    private float cellY;

    private float mWidth = 50;
    private float mHeight;
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
        this(context, null);
    }

    public IndexView(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public IndexView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initPaint();
    }

    private void initPaint() {
        mPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        mPaint.setColor(Color.GRAY);
        mPaint.setTypeface(Typeface.DEFAULT_BOLD);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        drawText(canvas);
    }

    private void drawText(Canvas canvas) {
        canvas.setDrawFilter(new PaintFlagsDrawFilter(0, Paint.ANTI_ALIAS_FLAG | Paint.FILTER_BITMAP_FLAG));

        for (int i = 0; i < LETTERS.length; i++) {
            String text = LETTERS[i];
            //设置字体大小为cellHeight的三分之二大小
            mPaint.setTextSize(cellHeight * 2 / 3);
            // 计算坐标
            int x = getWidth() - (int) mPaint.getTextSize();
            // 获取文本高度
            Rect bounds = new Rect();
            mPaint.getTextBounds(text, 0, text.length(), bounds);
            int textHeight = bounds.height();
            int y = (int) (cellHeight / 2.0f + textHeight / 2.0f + i * cellHeight + mHeight * 0.1F);

            // 根据按下的字母, 设置画笔颜色，onTouch里获取touchIndex，默认-1不会引起任何字母的改变
            if (touchIndex == i) {
                setmWidth(35);
                setCellY(bounds.centerY() + y);
                mPaint.setColor(Color.BLACK);
                canvas.drawText(text, x, y, mPaint);
                drawPop(canvas, i, mPaint.measureText(text));
            } else {
                mPaint.setColor(Color.GRAY);
                canvas.drawText(text, x, y, mPaint);
            }
        }
    }

    private void drawPop(Canvas canvas, int i, float v) {
        float popX = getWidth() - mPaint.getTextSize();
        float popY = getCellY();

        Path path = new Path();
        path.moveTo(popX, popY);
        path.quadTo(popX - 30, popY - 40, popX - 50, popY - 40);
        path.moveTo(popX, popY);
        path.quadTo(popX - 30, popY + 40, popX - 50, popY + 40);
        path.cubicTo(popX - 100, popY + 40, popX - 100, popY - 40, popX - 50, popY - 40);

        RectF rect = new RectF(popX - 100, popY - 40, popX - 30, popY + 40);

        mPaint.setColor(Color.LTGRAY);
        canvas.drawPath(path, mPaint);
        mPaint.setColor(Color.WHITE);
        mPaint.setTextSize(DensityUtil.sp2px(18));
        float textX = popX - 50 - mPaint.measureText(LETTERS[i]) / 2f;
        float textY = rect.centerY() - (mPaint.getFontMetricsInt().bottom + mPaint.getFontMetricsInt().top) / 2f;
        canvas.drawText(LETTERS[i], textX, textY, mPaint);
    }

    int touchIndex = -1;

    //获取自定义view在布局中的长宽
    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        mHeight = (float) (getMeasuredHeight() * 0.7);
        int rHeight = (int) (getMeasuredHeight() * 0.8f);
        setMeasuredDimension((int) getmWidth(), rHeight);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        int index;
        switch (event.getAction()) {
            //点击和移动的时候获取y坐标
            case MotionEvent.ACTION_DOWN:
            case MotionEvent.ACTION_MOVE:
                //计算点击的y坐标在哪个字母上
                index = (int) ((event.getY() - mHeight * 0.1f) / cellHeight);
                if (index >= 0 && index < LETTERS.length) {
                    if (index != touchIndex) {
                        mOnLetterUpdateListener.letterUpdate(LETTERS[index]);
                    }
                    //赋值给touchindex，在绘制过程中检测这个值，相应的字母会有区别
                    touchIndex = index;
                }
                break;
            case MotionEvent.ACTION_UP:
                //手指抬起将不会有任何字母有区别
                touchIndex = -1;
                setmWidth(mPaint.getTextSize());
                requestLayout();
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
        cellWidth = 35;
        cellHeight = mHeight / LETTERS.length;
    }

    private float getmWidth() {
        return mWidth;
    }

    public void setmWidth(float mWidth) {
        this.mWidth = mWidth;
    }

    public float getCellY() {
        return cellY;
    }

    private void setCellY(float cellY) {
        this.cellY = cellY;
    }
}

