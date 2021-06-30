package com.newland.demo1.renderer

import android.opengl.GLES30
import android.opengl.GLSurfaceView
import com.newland.common.utils.ShaderUtils
import com.newland.demo1.R
import com.newland.opengl.utils.ResourceUtils
import java.nio.ByteBuffer
import java.nio.ByteOrder
import javax.microedition.khronos.egl.EGLConfig
import javax.microedition.khronos.opengles.GL10

/**
 * @author: leellun
 * @data: 30/6/2021.
 * 三角形 线条 点 预先设置
 */
class TrianglesRenderer2 : GLSurfaceView.Renderer {
    private val tableTriangles = floatArrayOf(
        0f, 0f, 1f, 1f, 1f,
        -0.5f, -0.5f, 0.7f, 0.7f, 0.7f,
        0.5f, -0.5f, 0.7f, 0.7f, 0.7f,
        0.5f, 0.5f, 0.7f, 0.7f, 0.7f,
        -0.5f, 0.5f, 0.7f, 0.7f, 0.7f,
        -0.5f, -0.5f, 0.7f, 0.7f, 0.7f,

        // Line 1
        -0.5f, 0f, 1f, 0f, 0f,
        0.5f, 0f, 1f, 0f, 0f,

        // Mallets
        0f, -0.25f, 0f, 0f, 1f,
        0f, 0.25f, 1f, 0f, 0f
    )
    private val tableBuffer =
        ByteBuffer.allocateDirect(tableTriangles.size * 4).order(ByteOrder.nativeOrder())
            .asFloatBuffer().apply {
                put(tableTriangles)
                position(0)
            }
    private var mProgram = 0
    private var aPositionLocation = 0
    private var uColorLocation = 0
    override fun onSurfaceCreated(gl: GL10?, config: EGLConfig?) {
        GLES30.glClearColor(0f, 0f, 0f, 0f)
        val vertexId =
            ShaderUtils.compileVertexShader(ResourceUtils.readResource(R.raw.vertex_table))
        val framentId =
            ShaderUtils.compileFragmentShader(ResourceUtils.readResource(R.raw.fragment_air_hockey))
        val programId = ShaderUtils.linkProgram(vertexId, framentId)
        mProgram = programId
        GLES30.glUseProgram(mProgram)
        aPositionLocation = GLES30.glGetAttribLocation(mProgram, "a_Position")
        uColorLocation = GLES30.glGetAttribLocation(mProgram, "u_Color")
        tableBuffer.position(0)
        GLES30.glVertexAttribPointer(
            aPositionLocation,
            2,
            GLES30.GL_FLOAT,
            false,
            5 * 4,
            tableBuffer
        )
        GLES30.glEnableVertexAttribArray(aPositionLocation)
        tableBuffer.position(2)
        GLES30.glVertexAttribPointer(
            uColorLocation,
            3,
            GLES30.GL_FLOAT,
            false,
            5 * 4,
            tableBuffer
        )
        GLES30.glEnableVertexAttribArray(uColorLocation)
    }

    override fun onSurfaceChanged(gl: GL10?, width: Int, height: Int) {
        gl?.glViewport(0, 0, width, height)
    }

    override fun onDrawFrame(gl: GL10?) {
        GLES30.glClear(GLES30.GL_COLOR_BUFFER_BIT)
        GLES30.glDrawArrays(GLES30.GL_TRIANGLES, 0, 6)

        GLES30.glLineWidth(10f)
        GLES30.glDrawArrays(GLES30.GL_LINES, 6, 2)

        GLES30.glDrawArrays(GLES30.GL_POINTS, 8, 1)

        GLES30.glDrawArrays(GLES30.GL_POINTS, 9, 1)
    }
}