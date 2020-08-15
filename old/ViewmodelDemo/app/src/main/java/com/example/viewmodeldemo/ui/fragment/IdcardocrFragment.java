package com.example.viewmodeldemo.ui.fragment;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.ImageFormat;
import android.hardware.camera2.CameraAccessException;
import android.hardware.camera2.CameraCaptureSession;
import android.hardware.camera2.CameraCharacteristics;
import android.hardware.camera2.CameraDevice;
import android.hardware.camera2.CameraManager;
import android.hardware.camera2.CaptureRequest;
import android.hardware.camera2.params.StreamConfigurationMap;
import android.media.Image;
import android.media.ImageReader;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;

import android.os.Handler;
import android.os.HandlerThread;
import android.os.Looper;
import android.os.Message;
import android.provider.MediaStore;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.widget.RadioGroup;
import android.widget.Toast;

import com.example.viewmodeldemo.Bean.IdcardResult;
import com.example.viewmodeldemo.Bean.PassportResult;
import com.example.viewmodeldemo.R;
import com.example.viewmodeldemo.databinding.FragmentIdcardocrBinding;
import com.example.viewmodeldemo.utils.AuthService;
import com.example.viewmodeldemo.utils.BitmapToBase64;
import com.example.viewmodeldemo.utils.IdcardNetOcr;
import com.example.viewmodeldemo.utils.PassportOcr;

import org.jetbrains.annotations.NotNull;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.util.Arrays;

import static android.content.ContentValues.TAG;


public class IdcardocrFragment extends Fragment {

    private static final int REQUEST_CODE_WRITE_EXTERNAL_STORAGE = 1002;
    private FragmentIdcardocrBinding binding;
    private static final int RESULT_IDCARD = 1;
    private static final int RESULT_PASSPORT = 2;
    private static final int RESULT_M = 200;//相册的返回码

    private String mCameraId;//声明后置摄像的id
    private String mFlashId;//声明闪光灯的id
    private CameraManager mCameraManager;
    private ImageReader mImageReader;
    private static boolean flashChange = false;
    private SurfaceView mSurfaceView;//相机预览
    private SurfaceHolder mSurfaceHolder;
    private Handler childHandler;
    private Handler mainHandler;
    private CameraCaptureSession mCameraCaptureSession;
    private CaptureRequest.Builder mCaptureRequestBuild = null;
    private CaptureRequest mCaptureRequest;

    private Uri picUri;
    private static String token;//百度云证件识别token

    private IdcardResult idcardResult;
    private PassportResult passportResult;
    private CameraDevice mCameraDevice;

    private int checkRadioId;

