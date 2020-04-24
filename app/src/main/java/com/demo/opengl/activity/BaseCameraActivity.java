package com.demo.opengl.activity;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Context;
import android.content.pm.PackageManager;
import android.graphics.ImageFormat;
import android.graphics.SurfaceTexture;
import android.hardware.camera2.CameraAccessException;
import android.hardware.camera2.CameraCaptureSession;
import android.hardware.camera2.CameraCharacteristics;
import android.hardware.camera2.CameraDevice;
import android.hardware.camera2.CameraManager;
import android.hardware.camera2.CaptureRequest;
import android.hardware.camera2.params.StreamConfigurationMap;
import android.media.Image;
import android.media.ImageReader;
import android.opengl.GLSurfaceView;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.util.Log;
import android.util.Size;
import android.view.Surface;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import com.demo.opengl.render.BaseRender;

import java.util.Arrays;
import java.util.List;

/**
 * Copyright 2020 VW-Mobvoi Inc. All Rights Reserved
 *
 * @author: xwli@vw-mobvoi.com
 * @created: 2020/04/24
 * @content: 相机预览基类Activity
 * @version: 1.0.0
 */
public abstract class BaseCameraActivity extends AppCompatActivity implements BaseRender.RenderSurfaceListener {

    protected abstract BaseRender createRender(Context context);
    protected abstract GLSurfaceView createGLSurfaceView();
    protected abstract int getLayoutId();

    private static final String TAG = "Camera2SurfaceActivity";
    private static final int PERMISSON_CODE = 100;

    private int cameraId = CameraCharacteristics.LENS_FACING_BACK;

    private CameraManager cameraManager;
    private Size photoSize;
    private CameraDevice cameraDevice;
    private CaptureRequest previewRequest;
    private ImageReader mPreviewImageReader;
    private GLSurfaceView glSurfaceView;
    private BaseRender renderer;

    private CameraCaptureSession.StateCallback sessionsStateCallback = new CameraCaptureSession.StateCallback() {
        @Override
        public void onConfigured(CameraCaptureSession session) {
            if (null == cameraDevice) {
                return;
            }

            try {
                session.setRepeatingRequest(previewRequest,
                        null,
                        null);
            } catch (CameraAccessException e) {
                e.printStackTrace();
            }
        }

        @Override
        public void onConfigureFailed(CameraCaptureSession session) {

        }
    };

    private CameraDevice.StateCallback cameraStateCallback = new CameraDevice.StateCallback() {
        @Override
        public void onOpened(CameraDevice camera) {
            SurfaceTexture surfaceTexture = renderer.getSurfaceTexture();
            if (surfaceTexture == null) {
                return;
            }

            surfaceTexture.setDefaultBufferSize(photoSize.getWidth(), photoSize.getHeight());
            surfaceTexture.setOnFrameAvailableListener(new SurfaceTexture.OnFrameAvailableListener() {
                @Override
                public void onFrameAvailable(final SurfaceTexture surfaceTexture) {
                    glSurfaceView.requestRender();
                }
            });

            Surface surface = new Surface(surfaceTexture);

            try {
                cameraDevice = camera;
                CaptureRequest.Builder previewRequestBuilder = cameraDevice.createCaptureRequest(CameraDevice.TEMPLATE_PREVIEW);
                previewRequestBuilder.addTarget(surface);
                previewRequestBuilder.addTarget(mPreviewImageReader.getSurface());
                previewRequest = previewRequestBuilder.build();
                cameraDevice.createCaptureSession(Arrays.asList(surface,
                        mPreviewImageReader.getSurface()), sessionsStateCallback, null);

            } catch (CameraAccessException e) {
                e.printStackTrace();
            }
        }

        @Override
        public void onDisconnected(CameraDevice camera) {

        }

        @Override
        public void onError(CameraDevice camera, int error) {

        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(getLayoutId());
        glSurfaceView = createGLSurfaceView();
        glSurfaceView.setEGLContextClientVersion(3);
        renderer = createRender(this);
        renderer.setRenderSurface(this);
        glSurfaceView.setRenderer(renderer);
        requestPermission();
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == PERMISSON_CODE && grantResults.length > 0) {
            if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                initCamera();
            }
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        closeCamera();
    }

    private void requestPermission() {
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.CAMERA}, 100);
        } else {
            initCamera();
        }
    }

    private void closeCamera() {
        if (cameraDevice != null) {
            cameraDevice.close();
            cameraDevice = null;
        }
    }

    private void initCamera() {
        cameraManager = (CameraManager) getApplication().
                getSystemService(Context.CAMERA_SERVICE);
        photoSize = getCameraOutputSizes(cameraId, SurfaceTexture.class);

        mPreviewImageReader = ImageReader.newInstance(photoSize.getWidth(),
                photoSize.getHeight(), ImageFormat.YUV_420_888
                , 2);
        mPreviewImageReader.setOnImageAvailableListener(new ImageReader.OnImageAvailableListener() {
            @Override
            public void onImageAvailable(final ImageReader reader) {
                Image image = reader.acquireNextImage();
                onImageProcess(image);
                image.close();
                onImageProcessAfter();
            }
        }, null);

    }

    protected void onImageProcessAfter() {

    }

    protected void onImageProcess(Image image) {

    }


    @SuppressLint("MissingPermission")
    public void openCamera() {
        try {
            cameraManager.openCamera(String.valueOf(cameraId), cameraStateCallback, null);
        } catch (CameraAccessException e) {
            e.printStackTrace();
            Log.e(TAG, "openCamera fail");
        }
    }

    private Size getCameraOutputSizes(int cameraId, Class clz) {
        try {

            DisplayMetrics metrics = new DisplayMetrics();
            getWindowManager().getDefaultDisplay().getRealMetrics(metrics);
            int width = metrics.widthPixels;
            int height = metrics.heightPixels;
            float radio = (float) width / height;
            CameraCharacteristics characteristics = cameraManager.getCameraCharacteristics(String.valueOf(cameraId));
            StreamConfigurationMap configs = characteristics.get(CameraCharacteristics.SCALER_STREAM_CONFIGURATION_MAP);
            List<Size> sizes = Arrays.asList(configs.getOutputSizes(clz));
            int key = 0;
            for (int i = 0; i < sizes.size(); i++) {
                Size size = sizes.get(i);
                if ((float) size.getHeight() / size.getWidth() == radio
                ) {
                    key = i;
                }
            }

            return sizes.get(key);

        } catch (CameraAccessException e) {
            e.printStackTrace();
        }

        return null;
    }

    public Size getPhotoSize() {
        return photoSize;
    }

    @Override
    public void surfaceCreated() {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                openCamera();

            }
        });
    }

}
