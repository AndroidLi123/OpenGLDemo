package com.demo.opengl.activity;

import android.os.Bundle;
import android.os.Environment;
import android.view.View;
import android.widget.Button;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.demo.opengl.R;
import com.deomo.ffmpeglib.Player;

/**
 * @author: xwli
 * @created: 2020/04/29
 * @content: 简易的音乐万能播放器
 * @version: 1.0.0
 */
public class PlayerActivity extends AppCompatActivity {


    private Button btnPlay;
    private Player player;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_player);
        btnPlay = findViewById(R.id.btn_audio);
        player = new Player();
        btnPlay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String path = "http://audio.xmcdn.com/group9/M01/43/CA/wKgDYlWlhymSp6OfAKy67-8X430449.m4a";
                player.play(path);
            }
        });
    }


}
