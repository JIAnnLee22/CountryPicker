package com.example.viewmodeldemo.utils;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.provider.MediaStore;
import android.util.Log;

import com.huawei.hms.mlplugin.card.icr.cn.MLCnIcrCapture;
import com.huawei.hms.mlplugin.card.icr.cn.MLCnIcrCaptureConfig;
import com.huawei.hms.mlplugin.card.icr.cn.MLCnIcrCaptureFactory;
import com.huawei.hms.mlplugin.card.icr.cn.MLCnIcrCaptureResult;

/**
 * 离线识别身份证的方法，识别率低，消耗资源大，不推荐使用
 * 只能识别姓名，证件号，性别
 * 识别过后手机发热严重
 */
public class IdcardocrUtil {


    private static final int RESULT_M = 22;//定义跳转相册的返回码
    private String idName;//名字
    private String idAddress;//住址
    private String idAuthority;//签发机关
    private String idNum;//身份证号
    private String idSex;//性别
    private String idNation;//国籍
    private String idBirthday;//生日
    private String idValidDate;//生日

    private boolean isFront;//是否正面
    private boolean isRemote;//是否正面

    private Context context;

    private static final String TAG = "IdcardocrUtil";

    private MLCnIcrCapture.CallBack idCallback = new MLCnIcrCapture.CallBack() {
        @Override
        public void onSuccess(MLCnIcrCaptureResult idCardResult) {
            // 识别成功处理。
            if (idCardResult != null) {
                if (isFront()) {
                    setIdName(idCardResult.name);
                    setIdAddress(idCardResult.address);
                    setIdBirthday(idCardResult.birthday);
                    setIdSex(idCardResult.sex);
                    setIdNum(idCardResult.idNum);
                } else {
                    setIdAuthority(idCardResult.authority);
                    setIdNation(idCardResult.nation);
                    setIdValidDate(idCardResult.validDate);
                }
            } else {
                Log.d(TAG, "onSuccess: 失败");
            }

        }

        @Override
        public void onCanceled() {
            // 用户取消处理。
        }

        // 识别不到任何文字信息或识别过程发生系统异常的回调方法。
        // retCode：错误码。
        // bitmap：检测失败的身份证图片。
        @Override
        public void onFailure(int retCode, Bitmap bitmap) {
            // 识别异常处理。
        }

        @Override
        public void onDenied() {
            // 相机不支持等场景处理。
        }
    };

    private MLCnIcrCapture.CallBack idFrontCallback = new MLCnIcrCapture.CallBack() {
        @Override
        public void onSuccess(MLCnIcrCaptureResult idCardResult) {
            // 识别成功处理。
            setIdName(idCardResult.name);
            setIdAddress(idCardResult.address);
            setIdBirthday(idCardResult.birthday);
            setIdSex(idCardResult.sex);
            setIdNum(idCardResult.idNum);
        }

        @Override
        public void onCanceled() {
            // 用户取消处理。
        }

        // 识别不到任何文字信息或识别过程发生系统异常的回调方法。
        // retCode：错误码。
        // bitmap：检测失败的身份证图片。
        @Override
        public void onFailure(int retCode, Bitmap bitmap) {
            // 识别异常处理。
        }

        @Override
        public void onDenied() {
            // 相机不支持等场景处理。
        }
    };

    private MLCnIcrCapture.CallBack idBackCallback = new MLCnIcrCapture.CallBack() {
        @Override
        public void onSuccess(MLCnIcrCaptureResult idCardResult) {
            // 识别成功处理。
            setIdAuthority(idCardResult.authority);
            setIdNation(idCardResult.nation);
            setIdValidDate(idCardResult.validDate);
        }

        @Override
        public void onCanceled() {
            // 用户取消处理。
        }

        // 识别不到任何文字信息或识别过程发生系统异常的回调方法。
        // retCode：错误码。
        // bitmap：检测失败的身份证图片。
        @Override
        public void onFailure(int retCode, Bitmap bitmap) {
            // 识别异常处理。
        }

        @Override
        public void onDenied() {
            // 相机不支持等场景处理。
        }
    };

    public IdcardocrUtil() {

    }

    public void idcardOcr(Bitmap bitmap, boolean isFront, boolean isRemote) {
        setFront(isFront);
        MLCnIcrCaptureConfig config = new MLCnIcrCaptureConfig.Factory()
                .setFront(isFront)
                .setRemote(isRemote)
                .create();
        MLCnIcrCapture icrCapture = MLCnIcrCaptureFactory.getInstance().getIcrCapture(config);
            icrCapture.captureImage(bitmap, idCallback);

    }

    public void idcardOcr(Bitmap bitmapFront, Bitmap bitmapBack) {
        setFront(isFront);
        MLCnIcrCaptureConfig configFront = new MLCnIcrCaptureConfig.Factory()
                .setFront(true)
                .setRemote(false)
                .create();
        MLCnIcrCaptureConfig configBack = new MLCnIcrCaptureConfig.Factory()
                .setFront(true)
                .setRemote(false)
                .create();
        MLCnIcrCapture icrCaptureFront = MLCnIcrCaptureFactory.getInstance().getIcrCapture(configFront);
        MLCnIcrCapture icrCaptureBack = MLCnIcrCaptureFactory.getInstance().getIcrCapture(configBack);
        icrCaptureFront.captureImage(bitmapFront, idFrontCallback);
        icrCaptureBack.captureImage(bitmapBack, idBackCallback);
    }

    public boolean isFront() {
        return isFront;
    }

    private void setFront(boolean front) {
        isFront = front;
    }

    public String getIdName() {
        return idName;
    }

    private void setIdName(String idName) {
        this.idName = idName;
    }

    public String getIdAddress() {
        return idAddress;
    }

    private void setIdAddress(String idAddress) {
        this.idAddress = idAddress;
    }

    public String getIdAuthority() {
        return idAuthority;
    }

    private void setIdAuthority(String idAuthority) {
        this.idAuthority = idAuthority;
    }

    public String getIdNum() {
        return idNum;
    }

    private void setIdNum(String idNum) {
        this.idNum = idNum;
    }

    public String getIdSex() {
        return idSex;
    }

    private void setIdSex(String idSex) {
        this.idSex = idSex;
    }

    public String getIdNation() {
        return idNation;
    }

    private void setIdNation(String idNation) {
        this.idNation = idNation;
    }

    public String getIdBirthday() {
        return idBirthday;
    }

    private void setIdBirthday(String idBirthday) {
        this.idBirthday = idBirthday;
    }

    public String getIdValidDate() {
        return idValidDate;
    }

    private void setIdValidDate(String idValidDate) {
        this.idValidDate = idValidDate;
    }

    public String toString() {
        return getIdName() + "\t" + getIdSex() + "\t" + getIdNation() + "\t" + getIdBirthday() + "\n" + getIdAddress() + "\t" + getIdNum() + "\n" + getIdAuthority() + "\t" + getIdValidDate();
    }
}
