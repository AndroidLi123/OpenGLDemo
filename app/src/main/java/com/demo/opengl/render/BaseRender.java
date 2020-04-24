package com.demo.opengl.render;

import android.content.Context;
import android.graphics.SurfaceTexture;
import android.opengl.GLES11Ext;
import android.opengl.GLES30;
import android.opengl.GLSurfaceView;

import com.demo.opengl.filter.GLImageFilter;
import com.demo.opengl.filter.GLImageOESInputFilter;
import com.demo.opengl.utils.OpenGLUtils;
import com.demo.opengl.utils.TextureRotationUtils;

import java.nio.FloatBuffer;

import javax.microedition.khronos.egl.EGLConfig;
import javax.microedition.khronos.opengles.GL10;

/**
 * Copyright 2020 VW-Mobvoi Inc. All Rights Reserved
 *
 * @author: xwli@vw-mobvoi.com
 * @created: 2020/04/24
 * @content: 渲染基类Render
 * @version: 1.0.0
 */
public abstract class BaseRender implements GLSurfaceView.Renderer {
    protected abstract int drawFrameBuffer(GL10 gl, int textureId, FloatBuffer mVertexBuffer,FloatBuffer mTextureBuffer);

    private GLImageFilter imageFilter;
    private GLImageOESInputFilter inputFilter;
    private RenderSurfaceListener renderSurfaceListener;
    private FloatBuffer mVertexBuffer;
    private FloatBuffer mTextureBuffer;
    SurfaceTexture mSurfaceTexture;
    protected int textureId;
    protected Context mContext;
    private float[] transformMatrix = new float[16];

    public BaseRender(Context context) {
        this.mContext = context;
        mVertexBuffer = OpenGLUtils.createFloatBuffer(TextureRotationUtils.CubeVertices);
        mTextureBuffer = OpenGLUtils.createFloatBuffer(TextureRotationUtils.TextureVertices);
    }


    @Override
    public void onSurfaceCreated(GL10 gl, EGLConfig config) {
        GLES30.glClearColor(0.5f, 0.5f, 0.5f, 0.5f);
        inputFilter = new GLImageOESInputFilter(mContext);
        imageFilter = new GLImageFilter(mContext);
        //加载纹理
        textureId = loadTexture();
        mSurfaceTexture = new SurfaceTexture(textureId);
        if (renderSurfaceListener != null) {
            renderSurfaceListener.surfaceCreated();
        }

    }

    @Override
    public void onSurfaceChanged(GL10 gl, int width, int height) {
        GLES30.glViewport(0, 0, width, height);

        inputFilter.onDisplaySizeChanged(width, height);
        inputFilter.onInputSizeChanged(width, height);
        inputFilter.initFrameBuffer(width, height);

        imageFilter.onDisplaySizeChanged(width, height);
        imageFilter.onInputSizeChanged(width, height);
        imageFilter.initFrameBuffer(width, height);
    }

    @Override
    public void onDrawFrame(GL10 gl) {
        GLES30.glClear(GLES30.GL_COLOR_BUFFER_BIT | GLES30.GL_DEPTH_BUFFER_BIT);
        mSurfaceTexture.updateTexImage();
        mSurfaceTexture.getTransformMatrix(transformMatrix);
        inputFilter.setTextureTransformMatrix(transformMatrix);
        textureId = inputFilter.drawFrameBuffer(textureId, mVertexBuffer, mTextureBuffer);
        textureId = drawFrameBuffer(gl,textureId,mVertexBuffer,mTextureBuffer);
        imageFilter.drawFrame(textureId, mVertexBuffer, mTextureBuffer);
    }

    public SurfaceTexture getSurfaceTexture() {
        return mSurfaceTexture;
    }


    /**
     * 加载外部纹理
     *
     * @return
     */
    private int loadTexture() {
        int[] tex = new int[1];
        //创建一个纹理
        GLES30.glGenTextures(1, tex, 0);
        //绑定到外部纹理上
        GLES30.glBindTexture(GLES11Ext.GL_TEXTURE_EXTERNAL_OES, tex[0]);
        //设置纹理过滤参数
        GLES30.glTexParameterf(GLES11Ext.GL_TEXTURE_EXTERNAL_OES, GLES30.GL_TEXTURE_MIN_FILTER, GLES30.GL_NEAREST);
        GLES30.glTexParameterf(GLES11Ext.GL_TEXTURE_EXTERNAL_OES, GLES30.GL_TEXTURE_MAG_FILTER, GLES30.GL_LINEAR);
        GLES30.glTexParameterf(GLES11Ext.GL_TEXTURE_EXTERNAL_OES, GLES30.GL_TEXTURE_WRAP_S, GLES30.GL_CLAMP_TO_EDGE);
        GLES30.glTexParameterf(GLES11Ext.GL_TEXTURE_EXTERNAL_OES, GLES30.GL_TEXTURE_WRAP_T, GLES30.GL_CLAMP_TO_EDGE);
        //解除纹理绑定
        GLES30.glBindTexture(GLES11Ext.GL_TEXTURE_EXTERNAL_OES, 0);
        return tex[0];
    }

    public void setRenderSurface(RenderSurfaceListener renderSurface) {
        this.renderSurfaceListener = renderSurface;
    }

    public interface RenderSurfaceListener {
        void surfaceCreated();
    }


}
