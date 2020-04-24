package com.demo.opengl.activity;

import android.content.Context;
import android.media.Image;
import android.opengl.GLSurfaceView;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.HandlerThread;
import android.util.Log;

import com.demo.opengl.R;
import com.demo.opengl.render.BaseRender;
import com.demo.opengl.render.FacePointRender;
import com.demo.opengl.utils.ConverUtil;

import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.util.List;

import zeusees.tracking.Face;
import zeusees.tracking.FaceTracking;

/**
 *
 *
 * @author: xwli
 * @created: 2020/04/24
 * @content: 人脸检测
 * @version: 1.0.0
 */
public class FaceDetectActivity extends BaseCameraActivity {
    private static final String PATH_MODEL = "/sdcard/ZeuseesFaceTracking/models";
    private static final int COLOR_FORMATN_V21 = 2;
    private volatile byte[] data;
    private boolean mTrack106 = false;
    private Handler mHandler;
    private FacePointRender pointRenderer;
    private FaceTracking mMultiTrack106 = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        HandlerThread mHandlerThread = new HandlerThread("DrawFacePointsThread");
        mHandlerThread.start();
        mHandler = new Handler(mHandlerThread.getLooper());
        mMultiTrack106 = new FaceTracking(PATH_MODEL);
        initModelFiles();
    }

    @Override
    protected BaseRender createRender(Context context) {
        pointRenderer = new FacePointRender(this);
        return pointRenderer;
    }

    @Override
    protected GLSurfaceView createGLSurfaceView() {
        return findViewById(R.id.gl);
    }

    @Override
    protected int getLayoutId() {
        return R.layout.activity_camera;
    }

    @Override
    protected void onImageProcessAfter() {
        super.onImageProcessAfter();
        mHandler.post(new Runnable() {
            @Override
            public void run() {
                generatePoints(data);

            }
        });
    }

    @Override
    protected void onImageProcess(Image image) {
        super.onImageProcess(image);
        data = ConverUtil.getDataFromImage(image, COLOR_FORMATN_V21);

    }

    private void initModelFiles() {
        String assetPath = "ZeuseesFaceTracking";
        String sdcardPath = Environment.getExternalStorageDirectory()
                + File.separator + assetPath;
        copyFilesFromAssets(this, assetPath, sdcardPath);

    }

    public void copyFilesFromAssets(Context context, String oldPath, String newPath) {
        try {
            String[] fileNames = context.getAssets().list(oldPath);
            assert fileNames != null;
            if (fileNames.length > 0) {
                // directory
                File file = new File(newPath);
                if (!file.mkdir()) {
                    Log.d("mkdir", "can't make folder");

                }

                for (String fileName : fileNames) {
                    copyFilesFromAssets(context, oldPath + "/" + fileName,
                            newPath + "/" + fileName);
                }

            } else {
                // file
                InputStream is = context.getAssets().open(oldPath);
                FileOutputStream fos = new FileOutputStream(new File(newPath));
                byte[] buffer = new byte[1024];
                int byteCount;
                while ((byteCount = is.read(buffer)) != -1) {
                    fos.write(buffer, 0, byteCount);
                }
                fos.flush();
                is.close();
                fos.close();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    private void generatePoints(byte[] data) {
        if (mTrack106) {
            mMultiTrack106.FaceTrackingInit(data, getPhotoSize().getHeight(), getPhotoSize().getWidth());
            mTrack106 = !mTrack106;
        } else {
            mMultiTrack106.Update(data, getPhotoSize().getHeight(), getPhotoSize().getWidth());
        }

        List<Face> faceActions = mMultiTrack106.getTrackingInfo();
        if (faceActions.size() > 0) {
            Face face = faceActions.get(0);
            float[] points = new float[106 * 2];
            for (int i = 0; i < 106; i++) {
                int x = face.landmarks[i * 2];
                int y = face.landmarks[i * 2 + 1];
                points[i * 2] = view2openglX(x, getPhotoSize().getHeight());
                points[i * 2 + 1] = view2openglY(y, getPhotoSize().getWidth());
            }
            pointRenderer.setmPoints(points);
        }

    }

    private float view2openglX(int x, int width) {
        float centerX = width / 2.0f;
        float t = x - centerX;
        return t / centerX;
    }

    private float view2openglY(int y, int height) {
        float centerY = height / 2.0f;
        float s = centerY - y;
        return s / centerY;
    }


}
