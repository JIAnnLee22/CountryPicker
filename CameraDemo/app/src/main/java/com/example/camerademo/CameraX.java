package com.example.camerademo;

import android.content.Context;
import android.hardware.Camera;
import android.hardware.camera2.CameraCharacteristics;
import android.hardware.camera2.CameraDevice;
import android.hardware.camera2.CameraManager;

class CameraX {

    private CameraManager cameraManager;
    private CameraDevice cameraDevice;
    private CameraCharacteristics cameraCharacteristics;
    private Context context;

    public CameraX(){

    }

    public CameraX(Context context) {
        this.context = context;
    }

    public void creatCamera(){

    }

}
