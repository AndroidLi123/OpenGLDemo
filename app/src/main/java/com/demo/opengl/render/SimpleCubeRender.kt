package com.demo.opengl.render

import android.opengl.GLES30
import android.opengl.GLSurfaceView
import android.opengl.Matrix
import com.demo.opengl.utils.Util
import java.nio.ByteBuffer
import java.nio.ByteOrder
import java.nio.FloatBuffer

import javax.microedition.khronos.egl.EGLConfig
import javax.microedition.khronos.opengles.GL10

/**
 * 
 *
 * @author: xwli
 * @created: 2020/04/01
 * @content:绘制立方体
 * @version: 1.0.0
 */
class SimpleCubeRender : GLSurfaceView.Renderer {

    private val vertexData = floatArrayOf(
        // 正面
        // front face
        -1f, -1f, 1f, -1f, 1f, 1f, 1f, 1f, 1f, -1f, -1f, 1f, 1f, 1f, 1f, 1f, -1f, 1f,
        // 背面
        // back face
        -1f, -1f, -1f, -1f, 1f, -1f, 1f, 1f, -1f, -1f, -1f, -1f, 1f, 1f, -1f, 1f, -1f, -1f,
        // 顶面
        // Top face
        -1f, 1f, -1f, -1f, 1f, 1f, 1f, 1f, 1f, -1f, 1f, -1f, 1f, 1f, 1f, 1f, 1f, -1f,
        // 底面
        // Bottom face
        -1f, -1f, -1f, -1f, -1f, 1f, 1f, -1f, 1f, -1f, -1f, -1f, 1f, -1f, 1f, 1f, -1f, -1f,
        // 左面
        // Left face
        -1f, -1f, -1f, -1f, -1f, 1f, -1f, 1f, 1f, -1f, -1f, -1f, -1f, 1f, 1f, -1f, 1f, -1f,
        // 右面
        // Right face
        1f, -1f, -1f, 1f, -1f, 1f, 1f, 1f, 1f, 1f, -1f, -1f, 1f, 1f, 1f, 1f, 1f, -1f
    )

    private val normalData = floatArrayOf(
        // 正面
        // front face
        0f, 0f, 1f, 0f, 0f, 1f, 0f, 0f, 1f, 0f, 0f, 1f, 0f, 0f, 1f, 0f, 0f, 1f,
        // 背面
        // back face
        0f, 0f, -1f, 0f, 0f, -1f, 0f, 0f, -1f, 0f, 0f, -1f, 0f, 0f, -1f, 0f, 0f, -1f,
        // 顶面
        // Top face
        0f, 1f, 0f, 0f, 1f, 0f, 0f, 1f, 0f, 0f, 1f, 0f, 0f, 1f, 0f, 0f, 1f, 0f,
        // 底面
        // Bottom face
        0f, -1f, 0f, 0f, -1f, 0f, 0f, -1f, 0f, 0f, -1f, 0f, 0f, -1f, 0f, 0f, -1f, 0f,
        // 左面
        // Left face
        -1f, 0f, 0f, -1f, 0f, 0f, -1f, 0f, 0f, -1f, 0f, 0f, -1f, 0f, 0f, -1f, 0f, 0f,
        // 右面
        // Right face
        1f, 0f, 0f, 1f, 0f, 0f, 1f, 0f, 0f, 1f, 0f, 0f, 1f, 0f, 0f, 1f, 0f, 0f
    )

    private val color = floatArrayOf(
        //背面矩形颜色
        0.3f, 0.4f, 0.5f, 1f,
        0.3f, 0.4f, 0.5f, 1f,
        0.3f, 0.4f, 0.5f, 1f,
        0.3f, 0.4f, 0.5f, 1f,
        0.3f, 0.4f, 0.5f, 1f,
        0.3f, 0.4f, 0.5f, 1f,

        //左侧矩形颜色
        0.3f, 0.4f, 0.5f, 1f,
        0.3f, 0.4f, 0.5f, 1f,
        0.3f, 0.4f, 0.5f, 1f,
        0.3f, 0.4f, 0.5f, 1f,
        0.3f, 0.4f, 0.5f, 1f,
        0.3f, 0.4f, 0.5f, 1f,

        //底部矩形颜色
        0.3f, 0.4f, 0.5f, 1f,
        0.3f, 0.4f, 0.5f, 1f,
        0.3f, 0.4f, 0.5f, 1f,
        0.3f, 0.4f, 0.5f, 1f,
        0.3f, 0.4f, 0.5f, 1f,
        0.3f, 0.4f, 0.5f, 1f,

        //正面矩形颜色
        0.3f, 0.4f, 0.5f, 1f,
        0.3f, 0.4f, 0.5f, 1f,
        0.3f, 0.4f, 0.5f, 1f,
        0.3f, 0.4f, 0.5f, 1f,
        0.3f, 0.4f, 0.5f, 1f,
        0.3f, 0.4f, 0.5f, 1f,

        //右侧矩形颜色
        0.3f, 0.4f, 0.5f, 1f,
        0.3f, 0.4f, 0.5f, 1f,
        0.3f, 0.4f, 0.5f, 1f,
        0.3f, 0.4f, 0.5f, 1f,
        0.3f, 0.4f, 0.5f, 1f,
        0.3f, 0.4f, 0.5f, 1f,

        //顶部矩形颜色
        0.3f, 0.4f, 0.5f, 1f,
        0.3f, 0.4f, 0.5f, 1f,
        0.3f, 0.4f, 0.5f, 1f,
        0.3f, 0.4f, 0.5f, 1f,
        0.3f, 0.4f, 0.5f, 1f,
        0.3f, 0.4f, 0.5f, 1f
    )

