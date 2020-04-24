package com.demo.opengl.render;

import android.content.Context;
import android.opengl.GLES30;

import com.demo.opengl.filter.GLImageBlackWhiteFilter;
import com.demo.opengl.filter.GLImageColdFilter;
import com.demo.opengl.filter.GLImageFilter;
import com.demo.opengl.filter.GLImageMosaicFilter;
import com.demo.opengl.filter.GLImageNegativeFilter;
import com.demo.opengl.filter.GLImageNostalgiaFilter;

import java.nio.FloatBuffer;
import java.util.ArrayList;
import java.util.List;

import javax.microedition.khronos.egl.EGLConfig;
import javax.microedition.khronos.opengles.GL10;

/**
 *
 *
 * @author: xwli
 * @created: 2020/04/24
 * @content: 颜色渲染
 * @version: 1.0.0
 */
public class ColorRender extends BaseRender {

    private List<GLImageFilter> filters = new ArrayList<>();
    private ColorRender.Filter filterIndex = ColorRender.Filter.ORIGIN;

    public ColorRender(Context context) {
        super(context);
    }

    public enum Filter {
        ORIGIN(-1), NEGATIVE(0), BLACK(1), COLD(2),
        NOSTALGIA(3), MOSAIC(4);
        private int index;

        private Filter(int index) {
            this.index = index;
        }

        public int getFilter() {
            return index;
        }
    }


    public void setColorFilter(ColorRender.Filter index) {
        filterIndex = index;

    }

    @Override
    public void onSurfaceCreated(GL10 gl, EGLConfig config) {
        super.onSurfaceCreated(gl, config);
        filters.add(new GLImageNegativeFilter(mContext));
        filters.add(new GLImageBlackWhiteFilter(mContext));
        filters.add(new GLImageColdFilter(mContext));
        filters.add(new GLImageNostalgiaFilter(mContext));
        filters.add(new GLImageMosaicFilter(mContext));

    }

    @Override
    public void onSurfaceChanged(GL10 gl, int width, int height) {
        super.onSurfaceChanged(gl, width, height);
        GLES30.glViewport(0, 0, width, height);
        for (int i = 0; i < filters.size(); i++) {
            filters.get(i).onDisplaySizeChanged(width, height);
            filters.get(i).onInputSizeChanged(width, height);
            filters.get(i).initFrameBuffer(width, height);

        }

    }

    @Override
    protected int drawFrameBuffer(GL10 gl, int textureId, FloatBuffer mVertexBuffer,
                                  FloatBuffer mTextureBuffer) {
        if (filterIndex != Filter.ORIGIN) {
            return filters.get(filterIndex.getFilter()).drawFrameBuffer(textureId, mVertexBuffer, mTextureBuffer);
        }
        return textureId;

    }

}