    @SuppressLint("HandlerLeak")
    private Handler handler = new Handler() {
        @SuppressLint("SetTextI18n")
        @Override
        public void handleMessage(@NonNull Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case RESULT_IDCARD:
                    //将获取的实例化对象打印
                    //根据识别的图片状态区分是否识别成功
                    break;
                case RESULT_PASSPORT:
                    //将获取的实例化对象打印
                    break;
            }
        }

    };

    public IdcardocrFragment() {
    }


    @Override
    public View onCreateView(@NotNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        binding = FragmentIdcardocrBinding.inflate(inflater);
        return binding.getRoot();
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        click();
        getToken();
        initSurfaceView();

    }

    @Override
    public void onResume() {
        super.onResume();
        flashChange = false;
        if (flashChange) {
            binding.flashBtn.setBackgroundResource(R.drawable.ic_flash_select);
        } else {
            binding.flashBtn.setBackgroundResource(R.drawable.ic_flash);
        }
        initTakepic();
    }

    //离开fragment关闭摄像头调用,闪光灯恢复初始
    @Override
    public void onPause() {
        super.onPause();
        if (flashChange) {
            binding.flashBtn.setBackgroundResource(R.drawable.ic_flash_select);
        } else {
            binding.flashBtn.setBackgroundResource(R.drawable.ic_flash);
        }
        checkRadioId = binding.certificateSelect.getCheckedRadioButtonId();
        //没有聚焦在这个fragment的时候关闭闪光灯
        //并且将按钮设回 闪光灯-关 背景
        if (mCameraManager != null) {
            flashChange = false;
            flashChange();
        }
        binding.flashBtn.setBackgroundResource(R.drawable.ic_flash);
        if (mCameraCaptureSession != null) {
            mCameraCaptureSession.close();
            mCameraCaptureSession = null;
        }
        if (mCameraDevice != null) {
            mCameraDevice.close();
            mCameraDevice = null;
        }
    }

    //跳转相册返回结果
    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (data == null) {
            Toast.makeText(binding.getRoot().getContext(), "请选择图片", Toast.LENGTH_SHORT).show();
        }

        //都不为空获取照片的路径
        if (requestCode == RESULT_M && data != null) {
            Uri dataUri = data.getData();
            setPicUri(dataUri);
            test();
        }


    }

    private void test() {
        IdcardNetOcr idcardNetOcr = new IdcardNetOcr();
        String string = idcardNetOcr.idcard(bitmapToBase64(), token);
        Log.d(TAG, "test: " + getBitmapFromUri());
        Log.d(TAG, "test: " + bitmapToBase64());
        Log.d(TAG, "test: " + string);
    }
    //获取图片并转bitmap
    private Bitmap getBitmapFromUri() {
        Bitmap bitmap = null;
        try {
            bitmap = MediaStore.Images.Media.getBitmap(requireContext().getContentResolver(), picUri);
            return bitmap;
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    private byte[] bitmapToBase64() {
        BitmapToBase64 bitmapToBase64 = new BitmapToBase64(requireContext());
        return bitmapToBase64.bitmapToBase(getBitmapFromUri());
    }


    //各个按键的点击事件
    private void click() {

        //点击切换扫描框的比例
        binding.certificateSelect.setOnCheckedChangeListener((radioGroup, i) -> {
            switch (i) {
                case R.id.idcard_radio:
                    binding.info.setText(R.string.idcard_info);
                    binding.takePic.setmScale((float) (85.6 / 54));
                    drawView();
                    break;
                case R.id.passport_radio:
                    binding.info.setText(R.string.passport_info);
                    binding.takePic.setmScale((float) (12.5 / 9));
                    drawView();
                    break;
            }
        });

        //按下知道了
        binding.knowBtn.setOnClickListener(view -> {
            binding.idcarocrGone.setVisibility(View.GONE);
            binding.idcardocrShow.setVisibility(View.VISIBLE);
            initTakepic();
        });

        //点击跳转相册选择图片
        binding.albumBtn.setOnClickListener(view -> {
            goAlbum();
            flashChange = false;
            flashChange();
        });

        //闪光灯的控制
        binding.flashBtn.setOnClickListener(view -> {
            if (flashChange) {
                view.setBackgroundResource(R.drawable.ic_flash_select);
                takePreview();
//                flashChange = false;
            } else {
                view.setBackgroundResource(R.drawable.ic_flash);
                takePreview();
//                flashChange = true;
            }
//                flashChange();


        });

        //帮助按钮小问号
        binding.helpBtn.setOnClickListener(view -> {
            Toast.makeText(requireContext(), "你点击了帮助按钮", Toast.LENGTH_SHORT).show();
        });

    }

    //获取百度云token
    private void getToken() {
        if (token == null) {
            new Thread() {
                @Override
                public void run() {
                    super.run();
                    token = AuthService.getAuth();
                }
            }.start();
        }
    }

    //把进入fragment调整扫描框代码放在一个方法里，多处调用
    private void initTakepic() {
        //设置比例（85.6/54）是身份证的长宽比
        binding.takePic.setmScale((float) (85.6 / 54));
        //手动测量镂空部分的大小并传进自定义view，让镂空部分的框体符合身份证的长宽比
        ViewTreeObserver vto = binding.takePic.getViewTreeObserver();
        vto.addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {
                drawView();
                //绘制完成之后销毁，不然无限重绘
                binding.takePic.getViewTreeObserver().removeOnGlobalLayoutListener(this);
            }
        });
        //当用户切换后台再进来，获取radio组里被点击的那个，保持进来的页面和出去时一致
        if (checkRadioId != 0) {
            switch (checkRadioId) {
                case R.id.idcard_radio:
                    binding.takePic.setmScale((float) (85.6 / 54));
                    drawView();
                    binding.certificateSelect.check(checkRadioId);
                    break;
                case R.id.passport_radio:
                    binding.takePic.setmScale((float) (12.5 / 9));
                    drawView();
                    binding.certificateSelect.check(checkRadioId);
                    break;
            }
        }
    }

    //初始化surfaceview，初始化后打开相机
    private void initSurfaceView() {
        mSurfaceView = binding.surfaceView;
        mSurfaceHolder = mSurfaceView.getHolder();
        mSurfaceHolder.setKeepScreenOn(true);
        mSurfaceHolder.addCallback(new SurfaceHolder.Callback() {
            @Override
            public void surfaceCreated(@NonNull SurfaceHolder surfaceHolder) {
                creatCamera();
            }

            @Override
            public void surfaceChanged(@NonNull SurfaceHolder surfaceHolder, int i, int i1, int i2) {

            }

            @Override
            public void surfaceDestroyed(@NonNull SurfaceHolder surfaceHolder) {

            }
        });

    }

    //获取扫描框的位置
    private void drawView() {
        binding.takePic.setVisibility(View.GONE);
        binding.takePic.setmTop(binding.certificateSelect.getBottom());
        binding.takePic.setmBottom(binding.info.getTop() - 32);
        binding.takePic.setParentWidth(binding.getRoot().getWidth());
        Log.d(TAG, "drawView: "+binding.getRoot().getWidth());
        binding.takePic.setVisibility(View.VISIBLE);
        binding.takePic.requestLayout();
    }

    //摄像头回调
    private CameraDevice.StateCallback stateCallback = new CameraDevice.StateCallback() {
        @Override
        public void onOpened(CameraDevice camera) {//打开摄像头
            mCameraDevice = camera;
            //开启预览
            takePreview();
        }

        @Override
        public void onDisconnected(CameraDevice camera) {//关闭摄像头
            if (null != mCameraDevice) {
                mCameraDevice.close();
                camera.close();
                mCameraDevice = null;
            }
        }

        @Override
        public void onError(CameraDevice camera, int error) {//发生错误
            Toast.makeText(getContext(), "摄像头开启失败" + error, Toast.LENGTH_SHORT).show();
            if (null != mCameraDevice) {
                mCameraDevice.close();
                camera.close();
                mCameraDevice = null;
            }
            camera.close();
        }
    };

    //初始化摄像头，获取摄像头之后开启摄像头
    @SuppressLint("MissingPermission")
    private void creatCamera() {

        HandlerThread handlerThread = new HandlerThread("camera");
        handlerThread.start();
        childHandler = new Handler(handlerThread.getLooper());
        mainHandler = new Handler(Looper.getMainLooper());

        mImageReader = ImageReader.newInstance(1080, 1920, ImageFormat.JPEG, 1);
        mImageReader.setOnImageAvailableListener(imageReader -> {
            Image image = imageReader.acquireNextImage();
            ByteBuffer buffer = image.getPlanes()[0].getBuffer();
            byte[] bytes = new byte[buffer.remaining()];
            buffer.get(bytes);//由缓冲区存入字节数组
            final Bitmap bitmap = BitmapFactory.decodeByteArray(bytes, 0, bytes.length);
        }, mainHandler);
        CameraManager cameraManager = (CameraManager) binding.getRoot().getContext().getSystemService(Context.CAMERA_SERVICE);
        try {
            for (String id : cameraManager.getCameraIdList()) {
                CameraCharacteristics characteristics = cameraManager.getCameraCharacteristics(id);
                Integer facing = characteristics.get(CameraCharacteristics.LENS_FACING);
                Boolean flashAvailable = characteristics.get(CameraCharacteristics.FLASH_INFO_AVAILABLE);
                //获取后置摄像头id
                if (facing != null && facing == CameraCharacteristics.LENS_FACING_BACK) {
                    mCameraId = id;
                }
                //获取带闪光灯的id
                if (facing != null && flashAvailable) {
                    mFlashId = id;
                }
            }
            mCameraManager = (CameraManager) binding.getRoot().getContext().getSystemService(Context.CAMERA_SERVICE);
            mCameraManager.openCamera(mCameraId, stateCallback, childHandler);
        } catch (CameraAccessException e) {
            e.printStackTrace();
        }
    }

    //预览摄像头内容
    private void takePreview() {
        try {
            // 创建预览需要的CaptureRequest.Builder
//            final CaptureRequest.Builder previewRequestBuilder = mCameraDevice.createCaptureRequest(CameraDevice.TEMPLATE_PREVIEW);
            mCaptureRequestBuild = mCameraDevice.createCaptureRequest(CameraDevice.TEMPLATE_PREVIEW);
            // 将SurfaceView的surface作为CaptureRequest.Builder的目标
            mCaptureRequestBuild.addTarget(mSurfaceHolder.getSurface());
            // 创建CameraCaptureSession，该对象负责管理处理预览请求和拍照请求
            mCameraDevice.createCaptureSession(Arrays.asList(mSurfaceHolder.getSurface(), mImageReader.getSurface()), new CameraCaptureSession.StateCallback() // ③
            {
                @Override
                public void onConfigured(@NotNull CameraCaptureSession cameraCaptureSession) {
                    if (null == mCameraDevice) return;
                    // 当摄像头已经准备好时，开始显示预览
                    mCameraCaptureSession = cameraCaptureSession;
                    try {
                        //自动对焦
                        mCaptureRequestBuild.set(CaptureRequest.CONTROL_AF_MODE, CaptureRequest.CONTROL_AF_MODE_CONTINUOUS_PICTURE);
                        // 打开闪光灯--（打不开啊？）
                        mCaptureRequestBuild.set(CaptureRequest.CONTROL_AE_MODE, CaptureRequest.CONTROL_AE_MODE_ON);
                        if (flashChange) {
                            mCaptureRequestBuild.set(CaptureRequest.FLASH_MODE, CaptureRequest.FLASH_MODE_TORCH);
                            flashChange = false;
                        } else {
                            mCaptureRequestBuild.set(CaptureRequest.FLASH_MODE, CaptureRequest.FLASH_MODE_OFF);
                            flashChange = true;
                        }
                        // 显示预览
                        CaptureRequest previewRequest = mCaptureRequestBuild.build();
                        mCameraCaptureSession.setRepeatingRequest(previewRequest, null, childHandler);
                    } catch (CameraAccessException e) {
                        e.printStackTrace();
                    }
                }

                @Override
                public void onConfigureFailed(@NotNull CameraCaptureSession cameraCaptureSession) {
                    Toast.makeText(getContext(), "摄像头配置失败", Toast.LENGTH_SHORT).show();
                }
            }, childHandler);
        } catch (CameraAccessException e) {
            e.printStackTrace();
        }
    }

    //闪光灯状态更改
    private void flashChange() {
        if (flashChange) {
//            FlashUtils flashUtils = new FlashUtils(binding.getRoot().getContext());
//            flashUtils.changeFlashLight(mCameraManager, flashChange);
//                mCameraManager.setTorchMode(getmFlashId(), true);
            mCaptureRequestBuild.set(CaptureRequest.CONTROL_AE_MODE, CaptureRequest.CONTROL_AE_MODE_ON_ALWAYS_FLASH);
            flashChange = false;
        } else {
//            FlashUtils flashUtils = new FlashUtils(binding.getRoot().getContext());
//            flashUtils.changeFlashLight(mCameraManager, flashChange);
//                mCameraManager.setTorchMode(getmFlashId(), false);
            mCaptureRequestBuild.set(CaptureRequest.CONTROL_AE_MODE, CaptureRequest.CONTROL_AE_MODE_OFF);
            flashChange = true;
        }
    }

    //截取预览框的图片
    private void takePicture() {
        /**
         * 获取框里面的四个坐标，可以只截扫描框里面的图
         * 这样证件的内边距就不会太远，内边距太大识别困难
         * binding.takePic.getOrigin();//x头
         * binding.takePic.getEnd();//x尾
         * binding.takePic.getmTop();//y头
         * binding.takePic.getmBottom();//y尾
         */

    }

    private void takeOcr(){
        
    }

    //跳转相册
    private void goAlbum() {
        Intent intent = new Intent();
        intent.setAction(Intent.ACTION_GET_CONTENT);
        // 设置文件类型
        Intent pickIntent = new Intent(Intent.ACTION_PICK,
                MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        pickIntent.setDataAndType(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, "image/*");
        startActivityForResult(pickIntent, RESULT_M);
    }

    //获取选取的照片的uri
    public Uri getPicUri() {
        return picUri;
    }

    private void setPicUri(Uri picUri) {
        this.picUri = picUri;
    }

    //赋值和获取身份证对象的信息
    public IdcardResult getIdcardResult() {
        return idcardResult;
    }

    public void setIdcardResult(IdcardResult idcardResult) {
        this.idcardResult = idcardResult;
    }

    //赋值和获取护照对象的信息
    public PassportResult getPassportResult() {
        return passportResult;
    }

    public void setPassportResult(PassportResult passportResult) {
        this.passportResult = passportResult;
    }

    private void setmCameraDevice(CameraDevice mCameraDevice) {
        this.mCameraDevice = mCameraDevice;
    }

    public CameraDevice getmCameraDevice() {
        return mCameraDevice;
    }

    public String getmCameraId() {
        return mCameraId;
    }

    private void setmCameraId(String mCameraId) {
        this.mCameraId = mCameraId;
    }

    public String getmFlashId() {
        return mFlashId;
    }

    private void setmFlashId(String mFlashId) {
        this.mFlashId = mFlashId;
    }
}