    var translateX = 0f
    var translateY = 0f
    var translateZ = 0f
    var rotateX = 30f
    var rotateY = -45f
    var rotateZ = 0f
    var scaleX = 1f
    var scaleY = 1f
    var scaleZ = 1f
    var cameraPositionX = 0f
    var cameraPositionY = 0f
    var cameraPositionZ = 5f
    var lookAtX = 0f
    var lookAtY = 0f
    var lookAtZ = 0f
    var cameraUpX = 0f
    var cameraUpY = 1f
    var cameraUpZ = 0f
    var nearPlaneLeft = -1f
    var nearPlaneRight = 1f
    var nearPlaneBottom = -1f
    var nearPlaneTop = 1f
    var nearPlane = 2f
    var farPlane = 100f

    private var glSurfaceViewWidth = 0
    private var glSurfaceViewHeight = 0

    private var programId = 0


    private lateinit var vertexDataBuffer: FloatBuffer
    private lateinit var normalDataBuffer: FloatBuffer
    private lateinit var colorDataBuffer: FloatBuffer

    private val LOCATION_ATTRIBUTE_POSITION = 0
    private val LOCATION_ATTRIBUTE_NORMAL = 2
    private val LOCATION_ATTRIBUTE_COLOR = 3

    private val VERTEX_COMPONENT_COUNT = 3
    private val NORMAL_COMPONENT_COUNT = 3

    override fun onSurfaceCreated(gl: GL10, config: EGLConfig) {
        programId = GLES30.glCreateProgram()

        val vertexShader = GLES30.glCreateShader(GLES30.GL_VERTEX_SHADER)
        val fragmentShader = GLES30.glCreateShader(GLES30.GL_FRAGMENT_SHADER)
        GLES30.glShaderSource(vertexShader,
            Util.loadShaderFromAssets("cube.vs")
        )
        GLES30.glShaderSource(fragmentShader,
            Util.loadShaderFromAssets("cube.fs")
        )
        GLES30.glCompileShader(vertexShader)
        GLES30.glCompileShader(fragmentShader)

        GLES30.glAttachShader(programId, vertexShader)
        GLES30.glAttachShader(programId, fragmentShader)

        GLES30.glLinkProgram(programId)
        GLES30.glUseProgram(programId)

        vertexDataBuffer = ByteBuffer.allocateDirect(vertexData.size * java.lang.Float.SIZE / 8)
            .order(ByteOrder.nativeOrder())
            .asFloatBuffer()
        vertexDataBuffer.put(vertexData)
        vertexDataBuffer.position(0)

        GLES30.glEnableVertexAttribArray(LOCATION_ATTRIBUTE_POSITION)

        GLES30.glVertexAttribPointer(
            LOCATION_ATTRIBUTE_POSITION,
            VERTEX_COMPONENT_COUNT,
            GLES30.GL_FLOAT,
            false,
            0,
            vertexDataBuffer
        )

        normalDataBuffer = ByteBuffer.allocateDirect(normalData.size * java.lang.Float.SIZE / 8)
            .order(ByteOrder.nativeOrder())
            .asFloatBuffer()
        normalDataBuffer.put(normalData)
        normalDataBuffer.position(0)

        GLES30.glEnableVertexAttribArray(LOCATION_ATTRIBUTE_NORMAL)

        GLES30.glVertexAttribPointer(
            LOCATION_ATTRIBUTE_NORMAL, NORMAL_COMPONENT_COUNT,
            GLES30.GL_FLOAT, false, 0, normalDataBuffer
        )

        colorDataBuffer = ByteBuffer.allocateDirect(
            color.size * 4
        ).order(ByteOrder.nativeOrder())
            .asFloatBuffer()
        colorDataBuffer.put(color)
        colorDataBuffer.position(0)
        GLES30.glEnableVertexAttribArray(LOCATION_ATTRIBUTE_COLOR)
        GLES30.glVertexAttribPointer(
            LOCATION_ATTRIBUTE_COLOR, 4,
            GLES30.GL_FLOAT, false,
            0, colorDataBuffer
        )


    }

    override fun onSurfaceChanged(gl: GL10, width: Int, height: Int) {

        glSurfaceViewWidth = width
        glSurfaceViewHeight = height
        nearPlaneBottom = -height.toFloat() / width
        nearPlaneTop = height.toFloat() / width
    }

