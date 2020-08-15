package com.example.viewmodeldemo.utils;

import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Handler;
import android.os.HandlerThread;
import android.util.Log;

import java.net.URLEncoder;
import java.util.Arrays;
import java.util.TreeMap;

import static android.content.ContentValues.TAG;

public class IdcardNetOcr {

    private byte[] picUriByte;
    private String token;

    public IdcardNetOcr() {
    }

    public IdcardNetOcr(byte[] bitmapToBase, String auth) {
        this.picUriByte = bitmapToBase;
        this.token = auth;
    }

    /**
     * 使用有参构造方法调用此方法
     * @return 识别结果
     */
    public String idcard() {
        return idcard(picUriByte, token);
    }

    /**
     * 使用无参构造方法
     * 调用此方法时候传入参数和token
     * @param bytes
     * @param token
     * @return 返回识别结果
     */
    public String idcard(byte[] bytes, String token) {
        // 请求url
        String url = "https://aip.baidubce.com/rest/2.0/ocr/v1/idcard";
        try {
            String imgStr = Base64Util.encode(bytes);
            String imgParam = URLEncoder.encode(imgStr, "UTF-8");
            String param = "id_card_side=" + "front" + "&image=" + imgParam;
            String result = HttpUtil.post(url, token, param);
            System.out.println(result);
            return result;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

}
