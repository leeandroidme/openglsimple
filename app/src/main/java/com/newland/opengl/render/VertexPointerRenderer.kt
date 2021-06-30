package com.newland.opengl.render

import android.opengl.GLES30
import android.opengl.GLSurfaceView
import com.newland.opengl.R
import com.newland.opengl.utils.ResourceUtils
import com.newland.common.utils.ShaderUtils
import java.nio.ByteBuffer
import java.nio.ByteOrder
import java.nio.FloatBuffer
import javax.microedition.khronos.egl.EGLConfig
import javax.microedition.khronos.opengles.GL10

/**
 */
class VertexPointerRenderer : GLSurfaceView.Renderer {
    private val vertexBuffer: FloatBuffer
    private var mProgram = 0

    /**
     * 点的坐标
     */
    private val vertexPoints = floatArrayOf( //前两个为坐标,后三个为颜色
        0.0f, 0.0f, 1.0f, 1.0f, 1.0f,
        -0.5f, -0.5f, 1.0f, 1.0f, 1.0f,
        0.5f, -0.5f, 1.0f, 1.0f, 1.0f,
        0.5f, 0.5f, 1.0f, 1.0f, 1.0f,
        -0.5f, 0.5f, 1.0f, 1.0f, 1.0f,
        -0.5f, -0.5f, 1.0f, 1.0f, 1.0f,  //两个点的顶点属性
        0.0f, 0.25f, 0.5f, 0.5f, 0.5f,
        0.0f, -0.25f, 0.5f, 0.5f, 0.5f
    )

    override fun onSurfaceCreated(gl: GL10, config: EGLConfig) {
        //设置背景颜色
        GLES30.glClearColor(0.5f, 0.5f, 0.5f, 0.5f)
        //编译
        val vertexShaderId: Int =
            ShaderUtils.compileVertexShader(ResourceUtils.readResource(R.raw.vertex_pointer_shader))
        val fragmentShaderId: Int =
            ShaderUtils.compileFragmentShader(ResourceUtils.readResource(R.raw.fragment_pointer_shader))
        //鏈接程序片段
        mProgram = ShaderUtils.linkProgram(vertexShaderId, fragmentShaderId)
        //使用程序片段
        GLES30.glUseProgram(mProgram)
        vertexBuffer.position(0)
        GLES30.glVertexAttribPointer(
            0,
            VERTEX_POSITION_SIZE,
            GLES30.GL_FLOAT,
            false,
            VERTEX_ATTRIBUTES_SIZE,
            vertexBuffer
        )
        //启用顶点属性
        GLES30.glEnableVertexAttribArray(0)
        //定位本地内存的位置
        vertexBuffer.position(VERTEX_POSITION_SIZE)
        GLES30.glVertexAttribPointer(
            1,
            VERTEX_COLOR_SIZE,
            GLES30.GL_FLOAT,
            false,
            VERTEX_ATTRIBUTES_SIZE,
            vertexBuffer
        )
        GLES30.glEnableVertexAttribArray(1)
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
        /**
         * 位置顶点属性的大小
         * 包含(x,y)
         */
        private const val VERTEX_POSITION_SIZE = 2

        /**
         * 颜色顶点属性的大小
         * 包含(r,g,b)
         */
        private const val VERTEX_COLOR_SIZE = 3

        /**
         * 浮点型数据占用字节数
         */
        private const val BYTES_PER_FLOAT = 4

        /**
         * 跨距
         */
        private const val VERTEX_ATTRIBUTES_SIZE =
            (VERTEX_POSITION_SIZE + VERTEX_COLOR_SIZE) * BYTES_PER_FLOAT
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