package com.newland.opengl.render

import android.opengl.GLES30
import android.opengl.GLSurfaceView
import android.opengl.Matrix
import com.newland.common.OpenBaseApplication
import com.newland.common.utils.ShaderUtils
import com.newland.opengl.R
import com.newland.opengl.utils.ResourceUtils
import java.nio.ByteBuffer
import java.nio.ByteOrder
import java.nio.FloatBuffer
import javax.microedition.khronos.egl.EGLConfig
import javax.microedition.khronos.opengles.GL10

/**
 * @author: leellun
 * @data: 28/6/2021.
 *
 */
class RectangleRenderer : GLSurfaceView.Renderer {
    companion object {
        private const val POSITION_COUNT = 2
        private const val COLOR_COUNT = 3
        private const val BYTES_FLOAT = 4
        private const val STRIDE = (POSITION_COUNT + COLOR_COUNT) * BYTES_FLOAT
    }

    private val vertexPoints = floatArrayOf(
        0.0f, 0.0f, 1.0f, 1.0f, 1.0f,
        -0.5f, -0.8f, 1.0f, 1.0f, 1.0f,
        0.5f, -0.8f, 1.0f, 1.0f, 1.0f,
        0.5f, 0.8f, 1.0f, 1.0f, 1.0f,
        -0.5f, 0.8f, 1.0f, 1.0f, 1.0f,
        -0.5f, -0.8f, 1.0f, 1.0f, 1.0f,
        0.0f, 0.25f, 0.5f, 0.5f, 0.5f,
        0.0f, -0.25f, 0.5f, 0.5f, 0.5f
    )

    private var vertexBuffer: FloatBuffer =
        ByteBuffer.allocateDirect(vertexPoints.size * BYTES_FLOAT).order(ByteOrder.nativeOrder())
            .asFloatBuffer().apply {
                put(vertexPoints)
                position(0)
            }
    private val mMatrix = FloatArray(16)

    //渲染程序
    private var mProgram: Int = -1

    private var uMatrixLocation = 0
    private var aPositionLocation = 0
    private var aColorLocation = 0

    override fun onSurfaceCreated(gl: GL10?, config: EGLConfig?) {
        GLES30.glClearColor(0.5f, 0.5f, 0.5f, 0.5f)
        var vertexShaderStr =
            ResourceUtils.readResource(OpenBaseApplication.application, R.raw.rectangle_vertex_shade)
        //编译顶点着色程序
        var vertexShaderId = ShaderUtils.compileVertexShader(vertexShaderStr)
        var fragmentShaderStr =
            ResourceUtils.readResource(
                OpenBaseApplication.application,
                R.raw.rectangle_fragment_shade
            )
        //编译片段着色程序
        var fragmentShaderId = ShaderUtils.compileFragmentShader(fragmentShaderStr)
        mProgram = ShaderUtils.likeProgram(vertexShaderId, fragmentShaderId)
        //在OpenGLES环境中使用程序
        GLES30.glUseProgram(mProgram)

        uMatrixLocation = GLES30.glGetUniformLocation(mProgram, "u_Matrix")
        aPositionLocation = GLES30.glGetAttribLocation(mProgram, "vPosition")
        aColorLocation = GLES30.glGetAttribLocation(mProgram, "aColor")

        vertexBuffer.position(0)
        GLES30.glVertexAttribPointer(
            aPositionLocation,
            POSITION_COUNT, GLES30.GL_FLOAT, false, STRIDE, vertexBuffer
        )

        GLES30.glEnableVertexAttribArray(aPositionLocation)

        vertexBuffer.position(POSITION_COUNT)
        GLES30.glVertexAttribPointer(
            aColorLocation,
            COLOR_COUNT, GLES30.GL_FLOAT, false, STRIDE, vertexBuffer
        )

        GLES30.glEnableVertexAttribArray(aColorLocation)
    }

    override fun onSurfaceChanged(gl: GL10?, width: Int, height: Int) {
        GLES30.glViewport(0, 0, width, height)

        val aspectRatio =
            if (width > height) width.toFloat() / height.toFloat() else height.toFloat() / width.toFloat()
        if (width > height) {
            //横屏
            Matrix.orthoM(mMatrix, 0, -aspectRatio, aspectRatio, -1f, 1f, -1f, 1f)
        } else {
            //竖屏
            Matrix.orthoM(mMatrix, 0, -1f, 1f, -aspectRatio, aspectRatio, -1f, 1f)
        }
    }

    override fun onDrawFrame(gl: GL10?) {
        GLES30.glClear(GLES30.GL_COLOR_BUFFER_BIT)
        GLES30.glUniformMatrix4fv(uMatrixLocation, 1, true, mMatrix, 0)
        //绘制矩形
        GLES30.glDrawArrays(GLES30.GL_TRIANGLE_STRIP, 0, 8)

        //绘制两个点
        GLES30.glDrawArrays(GLES30.GL_POINTS, 6, 2)
    }
}