    override fun onDrawFrame(gl: GL10) {
        rotateY += 1f

        GLES30.glUseProgram(programId)

        GLES30.glClearColor(0f, 0f, 0f, 0f)

        GLES30.glClear(GLES30.GL_COLOR_BUFFER_BIT)

        GLES30.glClear(GLES30.GL_DEPTH_BUFFER_BIT)

        GLES30.glViewport(0, 0, glSurfaceViewWidth, glSurfaceViewHeight)

        GLES30.glEnableVertexAttribArray(LOCATION_ATTRIBUTE_POSITION)
        GLES30.glVertexAttribPointer(
            LOCATION_ATTRIBUTE_POSITION,
            VERTEX_COMPONENT_COUNT,
            GLES30.GL_FLOAT,
            false,
            0,
            vertexDataBuffer
        )

        GLES30.glEnableVertexAttribArray(LOCATION_ATTRIBUTE_COLOR)
        GLES30.glVertexAttribPointer(
            LOCATION_ATTRIBUTE_COLOR, 4,
            GLES30.GL_FLOAT, false, 0, colorDataBuffer
        )

        GLES30.glEnableVertexAttribArray(LOCATION_ATTRIBUTE_NORMAL)
        GLES30.glVertexAttribPointer(
            LOCATION_ATTRIBUTE_NORMAL,
            NORMAL_COMPONENT_COUNT,
            GLES30.GL_FLOAT,
            false,
            0,
            normalDataBuffer
        )

        val mvpMatrix = getIdentity()
        val translateMatrix = getIdentity()
        val rotateMatrix = getIdentity()
        val scaleMatrix = getIdentity()
        val modelMatrix = getIdentity()
        val viewMatrix = getIdentity()
        val projectMatrix = getIdentity()

        Matrix.translateM(translateMatrix, 0, translateX, translateY, translateZ)
        Matrix.rotateM(rotateMatrix, 0, rotateZ, 1f, 0f, 0f)
        Matrix.rotateM(rotateMatrix, 0, rotateY, 0f, 1f, 0f)
        Matrix.rotateM(rotateMatrix, 0, rotateX, 0f, 0f, 1f)
        Matrix.scaleM(scaleMatrix, 0, scaleX, scaleY, scaleZ)
        Matrix.multiplyMM(modelMatrix, 0, rotateMatrix, 0, scaleMatrix, 0)
        Matrix.multiplyMM(modelMatrix, 0, modelMatrix, 0, translateMatrix, 0)

        Matrix.setLookAtM(
            viewMatrix,
            0,
            cameraPositionX, cameraPositionY, cameraPositionZ,
            lookAtX, lookAtY, lookAtZ,
            cameraUpX, cameraUpY, cameraUpZ
        )

        Matrix.frustumM(
            projectMatrix,
            0,
            nearPlaneLeft, nearPlaneRight, nearPlaneBottom, nearPlaneTop,
            nearPlane,
            farPlane
        )

        Matrix.multiplyMM(mvpMatrix, 0, viewMatrix, 0, modelMatrix, 0)
        Matrix.multiplyMM(mvpMatrix, 0, projectMatrix, 0, mvpMatrix, 0)

        GLES30.glUniformMatrix4fv(
            GLES30.glGetUniformLocation(programId, "model"),
            1,
            false,
            modelMatrix,
            0
        )

        GLES30.glUniformMatrix4fv(
            GLES30.glGetUniformLocation(programId, "view"),
            1,
            false,
            viewMatrix,
            0
        )

        GLES30.glUniformMatrix4fv(
            GLES30.glGetUniformLocation(programId, "projection"),
            1,
            false,
            projectMatrix,
            0
        )

        GLES30.glUniform1i(GLES30.glGetUniformLocation(programId, "imageTex"), 0)
        GLES30.glUniform3f(GLES30.glGetUniformLocation(programId, "viewPos"), 0f, 0f, 5f)
        GLES30.glUniform3f(
            GLES30.glGetUniformLocation(programId, "lightColor"),
            1f,
            1f,
            1f
        )
        GLES30.glUniform3f(
            GLES30.glGetUniformLocation(programId, "objectColor"), 0.5f, 1f, 0f
        )
        GLES30.glUniform3f(GLES30.glGetUniformLocation(programId, "lightDirection"), -10f, 0f, 0f)

        GLES30.glDrawArrays(GLES30.GL_TRIANGLES, 0, vertexData.size / VERTEX_COMPONENT_COUNT)
        assert(GLES30.glGetError() == 0)
        GLES30.glEnable(GLES30.GL_DEPTH_TEST)

    }


    private fun getIdentity(): FloatArray {
        return floatArrayOf(
            1f, 0f, 0f, 0f,
            0f, 1f, 0f, 0f,
            0f, 0f, 1f, 0f,
            0f, 0f, 0f, 1f
        )
    }
}
