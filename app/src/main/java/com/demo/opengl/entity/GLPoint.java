package com.demo.opengl.entity;

import android.opengl.GLES30;


import com.demo.opengl.R;
import com.demo.opengl.entity.GLBuffer;
import com.demo.opengl.utils.RenderUtil;
import com.demo.opengl.utils.ResReadUtils;

import java.nio.FloatBuffer;

/**
 *
 *
 * @author: xwli
 * @created: 2020/02/20
 * @content:
 * @version: 1.0.0
 */
public class GLPoint {
    private int program;
    private FloatBuffer vertBuffer;
    private int aPosition = 0;//位置的句柄

    public GLPoint() {
        //编译
        final int vertexShaderId = RenderUtil.compileShader(GLES30.GL_VERTEX_SHADER,
                ResReadUtils.readResource(R.raw.face_vertext_point_shader));
        final int fragmentShaderId = RenderUtil.compileShader(GLES30.GL_FRAGMENT_SHADER,
                ResReadUtils.readResource(R.raw.fragment_point_shader));
        //链接程序片段
        program = RenderUtil.linkProgram(vertexShaderId, fragmentShaderId);

    }

    public void setPoints(float[] points) {

        vertBuffer = GLBuffer.getFloatBuffer(points);
    }

    public void draw() {

        GLES30.glUseProgram(program);
        //启用顶点句柄
        GLES30.glEnableVertexAttribArray(aPosition);
        //准备坐标数据
        GLES30.glVertexAttribPointer(
                aPosition, 2,
                GLES30.GL_FLOAT, false,
                0, vertBuffer);

        GLES30.glLineWidth(10);
        GLES30.glDrawArrays(GLES30.GL_POINTS, 0, 106);
        //禁用顶点数组
        GLES30.glDisableVertexAttribArray(aPosition);

    }

}


