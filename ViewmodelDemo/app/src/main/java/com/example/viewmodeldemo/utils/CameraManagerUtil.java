package com.example.viewmodeldemo.utils;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Context;
import android.content.pm.PackageManager;
import android.hardware.camera2.CameraAccessException;
import android.hardware.camera2.CameraCaptureSession;
import android.hardware.camera2.CameraCharacteristics;
import android.hardware.camera2.CameraDevice;
import android.hardware.camera2.CameraManager;
import android.hardware.camera2.CaptureRequest;
import android.os.Handler;
import android.os.HandlerThread;
import android.util.Log;
import android.view.Surface;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;

import java.util.Collections;

import static android.content.ContentValues.TAG;

public class CameraManagerUtil {

    private Context mContext;
    private Handler mChildHandler;
    private Handler mMainHandler;
    private CameraDevice mCameraDevice;
    private CameraManager mCameraManager;
    private String mCameraId;
    private String mFlashId;
    private SurfaceHolder mSurfaceHolder;
    private CameraCaptureSession mCameraCaptureSession;
    private CaptureRequest.Builder mCaptureRequestBuild;
    private CaptureRequest mCaptureRequest;
    //    private SurfaceView mSurfaceView;

    //摄像头回调
    private final CameraDevice.StateCallback mStateCallback = new CameraDevice.StateCallback() {

        @Override
        public void onOpened(@NonNull CameraDevice cameraDevice) {
            setmCameraDevice(cameraDevice);
        }

        @Override
        public void onDisconnected(@NonNull CameraDevice cameraDevice) {

        }

        @Override
        public void onError(@NonNull CameraDevice cameraDevice, int i) {

        }
    };

    public CameraManagerUtil(Context context) {
        mContext = context;
        build();
    }

    @SuppressLint("MissingPermission")
    public void build() {
        //create childhandler
        try {
            CameraManager cameraManager = (CameraManager) mContext.getSystemService(Context.CAMERA_SERVICE);
            //获取所有摄像头ID
            String[] ids = cameraManager.getCameraIdList();
            for (String id : ids) {
                CameraCharacteristics c = cameraManager.getCameraCharacteristics(id);
                Integer facing = c.get(CameraCharacteristics.LENS_FACING);
                Boolean flashAvailable = c.get(CameraCharacteristics.FLASH_INFO_AVAILABLE);
                if (facing == CameraCharacteristics.LENS_FACING_FRONT) {
                    setmCameraId(id);
                }
                if (flashAvailable) {
                    setmFlashId(id);
                }
            }
            setmCameraManager(cameraManager);
        } catch (CameraAccessException e) {
            e.printStackTrace();
        }
    }

    public void openCamera(HandlerThread handlerThread) {
        mChildHandler = new Handler(handlerThread.getLooper());
        try {
            if (ActivityCompat.checkSelfPermission(mContext, Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
                // TODO: Consider calling
                //    ActivityCompat#requestPermissions
                // here to request the missing permissions, and then overriding
                //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
                //                                          int[] grantResults)
                // to handle the case where the user grants the permission. See the documentation
                // for ActivityCompat#requestPermissions for more details.
                Log.d(TAG, "openCamera: 没有权限");
                return;
            }
            getmCameraManager().openCamera(getmCameraId(), mStateCallback, mChildHandler);
        } catch (CameraAccessException e) {
            e.printStackTrace();
        }
    }

    public void flash(boolean openOrOff) {
        try {
            getmCameraManager().setTorchMode(getmFlashId(), openOrOff);
        } catch (CameraAccessException e) {
            e.printStackTrace();
        }
    }

    /**
     * 预览摄像头内容
     */
    public void takePreview(SurfaceView mSurfaceView) {
//        mSurfaceView = binding.camera;
        mSurfaceHolder = mSurfaceView.getHolder();
        try {
            Surface surface = mSurfaceHolder.getSurface();
            mCaptureRequestBuild = getmCameraDevice().createCaptureRequest(CameraDevice.TEMPLATE_PREVIEW);
            if (surface != null) {
                mCaptureRequestBuild.addTarget(surface);
            } else {
                Log.d(TAG, "takePreview: surface为空");
            }

            getmCameraDevice().createCaptureSession(Collections.singletonList(surface), new CameraCaptureSession.StateCallback() {
                @Override
                public void onConfigured(@NonNull CameraCaptureSession cameraCaptureSession) {
                    mCaptureRequest = mCaptureRequestBuild.build();
                    mCameraCaptureSession = cameraCaptureSession;
                    try {
                        mCameraCaptureSession.setRepeatingBurst(Collections.singletonList(mCaptureRequest), null, mChildHandler);
                    } catch (CameraAccessException e) {
                        e.printStackTrace();
                    }
                }

                @Override
                public void onConfigureFailed(@NonNull CameraCaptureSession cameraCaptureSession) {

                }
            }, mChildHandler);
        } catch (CameraAccessException e) {
            e.printStackTrace();
        }
    }

    public void closeCamera() {
        if (mCameraCaptureSession != null) {
            mCameraCaptureSession.close();
            mCameraCaptureSession = null;
        }
        if (mCameraDevice != null) {
            mCameraDevice.close();
            mCameraDevice = null;
        }
    }

    private CameraManager getmCameraManager() {
        return mCameraManager;
    }

    private void setmCameraManager(CameraManager mCameraManager) {
        this.mCameraManager = mCameraManager;
    }

    private void setmCameraDevice(CameraDevice mCameraDevice) {
        this.mCameraDevice = mCameraDevice;
    }

    private CameraDevice getmCameraDevice() {
        return mCameraDevice;
    }

    private String getmCameraId() {
        return mCameraId;
    }

    private void setmCameraId(String mCameraId) {
        this.mCameraId = mCameraId;
    }

    private String getmFlashId() {
        return mFlashId;
    }

    private void setmFlashId(String mFlashId) {
        this.mFlashId = mFlashId;
    }

}
