package com.newland.opengl.render

import android.opengl.GLES30
import android.opengl.GLSurfaceView
import com.newland.common.OpenBaseApplication
import com.newland.common.utils.ShaderUtils
import com.newland.opengl.R
import com.newland.opengl.utils.ResourceUtils
import java.nio.ByteBuffer
import java.nio.ByteOrder
import javax.microedition.khronos.egl.EGLConfig
import javax.microedition.khronos.opengles.GL10

/**
 * @author: leellun
 * @data: 28/6/2021.
 * 立方体
 */
class ColorCubeRenderer : GLSurfaceView.Renderer {
    companion object {
        private const val VERTEX_POSITION_SIZE = 3

        private const val VERTEX_COLOR_SIZE = 4
    }

    private var mProgram = 0

    /**
     * 点的坐标
     */
    private val vertexPoints = floatArrayOf( //背面矩形
        0.75f, 0.75f, 0.0f,  //V5
        -0.25f, 0.75f, 0.0f,  //V6
        -0.25f, -0.25f, 0.0f,  //V7
        0.75f, 0.75f, 0.0f,  //V5
        -0.25f, -0.25f, 0.0f,  //V7
        0.75f, -0.25f, 0.0f,  //V4
        //左侧矩形
        -0.25f, 0.75f, 0.0f,  //V6
        -0.75f, 0.25f, 0.0f,  //V1
        -0.75f, -0.75f, 0.0f,  //V2
        -0.25f, 0.75f, 0.0f,  //V6
        -0.75f, -0.75f, 0.0f,  //V2
        -0.25f, -0.25f, 0.0f,  //V7
        //底部矩形
        0.75f, -0.25f, 0.0f,  //V4
        -0.25f, -0.25f, 0.0f,  //V7
        -0.75f, -0.75f, 0.0f,  //V2
        0.75f, -0.25f, 0.0f,  //V4
        -0.75f, -0.75f, 0.0f,  //V2
        0.25f, -0.75f, 0.0f,  //V3
        //正面矩形
        0.25f, 0.25f, 0.0f,  //V0
        -0.75f, 0.25f, 0.0f,  //V1
        -0.75f, -0.75f, 0.0f,  //V2
        0.25f, 0.25f, 0.0f,  //V0
        -0.75f, -0.75f, 0.0f,  //V2
        0.25f, -0.75f, 0.0f,  //V3
        //右侧矩形
        0.75f, 0.75f, 0.0f,  //V5
        0.25f, 0.25f, 0.0f,  //V0
        0.25f, -0.75f, 0.0f,  //V3
        0.75f, 0.75f, 0.0f,  //V5
        0.25f, -0.75f, 0.0f,  //V3
        0.75f, -0.25f, 0.0f,  //V4
        //顶部矩形
        0.75f, 0.75f, 0.0f,  //V5
        -0.25f, 0.75f, 0.0f,  //V6
        -0.75f, 0.25f, 0.0f,  //V1
        0.75f, 0.75f, 0.0f,  //V5
        -0.75f, 0.25f, 0.0f,  //V1
        0.25f, 0.25f, 0.0f //V0
    )


    //立方体的顶点颜色
    private val colors = floatArrayOf( //背面矩形颜色
        1f, 0f, 1f, 1f,
        1f, 0f, 1f, 1f,
        1f, 0f, 1f, 1f,
        1f, 0f, 1f, 1f,
        1f, 0f, 1f, 1f,
        1f, 0f, 1f, 1f,  //左侧矩形颜色
        0f, 1f, 0f, 1f,
        0f, 1f, 0f, 1f,
        0f, 1f, 0f, 1f,
        0f, 1f, 0f, 1f,
        0f, 1f, 0f, 1f,
        0f, 1f, 0f, 1f,  //底部矩形颜色
        1f, 0f, 0.5f, 1f,
        1f, 0f, 0.5f, 1f,
        1f, 0f, 0.5f, 1f,
        1f, 0f, 0.5f, 1f,
        1f, 0f, 0.5f, 1f,
        1f, 0f, 0.5f, 1f,  //正面矩形颜色
        0.2f, 0.3f, 0.2f, 1f,
        0.2f, 0.3f, 0.2f, 1f,
        0.2f, 0.3f, 0.2f, 1f,
        0.2f, 0.3f, 0.2f, 1f,
        0.2f, 0.3f, 0.2f, 1f,
        0.2f, 0.3f, 0.2f, 1f,  //右侧矩形颜色
        0.1f, 0.2f, 0.3f, 1f,
        0.1f, 0.2f, 0.3f, 1f,
        0.1f, 0.2f, 0.3f, 1f,
        0.1f, 0.2f, 0.3f, 1f,
        0.1f, 0.2f, 0.3f, 1f,
        0.1f, 0.2f, 0.3f, 1f,  //顶部矩形颜色
        0.3f, 0.4f, 0.5f, 1f,
        0.3f, 0.4f, 0.5f, 1f,
        0.3f, 0.4f, 0.5f, 1f,
        0.3f, 0.4f, 0.5f, 1f,
        0.3f, 0.4f, 0.5f, 1f,
        0.3f, 0.4f, 0.5f, 1f
    )
    private var vertexBuffer = ByteBuffer.allocateDirect(vertexPoints.size * 4)
        .order(ByteOrder.nativeOrder())
        .asFloatBuffer().apply {
            put(vertexPoints)
            position(0)
        }
    private var colorBuffer =
        ByteBuffer.allocateDirect(colors.size * 4).order(ByteOrder.nativeOrder()).asFloatBuffer()
            .apply {
                put(colors)
                position(0)
            }

    override fun onSurfaceCreated(gl: GL10?, config: EGLConfig?) {
        //设置背景颜色
        GLES30.glClearColor(0.5f, 0.5f, 0.5f, 0.5f)
        //编译
        val vertexShaderId: Int =
            ShaderUtils.compileVertexShader(
                ResourceUtils.readResource(
                    OpenBaseApplication.application,
                    R.raw.vertex_colorcube_shader
                )
            )
        val fragmentShaderId: Int =
            ShaderUtils.compileFragmentShader(
                ResourceUtils.readResource(
                    OpenBaseApplication.application,
                    R.raw.fragment_colorcube_shader
                )
            )
        //链接程序片段
        mProgram = ShaderUtils.likeProgram(vertexShaderId, fragmentShaderId)
        //使用程序片段
        GLES30.glUseProgram(mProgram)
        GLES30.glVertexAttribPointer(
            0,
            VERTEX_POSITION_SIZE,
            GLES30.GL_FLOAT,
            false,
            0,
            vertexBuffer
        )
        //启用位置顶点属性
        GLES30.glEnableVertexAttribArray(0)
        GLES30.glVertexAttribPointer(1, VERTEX_COLOR_SIZE, GLES30.GL_FLOAT, false, 0, colorBuffer)
        //启用颜色顶点属性
        GLES30.glEnableVertexAttribArray(1)
    }

    override fun onSurfaceChanged(gl: GL10?, width: Int, height: Int) {
        GLES30.glViewport(0, 0, width, height)
    }

    override fun onDrawFrame(gl: GL10?) {
        GLES30.glClear(GLES30.GL_COLOR_BUFFER_BIT)
        GLES30.glDrawArrays(GLES30.GL_TRIANGLES, 0, 36)
    }
}