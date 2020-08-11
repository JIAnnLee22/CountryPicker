package com.test.ocr;

import android.graphics.Bitmap;
import android.util.Base64;

import java.io.ByteArrayOutputStream;

public class HWOcrClientUtils {
    /**
     * Bitmap convert base64 string
     *
     * @param bit
     * @return base64 string
     */
    public static String BitmapStrByBase64(Bitmap bit) {
        if (isEmptyBitmap(bit)) return null;
        ByteArrayOutputStream bos = new ByteArrayOutputStream();
        bit.compress(Bitmap.CompressFormat.JPEG, 80, bos);
        byte[] bytes = bos.toByteArray();
        return Base64.encodeToString(bytes, Base64.DEFAULT);
    }

    /**
     * Returns true if the Bitmap is null or 0-length.
     *
     * @param src the Bitmap to be examined
     * @return true if src is null or zero length
     */
    public static boolean isEmptyBitmap(final Bitmap src) {
        return src == null || src.getWidth() == 0 || src.getHeight() == 0;
    }
}
