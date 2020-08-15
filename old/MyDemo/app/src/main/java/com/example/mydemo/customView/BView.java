package com.example.mydemo.customView;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;

import androidx.annotation.Nullable;

import com.example.mydemo.R;

import static android.content.ContentValues.TAG;

public class BView extends View {

    private Paint paint;
    private Bitmap bitmap;

    public BView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    @SuppressLint("DrawAllocation")
    @Override
    protected void onDraw(Canvas canvas) {

        paint = new Paint();
        bitmap = BitmapFactory.decodeResource(getResources(), R.drawable.ic_head);
        Log.d(TAG, "onDraw: "+bitmap);
        if (getBitmap(getContext(),R.drawable.ic_head) != null) {
            canvas.drawBitmap(getBitmap(getContext(),R.drawable.ic_head), 0, 0, paint);
        }

        Rect rect = new Rect(100,100,200,200);
        paint.setColor(Color.BLUE);
        canvas.drawCircle(100,100,10,paint);
        paint.setTextSize(100);
        canvas.drawText("xxxxxxx",100,100,paint);
        super.onDraw(canvas);
    }

    private static Bitmap getBitmap(Context context,int vectorDrawableId) {
        Bitmap bitmap=null;
        if (Build.VERSION.SDK_INT>Build.VERSION_CODES.LOLLIPOP){
            Drawable vectorDrawable = context.getDrawable(vectorDrawableId);
            assert vectorDrawable != null;
            bitmap = Bitmap.createBitmap(vectorDrawable.getIntrinsicWidth(),
                    vectorDrawable.getIntrinsicHeight(), Bitmap.Config.ARGB_8888);
            Canvas canvas = new Canvas(bitmap);
            vectorDrawable.setBounds(0, 0, canvas.getWidth(), canvas.getHeight());
            vectorDrawable.draw(canvas);
        }else {
            bitmap = BitmapFactory.decodeResource(context.getResources(), vectorDrawableId);
        }
        return bitmap;
    }
}
