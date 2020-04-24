package com.demo.opengl.activity;

import android.content.Context;
import android.opengl.GLSurfaceView;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.demo.opengl.R;
import com.demo.opengl.render.BaseRender;
import com.demo.opengl.render.ColorRender;

/**
 * Copyright 2020 VW-Mobvoi Inc. All Rights Reserved
 *
 * @author: xwli@vw-mobvoi.com
 * @created: 2020/04/24
 * @content: 颜色滤镜
 * @version: 1.0.0
 */
public class ColorFilterActivity extends BaseCameraActivity{
    private Button btnBlack;
    private Button btnOrigin;
    private Button btnNagative;
    private Button btnCold;
    private Button btnNostalgia;
    private Button btnMosaic;
    private ColorRender colorRender;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        btnBlack = findViewById(R.id.btn_black);
        btnOrigin = findViewById(R.id.btn_origin);
        btnNagative = findViewById(R.id.btn_negative);
        btnCold = findViewById(R.id.btn_cold);
        btnNostalgia = findViewById(R.id.btn_nostalgia);
        btnMosaic = findViewById(R.id.btn_mosaic);
        btnOrigin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                colorRender.setColorFilter(ColorRender.Filter.ORIGIN);
            }
        });

        btnBlack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                colorRender.setColorFilter(ColorRender.Filter.BLACK);
            }
        });

        btnNagative.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                colorRender.setColorFilter(ColorRender.Filter.NEGATIVE);

            }
        });

        btnCold.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                colorRender.setColorFilter(ColorRender.Filter.COLD);
            }
        });

        btnNostalgia.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                colorRender.setColorFilter(ColorRender.Filter.NOSTALGIA);
            }
        });

        btnMosaic.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                colorRender.setColorFilter(ColorRender.Filter.MOSAIC);

            }
        });
    }

    @Override
    protected BaseRender createRender(Context context) {
        colorRender = new ColorRender(context);
        return colorRender;
    }

    @Override
    protected GLSurfaceView createGLSurfaceView() {
        return findViewById(R.id.gl);
    }

    @Override
    protected int getLayoutId() {
        return R.layout.activity_colorfilter;
    }
}
