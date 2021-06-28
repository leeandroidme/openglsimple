package com.newland.opengl.render

import android.opengl.GLES30
import android.opengl.GLSurfaceView
import com.newland.opengl.R
import com.newland.opengl.utils.ResourceUtils
import com.newland.opengl.utils.ShaderUtils
import java.nio.ByteBuffer
import java.nio.ByteOrder
import java.nio.FloatBuffer
import javax.microedition.khronos.egl.EGLConfig
import javax.microedition.khronos.opengles.GL10

/**
 *
 */
class RectangleWRenderer : GLSurfaceView.Renderer {
    private val vertexBuffer: FloatBuffer
    private var mProgram = 0

    /**
     * 点的坐标
     * (X, Y, Z, W, R, G, B)
     */
    private val vertexPoints = floatArrayOf(
        0.0f, 0.0f, 0.0f, 1.5f, 1.0f, 1.0f, 1.0f,
        -0.5f, -0.6f, 0.0f, 1f, 1.0f, 1.0f, 1.0f,
        0.5f, -0.6f, 0.0f, 1f, 1.0f, 1.0f, 1.0f,
        0.5f, 0.6f, 0.0f, 2f, 1.0f, 1.0f, 1.0f,
        -0.5f, 0.6f, 0.0f, 2f, 1.0f, 1.0f, 1.0f,
        -0.5f, -0.6f, 0.0f, 1f, 1.0f, 1.0f, 1.0f,
        0.0f, -0.4f, 0.0f, 1.25f, 0.5f, 0.5f, 0.5f,
        0.0f, 0.4f, 0.0f, 1.75f, 0.5f, 0.5f, 0.5f
    )
    private var aPositionLocation = 0
    private var aColorLocation = 0
    override fun onSurfaceCreated(gl: GL10, config: EGLConfig) {
        //设置背景颜色
        GLES30.glClearColor(0.5f, 0.5f, 0.5f, 0.5f)

        //编译
        val vertexShaderId: Int =
            ShaderUtils.compileVertexShader(ResourceUtils.readResource(R.raw.vertex_w_shader))
        val fragmentShaderId: Int =
            ShaderUtils.compileFragmentShader(ResourceUtils.readResource(R.raw.fragment_w_shader))
        //鏈接程序片段
        mProgram = ShaderUtils.linkProgram(vertexShaderId, fragmentShaderId)
        //在OpenGLES环境中使用程序片段
        GLES30.glUseProgram(mProgram)

        //uMatrixLocation = GLES30.glGetUniformLocation(mProgram, "u_Matrix");
        aPositionLocation = GLES30.glGetAttribLocation(mProgram, "vPosition")
        aColorLocation = GLES30.glGetAttribLocation(mProgram, "aColor")
        vertexBuffer.position(0)
        GLES30.glVertexAttribPointer(
            aPositionLocation,
            POSITION_COMPONENT_COUNT, GLES30.GL_FLOAT, false, STRIDE, vertexBuffer
        )
        GLES30.glEnableVertexAttribArray(aPositionLocation)
        vertexBuffer.position(POSITION_COMPONENT_COUNT)
        GLES30.glVertexAttribPointer(
            aColorLocation,
            COLOR_COMPONENT_COUNT, GLES30.GL_FLOAT, false, STRIDE, vertexBuffer
        )
        GLES30.glEnableVertexAttribArray(aColorLocation)
    }

    override fun onSurfaceChanged(gl: GL10, width: Int, height: Int) {
        GLES30.glViewport(0, 0, width, height)
    }

    override fun onDrawFrame(gl: GL10) {
        GLES30.glClear(GLES30.GL_COLOR_BUFFER_BIT)

        //绘制矩形
        GLES30.glDrawArrays(GLES30.GL_TRIANGLE_STRIP, 0, 6)

        //绘制两个点
        GLES30.glDrawArrays(GLES30.GL_POINTS, 6, 2)
    }

    companion object {
        private const val POSITION_COMPONENT_COUNT = 3
        private const val COLOR_COMPONENT_COUNT = 4
        private const val BYTES_PER_FLOAT = 4
        private const val STRIDE =
            (POSITION_COMPONENT_COUNT + COLOR_COMPONENT_COUNT) * BYTES_PER_FLOAT
    }

    init {
        //分配内存空间,每个浮点型占4字节空间
        vertexBuffer = ByteBuffer.allocateDirect(vertexPoints.size * 4)
            .order(ByteOrder.nativeOrder())
            .asFloatBuffer()
        //传入指定的坐标数据
        vertexBuffer.put(vertexPoints)
        vertexBuffer.position(0)
    }
}