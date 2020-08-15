package com.example.cardviewdemo;

import android.content.res.Resources;
import android.util.Log;

import static android.content.ContentValues.TAG;

public class DensityUtil {

    private float density;
    private float fontScale;

    public DensityUtil() {
        density = Resources.getSystem().getDisplayMetrics().density;
        fontScale = Resources.getSystem().getDisplayMetrics().scaledDensity;
        Log.d(TAG, "DensityUtil: " + fontScale);
    }

    /**
     * 根据手机的分辨率从 dp 的单位 转成为 px(像素)
     */
    public static int dp2px(float dpValue) {
        return (int) (0.5f + dpValue * Resources.getSystem().getDisplayMetrics().density);
    }

    /**
     * 根据手机的分辨率从 px(像素) 的单位 转成为 dp
     */
    public static float px2dp(float pxValue) {
        return (pxValue / Resources.getSystem().getDisplayMetrics().density);
    }

    /**
     * 根据手机的分辨率从 dp 的单位 转成为 px(像素)
     */
    public int dip2px(float dpValue) {
        return (int) (0.5f + dpValue * density);
    }

    /**
     * 根据手机的分辨率从 px(像素) 的单位 转成为 dp
     */
    public float px2dip(float pxValue) {
        return (pxValue / density);
    }

    public static int sp2px(float spValue) {
        return (int) (spValue * Resources.getSystem().getDisplayMetrics().scaledDensity + 0.5f);
    }

    public int sip2px(float spValue) {
        return (int) (spValue * fontScale + 0.5f);
    }


}
