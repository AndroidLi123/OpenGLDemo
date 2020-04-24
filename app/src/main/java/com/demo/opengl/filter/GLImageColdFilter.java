package com.demo.opengl.filter;

import android.content.Context;

import com.demo.opengl.R;
import com.demo.opengl.utils.ResReadUtils;

/**
 *
 *
 * @author: xwli
 * @created: 2020/04/23
 * @content:
 * @version: 1.0.0
 */
public class GLImageColdFilter extends GLImageFilter{
    public GLImageColdFilter(Context context) {
        this(context, VERTEX_SHADER,
                ResReadUtils.readResource(R.raw.fragment_cold));
    }

    public GLImageColdFilter(Context context, String vertexShader, String fragmentShader) {
        super(context, vertexShader, fragmentShader);
    }
}
