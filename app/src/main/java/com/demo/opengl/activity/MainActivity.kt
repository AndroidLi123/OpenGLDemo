package com.demo.opengl.activity

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import com.demo.opengl.R
import com.demo.opengl.utils.Util

/**
 *
 *
 * @author: xwli
 * @created: 2020/04/24
 * @content: 入口Activity
 * @version: 1.0.0
 */
class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        Util.context = applicationContext
        var btnCube:Button = findViewById(R.id.btn_cube)
        var btnDetect:Button = findViewById(R.id.btn_detect)
        var btnTexture:Button = findViewById(R.id.btn_texture)
        var btnFilter:Button = findViewById(R.id.btn_filter)
        var btnColor:Button = findViewById(R.id.btn_color)
        var btnPlayer:Button = findViewById(R.id.btn_player)
        btnCube.setOnClickListener{
            val intent = Intent(this@MainActivity, CubeActivity::class.java)
            this@MainActivity.startActivity(intent)
        }

        btnDetect.setOnClickListener{
            val intent = Intent(this@MainActivity, FaceDetectActivity::class.java)
            this@MainActivity.startActivity(intent)
        }

        btnTexture.setOnClickListener{
            val intent = Intent(this@MainActivity, SimpleTextureActivity::class.java)
            this@MainActivity.startActivity(intent)
        }

        btnFilter.setOnClickListener{
            val intent = Intent(this@MainActivity, BeautyActivity::class.java)
            this@MainActivity.startActivity(intent)
        }

        btnColor.setOnClickListener{
            val intent = Intent(this@MainActivity, ColorFilterActivity::class.java)
            this@MainActivity.startActivity(intent)
        }

        btnPlayer.setOnClickListener{
            val intent = Intent(this@MainActivity, PlayerActivity::class.java)
            this@MainActivity.startActivity(intent)
        }
    }
}
