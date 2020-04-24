package com.demo.opengl.render;

import android.content.Context;

import com.demo.opengl.entity.GLPoint;

import java.nio.FloatBuffer;

import javax.microedition.khronos.egl.EGLConfig;
import javax.microedition.khronos.opengles.GL10;

/**
 * Copyright 2020 VW-Mobvoi Inc. All Rights Reserved
 *
 * @author: xwli@vw-mobvoi.com
 * @created: 2020/04/24
 * @content: 人脸检测渲染
 * @version: 1.0.0
 */
public class FacePointRender extends BaseRender {
    private GLPoint glPoint;
    private float[] mPoints;

    public void setmPoints(float... mPoints) {
        this.mPoints = mPoints;

    }

    public FacePointRender(Context context) {
        super(context);
    }

    @Override
    public void onSurfaceCreated(GL10 gl, EGLConfig config) {
        super.onSurfaceCreated(gl, config);
        glPoint = new GLPoint();
    }

    @Override
    public void onDrawFrame(GL10 gl) {
        super.onDrawFrame(gl);
        if (mPoints != null) {
            glPoint.setPoints(mPoints);
            glPoint.draw();
        }
    }

    @Override
    protected int drawFrameBuffer(GL10 gl, int textureId, FloatBuffer mVertexBuffer, FloatBuffer mTextureBuffer) {
        return textureId;
    }
}
