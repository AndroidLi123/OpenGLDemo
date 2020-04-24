package com.demo.opengl.activity;

import android.content.Context;
import android.opengl.GLSurfaceView;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.demo.opengl.R;
import com.demo.opengl.render.BaseRender;
import com.demo.opengl.render.BeautyRender;

/**
 *
 *
 * @author: xwli
 * @created: 2020/04/24
 * @content: 美颜
 * @version: 1.0.0
 */
public class BeautyActivity extends BaseCameraActivity {
    private Button btnBeauty;
    private BeautyRender render;
    private boolean closeBeauty;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        btnBeauty = findViewById(R.id.btn_beauty);
        btnBeauty.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                closeBeauty = !closeBeauty;
                render.setCloseBeauty(closeBeauty);
                if (closeBeauty) {
                    btnBeauty.setText("开启美颜");
                } else {
                    btnBeauty.setText("关闭美颜");
                }

            }
        });

    }

    @Override
    protected BaseRender createRender(Context context) {
        render = new BeautyRender(context);
        return render;
    }

    @Override
    protected GLSurfaceView createGLSurfaceView() {
        return findViewById(R.id.gl);
    }

    @Override
    protected int getLayoutId() {
        return R.layout.activity_camera;
    }
}
