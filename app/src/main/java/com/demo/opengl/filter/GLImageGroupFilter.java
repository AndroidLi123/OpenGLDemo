package com.demo.opengl.filter;

import android.content.Context;

/**
 *
 *
 * @author: xwli
 * @created: 2020/04/23
 * @content:
 * @version: 1.0.0
 */
public class GLImageGroupFilter extends GLImageFilter{
    public GLImageGroupFilter(Context context) {
        super(context);
    }

    public GLImageGroupFilter(Context context, String vertexShader, String fragmentShader) {
        super(context, vertexShader, fragmentShader);
    }
}
