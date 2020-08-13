package com.example.myapplication;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;

import androidx.annotation.Nullable;

import static android.content.ContentValues.TAG;

public class MyPreview extends View {

    private int origin = 0;//作为X坐标起点的一个值
    private int end = 0;//作为X坐标终点的一个值
    private int parentWidth;//定义父容器的高
    private int mTop;//获取扫描框的上面一个控件的getbottom（）
    private int mBottom;//获取扫描框的下面一个控件的gettop（）

    private float mScale;//定义取景框的长宽比

    private Paint paintLine;//定义画线的笔
    private Paint paintRect;//定义画矩形的笔
    private Paint eraser = new Paint(Paint.ANTI_ALIAS_FLAG);//定义镂空矩形的画笔
    private Paint paintBitmap;//定义将图片资源画在view上的画笔
    private Paint paintText;//定义写字的笔，护照的遮罩
    private Bitmap mBitmap;//定义头像框图片

    public MyPreview(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        initPaint();
    }

    private void initPaint() {
        //初始化各个画笔
        paintLine = new Paint();
        paintRect = new Paint();
        paintBitmap = new Paint();
        paintText = new Paint();

    }

    @SuppressLint("DrawAllocation")
    @Override
    protected void onDraw(Canvas canvas) {
        //定义父容器的高
        int mWidth = (int) ((getmBottom() - getmTop() - 16) * getmScale());//根据身份证的长宽比算出扫描框的宽
        //y轴的起点和终点分别是getmTop()、getmBottom()
//        origin = (int) ((getParentWidth() - mWidth) * 0.5);//算出起点
        setOrigin((int) ((getParentWidth() - mWidth) * 0.5));
//        end = origin + mWidth;//算出终点
        setEnd(origin + mWidth);
        Log.d(TAG, "onDraw: " + getParentWidth() + "\t" + mWidth + "\n" + getOrigin() + "\t" + end);
        setBackgroundColor(Color.TRANSPARENT);//设置背景为透明，可以看见相机的预览
        paintLine.setColor(Color.GREEN);//画笔的颜色
        paintLine.setStrokeWidth(8);//画笔的宽度？
        paintLine.setAlpha(250);//画笔的透明度

        //画一个大遮罩
        canvas.drawColor(Color.BLACK);

        //镂空一个矩形扫描框
        Rect rect = new Rect(getOrigin(), (int) getmTop(), getEnd(), (int) getmBottom());
        canvas.drawRect(rect, eraser);
        eraser.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.CLEAR));

        //根据view的宽高来描绘框线，这个+8、-8是线宽，这样可以消除两两相交之间的缺口,这些-4+4-8+8是为了将线放在镂空的外面包围
        //左上角两条线
        //线的长度
        int line = 80;
        canvas.drawLine(getOrigin() - 4, getmTop(), getOrigin() - 4, getmTop() + line, paintLine);//横
        canvas.drawLine(getOrigin() - 8, getmTop() - 4, getOrigin() + line, getmTop() - 4, paintLine);//竖
        //左下角两条线
        canvas.drawLine(getOrigin() - 8, getmBottom() + 4, getOrigin() + line, getmBottom() + 4, paintLine);//横
        canvas.drawLine(getOrigin() - 4, getmBottom(), getOrigin() - 4, getmBottom() - line, paintLine);//竖
        //右上角两条线
        canvas.drawLine(getEnd() + 8, getmTop() - 4, getEnd() - line, getmTop() - 4, paintLine);//横
        canvas.drawLine(getEnd() + 4, getmTop(), getEnd() + 4, getmTop() + line, paintLine);//竖
        //右下角两条线
        canvas.drawLine(getEnd() + 8, getmBottom() + 4, getEnd() - line, getmBottom() + 4, paintLine);//横
        canvas.drawLine(getEnd() + 4, getmBottom(), getEnd() + 4, getmBottom() - line, paintLine);//竖

        //如果是护照的比例，画个遮罩在扫描框里，是身份证的比例就画个头像在扫描框里（护照比例1.3多，身份证比例1.58多）
        if (getmScale() < 1.4) {
            paintRect.setAlpha(1);
            paintRect.setColor(Color.rgb(246, 246, 246));
            canvas.drawRect(getOrigin(), getmBottom() - (getmBottom() - getmTop()) / 5, getEnd(), getmBottom(), paintRect);
            paintText.setColor(Color.rgb(211, 211, 211));
            paintText.setTextSize(30);
            //两行字
            String text1 = "POCHN<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<";
            String text2 = "<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<";
            //两行字的基准线
            float scanHeight = (getmBottom() - getmTop()) / 5;
            float textY = getmBottom() - (getmBottom() - getmTop()) / 5;
            //文字宽度上限
            int textWidth = getEnd() - getOrigin() - 64;

            int measuredCount1;
            int measuredCount2;
            float[] measuredWidth = {0};
            // 宽度上限比镂空的矩形宽度少64 （不够用，截断）
            measuredCount1 = paintText.breakText(text1, 0, text1.length(), true, textWidth, measuredWidth);
            measuredCount2 = paintText.breakText(text2, 0, text2.length(), true, textWidth, measuredWidth);
            canvas.drawText(text1, 0, measuredCount1, getOrigin() + 32, textY + scanHeight * 2 / 5, paintText);
            canvas.drawText(text2, 0, measuredCount2, getOrigin() + 32, textY + scanHeight * 4 / 5, paintText);

//            canvas.drawText(text1, origin, textY + scanHeight * 2 / 5, paintText);
//            canvas.drawText(text2, origin, textY + scanHeight * 4 / 5, paintText);
        } else {
            Bitmap bitmap = getBitmap(getContext(), R.drawable.ic_head);
            setmBitmap(bitmap);
            canvas.drawBitmap(getmBitmap(), getEnd() - (getEnd() - getOrigin()) * 3 / 7, getmTop() + (getmBottom() - getmTop()) / 5, paintBitmap);
        }
        super.onDraw(canvas);

    }

    private static Bitmap getBitmap(Context context, int vectorDrawableId) {
        Bitmap bitmap;
        if (Build.VERSION.SDK_INT > Build.VERSION_CODES.LOLLIPOP) {
            Drawable vectorDrawable = context.getDrawable(vectorDrawableId);
            assert vectorDrawable != null;
            bitmap = Bitmap.createBitmap(vectorDrawable.getIntrinsicWidth(),
                    vectorDrawable.getIntrinsicHeight(), Bitmap.Config.ARGB_8888);
            Canvas canvas = new Canvas(bitmap);
            vectorDrawable.setBounds(0, 0, canvas.getWidth(), canvas.getHeight());
            vectorDrawable.draw(canvas);
        } else {
            bitmap = BitmapFactory.decodeResource(context.getResources(), vectorDrawableId);
        }
        return bitmap;
    }

    public Bitmap getmBitmap() {
        return mBitmap;
    }

    //将头形轮廓缩放到扫描框的2/5的长宽
    public void setmBitmap(Bitmap mBitmap) {
        Bitmap bitmap = Bitmap.createScaledBitmap(mBitmap, (getEnd() - getOrigin()) / 5 * 2, (getEnd() - getOrigin()) / 5 * 2, true);
        this.mBitmap = bitmap;
    }

    public int getOrigin() {
        return origin;
    }

    private void setOrigin(int origin) {
        this.origin = origin;
    }

    public int getEnd() {
        return end;
    }

    private void setEnd(int end) {
        this.end = end;
    }

    public float getmBottom() {
        return mBottom;
    }

    public void setmBottom(int mBottom) {
        this.mBottom = mBottom;
    }

    public float getmTop() {
        return mTop;
    }

    public void setmTop(int mTop) {
        this.mTop = mTop;
    }

    public void setParentWidth(int parentWidth) {
        this.parentWidth = parentWidth;
    }

    private float getParentWidth() {
        return parentWidth;
    }

    private float getmScale() {
        return mScale;
    }

    public void setmScale(float mScale) {
        this.mScale = mScale;
    }

}
