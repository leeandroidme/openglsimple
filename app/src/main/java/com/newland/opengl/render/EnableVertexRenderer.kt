package com.newland.opengl.render

import android.opengl.GLES30
import android.opengl.GLSurfaceView
import com.newland.opengl.R
import com.newland.opengl.utils.ResourceUtils
import com.newland.opengl.utils.ShaderUtils
import java.nio.ByteBuffer
import java.nio.ByteOrder
import javax.microedition.khronos.egl.EGLConfig
import javax.microedition.khronos.opengles.GL10

/**
 * @author: leellun
 * @data: 28/6/2021.
 *
 */
class EnableVertexRenderer : GLSurfaceView.Renderer {

    private var mProgram = 0

    /**
     * 点的坐标
     */
    private val vertexPoints = floatArrayOf(
        0.0f, 0.5f, 0.0f,
        -0.5f, -0.5f, 0.0f,
        0.5f, -0.5f, 0.0f
    )

    private val vertexColors = floatArrayOf(
        0.5f, 0.5f, 0.8f, 1.0f
    )
    private var vertexBuffer = ByteBuffer.allocateDirect(vertexPoints.size * 4)
        .order(ByteOrder.nativeOrder())
        .asFloatBuffer().apply {
            put(vertexPoints)
            position(0)
        }
    private var colorBuffer =
        ByteBuffer.allocateDirect(vertexColors.size * 4).order(ByteOrder.nativeOrder())
            .asFloatBuffer()
            .apply {
                put(vertexColors)
                position(0)
            }

    override fun onSurfaceCreated(gl: GL10?, config: EGLConfig?) {
        //设置背景颜色
        GLES30.glClearColor(0.5f, 0.5f, 0.5f, 0.5f)
        //编译
        val vertexShaderId: Int =
            ShaderUtils.compileVertexShader(ResourceUtils.readResource(R.raw.vertex_enable_shader))
        val fragmentShaderId: Int =
            ShaderUtils.compileFragmentShader(ResourceUtils.readResource(R.raw.fragment_enable_shader))
        //鏈接程序片段
        mProgram = ShaderUtils.linkProgram(vertexShaderId, fragmentShaderId)
        //使用程序片段
        GLES30.glUseProgram(mProgram)
    }

    override fun onSurfaceChanged(gl: GL10?, width: Int, height: Int) {
        GLES30.glViewport(0, 0, width, height)
    }

    override fun onDrawFrame(gl: GL10?) {
        GLES30.glClear(GLES30.GL_COLOR_BUFFER_BIT)
        //颜色数据都是一致的
        GLES30.glVertexAttrib4fv(1, colorBuffer)

        //获取位置的顶点数组
        GLES30.glVertexAttribPointer(0, 3, GLES30.GL_FLOAT, false, 0, vertexBuffer)
        //启用位置顶点属性
        GLES30.glEnableVertexAttribArray(0)

        //绘制矩形
        GLES30.glDrawArrays(GLES30.GL_TRIANGLES, 0, 3)

        //禁用顶点属性
        GLES30.glDisableVertexAttribArray(0)
    }
}