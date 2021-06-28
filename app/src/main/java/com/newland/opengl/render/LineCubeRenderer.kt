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
 * 线条立方体
 */
class LineCubeRenderer : GLSurfaceView.Renderer {
    private val vertexBuffer: FloatBuffer
    private var mProgram = 0

    /**
     * 点的坐标
     */
    private val vertexPoints = floatArrayOf(
        0.25f, 0.25f, 0.0f,  //V0
        -0.75f, 0.25f, 0.0f,  //V1
        -0.75f, -0.75f, 0.0f,  //V2
        0.25f, -0.75f, 0.0f,  //V3
        0.75f, -0.25f, 0.0f,  //V4
        0.75f, 0.75f, 0.0f,  //V5
        -0.25f, 0.75f, 0.0f,  //V6
        -0.25f, -0.25f, 0.0f,  //V7
        -0.25f, 0.75f, 0.0f,  //V6
        -0.75f, 0.25f, 0.0f,  //V1
        0.75f, 0.75f, 0.0f,  //V5
        0.25f, 0.25f, 0.0f,  //V0
        -0.25f, -0.25f, 0.0f,  //V7
        -0.75f, -0.75f, 0.0f,  //V2
        0.75f, -0.25f, 0.0f,  //V4
        0.25f, -0.75f, 0.0f //V3
    )

    override fun onSurfaceCreated(gl: GL10, config: EGLConfig) {
        //设置背景颜色
        GLES30.glClearColor(0.5f, 0.5f, 0.5f, 0.5f)
        //编译
        val vertexShaderId: Int =
            ShaderUtils.compileVertexShader(ResourceUtils.readResource(R.raw.vertex_linecube_shader))
        val fragmentShaderId: Int =
            ShaderUtils.compileFragmentShader(ResourceUtils.readResource(R.raw.fragment_linecube_shader))
        //链接程序片段
        mProgram = ShaderUtils.linkProgram(vertexShaderId, fragmentShaderId)
        //使用程序片段
        GLES30.glUseProgram(mProgram)
        GLES30.glVertexAttribPointer(
            0,
            POSITION_COMPONENT_COUNT,
            GLES30.GL_FLOAT,
            false,
            0,
            vertexBuffer
        )
        GLES30.glEnableVertexAttribArray(0)
    }

    override fun onSurfaceChanged(gl: GL10, width: Int, height: Int) {
        GLES30.glViewport(0, 0, width, height)
    }

    override fun onDrawFrame(gl: GL10) {
        GLES30.glClear(GLES30.GL_COLOR_BUFFER_BIT)

        //指定线宽
        GLES30.glLineWidth(5f)
        GLES30.glDrawArrays(GLES30.GL_LINE_LOOP, 0, 4)
        GLES30.glDrawArrays(GLES30.GL_LINE_LOOP, 4, 4)
        GLES30.glDrawArrays(GLES30.GL_LINES, 8, 8)
    }

    companion object {
        private const val POSITION_COMPONENT_COUNT = 3
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