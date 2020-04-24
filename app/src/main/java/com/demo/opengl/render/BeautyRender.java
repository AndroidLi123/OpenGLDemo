package com.demo.opengl.render;

import android.content.Context;
import android.graphics.SurfaceTexture;
import android.opengl.GLES30;

import com.demo.opengl.filter.beauty.GLImageBeautyFilter;
import com.demo.opengl.filter.beauty.bean.BeautyParam;

import java.nio.FloatBuffer;

import javax.microedition.khronos.egl.EGLConfig;
import javax.microedition.khronos.opengles.GL10;

/**
 *
 *
 * @author: xwli
 * @created: 2020/04/24
 * @content: 美颜Render
 * @version: 1.0.0
 */
public class BeautyRender extends BaseRender {
    private boolean closeBeauty;
    private GLImageBeautyFilter beautyFilter;

    public void setCloseBeauty(boolean closeBeauty) {
        this.closeBeauty = closeBeauty;
    }

    public BeautyRender(Context mContext) {
        super(mContext);

    }

    @Override
    public void onSurfaceCreated(GL10 gl, EGLConfig config) {
        super.onSurfaceCreated(gl, config);
        beautyFilter = new GLImageBeautyFilter(mContext);

    }

    @Override
    public void onSurfaceChanged(GL10 gl, int width, int height) {
        super.onSurfaceChanged(gl, width, height);
        GLES30.glViewport(0, 0, width, height);
        beautyFilter.onInputSizeChanged(width, height);
        beautyFilter.initFrameBuffer(width, height);
        beautyFilter.onDisplaySizeChanged(width, height);

    }

    @Override
    protected int drawFrameBuffer(GL10 gl, int textureId, FloatBuffer mVertexBuffer, FloatBuffer mTextureBuffer) {
        if (!closeBeauty) {
            beautyFilter.onBeauty(new BeautyParam());
            return beautyFilter.drawFrameBuffer(textureId, mVertexBuffer, mTextureBuffer);
        }
        return textureId;
    }

    @Override
    public SurfaceTexture getSurfaceTexture() {
        return mSurfaceTexture;
    }


}

