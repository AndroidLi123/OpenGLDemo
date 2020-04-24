package com.demo.opengl.activity

import android.content.Context
import android.opengl.GLSurfaceView
import android.os.Bundle
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import com.demo.opengl.R
import com.demo.opengl.render.SimpleTextureRender
import kotlinx.android.synthetic.main.activity_cube.*

/**
 * Copyright 2020 VW-Mobvoi Inc. All Rights Reserved
 * @author: xwli@vw-mobvoi.com
 * @created: 2020/04/14
 * @content:纹理立方体
 * @version: 1.0.0
 */
class SimpleTextureActivity:AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_simple_texture)
        updateSample(glSurfaceViewContainer, SimpleTextureRender())


    }

    private fun updateSample(rootView: View, renderer: GLSurfaceView.Renderer) {
        rootView as ViewGroup
        rootView.removeAllViews()
        rootView.addView(createGLSurfaceView(rootView.context, renderer))
    }

    private fun createGLSurfaceView(context: Context, renderer: GLSurfaceView.Renderer): GLSurfaceView {
        val glSurfaceView = GLSurfaceView(context)
        glSurfaceView.setEGLContextClientVersion(3)
        glSurfaceView.setEGLConfigChooser(8, 8, 8, 8, 8, 0)
        glSurfaceView.setRenderer(renderer)
        return glSurfaceView
    }
}