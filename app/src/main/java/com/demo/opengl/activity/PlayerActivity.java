package com.demo.opengl.activity;

import android.os.Bundle;
import android.os.Environment;
import android.view.View;
import android.widget.Button;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.demo.opengl.R;

/**
 *
 *
 * @author: xwli
 * @created: 2020/04/29
 * @content:  简易的音乐万能播放器
 * @version: 1.0.0
 */
public class PlayerActivity extends AppCompatActivity {

    static {

        System.loadLibrary("avcodec-58");
        System.loadLibrary("avdevice-58");
        System.loadLibrary("avfilter-7");
        System.loadLibrary("avformat-58");
        System.loadLibrary("avutil-56");
        System.loadLibrary("swresample-3");
        System.loadLibrary("swscale-5");
        System.loadLibrary("wlffmpeg");
    }


    private Button btnPlay;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_player);
        btnPlay = findViewById(R.id.btn_audio);

        btnPlay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String path1 = "http://audio.xmcdn.com/group9/M01/43/CA/wKgDYlWlhymSp6OfAKy67-8X430449.m4a";
                playWithFFmpeg(path1);

            }
        });
    }

    /**
     * 播放音乐
     * @param path 文件路径
     */
    public native void playWithFFmpeg(String path);

}
