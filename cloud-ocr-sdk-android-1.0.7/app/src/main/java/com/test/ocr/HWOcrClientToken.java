package com.test.ocr;

import android.content.Context;
import android.graphics.Bitmap;
import android.text.TextUtils;
import android.util.Log;
import android.widget.Toast;

import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.JSONArray;

import java.io.IOException;
import java.util.concurrent.CountDownLatch;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.RequestBody;
import okhttp3.Response;


public class HWOcrClientToken {
    private String domainName = null;
    private String userName = null;
    private String password = null;
    private String region = null;
    private String endPoint = null;
    private String ocrToken = null;
    private Context context;
    private int repeatCount = 0;
    static  int retry_times_max = 3;
    static  long  intervalTime = 2000;

    private static CountDownLatch latch = new CountDownLatch(1);


    public HWOcrClientToken(Context context, String domainName, String userName, String password, String region) {
        if (TextUtils.isEmpty(domainName)) {
            Toast.makeText(context, "domainName cannot be empty", Toast.LENGTH_LONG).show();
            return;
        }
        if (TextUtils.isEmpty(userName)) {
            Toast.makeText(context, "userName cannot be empty", Toast.LENGTH_LONG).show();
            return;
        }
        if (TextUtils.isEmpty(password)) {
            Toast.makeText(context, "password cannot be empty", Toast.LENGTH_LONG).show();
            return;
        }
        if (TextUtils.isEmpty(region)) {
            Toast.makeText(context, "region cannot be empty", Toast.LENGTH_LONG).show();
            return;
        }

        this.domainName = domainName;
        this.userName = userName;
        this.password = password;
        this.region = region;
        this.endPoint = "ocr." + region + ".myhuaweicloud.com";
        this.context = context;
    }

    //  set required parameters to request the token of ocr service
    private String requestBody() {
        JSONObject auth = new JSONObject();
        JSONObject identity = new JSONObject();
        JSONArray methods = new JSONArray();
        methods.add("password");
        identity.put("methods", methods);
        JSONObject pw = new JSONObject();
        JSONObject user = new JSONObject();
        user.put("name", userName);
        user.put("password", password);
        JSONObject domain = new JSONObject();
        domain.put("name", domainName);
        user.put("domain", domain);
        pw.put("user", user);
        identity.put("password", pw);
        JSONObject scope = new JSONObject();
        JSONObject scopeProject = new JSONObject();
        scopeProject.put("name", region);
        scope.put("project", scopeProject);
        auth.put("identity", identity);
        auth.put("scope", scope);
        JSONObject params = new JSONObject();
        params.put("auth", auth);
        return params.toJSONString();
    }

    /**
     * return ocr token
     */
    private String getToken() {
        if (ocrToken != null) return ocrToken;

        while (true) {
            getTokenRequest();
            if (null == ocrToken){
                if ( repeatCount < retry_times_max){
                    repeatCount++;
                    try {
                        Thread.sleep(intervalTime);
                    } catch (InterruptedException e) {
                        System.out.println("Thread sleep exception e=" + e);
                        e.printStackTrace();
                    }
                }else {
                    return null;
                }
            }else {
                return  ocrToken;
            }
        }
    }

    private void getTokenRequest() {
        new Thread() {
            @Override
            public void run() {
                super.run();
                String requestBodyJsonStr = requestBody();
                String url = "https://iam." + region + ".myhuaweicloud.com/v3/auth/tokens";
                OkHttpClient okHttpClient = new OkHttpClient();
                RequestBody requestBody = RequestBody.create(MediaType.parse("application/json; charset=utf-8"), requestBodyJsonStr);
                okhttp3.Request request = new okhttp3.Request.Builder()
                        .url(url)
                        .post(requestBody)
                        .build();
                Call call = okHttpClient.newCall(request);
                call.enqueue(new Callback() {
                    @Override
                    public void onFailure(Call call, IOException e) {
                        Log.e("ERROR", "get token failure");
                        latch.countDown();
                    }

                    @Override
                    public void onResponse(Call call, Response response) throws IOException {
                        if (response.code() == 201) {
                            ocrToken = response.header("X-Subject-Token").toString();
                            Log.d("onResponse", "token = " + ocrToken);
                        }
                        latch.countDown();
                    }
                });

            }
        }.start();
        try {
            latch.await();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    /**
     * request ocr service
     *
     * @param uri      ocr request uri
     * @param bit      detective image
     * @param option   optional parameters
     * @param callback the callback of success or Failure
     */
    public void requestOcrTokenService(String uri, Bitmap bit, JSONObject option, Callback callback) {
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
        ocrToken = getToken();
        if (null == ocrToken) {
            Toast.makeText(context, "ocrToken is empty", Toast.LENGTH_LONG).show();
            return;
        }
        String fileBase64Str = HWOcrClientUtils.BitmapStrByBase64(bit);
        if (option == null) {
            option = new JSONObject();
        }
        if (url == null) {
            option.put("image", fileBase64Str);
        }

        OkHttpClient okHttpClient = new OkHttpClient();

        RequestBody requestBody = RequestBody.create(MediaType.parse("application/json; charset=utf-8"), option.toJSONString());
        okhttp3.Request request = new okhttp3.Request.Builder()
                .url("https://" + endPoint + uri)
                .post(requestBody)
                .addHeader("X-Auth-Token", ocrToken)
                .build();

        Call call = okHttpClient.newCall(request);
        call.enqueue(callback);
    }
}
