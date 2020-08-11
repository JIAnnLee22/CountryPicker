package com.test.ocr;

import android.content.Context;
import android.graphics.Bitmap;
import android.text.TextUtils;
import android.widget.Toast;

import com.alibaba.fastjson.JSONObject;
import com.cloud.apigateway.sdk.utils.Client;
import com.cloud.apigateway.sdk.utils.Request;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;


public class HWOcrClientAKSK {
    private String appKey = null;
    private String appSecret = null;
    private String httpEndPoint = null;
    private Context context;

    public HWOcrClientAKSK(Context context, String appKey, String appSecret, String region) {
        if (TextUtils.isEmpty(appKey)) {
            Toast.makeText(context, "appKey cannot be empty", Toast.LENGTH_LONG).show();
            return;
        }
        if (TextUtils.isEmpty(appSecret)) {
            Toast.makeText(context, "appSecret cannot be empty", Toast.LENGTH_LONG).show();
            return;
        }
        if (TextUtils.isEmpty(region)) {
            Toast.makeText(context, "region cannot be empty", Toast.LENGTH_LONG).show();
            return;
        }
        this.appKey = appKey;
        this.appSecret = appSecret;
        this.httpEndPoint = "ocr." + region + ".myhuaweicloud.com";
        this.context = context;
    }

    /**
     * request ocr service
     *
     * @param uri      ocr request uri
     * @param bit      detective image
     * @param option   optional parameters
     * @param callback the callback of success or Failure
     */
    public void requestOcrAkskService(String uri, Bitmap bit, JSONObject option, Callback callback) {
        Request request = new Request();
        try {
            if (TextUtils.isEmpty(uri)) {
                Toast.makeText(context, "uri cannot be empty", Toast.LENGTH_LONG).show();
                return;
            }
            String url = option.getString("url");
            if (HWOcrClientUtils.isEmptyBitmap(bit) && url == null) {
                Toast.makeText(context, "Bitmap or image url cannot be all empty", Toast.LENGTH_LONG).show();
                return;
            }
            if (!HWOcrClientUtils.isEmptyBitmap(bit) && url != null) {
                Toast.makeText(context, "Bitmap or image url could be choose one", Toast.LENGTH_LONG).show();
                return;
            }
            request.setAppKey(appKey);
            request.setAppSecrect(appSecret);
            request.setMethod("POST");
            request.setUrl("https://" + httpEndPoint + uri);
            request.addHeader("Content-Type", "application/json");
            String fileBase64Str = HWOcrClientUtils.BitmapStrByBase64(bit);
            if (option == null) {
                option = new JSONObject();
            }
            if (url == null) {
                option.put("image", fileBase64Str);
            }
            request.setBody(option.toJSONString());
        } catch (Exception e) {
            e.printStackTrace();
            return;
        }
        try {
            //Sign the request.
            okhttp3.Request signedRequest = Client.signOkhttp(request);
            OkHttpClient.Builder httpClient = new OkHttpClient.Builder();
            OkHttpClient client = httpClient.build();
            Call call = client.newCall(signedRequest);
            call.enqueue(callback);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
