package com.example.myapplication;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.graphics.PixelFormat;
import android.graphics.Point;
import android.hardware.Camera;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.provider.MediaStore;
import android.util.Log;
import android.view.KeyEvent;
import android.view.Surface;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import android.view.ViewTreeObserver;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    private static final String TAG = "MainActivity";
    private static final int RESULT_M = 101;
    private static final int OCR_IDCARD = 1001;
    private static final int OCR_PASSPORT = 1002;
    private static String BD_TOKEN = "";//百度云证件识别token

    //各个控件
    private SurfaceView mSurfaceView;//预览
    private MyPreview myPreview;//界面遮罩
    private Button knowBtn;//知道了按钮
    private Button helpBtn;//help按钮
    private Button flashBtn;//闪光灯按钮
    private Button albumBtn;//跳转相册按钮
    private ImageButton backBtn;//返回按钮
    private RadioGroup type;
    private TextView infoText;
    private int checkRadioId = 0;

    private ConstraintLayout goneLayout;//要隐藏的布局
    private ConstraintLayout showLayout;//要显示的布局

    private SurfaceHolder mSurfaceHolder;
    private int mCameraId;//相机id
    private Camera mCamera;//相机实例
    private Camera.CameraInfo mCameraInfo;//相机信息
    private Camera.Parameters mParameters;//相机参数

    private static boolean isFlash = false;//闪光灯状态

    private Uri picUri;//所选择图片的uri

    private IdcardBean mIdcardBean;//javaBean
    private PassportBean mPassportBean;//javaBean

    private SensorManager mSensorManager;//传感器管理器

    private long lastTime;//代表上次获取值的时间
    private float lastX;//代表上次的值
    private float lastY;//代表上次的值
    private float lastZ;//代表上次的值
    private double lastSpeed;//代表上次的速度

    //重力感应器监听
    private SensorEventListener mSensorEventListener = new SensorEventListener() {
        @Override
        public void onSensorChanged(SensorEvent sensorEvent) {
            if (sensorEvent.sensor.getType() == Sensor.TYPE_ACCELEROMETER) {

                long currentUpdateTime = System.currentTimeMillis();
                long timeInterval = currentUpdateTime - lastTime;

                if (timeInterval < 1000) return;

                lastTime = currentUpdateTime;

                //现在的值
                float x = sensorEvent.values[SensorManager.DATA_X];
                float y = sensorEvent.values[SensorManager.DATA_Y];
                float z = sensorEvent.values[SensorManager.DATA_Z];

                //跟之前的差值
                float deltaX = x - lastX;
                float deltaY = x - lastY;
                float deltaZ = x - lastZ;

                //把现在的值赋给之前变量
                lastX = x;
                lastY = y;
                lastZ = z;

                //计算速度变化
                double speed = Math.sqrt(deltaX * deltaX + deltaY * deltaY + deltaZ * deltaZ);
                double detalSpeed = speed - lastSpeed;
                lastSpeed = speed;

                //当速度变化小的时候拍照识别
                if (Math.abs(detalSpeed) < 0.05f) {
                    mSensorManager.unregisterListener(mSensorEventListener);
                    takeCameraPic();
                }
//                Log.d(TAG, "onSensorChanged: " + detalSpeed);
//                Log.d(TAG, "onSensorChanged: " + deltaX + "\t" + deltaY + "\t" + deltaZ);
            }
        }

        @Override
        public void onAccuracyChanged(Sensor sensor, int i) {

        }

    };

    //接收发送的消息，做出相应的操作
    @SuppressLint("HandlerLeak")
    private final Handler ocrHandler = new Handler() {
        @Override
        public void handleMessage(@NonNull Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case OCR_IDCARD:
                    //如果获取到结果，
                    switch (getmIdcardBean().getImage_status()) {
                        case "normal":
                            Log.d(TAG, "handleMessage: 识别正常");
                            break;
                        case "non_idcard":
                            Toast.makeText(getApplicationContext(), "不是身份证", Toast.LENGTH_SHORT).show();
                            initSensorManager();
                            break;
                        case "reversed_side":
                            Toast.makeText(getApplicationContext(), "身份证颠倒", Toast.LENGTH_SHORT).show();
                            break;
                        case "blurred":
                            Toast.makeText(getApplicationContext(), "身份证模糊", Toast.LENGTH_SHORT).show();
                            break;
                        case "other_type_card":
                            Toast.makeText(getApplicationContext(), "其他类型证照", Toast.LENGTH_SHORT).show();
                            break;
                        case "over_exposure":
                            Toast.makeText(getApplicationContext(), "身份证关键字段反光或过曝", Toast.LENGTH_SHORT).show();
                            break;
                        case "over_dark":
                            Toast.makeText(getApplicationContext(), "请打开闪光灯", Toast.LENGTH_SHORT).show();
                            break;
                        case "unknown":
                            if (getmIdcardBean().getWords_result().get姓名().getWords() != null)
                                return;
                            else
                                Toast.makeText(getApplicationContext(), "未知状态", Toast.LENGTH_SHORT).show();
                            break;
                    }
                    //使用getmIdcardBean()可以获得身份证识别信息

                    break;
                case OCR_PASSPORT:
                    if (getmPassportBean().getWords_result_num()<11){
                        Toast.makeText(getApplicationContext(),"扫描获取的信息不全",Toast.LENGTH_SHORT).show();
                    }
                    //使用getmPassportBean()可以获取护照识别信息
//                    if (getmPassportBean().getWords_result().get姓名() == null) return;
                    Log.d(TAG, "handleMessage: " + getmPassportBean().getWords_result().get姓名().getWords());
                    break;
            }
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        //横屏
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
        initView();
    }

    @Override
    protected void onResume() {
        super.onResume();
        //闪光灯是否开启，一开始都是没有，再次进来也是没有
        isFlash = false;
        //如果token是空的，申请token
        if (BD_TOKEN.equals("")) {
            getToken();
        }
        click();
        //获取权限
        getPermission();
    }

    @Override
    protected void onPause() {
        super.onPause();
        if (mCamera != null) {
            closeCamera();
        }
        if (mSensorManager != null) {
            mSensorManager.unregisterListener(mSensorEventListener);
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (data == null) {
            Toast.makeText(this, "请选择图片", Toast.LENGTH_SHORT).show();
        }

        //都不为空获取照片的路径
        if (requestCode == RESULT_M && data != null) {
            //所点击图片的Uri
            picUri = data.getData();
            idcardOcr(bitmapToBase64(getBitmapFromUri()));
//            Log.d(TAG, "onActivityResult: "+getBitmapFromUri());
        }

    }

    //进行身份证识别的方法
    private void idcardOcr(final byte[] bytes) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                IdcardNetOcr idcardNetOcr = new IdcardNetOcr();
                String string = idcardNetOcr.idcard(bytes, BD_TOKEN);
                IdcardBean idcardBean = GsonUtils.fromJson(string, IdcardBean.class);
                setmIdcardBean(idcardBean);
                Log.d(TAG, "run: " + string);
                Message message = ocrHandler.obtainMessage();
                message.what = OCR_IDCARD;
                message.obj = idcardBean;
                ocrHandler.sendMessage(message);
                Log.d(TAG, "进行身份证识别");
            }
        }).start();
    }

    //进行护照的识别
    private void passportOcr(final byte[] bytes) {
        new Thread() {
            @Override
            public void run() {
                super.run();
                PassportOcr passportOcr = new PassportOcr();
                String string = passportOcr.passport(bytes, BD_TOKEN);
                PassportBean passportBean = GsonUtils.fromJson(string, PassportBean.class);
                setmPassportBean(passportBean);
                Message message = ocrHandler.obtainMessage();
                message.what = OCR_PASSPORT;
                message.obj = string;
                ocrHandler.sendMessage(message);
                Log.d(TAG, "run: 进行护照识别");
            }
        }.start();
    }

    private void getPermission() {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.CAMERA}, 1);
        } else {
//            mCamera.release();
            initSurfaceView();
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == 1) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
//                mCamera.release();
                initSurfaceView();
            }
        } else {
            getPermission();
        }
    }

    private void initView() {
        knowBtn = findViewById(R.id.know_btn);
        helpBtn = findViewById(R.id.help_btn);
        backBtn = findViewById(R.id.idcard_back);
        flashBtn = findViewById(R.id.flash_btn);
        albumBtn = findViewById(R.id.album_btn);
        mSurfaceView = findViewById(R.id.surfaceView);
        myPreview = findViewById(R.id.take_pic);
        goneLayout = findViewById(R.id.gone_layout);
        showLayout = findViewById(R.id.idcardocr_show);
        infoText = findViewById(R.id.info);
        type = findViewById(R.id.certificate_select);
        //监听需要点击的控件
        knowBtn.setOnClickListener(this);
        backBtn.setOnClickListener(this);
        helpBtn.setOnClickListener(this);
        flashBtn.setOnClickListener(this);
        albumBtn.setOnClickListener(this);
        //设置比例（85.6/54）是身份证的长宽比
//        myPreview.setmScale((float) (85.6 / 54));
//        //手动测量镂空部分的大小并传进自定义view，让镂空部分的框体符合身份证的长宽比
//        ViewTreeObserver vto = myPreview.getViewTreeObserver();
//        vto.addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
//            @Override
//            public void onGlobalLayout() {
//                drawView();
//                //绘制完成之后销毁，不然无限重绘
//                myPreview.getViewTreeObserver().removeOnGlobalLayoutListener(this);
//            }
//        });

    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.know_btn:
                goneLayout.setVisibility(View.GONE);
                showLayout.setVisibility(View.VISIBLE);
//                myPreview.setmScale((float) (85.6 / 54));
//                drawView();
                initPreview();
                break;
            case R.id.idcard_back:
                finish();
                break;
            case R.id.help_btn:
                Toast.makeText(this, "点击小问号", Toast.LENGTH_SHORT).show();
                break;
            case R.id.flash_btn:
                if (isFlash) {
                    flashBtn.setBackgroundResource(R.drawable.ic_flash);
                    isFlash = false;
                    initCamera();
                    Log.d(TAG, "onClick: 点击闪光灯");
                } else {
                    flashBtn.setBackgroundResource(R.drawable.ic_flash_select);
                    isFlash = true;
                    initCamera();
                    Log.d(TAG, "onClick: 点击闪光灯");
                }
                break;
            case R.id.album_btn:
                goAlbum();
                break;

        }
    }

    private void click() {
        type.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup radioGroup, int i) {
                switch (i) {
                    case R.id.idcard_radio:
                        checkRadioId = 0;
                        infoText.setText(R.string.idcard_info);
                        myPreview.setmScale((float) (85.6 / 54));
                        drawView();
                        break;
                    case R.id.passport_radio:
                        checkRadioId = 1;
                        infoText.setText(R.string.passport_info);
                        myPreview.setmScale((float) (12.5 / 9));
                        drawView();
                        break;
                }
            }
        });
    }

    //获取扫描框的位置
    private void initPreview() {
        //设置比例（85.6/54）是身份证的长宽比
        myPreview.setmScale((float) (85.6 / 54));
        //手动测量镂空部分的大小并传进自定义view，让镂空部分的框体符合身份证的长宽比
        ViewTreeObserver vto = myPreview.getViewTreeObserver();
        vto.addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
            @Override
            public void onGlobalLayout() {
                drawView();
                //绘制完成之后销毁，不然无限重绘
                myPreview.getViewTreeObserver().removeOnGlobalLayoutListener(this);
            }
        });
        //当用户切换后台再进来，获取radio组里被点击的那个，保持进来的页面和出去时一致
        if (checkRadioId != 0) {
            switch (checkRadioId) {
                case R.id.idcard_radio:
                    myPreview.setmScale((float) (85.6 / 54));
                    drawView();
                    type.check(checkRadioId);
                    break;
                case R.id.passport_radio:
                    myPreview.setmScale((float) (12.5 / 9));
                    drawView();
                    type.check(checkRadioId);
                    break;
            }
        }
    }

    //遮罩自适应
    private void drawView() {
        myPreview.setVisibility(View.GONE);
        myPreview.setmTop(type.getBottom());
        myPreview.setmBottom(infoText.getTop() - 32);
        myPreview.setParentWidth(getWindowManager().getDefaultDisplay().getWidth());
        myPreview.setVisibility(View.VISIBLE);
        myPreview.requestLayout();
    }

    //初始化surfaceview
    private void initSurfaceView() {
        mSurfaceHolder = mSurfaceView.getHolder();
        mSurfaceHolder.setKeepScreenOn(true);
        mSurfaceHolder.addCallback(new SurfaceHolder.Callback() {
            @Override
            public void surfaceCreated(@NonNull SurfaceHolder surfaceHolder) {
                initCamera();
            }

            @Override
            public void surfaceChanged(@NonNull SurfaceHolder surfaceHolder, int i, int i1, int i2) {

            }

            @Override
            public void surfaceDestroyed(@NonNull SurfaceHolder surfaceHolder) {

            }
        });
    }

    //初始化相机
    private void initCamera() {
//        mCamera = Camera.open();
        //获取摄像头个数
        Log.d(TAG, "initCamera: " + isFlash);
        int cameraNum = Camera.getNumberOfCameras();
        for (int cameraId = 0; cameraId < cameraNum; cameraId++) {
            mCameraInfo = new Camera.CameraInfo();
            Camera.getCameraInfo(cameraId, mCameraInfo);
            //查看哪个是后置摄像头
            if (mCameraInfo.facing == Camera.CameraInfo.CAMERA_FACING_BACK) {
                mCameraId = cameraId;
            }
        }
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA) == PackageManager.PERMISSION_GRANTED) {
            mCamera = Camera.open(mCameraId);
        } else {
            getPermission();
        }
        if (mCamera != null) {
            try {
                mCamera.setPreviewDisplay(mSurfaceHolder);
            } catch (IOException e) {
                e.printStackTrace();
            }
            Point surfacePoint = new Point(mSurfaceView.getWidth(), mSurfaceView.getHeight());
            Log.d(TAG, "initCamera:surfacePoint " + surfacePoint.x + "\t" + surfacePoint.y);
            mParameters = mCamera.getParameters();
//            mParameters.setPreviewSize(1920, 1080);
            mParameters.setPreviewSize(getCameraResolution(mParameters, surfacePoint).x, getCameraResolution(mParameters, surfacePoint).y);
            mParameters.setPictureFormat(PixelFormat.JPEG);
            mParameters.setPreviewFormat(PixelFormat.YCbCr_420_SP);
            if (isFlash) {
                mParameters.setFlashMode(Camera.Parameters.FLASH_MODE_TORCH);
//                Log.d(TAG, "doAutoFocus: " + isFlash);
            } else {
                mParameters.setFlashMode(Camera.Parameters.FLASH_MODE_OFF);
//                Log.d(TAG, "doAutoFocus: " + isFlash);
            }

            setDisplayOrientation();
            mCamera.setParameters(mParameters);//设置摄像头参数
            mCamera.startPreview();
            mCamera.autoFocus(new Camera.AutoFocusCallback() {
                @Override
                public void onAutoFocus(boolean success, Camera camera) {
                    if (success) {
                        camera.cancelAutoFocus();// 加上了这一句，才自动对焦。
                        if (!Build.MODEL.equals("KORIDY H30")) {
                            mParameters = camera.getParameters();
                            mParameters.setFocusMode(Camera.Parameters.FOCUS_MODE_CONTINUOUS_PICTURE);// 连续自动对焦
                            mCamera.setParameters(mParameters);
                        } else {
                            mParameters = camera.getParameters();
                            mParameters.setFocusMode(Camera.Parameters.FOCUS_MODE_AUTO);
                            mCamera.setParameters(mParameters);
                        }
                    }
                }
            });
            initSensorManager();
        }
    }

    private void initSensorManager() {
        mSensorManager = (SensorManager) this.getSystemService(Context.SENSOR_SERVICE);
        Sensor sensor = mSensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
        mSensorManager.registerListener(mSensorEventListener, sensor, SensorManager.SENSOR_DELAY_UI);
    }

    //释放相机资源
    private void closeCamera() {
        if (mCamera != null) {
            mCamera.setPreviewCallback(null);
            mCamera.stopPreview();
            mCamera.release();
            mCamera = null;
            mParameters = null;
//            mSurfaceHolder = null;

        }
    }

    //防止拉伸倒立镜像
    private void setDisplayOrientation() {
        int rotation = this.getWindowManager().getDefaultDisplay().getRotation();
        int degrees = 0;
        switch (rotation) {
            case Surface.ROTATION_0:
                degrees = 0;
                break;
            case Surface.ROTATION_90:
                degrees = 90;
                break;
            case Surface.ROTATION_180:
                degrees = 180;
                break;
            case Surface.ROTATION_270:
                degrees = 270;
                break;
        }
        int result;
        if (mCameraInfo.facing == Camera.CameraInfo.CAMERA_FACING_FRONT) {
            result = (mCameraInfo.orientation + degrees) % 360;
            result = (360 - result) % 360;  // compensate the mirror
        } else {  // back-facing
            result = (mCameraInfo.orientation - degrees + 360) % 360;
        }
        mCamera.setDisplayOrientation(result);
    }

    //获取适合预览的分辨率，并选择最大的
    private Point getCameraResolution(Camera.Parameters parameters, Point screenResolution) {
        float tmp = 0f;
        float mindiff = 10f;
        float x_d_y = (float) screenResolution.x / (float) screenResolution.y;
        Camera.Size best = null;
        List<Camera.Size> supportedPreviewSizes = parameters.getSupportedPreviewSizes();
        for (Camera.Size s : supportedPreviewSizes) {
//            Log.d(TAG, "getCameraResolution: " + s.width + "\t" + s.height);
            tmp = Math.abs(((float) s.height / (float) s.width) - x_d_y);
            if (tmp < mindiff) {
                mindiff = tmp;
                best = s;
            }
        }
        assert best != null;
//        supportedPreviewSizes.get(supportedPreviewSizes.size()).width;
//        Log.d(TAG, "getCameraResolution: best" + best.width + "\t" + best.height);
        return new Point(supportedPreviewSizes.get(0).width, supportedPreviewSizes.get(0).height);
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

    //获取图片并转bitmap
    private Bitmap getBitmapFromUri() {
        Bitmap bitmap = null;
        try {
            bitmap = MediaStore.Images.Media.getBitmap(this.getContentResolver(), picUri);
            return bitmap;
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    //bitmap转base64，从相册选择相片转bitmap再转base64
    private byte[] bitmapToBase64(Bitmap bitmap) {
        BitmapToBase64 bitmapToBase64 = new BitmapToBase64(this);
        return bitmapToBase64.bitmapToBase(bitmap);
    }

    //获取百度云token
    private void getToken() {
        new Thread() {
            @Override
            public void run() {
                super.run();
                BD_TOKEN = AuthService.getAuth();
            }
        }.start();
    }

    //设置获取身份证信息
    private IdcardBean getmIdcardBean() {
        return mIdcardBean;
    }

    private void setmIdcardBean(IdcardBean mIdcardBean) {
        this.mIdcardBean = mIdcardBean;
    }

    //设置获取护照信息
    private PassportBean getmPassportBean() {
        return mPassportBean;
    }

    private void setmPassportBean(PassportBean mPassportBean) {
        this.mPassportBean = mPassportBean;
    }

//    //保存bitmap，检查截图的内容
//    private void saveBitmap(Bitmap bitmap) {
//        String extStorageDirectory = Environment.getExternalStorageDirectory().toString();
//        OutputStream outStream = null;
//        String filename;//声明文件名
//        //以保存时间为文件名
//        Date date = new Date(System.currentTimeMillis());
//        SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddHHmmss");
//        filename = sdf.format(date);
//        File file = new File(extStorageDirectory, filename + ".JPEG");//创建文件，第一个参数为路径，第二个参数为文件名
//        try {
//            outStream = new FileOutputStream(file);//创建输入流
//            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, outStream);
//            outStream.close();
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//    }

    //相机拍照，拍完之后检查选择的是哪种证件
    private void takeCameraPic() {
        mCamera.takePicture(null, null, new Camera.PictureCallback() {
            @Override
            public void onPictureTaken(byte[] bytes, Camera camera) {
                switch (checkRadioId) {
                    case 0:
                        idcardOcr(bytes);
                        break;
                    case 1:
                        passportOcr(bytes);
                        break;
                }
                //拍照后会停止预览，重新打开
                mCamera.startPreview();
            }
        });
    }
}