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
public class GLImageNostalgiaFilter extends GLImageFilter {
    public GLImageNostalgiaFilter(Context context) {
        this(context, VERTEX_SHADER,
                ResReadUtils.readResource(R.raw.fragment_nostalgia));
    }

    public GLImageNostalgiaFilter(Context context, String vertexShader, String fragmentShader) {
        super(context, vertexShader, fragmentShader);
    }
}
