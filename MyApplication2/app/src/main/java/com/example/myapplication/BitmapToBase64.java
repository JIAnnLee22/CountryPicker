package com.example.myapplication;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;

import java.io.ByteArrayOutputStream;
import java.io.FileNotFoundException;
import java.io.IOException;

public class BitmapToBase64 {

    private Context context;

    public BitmapToBase64() {

    }

    public BitmapToBase64(Context context) {
        this.context = context;
    }

    public byte[] bitmapToBase(Uri picUri) {

        ByteArrayOutputStream baos;
        Bitmap bitmap = null;
        try {
            bitmap = BitmapFactory.decodeStream(context.getContentResolver().openInputStream(picUri));
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }

        try {
            if (bitmap != null) {
                baos = new ByteArrayOutputStream();
                //格式转换在此完成
                bitmap.compress(Bitmap.CompressFormat.JPEG, 100, baos);

                baos.flush();
                baos.close();
                return baos.toByteArray();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    public byte[] bitmapToBase(Bitmap bitmap) {

        ByteArrayOutputStream baos;
//        Bitmap bitmap = null;
//        try {
//            bitmap = BitmapFactory.decodeStream(context.getContentResolver().openInputStream(picUri));
//        } catch (FileNotFoundException e) {
//            e.printStackTrace();
//        }
        try {
            if (bitmap != null) {
                baos = new ByteArrayOutputStream();
                //格式转换在此完成
                bitmap.compress(Bitmap.CompressFormat.JPEG, 100, baos);

                baos.flush();
                baos.close();
                return baos.toByteArray();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

}
